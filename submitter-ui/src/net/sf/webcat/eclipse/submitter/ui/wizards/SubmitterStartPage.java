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
import java.util.ArrayList;

import net.sf.webcat.eclipse.submitter.core.SubmittableEclipseResource;
import net.sf.webcat.eclipse.submitter.core.SubmitterCore;
import net.sf.webcat.eclipse.submitter.ui.SWTUtil;
import net.sf.webcat.eclipse.submitter.ui.SubmitterUIPlugin;
import net.sf.webcat.eclipse.submitter.ui.i18n.Messages;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.webcat.submitter.RequiredItemsMissingException;
import org.webcat.submitter.SubmissionManifest;
import org.webcat.submitter.SubmissionTargetException;
import org.webcat.submitter.Submitter;
import org.webcat.submitter.targets.AssignmentTarget;
import org.webcat.submitter.targets.SubmissionTarget;

/**
 * The main page of the submission wizard contains the assignment tree and other
 * user-input fields.
 * 
 * @author Tony Allevato (Virginia Tech Computer Science)
 */
public class SubmitterStartPage extends WizardPage
{
	// === Methods ============================================================

	// ------------------------------------------------------------------------
	/**
	 * Creates a new instance of the main wizard page.
	 * 
	 * @param submitter
	 *            The ISubmissionEngine to which to submit.
	 * @param project
	 *            The project being submitted.
	 */
	protected SubmitterStartPage(Submitter submitter, IProject project)
	{
		super(Messages.STARTPAGE_PAGE_NAME);

		setTitle(Messages.STARTPAGE_PAGE_TITLE);
		setDescription(Messages.STARTPAGE_PAGE_DESCRIPTION);

		this.submitter = submitter;
		this.project = project;
	}


	// ------------------------------------------------------------------------
	/**
	 * Gets the assignment currently selected in the tree.
	 * 
	 * @return The IDefinitionObject representing the currently selected
	 *         assignment.
	 */
	public AssignmentTarget getSelectedAssignment()
	{
		IStructuredSelection sel = (IStructuredSelection)assignmentTree
		        .getSelection();
		
		if (sel.getFirstElement() instanceof AssignmentTarget)
		{
			return (AssignmentTarget) sel.getFirstElement();
		}
		else
		{
			return null;
		}
	}


