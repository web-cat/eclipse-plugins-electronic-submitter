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
import java.util.Vector;

import net.sf.webcat.eclipse.submitter.core.ITarget;
import net.sf.webcat.eclipse.submitter.core.ITargetChangedEvent;
import net.sf.webcat.eclipse.submitter.core.ITargetChangedListener;
import net.sf.webcat.eclipse.submitter.core.ITargetRoot;
import net.sf.webcat.eclipse.submitter.core.SubmissionTargetException;
import net.sf.webcat.eclipse.submitter.core.TargetChangedEvent;

import org.w3c.dom.Node;

/**
 * Represents the root of the submission definition tree. The root contains
 * settings that are inherited down throughout the entire tree.
 * 
 * @author Tony Allevato (Virginia Tech Computer Science)
 */
public class TargetRoot extends AbstractTarget implements ITargetRoot
{
	// === Methods ============================================================
	
	// ------------------------------------------------------------------------
	/**
	 * Creates a new root node.
	 */
	public TargetRoot()
	{
		super(null);

		changeListeners = new Vector<ITargetChangedListener>();
	}


	// ------------------------------------------------------------------------
	public boolean isContainer()
	{
		return true;
	}


	// ------------------------------------------------------------------------
	public boolean isActionable()
	{
		return false;
	}


	// ------------------------------------------------------------------------
	public boolean isNested()
	{
		return false;
	}


	// ------------------------------------------------------------------------
	public boolean isLoaded()
	{
		return true;
	}


	// ------------------------------------------------------------------------
	public void addTargetChangedListener(ITargetChangedListener listener)
	{
		changeListeners.add(listener);
	}


	// ------------------------------------------------------------------------
	public void fireTargetChanged(ITargetChangedEvent event)
	{
		for(int i = 0; i < changeListeners.size(); i++)
		{
			ITargetChangedListener listener = changeListeners.get(i);
			listener.targetChanged(event);
		}
	}


	// ------------------------------------------------------------------------
	public void fireTargetObjectChanged(ITarget object, String property,
	        Object oldValue, Object newValue)
	{
		TargetChangedEvent event = new TargetChangedEvent(this, object,
		        property, oldValue, newValue);
		fireTargetChanged(event);
	}


	// ------------------------------------------------------------------------
	public void removeTargetChangedListener(ITargetChangedListener listener)
	{
		changeListeners.remove(listener);
	}


	// ------------------------------------------------------------------------
	public void parse(Node node) throws SubmissionTargetException
	{
		String nodeName = node.getLocalName();

		if("submission-targets".equals(nodeName))
		{
			parseSubmissionTargets(node);
		}
	}


	// ------------------------------------------------------------------------
	private void parseSubmissionTargets(Node parentNode)
	        throws SubmissionTargetException
	{
		Node node = parentNode.getFirstChild();

		ArrayList<String> includes = new ArrayList<String>();
		ArrayList<String> excludes = new ArrayList<String>();
		ArrayList<String> required = new ArrayList<String>();
		ArrayList<ITarget> children = new ArrayList<ITarget>();

		while(node != null)
		{
			String nodeName = node.getLocalName();

			if("filter-ambiguity".equals(nodeName))
			{
				parseFilterAmbiguity(node);
			}
			else if("include".equals(nodeName))
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
			else if("assignment-group".equals(nodeName))
			{
				children.add(parseAssignmentGroup(node));
			}
			else if("import-group".equals(nodeName))
			{
				children.add(parseImportGroup(node));
			}
			else if("assignment".equals(nodeName))
			{
				children.add(parseAssignment(node));
			}

			node = node.getNextSibling();
		}

		setIncludes(includes);
		setExcludes(excludes);
		setRequired(required);
		setChildren(children);
	}


	// ------------------------------------------------------------------------
	private ITarget parseAssignmentGroup(Node node)
	        throws SubmissionTargetException
	{
		TargetAssignmentGroup group = new TargetAssignmentGroup(this);
		group.parse(node);
		return group;
	}


	// ------------------------------------------------------------------------
	private ITarget parseImportGroup(Node node)
	        throws SubmissionTargetException
	{
		TargetImportGroup group = new TargetImportGroup(this);
		group.parse(node);
		return group;
	}


	// ------------------------------------------------------------------------
	private ITarget parseAssignment(Node node) throws SubmissionTargetException
	{
		TargetAssignment assignment = new TargetAssignment(this);
		assignment.parse(node);
		return assignment;
	}


	// ------------------------------------------------------------------------
	public void writeToXML(PrintWriter writer, int indentLevel)
	{
		// Write opening tag.
		padToIndent(indentLevel, writer);
		writer
		        .println("<submission-targets xmlns=\"http://web-cat.cs.vt.edu/submissionTargets\">");

		writeSharedProperties(indentLevel + 1, writer);

		writeChildren(indentLevel + 1, writer);

		// Write closing tag.
		padToIndent(indentLevel, writer);
		writer.println("</submission-targets>");
	}


	// === Instance Variables =================================================

	/**
	 * A Vector that contains ITargetChangedListener objects that are registered
	 * to be notified when a submission target in this tree changes.
	 */
	private Vector<ITargetChangedListener> changeListeners;
}
