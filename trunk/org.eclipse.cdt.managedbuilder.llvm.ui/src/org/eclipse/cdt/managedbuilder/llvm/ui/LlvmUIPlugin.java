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
 *      Petri Tuononen - Initial implementation
 *******************************************************************************/
package org.eclipse.cdt.managedbuilder.llvm.ui;

import java.io.IOException;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 * The main plugin class to be used in the desktop.
 * 
 * This class is not intended to be subclassed by clients.
 * This class is not intended to be instantiated by clients.
 */
public class LlvmUIPlugin extends AbstractUIPlugin { 

	//The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.cdt.managedbuilder.llvm.ui";

	//The shared instance
	private static LlvmUIPlugin plugin;
	
	//Resource bundle
	private ResourceBundle resourceBundle;
	
	//Name for the properties file
	private final static String PROPERTIES = "plugin.properties";

	//Property Resource bundle
	private PropertyResourceBundle properties;
	
	/**
	 * Constructor.
	 */
	public LlvmUIPlugin() {
		super();
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
		resourceBundle = null;
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static LlvmUIPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = LlvmUIPlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		try {
			if (resourceBundle == null)
				resourceBundle = ResourceBundle.getBundle(this.getClass().getName()+ "Resources"); //$NON-NLS-1$
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
		return resourceBundle;
	}

	/**
	 * Log error.
	 * 
	 * @param e
	 */
	public void log(Throwable e) {
		log(new Status(IStatus.ERROR, PLUGIN_ID, IStatus.ERROR, "Error", e)); //$NON-NLS-1$
	}

	/**
	 * Log status.
	 * 
	 * @param status
	 */
	public void log(IStatus status) {
		getLog().log(status);
	}
	
	/**
	 * Get plugin.properties
	 * 
	 * @return PropertyResourceBundle
	 */
	public PropertyResourceBundle getProperties(){
		if (properties == null){
			try {
				properties = new PropertyResourceBundle(
						FileLocator.openStream(this.getBundle(),
								new Path(PROPERTIES),false));
			} catch (IOException e) {
				//log error
				e.getMessage();
			}
		}
		return properties;
	}	  
	
	/**
	 * Get String from the plugin.properties file
	 * 
	 * @param var Variable name wanted as a String e.g. "ToolName.assembler.llvm"
	 * @return String e.g. LLVM assembler
	 */
	public static String getPropertyString(String var) {
		PropertyResourceBundle properties = LlvmUIPlugin.getDefault().getProperties();
		return properties.getString(var);
	}

}
