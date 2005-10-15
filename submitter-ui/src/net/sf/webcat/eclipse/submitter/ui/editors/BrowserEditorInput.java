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
package net.sf.webcat.eclipse.submitter.ui.editors;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

/**
 * An editor input that represents HTML code from an HTTP response, which
 * will be displayed by the browser editor.
 * 
 * @author Tony Allowatt (Virginia Tech Computer Science)
 */
public class BrowserEditorInput implements IEditorInput
{
	private String html;
	private IProject project;

	public BrowserEditorInput(IProject project,
			String html)
	{
		this.project = project;
		this.html = html;
	}

	public boolean exists()
	{
		return false;
	}

	public String getHtml()
	{
		return html;
	}

	public IProject getProject()
	{
		return project;
	}

	public ImageDescriptor getImageDescriptor()
	{
		return null;
	}

	public String getName()
	{
		return project.getName() + " Submission Results";
	}

	public IPersistableElement getPersistable()
	{
		return null;
	}

	public String getToolTipText()
	{
		return getName();
	}

	public Object getAdapter(Class adapter)
	{
		return null;
	}
}
