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

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import net.sf.webcat.eclipse.submitter.core.ITarget;
import net.sf.webcat.eclipse.submitter.core.ITargetRoot;
import net.sf.webcat.eclipse.submitter.core.SubmissionTargetException;
import net.sf.webcat.eclipse.submitter.internal.core.util.FilePattern;

import org.eclipse.jface.operation.IRunnableContext;
import org.w3c.dom.Node;

/**
 * An abstract base class from which all the objects in the submission target
 * tree are derived. This class contains common implementation of the ITarget
 * interface.
 * 
 * @author Tony Allowatt (Virginia Tech Computer Science)
 */
public abstract class AbstractTarget implements ITarget
{
	/**
	 * The default packager to use when none is specified.
	 */
	private static String DEFAULT_PACKAGER = "net.sf.webcat.eclipse.submitter.packagers.zip";

	/**
	 * The parent object to this object in the tree.
	 */
	private AbstractTarget _parent;

	/**
	 * The list of file patterns that represent files that are to be included in
	 * a submission.
	 */
	private String[] includes;

	/**
	 * The list of file patterns that represent files that are to be excluded
	 * from a submission.
	 */
	private String[] excludes;

	/**
	 * The list of file patterns that represent files that must be included in a
	 * submission.
	 */
	private String[] required;

	/**
	 * The string that contains the submission protocol and destination
	 * location.
	 */
	private String transport;

	/**
	 * A table of name-value pairs that represent additional parameters that are
	 * included with the submission.
	 */
	private LinkedHashMap transportParams;

	/**
	 * The globally unique identifier of the packager used to submit the
	 * project. The packager should already have been registered with the
	 * submission core plug-in.
	 */
	private String packager;

	/**
	 * A table of name-value pairs that represent additional parameters that are
	 * passed to the packager.
	 */
	private LinkedHashMap packagerParams;

	/**
	 * The list of child nodes to this node in the submission target tree.
	 */
	private ITarget[] children;

	/**
	 * Initializes fields in the SubmissionObject abstract base class.
	 * 
	 * @param parent
	 *            The SubmissionObject that represents this node's parent in the
	 *            tree.
	 */
	protected AbstractTarget(AbstractTarget parent)
	{
		packager = DEFAULT_PACKAGER;

		_parent = parent;

		transportParams = new LinkedHashMap();
		packagerParams = new LinkedHashMap();
	}

	public Object getAdapter(Class adapterClass)
	{
		return null;
	}

	public ITarget parent()
	{
		return _parent;
	}

	protected void tryFireTargetObjectChanged(String property, Object oldValue, Object newValue)
	{
		ITargetRoot root = getRoot();
		if(root != null)
			root.fireTargetObjectChanged(this, property, oldValue, newValue);
	}

	public String[] getIncludes(IRunnableContext context)
			throws SubmissionTargetException
	{
		if (includes == null)
			return new String[0];
		else
			return includes;
	}

	public void setIncludes(String[] array)
	{
		String[] oldValue = includes;
		includes = array;
		
		tryFireTargetObjectChanged("includes", oldValue, includes);
	}

	public String[] getExcludes(IRunnableContext context)
			throws SubmissionTargetException
	{
		if (excludes == null)
			return new String[0];
		else
			return excludes;
	}

	public void setExcludes(String[] array)
	{
		String[] oldValue = excludes;
		excludes = array;

		tryFireTargetObjectChanged("excludes", oldValue, includes);
	}

	public String[] getRequired(IRunnableContext context)
			throws SubmissionTargetException
	{
		if (required == null)
			return new String[0];
		else
			return required;
	}

	public void setRequired(String[] array)
	{
		String[] oldValue = required;
		required = array;
		
		tryFireTargetObjectChanged("required", oldValue, required);
	}

	public String[] getAllRequired(IRunnableContext context)
			throws SubmissionTargetException
	{
		Vector vec = new Vector();
		getAllRequired(vec, context);

		String[] array = new String[vec.size()];
		vec.toArray(array);
		return array;
	}

