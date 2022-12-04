package yandexTask;

/**
 * Константы
 * @author dm
 *
 */
public interface Constant {
	/** Начальное значение счётчика */
	int InitCounterValue = 0;
	/** Интервал увеличения счетчика в секундах */
	int DispatcherInterval = 1;
	/** Порт IP сокета */
	int SocketPort = 6789;
	/** Групповой адрес для многоадресной рассылки */ //https://en.wikipedia.org/wiki/Multicast_address
	String SocketGroup = "239.8.7.6";
	/** Имя сетевого интерфейса для многоадресной рассылки */
	String NetworkInterfaceName = "enp0s25";
	/** Имя файла для синхронизации запуска диспетчеров */
	String LockFileName = "/home/dm/git/examples/yandex-task/target/YandexTaskLock";
	
	/** Цвета текста консоли. <br/> 
	 * см. <a href="https://en.wikipedia.org/wiki/ANSI_escape_code">ANSI escape code</a> */
	String ANSI_RESET = "\u001B[0m";
	String ANSI_BLACK = "\u001B[30m";
	String ANSI_RED = "\u001B[31m";
	String ANSI_GREEN = "\u001B[32m";
	String ANSI_YELLOW = "\u001B[33m";
	String ANSI_BLUE = "\u001B[34m";
	String ANSI_PURPLE = "\u001B[35m";
	String ANSI_CYAN = "\u001B[36m";
	String ANSI_WHITE = "\u001B[37m";
}
