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
package net.sf.webcat.eclipse.submitter.internal.packagers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import net.sf.webcat.eclipse.submitter.core.IPackager;
import net.sf.webcat.eclipse.submitter.core.ITarget;
import net.sf.webcat.eclipse.submitter.core.SubmissionParameters;
import net.sf.webcat.eclipse.submitter.core.SubmissionTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.operation.IRunnableContext;

/**
 * A submission packager that writes the project files to a ZIP archive.
 * 
 * @author Tony Allevato (Virginia Tech Computer Science)
 */
public class ZipPackager implements IPackager
{
	// === Methods ============================================================

	// ------------------------------------------------------------------------
	public void pack(IRunnableContext context, SubmissionParameters params,
	        OutputStream stream) throws CoreException, IOException
	{
		try
		{
			this.asmt = params.getAssignment();
			this.project = params.getProject();

			zip = new ZipOutputStream(stream);

			IResource[] members = project.members();
			recurseProjectContents(context, members);

			zip.finish();
		}
		catch(SubmissionTargetException e)
		{
		}
	}


	// ------------------------------------------------------------------------
	private void recurseProjectContents(IRunnableContext context,
	        IResource[] members) throws CoreException, IOException,
	        SubmissionTargetException
	{
		for(int i = 0; i < members.length; i++)
		{
			IResource current = members[i];

			if(current.getType() == IResource.FOLDER)
			{
				recurseProjectContents(context, ((IFolder)current).members());
			}
			else if(current.getType() == IResource.FILE)
			{
				IFile file = (IFile)current;
				String projectRelativePath =
				        current.getProjectRelativePath().toString();

				// Find out whether this file is included or
				// excluded.
				boolean excluded =
				        asmt.isFileExcluded(projectRelativePath, context);
				if(!excluded)
				{
					ZipEntry entry = new ZipEntry(projectRelativePath);
					zip.putNextEntry(entry);

					File inFileSpec = file.getLocation().toFile();
					FileInputStream inFile = new FileInputStream(inFileSpec);
					byte[] data = new byte[(int)inFileSpec.length()];
					inFile.read(data);
					inFile.close();

					zip.write(data);
					zip.flush();
				}
			}
		}
	}


	// === Instance Variables =================================================

	/**
	 * The submission target to which the project is being submitted.
	 */
	private ITarget asmt;

	/**
	 * The project resource that is being submitted.
	 */
	private IProject project;

	/**
	 * The output stream to which the contents of the zip file should be
	 * written.
	 */
	private ZipOutputStream zip;
}