	/**
	 * This helper function manages the actual recursion.
	 */
	private void getAllRequired(Vector vec, IRunnableContext context)
			throws SubmissionTargetException
	{
		vec.addAll(Arrays.asList(getRequired(context)));

		if (_parent != null)
			_parent.getAllRequired(vec, context);
	}

	public String getTransport(IRunnableContext context)
			throws SubmissionTargetException
	{
		if (transport != null)
			return transport;
		else
		{
			if (_parent != null)
				return _parent.getTransport(context);
			else
				return null;
		}
	}

	public String getLocalTransport(IRunnableContext context)
			throws SubmissionTargetException
	{
		return transport;
	}

	public void setTransport(String uri)
	{
		String oldValue = transport;
		transport = uri;
		
		tryFireTargetObjectChanged("transport", oldValue, transport);
	}

	public Map getTransportParams(IRunnableContext context)
			throws SubmissionTargetException
	{
		if (transport != null)
			return transportParams;
		else
		{
			if (_parent != null)
				return _parent.getTransportParams(context);
			else
				return new LinkedHashMap();
		}
	}

	public Map getLocalTransportParams(IRunnableContext context)
			throws SubmissionTargetException
	{
		return transportParams;
	}

	public void setTransportParams(Map params)
	{
		Map oldValue = transportParams;
		transportParams = new LinkedHashMap(params);
	
		tryFireTargetObjectChanged("transportParams", oldValue, transportParams);
	}

	public String getPackager(IRunnableContext context)
			throws SubmissionTargetException
	{
		if (packager != null)
			return packager;
		else
		{
			if (_parent != null)
				return _parent.getPackager(context);
			else
				return null;
		}
	}

	public String getLocalPackager(IRunnableContext context)
			throws SubmissionTargetException
	{
		return packager;
	}

	public void setPackager(String id)
	{
		String oldValue = packager;
		packager = id;

		tryFireTargetObjectChanged("packager", oldValue, packager);
	}

	public Map getPackagerParams(IRunnableContext context)
			throws SubmissionTargetException
	{
		if (packager != null)
			return packagerParams;
		else
		{
			if (_parent != null)
				return _parent.getPackagerParams(context);
			else
				return new LinkedHashMap();
		}
	}

	public Map getLocalPackagerParams(IRunnableContext context)
			throws SubmissionTargetException
	{
		return packagerParams;
	}

	public void setPackagerParams(Map params)
	{
		Map oldValue = packagerParams;
		packagerParams = new LinkedHashMap(params);

		tryFireTargetObjectChanged("packagerParams", oldValue, packagerParams);
	}

	public ITarget[] getChildren(IRunnableContext context)
			throws SubmissionTargetException
	{
		if (children == null)
			return new ITarget[0];
		else
			return children;
	}

	public void setChildren(ITarget[] array)
	{
		ITarget[] oldValue = children;
		children = array;

		tryFireTargetObjectChanged("children", oldValue, children);
	}

	/**
	 * A helper function to ease initializing the included files from a list.
	 */
	protected void setIncludes(List list)
	{
		String[] oldValue = includes;
		includes = new String[list.size()];
		list.toArray(includes);

		tryFireTargetObjectChanged("includes", oldValue, includes);
	}

	/**
	 * A helper function to ease initializing the excluded files from a list.
	 */
	protected void setExcludes(List list)
	{
		String[] oldValue = excludes;
		excludes = new String[list.size()];
		list.toArray(excludes);

		tryFireTargetObjectChanged("excludes", oldValue, excludes);
	}

	/**
	 * A helper function to ease initializing the required files from a list.
	 */
	protected void setRequired(List list)
	{
		String[] oldValue = required;
		required = new String[list.size()];
		list.toArray(required);

		tryFireTargetObjectChanged("required", oldValue, required);
	}

	/**
	 * A helper function to ease initializing the children from a list.
	 */
	protected void setChildren(List list)
	{
		ITarget[] oldValue = children;
		children = new ITarget[list.size()];
		list.toArray(children);

		tryFireTargetObjectChanged("children", oldValue, children);
	}

	public ITargetRoot getRoot()
	{
		ITarget par = this;
		while(par.parent() != null)
			par = par.parent();

		if(par instanceof ITargetRoot)
			return (ITargetRoot)par;
		else
			return null;
	}

