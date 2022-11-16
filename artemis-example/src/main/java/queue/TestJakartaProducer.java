package queue;

import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.MessageProducer;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class TestJakartaProducer {
	private static ConnectionFactory cf = new ActiveMQConnectionFactory();
	private static String QueueName = "ActiveMQTestQueue";

	public static void main(String[] args) throws Exception 
	{
		try (Connection conn = cf.createConnection();)
		{
			System.out.println("Send test message ...");
			Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue(QueueName);
			MessageProducer producer = session.createProducer(queue);
			TextMessage msg = session.createTextMessage("Test Message");
			producer.send(msg);
			System.out.printf("msg: %s\nbody: %s\n", msg, msg.getBody(String.class));			
			System.out.println("Send test message OK");
		}
	}

}
