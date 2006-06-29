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

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

/**
 * Implements an Eclipse editor that hosts the SWT Browser widget, so an HTML
 * page can be displayed in the workbench editor window.
 * 
 * @author Tony Allowatt (Virginia Tech Computer Science)
 */
public class BrowserEditor extends EditorPart
{
	public static final String ID =
		"net.sf.webcat.eclipse.submitter.ui.editors.BrowserEditor";

	/**
	 * The embedded browser widget.
	 */
	private Browser browser;

	private BrowserEditorInput editorInput;

	/**
	 * Called when the editor is initialized.
	 */
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException
	{
		if (!(input instanceof BrowserEditorInput))
			throw new PartInitException(
					"Invalid Input: Must be BrowserEditorInput");

		setSite(site);
		setInput(input);

		editorInput = (BrowserEditorInput) input;

		setPartName(
				editorInput.getProject().getName() + " Submission Results");
	}

	/**
	 * Creates the embedded browser widget.
	 */
	public void createPartControl(Composite parent)
	{
		parent.setLayout(new FillLayout());
		browser = new Browser(parent, SWT.NONE);

		String html = editorInput.getHtml();
		if (html != null)
			browser.setText(html);
	}

	public void doSave(IProgressMonitor monitor)
	{
	}

	public void doSaveAs()
	{
	}

	public boolean isDirty()
	{
		return false;
	}

	public boolean isSaveAsAllowed()
	{
		return false;
	}

	public void setFocus()
	{
		if (browser != null)
			browser.setFocus();
	}
}