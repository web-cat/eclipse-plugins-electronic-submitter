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
 * The interface that represents an assignment in the submission target tree. An
 * assignment is an entity to which a project can be submitted. It must have a
 * name, but it does not contain other elements.
 * 
 * @author Tony Allevato (Virginia Tech Computer Science)
 */
public interface ITargetAssignment extends ITarget
{
	// ------------------------------------------------------------------------
	/**
	 * Gets the name of the assignment.
	 * 
	 * @return A String containing the name of the assignment.
	 */
	public String getName();


	// ------------------------------------------------------------------------
	/**
	 * Sets the name assignmed to the assignment.
	 * 
	 * @param value
	 *            A String containing the name of the assignment, or null if no
	 *            name should be assigned.
	 */
	void setName(String value);


	// ------------------------------------------------------------------------
	/**
	 * Gets a value indicating whether the assignment should be hidden in the
	 * user-interface.
	 * 
	 * @return true if the assignment should be hidden; otherwise, false.
	 */
	public boolean isHidden();


	// ------------------------------------------------------------------------
	/**
	 * Sets a value indicating whether or not the object should be hidden.
	 * 
	 * @param value
	 *            true if the assignment should be hidden; otherwise, false.
	 */
	void setHidden(boolean value);
}
