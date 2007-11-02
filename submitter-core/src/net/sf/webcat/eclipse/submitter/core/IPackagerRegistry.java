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
 * The packager registry holds the master list of all registered packagers
 * currently loaded by Eclipse.
 * 
 * This interface is not intended to be implemented by clients.
 * 
 * @author Tony Allevato (Virginia Tech Computer Science)
 */
public interface IPackagerRegistry
{
	// ------------------------------------------------------------------------
	/**
	 * Returns the specified packager, or null if there is no such packager
	 * currently registered.
	 * 
	 * @param id
	 *            The unique identifier of the packager (e.g.
	 *            "net.sf.webcat.eclipse.submitter.packagers.zip")
	 * @return The packager, or null
	 */
	IPackager getPackager(String id);


	// ------------------------------------------------------------------------
	/**
	 * Returns an array containing all of the currently loaded packager
	 * extensions.
	 * 
	 * @return An array of Strings, each element of which is the unique
	 *         identifier of a currently loaded packager extension.
	 */
	String[] getPackagerNames();
}
