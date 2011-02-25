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
package org.eclipse.cdt.managedbuilder.llvm.scannerconfig;

import org.eclipse.cdt.build.core.scannerconfig.CfgInfoContext;
import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.core.settings.model.ICProjectDescription;
import org.eclipse.cdt.make.core.scannerconfig.IScannerInfoCollector;
import org.eclipse.cdt.make.core.scannerconfig.InfoContext;
import org.eclipse.cdt.make.internal.core.scannerconfig.gnu.GCCScannerInfoConsoleParser;
import org.eclipse.cdt.make.internal.core.scannerconfig2.PerProjectSICollector;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.core.resources.IProject;

/**
 * Copy/pasted from org.eclipse.cdt.managedbuilder.internal.scannerconfig.ManagedGCCScannerInfoConsoleParser
 * because that class cannot be referenced to due to restrictions in the exported packages definition.
 * 
 * @author Leo Hippelainen
 * TODO: Javadoc comments
 */
@SuppressWarnings("restriction")
public class ManagedLlvmScannerInfoConsoleParser
	extends GCCScannerInfoConsoleParser {
	
	private Boolean fManagedBuildOnState;
	
	/**
	 * 
	 * 
	 * @param line String
	 * @return boolean
	 */
	public boolean processLine(String line) {
		if(isManagedBuildOn())
			return false;
		return super.processLine(line);
	}

	/**
	 * 
	 */
	public void shutdown() {
		if(!isManagedBuildOn()){
			super.shutdown();
		}
		fManagedBuildOnState = null;
	}

	/**
	 * 
	 * 
	 * @param project IProject
	 * @param collector IScannerInfoCollector
	 */
	public void startup(IProject project, IScannerInfoCollector collector) {
		if(isManagedBuildOn())
			return;
		super.startup(project, collector);
	}

	/**
	 * 
	 * 
	 * @return boolean
	 */
	protected boolean isManagedBuildOn(){
		if(fManagedBuildOnState == null)
			fManagedBuildOnState = Boolean.valueOf(doCalcManagedBuildOnState());
		return fManagedBuildOnState.booleanValue();
	}

	/**
	 * 
	 * 
	 * @return boolean
	 */
	protected boolean doCalcManagedBuildOnState(){
		IScannerInfoCollector cr = getCollector();
		InfoContext c;
		if(cr instanceof PerProjectSICollector){
			c = ((PerProjectSICollector)cr).getContext();
		} else {
			return false;
		}
		IProject project = c.getProject();
		ICProjectDescription des = CoreModel.getDefault().getProjectDescription(project, false);
		CfgInfoContext cc = CfgInfoContext.fromInfoContext(des, c);
		if(cc != null){
			IConfiguration cfg = cc.getConfiguration();
			return cfg.isManagedBuildOn();
		}
		return false;
	}
	
}
