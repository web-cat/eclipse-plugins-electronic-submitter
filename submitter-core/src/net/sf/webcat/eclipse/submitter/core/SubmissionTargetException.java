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
 * A general exception type used by the submission engine that wraps other more
 * specific exceptions so that they can be handled in a common manner. This
 * class is also the base for other more specific submission-related exceptions.
 * 
 * @author Tony Allevato (Virginia Tech Computer Science)
 */
public class SubmissionTargetException extends Exception
{
	// === Methods ============================================================

	// ------------------------------------------------------------------------
	/**
	 * Creates a new instance of this exception with no inner exception. This is
	 * only used by subclasses that don't represent their errors using wrapped
	 * exceptions.
	 */
	protected SubmissionTargetException()
	{
		inner = null;
	}


	// ------------------------------------------------------------------------
	/**
	 * Creates a new instance of this exception that wraps the specified
	 * exception.
	 * 
	 * @param inner
	 *            A Throwable that represents the actual exception that was
	 *            thrown.
	 */
	public SubmissionTargetException(Throwable inner)
	{
		this.inner = inner;
	}


	// ------------------------------------------------------------------------
	/**
	 * Gets the actual exception that is wrapped by this exception.
	 * 
	 * @return A Throwable that represents the wrapped exception.
	 */
	public Throwable getInnerException()
	{
		return inner;
	}


	// === Static Variables ===================================================

	private static final long serialVersionUID = 1L;

	
	// === Instance variables =================================================

	/**
	 * A reference to the actual exception that was thrown.
	 */
	private Throwable inner;
}
