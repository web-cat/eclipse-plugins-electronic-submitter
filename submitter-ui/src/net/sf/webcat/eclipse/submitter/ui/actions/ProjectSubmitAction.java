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
package net.sf.webcat.eclipse.submitter.ui.actions;

import net.sf.webcat.eclipse.submitter.ui.SubmitterUIPlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * The workbench action delegate that invokes the submission wizard.
 * 
 * @author Tony Allowatt (Virginia Tech Computer Science)
 */
public class ProjectSubmitAction implements IWorkbenchWindowActionDelegate
{
	private IWorkbenchWindow window;

	private IProject currentProject;

	/**
	 * Called when the workbench action is invoked.
	 */
	public void run(IAction action)
	{
		if(currentProject != null)
			SubmitterUIPlugin.getDefault().spawnSubmissionUI(window.getShell(),
					currentProject);
	}

	/**
	 * Called when the selection in the workbench has changed.
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

		IWorkbenchPage activePage = window.getActivePage();
		if(activePage != null)
		{
			IEditorPart activeEditor = activePage.getActiveEditor();
			
			if(activeEditor != null)
			{
				IEditorInput editorInput = activeEditor.getEditorInput();
				if(editorInput instanceof IFileEditorInput)
				{
					IFile file = ((IFileEditorInput)editorInput).getFile();
					currentProject = file.getProject();
				}
			}
		}

		if(currentProject == null)
			action.setEnabled(false);
		else
			action.setEnabled(true);
	}

	/**
	 * Called when the delegate is disposed.
	 */
	public void dispose()
	{
	}

	/**
	 * Called when the delegate is initialized.
	 */
	public void init(IWorkbenchWindow window)
	{
		this.window = window;
	}
}
