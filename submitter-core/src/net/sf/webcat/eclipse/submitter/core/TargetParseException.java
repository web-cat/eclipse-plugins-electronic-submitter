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
 * This exception class collects all the errors that occur during the parsing of
 * the submission definitions file. Once parsing is complete, the user of the
 * submission engine can access the list of errors, if any, from the exception.
 * 
 * @author Tony Allevato (Virginia Tech Computer Science)
 */
public class TargetParseException extends SubmissionTargetException
{
	// ------------------------------------------------------------------------
	/**
	 * Creates a new exception object with the specified errors.
	 * 
	 * @param errors
	 *            An array of DefinitionParseError objects that describe the
	 *            errors that occurred during parsing.
	 */
	public TargetParseException(TargetParseError[] errors)
	{
		this.errors = errors;
	}


	// ------------------------------------------------------------------------
	/**
	 * Gets the list of errors that occurred during parsing.
	 * 
	 * @return An array of DefinitionParseError objects, or null if no errors
	 *         occurred.
	 */
	public TargetParseError[] getErrors()
	{
		return errors;
	}


	// === Static Variables ===================================================

	private static final long serialVersionUID = 1L;


	// === Instance Variables =================================================

	/**
	 * The list of errors that occurred during parsing.
	 */
	private TargetParseError[] errors;
}
