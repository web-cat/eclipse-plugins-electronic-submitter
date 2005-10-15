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
public class TargetChangedEvent implements ITargetChangedEvent
{
	private ITargetChangeProvider changeProvider;
	private ITarget[] changedObjects;
	private String changedProperty;
	private Object oldValue;
	private Object newValue;

	public TargetChangedEvent(ITargetChangeProvider provider,
			ITarget object, String property,
			Object oldValue, Object newValue)
	{
		changeProvider = provider;
		changedObjects = new ITarget[] { object };
		changedProperty = property;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public ITargetChangeProvider getChangeProvider()
	{
		return changeProvider;
	}

	public ITarget[] getChangedObjects()
	{
		return changedObjects;
	}

	public String getChangedProperty()
	{
		return changedProperty;
	}

	public Object getOldValue()
	{
		return oldValue;
	}

	public Object getNewValue()
	{
		return newValue;
	}
}
