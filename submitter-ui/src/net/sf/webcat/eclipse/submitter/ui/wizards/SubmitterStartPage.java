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

import java.net.MalformedURLException;
import java.net.UnknownHostException;

import net.sf.webcat.eclipse.submitter.core.ISubmissionEngine;
import net.sf.webcat.eclipse.submitter.core.ITarget;
import net.sf.webcat.eclipse.submitter.core.RequiredFilesMissingException;
import net.sf.webcat.eclipse.submitter.core.SubmissionParameters;
import net.sf.webcat.eclipse.submitter.core.SubmissionTargetException;
import net.sf.webcat.eclipse.submitter.core.SubmitterCore;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * The main page of the submission wizard contains the assignment tree and
 * other user-input fields.
 * 
 * @author Tony Allowatt (Virginia Tech Computer Science)
 */
public class SubmitterStartPage extends WizardPage
{
	private ISubmissionEngine engine;

	private IProject project;

	private TreeViewer assignmentTree;

	private Text username;

	private Text password;

	/**
	 * Creates a new instance of the main wizard page.
	 * 
	 * @param engine
	 *            The ISubmissionEngine to which to submit.
	 * @param project
	 *            The project being submitted.
	 */
	protected SubmitterStartPage(ISubmissionEngine engine, IProject project)
	{
		super("Submission Start Page");

		setTitle("Electronic Submission");
		setDescription("Please choose the assignment to which you want to submit below, "
				+ "and enter\nthe username and password that you use to connect to "
				+ "the electronic grader.");

		this.engine = engine;
		this.project = project;
	}

	/**
	 * Gets the assignment currently selected in the tree.
	 * 
	 * @return The IDefinitionObject representing the currently selected
	 *         assignment.
	 */
	public ITarget getSelectedAssignment()
	{
		IStructuredSelection sel = (IStructuredSelection)assignmentTree.getSelection();
		return (ITarget)sel.getFirstElement();
	}

	public void createControl(Composite parent)
	{
		IRunnableContext context = getContainer();

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		composite.setLayout(gl);

		new Label(composite, SWT.NONE).setText("Assignment:");
		assignmentTree = new TreeViewer(composite);
		assignmentTree.setContentProvider(new SubmissionTargetsContentProvider(context));
		assignmentTree.setLabelProvider(new SubmissionTargetsLabelProvider());
		assignmentTree.setInput(engine.getRoot());
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 150;
		assignmentTree.getControl().setLayoutData(gd);
		assignmentTree.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent e)
			{
				assignmentTreeSelectionChanged();
			}
		});

		new Label(composite, SWT.NONE).setText("Username:");
		username = new Text(composite, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 144;
		username.setLayoutData(gd);
		username.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e)
			{
				updatePageComplete();
			}
		});

		new Label(composite, SWT.NONE).setText("Password:");
		password = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		gd = new GridData();
		gd.widthHint = 144;
		password.setLayoutData(gd);
		password.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e)
			{
				updatePageComplete();
			}
		});

		setControl(composite);

		String defUsername = SubmitterCore.getDefault().getOption(
				SubmitterCore.IDENTIFICATION_DEFAULTUSERNAME);
		username.setText(defUsername);

		expandAllLocalGroups(engine.getRoot(), context);
	}

	private void expandAllLocalGroups(ITarget obj, IRunnableContext context)
	{
		try
		{
			ITarget[] children = obj.getChildren(context);
	
			for(int i = 0; i < children.length; i++)
			{
				ITarget child = children[i];
	
				if(child.isLoaded())
				{
					if(assignmentTree.isExpandable(child))
					{
						assignmentTree.setExpandedState(child, true);
						expandAllLocalGroups(child, context);
					}
				}
			}
		}
		catch(SubmissionTargetException e)
		{
		}
	}

	private void assignmentTreeSelectionChanged()
	{
		updatePageComplete();
	}

	public boolean canFlipToNextPage()
	{
		return isPageComplete();
	}

	private void updatePageComplete()
	{
		IStructuredSelection sel = (IStructuredSelection)assignmentTree.getSelection();
		if(sel.isEmpty())
		{
			setPageComplete(false);
			return;
		}

		ITarget object = getSelectedAssignment();

		if(object == null || !object.isActionable())
			setPageComplete(false);
		else
			setPageComplete(true);

		if(username.getText().trim().length() == 0)
		{
			setPageComplete(false);
			return;
		}
	}

	public IWizardPage getNextPage()
	{
		SubmitterSummaryPage nextPage = (SubmitterSummaryPage)super
				.getNextPage();

		if(isPageComplete())
		{
			SubmissionParameters params = new SubmissionParameters();
			params.setAssignment(getSelectedAssignment());
			params.setProject(project);
			params.setUsername(username.getText().trim());
			params.setPassword(password.getText());

			try
			{
				engine.submitProject(this.getContainer(), params);

				nextPage
						.setResultCode(SubmitterSummaryPage.RESULT_OK,
								"Please click the \"Finish\" button to exit the wizard.");
			}
			catch(RequiredFilesMissingException e)
			{
				StringBuffer buffer = new StringBuffer();
				buffer
						.append("Your project could not be submitted because it was missing some "
								+ "required files.  The following files could not be found in your "
								+ "project:\n\n");

				for(int i = 0; i < e.getMissingFiles().length; i++)
				{
					buffer.append('\u2022');
					buffer.append(' ');
					buffer.append(e.getMissingFiles()[i]);
					buffer.append('\n');
				}

				nextPage.setResultCode(SubmitterSummaryPage.RESULT_INCOMPLETE,
						buffer.toString());
			}
			catch(MalformedURLException e)
			{
				nextPage
						.setResultCode(
								SubmitterSummaryPage.RESULT_ERROR,
								"The URL to which the submission was to be made is malformed. "
										+ "The likely cause is that there is an error in the assignment "
										+ "definition file, or that you do not have a plug-in installed "
										+ "for the required protocol.  If you are seeing this error, you "
										+ "may wish to notify your instructor.\n\n"
										+ "Details: \n" + e.toString());
			}
			catch(UnknownHostException e)
			{
				nextPage
						.setResultCode(
								SubmitterSummaryPage.RESULT_ERROR,
								"The host to which the submission was to be made could not be "
										+ "reached.  Your network connection may be at fault, or there "
										+ "may be an error in the assignment definition file.\n\n"
										+ "If you believe your network connection is not at fault, "
										+ "you may wish to notify your instructor.\n\n"
										+ "Details: \n" + e.toString());
			}
			catch(InterruptedException e)
			{
				nextPage
						.setResultCode(
								SubmitterSummaryPage.RESULT_CANCELED,
								"The submission was canceled.\n\n"
										+ "However, if this submission was being made to a remote system, "
										+ "that system may have already recorded a partial submission.");
			}
			catch(Throwable e)
			{
				nextPage.setResultCode(SubmitterSummaryPage.RESULT_ERROR,
						"The following generic error occurred while submitting:\n\n"
								+ e.toString());
			}
		}

		return nextPage;
	}
}