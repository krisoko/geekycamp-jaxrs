package geekycamp.jaxrs.init;

import geekycamp.jaxrs.SchoolResource;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

public class JaxRsApplication extends Application{

	private static final Set<Object> emptySet = Collections.emptySet();
	
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> classes = new HashSet<Class<?>>();
		classes.add(SchoolResource.class);
		return classes;
	}
	
	@Override
	public Set<Object> getSingletons() {
		return emptySet;
	}
}
