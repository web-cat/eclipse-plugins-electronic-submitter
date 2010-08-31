/*
 *	This file is part of Web-CAT Eclipse Plugins.
 *
 *	Web-CAT is free software; you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation; either version 2 of the License, or
 *	(at your option) any later version.
 *
 *	Web-CAT is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with Web-CAT; if not, write to the Free Software
 *	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.webcat.eclipse.submitter.ui;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.webcat.eclipse.submitter.core.RunnableContextLongRunningTaskManager;
import org.webcat.eclipse.submitter.core.SubmitterCore;
import org.webcat.eclipse.submitter.ui.dialogs.SubmissionParserErrorDialog;
import org.webcat.eclipse.submitter.ui.i18n.Messages;
import org.webcat.eclipse.submitter.ui.wizards.SubmitterWizard;
import org.webcat.submitter.Submitter;

/**
 * The main plugin class to be used in the desktop.
 * 
 * @author Tony Allevato (Virginia Tech Computer Science)
 */
public class SubmitterUIPlugin extends AbstractUIPlugin
{
	// === Methods ============================================================

	// ------------------------------------------------------------------------
	/**
	 * The constructor.
	 */
	public SubmitterUIPlugin()
	{
		super();
		plugin = this;
		try
		{
			resourceBundle = ResourceBundle
			        .getBundle("org.webcat.eclipse.submitter.ui.SubmitterUIPluginResources"); //$NON-NLS-1$
		}
		catch(MissingResourceException x)
		{
			resourceBundle = null;
		}
	}


	// ------------------------------------------------------------------------
	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception
	{
		super.start(context);
	}


	// ------------------------------------------------------------------------
	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception
	{
		super.stop(context);
	}


	// ------------------------------------------------------------------------
	/**
	 * Returns the shared instance.
	 */
	public static SubmitterUIPlugin getDefault()
	{
		return plugin;
	}


	// ------------------------------------------------------------------------
	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found.
	 */
	public static String getResourceString(String key)
	{
		ResourceBundle bundle = SubmitterUIPlugin.getDefault()
		        .getResourceBundle();
		try
		{
			return (bundle != null) ? bundle.getString(key) : key;
		}
		catch(MissingResourceException e)
		{
			return key;
		}
	}


	// ------------------------------------------------------------------------
	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle()
	{
		return resourceBundle;
	}


	// ------------------------------------------------------------------------
	/**
	 * Initializes the submission engine and invokes the submission wizard.
	 * 
	 * @param shell
	 *            The shell that will be the parent to the wizard.
	 * @param project
	 *            The project to be submitted.
	 */
	public void spawnSubmissionUI(Shell shell, IProject project)
	{
		URL url;
		Submitter engine = new Submitter();

		try
		{
			url = new URL(SubmitterCore.getDefault().getOption(
			        SubmitterCore.DEFINITIONS_URL));

			ProgressMonitorDialog dlg = new ProgressMonitorDialog(shell);
			
			RunnableContextLongRunningTaskManager taskManager =
				new RunnableContextLongRunningTaskManager(dlg);
			engine.setLongRunningTaskManager(taskManager);
			
			engine.readSubmissionTargets(url);
			
			engine.setLongRunningTaskManager(null);
		}
		catch(MalformedURLException e)
		{
			MessageDialog.openWarning(null, Messages.PLUGINUI_NO_DEF_URL_TITLE,
			        Messages.PLUGINUI_NO_DEF_URL_DESCRIPTION);
			return;
		}
		catch(Throwable e)
		{
			SubmissionParserErrorDialog dlg = new SubmissionParserErrorDialog(
			        shell, e);
			dlg.open();

			return;
		}

		SubmitterWizard wizard = new SubmitterWizard();
		wizard.init(engine, project);

		// Instantiates the wizard container with the wizard and opens it
		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.open();
	}


	// ------------------------------------------------------------------------
	/**
	 * Returns an image descriptor for the specified image in the plug-in's
	 * "icons" directory.
	 * 
	 * @param path
	 *            the path to the icon that should be loaded, relative to the
	 *            "icons" folder in the plug-in
	 * 
	 * @return an ImageDescriptor for the image
	 */
	public static ImageDescriptor getImageDescriptor(String path)
	{
		try
		{
			URL base = Platform.getBundle(PLUGIN_ID).getEntry("/icons/"); //$NON-NLS-1$
			URL url = new URL(base, path);

			return ImageDescriptor.createFromURL(url);
		}
		catch(MalformedURLException e)
		{
		}

		return null;
	}
	
	
	// ------------------------------------------------------------------------
	public String getLastSelectedAssignmentPath()
	{
		return lastSelectedAssignmentPath;
	}


	// ------------------------------------------------------------------------
	public void setLastSelectedAssignmentPath(String path)
	{
		lastSelectedAssignmentPath = path;
	}


	// === Static Variables ===================================================

	/**
	 * The unique identifier of the plug-in.
	 */
	public static final String PLUGIN_ID = "org.webcat.eclipse.submitter.ui"; //$NON-NLS-1$

	/**
	 * The shared instance of the plug-in.
	 */
	private static SubmitterUIPlugin plugin;


	// === Instance Variables =================================================

	/**
	 * The resource bundle of the plug-in.
	 */
	private ResourceBundle resourceBundle;
	
	
	private String lastSelectedAssignmentPath;
}
