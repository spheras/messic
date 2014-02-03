package org.messic.starter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;

import javax.swing.JFrame;

import org.apache.commons.io.FileUtils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

public class Starter {

	public static void main(String[] args) throws BundleException,
			InterruptedException {

		deleteFelixCache();
		setJettyConfig();
		final Framework framework = createFramework();

		installBundles(framework);

		// Runtime.getRuntime().addShutdownHook(new Thread()
		// {
		// @Override
		// public void run()
		// {
		// System.out.println("Shutdown hook ran!");
		// }
		// });

		JFrame frame = new JFrame("messic");
		frame.setSize(200, 200);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				System.out.println("Window Closing!");
				try {

					closingEvent(framework);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					System.out.println("Exiting Application!");
					System.exit(0);
				}
			}
		});

		frame.setVisible(true);

	}

	/**
	 * Closing Osgi Framework Event. This the place to do things when the framework is closing.
	 * @param framework {@link Framework}
	 */
	private static void closingEvent(Framework framework) {
		stopFramework(framework);
		deleteFelixCache();
	}

	/**
	 * Delete the Felix Cache folder
	 */
	private static void deleteFelixCache(){
		File felixCache=new File("./felix-cache");
		if(felixCache.exists() && felixCache.isDirectory()){
			try {
				FileUtils.deleteDirectory(felixCache);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Stop the Osgi framework
	 * @param framework {@link Framework} to stop
	 */
	private static void stopFramework(Framework framework){
		try {
			framework.stop();
			framework.waitForStop(30000);
			System.out.println("Framework Stopped!");
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		} catch (BundleException be) {
			be.printStackTrace();
		}
	}

	/**
	 * Create an osgi framework
	 * @return {@link Framework} osgi framework created
	 * @throws BundleException
	 */
	private static Framework createFramework() throws BundleException {
		FrameworkFactory frameworkFactory = ServiceLoader
				.load(FrameworkFactory.class).iterator().next();
		Framework framework = frameworkFactory.newFramework(getFelixConfig());
		framework.start();
		return framework;
	}

	/**
	 * Install the bundles at the osgi framework.
	 * @param framework {@link Framework} to install bundles
	 * @throws BundleException
	 */
	private static void installBundles(Framework framework)
			throws BundleException {
		BundleContext context = framework.getBundleContext();
		List<Bundle> installedBundles = new LinkedList<Bundle>();

		installedBundles = installFolderBundles(context, "./bundles");

		for (Bundle bundle : installedBundles) {
			if (bundle.getLocation().indexOf("-ns-") <= 0) {
				System.out.println("Starting " + bundle.getLocation());
				bundle.start();
			} else {
				System.out.println("NOT Starting " + bundle.getLocation());
			}
		}
	}

	/**
	 * Install all the bundles found at a certain folder. It install the bundles sorted by the name of the file.
	 * If any subfolder found, then try to install also.
	 * @param context {@link BundleContext}  context to install bundles
	 * @param folder {@link String} folder path to search bundles
	 * @return List<Bundle/> list of installed bundles
	 * @throws BundleException
	 */
	private static List<Bundle> installFolderBundles(BundleContext context,
			String folder) throws BundleException {
		List<Bundle> installedBundles = new LinkedList<Bundle>();
		String[] bundles = new File(folder).list();
		ArrayList<String> albundles = new ArrayList<String>();
		for (String bundle : bundles) {
			albundles.add(bundle);
		}
		Collections.sort(albundles, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return (o1).compareTo(o2);
			}
		});
		for (String string : albundles) {
			String path = folder + File.separatorChar + string;
			if (new File(path).isDirectory()) {
				List<Bundle> folderInstalled = installFolderBundles(context,
						path);
				installedBundles.addAll(folderInstalled);
			} else {
				String sfile = "file:" + folder + File.separatorChar + string;
				System.out.println("INSTALLING " + sfile);
				installedBundles.add(context.installBundle(sfile));
			}
		}

		return installedBundles;
	}

	private static void setJettyConfig() {
		System.setProperty("jetty.port", "8181");
		System.setProperty("jetty.home", "./jetty");
	}
	
	/**
	 * Get the Felix configuration based on the config.properties stored at ./felix/conf folder
	 * @return Map<String,String/> configuration properties
	 */
	private static Map<String, String> getFelixConfig(){
		Map<String, String> config = new HashMap<String, String>();
		//config.put("org.osgi.framework.bootdelegation", "sun.*,com.sun.*");
		Properties p=new Properties();
		try {
			p.load(new FileInputStream(new File("./felix/conf/config.properties")));
			Enumeration<Object> keys=p.keys();
			while(keys.hasMoreElements()){
				String key=""+keys.nextElement();
				String value=p.getProperty(key);
				config.put(key,value);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return config;
	}

}
