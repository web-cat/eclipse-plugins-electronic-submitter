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

import java.io.PrintWriter;
import java.net.URL;
import java.util.Map;

import net.sf.webcat.eclipse.submitter.core.IHideableTarget;
import net.sf.webcat.eclipse.submitter.core.INameableTarget;
import net.sf.webcat.eclipse.submitter.core.ITarget;
import net.sf.webcat.eclipse.submitter.core.ITargetImportGroup;
import net.sf.webcat.eclipse.submitter.core.SubmissionTargetException;
import net.sf.webcat.eclipse.submitter.core.TargetParseException;

import org.eclipse.jface.operation.IRunnableContext;
import org.w3c.dom.Node;

/**
 * Represents an imported group in the submission target tree.  An imported
 * group refers to an external XML submission target file that will be merged
 * with the tree at the location of the import group node.  The imported group
 * must have a name and a valid URL to the external file.
 *  
 * @author Tony Allowatt (Virginia Tech Computer Science)
 */
public class TargetImportGroup extends AbstractTarget implements
		ITargetImportGroup
{
	/**
	 * The name of the assignment group.
	 */
	private String name;

	/**
	 * The URL to the external submission target file for this group.
	 */
	private String ref;

	/**
	 * Indicates whether or not the assignment group should be hidden in the
	 * user-interface.
	 */
	private boolean hidden;

	/**
	 * Indicates whether the group has been loaded from the external file.
	 */
	private boolean loaded;

	/**
	 * An adapter class that manages the INameableDefinition interface for
	 * this assignment group.
	 */
	private class NameableDefinitionAdapter implements INameableTarget
	{
		private TargetImportGroup asmt;

		public NameableDefinitionAdapter(TargetImportGroup asmt)
		{
			this.asmt = asmt;
		}

		public String getName()
		{
			return asmt.getName();
		}
		
		public void setName(String value)
		{
			asmt.setName(value);
		}
	}

	/**
	 * An adapter class that manages the IHideableDefinition interface for
	 * this assignment group.
	 */
	private class HideableDefinitionAdapter implements IHideableTarget
	{
		private TargetImportGroup asmt;

		public HideableDefinitionAdapter(TargetImportGroup asmt)
		{
			this.asmt = asmt;
		}

		public boolean isHidden()
		{
			return asmt.isHidden();
		}
		
		public void setHidden(boolean value)
		{
			asmt.setHidden(value);
		}
	}

	/**
	 * Creates a new imported group node with the specified parent.
	 * 
	 * @param parent The node that will be assigned the parent of the new
	 *               node.
	 */
	public TargetImportGroup(AbstractTarget parent)
	{
		super(parent);

		loaded = false;
	}

	public Object getAdapter(Class adapterClass)
	{
		if (adapterClass.equals(INameableTarget.class))
			return new NameableDefinitionAdapter(this);
		else if (adapterClass.equals(IHideableTarget.class))
			return new HideableDefinitionAdapter(this);
		else
			return super.getAdapter(adapterClass);
	}

	public boolean isContainer()
	{
		return true;
	}

	public boolean isNested()
	{
		return true;
	}

	public boolean isLoaded()
	{
		return loaded;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String value)
	{
		String oldValue = name;
		name = value;
		
		tryFireTargetObjectChanged("name", oldValue, name);
	}

	public String getHref()
	{
		return ref;
	}

	public void setHref(String url)
	{
		String oldValue = ref;
		ref = url;

		tryFireTargetObjectChanged("ref", oldValue, ref);
	}

	public boolean isHidden()
	{
		return hidden;
	}
	
	public void setHidden(boolean value)
	{
		boolean oldValue = hidden;
		hidden = value;

		tryFireTargetObjectChanged(
				"hidden", new Boolean(oldValue), new Boolean(hidden));
	}

	public boolean isActionable()
	{
		return false;
	}

	public String[] getIncludes(IRunnableContext context)
			throws SubmissionTargetException
	{
		if (!loaded)
			loadImportedDefinitions(context);
		return super.getIncludes(context);
	}

	public String[] getExcludes(IRunnableContext context)
			throws SubmissionTargetException
	{
		if (!loaded)
			loadImportedDefinitions(context);
		return super.getExcludes(context);
	}

	public String[] getRequired(IRunnableContext context)
			throws SubmissionTargetException
	{
		if (!loaded)
			loadImportedDefinitions(context);
		return super.getRequired(context);
	}

	public String getTransport(IRunnableContext context)
			throws SubmissionTargetException
	{
		if (!loaded)
			loadImportedDefinitions(context);
		return super.getTransport(context);
	}

	public Map getTransportParams(IRunnableContext context)
			throws SubmissionTargetException
	{
		if (!loaded)
			loadImportedDefinitions(context);
		return super.getTransportParams(context);
	}

	public String getPackager(IRunnableContext context)
			throws SubmissionTargetException
	{
		if (!loaded)
			loadImportedDefinitions(context);
		return super.getPackager(context);
	}

	public Map getPackagerParams(IRunnableContext context)
			throws SubmissionTargetException
	{
		if (!loaded)
			loadImportedDefinitions(context);
		return super.getPackagerParams(context);
	}

	public ITarget[] getChildren(IRunnableContext context)
			throws SubmissionTargetException
	{
		if (!loaded)
			loadImportedDefinitions(context);
		return super.getChildren(context);
	}

	public void parse(Node parentNode) throws SubmissionTargetException
	{
		Node nameNode = parentNode.getAttributes().getNamedItem("name");
		Node refNode = parentNode.getAttributes().getNamedItem("href");
		Node hiddenNode = parentNode.getAttributes().getNamedItem("hidden");

		String hiddenString = null;

		if (nameNode != null)
			name = nameNode.getNodeValue();

		if (refNode != null)
			ref = refNode.getNodeValue();

		if (hiddenNode != null)
			hiddenString = hiddenNode.getNodeValue();

		hidden = new Boolean(hiddenString).booleanValue();
	}

	private void loadImportedDefinitions(IRunnableContext context)
			throws SubmissionTargetException
	{
		SubmissionEngine engine = new SubmissionEngine();

		try
		{
			engine.openDefinitions(new URL(ref), context);
			ITarget root = engine.getRoot();

			copyFrom(root, context);

			loaded = true;
		}
		catch (TargetParseException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new SubmissionTargetException(e);
		}
	}

	public void writeToXML(PrintWriter writer, int indentLevel)
	{
		// Write opening tag.
		padToIndent(indentLevel, writer);
		writer.print("<import-group name=\"");
		writeXMLString(name, writer);
		writer.print("\"");

		if (hidden)
			writer.print(" hidden=\"true\"");

		writer.print(" href=\"");
		writeXMLString(ref.toString(), writer);
		writer.println("\"/>");
	}
}
