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
package net.sf.webcat.eclipse.submitter.ui.wizards;

import net.sf.webcat.eclipse.submitter.core.ISubmissionEngine;
import net.sf.webcat.eclipse.submitter.ui.SubmitterUIPlugin;
import net.sf.webcat.eclipse.submitter.ui.editors.BrowserEditor;
import net.sf.webcat.eclipse.submitter.ui.editors.BrowserEditorInput;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;

/**
 * The main wizard that allows the user to electronically submit a project
 * through Eclipse.
 *
 * @author Tony Allowatt (Virginia Tech Computer Science)
 */
public class SubmitterWizard extends Wizard
{
	/**
	 * The main page of the wizard that contains the assignment list and
	 * other entry field.
	 */
	private SubmitterStartPage startPage;

	/**
	 * The summary page that shows the status of the submission, and any
	 * errors that may have occurred.
	 */
	private SubmitterSummaryPage finalPage;

	/**
	 * A reference to the submission engine that should be used by the wizard.
	 */
	private ISubmissionEngine engine;

	/**
	 * A reference to the project that will be submitted.
	 */
	private IProject project;

	public void addPages()
	{
		// Add the wizard pages to the wizard.

		startPage = new SubmitterStartPage(engine, project);
		finalPage = new SubmitterSummaryPage(engine, project);

		addPage(startPage);
		addPage(finalPage);
	}

	public void init(ISubmissionEngine engine, IProject project)
	{
		// Initialize the wizard.

		this.engine = engine;
		this.project = project;
		this.setWindowTitle("Electronic Submission");

		this.setDefaultPageImageDescriptor(
				SubmitterUIPlugin.getImageDescriptor("banner.png"));
		this.setNeedsProgressMonitor(true);
	}

	public boolean canFinish()
	{
		// We only want the "Finish" button to be enabled if the user is
		// on the final (summary) page of the wizard.

		if(getContainer().getCurrentPage() == finalPage)
			return true;
		else
			return false;
	}

	public boolean performFinish()
	{
		// Now that the submission is complete, if the submission generated a
		// response (e.g., HTTP POST), we should display that to the user in
		// an embedded browser window.

		if(engine.hasResponse())
		{
			try
			{
				BrowserEditorInput input = new BrowserEditorInput(
						project, engine.getSubmissionResponse());

				PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getActivePage().openEditor(
								input, BrowserEditor.ID);
			}
			catch(Exception e)
			{
				System.out.println(e.toString());
			}
		}

		return true;
	}
}