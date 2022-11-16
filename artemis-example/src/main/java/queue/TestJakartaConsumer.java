package queue;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Message;
import jakarta.jms.MessageConsumer;
import jakarta.jms.Queue;
import jakarta.jms.Session;

public class TestJakartaConsumer {
	private static ConnectionFactory cf = new ActiveMQConnectionFactory();
	private static String QueueName = "ActiveMQTestQueue";
	
	public static void main(String[] args) throws Exception 
	{
		try (Connection conn = cf.createConnection();)
		{
			System.out.println("Receive test message ...");
			
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue(QueueName);
			
			conn.start();
			MessageConsumer consumer = session.createConsumer(queue);
			Message msg = consumer.receive(5000);
			if (msg != null)
				System.out.printf("msg: %s\nbody: %s\n", msg, msg.getBody(String.class));
			else
				System.out.println("Recieve message failed: timeout expires or this message consumer is concurrently closed");
			
			System.out.println("Receive test message OK");
		}
	}
}
