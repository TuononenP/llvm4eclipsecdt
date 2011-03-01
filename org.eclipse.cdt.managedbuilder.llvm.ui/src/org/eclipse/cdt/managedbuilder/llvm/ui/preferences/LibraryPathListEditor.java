/*******************************************************************************
 * Copyright (c) 2010, 2011 Nokia Siemens Networks Oyj, Finland.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *      Nokia Siemens Networks - initial implementation
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
 * Used to select a library path from the dialog.
 * 
 * @author Petri Tuononen
 */
public class LibraryPathListEditor extends LlvmListEditor {

	/**
	 * Constructor.
	 * 
	 * @param name the name of the preference this field editor works on
	 * @param labelText the label text of the field editor
	 * @param parent the parent of the field editor's control
	 */
	LibraryPathListEditor(String name, String labelText, Composite parent) {
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
		dlg.setText("Browse a directory path");
		dlg.setMessage("Select a directory");
		String dir = dlg.open();
		//remove white spaces
		dir = dir.trim();
		if (dir != null && dir.length()!=0) {
			String[] existingItems = getList().getItems();
			if (existingItems.length>0) {
				//return null if duplicate item found
				for (String item : existingItems) {
					if (item.equalsIgnoreCase(dir)) {
						return null;
					}
				}					
			}
			//add a new library search path to LLVM preference store
			LlvmPreferenceStore.appendLibraryPath(dir);
			//add a new library path to LLVM linker's option
			LlvmToolOptionPathUtil.addLlvmLibSearchPath(dir);
			//inform LLVM environment variable supplier that there has been a change
			LlvmEnvironmentVariableSupplier.invalidatePaths();
			return dir;
		}  else {
			return null;
		}
	}

	@Override
	/**
	 * Removes the path from the list as well as from the Tool's Option.
	 */
	protected void removePressed() {
		List list = getList();
        setPresentsDefaultValue(false);
        int index = list.getSelectionIndex();
        //remove a library path from the LLVM preference store
        LlvmPreferenceStore.removeLibraryPath(list.getItem(index).toString());
        //remove a library path from LLVM linker's option
        LlvmToolOptionPathUtil.removeLlvmLibSearchPath(list.getItem(index).toString());
		//inform LLVM environment variable supplier that there has been a change
		LlvmEnvironmentVariableSupplier.invalidatePaths();
        if (index >= 0) {
            list.remove(index);
            selectionChanged();
        }
	}
	
}