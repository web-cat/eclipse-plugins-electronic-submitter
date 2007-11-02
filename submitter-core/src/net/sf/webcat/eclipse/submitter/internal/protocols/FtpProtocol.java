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
package net.sf.webcat.eclipse.submitter.internal.protocols;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import net.sf.webcat.eclipse.submitter.core.IPackager;
import net.sf.webcat.eclipse.submitter.core.IPackagerRegistry;
import net.sf.webcat.eclipse.submitter.core.IProtocol;
import net.sf.webcat.eclipse.submitter.core.SubmissionParameters;
import net.sf.webcat.eclipse.submitter.core.SubmissionTargetException;
import net.sf.webcat.eclipse.submitter.core.SubmitterCore;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableContext;

/**
 * A protocol for the "ftp" URI scheme that supports uploading the submitted
 * file to a remote server via FTP.
 * 
 * @author Tony Allevato (Virginia Tech Computer Science)
 */
public class FtpProtocol implements IProtocol
{
	// ------------------------------------------------------------------------
	public void submit(IRunnableContext context, IProgressMonitor monitor,
	        SubmissionParameters params, URI transport) throws CoreException,
	        IOException, InterruptedException
	{
		try
		{
			URL url = transport.toURL();

			URLConnection connection = url.openConnection();
			OutputStream outStream = connection.getOutputStream();

			IPackagerRegistry manager = SubmitterCore.getDefault()
			        .getPackagerRegistry();
			IPackager packager = manager.getPackager(params.getAssignment()
			        .getPackager(context));
			packager.pack(context, params, outStream);

			outStream.close();
		}
		catch(SubmissionTargetException e)
		{
		}
	}


	// ------------------------------------------------------------------------
	public boolean hasResponse()
	{
		return false;
	}


	// ------------------------------------------------------------------------
	public String getResponse()
	{
		return null;
	}
}
