package queueRequestor;

import static queueRequestor.QueueAttributes.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageProducer;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;

/**
 * Сервис обработки запросов. Создаёт пул из NumThreads потоков
 * @author dm
 *
 */
public class TestQueueRequestorService {
	static final Logger logger = Logger.getLogger(TestQueueRequestorService.class.getName());
	static final ConnectionFactory cf = new ActiveMQConnectionFactory();
	static final ExecutorService requestExecutor = Executors.newFixedThreadPool(NumServiceThreads);
		
	static ThreadLocal<ExecutorThreadContext> executorThreadContext;
	
	
	// /usr/java/jdk-17.0.1/bin/java -Dfile.encoding=UTF-8 -classpath /home/dm/git/examples/artemis-example/target/classes:/home/dm/.m2/repository/org/apache/activemq/artemis-jakarta-client-all/2.26.0/artemis-jakarta-client-all-2.26.0.jar -XX:+ShowCodeDetailsInExceptionMessages queueRequestor.TestQueueRequestorService
	public static void main(String[] args) throws Exception {
		long pid = ProcessHandle.current().pid();
		logger.info("TestQueueRequestorService. pid="+pid);
		try (Connection conn = cf.createConnection();)
		{
			executorThreadContext = ThreadLocal.withInitial(()->new ExecutorThreadContext(conn));
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue requestQueue = session.createQueue(QueueName);
			MessageConsumer requestConsumer = session.createConsumer(requestQueue);
			
			conn.start();
			while (true)
			{
				logSync(Level.INFO, "Receive requests on "+QueueName+" ...", null);
				Message requestMsg = requestConsumer.receive();
				if (requestMsg != null)
				{
					if (ShutdownMessageType.equals(requestMsg.getJMSType()))
					{
						logSync(Level.INFO, "Shutdown message received", null);
						shutdown();
						break;
					}
					requestExecutor.execute(()->
					{
						try
						{
							long threadId = Thread.currentThread().getId();
							ExecutorThreadContext threadContext = executorThreadContext.get();
							threadContext.printMessage(requestMsg, "requestMsg", 0);
							
							MessageProducer replyProducer = threadContext.replyProducer;
							Session replySession = threadContext.session;							
							Destination replyDestination = requestMsg.getJMSReplyTo();
							
							long startTime = System.nanoTime();
							
							doWork();
							
							TextMessage replyMsg = replySession.createTextMessage("Service "+pid+"  thread "+threadId+" reply to: "+requestMsg.getBody(Object.class));
							replyMsg.setJMSCorrelationID(requestMsg.getJMSCorrelationID());
							replyProducer.send(replyDestination, replyMsg);
							long elapsedTime = System.nanoTime()-startTime;
							threadContext.printMessage(replyMsg, "replyMsg", elapsedTime);
						} catch (Exception e)
						{
							logSync(Level.SEVERE, "Process request failed: ", e);
							throw new RuntimeException(e);
						}
					});
					
				} else
				{
					logSync(Level.SEVERE, "Recieve request failed.", null);
					break;
				}
			}
		}
		logger.info("TestQueueRequestorService finished");
		
	}
	
	static void doWork() throws InterruptedException
	{
		Thread.sleep(1000L+(long)(5000*Math.random()));
	}
	
	static void logSync(Level level, String msg, Throwable thrown)
	{
		synchronized (System.out) 
		{
			logger.log(level, msg, thrown);
		}
	}
	
	static void shutdown() throws InterruptedException
	{
		logSync(Level.INFO, "shutdown...", null);
		requestExecutor.shutdown();
		if (requestExecutor.awaitTermination(10, TimeUnit.SECONDS))
			logger.info("requestExecutor terminated");
		else
			logger.warning("requestExecutor.awaitTermination() timed out");
		logger.info("OK");
	}
}

/**
 * Данные для потока обработки запроса. Отдельный экземпляр для каждого потока.
 * @author dm
 *
 */
class ExecutorThreadContext
{
	static final Logger logger = Logger.getLogger(ExecutorThreadContext.class.getName());
	/** Цвета текста консоли. <br/> 
	 * см. <a href="https://en.wikipedia.org/wiki/ANSI_escape_code">ANSI escape code</a> */
	static final String ANSI_RESET = "\u001B[0m";
	static final String ANSI_BLACK = "\u001B[30m";
	static final String ANSI_RED = "\u001B[31m";
	static final String ANSI_GREEN = "\u001B[32m";
	static final String ANSI_YELLOW = "\u001B[33m";
	static final String ANSI_BLUE = "\u001B[34m";
	static final String ANSI_PURPLE = "\u001B[35m";
	static final String ANSI_CYAN = "\u001B[36m";
	static final String ANSI_WHITE = "\u001B[37m";
	static final String[] ConsoleTextColors = {ANSI_BLACK, ANSI_RED, ANSI_GREEN, ANSI_YELLOW, ANSI_BLUE, ANSI_PURPLE, ANSI_CYAN, ANSI_WHITE};
	static int NextTextColorIdx;
	
	final Session session;
	final MessageProducer replyProducer;
	final String textColor;
	
	public ExecutorThreadContext(Connection conn) {
		synchronized (System.out)
		{
			logger.info("ExecutorThreadContext ctor() thread "+Thread.currentThread().getId());
		}
		try {
			this.session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			this.replyProducer = session.createProducer(null);
			this.textColor = ConsoleTextColors[++NextTextColorIdx];
		} catch (JMSException e) {
			synchronized (System.out)
			{
				logger.log(Level.SEVERE, "ExecutorThreadContext ctor() failed: ", e);
			}
			throw new RuntimeException(e);
		}
	}
	
	void printMessage(Message msg, String header, long elapsedTime) throws JMSException
	{
		long threadId = Thread.currentThread().getId();
		System.out.printf("%s  thread %d: %s: %s\n    correlationID: %s\n    destination: %s\n    body: %s\n    elapsed: %.3f sec.\n"+ExecutorThreadContext.ANSI_RESET,
				textColor, threadId, header,
				msg.getJMSMessageID(), msg.getJMSCorrelationID(), msg.getJMSDestination(), msg.getBody(Object.class),
				elapsedTime/1e9);		
	}
	
	
}
