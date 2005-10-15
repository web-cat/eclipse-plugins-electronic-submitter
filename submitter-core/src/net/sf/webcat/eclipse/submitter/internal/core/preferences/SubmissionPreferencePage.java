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
package net.sf.webcat.eclipse.submitter.internal.core.preferences;

import net.sf.webcat.eclipse.submitter.core.SubmitterCore;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * The preference page used to edit settings for the electronic submission
 * plug-in.
 * 
 * @author Tony Allowatt (Virginia Tech Computer Science)
 */
public class SubmissionPreferencePage extends FieldEditorPreferencePage
		implements
			IWorkbenchPreferencePage
{
	/**
	 * Creates a new instance of the preference page.
	 */
	public SubmissionPreferencePage()
	{
		super(FieldEditorPreferencePage.GRID);

		setPreferenceStore(SubmitterCore.getDefault().getPreferenceStore());
		setDescription("Please enter the URL provided by your instructor "
				+ "that contains the assignment definitions to be used by the "
				+ "electronic submission plug-in in the field below.");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common
	 * GUI blocks needed to manipulate various types of preferences. Each field
	 * editor knows how to save and restore itself.
	 */
	public void createFieldEditors()
	{
		addField(new StringFieldEditor(SubmitterCore.DEFINITIONS_URL,
				"&Assignment definition URL:", getFieldEditorParent()));

		addField(new StringFieldEditor(
				SubmitterCore.IDENTIFICATION_DEFAULTUSERNAME,
				"Default &username:", getFieldEditorParent()));

		addField(new StringFieldEditor(SubmitterCore.IDENTIFICATION_SMTPSERVER,
				"&Outgoing (SMTP) mail server:", getFieldEditorParent()));

		addField(new StringFieldEditor(
				SubmitterCore.IDENTIFICATION_EMAILADDRESS, "&E-mail address:",
				getFieldEditorParent()));
	}

 	public void init(IWorkbench workbench)
	{
	}
}