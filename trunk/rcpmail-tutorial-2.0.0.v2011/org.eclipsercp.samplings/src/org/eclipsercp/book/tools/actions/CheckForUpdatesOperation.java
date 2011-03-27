package org.eclipsercp.book.tools.actions;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.PluginVersionIdentifier;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.update.configurator.ConfiguratorUtils;
import org.eclipse.update.configurator.IPlatformConfiguration;
import org.eclipse.update.core.Feature;
import org.eclipse.update.core.IFeature;
import org.eclipse.update.internal.configurator.UpdateURLDecoder;
import org.eclipse.update.internal.search.SiteSearchCategory;
import org.eclipse.update.search.IUpdateSearchResultCollector;
import org.eclipse.update.search.UpdateSearchRequest;
import org.eclipse.update.search.UpdateSearchScope;
import org.eclipse.update.standalone.InstallCommand;
import org.eclipsercp.book.tools.Utils;

public class CheckForUpdatesOperation {
	
	private Shell shell = null;

	public CheckForUpdatesOperation(Shell shell) {
		super();
		this.shell = shell;
	}

	/**
	 * Returns <code>true</code> if new sample code was found an installed and 
	 * <code>false</code> otherwise.
	 * 
	 * @param fromSite
	 * @param fromFeatureId
	 * 
	 * @return Returns <code>true</code> if new sample code was found an installed and 
	 * <code>false</code> otherwise.
	 * 
	 * @throws InvocationTargetException
	 * @throws InterruptedException
	 */
	public boolean run(final String fromSite, final String fromFeatureId)
			throws InvocationTargetException, InterruptedException {
		final boolean[] retVal = new boolean[] {true};
		try {
			URL remoteSiteURL = new URL(UpdateURLDecoder.decode(fromSite,
					"UTF-8")); //$NON-NLS-1$
			UpdateSearchScope searchScope = new UpdateSearchScope();
			searchScope.addSearchSite("remoteSite", //$NON-NLS-1$
					remoteSiteURL, new String[0]);
			final UpdateSearchRequest searchRequest = new UpdateSearchRequest(
					new SiteSearchCategory(), searchScope);
			final List newFeatures = new ArrayList();
			final IUpdateSearchResultCollector collector = new IUpdateSearchResultCollector() {
				public void accept(IFeature match) {
					if (match instanceof Feature)
						newFeatures.add(match);
				}
			};
			final List featuresNeedingToBeInstalled = new ArrayList();
			ProgressMonitorDialog pmd = new ProgressMonitorDialog(shell);
			pmd.run(true, true, new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {
					try {
						searchRequest.performSearch(collector, monitor);
					} catch (CoreException e) {
						throw new InvocationTargetException(e);
					}
					for (Iterator it = newFeatures.iterator(); it.hasNext();) {
						Feature feature = (Feature) it.next();
						String featureIdentifier = feature
								.getFeatureIdentifier();
						if (featureIdentifier
								.equals(fromFeatureId)
								&& checkFeature(featureIdentifier, feature
										.getFeatureVersion()))
							featuresNeedingToBeInstalled.add(feature);
					}
				}
			});

			if (featuresNeedingToBeInstalled.isEmpty()) {
				MessageDialog
						.openInformation(shell, "Up-to-date",
								"You currently have all the latest plug-ins for the RCP book.");
				retVal[0] = false;
			} else {
				if (MessageDialog
						.openQuestion(shell, "Found Updates",
								"There are updates available. Do you want to download and install them now?")) {
					pmd = new ProgressMonitorDialog(shell);
					pmd.run(true, true, new IRunnableWithProgress() {
						public void run(IProgressMonitor monitor) {
							for (Iterator it = featuresNeedingToBeInstalled
									.iterator(); it.hasNext();) {
								Feature element = (Feature) it.next();
								try {
									downloadFeature(element
											.getFeatureIdentifier(), element
											.getFeatureVersion(),
											fromSite,
											monitor);
								} catch (Exception e) {
									MessageDialog.openError(shell,
											"Error",
											"Error downloading new plug-ins. "
													+ e.getMessage());
									retVal[0] = false;
								}
							}
						}
					});
				}
			}
		} catch (MalformedURLException e) {
			Utils.handleError(shell, e, "Error", "Checking for updates");
		} catch (UnsupportedEncodingException e) {
			Utils.handleError(shell, e, "Error", "Checking for updates");
		}
		return retVal[0];
	}
	
	private void downloadFeature(String feature, String version,
			String fromSite, IProgressMonitor monitor) throws Exception {
		InstallCommand command = new InstallCommand(feature, version, fromSite,
				null, "false");
		command.run(monitor);
		command.applyChangesNow();
	}

	private boolean checkFeature(String feature, String version) {
		IPlatformConfiguration config = ConfiguratorUtils
				.getCurrentPlatformConfiguration();
		IPlatformConfiguration.IFeatureEntry[] features = config
				.getConfiguredFeatureEntries();
		PluginVersionIdentifier targetVersion = new PluginVersionIdentifier(
				version);
		// check installed features
		for (int i = 0; i < features.length; i++) {
			String id = features[i].getFeatureIdentifier();
			if (feature.equals(id)) {
				PluginVersionIdentifier v = new PluginVersionIdentifier(
						features[i].getFeatureVersion());
				if (targetVersion.isGreaterThan(v))
					return true;
				else
					return false;
			}
		}
		// if the feature isn't installed consider it has needing to be
		// installed.
		return true;
	}
}
