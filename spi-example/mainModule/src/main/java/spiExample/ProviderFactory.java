package spiExample;

import java.util.Properties;

/**
 * Фабрика SPI провайдеров
 * @author dm
 *
 * @param <T>
 */
public interface ProviderFactory<T> {
	
	/** Уникальное имя-идентификатор провайдера */
	String getName();

	/** Вызывается один раз после создания фабрики 
	 * @param config настройки провайдера
	 * */
	void init(Properties config);
	
	/**
	 * Создаёт экземпляр провайдера
	 * @return экземпляр провайдера
	 */
	T create();
}