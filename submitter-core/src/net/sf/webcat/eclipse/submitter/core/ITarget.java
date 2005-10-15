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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.operation.IRunnableContext;

import java.io.File;
import java.io.PrintWriter;
import java.util.Map;

import org.w3c.dom.Node;

/**
 * The interface for all definition objects in the system. The definition root
 * and all assignment groups and assignments implement this common interface.
 * It provides methods for traversing the definition tree as well as for
 * accessing inherited properties, such as included and excluded files.
 * 
 * @author Tony Allowatt (Virginia Tech Computer Science)
 */
public interface ITarget extends IAdaptable
{
	/**
	 * Returns the parent node to this node in the tree.
	 * 
	 * @return A SubmissionObject that represent this node's parent.
	 */
	ITarget parent();

	/**
	 * Returns the root of the tree that this object is contained in.
	 * 
	 * @return The root of the tree, or null if it could not be found.
	 */
	ITargetRoot getRoot();

	/**
	 * Gets all of the included file patterns for this node.
	 * 
	 * @param context The context in which to run the operation, if
	 *          delay-loading is required.
	 * 
	 * @return An array of Strings that represent the file patterns.
	 * 
	 * @throws SubmissionTargetException
	 *         if an error occurs while delay-loading the node.
	 */
	String[] getIncludes(IRunnableContext context) throws SubmissionTargetException;

	/**
	 * Sets the included file patterns for this node.
	 * 
	 * @param array An array of Strings that represent the file patterns.
	 */
	void setIncludes(String[] array);

	/**
	 * Gets all of the excluded file patterns for this node.
	 * 
	 * @param context The context in which to run the operation, if
	 *          delay-loading is required.
	 * 
	 * @return An array of Strings that represent the file patterns.
	 * 
	 * @throws SubmissionTargetException
	 *         if an error occurs while delay-loading the node.
	 */
	String[] getExcludes(IRunnableContext context) throws SubmissionTargetException;
	
	/**
	 * Sets the excluded file patterns for this node.
	 * 
	 * @param array An array of Strings that represent the file patterns.
	 */
	void setExcludes(String[] array);

	/**
	 * Gets all of the required file patterns for this node.
	 * 
	 * @param context The context in which to run the operation, if
	 *          delay-loading is required.
	 * 
	 * @return An array of Strings that represent the file patterns.
	 * 
	 * @throws SubmissionTargetException
	 *         if an error occurs while delay-loading the node.
	 */
	String[] getRequired(IRunnableContext context) throws SubmissionTargetException;
	
	/**
	 * Sets the required file patterns for this node.
	 * 
	 * @param array An array of Strings that represent the file patterns.
	 */
	void setRequired(String[] array);

	/**
	 * Recursively walks up the tree from the current node and collects a list
	 * of all the required file patterns for a submission at this level.
	 * 
	 * @param context The context in which to run the operation, if
	 *          delay-loading is required.
	 * 
	 * @return An array of Strings representing all the required files.
	 * 
	 * @throws SubmissionTargetException
	 *         if an error occurs while delay-loading the node.
	 */
	String[] getAllRequired(IRunnableContext context) throws SubmissionTargetException;

	/**
	 * Returns the transport URI for submission targets at this level in the
	 * tree.  This function walks up the tree to find an inherited transport,
	 * if necessary.
	 * 
	 * @param context The context in which to run the operation, if
	 *          delay-loading is required.
	 * 
	 * @return A String containing the transport URI.
	 * 
	 * @throws SubmissionTargetException
	 *         if an error occurs while delay-loading the node.
	 */
	String getTransport(IRunnableContext context) throws SubmissionTargetException;

	/**
	 * Returns the transport URI for submission targets at this level in the
	 * tree.  This function does not walk up the tree to find an inherited
	 * transport--it returns the transport specified for this node only.
	 * 
	 * @param context The context in which to run the operation, if
	 *          delay-loading is required.
	 * 
	 * @return A String containing the transport URI.
	 * 
	 * @throws SubmissionTargetException
	 *         if an error occurs while delay-loading the node.
	 */
	String getLocalTransport(IRunnableContext context) throws SubmissionTargetException;

	/**
	 * Sets the transport URI for this node.
	 * 
	 * @param uri A String containing the transport URI.
	 */
	void setTransport(String uri);

	/**
	 * Gets a map containing the transport parameter name/value pairs for this
	 * node.  This function walks up the tree to find an inherited transport,
	 * if necessary.
	 * 
	 * @param context The context in which to run the operation, if
	 *          delay-loading is required.
	 * 
	 * @return A Map containing the parameter names and values.
	 * 
	 * @throws SubmissionTargetException
	 *         if an error occurs while delay-loading the node.
	 */
	Map getTransportParams(IRunnableContext context) throws SubmissionTargetException;

	/**
	 * Gets a map containing the transport parameter name/value pairs for this
	 * node.  This function does not walk up the tree to find an inherited
	 * transport--it returns the transport specified for this node only.
	 * 
	 * @param context The context in which to run the operation, if
	 *          delay-loading is required.
	 * 
	 * @return A Map containing the parameter names and values.
	 * 
	 * @throws SubmissionTargetException
	 *         if an error occurs while delay-loading the node.
	 */
	Map getLocalTransportParams(IRunnableContext context) throws SubmissionTargetException;

