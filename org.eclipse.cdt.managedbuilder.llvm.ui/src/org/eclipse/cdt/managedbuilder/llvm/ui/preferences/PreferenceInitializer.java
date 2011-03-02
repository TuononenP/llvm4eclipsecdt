/*******************************************************************************
 * Copyright (c) 2010, 2011 Nokia Siemens Networks Oyj, Finland.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Nokia Siemens Networks - initial implementation
 *      Leo Hippelainen - Initial implementation
 *******************************************************************************/
package org.eclipse.cdt.managedbuilder.llvm.ui.preferences;

import org.eclipse.cdt.managedbuilder.llvm.ui.LlvmUIPlugin;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Class used to initialize the default preference values.
 * 
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = LlvmUIPlugin.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.P_LLVM_PATH, Messages.PreferenceInitializer_0); 
		store.setDefault(PreferenceConstants.P_LLVM_INCLUDE_PATH, Messages.PreferenceInitializer_1); 
		store.setDefault(PreferenceConstants.P_LLVM_LIBRARY_PATH, Messages.PreferenceInitializer_2); 
		store.setDefault(PreferenceConstants.P_LLVM_LIBRARIES, Messages.PreferenceInitializer_3); 
	}

}
