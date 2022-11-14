package spiExample;

public class DefaultHelloServiceProvider implements HelloServiceProvider {

	@Override
	public String hello() {
		return "Hello!";
	}

}
