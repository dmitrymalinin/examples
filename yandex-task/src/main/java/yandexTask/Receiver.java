package yandexTask;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.MembershipKey;

/**
 * Приёмник
 * @author dm
 *
 */
public class Receiver extends Thread {
	private final Counter c;
		
	public Receiver(Counter c) {
		super();
		this.c = c;
	}
	
	@Override
	public void run() {
		try {
			System.out.println("Receiver started");
			DatagramChannel dc = openDatagramChannel();
			
			ByteBuffer buf = ByteBuffer.allocate(4);
			while (true)
			{
				buf.clear();
				dc.receive(buf);
				buf.flip();
				c.setCnt(buf.getInt());
				// Вывести значение счётчика жёлтым цветом
				System.out.printf(Constant.ANSI_YELLOW+"\r%05d"+Constant.ANSI_RESET, c.getCnt());
			}
		} catch (IOException e) {
			System.out.println(Constant.ANSI_RED+"\nReceiver failed: "+e+Constant.ANSI_RESET);
			e.printStackTrace();
		}
	}
	
	private DatagramChannel openDatagramChannel() throws IOException
	{
		System.out.printf("\nOpen datagram channel ...\n");
		NetworkInterface ni = NetworkInterface.getByName(Constant.NetworkInterfaceName);
		DatagramChannel dc = DatagramChannel.open(StandardProtocolFamily.INET6)
					.setOption(StandardSocketOptions.SO_REUSEADDR, Boolean.TRUE)
					.bind(new InetSocketAddress(Constant.SocketPort))
					.setOption(StandardSocketOptions.IP_MULTICAST_IF, ni);
		InetAddress group = InetAddress.getByName(Constant.SocketGroup);
		MembershipKey key = dc.join(group, ni);
		if (!key.isValid())
		{
			throw new IOException("MembershipKey is not valid");
		}

		System.out.printf("\nOpen datagram channel OK.  Bound to %s \n", dc.getLocalAddress());
		return dc;
	}
}
