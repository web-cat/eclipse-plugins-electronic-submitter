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
package net.sf.webcat.eclipse.submitter.core;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import net.sf.webcat.eclipse.submitter.internal.core.PackagerRegistry;
import net.sf.webcat.eclipse.submitter.internal.core.ProtocolRegistry;
import net.sf.webcat.eclipse.submitter.internal.core.SubmissionEngine;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 * 
 * @author Tony Allowatt (Virginia Tech Computer Science)
 */
public class SubmitterCore extends AbstractUIPlugin
{
	/**
	 * The shared instance of the plug-in.
	 */
	private static SubmitterCore plugin;

	/**
	 * The plug-in identifier of the submitter's core support (value
	 * <code>"net.sf.webcat.eclipse.submitter.core"</code>).
	 */
	public static final String PLUGIN_ID = "net.sf.webcat.eclipse.submitter";

	/**
	 * The preference key that stores the URL to the submission targets file.
	 */
	public static final String DEFINITIONS_URL = PLUGIN_ID
			+ ".definitions.URL";

	/**
	 * The preference key that stores the outgoing mail server hostname.
	 */
	public static final String IDENTIFICATION_SMTPSERVER = PLUGIN_ID
			+ ".identification.smtpServer";

	/**
	 * The preference key that stores the default username.
	 */
	public static final String IDENTIFICATION_DEFAULTUSERNAME = PLUGIN_ID
			+ ".identification.defaultUsername";

	/**
	 * The preference key that stores the e-mail address of the user.
	 */
	public static final String IDENTIFICATION_EMAILADDRESS = PLUGIN_ID
			+ ".identification.emailAddress";

	/**
	 * The plug-in's resource bundle.
	 */
	private ResourceBundle resourceBundle;

	/**
	 * An instance of the protocol registry, which manages all of the protocol
	 * extensions loaded by Eclipse.
	 */
	private ProtocolRegistry protocolRegistry;

	/**
	 * An instance of the packager registry, which manages all of the packager
	 * extensions loaded by Eclipse.
	 */
	private PackagerRegistry packagerRegistry;

	/**
	 * The constructor.
	 */
	public SubmitterCore()
	{
		super();

		plugin = this;
		try
		{
			resourceBundle = ResourceBundle
					.getBundle("net.sf.webcat.eclipse.submitter.core.SubmitterCoreResources");
		}
		catch (MissingResourceException x)
		{
			resourceBundle = null;
		}
	}

	/**
	 * This method is called upon plug-in activation.
	 */
	public void start(BundleContext context) throws Exception
	{
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped.
	 */
	public void stop(BundleContext context) throws Exception
	{
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 */
	public static SubmitterCore getDefault()
	{
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found.
	 */
	public static String getResourceString(String key)
	{
		ResourceBundle bundle = SubmitterCore.getDefault().getResourceBundle();
		try
		{
			return (bundle != null) ? bundle.getString(key) : key;
		}
		catch (MissingResourceException e)
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
	 * Gets the value of the specified preference string.
	 * 
	 * @param id The key of the preference value to obtain.
	 * 
	 * @return The value of the requested preference.
	 */
	public String getOption(String id)
	{
		IPreferenceStore store = getPreferenceStore();
		return store.getString(id);
	}

	/**
	 * Returns the instance of the submitter's core submission engine.
	 * 
	 * @return An ISubmissionEngine interface representing the plug-in's
	 *         submission engine.
	 */
	public ISubmissionEngine createSubmissionEngine()
	{
		return new SubmissionEngine();
	}

	/**
	 * Returns the instance of the submitter plug-in's protocol registry.
	 * 
	 * @return An IProtocolManager interface representing the plug-in's protocol
	 *         registry.
	 */
	public IProtocolRegistry getProtocolRegistry()
	{
		if (protocolRegistry == null)
			protocolRegistry = new ProtocolRegistry();

		return protocolRegistry;
	}

	/**
	 * Returns the instance of the submitter plug-in's packager registry.
	 * 
	 * @return An IPackagerManager interface representing the plug-in's packager
	 *         registry.
	 */
	public IPackagerRegistry getPackagerRegistry()
	{
		if (packagerRegistry == null)
			packagerRegistry = new PackagerRegistry();

		return packagerRegistry;
	}
}
