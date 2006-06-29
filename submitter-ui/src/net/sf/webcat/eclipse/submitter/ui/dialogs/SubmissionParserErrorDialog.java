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
package net.sf.webcat.eclipse.submitter.ui.dialogs;

import net.sf.webcat.eclipse.submitter.core.SubmissionTargetException;
import net.sf.webcat.eclipse.submitter.core.TargetParseError;
import net.sf.webcat.eclipse.submitter.core.TargetParseException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Displays to the user any errors that occurred during the parsing of the
 * submission definitions file.
 * 
 * @author Tony Allowatt (Virginia Tech Computer Science)
 */
public class SubmissionParserErrorDialog extends Dialog
{
	/**
	 * The label that displays the description of the error.
	 */
	private Label summaryLabel;

	/**
	 * The text field that displays the error list or stack trace.
	 */
	private Text errorField;

	/**
	 * The string containing the description of the error.
	 */
	private String summaryString;

	/**
	 * The string containing the error list or stack trace.
	 */
	private String errorString;

	/**
	 * Create a new instance of the error dialog with the specified parent
	 * shell and getting its information from the given exception.
	 * 
	 * @param shell The shell that will parent this dialog.
	 * @param exception The exception described by the dialog.
	 */
	public SubmissionParserErrorDialog(Shell shell, Throwable exception)
	{
		super(shell);
		setShellStyle(getShellStyle() | SWT.RESIZE);

		if(exception instanceof TargetParseException)
			setFromParseErrors(((TargetParseException)exception).getErrors());
		else
			setFromException(exception);
	}

	/**
	 * Creates the dialog controls.
	 */
	protected Control createDialogArea(Composite parent)
	{
		Composite composite = (Composite)super.createDialogArea(parent);

		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		composite.setLayout(gl);

		Composite headerComp = new Composite(composite, SWT.NONE);
		gl = new GridLayout();
		gl.numColumns = 2;
		headerComp.setLayout(gl);

		Label imageLabel = new Label(headerComp, SWT.NONE);
		imageLabel.setSize(32, 32);
		imageLabel.setImage(Display.getCurrent().getSystemImage(SWT.ICON_WARNING));
		GridData gd = new GridData();
		gd.widthHint = 32;
		gd.heightHint = 32;
		gd.verticalAlignment = GridData.BEGINNING;
		imageLabel.setLayoutData(gd);

		summaryLabel = new Label(headerComp, SWT.WRAP);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 368;
		summaryLabel.setLayoutData(gd);

		errorField = new Text(composite, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI
				| SWT.H_SCROLL | SWT.V_SCROLL);
		gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 400;
		gd.heightHint = 150;
		errorField.setLayoutData(gd);

		if(errorString != null)
			errorField.setText(errorString);

		if(summaryString != null)
			summaryLabel.setText(summaryString);

		getShell().setText("Error Accessing Submission Definitions");

		return composite;
	}

	/**
	 * Creates the main buttons for the dialog.
	 */
	protected void createButtonsForButtonBar(Composite parent)
	{
		// Create only an OK button.
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
	}

	private void setFromParseErrors(TargetParseError[] errors)
	{
		StringBuffer buffer = new StringBuffer();

		for(int i = 0; i < errors.length; i++)
		{
			buffer.append(errors[i].toString());
			buffer.append('\n');
		}

		errorString = buffer.toString();

		summaryString =
			"The submission definitions file could not be parsed, and "
				+ "the following errors were generated.  You may wish to send "
				+ "this information to your instructor.";
	}

	private void setFromException(Throwable e)
	{
		if(e instanceof SubmissionTargetException)
			e = ((SubmissionTargetException)e).getInnerException();

		StringBuffer buffer = new StringBuffer();
		buffer.append(e.toString());
		buffer.append("\n\n");

		StackTraceElement[] trace = e.getStackTrace();
		for(int i = 0; i < trace.length; i++)
		{
			buffer.append(trace[i].toString());
			buffer.append('\n');
		}

		errorString = buffer.toString();

		summaryString =
			"The submission definitions file could not be parsed, and "
				+ "the following error was generated.  You may wish to send "
				+ "this information to your instructor.";
	}
}