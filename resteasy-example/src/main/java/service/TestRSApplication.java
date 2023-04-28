package service;

import java.util.Collections;
import java.util.Set;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/rest")
public class TestRSApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		return Collections.singleton(TestResource.class);
	}
}