	public boolean isFileExcluded(File file, IRunnableContext context)
			throws SubmissionTargetException
	{
		// Check to see if the file is excluded locally.
		String[] exc = getExcludes(context);
		for (int i = 0; i < exc.length; i++)
		{
			FilePattern pattern = new FilePattern(exc[i]);
			if (pattern.matches(file.getName()))
				return true;
		}

		// Check to see if the file is explicitly included
		// locally.
		String[] inc = getIncludes(context);
		for (int i = 0; i < inc.length; i++)
		{
			FilePattern pattern = new FilePattern(inc[i]);
			if (pattern.matches(file.getName()))
				return false;
		}

		// If no explicit mention of the file was found,
		// try going up the assignment tree.
		if (_parent != null)
			return _parent.isFileExcluded(file, context);
		else
			return (inc.length != 0);
	}

	/**
	 * Used by delay-loading nodes, this function copies the entire tree rooted
	 * at the specified node into the current tree at the position represented
	 * by the node on which this method is invoked.
	 * 
	 * @param obj
	 *            The tree from which to copy.
	 * @param context
	 *            The context on which to run the operation.
	 * 
	 * @throws SubmissionTargetException
	 *             if there are any errors during delay-loading.
	 */
	protected void copyFrom(ITarget obj, IRunnableContext context)
			throws SubmissionTargetException
	{
		// Used for delay-loading. Takes the specified definition object
		// and copies it into the current object.
		setChildren(obj.getChildren(context));
		setIncludes(obj.getIncludes(context));
		setExcludes(obj.getExcludes(context));
		setRequired(obj.getRequired(context));

		setTransport(obj.getTransport(context));
		setTransportParams(obj.getTransportParams(context));

		setPackager(obj.getPackager(context));
		setPackagerParams(obj.getPackagerParams(context));
	}

	/**
	 * Parses a transport element and initializes the appropriate fields.
	 * 
	 * @param parentNode
	 *            The XML node representing the transport element.
	 * 
	 * @throws SubmissionTargetException
	 *             if there are any parsing errors.
	 */
	protected void parseTransport(Node parentNode)
			throws SubmissionTargetException
	{
		Node uriNode = parentNode.getAttributes().getNamedItem("uri");
		if (uriNode != null)
			transport = uriNode.getNodeValue();

		// Parse the parameter tags.
		Node node = parentNode.getFirstChild();
		while (node != null)
		{
			String nodeName = node.getLocalName();

			if ("param".equals(nodeName))
			{
				parseTransportParameter(node);
			}
			else if ("file-param".equals(nodeName))
			{
				parseTransportFileParameter(node);
			}

			node = node.getNextSibling();
		}
	}

	/**
	 * Parses a packager element and initializes the appropriate fields.
	 * 
	 * @param parentNode
	 *            The XML node representing the packager element.
	 * 
	 * @throws SubmissionTargetException
	 *             if there are any parsing errors.
	 */
	protected void parsePackager(Node parentNode)
			throws SubmissionTargetException
	{
		Node idNode = parentNode.getAttributes().getNamedItem("id");
		if (idNode != null)
			packager = idNode.getNodeValue();

		// Parse the parameter tags.
		Node node = parentNode.getFirstChild();
		while (node != null)
		{
			String nodeName = node.getLocalName();

			if ("param".equals(nodeName))
			{
				parsePackagerParameter(node);
			}

			node = node.getNextSibling();
		}
	}

	/**
	 * Parses a packager param element and initializes the appropriate fields.
	 * 
	 * @param node
	 *            The XML node representing the param element.
	 * 
	 * @throws SubmissionTargetException
	 *             if there are any parsing errors.
	 */
	protected void parsePackagerParameter(Node node)
			throws SubmissionTargetException
	{
		Node nameNode = node.getAttributes().getNamedItem("name");
		Node valueNode = node.getAttributes().getNamedItem("value");
		if (nameNode != null && valueNode != null)
			packagerParams.put(nameNode.getNodeValue(), valueNode
					.getNodeValue());
	}

