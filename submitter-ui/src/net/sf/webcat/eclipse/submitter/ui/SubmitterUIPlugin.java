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
package net.sf.webcat.eclipse.submitter.ui;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import net.sf.webcat.eclipse.submitter.core.ISubmissionEngine;
import net.sf.webcat.eclipse.submitter.core.SubmitterCore;
import net.sf.webcat.eclipse.submitter.ui.dialogs.SubmissionParserErrorDialog;
import net.sf.webcat.eclipse.submitter.ui.wizards.SubmitterWizard;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 * 
 * @author Tony Allowatt (Virginia Tech Computer Science)
 */
public class SubmitterUIPlugin extends AbstractUIPlugin
{
	/**
	 * The unique identifier of the plug-in.
	 */
	public static final String PLUGIN_ID = "net.sf.webcat.eclipse.submitter";

	/**
	 * The shared instance of the plug-in.
	 */
	private static SubmitterUIPlugin plugin;

	/**
	 * The resource bundle of the plug-in.
	 */
	private ResourceBundle resourceBundle;

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
					.getBundle("net.sf.webcat.eclipse.submitter.SubmitterUIPluginResources");
		}
		catch(MissingResourceException x)
		{
			resourceBundle = null;
		}
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception
	{
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception
	{
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 */
	public static SubmitterUIPlugin getDefault()
	{
		return plugin;
	}

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

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle()
	{
		return resourceBundle;
	}

	/**
	 * Initializes the submission engine and invokes the submission wizard.
	 * 
	 * @param shell
	 *          The shell that will be the parent to the wizard.
	 * @param project
	 *          The project to be submitted.
	 */
	public void spawnSubmissionUI(Shell shell, IProject project)
	{
		URL url;
		ISubmissionEngine engine = SubmitterCore.getDefault()
				.createSubmissionEngine();

		try
		{
			url = new URL(SubmitterCore.getDefault().getOption(
					SubmitterCore.DEFINITIONS_URL));

			ProgressMonitorDialog dlg = new ProgressMonitorDialog(shell);

			engine.openDefinitions(url, dlg);
		}
		catch(MalformedURLException e)
		{
			MessageDialog
					.openWarning(
							null,
							"No Assignment Definition URL Specified",
							"There is no assignment definition URL specified in the "
									+ "Eclipse preferences, or the URL is malformed.\n\n"
									+ "Please open the Preferences window (Window menu/Preferences...) "
									+ "and enter the URL provided by your instructor in the "
									+ "\"Electronic Submission\" panel.");
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
		dialog.create();
		dialog.open();
	}
}
