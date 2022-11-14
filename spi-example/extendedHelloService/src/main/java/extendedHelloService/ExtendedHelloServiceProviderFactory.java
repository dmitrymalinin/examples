package extendedHelloService;

import java.util.Properties;
import java.util.logging.Logger;

import spiExample.HelloServiceProviderFactory;

public class ExtendedHelloServiceProviderFactory implements HelloServiceProviderFactory {
	private static final Logger logger = Logger.getLogger(ExtendedHelloServiceProviderFactory.class.getName());
	private String title;
	
	@Override
	public String getName() {
		return "ExtendedHelloServiceProvider";
	}

	@Override
	public void init(Properties config) {
		logger.info("init() config="+config);
		if (config != null)
			title = config.getProperty("ExtendedHelloServiceProvider.title");
		logger.info("init() title="+title);
	}

	@Override
	public ExtendedHelloServiceProvider create() {
		return new ExtendedHelloServiceProvider(title);
	}

}
