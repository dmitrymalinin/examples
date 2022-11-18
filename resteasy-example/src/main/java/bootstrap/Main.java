package bootstrap;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import jakarta.ws.rs.SeBootstrap;

/**
 * см. 
 * <br/><a href="https://jakarta.ee/specifications/restful-ws/3.1/jakarta-restful-ws-spec-3.1.html#java-se">Jakarta RESTful Web Services. Java SE Bootstrap</a>
 * <br/><a href="https://resteasy.dev/2022/11/01/sebootstrap-usage/">An Introduction to the RESTEasy SeBootstrap Usage</a>
 * @author dm
 *
 */
public class Main {
	public static void main(String[] args) throws Exception {

		System.out.println("bootstrap.Main. pid="+ProcessHandle.current().pid());
		
		final SeBootstrap.Instance instance = startServer();
		
		Runtime.getRuntime().addShutdownHook(new Thread(()->{
			System.out.println("Stop server...");
			CompletableFuture<SeBootstrap.Instance.StopResult> future = instance.stop().toCompletableFuture();
			try {
				future.get();
				System.out.println("Stop server OK");
			} catch (Exception e) {
				System.err.println("Stop server failed: "+e);
			} 			
		}));
		
		Thread.currentThread().join();
	}
	
	private static SeBootstrap.Instance startServer() throws InterruptedException, ExecutionException
	{
		CompletableFuture<SeBootstrap.Instance> future = SeBootstrap.start(TestRSApplication.class)
				.thenApply((instance) ->
				{
					System.out.printf("%s running at %s \n", instance.getClass().getName(), instance.configuration().baseUri());
					instance.stopOnShutdown((stopResult) -> {
						System.out.printf("%s stopped. result: %s\n", instance.getClass().getName(), stopResult.unwrap(Object.class));
					});
					return instance;
				}).toCompletableFuture();
		
		return future.get();
	}
}
