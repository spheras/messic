/*
 * Copyright (C) 2013
 *
 *  This file is part of Messic.
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.messic.server.updater;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.felix.bundlerepository.Repository;
import org.apache.felix.bundlerepository.RepositoryAdmin;
import org.apache.felix.bundlerepository.Resource;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class Activator implements BundleActivator {

	public final static String PRESENTATION_NAME = "messic-updater-bundle";

	private BundleContext mBC;

	/**
	 * Implements BundleActivator.start().
	 * 
	 * @param bundleContext
	 *            - the framework context for the bundle.
	 **/
	public void start(BundleContext bundleContext) {
		try {
			System.out.println("Starting messic updater");
			mBC = bundleContext;

			ServiceReference<RepositoryAdmin> sr = bundleContext
					.getServiceReference(org.apache.felix.bundlerepository.RepositoryAdmin.class);

			if (sr != null) {
				RepositoryAdmin rAdmin = bundleContext.getService(sr);
				Repository repo = getLocalRepository(rAdmin);
				if (repo == null) {
					throw new RuntimeException("Repository path not setted.");
				}

				Resource[] array = rAdmin
						.discoverResources("(symbolicname=*messic*)");

				List<Bundle> installedBundles = new ArrayList<Bundle>();
				for (int i = 0; i < array.length; i++) {
					Bundle bnd = installBundle(repo, array[i]);
					if (bnd != null) {
						installedBundles.add(bnd);
					}
				}

				// ahora hay que lanzar los bundles
				for (Bundle target : installedBundles) {
					target.start();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Bundle installBundle(Repository repo, Resource resource) {
		Bundle aBundle = null;
		// hay que filtrar el propio bundle
		if (!resource.getPresentationName().equals(PRESENTATION_NAME)) {
			try {

				String uriPath = repo.getURI();
				if (uriPath.startsWith("file:")) {
					uriPath = uriPath.substring("file:".length());

					File repoBaseDir = new File(uriPath).getParentFile();
					File bundleFile = new File(repoBaseDir, resource.getURI());
					String bundlePath = bundleFile.toURI().toString();

					aBundle = mBC.getBundle(bundlePath);
					if (aBundle == null) {
						aBundle = mBC.installBundle(bundlePath);
					} else {
						if (aBundle.getState() == Bundle.ACTIVE) {
							aBundle.stop();
						}
						aBundle.update();
					}
				} else {
					System.err
							.println("Bundle URI not supported by messic-updater: "
									+ uriPath);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return aBundle;
	}

	/**
	 * Implements BundleActivator.stop()
	 * 
	 * @param bundleContext
	 *            - the framework context for the bundle.
	 **/
	public void stop(BundleContext bundleContext) {
		mBC = null;
		// NOTE: The service is automatically unregistered.
		System.out.println("Messic updater stoped");
	}

	private Repository getLocalRepository(RepositoryAdmin anAdmin) {
		Repository finded = null;
		String repoPath = System.getProperty("repository.path");

		// es necesario tener la ruta al repositorio
		if (repoPath != null) {
			for (Repository aRepo : anAdmin.listRepositories()) {
				String repoURI = aRepo.getURI();
				if (repoPath.equals(repoURI)) {
					finded = aRepo;
					break;
				}
			}

			if (finded == null) {
				try {
					finded = anAdmin.addRepository(repoPath);
				} catch (Exception e) {
					System.err
							.println("Fallo al instalar el repositorio local: "
									+ repoPath);
					e.printStackTrace();
				}
			}
		}
		return finded;
	}
}