	/**
	 * Sets the transport parameter name/value pairs for this node.
	 * 
	 * @param params A Map containing the parameter names and values.
	 */
	void setTransportParams(Map params);

	/**
	 * Returns the class name of the packager used to submit the project.
	 * This function walks up the tree to find an inherited packager, if
	 * necessary.
	 * 
	 * @param context The context in which to run the operation, if
	 *          delay-loading is required.
	 * 
	 * @return A String containing the packager class name.
	 * 
	 * @throws SubmissionTargetException
	 *         if an error occurs while delay-loading the node.
	 */
	String getPackager(IRunnableContext context) throws SubmissionTargetException;

	/**
	 * Returns the class name of the packager used to submit the project.
	 * This function does not walk up the tree to find an inherited
	 * packager--it returns the packager specified for this node only.
	 * 
	 * @param context The context in which to run the operation, if
	 *          delay-loading is required.
	 * 
	 * @return A String containing the packager class name.
	 * 
	 * @throws SubmissionTargetException
	 *         if an error occurs while delay-loading the node.
	 */
	String getLocalPackager(IRunnableContext context) throws SubmissionTargetException;

	/**
	 * Sets the packager identifier for this node.
	 * 
	 * @param id A String containing the unique identifier of a packager.
	 */
	void setPackager(String id);

	/**
	 * Gets a map containing the packager parameter name/value pairs for this
	 * node.
	 * 
	 * @param context The context in which to run the operation, if
	 *          delay-loading is required.
	 * 
	 * @return A Map containing the packager parameter names and values.
	 * 
	 * @throws SubmissionTargetException
	 *         if an error occurs while delay-loading the node.
	 */
	Map getPackagerParams(IRunnableContext context) throws SubmissionTargetException;

	/**
	 * Gets a map containing the packager parameter name/value pairs for this
	 * node.  This function does not walk up the tree to find an inherited
	 * packager--it returns the transport specified for this node only.
	 * 
	 * @param context The context in which to run the operation, if
	 *          delay-loading is required.
	 * 
	 * @return A Map containing the parameter names and values.
	 * 
	 * @throws SubmissionTargetException
	 *         if an error occurs while delay-loading the node.
	 */
	Map getLocalPackagerParams(IRunnableContext context) throws SubmissionTargetException;

	/**
	 * Sets the packager parameter name/value pairs for this node.
	 * 
	 * @param params A Map containing the parameter names and values.
	 */
	void setPackagerParams(Map params);

	/**
	 * Gets the children of this node.
	 * 
	 * @param context The context in which to run the operation, if
	 *          delay-loading is required.
	 * 
	 * @return An array of IDefinitionObjects that represent the children of
	 *         the node.
	 * 
	 * @throws SubmissionTargetException
	 *         if an error occurs while delay-loading the node.
	 */
	ITarget[] getChildren(IRunnableContext context) throws SubmissionTargetException;

	/**
	 * Sets the children of this node.
	 * 
	 * @param array An array of IDefinitionObjects that represent the new
	 *              children of the node.
	 */
	void setChildren(ITarget[] array);

	/**
	 * Recursively walks up the submission target tree to determine if the
	 * specified file should be excluded from the submission.
	 * 
	 * @param context The context in which to run the operation, if
	 *          delay-loading is required.
	 * 
	 * @param file The File to check for exclusion.
	 * 
	 * @return true if the file should be excluded; otherwise, false.
	 * 
	 * @throws SubmissionTargetException
	 *         if an error occurs while delay-loading the node.
	 */
	boolean isFileExcluded(File file, IRunnableContext context) throws SubmissionTargetException;

	/**
	 * Overridden by derived classes to specify whether the node may contain
	 * children.
	 * 
	 * @return true if the node may contain children; otherwise, false.
	 */
	boolean isContainer();

	/**
	 * Overridden by derived classes to specify whether the node should be
	 * displayed at the same level as its parent, or if it should be nested at a
	 * lower level.
	 * 
	 * @return true if the node should be nested at a lower level in the tree;
	 *         false if it should be displayed at the same level as its parent.
	 */
	boolean isNested();

	/**
	 * Overridden by derived classes to specify whether an action can be taken
	 * on this node. In a wizard, for example, this would enable the Next/Finish
	 * button so the user can continue with the submission.
	 * 
	 * @return true if the node is actionable; otherwise, false.
	 */
	boolean isActionable();

	/**
	 * Overridden by derived classes to specify whether the node has been
	 * loaded into local memory.  This is always true for most nodes except
	 * imported groups, which return true only if the external XML file
	 * has already been processed.
	 * 
	 * @return true if the node is local or if it has been delay-loaded;
	 *         false if the node is an imported group that has not let been
	 *         expanded.
	 */
	boolean isLoaded();

	/**
	 * Parses the specified XML node and builds a subtree from the data.
	 * 
	 * @param node
	 *            The XML document node to parse from.
	 * @throws SubmissionTargetException
	 *             if any errors occurred during parsing.
	 */
	void parse(Node node) throws SubmissionTargetException;

	/**
	 * Generates the XML code for this element and its children.
	 *
	 * @param writer
	 *            The writer that will store the XML code.  
	 * @param indentLevel
	 *            An integer specifying the indentation level of the element.
	 */
	void writeToXML(PrintWriter writer, int indentLevel);
}