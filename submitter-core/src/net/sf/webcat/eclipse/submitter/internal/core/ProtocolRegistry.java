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

import net.sf.webcat.eclipse.submitter.core.IProtocol;
import net.sf.webcat.eclipse.submitter.core.IProtocolRegistry;
import net.sf.webcat.eclipse.submitter.core.SubmitterCore;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

/**
 * Maintains the list of all available protocol extensions loaded into Eclipse.
 * 
 * @author Tony Allowatt (Virginia Tech Computer Science)
 */
public class ProtocolRegistry implements IProtocolRegistry
{
	/**
	 * The protocols extension point id.
	 */
	private static final String protocolsId =
		SubmitterCore.PLUGIN_ID + ".protocols";

	public IProtocol getProtocol(String scheme)
	{
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint =
			registry.getExtensionPoint(protocolsId);
		
		IConfigurationElement[] elements =
			extensionPoint.getConfigurationElements();
		
		for(int i = 0; i < elements.length; i++)
		{
			IConfigurationElement element = elements[i];
			
			if(element.getAttribute("scheme").equals(scheme))
			{
				try
				{
					Object protocol =
						element.createExecutableExtension("class");
					
					if(protocol instanceof IProtocol)
						return (IProtocol)protocol;
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

	public String[] getProtocolNames()
	{
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = registry
				.getExtensionPoint(protocolsId);

		IConfigurationElement[] elements =
			extensionPoint.getConfigurationElements();

		String[] names = new String[elements.length];
		
		for(int i = 0; i < elements.length; i++)
		{
			IConfigurationElement element = elements[i];
			names[i] = element.getAttribute("scheme");
		}

		return names;
	}
}
