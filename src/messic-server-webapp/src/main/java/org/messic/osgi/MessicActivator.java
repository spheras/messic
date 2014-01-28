package org.messic.osgi;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class MessicActivator implements BundleActivator {
	
	public static void main(String[] args) {
		boolean value=javax.servlet.Servlet.class.isAssignableFrom(org.springframework.web.servlet.DispatcherServlet.class);
		System.out.println("return:"+value);
	}
	
	public void start(BundleContext context) {
		System.out.println("Hello World");
	}

	public void stop(BundleContext context) {
		System.out.println("Goodbye All");
	}
	
}

