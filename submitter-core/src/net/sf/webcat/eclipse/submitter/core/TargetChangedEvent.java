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
 * A concrete implementation of the ITargetChangedEvent interface.
 * 
 * @author Tony Allevato (Virginia Tech Computer Science)
 */
public class TargetChangedEvent implements ITargetChangedEvent
{
	// === Methods ===========================================================

	// -----------------------------------------------------------------------
	/**
	 * Creates a new instance of the TargetChangedEvent class.
	 * 
	 * @param provider
	 *            the target change provider that fired this event
	 * @param object
	 *            the ITarget object that was changed
	 * @param property
	 *            the property that was changed in the target
	 * @param oldValue
	 *            the old value of the property before it was changed
	 * @param newValue
	 *            the new value of the property after it was changed
	 */
	public TargetChangedEvent(ITargetChangeProvider provider, ITarget object,
	        String property, Object oldValue, Object newValue)
	{
		changeProvider = provider;
		changedObjects = new ITarget[] { object };
		changedProperty = property;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}


	// ------------------------------------------------------------------------
	/**
	 * @see ITargetChangedEvent.getChangeProvider()
	 */
	public ITargetChangeProvider getChangeProvider()
	{
		return changeProvider;
	}


	// ------------------------------------------------------------------------
	/**
	 * @see ITargetChangedEvent.getChangedObjects()
	 */
	public ITarget[] getChangedObjects()
	{
		return changedObjects;
	}


	// ------------------------------------------------------------------------
	/**
	 * @see ITargetChangedEvent.getChangedProperty()
	 */
	public String getChangedProperty()
	{
		return changedProperty;
	}


	// ------------------------------------------------------------------------
	/**
	 * @see ITargetChangedEvent.getOldValue()
	 */
	public Object getOldValue()
	{
		return oldValue;
	}


	// ------------------------------------------------------------------------
	/**
	 * @see ITargetChangedEvent.getNewValue()
	 */
	public Object getNewValue()
	{
		return newValue;
	}

	// === Instance Variables ================================================

	/**
	 * The target change provider that fired this event.
	 */
	private ITargetChangeProvider changeProvider;

	/**
	 * The ITarget objects that were changed. Currently this is always an array
	 * with a single object.
	 */
	private ITarget[] changedObjects;

	/**
	 * The property that was changed in the target.
	 */
	private String changedProperty;

	/**
	 * The old value of the property before it was changed.
	 */
	private Object oldValue;

	/**
	 * The new value of the property after it was changed.
	 */
	private Object newValue;
}
