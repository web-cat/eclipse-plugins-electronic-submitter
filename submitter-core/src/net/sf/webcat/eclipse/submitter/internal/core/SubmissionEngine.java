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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.webcat.eclipse.submitter.core.IProtocol;
import net.sf.webcat.eclipse.submitter.core.ISubmissionEngine;
import net.sf.webcat.eclipse.submitter.core.ISubmissionListener;
import net.sf.webcat.eclipse.submitter.core.ITarget;
import net.sf.webcat.eclipse.submitter.core.ITargetRoot;
import net.sf.webcat.eclipse.submitter.core.RequiredFilesMissingException;
import net.sf.webcat.eclipse.submitter.core.SubmissionParameters;
import net.sf.webcat.eclipse.submitter.core.SubmissionTargetException;
import net.sf.webcat.eclipse.submitter.core.SubmitterCore;
import net.sf.webcat.eclipse.submitter.core.TargetParseError;
import net.sf.webcat.eclipse.submitter.core.TargetParseException;
import net.sf.webcat.eclipse.submitter.internal.core.util.FilePattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * This class is the core submission engine, which manages the object tree that
 * represents the submission targets in the assignment definiton XML file, and
 * also handles packaging and submitting the project files.
 * 
 * @author Tony Allevato (Virginia Tech Computer Science)
 */
public class SubmissionEngine implements ISubmissionEngine
{
	// === Methods ============================================================

	// ------------------------------------------------------------------------
	public ITargetRoot getRoot()
	{
		return root;
	}


	// ------------------------------------------------------------------------
	public void openDefinitions(URL definitionsURL, IRunnableContext context)
	        throws IOException, SubmissionTargetException
	{
		final URL url = definitionsURL;

		IRunnableWithProgress runnable = new IRunnableWithProgress()
		{
			public void run(IProgressMonitor monitor)
			        throws InvocationTargetException, InterruptedException
			{
				InputStream stream = null;

				monitor.beginTask("Accessing submission definitions...",
				        IProgressMonitor.UNKNOWN);

				try
				{
					stream = url.openStream();
					openFromStream(stream, monitor);
				}
				catch(Exception e)
				{
					throw new InvocationTargetException(e);
				}
				finally
				{
					monitor.done();

					try
					{
						if(stream != null)
							stream.close();
					}
					catch(IOException e)
					{
					}
				}
			}
		};

		try
		{
			context.run(true, false, runnable);
		}
		catch(InvocationTargetException e)
		{
			Throwable te = e.getTargetException();

			if(te instanceof IOException)
				throw (IOException)te;
			else if(te instanceof SubmissionTargetException)
				throw (SubmissionTargetException)te;
			else
				throw new SubmissionTargetException(te);
		}
		catch(InterruptedException e)
		{
		}
	}


	// ------------------------------------------------------------------------
	public void openDefinitions(final String definitionsStr,
	        IRunnableContext context) throws IOException,
	        SubmissionTargetException
	{
		IRunnableWithProgress runnable = new IRunnableWithProgress()
		{
			public void run(IProgressMonitor monitor)
			        throws InvocationTargetException, InterruptedException
			{
				InputStream stream = null;

				monitor.beginTask("Accessing submission definitions...",
				        IProgressMonitor.UNKNOWN);

				try
				{
					stream = new ByteArrayInputStream(definitionsStr.getBytes());
					openFromStream(stream, monitor);
				}

				catch(Exception e)
				{
					throw new InvocationTargetException(e);
				}
				finally
				{
					monitor.done();

					try
					{
						if(stream != null)
							stream.close();
					}
					catch(IOException e)
					{
					}
				}
			}
		};

		try
		{
			context.run(true, false, runnable);
		}
		catch(InvocationTargetException e)
		{
			Throwable te = e.getTargetException();

			if(te instanceof IOException)
				throw (IOException)te;
			else if(te instanceof SubmissionTargetException)
				throw (SubmissionTargetException)te;
			else
				throw new SubmissionTargetException(te);
		}
		catch(InterruptedException e)
		{
		}
	}


