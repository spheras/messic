package org.messic.starter;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import javax.swing.JFrame;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

public class Starter {

	public static void main(String[] args) throws BundleException,
			InterruptedException {
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
				try {
					System.out.println("Window Closing!");
					framework.stop();
					framework.waitForStop(30000);
					System.out.println("Framework Stopped!");
				} catch (InterruptedException ie) {
					ie.printStackTrace();
				} catch (BundleException be) {
					be.printStackTrace();
				} finally {
					System.out.println("Exiting Application!");
					System.exit(0);
				}
			}
		});

		frame.setVisible(true);

	}

	private static Framework createFramework() throws BundleException {
		FrameworkFactory frameworkFactory = ServiceLoader.load(FrameworkFactory.class).iterator().next();
		Map<String, String> config = new HashMap<String, String>();
		// TODO: add some config properties
		Framework framework = frameworkFactory.newFramework(config);
		framework.start();
		return framework;
	}

	private static void installBundles(Framework framework)
			throws BundleException {
		BundleContext context = framework.getBundleContext();
		List<Bundle> installedBundles = new LinkedList<Bundle>();

		installedBundles = installFolderBundles(context, "./bundles");

		for (Bundle bundle : installedBundles) {
			bundle.start();
		}

		//org.eclipse.osgi.baseadaptor.bundlefile.ZipBundleEntry, esta es la que busca jetty
		//org.eclipse.osgi.storage.bundlefile.ZipBundleEntry, esta es la que hay
	}

	private static List<Bundle> installFolderBundles(BundleContext context,
			String folder) throws BundleException {
		List<Bundle> installedBundles = new LinkedList<Bundle>();
		String[] bundles = new File(folder).list();
		for (String string : bundles) {
			String path = folder + File.separatorChar + string;
			if (new File(path).isDirectory()) {
				List<Bundle> folderInstalled = installFolderBundles(context,
						path);
				installedBundles.addAll(folderInstalled);
			} else {
				installedBundles.add(context.installBundle("file:" + folder
						+ File.separatorChar + string));
			}
		}

		return installedBundles;
	}

	private static void setJettyConfig() {
		System.setProperty("jetty.port", "8181");
		System.setProperty("jetty.home", "./jetty");
	}

}
