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
 * @author Tony Allowatt (Virginia Tech Computer Science)
 */
public interface ITargetChangedEvent
{
	/**
	 * Returns the provider that fired this event.
	 * 
	 * @return the event provider
	 */
	public ITargetChangeProvider getChangeProvider();

	/**
	 * Returns an array of model objects that are affected by the change.
	 * 
	 * @return array of affected objects
	 */
	public ITarget[] getChangedObjects();

	/**
	 * Returns a name of the object's property that has been changed.
	 * 
	 * @return property that has been changed in the model object,
	 *         or if more than one property has been changed.
	 */
	public String getChangedProperty();

	/**
	 * Used to obtain the old value of the property (before the change).
	 * 
	 * @return the old value of the changed property
	 */
	public Object getOldValue();
	
	/**
	 * Used to obtain the new value of the property (after the change).
	 * 
	 * @return the new value of the changed property.
	 */
	public Object getNewValue();
}
