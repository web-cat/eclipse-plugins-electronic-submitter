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
 * The interface that represents an imported group in the submission target
 * tree.  An imported group refers to an external XML submission target file
 * that will be merged with the tree at the location of the import group node.
 * The imported group must have a name and a valid URL to the external file.
 *  
 * @author Tony Allowatt (Virginia Tech Computer Science)
 */
public interface ITargetImportGroup extends ITarget
{
	/**
	 * Gets the name of the imported group.
	 * 
	 * @return A String containing the name of the imported group.
	 */
	public String getName();
	
	/**
	 * Sets the name assignmed to the definition object.
	 * 
	 * @param value A String containing the name of the object, or null if no
	 *              name should be assigned.
	 */
	void setName(String value);

	/**
	 * Gets a String containing the URL to the external submission targets
	 * file that will be imported into this group.
	 * 
	 * @return A String containing the URL to the external submissions
	 *         targets file.
	 */
	public String getHref();

	/**
	 * Sets the URL to the external submission targets file that will be
	 * imported into this group.
	 * 
	 * @param url A String containing the external submissions targets file
	 *            URL.
	 */
	public void setHref(String url);

	/**
	 * Gets a value indicating whether the imported group and its children
	 * should be hidden in the user-interface.
	 * 
	 * @return true if the group should be hidden; otherwise, false.
	 */
	public boolean isHidden();
	
	/**
	 * Sets a value indicating whether or not the object should be hidden.
	 * 
	 * @param value true if the object should be hidden; otherwise, false.
	 */
	void setHidden(boolean value);
}
