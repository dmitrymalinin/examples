package yandexTask;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Диспетчер
 * @author dm
 *
 */
public class Dispatcher extends Thread {
	private final Counter c;
	private boolean stop;
		
	public Dispatcher(Counter c) {
		super("Dispatcher Thread");
		this.c = c;
	}
	
	public void setStop() {
		stop = true;
	}
	
	@Override
	public void run() {
		try {
			stop = false;
			lockFile();
			System.out.println("\nDispatcher started");
			DatagramChannel dc = DatagramChannel.open(StandardProtocolFamily.INET6);
			InetAddress mcastaddr = InetAddress.getByName(Constant.SocketGroup);
			InetSocketAddress group = new InetSocketAddress(mcastaddr, Constant.SocketPort);
			ByteBuffer buf = ByteBuffer.allocate(4);
			int cnt = c.getCnt();
			synchronized (this) {
				while (!stop)
				{
					// Разослать текущее значение счётчика
					buf.clear().putInt(cnt).flip();
					int w = dc.send(buf, group);			
					if (w != 4)
						throw new IOException("send() failed: number of bytes sent != 4");
					
					// Вывести значение счётчика зелёным цветом
					System.out.printf(Constant.ANSI_GREEN+"\r%05d"+Constant.ANSI_RESET, cnt);
					
					// Ждать заданный интервал
					wait(Constant.DispatcherInterval * 1000);
					
					// Увеличить значение счётчика
					cnt++;
				}
			}
			
			System.out.println("\nDispatcher finished successfully. Counter="+c.getCnt());
		} catch (IOException | InterruptedException e) {
			System.out.println(Constant.ANSI_RED+"\nDispatcher failed: "+e+Constant.ANSI_RESET);
			e.printStackTrace();
		}
		
	}
	
	private void lockFile() throws IOException
	{
		Path lockPath = Paths.get(Constant.LockFileName);
		System.out.printf("Open lockfile %s ...\n", lockPath);
		FileChannel f = FileChannel.open(lockPath, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
		System.out.println("Wait for lock file...");
		f.lock();
	}
	
}
