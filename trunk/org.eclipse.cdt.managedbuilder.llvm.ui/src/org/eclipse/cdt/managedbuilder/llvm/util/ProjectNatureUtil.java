/*******************************************************************************
 * Copyright (c) 2010 Nokia Siemens Networks Oyj, Finland.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Nokia Siemens Networks - initial implementation
 *******************************************************************************/
package org.eclipse.cdt.managedbuilder.llvm.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;

public class ProjectNatureUtil {

	/**
	 * Set LLVM nature to all projects in the workspace.
	 */
	public static void addProjectNature() {
		//get all projects in the workspace
		IProject[] projects = LlvmToolOptionPathUtil.getProjectsInWorkspace();
		//first project in the workspace
		IProject project = projects[0];
		try {
		      IProjectDescription description = project.getDescription();
		      String[] natures = description.getNatureIds();
		      String[] newNatures = new String[natures.length + 1];
		      System.arraycopy(natures, 0, newNatures, 0, natures.length);
		      newNatures[natures.length] = "llvmNature";
		      description.setNatureIds(newNatures);
		      project.setDescription(description, null);
		   } catch (CoreException e) {
			   //log error
		   }
	}
	
}