	/**
	 * Parses a transport param element and initializes the appropriate fields.
	 * 
	 * @param node
	 *            The XML node representing the param element.
	 * 
	 * @throws SubmissionTargetException
	 *             if there are any parsing errors.
	 */
	protected void parseTransportParameter(Node node)
			throws SubmissionTargetException
	{
		Node nameNode = node.getAttributes().getNamedItem("name");
		Node valueNode = node.getAttributes().getNamedItem("value");
		if (nameNode != null && valueNode != null)
			transportParams.put(nameNode.getNodeValue(), valueNode
					.getNodeValue());
	}

	/**
	 * Parses a transport file-param element and initializes the appropriate
	 * fields.
	 * 
	 * @param node
	 *            The XML node representing the file-param element.
	 * 
	 * @throws SubmissionTargetException
	 *             if there are any parsing errors.
	 */
	protected void parseTransportFileParameter(Node node)
			throws SubmissionTargetException
	{
		Node nameNode = node.getAttributes().getNamedItem("name");
		Node valueNode = node.getAttributes().getNamedItem("value");
		if (nameNode != null && valueNode != null)
			transportParams.put("$file." + nameNode.getNodeValue(), valueNode
					.getNodeValue());
	}

	/**
	 * Parses the file pattern from an include, exclude, or required element and
	 * initializes the appropriate fields.
	 * 
	 * @param node
	 *            The XML node representing the file pattern element.
	 * 
	 * @throws SubmissionTargetException
	 *             if there are any parsing errors.
	 */
	protected String parseFilePattern(Node node)
			throws SubmissionTargetException
	{
		Node patternNode = node.getAttributes().getNamedItem("pattern");
		if (patternNode != null)
			return patternNode.getNodeValue();
		else
			return null;
	}

	/**
	 * Writes three spaces for each indentation level to the specified writer.
	 * 
	 * @param indentLevel
	 *            An integer specifying the indentation level of the element.
	 * @param writer
	 *            The writer that will store the XML code.
	 */
	protected void padToIndent(int indentLevel, PrintWriter writer)
	{
		for (int i = 0; i < indentLevel; i++)
			writer.print("   ");
	}

	/**
	 * Writes the given string to the writer, first converting any special XML
	 * characters (angle brackets, quotation mark, ampersand) to XML entities.
	 * 
	 * @param string
	 *            The string to convert and write.
	 * @param writer
	 *            The writer that will store the XML code.
	 */
	protected void writeXMLString(String string, PrintWriter writer)
	{
		for (int i = 0; i < string.length(); i++)
		{
			char ch = string.charAt(i);

			if (ch == '<')
				writer.print("&lt;");
			else if (ch == '>')
				writer.print("&gt;");
			else if (ch == '&')
				writer.print("&amp;");
			else if (ch == '"')
				writer.print("&quot;");
			else
				writer.print(ch);
		}
	}

	/**
	 * Writes the file patterns, transport, and packager elements for the
	 * current node to the specified writer.
	 * 
	 * @param indentLevel
	 *            An integer specifying the indentation level of the element.
	 * @param writer
	 *            The writer that will store the XML code.
	 */
	protected void writeSharedProperties(int indentLevel, PrintWriter writer)
	{
		// Write file patterns.
		writeFilePatterns(indentLevel, writer);

		// Write packager.
		writePackager(indentLevel, writer);

		// Write transport.
		writeTransport(indentLevel, writer);
	}

	/**
	 * Writes the file patterns for the current node to the specified writer.
	 * 
	 * @param indentLevel
	 *            An integer specifying the indentation level of the element.
	 * @param writer
	 *            The writer that will store the XML code.
	 */
	protected void writeFilePatterns(int indentLevel, PrintWriter writer)
	{
		if (includes.length > 0)
		{
			for (int i = 0; i < includes.length; i++)
			{
				String pattern = includes[i];
				writePatternTag(indentLevel, "include", pattern, writer);
			}
		}

		if (excludes.length > 0)
		{
			for (int i = 0; i < excludes.length; i++)
			{
				String pattern = excludes[i];
				writePatternTag(indentLevel, "exclude", pattern, writer);
			}
		}

		if (required.length > 0)
		{
			for (int i = 0; i < required.length; i++)
			{
				String pattern = required[i];
				writePatternTag(indentLevel, "required", pattern, writer);
			}
		}
	}

