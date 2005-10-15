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
package net.sf.webcat.eclipse.submitter.internal.core.util;

import java.io.File;
import java.util.regex.Pattern;

/**
 * This class internally converts a DOS-style wildcard pattern into a regular
 * expression that can be used to match filenames.
 * 
 * @author Tony Allowatt (Virginia Tech Computer Science)
 */
public class FilePattern
{
	/**
	 * The regular expression that represents this file pattern.
	 */
	private Pattern pattern;

	/**
	 * Creates a new file pattern matcher.
	 * 
	 * @param pattern The DOS-style wildcard pattern to match.
	 */
	public FilePattern(String pattern)
	{
		int length = pattern.length();

		StringBuffer buffer = new StringBuffer(length);

		for(int i = 0; i < length; i++)
		{
			char c = pattern.charAt(i);

			if(!Character.isLetterOrDigit(c))
			{
				switch(c)
				{
					case '?': // Fall through
					case '*':
						buffer.append('.');
						break;
					default:
						buffer.append('\\');
						break;
				}
			}

			buffer.append(c);
		}

		this.pattern = Pattern.compile(buffer.toString());
	}

	/**
	 * Determines if the specified path matches the pattern.
	 * 
	 * @param path The path to match.
	 * 
	 * @return true if the path matches the pattern; otherwise, false.
	 */
	public boolean matches(String path)
	{
		return pattern.matcher(path).matches();
	}

	/**
	 * Determines if the specified File object matches the pattern.
	 * 
	 * @param file The File to match.
	 * 
	 * @return true if the File matches the pattern; otherwise, false.
	 */
	public boolean matches(File file)
	{
		return matches(file.getAbsolutePath());
	}
}