	// ------------------------------------------------------------------------
	private void openFromStream(InputStream stream, IProgressMonitor monitor)
	        throws SubmissionTargetException, IOException,
	        ParserConfigurationException, SAXException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		factory.setIgnoringComments(true);
		factory.setCoalescing(false);
		factory.setNamespaceAware(true);
		factory.setValidating(true);
		factory.setAttribute(
		        "http://java.sun.com/xml/jaxp/properties/schemaLanguage",
		        "http://www.w3.org/2001/XMLSchema");
		factory.setAttribute(
		        "http://java.sun.com/xml/jaxp/properties/schemaSource",
		        getClass().getResource("submission-targets.xsd").toString());

		SubmissionParserErrorHandler errorHandler = new SubmissionParserErrorHandler();

		DocumentBuilder builder = factory.newDocumentBuilder();
		builder.setErrorHandler(errorHandler);

		Document document = builder.parse(stream);

		TargetParseError[] errors = errorHandler.getErrors();
		if(errors != null)
		{
			throw new TargetParseException(errors);
		}
		else
		{
			root = new TargetRoot();
			root.parse(document.getDocumentElement());
		}
	}


	// ------------------------------------------------------------------------
	public boolean hasResponse()
	{
		return _hasResponse;
	}


	// ------------------------------------------------------------------------
	public String getSubmissionResponse()
	{
		return submissionResponse;
	}


	// ------------------------------------------------------------------------
	public void submitProject(final IRunnableContext context,
	        final SubmissionParameters params) throws InterruptedException,
	        MalformedURLException, IOException, CoreException,
	        RequiredFilesMissingException
	{
		_hasResponse = false;
		submissionResponse = null;

		// Check that all the required files are found.
		String[] missingFiles = verifyRequiredFiles(context, params
		        .getAssignment(), params.getProject(), "", params.getProject()
		        .members());
		if(missingFiles != null)
			throw new RequiredFilesMissingException(missingFiles);

		IRunnableWithProgress runnable = new IRunnableWithProgress()
		{
			public void run(IProgressMonitor monitor)
			        throws InvocationTargetException, InterruptedException
			{
				notifyListenersOfStart(params);

				try
				{
					monitor.beginTask("Please wait... ",
					        IProgressMonitor.UNKNOWN);
					monitor.subTask("Submitting project files...");

					String transportString = null;

					try
					{
						transportString = params.resolveParameter(params
						        .getAssignment().getTransport(context));
					}
					catch(SubmissionTargetException e)
					{
						throw e.getInnerException();
					}

					URI transport = null;

					try
					{
						transport = new URI(transportString);
					}
					catch(URISyntaxException e)
					{
						throw new MalformedURLException(e.getMessage());
					}

					IProtocol protocol = null;
					String protocolName = transport.getScheme();

					protocol = SubmitterCore.getDefault().getProtocolRegistry()
					        .getProtocol(protocolName);

					if(protocol != null)
					{
						protocol.submit(context, monitor, params, transport);
						monitor.done();

						if(protocol.hasResponse())
						{
							_hasResponse = true;
							submissionResponse = protocol.getResponse();
						}
					}
					else
					{
						throw new MalformedURLException(
						        "The protocol \""
						                + protocolName
						                + "\" was not registered with the submission plug-in.");
					}
				}
				catch(Throwable e)
				{
					notifyListenersOfFailure(params, e);

					throw new InvocationTargetException(e);
				}

				notifyListenersOfSuccess(params, submissionResponse);
			}
		};

		try
		{
			context.run(true, false, runnable);
		}
		catch(InvocationTargetException e)
		{
			Throwable te = e.getTargetException();

			if(te instanceof MalformedURLException)
				throw (MalformedURLException)te;
			else if(te instanceof CoreException)
				throw (CoreException)te;
			else if(te instanceof IOException)
				throw (IOException)te;
		}
	}


	// ------------------------------------------------------------------------
	private void notifyListenersOfStart(final SubmissionParameters params)
	{
		final ISubmissionListener[] listeners = SubmitterCore.getDefault()
		        .getSubmissionListeners();

		for(int i = 0; i < listeners.length; i++)
			listeners[i].submissionStarted(params);
	}


	// ------------------------------------------------------------------------
	private void notifyListenersOfSuccess(final SubmissionParameters params,
	        final String response)
	{
		final ISubmissionListener[] listeners = SubmitterCore.getDefault()
		        .getSubmissionListeners();

		for(int i = 0; i < listeners.length; i++)
			listeners[i].submissionSucceeded(params, response);
	}


	// ------------------------------------------------------------------------
	private void notifyListenersOfFailure(final SubmissionParameters params,
	        final Throwable exception)
	{
		final ISubmissionListener[] listeners = SubmitterCore.getDefault()
		        .getSubmissionListeners();

		for(int i = 0; i < listeners.length; i++)
			listeners[i].submissionFailed(params, exception);
	}


	// ------------------------------------------------------------------------
	private String[] verifyRequiredFiles(IRunnableContext context,
	        ITarget asmt, IProject project, String folder, IResource[] members)
	        throws CoreException
	{
		Hashtable<String, Boolean> requiredFiles = new Hashtable<String, Boolean>();

		String[] patterns = null;
		try
		{
			patterns = asmt.getAllRequired(context);
		}
		catch(SubmissionTargetException e)
		{
			// We can ignore this exception safely because the target
			// will already be loaded at this point, or else it could
			// not have been passed in to the function. It should
			// never be thrown at this point.
		}

		for(int i = 0; i < patterns.length; i++)
			requiredFiles.put(patterns[i], Boolean.FALSE);

		recurseRequiredFiles(asmt, project, folder, members, requiredFiles);

		Vector<String> vec = new Vector<String>();
		Enumeration<String> reqEnum = requiredFiles.keys();
		while(reqEnum.hasMoreElements())
		{
			String reqFile = reqEnum.nextElement();
			boolean found = requiredFiles.get(reqFile).booleanValue();

			if(!found)
				vec.add(reqFile);
		}

		if(vec.isEmpty())
			return null;
		else
		{
			String[] array = new String[vec.size()];
			vec.toArray(array);
			return array;
		}
	}


	// ------------------------------------------------------------------------
	private void recurseRequiredFiles(ITarget asmt, IProject project,
	        String folder, IResource[] members,
	        Hashtable<String, Boolean> requiredFiles)
	        throws CoreException
	{
		for(int i = 0; i < members.length; i++)
		{
			IResource current = members[i];
			if(current.getType() == IResource.FOLDER)
			{
				String folderName = current.getName();
				if(folder != "")
					folderName = folder + "/" + folderName;

				recurseRequiredFiles(asmt, project, folderName,
				        ((IFolder)current).members(), requiredFiles);
			}
			else if(current.getType() == IResource.FILE)
			{
				IFile file = (IFile)current;
				String fileName;
				if(folder == "")
					fileName = file.getName();
				else
					fileName = folder + "/" + file.getName();

				Enumeration<String> reqEnum = requiredFiles.keys();
				while(reqEnum.hasMoreElements())
				{
					String reqFile = reqEnum.nextElement();
					FilePattern pattern = new FilePattern(reqFile);
					if(pattern.matches(fileName))
					{
						requiredFiles.put(reqFile, Boolean.TRUE);
					}
				}
			}
		}
	}


	// === Instance Variables =================================================

	/**
	 * The root of the submission target object tree.
	 */
	private TargetRoot root;

	/**
	 * Indicates whether the submission generated a response.
	 */
	private boolean _hasResponse;

	/**
	 * Contains the response generated by the submission, if any.
	 */
	private String submissionResponse;
}
