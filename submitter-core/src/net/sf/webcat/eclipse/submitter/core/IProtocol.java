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
import java.net.URI;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableContext;

/**
 * Defines the interface used by the submission engine to electronically submit
 * a project. Submission protocols should implement this interface and put their
 * custom submission functionality in the submit method.
 * 
 * @author Tony Allowatt (Virginia Tech Computer Science)
 */
public interface IProtocol
{
	/**
	 * Submits the project.
	 * 
	 * @param monitor
	 *            An IProgressMonitor that is used to display the progress of
	 *            the submission process.
	 * @param params
	 *            A SubmissionParameters object that contains information about
	 *            the project to be submitted.
	 * @param transport
	 *            A URI to which the project will be submitted.
	 * 
	 * @throws CoreException
	 *             if there is an Eclipse runtime error while traversing the
	 *             project tree.
	 * @throws IOException
	 *             if there is an I/O error.
	 * @throws InterruptedException
	 *             if the submission process was interrupted by the user.
	 */
	void submit(IRunnableContext context,
			IProgressMonitor monitor, SubmissionParameters params,
			URI transport) throws CoreException, IOException,
			InterruptedException;

	/**
	 * Returns a value indicating whether the protocol sends back a response
	 * from the submission.
	 * 
	 * @return true if a meaningful response is returned; otherwise, false.
	 */
	boolean hasResponse();

	/**
	 * Returns the response to the submission.
	 * 
	 * @return A String containing a protocol-specific response.
	 */
	String getResponse();
}