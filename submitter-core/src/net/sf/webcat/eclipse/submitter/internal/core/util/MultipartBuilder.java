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

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Random;

/**
 * A utility class to aid in the construction of an HTTP POST multipart
 * request.
 * 
 * @author Tony Allowatt (Virginia Tech Computer Science)
 */
public class MultipartBuilder
{
	/**
	 * The connection to which the request will be sent. 
	 */
	private HttpURLConnection connection;
	
	/**
	 * The stream to which the request will be written.
	 */
	private OutputStream outStream;

	private static Random random = new Random();

	private static String randomString()
	{
		return Long.toString(random.nextLong(), 36);
	}

	private static final String boundaryString = "---------------------------"
			+ randomString() + randomString() + randomString();

	/**
	 * Generates a new multipart request builder attached to the specified
	 * connection.
	 * 
	 * @param con The connection that to which the multipart request will be
	 *            sent.
	 * 
	 * @throws IOException if any I/O errors occur.
	 */
	public MultipartBuilder(HttpURLConnection con) throws IOException
	{
		connection = con;

		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type",
				"multipart/form-data; boundary=" + boundaryString);

		outStream = connection.getOutputStream();
	}

	private void writeBoundary() throws IOException
	{
		write("--");
		write(boundaryString);
	}

	protected void write(char c) throws IOException
	{
		outStream.write(c);
	}

	protected void write(String s) throws IOException
	{
		outStream.write(s.getBytes());
	}

	protected void newline() throws IOException
	{
		write("\r\n");
	}

	protected void writeln(String s) throws IOException
	{
		write(s);
		newline();
	}

	private void writeName(String name) throws IOException
	{
		newline();
		write("Content-Disposition: form-data; name=\"");
		write(name);
		write('"');
	}

	/**
	 * Adds a parameter name/value pair to the request.
	 * 
	 * @param name The name of the parameter.
	 * @param value The value of the parameter.
	 * 
	 * @throws IOException if any I/O errors occur.
	 */
	public void writeParameter(String name, String value) throws IOException
	{
		writeBoundary();
		writeName(name);
		newline();
		newline();
		writeln(value);
	}

	/**
	 * Adds the appropriate headers for a file attachment to the request.
	 * 
	 * @param name The name of the request parameter.
	 * @param filename The name of the file attachment.
	 * @param contentType The MIME content type of the attachment.
	 * 
	 * @return The OutputStream to which the file can be written.
	 * 
	 * @throws IOException if any I/O errors occur.
	 */
	public OutputStream beginWriteFile(String name, String filename,
			String contentType) throws IOException
	{
		writeBoundary();
		writeName(name);
		write("; filename=\"");
		write(filename);
		write('"');
		newline();
		write("Content-Type: ");
		writeln(contentType);
		newline();
		return outStream;
	}

	/**
	 * Completes the file attachment operation begun by beginWriteFile.
	 * 
	 * @throws IOException if any I/O errors occur.
	 */
	public void endWriteFile() throws IOException
	{
		newline();
	}

	/**
	 * Completes the multipart request and closes the stream.
	 * 
	 * @throws IOException if any I/O errors occur.
	 */
	public void close() throws IOException
	{
		writeBoundary();
		writeln("--");
		outStream.close();
	}
}