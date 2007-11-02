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
 * An interface implemented by submission arget objects that can be given a
 * name. Users can query for this interface by using the getAdapter() method
 * provided by objects that implement the ITarget interface.
 * 
 * @author Tony Allevato (Virginia Tech Computer Science)
 */
public interface INameableTarget
{
	// ------------------------------------------------------------------------
	/**
	 * Gets the name assigned to the submission target.
	 * 
	 * @return A String containing the name of the target, or null if no name is
	 *         given.
	 */
	String getName();


	// ------------------------------------------------------------------------
	/**
	 * Sets the name assignmed to the submission target.
	 * 
	 * @param value
	 *            A String containing the name of the target, or null if no name
	 *            should be assigned.
	 */
	void setName(String value);
}
