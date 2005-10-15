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
package net.sf.webcat.eclipse.submitter.internal.core;

import net.sf.webcat.eclipse.submitter.core.IPackager;
import net.sf.webcat.eclipse.submitter.core.IPackagerRegistry;
import net.sf.webcat.eclipse.submitter.core.SubmitterCore;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

/**
 * Maintains the list of all available packager extensions loaded into Eclipse.
 * 
 * @author Tony Allowatt (Virginia Tech Computer Science)
 */
public class PackagerRegistry implements IPackagerRegistry
{
	/**
	 * The packager extension point id.
	 */
	private static final String packagersId =
		SubmitterCore.PLUGIN_ID + ".packagers";

	public IPackager getPackager(String id)
	{
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = registry
				.getExtensionPoint(packagersId);

		IConfigurationElement[] elements =
			extensionPoint.getConfigurationElements();

		for(int i = 0; i < elements.length; i++)
		{
			IConfigurationElement element = elements[i];

			if(element.getAttribute("id").equals(id))
			{
				try
				{
					Object packager = element
							.createExecutableExtension("class");

					if(packager instanceof IPackager)
						return (IPackager)packager;
				}
				catch(CoreException e)
				{
					e.printStackTrace();
					return null;
				}
			}
		}

		return null;
	}

	public String[] getPackagerNames()
	{
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = registry
				.getExtensionPoint(packagersId);

		IConfigurationElement[] elements =
			extensionPoint.getConfigurationElements();

		String[] names = new String[elements.length];
		
		for(int i = 0; i < elements.length; i++)
		{
			IConfigurationElement element = elements[i];
			names[i] = element.getAttribute("id");
		}

		return names;
	}
}
