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
 * This interface allows users to hook listeners into a submission target in
 * order to get notifications when a target changes due to a call to one of
 * its set*() methods.
 * 
 * This interface is meant to be used as part of a visual editor for submission
 * target files.
 * 
 * This interface is not intended to be implemented by clients.
 *
 * @author Tony Allevato (Virginia Tech Computer Science)
 */
public interface ITargetChangeProvider
{
	// ------------------------------------------------------------------------
	/**
	 * Adds the listener to the list of listeners that will be notified on
	 * target changes.
	 * 
	 * @param listener
	 *            a target change listener to be added
	 */
	void addTargetChangedListener(ITargetChangedListener listener);


	// ------------------------------------------------------------------------
	/**
	 * Delivers change event to all the registered listeners.
	 * 
	 * @param event
	 *            a change event that will be passed to all the listeners
	 */
	public void fireTargetChanged(ITargetChangedEvent event);


	// ------------------------------------------------------------------------
	/**
	 * Notifies listeners that a property of a target object changed. This is a
	 * utility method that will create a target event and fire it.
	 * 
	 * @param object
	 *            an affected target object
	 * @param property
	 *            name of the property that has changed
	 * @param oldValue
	 *            the old value of the property
	 * @param newValue
	 *            the new value of the property
	 */
	public void fireTargetObjectChanged(ITarget object, String property,
	        Object oldValue, Object newValue);


	// ------------------------------------------------------------------------
	/**
	 * Takes the listener off the list of registered change listeners.
	 * 
	 * @param listener
	 *            a target change listener to be removed
	 */
	public void removeTargetChangedListener(ITargetChangedListener listener);
}
