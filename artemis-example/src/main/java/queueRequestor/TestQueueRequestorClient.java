package queueRequestor;

import static queueRequestor.QueueAttributes.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageProducer;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import jakarta.jms.TemporaryQueue;
import jakarta.jms.TextMessage;

/**
 * Помещает запросы в очередь и ждёт ответов на временной очереди
 * @author dm
 *
 */
public class TestQueueRequestorClient {
	private static final Logger logger = Logger.getLogger(TestQueueRequestorClient.class.getName());
	private static final ConnectionFactory cf = new ActiveMQConnectionFactory();
	
	// /usr/java/jdk-17.0.1/bin/java -Dfile.encoding=UTF-8 -classpath /home/dm/git/examples/artemis-example/target/classes:/home/dm/.m2/repository/org/apache/activemq/artemis-jakarta-client-all/2.26.0/artemis-jakarta-client-all-2.26.0.jar -XX:+ShowCodeDetailsInExceptionMessages queueRequestor.TestQueueRequestorClient
	public static void main(String[] args) throws Exception {
		long pid = ProcessHandle.current().pid();
		logger.info("TestQueueRequestorClient. pid="+pid);
		
		boolean stopService = false;
		for (int i = 0; i < args.length; ++i)
		{
			if ("--stop-service".equals(args[i]))
			{
				stopService = true;
				break;
			}
		}
		
		try (Connection conn = cf.createConnection();)
		{
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue requestQueue = session.createQueue(QueueName);
			MessageProducer producer = session.createProducer(requestQueue);
			if (stopService)
			{
				Message msg = session.createTextMessage(ShutdownMessageType);
				msg.setJMSType(ShutdownMessageType);
				producer.send(msg);
				printMessage(msg, ShutdownMessageType);
				return;
			}
			TemporaryQueue replyQueue = session.createTemporaryQueue();
			MessageConsumer replyConsumer = session.createConsumer(replyQueue);
			
			////////////////////////////////////////////////
			Map<String, TextMessage> requestMap = new HashMap<>();
			conn.start();			
			for (int i = 0; ;++i)
			{
				System.out.println("\n * * * * *");	
				long startTime = System.nanoTime();
				String correlationID = ""+pid+"-"+i;
				TextMessage requestMsg = session.createTextMessage("Request message "+correlationID);
				requestMap.put(correlationID, requestMsg);
				requestMsg.setJMSReplyTo(replyQueue);
				requestMsg.setJMSCorrelationID(correlationID);
				producer.send(requestMsg);
				printMessage(requestMsg, "requestMsg");
				//////////////////////////////////////////////////////
				
				System.out.println("Receive reply...");
				Message replyMsg = replyConsumer.receive();
				if (replyMsg != null)
				{
					printMessage(replyMsg, "replyMsg");
					if (!requestMap.remove(replyMsg.getJMSCorrelationID(), requestMsg))
					{
						logger.severe("RequestMessage with correlationID "+replyMsg.getJMSCorrelationID()+" not found");
						break;
					}			
				} else
				{
					logger.severe("Recieve reply failed");
					break;
				}
				long elapsedTime = System.nanoTime() - startTime;
				System.out.printf("OK %.3f sec.\n", elapsedTime/1e9);
				
				Thread.sleep(1000L+(long)(3000*Math.random()));
			}
		}
	}

	static void printMessage(Message msg, String header) throws JMSException
	{
		System.out.printf("%s %s\n    correlationID: %s\n    destination: %s\n    body: %s\n",
				header,
				msg.getJMSMessageID(), msg.getJMSCorrelationID(), msg.getJMSDestination(), msg.getBody(Object.class));		
	}
}
