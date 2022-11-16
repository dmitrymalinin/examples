package queueRequestor;

public interface QueueAttributes {
	/** Тестовая очередь */
	String QueueName = "ActiveMQTestQueue";
	/** Тип сообщения для завершения цикла обработки сообщений сервиса */
	String ShutdownMessageType = "service-shutdown";
	/** Кол-во потоков обработки сообщений сервиса */
	int NumServiceThreads = 3;
}
