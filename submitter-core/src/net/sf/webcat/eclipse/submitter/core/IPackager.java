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
package net.sf.webcat.eclipse.submitter.core;

import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.operation.IRunnableContext;

/**
 * The packager interface implemented by classes that are registered as
 * packagers in the submission plug-in.
 * 
 * @author Tony Allevato (Virginia Tech Computer Science)
 */
public interface IPackager
{
	// ------------------------------------------------------------------------
	/**
	 * Invoked by the submission engine when the project needs to be packaged
	 * for submission.
	 * 
	 * @param context
	 *            An object that implements the IRunnableContext interface that
	 *            can be used to manage long-running stages of the packaging
	 *            process.
	 * @param params
	 *            A SubmissionParameters object that contains information about
	 *            the project to be submitted.
	 * @param stream
	 *            An OutputStream representing the destination file/storage to
	 *            which the packaged submission should be written.
	 * 
	 * @throws CoreException
	 *             if there is an Eclipse runtime error while traversing the
	 *             project tree.
	 * @throws IOException
	 *             if there is an I/O error.
	 */
	void pack(IRunnableContext context, SubmissionParameters params,
	        OutputStream stream) throws CoreException, IOException;
}