	/**
	 * Writes a single file pattern element to the specified writer.
	 * 
	 * @param indentLevel
	 *            An integer specifying the indentation level of the element.
	 * @param type
	 *            The element type name.
	 * @param value
	 *            The file pattern.
	 * @param writer
	 *            The writer that will store the XML code.
	 */
	protected void writePatternTag(int indentLevel, String type, String value,
			PrintWriter writer)
	{
		padToIndent(indentLevel, writer);
		writer.print("<");
		writer.print(type);
		writer.print(" pattern=\"");
		writeXMLString(value, writer);
		writer.println("\"/>");
	}

	/**
	 * Writes the transport element for the current node to the specified
	 * writer.
	 * 
	 * @param indentLevel
	 *            An integer specifying the indentation level of the element.
	 * @param writer
	 *            The writer that will store the XML code.
	 */
	protected void writeTransport(int indentLevel, PrintWriter writer)
	{
		if (transport != null)
		{
			padToIndent(indentLevel, writer);

			writer.print("<transport uri=\"");
			writeXMLString(transport, writer);
			writer.print("\"");

			if (transportParams.isEmpty())
			{
				writer.println("/>");
			}
			else
			{
				writer.println(">");

				Set paramSet = transportParams.entrySet();
				for (Iterator it = paramSet.iterator(); it.hasNext();)
				{
					Map.Entry entry = (Map.Entry) it.next();
					String name = (String) entry.getKey();
					String value = (String) entry.getValue();

					padToIndent(indentLevel + 1, writer);

					if (name.startsWith("$file."))
					{
						writer.print("<file-param name=\"");
						writeXMLString(name.substring(6), writer);
						writer.print("\" value=\"");
						writeXMLString(value, writer);
						writer.println("\"/>");
					}
					else
					{
						writer.print("<param name=\"");
						writeXMLString(name, writer);
						writer.print("\" value=\"");
						writeXMLString(value, writer);
						writer.println("\"/>");
					}
				}

				padToIndent(indentLevel, writer);
				writer.println("</transport>");
			}
		}
	}

	/**
	 * Writes the packager element for the current node to the specified writer.
	 * 
	 * @param indentLevel
	 *            An integer specifying the indentation level of the element.
	 * @param writer
	 *            The writer that will store the XML code.
	 */
	protected void writePackager(int indentLevel, PrintWriter writer)
	{
		if(packager.equals(DEFAULT_PACKAGER) &&
				(packagerParams == null || packagerParams.size() == 0))
		{
			return;
		}

		if (packager != null)
		{
			padToIndent(indentLevel, writer);

			writer.print("<packager id=\"");
			writeXMLString(packager, writer);
			writer.print("\"");

			if (packagerParams.isEmpty())
			{
				writer.println("/>");
			}
			else
			{
				writer.println(">");

				Set paramSet = packagerParams.entrySet();
				for (Iterator it = paramSet.iterator(); it.hasNext();)
				{
					Map.Entry entry = (Map.Entry) it.next();
					String name = (String) entry.getKey();
					String value = (String) entry.getValue();

					padToIndent(indentLevel + 1, writer);

					writer.print("<param name=\"");
					writeXMLString(name, writer);
					writer.print("\" value=\"");
					writeXMLString(value, writer);
					writer.println("\"/>");
				}

				padToIndent(indentLevel, writer);
				writer.println("</packager>");
			}
		}
	}

	/**
	 * Writes the children of the current node to the specified writer.
	 * 
	 * @param indentLevel
	 *            An integer specifying the indentation level of the element.
	 * @param writer
	 *            The writer that will store the XML code.
	 */
	protected void writeChildren(int indentLevel, PrintWriter writer)
	{
		for (int i = 0; i < children.length; i++)
		{
			ITarget obj = children[i];
			obj.writeToXML(writer, indentLevel);
		}
	}
}