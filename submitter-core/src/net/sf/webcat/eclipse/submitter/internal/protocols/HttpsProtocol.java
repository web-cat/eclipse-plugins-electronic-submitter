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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import net.sf.webcat.eclipse.submitter.core.IPackager;
import net.sf.webcat.eclipse.submitter.core.IPackagerRegistry;
import net.sf.webcat.eclipse.submitter.core.IProtocol;
import net.sf.webcat.eclipse.submitter.core.ITarget;
import net.sf.webcat.eclipse.submitter.core.SubmissionParameters;
import net.sf.webcat.eclipse.submitter.core.SubmissionTargetException;
import net.sf.webcat.eclipse.submitter.core.SubmitterCore;
import net.sf.webcat.eclipse.submitter.internal.core.util.MultipartBuilder;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableContext;

/**
 * A protocol for the "https" URI scheme that supports sending the submitted
 * file as part of a secure HTTP POST request to a remote server.  This
 * protocol generates an HTTP response that can be displayed to the user in
 * a browser window.
 * 
 * @author Tony Allowatt (Virginia Tech Computer Science)
 */
public class HttpsProtocol implements IProtocol
{
	private String response;

	public void submit(IRunnableContext context,
			IProgressMonitor monitor, SubmissionParameters params,
			URI transport) throws CoreException, IOException,
			InterruptedException
	{
		try
		{
			URL url = transport.toURL();
			
			HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
			MultipartBuilder multipart = new MultipartBuilder(connection);
	
			ITarget asmt = params.getAssignment();
	
			Set transportParams = asmt.getTransportParams(context).entrySet();
			for(Iterator it = transportParams.iterator(); it.hasNext(); )
			{
				Map.Entry entry = (Map.Entry)it.next();
				String paramName = (String)entry.getKey();
				String paramValue = (String)entry.getValue();
				String convertedValue = params.resolveParameter(paramValue);
	
				if(paramName.startsWith("$file."))
				{
					String key = paramName.substring(6);
					OutputStream outStream = multipart.beginWriteFile(key,
							convertedValue, "application/octet-stream");
	
					IPackagerRegistry manager = SubmitterCore.getDefault().getPackagerRegistry();
					IPackager packager = manager.getPackager(params.getAssignment().getPackager(context));
					packager.pack(context, params, outStream);
	
					multipart.endWriteFile();
				}
				else
				{
					multipart.writeParameter(paramName, convertedValue);
				}
			}
	
			multipart.close();
	
			InputStream inStream = connection.getInputStream();
			StringBuffer buffer = new StringBuffer();
	
			int nextChar = inStream.read();
			while(nextChar != -1)
			{
				buffer.append((char)nextChar);
				nextChar = inStream.read();
			}
	
			inStream.close();
	
			response = buffer.toString();
		}
		catch(SubmissionTargetException e)
		{
		}
	}

	public boolean hasResponse()
	{
		return true;
	}

	public String getResponse()
	{
		return response;
	}
}