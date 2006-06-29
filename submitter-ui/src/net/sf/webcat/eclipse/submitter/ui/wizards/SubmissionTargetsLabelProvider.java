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

import net.sf.webcat.eclipse.submitter.core.INameableTarget;
import net.sf.webcat.eclipse.submitter.core.ITarget;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * The label provider for the submission target tree in the wizard.
 * 
 * @author Tony Allowatt (Virginia Tech Computer Science)
 */
public class SubmissionTargetsLabelProvider extends LabelProvider
{
	/**
	 * The image used for assignment groups and imported groups.
	 */
	private Image folderImage;

	/**
	 * The image used for assignment targets.
	 */
	private Image fileImage;

	/**
	 * Creates a new instance of the label provider.
	 */
	public SubmissionTargetsLabelProvider()
	{
		folderImage = ImageDescriptor.createFromURL(
				getClass().getResource("images/folder.gif")).createImage();

		fileImage = ImageDescriptor.createFromURL(
				getClass().getResource("images/file.gif")).createImage();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	public void dispose()
	{
		folderImage.dispose();
		fileImage.dispose();

		super.dispose();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object element)
	{
		ITarget object = (ITarget)element;
		if(object.isContainer())
			return folderImage;
		else
			return fileImage;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object element)
	{
		ITarget object = (ITarget)element;
		
		INameableTarget nameable = (INameableTarget)object
			.getAdapter(INameableTarget.class);

		if(nameable != null && nameable.getName() != null)
			return nameable.getName();
		else
			return super.getText(element);
	}
}