	// ------------------------------------------------------------------------
	public void createControl(Composite parent)
	{
		IRunnableContext context = getContainer();

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		composite.setLayout(gl);

		new Label(composite, SWT.NONE).setText(Messages.STARTPAGE_PROJECT);
		Composite projectComp = new Composite(composite, SWT.NONE);
		GridLayout pgl = new GridLayout();
		pgl.numColumns = 2;
		pgl.marginWidth = 0;
		pgl.marginHeight = 0;

		projectComp.setLayout(pgl);
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		projectComp.setLayoutData(gd);

		projectField = new Text(projectComp, SWT.BORDER | SWT.READ_ONLY);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		projectField.setLayoutData(gd);
		if(project != null)
			projectField.setText(project.getName());

		Button projectChoose = new Button(projectComp, SWT.NONE);
		projectChoose.setText(Messages.STARTPAGE_CHOOSE_PROJECT);
		projectChoose.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent e)
			{
				chooseProjectToSubmit();
			}
		});
		gd = new GridData(SWT.CENTER, SWT.CENTER, false, false);
		gd.widthHint = SWTUtil.getButtonWidthHint(projectChoose);
		projectChoose.setLayoutData(gd);

		Label separator = new Label(composite, SWT.HORIZONTAL | SWT.SEPARATOR);
		gd = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd.horizontalSpan = 2;
		gd.heightHint = 12;
		separator.setLayoutData(gd);

		Label submitLabel = new Label(composite, SWT.NONE);
		submitLabel.setText(Messages.STARTPAGE_SUBMIT_AS);
		gd = new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false);
		submitLabel.setLayoutData(gd);

		assignmentTree = new TreeViewer(composite);
		assignmentTree.setContentProvider(
				new SubmissionTargetsContentProvider());
		assignmentTree.setLabelProvider(new SubmissionTargetsLabelProvider());
		assignmentTree.setInput(submitter.getRoot());

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 150;
		assignmentTree.getControl().setLayoutData(gd);
		assignmentTree
		        .addSelectionChangedListener(new ISelectionChangedListener()
		        {
			        public void selectionChanged(SelectionChangedEvent e)
			        {
				        assignmentTreeSelectionChanged();
			        }
		        });

		new Label(composite, SWT.NONE).setText(Messages.STARTPAGE_USERNAME);
		username = new Text(composite, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 144;
		username.setLayoutData(gd);
		username.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				updatePageComplete();
			}
		});

		new Label(composite, SWT.NONE).setText(Messages.STARTPAGE_PASSWORD);
		password = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		gd = new GridData();
		gd.widthHint = 144;
		password.setLayoutData(gd);
		password.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent e)
			{
				updatePageComplete();
			}
		});

		setControl(composite);

		String defUsername = SubmitterCore.getDefault().getOption(
		        SubmitterCore.IDENTIFICATION_DEFAULTUSERNAME);
		username.setText(defUsername);

		expandAllLocalGroups(submitter.getRoot(), context);
		selectLastSelectedAssignmentInTree();		
		initializationComplete = true;
	}


	// ------------------------------------------------------------------------
	private void chooseProjectToSubmit()
	{
		IProject[] allProjects = ResourcesPlugin.getWorkspace().getRoot()
		        .getProjects();
		ArrayList<IProject> filteredProjects = new ArrayList<IProject>();
		for(int i = 0; i < allProjects.length; i++)
			if(allProjects[i].isOpen())
				filteredProjects.add(allProjects[i]);

		ElementListSelectionDialog dialog = new ElementListSelectionDialog(
		        getShell(), new WorkbenchLabelProvider());

		dialog.setElements(filteredProjects.toArray());
		dialog.setMessage(Messages.STARTPAGE_CHOOSE_PROJECT_TITLE);
		dialog.setMatchEmptyString(true);
		dialog.setMultipleSelection(false);

		if(project != null)
			dialog.setInitialSelections(new IProject[] { project });

		int result = dialog.open();
		if(result == Window.OK)
		{
			project = (IProject)dialog.getResult()[0];
			projectField.setText(project.getName());
			
			// Update the wizard's copy of the project as well so that the
			// internal result browser gets initialized correctly.

			((SubmitterWizard) getWizard()).setProject(project);
		}

		updatePageComplete();
	}


	// ------------------------------------------------------------------------
	private void expandAllLocalGroups(SubmissionTarget obj,
			                          IRunnableContext context)
	{
		try
		{
			SubmissionTarget[] children = obj.getLogicalChildren();

			for(int i = 0; i < children.length; i++)
			{
				SubmissionTarget child = children[i];

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


	// ------------------------------------------------------------------------
	private void assignmentTreeSelectionChanged()
	{
		updatePageComplete();
	}


	// ------------------------------------------------------------------------
	public boolean canFlipToNextPage()
	{
		return isPageComplete();
	}


	// ------------------------------------------------------------------------
	private void setErrorMessageIfInitialized(String msg)
	{
		if(initializationComplete)
			setErrorMessage(msg);
	}


	// ------------------------------------------------------------------------
	private void updatePageComplete()
	{
		if(project == null)
		{
			setPageComplete(false);
			setErrorMessageIfInitialized(Messages.STARTPAGE_ERROR_NO_PROJECT);
			return;
		}

		IStructuredSelection sel = (IStructuredSelection)assignmentTree
		        .getSelection();
		if(sel.isEmpty())
		{
			setPageComplete(false);
			setErrorMessageIfInitialized(Messages.STARTPAGE_ERROR_NO_TARGET);
			return;
		}

		SubmissionTarget object = getSelectedAssignment();

		if(object == null || !object.isActionable())
		{
			setPageComplete(false);
			setErrorMessageIfInitialized(Messages.STARTPAGE_ERROR_INVALID_TARGET);
			return;
		}
		else
			setPageComplete(true);

		if(username.getText().trim().length() == 0)
		{
			setPageComplete(false);
			setErrorMessageIfInitialized(Messages.STARTPAGE_ERROR_NO_USERNAME);
			return;
		}

		setErrorMessage(null);
	}


	// ------------------------------------------------------------------------
	public IWizardPage getNextPage()
	{
		SubmitterSummaryPage nextPage = (SubmitterSummaryPage)super
		        .getNextPage();

		if(isPageComplete())
		{
			updateLastSelectedAssignmentPath();

			SubmissionManifest manifeset = new SubmissionManifest();
			manifeset.setAssignment(getSelectedAssignment());
			manifeset.setSubmittableItems(
					new SubmittableEclipseResource(project));
			manifeset.setUsername(username.getText().trim());
			manifeset.setPassword(password.getText());

			try
			{
				submitter.submit(manifeset);
				
				nextPage.setResultCode(SubmitterSummaryPage.RESULT_OK,
				        Messages.STARTPAGE_CLICK_FINISH_TO_EXIT);
			}
			catch(RequiredItemsMissingException e)
			{
				StringBuffer buffer = new StringBuffer();
				buffer.append(Messages.STARTPAGE_ERROR_REQUIRED_FILES_MISSING);

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
				nextPage.setResultCode(SubmitterSummaryPage.RESULT_ERROR,
				        Messages.STARTPAGE_ERROR_BAD_URL, e);
			}
			catch(UnknownHostException e)
			{
				nextPage.setResultCode(SubmitterSummaryPage.RESULT_ERROR,
				        Messages.STARTPAGE_ERROR_COULD_NOT_CONNECT, e);
			}
			catch(Throwable e)
			{
				nextPage.setResultCode(SubmitterSummaryPage.RESULT_ERROR,
				        Messages.STARTPAGE_ERROR_GENERIC, e);
			}
		}

		return nextPage;
	}
	

	// ----------------------------------------------------------
	private void selectLastSelectedAssignmentInTree()
	{
		String path = 
			SubmitterUIPlugin.getDefault().getLastSelectedAssignmentPath();

		Tree tree = assignmentTree.getTree();
		TreeItem item = null;

		if (path != null)
		{
			String[] components = path.split("/\\$#\\$/");
			TreeItem[] children = tree.getItems();

			for (String component : components)
			{
				item = findTreeItemWithText(component, children);
				
				if (item == null)
				{
					return;
				}
				else
				{
					item.setExpanded(true);
					children = item.getItems();
				}
			}

			if (item != null)
			{
				tree.select(item);
			}
		}
	}


	// ----------------------------------------------------------
	private TreeItem findTreeItemWithText(String text, TreeItem[] items)
	{
		for (TreeItem item : items)
		{
			if (item.getText().equals(text))
			{
				return item;
			}
		}
		
		return null;
	}


	// ----------------------------------------------------------
	private void updateLastSelectedAssignmentPath()
	{
		Tree tree = assignmentTree.getTree();
		
		TreeItem[] selItems = tree.getSelection();
		
		if (selItems != null && selItems.length > 0)
		{
			TreeItem item = selItems[0];
			StringBuffer buffer = new StringBuffer();

			buffer.append(item.getText());

			while ((item = item.getParentItem()) != null)
			{
				buffer.insert(0, "/$#$/");
				buffer.insert(0, item.getText());
			}

			SubmitterUIPlugin.getDefault().setLastSelectedAssignmentPath(
				buffer.toString());
		}
		else
		{
			SubmitterUIPlugin.getDefault().setLastSelectedAssignmentPath(null);
		}
	}


	// === Instance Variables =================================================

	/**
	 * The submission engine instance that should be used by this wizard to
	 * submit the project.
	 */
	private Submitter submitter;

	/**
	 * The currently selected project that will be submitted by the wizard.
	 */
	private IProject project;

	/**
	 * A text field that displays the name of the currently selected project.
	 */
	private Text projectField;

	/**
	 * A tree that displays the submission targets that can be selected for
	 * submission.
	 */
	private TreeViewer assignmentTree;

	/**
	 * A text field that contains the username of the person submitting the
	 * project.
	 */
	private Text username;

	/**
	 * A text field that contains the password to be used to authenticate with
	 * the remote submission target.
	 */
	private Text password;

	/**
	 * Set to false while control initialization occurs so that an error message
	 * will not be displayed in the wizard until actual user input occurs, as
	 * per Eclipse user interface guidelines.
	 */
	private boolean initializationComplete = false;
}
