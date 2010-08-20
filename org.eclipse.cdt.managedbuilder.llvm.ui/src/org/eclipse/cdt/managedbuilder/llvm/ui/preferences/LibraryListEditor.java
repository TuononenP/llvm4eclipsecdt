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
package org.eclipse.cdt.managedbuilder.llvm.ui.preferences;

import org.eclipse.cdt.managedbuilder.llvm.environment.LlvmEnvironmentVariableSupplier;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

/**
 * New implementation of LlvmListEditor.
 * Used to select a library file from the dialog.
 * 
 * @author Petri Tuononen
 */
public class LibraryListEditor extends LlvmListEditor {

	/**
	 * Constructor.
	 * 
	 * @param name
	 * @param labelText
	 * @param parent
	 */
	LibraryListEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
	}
	
	@Override
	/**
	 * Functionality for New button.
	 * Shows a browser dialog to select a file and returns that file.
	 */
	protected String getNewInputObject() {
		FileDialog dlg = new FileDialog(getShell());
		final Text text = new Text(getShell(), SWT.BORDER);
		dlg.setFilterPath(text.getText());
		dlg.setText("Browse a directory path");
		dlg.open();
		String file = dlg.getFileName();
		//remove white spaces
		file = file.trim();
		if (file != null && file.length()!=0) {
			//get all existing items in the list
			String[] existingItems = getList().getItems();
			//return null if duplicate item found
			for (String item : existingItems) {
				if (item.equalsIgnoreCase(file)) {
					return null;
				}
			}
			//add a new library to llvm preference store
			LlvmPreferenceStore.appendLibrary(file);
			//add a new library to llvm linker's option
//			LlvmPathUtil.addLlvmLib(file);
			//inform LLVM environment variable supplier that there has been a change
			LlvmEnvironmentVariableSupplier.invalidatePaths();
			return file;
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
        //remove a library from the llvm preference store
        LlvmPreferenceStore.removeLibrary(list.getItem(index).toString());
        //remove a library from llvm linker's option
//        LlvmPathUtil.removeLlvmLib(list.getItem(index).toString());
		//inform LLVM environment variable supplier that there has been a change
		LlvmEnvironmentVariableSupplier.invalidatePaths();
        if (index >= 0) {
            list.remove(index);
            selectionChanged();
        }
	}
	
}