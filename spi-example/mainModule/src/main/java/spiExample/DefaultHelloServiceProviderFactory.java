package spiExample;

import java.util.Properties;
import java.util.logging.Logger;

public class DefaultHelloServiceProviderFactory implements HelloServiceProviderFactory {
	private static final Logger logger = Logger.getLogger(DefaultHelloServiceProviderFactory.class.getName());
	private static final DefaultHelloServiceProvider singleton = new DefaultHelloServiceProvider();
	
	@Override
	public String getName() {
		return "DefaultHelloServiceProvider";
	}

	@Override
	public void init(Properties config) {
		logger.info("init()");
	}

	@Override
	public DefaultHelloServiceProvider create() {
		logger.info("create()");
		return singleton;
	}

}
