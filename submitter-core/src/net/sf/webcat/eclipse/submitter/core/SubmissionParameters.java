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

import java.util.regex.Pattern;

import org.eclipse.core.resources.IProject;

/**
 * This class collects references to a number of objects that are required in
 * various places during the submission process, so that they can be easily
 * passed between functions.
 * 
 * @author Tony Allevato (Virginia Tech Computer Science)
 */
public class SubmissionParameters
{
	// === Methods ============================================================

	// ------------------------------------------------------------------------
	/**
	 * Gets the assignment referred to by this object.
	 * 
	 * @return An IDefinitionObject representing the assignment.
	 */
	public ITarget getAssignment()
	{
		return assignment;
	}


	// ------------------------------------------------------------------------
	/**
	 * Sets the assignment referred to by this object.
	 * 
	 * @param value
	 *            An IDefinitionObject representing the assignment.
	 */
	public void setAssignment(ITarget value)
	{
		assignment = value;
	}


	// ------------------------------------------------------------------------
	/**
	 * Gets the project referred to by this object.
	 * 
	 * @return An IProject representing the Eclipse project.
	 */
	public IProject getProject()
	{
		return project;
	}


	// ------------------------------------------------------------------------
	/**
	 * Sets the project referred to by this object.
	 * 
	 * @param value
	 *            An IProject representing the Eclipse project.
	 */
	public void setProject(IProject value)
	{
		project = value;
	}


	// ------------------------------------------------------------------------
	/**
	 * Gets the username referred to by this object.
	 * 
	 * @return A String containing the username.
	 */
	public String getUsername()
	{
		return username;
	}


	// ------------------------------------------------------------------------
	/**
	 * Sets the username referred to by this object.
	 * 
	 * @param value
	 *            A String containing the username.
	 */
	public void setUsername(String value)
	{
		username = value;
	}


	// ------------------------------------------------------------------------
	/**
	 * Gets the password referred to by this object.
	 * 
	 * @return A String containing the password.
	 */
	public String getPassword()
	{
		return password;
	}


	// ------------------------------------------------------------------------
	/**
	 * Sets the password referred to by this object.
	 * 
	 * @param value
	 *            A String containing the password.
	 */
	public void setPassword(String value)
	{
		password = value;
	}


	// ------------------------------------------------------------------------
	/**
	 * Resolves the specified parameter string by replacing any variable
	 * placeholders with their actual values. Currently the following
	 * placeholders are supported:
	 * <ul>
	 * <li>${user} - the username</li>
	 * <li>${pw} - the password</li>
	 * <li>${assignment.name} - the name of the assignment</li>
	 * </ul>
	 * 
	 * @param value
	 *            The String containing placeholders to be resolved.
	 * 
	 * @return A copy of the original string with the placeholders replaced by
	 *         their actual values.
	 */
	public String resolveParameter(String value)
	{
		Pattern pattern;

		pattern = Pattern.compile("\\$\\{user\\}");
		value = pattern.matcher(value).replaceAll(username);

		pattern = Pattern.compile("\\$\\{pw\\}");
		value = pattern.matcher(value).replaceAll(password);

		INameableTarget nameable = (INameableTarget)assignment
		        .getAdapter(INameableTarget.class);

		if(nameable != null)
		{
			pattern = Pattern.compile("\\$\\{assignment\\.name\\}");
			String asmtName = nameable.getName().replaceAll(" ", "%20");
			value = pattern.matcher(value).replaceAll(asmtName);
		}

		return value;
	}

	
	// === Instance variables =================================================

	/**
	 * The assignment to which the user is submitting.
	 */
	private ITarget assignment;

	/**
	 * The Eclipse project that should be packaged and submitted.
	 */
	private IProject project;

	/**
	 * The ID of the user.
	 */
	private String username;

	/**
	 * The password used to log into the submission target system, if required.
	 */
	private String password;
}
