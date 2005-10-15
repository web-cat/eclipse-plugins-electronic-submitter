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
import java.util.ArrayList;

import net.sf.webcat.eclipse.submitter.core.IHideableTarget;
import net.sf.webcat.eclipse.submitter.core.INameableTarget;
import net.sf.webcat.eclipse.submitter.core.ITargetAssignment;
import net.sf.webcat.eclipse.submitter.core.SubmissionTargetException;

import org.w3c.dom.Node;

/**
 * Represents a single assignment in the submission definition tree. An
 * assignment is an actionable object to which projects can be submitted.
 * 
 * @author Tony Allowatt (Virginia Tech Computer Science)
 */
public class TargetAssignment extends AbstractTarget
	implements ITargetAssignment
{
	/**
	 * The name of the assignment.
	 */
	private String name;

	/**
	 * Indicates whether or not the assignment should be hidden in the
	 * user-interface.
	 */
	private boolean hidden;

	/**
	 * An adapter class that manages the INameableDefinition interface for
	 * this assignment.
	 */
	private class NameableDefinitionAdapter implements INameableTarget
	{
		private TargetAssignment asmt;

		public NameableDefinitionAdapter(TargetAssignment asmt)
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
	 * this assignment.
	 */
	private class HideableDefinitionAdapter implements IHideableTarget
	{
		private TargetAssignment asmt;

		public HideableDefinitionAdapter(TargetAssignment asmt)
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
	 * Creates a new assignment node with the specified parent.
	 * 
	 * @param parent The node that will be assigned the parent of the new
	 *               node.
	 */
	public TargetAssignment(AbstractTarget parent)
	{
		super(parent);
	}

	public Object getAdapter(Class adapterClass)
	{
		if(adapterClass.equals(INameableTarget.class))
			return new NameableDefinitionAdapter(this);
		else if(adapterClass.equals(IHideableTarget.class))
			return new HideableDefinitionAdapter(this);
		else
			return super.getAdapter(adapterClass);
	}

	public boolean isContainer()
	{
		return false;
	}

	public boolean isActionable()
	{
		return true;
	}

	public boolean isNested()
	{
		return true;
	}
	
	public boolean isLoaded()
	{
		return true;
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

	public void parse(Node parentNode) throws SubmissionTargetException
	{
		Node nameNode = parentNode.getAttributes().getNamedItem("name");
		Node hiddenNode = parentNode.getAttributes().getNamedItem("hidden");

		String hiddenString = null;

		if(nameNode != null)
			name = nameNode.getNodeValue();
		if(hiddenNode != null)
			hiddenString = hiddenNode.getNodeValue();

		hidden = new Boolean(hiddenString).booleanValue();

		Node node = parentNode.getFirstChild();

		ArrayList includes = new ArrayList();
		ArrayList excludes = new ArrayList();
		ArrayList required = new ArrayList();

		while(node != null)
		{
			String nodeName = node.getLocalName();

			if("include".equals(nodeName))
			{
				includes.add(parseFilePattern(node));
			}
			else if("exclude".equals(nodeName))
			{
				excludes.add(parseFilePattern(node));
			}
			else if("required".equals(nodeName))
			{
				required.add(parseFilePattern(node));
			}
			else if("transport".equals(nodeName))
			{
				parseTransport(node);
			}
			else if("packager".equals(nodeName))
			{
				parsePackager(node);
			}

			node = node.getNextSibling();
		}

		setIncludes(includes);
		setExcludes(excludes);
		setRequired(required);
	}

	public void writeToXML(PrintWriter writer, int indentLevel)
	{
		// Write opening tag.
		padToIndent(indentLevel, writer);
		writer.print("<assignment name=\"");
		writeXMLString(name, writer);
		writer.print("\"");
		
		if(hidden)
			writer.print(" hidden=\"true\"");
		
		writer.println(">");

		writeSharedProperties(indentLevel + 1, writer);

		// Write closing tag.
		padToIndent(indentLevel, writer);
		writer.println("</assignment>");
	}
}
