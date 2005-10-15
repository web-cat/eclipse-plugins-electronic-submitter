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
 * Thrown by the submission engine if the user tries to submit a project
 * that does not include all of the required files.
 * 
 * @author Tony Allowatt (Virginia Tech Computer Science)
 */
public class RequiredFilesMissingException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The required file patterns that could not be matched.
	 */
	private String[] missingFiles;

	/**
	 * Creates a new instance of this exception.
	 * 
	 * @param files An array of Strings representing the patterns that could
	 *        not be matched.
	 */
	public RequiredFilesMissingException(String[] files)
	{
		missingFiles = files;
	}

	/**
	 * Gets an array of required file patterns that could not be matched during
	 * the submission process.
	 * 
	 * @return An array of Strings representing the missing files.
	 */
	public String[] getMissingFiles()
	{
		return missingFiles;
	}
}
