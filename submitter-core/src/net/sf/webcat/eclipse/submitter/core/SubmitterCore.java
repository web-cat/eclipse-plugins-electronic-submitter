/*==========================================================================*\
 |  $Id$
 |*-------------------------------------------------------------------------*|
 |  Copyright (C) 2006-2009 Virginia Tech
 |
 |  This file is part of Web-CAT Eclipse Plugins.
 |
 |  Web-CAT is free software; you can redistribute it and/or modify
 |  it under the terms of the GNU General Public License as published by
 |  the Free Software Foundation; either version 2 of the License, or
 |  (at your option) any later version.
 |
 |  Web-CAT is distributed in the hope that it will be useful,
 |  but WITHOUT ANY WARRANTY; without even the implied warranty of
 |  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 |  GNU General Public License for more details.
 |
 |  You should have received a copy of the GNU General Public License along
 |  with Web-CAT; if not, see <http://www.gnu.org/licenses/>.
\*==========================================================================*/

package net.sf.webcat.eclipse.submitter.core;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;

//--------------------------------------------------------------------------
/**
 * The main class for the Eclipse electronic submitter plug-in.
 *
 * @author  Tony Allevato (Virginia Tech Computer Science)
 * @author  latest changes by: $Author$
 * @version $Revision$ $Date$
 */
public class SubmitterCore extends AbstractUIPlugin
{
	//~ Constructors ..........................................................

	// ----------------------------------------------------------
	/**
	 * Creates a new instance of the SubmitterCore class.
	 */
	public SubmitterCore()
	{
		plugin = this;

		try
		{
			resourceBundle = ResourceBundle.getBundle(
					"net.sf.webcat.eclipse.submitter.core."
					+ "SubmitterCoreResources");
		}
		catch (MissingResourceException e)
		{
			resourceBundle = null;
		}
	}


	// ----------------------------------------------------------
	/**
	 * Gets the shared instance of the plug-in.
	 * 
	 * @return the shared instance of the plug-in
	 */
	public static SubmitterCore getDefault()
	{
		return plugin;
	}


	// ----------------------------------------------------------
	/**
	 * Returns the string from the plugin's resource bundle, or 'key' if not
	 * found.
	 * 
	 * @param key the key to look up in the resource bundle
	 * @return the value of the string
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


	// ----------------------------------------------------------
	/**
	 * Gets the plugin's resource bundle.
	 * 
	 * @return the plugin's resource bundle
	 */
	public ResourceBundle getResourceBundle()
	{
		return resourceBundle;
	}


	// ----------------------------------------------------------
	/**
	 * Gets the value of the specified preference string.
	 * 
	 * @param id the key of the preference value to obtain
	 * @return the value of the requested preference
	 */
	public String getOption(String id)
	{
		IPreferenceStore store = getPreferenceStore();
		return store.getString(id);
	}


	//~ Static/instance variables .............................................

	/* The shared instance of the plug-in. */
	private static SubmitterCore plugin;

	/* The plug-in identifier of the submitter's core support (value
	   {@code "net.sf.webcat.eclipse.submitter"}). */
	public static final String PLUGIN_ID = "net.sf.webcat.eclipse.submitter";

	/* The preference key that stores the URL to the submission targets
	   file. */
	public static final String DEFINITIONS_URL = PLUGIN_ID + ".definitions.URL";

	/* The preference key that stores the outgoing mail server hostname. */
	public static final String IDENTIFICATION_SMTPSERVER = PLUGIN_ID
	        + ".identification.smtpServer";

	/* The preference key that stores the default username. */
	public static final String IDENTIFICATION_DEFAULTUSERNAME = PLUGIN_ID
	        + ".identification.defaultUsername";

	/* The preference key that stores the e-mail address of the user. */
	public static final String IDENTIFICATION_EMAILADDRESS = PLUGIN_ID
	        + ".identification.emailAddress";

	/* The plug-in's resource bundle. */
	private ResourceBundle resourceBundle;
}
