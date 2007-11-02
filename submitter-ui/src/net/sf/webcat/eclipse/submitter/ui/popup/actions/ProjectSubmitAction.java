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
package net.sf.webcat.eclipse.submitter.ui.popup.actions;

import net.sf.webcat.eclipse.submitter.ui.SubmitterUIPlugin;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * The action that represents the "Submit..." menu item on popups for the
 * IProject resource type.
 * 
 * @author Tony Allevato (Virginia Tech Computer Science)
 */
public class ProjectSubmitAction implements IObjectActionDelegate
{
	// === Methods ============================================================

	// ------------------------------------------------------------------------
	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart)
	{
		part = targetPart;
	}


	// ------------------------------------------------------------------------
	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action)
	{
		SubmitterUIPlugin.getDefault().spawnSubmissionUI(
		        part.getSite().getShell(), currentProject);
	}


	// ------------------------------------------------------------------------
	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection)
	{
		currentProject = null;
		if(selection != null)
		{
			if(selection instanceof IStructuredSelection)
			{
				IStructuredSelection ss = (IStructuredSelection)selection;
				Object obj = ss.getFirstElement();

				if(obj instanceof IAdaptable)
				{
					IAdaptable adapt = (IAdaptable)obj;
					currentProject = (IProject)adapt.getAdapter(IProject.class);
				}
			}
		}
	}


	// === Instance Variables =================================================
	
	/**
	 * The currently active workbench part.
	 */
	private IWorkbenchPart part;

	/**
	 * The project that is currently selected in the workbench (in the
	 * Navigator, Package Explorer, or similar view).
	 */
	private IProject currentProject;
}
