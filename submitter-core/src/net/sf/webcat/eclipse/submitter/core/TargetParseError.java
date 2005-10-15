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
 * Encapsulates information about a parsing error in the submission definitions
 * file.
 * 
 * @author Tony Allowatt (Virginia Tech Computer Science)
 */
public class TargetParseError
{
	/**
	 * The line number at which the error occurred.
	 */
	private int line;

	/**
	 * The column at which the error occurred.
	 */
	private int column;

	/**
	 * The description of the error that occurred.
	 */
	private String message;

	/**
	 * Creates a new instance of the error object with the specified line and
	 * column numbers and description.
	 * 
	 * @param line
	 *            The line number at which the error occurred.
	 * @param column
	 *            The column at which the error occurred.
	 * @param message
	 *            The description of the error that occurred.
	 */
	public TargetParseError(int line, int column, String message)
	{
		this.line = line;
		this.column = column;
		this.message = message;
	}

	/**
	 * Gets the line number at which the error occurred.
	 * 
	 * @return An integer representing the line number.
	 */
	public int getLine()
	{
		return line;
	}

	/**
	 * Gets the column number at which the error occurred.
	 * 
	 * @return An integer representing the column number.
	 */
	public int getColumn()
	{
		return column;
	}

	/**
	 * Gets the description of the error that occurred.
	 * 
	 * @return A String containing the description.
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * Produces a human-readable error message from this error object.
	 * 
	 * @return A String containing the formatted error message.
	 */
	public String toString()
	{
		return "Line " + line + ", column " + column + ": " + message;
	}
}