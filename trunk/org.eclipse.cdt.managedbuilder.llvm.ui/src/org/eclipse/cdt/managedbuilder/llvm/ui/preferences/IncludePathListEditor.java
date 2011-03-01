/*******************************************************************************
 * Copyright (c) 2010, 2011 Nokia Siemens Networks Oyj, Finland.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Nokia Siemens Networks - initial implementation
 *      Petri Tuononen - Initial implementation
 *******************************************************************************/
package org.eclipse.cdt.managedbuilder.llvm.ui.preferences;

import org.eclipse.cdt.managedbuilder.llvm.ui.LlvmEnvironmentVariableSupplier;
import org.eclipse.cdt.managedbuilder.llvm.util.LlvmToolOptionPathUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

/**
 * New implementation of LlvmListEditor.
 * Used to select an include path from the dialog.
 * 
 */
public class IncludePathListEditor extends LlvmListEditor {

	/**
	 * Constructor.
	 * 
	 * @param name the name of the preference this field editor works on
	 * @param labelText the label text of the field editor
	 * @param parent the parent of the field editor's control
	 */
	IncludePathListEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
	}
	
	@Override
	/**
	 * Functionality for New button.
	 * Shows a browser dialog to select a directory and returns that directory path.
	 */
	protected String getNewInputObject() {
		DirectoryDialog dlg = new DirectoryDialog(getShell());
		final Text text = new Text(getShell(), SWT.BORDER);
		dlg.setFilterPath(text.getText());
		dlg.setText("Browse a directory path"); //$NON-NLS-1$
		dlg.setMessage("Select a directory"); //$NON-NLS-1$
		String dir = dlg.open();
		//remove white spaces
		dir = dir.trim();
		if (dir != null && dir.length()!=0) {
			//get all existing items in the list
			String[] existingItems = getList().getItems();
			//check that the list doesn't already contain the added item
			if (existingItems.length>0) {
				//return null if duplicate item found
				for (String item : existingItems) {
					if (item.equalsIgnoreCase(dir)) {
						return null;
					}
				}					
			}
			//add a new include path to LLVM preference store
			LlvmPreferenceStore.appendIncludePath(dir);
			//add a new include path to LLVM assembler's option
			LlvmToolOptionPathUtil.addLlvmIncludePath(dir);
			//inform LLVM environment variable supplier that there has been a change
			LlvmEnvironmentVariableSupplier.invalidatePaths();
			return dir;
		}
		return null;
	}

	@Override
	/**
	 * Removes the path from the list as well as from the Tool's Option.
	 */
	protected void removePressed() {
		List incList = getList();
        setPresentsDefaultValue(false);
        int index = incList.getSelectionIndex();
        //remove an include path from the LLVM preference store
        LlvmPreferenceStore.removeIncludePath(incList.getItem(index).toString());
        //remove an include path from the LLVM assembler's option
        LlvmToolOptionPathUtil.removeLlvmIncludePath(incList.getItem(index).toString());
		//inform LLVM environment variable supplier that there has been a change
		LlvmEnvironmentVariableSupplier.invalidatePaths();
        if (index >= 0) {
        	incList.remove(index);
            selectionChanged();
        }
	}
	
}