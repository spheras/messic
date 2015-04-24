package org.messic.server.api.musicinfo.duckduckgoimages;

import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.Test;
import org.messic.server.api.musicinfo.service.MusicInfoPlugin;
import org.mockito.Mockito;
import org.osgi.framework.BundleContext;

public class ActivatorTest {

	@Test
	public void testStart() {

		BundleContext bc = Mockito.mock(BundleContext.class);

		Activator a = new Activator();
		a.start(bc);

		Dictionary<String, ?> props=new Hashtable<String, String>(); 
		
		Mockito.verify(bc, Mockito.times(1)).registerService(
				Mockito.eq(MusicInfoPlugin.class.getName()),
				Mockito.any(MusicInfoDuckDuckGoImages.class),
				Mockito.any(props.getClass()));
	}

}
