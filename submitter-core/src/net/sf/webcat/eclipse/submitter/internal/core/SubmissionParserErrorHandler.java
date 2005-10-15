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
package net.sf.webcat.eclipse.submitter.internal.core;

import java.util.Vector;

import net.sf.webcat.eclipse.submitter.core.TargetParseError;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * This error handler collects all errors that occur during the parsing of the
 * submission definitions file so they can be thrown as a single exception if
 * any occur.
 * 
 * @author Tony Allowatt (Virginia Tech Computer Science)
 */
public class SubmissionParserErrorHandler implements ErrorHandler
{
	/**
	 * A Vector that keeps track of all the errors encountered during parsing.
	 */
	private Vector errors;

	/**
	 * Creates a new submission parser error handler.
	 *  
	 */
	public SubmissionParserErrorHandler()
	{
		errors = new Vector();
	}

	public void warning(SAXParseException arg0) throws SAXException
	{
	}

	public void error(SAXParseException arg0) throws SAXException
	{
		TargetParseError error = new TargetParseError(arg0
				.getLineNumber(), arg0.getColumnNumber(), arg0.getMessage());
		errors.add(error);
	}

	public void fatalError(SAXParseException arg0) throws SAXException
	{
		TargetParseError error = new TargetParseError(arg0
				.getLineNumber(), arg0.getColumnNumber(), arg0.getMessage());
		errors.add(error);
	}

	/**
	 * Gets the list of errors that occurred during parsing.
	 * 
	 * @return An array of DefinitionParseError objects, or null if no errors
	 *         occurred.
	 */
	public TargetParseError[] getErrors()
	{
		if(errors.size() == 0)
			return null;

		TargetParseError[] array = new TargetParseError[errors.size()];
		for(int i = 0; i < errors.size(); i++)
			array[i] = (TargetParseError)errors.get(i);

		return array;
	}
}