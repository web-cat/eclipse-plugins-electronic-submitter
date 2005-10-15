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

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * The summary page shows the status of the submission, as well as any errors
 * that may have occurred.
 * 
 * @author Tony Allowatt (Virginia Tech Computer Science)
 */
public class SubmitterSummaryPage extends WizardPage
{
	/**
	 * The submission succeeded.
	 */
	public static final int RESULT_OK = 0;

	/**
	 * The submission was canceled by the user.
	 */
	public static final int RESULT_CANCELED = 1;

	/**
	 * The submission was incomplete (i.e., not all required files were found).
	 */
	public static final int RESULT_INCOMPLETE = 2;

	/**
	 * There was some other error during submission.
	 */
	public static final int RESULT_ERROR = 3;

	private Label imageLabel;

	private Label summaryLabel;

	private Text descriptionField;

	private Image infoImage;

	private Image warningImage;

	private Image errorImage;

	private Font boldFont;

	/**
	 * Creates a new instance of the wizard summary page.
	 * 
	 * @param engine
	 *            The ISubmissionEngine to which to submit.
	 * @param project
	 *            The project being submitted.
	 */
	protected SubmitterSummaryPage(ISubmissionEngine engine, IProject project)
	{
		super("Submission Result Page");

		setTitle("Electronic Submission");
		setDescription("The status of your submission is displayed below.");
	}

	public void createControl(Composite parent)
	{
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		composite.setLayout(gl);

		infoImage = Display.getCurrent().getSystemImage(SWT.ICON_INFORMATION);
		warningImage = Display.getCurrent().getSystemImage(SWT.ICON_WARNING);
		errorImage = Display.getCurrent().getSystemImage(SWT.ICON_ERROR);

		FontData fd = parent.getFont().getFontData()[0];
		fd.setStyle(fd.getStyle() | SWT.BOLD);
		boldFont = new Font(parent.getDisplay(), fd);

		imageLabel = new Label(composite, SWT.NONE);
		imageLabel.setSize(32, 32);
		GridData gd = new GridData();
		gd.widthHint = 32;
		gd.heightHint = 32;
		gd.verticalAlignment = GridData.BEGINNING;
		imageLabel.setLayoutData(gd);

		Composite subComposite = new Composite(composite, SWT.NONE);
		gl = new GridLayout();
		gl.numColumns = 1;
		subComposite.setLayout(gl);
		gd = new GridData(GridData.FILL_BOTH);
		subComposite.setLayoutData(gd);

		summaryLabel = new Label(subComposite, SWT.NONE);
		summaryLabel.setFont(boldFont);
		summaryLabel.setText("");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		summaryLabel.setLayoutData(gd);

		descriptionField = new Text(subComposite, SWT.READ_ONLY | SWT.MULTI
				| SWT.WRAP);
		gd = new GridData(GridData.FILL_BOTH);
		descriptionField.setLayoutData(gd);

		setControl(composite);
	}

	public void dispose()
	{
		if(infoImage != null)
			infoImage.dispose();
		if(warningImage != null)
			warningImage.dispose();
		if(errorImage != null)
			errorImage.dispose();
		if(boldFont != null)
			boldFont.dispose();
	}

	public boolean canFlipToNextPage()
	{
		return false;
	}

	/**
	 * Sets the result code/error status that will be displayed in the summary.
	 * 
	 * @param result
	 *            One of the RESULT_* values indicating the status of the
	 *            submission.
	 * @param description
	 *            A String containing a description of the error.
	 */
	public void setResultCode(int result, String description)
	{
		switch(result)
		{
		case RESULT_OK:
			imageLabel.setImage(infoImage);
			summaryLabel.setText("Your submission was successful.");
			descriptionField.setText(description);
			break;

		case RESULT_INCOMPLETE:
			imageLabel.setImage(warningImage);
			summaryLabel.setText("Your submission was incomplete.");
			descriptionField.setText(description);
			break;

		case RESULT_CANCELED:
			imageLabel.setImage(warningImage);
			summaryLabel.setText("Your submission was canceled.");
			descriptionField.setText(description);
			break;

		case RESULT_ERROR:
			imageLabel.setImage(errorImage);
			summaryLabel
					.setText("Your submission failed.  The following error occurred:");
			descriptionField.setText(description);
			break;
		}
	}
}