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

/**
 * Third-party plugins can extend the
 * "net.sf.webcat.eclipse.submitter.submissionListeners" extension point by
 * implementing this interface in order to hook into the submission process and
 * get notifications when submissions are made.
 * 
 * @author Tony Allevato (Virginia Tech Computer Science)
 */
public interface ISubmissionListener
{
	// ------------------------------------------------------------------------
	/**
	 * Called immediately before a project is about to be submitted through the
	 * electronic submission engine. This occurs, however, after the user has
	 * made a selection in the user interface as to which project, submission
	 * target, name, and password he or she will use to submit the project.
	 * 
	 * @param params
	 *            A SubmissionParameters object that contains information about
	 *            the project being submitted.
	 */
	void submissionStarted(SubmissionParameters params);


	// ------------------------------------------------------------------------
	/**
	 * Called when a submission has completed successfully with no errors.
	 * 
	 * @param params
	 *            A SubmissionParameters object that contains information about
	 *            the project being submitted.
	 * @param response
	 *            A String containing the response returned by the submission
	 *            target, or null if there was no response.
	 */
	void submissionSucceeded(SubmissionParameters params, String response);


	// ------------------------------------------------------------------------
	/**
	 * Called when a submission fails.
	 * 
	 * @param params
	 *            A SubmissionParameters object that contains information about
	 *            the project being submitted.
	 * @param exception
	 *            A Throwable object describing the reason that the submission
	 *            failed.
	 */
	void submissionFailed(SubmissionParameters params, Throwable exception);
}
