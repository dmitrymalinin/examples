package yandexTask;


public class Main {

	public static void main(String[] args) throws Exception {
		long pid = ProcessHandle.current().pid();
		System.out.println("yandexTask.Main pid="+pid);
		
		Counter c = new Counter(Constant.InitCounterValue);
		Dispatcher d = new Dispatcher(c);		
		d.start();
		Receiver r = new Receiver(c);
		r.start();
	
		Runtime.getRuntime().addShutdownHook(new Thread(()->{
			System.out.println("\nStop dispatcher...");			
			synchronized (d) {
				d.setStop();
				d.notify();
			}
								
			System.out.println("Stop dispatcher OK");
		}));
		Thread.currentThread().join();
	}

}
