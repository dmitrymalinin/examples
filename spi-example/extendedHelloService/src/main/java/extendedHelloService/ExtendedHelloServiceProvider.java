package extendedHelloService;

import java.util.logging.Logger;

import spiExample.HelloServiceProvider;

public class ExtendedHelloServiceProvider implements HelloServiceProvider {
	private static final Logger logger = Logger.getLogger(ExtendedHelloServiceProvider.class.getName());
	private final String title;
				
	public ExtendedHelloServiceProvider(String title) {
		super();
		logger.info("ctor()  title="+title);
		this.title = title;
	}

	@Override
	public String hello() {
		return "Hello, "+title+"!";
	}

}
