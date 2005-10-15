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

/**
 * The protocol registry holds the master list of all registered protocols
 * currently loaded by Eclipse.
 * 
 * This interface is not intended to be implemented by clients.
 * 
 * @author Tony Allowatt (Virginia Tech Computer Science)
 */
public interface IProtocolRegistry
{
	/**
	 * Returns the specified protocol, or null if there is no such protocol
	 * currently registered.
	 * 
	 * @param scheme
	 *            The URI scheme of the protocol (e.g. "http", "ftp", etc.)
	 * @return The protocol, or null
	 */
	IProtocol getProtocol(String scheme);

	/**
	 * Returns an array containing all of the currently loaded protocol
	 * extensions.
	 * 
	 * @return An array of Strings, each element of which is the URI scheme of
	 *         a currently loaded protocol extension. 
	 */
	String[] getProtocolNames();
}