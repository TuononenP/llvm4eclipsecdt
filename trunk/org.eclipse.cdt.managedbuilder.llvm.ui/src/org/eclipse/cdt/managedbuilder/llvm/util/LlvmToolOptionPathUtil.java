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
package org.eclipse.cdt.managedbuilder.llvm.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IManagedBuildInfo;
import org.eclipse.cdt.managedbuilder.core.IManagedProject;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.ITool;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;

/**
 * The main purpose of this class is to add include paths and libraries and library search paths
 * for LLVM assembler and linker Tools which are added in Preferences->LLVM to all projects
 * and build configurations that use LLVM ToolChain. Values added in Preferences->LLVM will
 * show in Project->Properties->C/C++ General->Paths and Symbols tabs.
 * 
 */
public class LlvmToolOptionPathUtil {
	
	//tool input extensions
	private static final String assemblerInputType = "ll"; //$NON-NLS-1$
	private static final String linkerInputType = "bc"; //$NON-NLS-1$
	//tool option values
	public static final int INCLUDE = 1;
	public static final int LIB = 2;
	public static final int LIB_PATH = 3;
	
	/**
	 * Adds new include path to LLVM Assembler's Include path option for every project
	 * in the workspace that use LLVM Toolchain and for for every build configuration. 
	 * 
	 * @param includePath Include path for LLVM assembler's Include Option 
	 */
	public static void addLlvmIncludePath(String includePath) {
		addPathToToolOption(includePath, INCLUDE);
	}

	/**
	 * Removes an include path from LLVM Assembler's Include path option for every project
	 * in the workspace that use LLVM Toolchain and for for every build configuration. 
	 * 
	 * @param includePath Include path for LLVM assembler's Include Option 
	 */
	public static void removeLlvmIncludePath(String includePath) {
		removePathFromToolOption(includePath, INCLUDE);
	}
	
	/**
	 * Adds a new Library to LLVM linker's Libraries Option for every project
	 * in the workspace that use LLVM Toolchain and for for every build configuration.
	 * 
	 * @param lib Library name for the LLVM linker's Libraries Option
	 */
	public static void addLlvmLib(String lib) {
		addPathToToolOption(lib, LIB);
	}

	/**
	 * Removes a Library to LLVM linker's Libraries Option for every project
	 * in the workspace that use LLVM Toolchain and for for every build configuration.
	 * 
	 * @param lib Library name for the LLVM linker's Libraries Option
	 */
	public static void removeLlvmLib(String lib) {
		removePathFromToolOption(lib, LIB);
	}
	
	/**
	 * Adds a new Library search path directory to LLVM linker's Library search path Option
	 * for every project in the workspace that use LLVM Toolchain and for for every
	 * build configuration.
	 * 
	 * @param libDir Library search path directory for LLVM linker's Library search path Option
	 */
	public static void addLlvmLibSearchPath(String libDir) {
		addPathToToolOption(libDir, LIB_PATH);
	}

	/**
	 * Removes a Library search path directory from LLVM linker's Library search path Option
	 * for every project in the workspace that use LLVM Toolchain and for for every
	 * build configuration.
	 * 
	 * @param libDir Library search path directory for LLVM linker's Library search path Option
	 */	
	public static void removeLlvmLibSearchPath(String libDir) {
		removePathFromToolOption(libDir, LIB_PATH);
	}
	
	/**
	 * Adds a path to Tool option.
	 * 
	 * @param path Path to add to Tool option
	 * @param var Tool option's value
	 */
	private static void addPathToToolOption(String path, int var) {
		//check if the given path exists
		if (pathExists(path) || var==2) {
			boolean success = false;
			//get all projects in the workspace
			IProject[] projects = getProjectsInWorkspace();
			IConfiguration[] configs;
			for (IProject proj : projects) {
				//get all build configurations of the IProject
				configs = getAllBuildConfigs(proj);
				//if build configurations found
				if (configs.length>0) {
					for (IConfiguration cf : configs) {
						//Add path for the Tool's option
						if (addPathToSelectedToolOptionBuildConf(cf, path, var)) {
							success = true;
						} else {
							success = false;
						}
					}
					//if the path was added successfully
					if (success) {
						//save project build info
						ManagedBuildManager.saveBuildInfo(proj, true);					
					}
				}
			}			
		}
	}
	
	/**
	 * Removes a path from Tool option.
	 * 
	 * @param path Path to remove from Tool option
	 * @param var Tool option's value
	 */
	private static void removePathFromToolOption(String path, int var) {
		//check if the given path exists
		if (pathExists(path) || var==2) {
			boolean success = false;
			//get all projects in the workspace
			IProject[] projects = getProjectsInWorkspace();
			IConfiguration[] configs;
			for (IProject proj : projects) {
				//get all build configurations of the IProject
				configs = getAllBuildConfigs(proj);
				//if build configurations found
				if (configs.length>0) {
					for (IConfiguration cf : configs) {
						//remove a path from the Tool's option
						if (removePathFromSelectedToolOptionBuildConf(cf, path, var)) {
							success = true;
						} else {
							success = false;
						}
					}
					//if the path was removed successfully
					if (success) {
						//save project build info
						ManagedBuildManager.saveBuildInfo(proj, true);					
					}
				}
			}			
		}
	}
	
	/**
	 * Add a path to specific build configuration's Tool option. 
	 * 
	 * @param cf Build configuration
	 * @param path Path or file name to add
	 * @param var Value of the option type
	 * @return boolean True if path was added successfully
	 */
	private static boolean addPathToSelectedToolOptionBuildConf(IConfiguration cf, String path, int var) {
		switch (var) {
		case 1:
			return addLlvmIncludePathToToolOption(cf, path);
		case 2:
			return addLlvmLibToToolOption(cf, path);
		case 3:
			return addLlvmLibSearchPathToToolOption(cf, path);
		default:
			return false;
		}
	}

	/**
	 * Removes a path from specific build configuration's Tool option. 
	 * 
	 * @param cf Build configuration
	 * @param path Path or file name to remove
	 * @param var Value of the option type
	 * @return boolean True if path was removed successfully
	 */
	private static boolean removePathFromSelectedToolOptionBuildConf(IConfiguration cf, String path, int var) {
		switch (var) {
		case 1:
			return removeLlvmIncludePathFromToolOption(cf, path);
		case 2:
			return removeLlvmLibFromToolOption(cf, path);
		case 3:
			return removeLlvmLibSearchPathFromToolOption(cf, path);
		default:
			return false;
		}
	}
	
	/**
	 * Returns all projects in the workspace.
	 * 
	 * @return IProject[]
	 */
	public static IProject[] getProjectsInWorkspace() {
		//get workspace
		IWorkspace root = ResourcesPlugin.getWorkspace();
		//get all projects in the workspace
		return root.getRoot().getProjects();
	}
	
	/**
	 * Returns all build configurations of the project.
	 * 
	 * @param proj IProject Project
	 * @return IConfiguration[] Build configurations
	 */
	private static IConfiguration[] getAllBuildConfigs(IProject proj) {
		IManagedBuildInfo info = null;
		//try to get Managed build info
		try {
			info = ManagedBuildManager.getBuildInfo(proj); //null if doesn't exists
		} catch (Exception e) { //if not a managed build project
			//print error
			e.printStackTrace();
			return null;
		}
		assert(info!=null); //throws an error if info is null
		//get ManagedProject associated with build info
		IManagedProject mProj = info.getManagedProject();

		//get all build configurations of the project
		return mProj.getConfigurations();
	}
	
	/**
	 * Adds an include path to LLVM assembler's include path option.
	 * 
	 * @param cf IConfiguration Build configuration
	 * @param newIncludePath Include path to be added to LLVM assembler's Include path option
	 */
	private static boolean addLlvmIncludePathToToolOption(IConfiguration cf, String newIncludePath) {
		//get LLVM assembler
		ITool llvmAsm = getLlvmAssembler(cf);
		//If the LLVM assembler is found from the given build configuration
		if (llvmAsm != null) {
			//get LLVM assembler Include paths option.
			IOption llvmAsmIncPathOption = getLlvmAssemblerIncludePathOption(cf);
			//add a new include path to assembler's Include paths option.
			addIncludePathToToolOption(cf, llvmAsm, llvmAsmIncPathOption, newIncludePath);
			return true;
		} 
		return false;
	}

	/**
	 * Removes an include path from LLVM assembler's include path option.
	 * 
	 * @param cf IConfiguration Build configuration
	 * @param removeIncludePath Include path to be removed from LLVM assembler's Include path option
	 */
	private static boolean removeLlvmIncludePathFromToolOption(IConfiguration cf, String removeIncludePath) {
		//get LLVM assembler
		ITool llvmAsm = getLlvmAssembler(cf);
		//If the LLVM assembler is found from the given build configuration
		if (llvmAsm != null) {
			//get LLVM assembler Include paths option.
			IOption llvmAsmIncPathOption = getLlvmAssemblerIncludePathOption(cf);
			//remove an include path from assembler's Include paths option.
			removeIncludePathFromToolOption(cf, llvmAsm, llvmAsmIncPathOption, removeIncludePath);
			return true;
		} 
		return false;
	}
	
	/**
	 * Adds a Library to LLVM linker's Libraries Option.
	 * 
	 * @param cf IConfiguration Build configuration
	 * @param lib Library name
	 * @return boolean Returns true if Library Option was added successfully for the LLVM Linker.
	 */
	private static boolean addLlvmLibToToolOption(IConfiguration cf, String lib) {
		//get LLVM linker
		ITool llvmLinker = getLlvmLinker(cf);
		//If the LLVM linker is found from the given build configuration
		if (llvmLinker != null) {
			//get LLVM Linker Libraries option
			IOption librariesOption = getLlvmLinkerLibrariesOption(cf);
			//add library to LLVM linker's Libraries Option type
			addLibraryToToolOption(cf, llvmLinker, librariesOption, lib);
			return true;
		} 
		//adding the library failed
		return false;
	}
	
	/**
	 * Removes a Library from LLVM linker's Libraries Option.
	 * 
	 * @param cf IConfiguration Build configuration
	 * @param removeLib Library name
	 * @return boolean Returns true if Library Option was removed successfully from the LLVM Linker.
	 */
	private static boolean removeLlvmLibFromToolOption(IConfiguration cf, String removeLib) {
		//get LLVM linker
		ITool llvmLinker = getLlvmLinker(cf);
		//If the LLVM linker is found from the given build configuration
		if (llvmLinker != null) {
			//get LLVM Linker Libraries option
			IOption librariesOption = getLlvmLinkerLibrariesOption(cf);
			//remove a library from LLVM linker's Libraries Option type
			removeLibraryFromToolOption(cf, llvmLinker, librariesOption, removeLib);
			return true;
		} 
		//removing the library failed
		return false;
	}
	
	/**
	 * Adds a Library search path to LLVM linker's Library search path Option.
	 * 
	 * @param cf IConfiguration Build configuration
	 * @param libDir Library search path
	 * @return boolean Returns true if Library search path Option was added successfully for the LLVM Linker.
	 */
	private static boolean addLlvmLibSearchPathToToolOption(IConfiguration cf, String libDir) {
		//get LLVM linker
		ITool llvmLinker = getLlvmLinker(cf);
		//If the LLVM linker is found from the given build configuration
		if (llvmLinker != null) {
			//get LLVM Linker Library search path option
			IOption libDirOption = getLlvmLinkerLibrarySearchPathOption(cf);
			//add library search path to LLVM linker's Library Search Path Option type
			addLibrarySearchPathToToolOption(cf, llvmLinker, libDirOption, libDir);
			return true;
		} 
		//adding library failed
		return false;
	}

	/**
	 * Removes a Library search path from LLVM linker's Library search path Option.
	 * 
	 * @param cf IConfiguration Build configuration
	 * @param removeLibDir Library search path
	 * @return boolean Returns true if Library search path Option was removed successfully from the LLVM Linker.
	 */
	private static boolean removeLlvmLibSearchPathFromToolOption(IConfiguration cf, String removeLibDir) {
		//get LLVM linker
		ITool llvmLinker = getLlvmLinker(cf);
		//If the LLVM linker is found from the given build configuration
		if (llvmLinker != null) {
			//get LLVM Linker Library search path option
			IOption libDirOption = getLlvmLinkerLibrarySearchPathOption(cf);
			//remove a library search path from LLVM linker's Library Search Path Option type
			removeLibrarySearchPathFromToolOption(cf, llvmLinker, libDirOption, removeLibDir);
			return true;
		} 
		//removing the library search path failed
		return false;
	}
	
	/**
	 * Adds include path for given Build configuration's Tool's Include path Option.
	 * 
	 * @param cf IConfiguration Build configuration
	 * @param cfTool ITool Tool
	 * @param option Tool Option type
	 * @param newIncludePath Include path to be added to Tool's Include path option
	 */
	private static void addIncludePathToToolOption(IConfiguration cf, ITool cfTool, IOption option, String newIncludePath) {
		try {
			//add a new include path to linker's Include paths option.
			addInputToToolOption(cf, cfTool, option, newIncludePath, option.getIncludePaths());
		} catch (BuildException e) {
			//show error
			e.printStackTrace();
		}
	}

	/**
	 * Removes an include path from given Build configuration's Tool's Include path Option.
	 * 
	 * @param cf IConfiguration Build configuration
	 * @param cfTool ITool Tool
	 * @param option Tool Option type
	 * @param removeIncludePath Include path to be removed from Tool's Include path option
	 */
	private static void removeIncludePathFromToolOption(IConfiguration cf, ITool cfTool, IOption option, String removeIncludePath) {
		try {
			//remove an include path from linker's Include paths option.
			removeInputFromToolOption(cf, cfTool, option, removeIncludePath, option.getIncludePaths());
		} catch (BuildException e) {
			//show error
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds new Library for the Linker's Libraries Option.
	 * 
	 * @param cf IConfiguration Build configuration
	 * @param cfTool ITool Tool
	 * @param option Tool Option type
	 * @param newLibrary Library
	 */
	private static void addLibraryToToolOption(IConfiguration cf, ITool cfTool, IOption option, String newLibrary) {
		try {
			//add a new library to linker's Libraries option.
			addInputToToolOption(cf, cfTool, option, newLibrary, option.getLibraries());
		} catch (BuildException e) {
			//show error
			e.printStackTrace();
		}
	}

	/**
	 * Removes a new Library from the Linker's Libraries Option.
	 * 
	 * @param cf IConfiguration Build configuration
	 * @param cfTool ITool Tool
	 * @param option Tool Option type
	 * @param removeLibrary Library
	 */
	private static void removeLibraryFromToolOption(IConfiguration cf, ITool cfTool, IOption option, String removeLibrary) {
		try {
			//remove a library from linker's Libraries option.
			removeInputFromToolOption(cf, cfTool, option, removeLibrary, option.getLibraries());
		} catch (BuildException e) {
			//show error
			e.printStackTrace();
		}
	}
	
	//Works only if Eclipse Bugzilla Bug 321040 fix is applied
	/**
	 * Adds new Library search path for the Linker's Library search path Option.
	 * 
	 * @param cf IConfiguration Build configuration
	 * @param cfTool ITool Tool
	 * @param option Tool Option type
	 * @param newSearchPath Library search path
	 */
	private static void addLibrarySearchPathToToolOption(IConfiguration cf, ITool cfTool, IOption option, String newSearchPath) {
		try {
			//add a new library path to linker's Library search path option.
			addInputToToolOption(cf, cfTool, option, newSearchPath, option.getLibraryPaths());
		} catch (BuildException e) {
			//show error
			e.printStackTrace();
		}
	}

	/**
	 * Removes a Library search path from the Linker's Library search path Option.
	 * Since CDT 8.0 (Bugzilla Bug 321040)
	 * 
	 * @param cf IConfiguration Build configuration
	 * @param cfTool ITool Tool
	 * @param option Tool Option type
	 * @param removeSearchPath Library search path
	 */
	private static void removeLibrarySearchPathFromToolOption(IConfiguration cf, ITool cfTool, IOption option, String removeSearchPath) {
		try {
			//remove a library path from linker's Library search path option.
			removeInputFromToolOption(cf, cfTool, option, removeSearchPath, option.getLibraryPaths());
		} catch (BuildException e) {
			//show error
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a new value to specific Option.
	 * 
	 * @param cf IConfiguration Build configuration
	 * @param cfTool ITool Tool
	 * @param option Tool Option type
	 * @param newValue New value to be added to the Option type
	 * @param existingValues Existing Option type values
	 */
	private static void addInputToToolOption(IConfiguration cf, ITool cfTool, IOption option, String newValue, String[] existingValues) {
		//if Option type is found
		if (option != null) {
			//append new value with existing values
			String[] newValues = addNewPathToExistingPathList(existingValues, newValue);
			//set new values array for the option for the given build configuration
			ManagedBuildManager.setOption(cf, cfTool, option, newValues);
		}
		else{
			//log error
		}
	}
	
	/**
	 * Removes a value from a specific Option.
	 * 
	 * @param cf IConfiguration Build configuration
	 * @param cfTool ITool Tool
	 * @param option Tool Option type
	 * @param removeValue Value to be removed from the Option type
	 * @param existingValues Existing Option type values
	 */
	private static void removeInputFromToolOption(IConfiguration cf, ITool cfTool, IOption option, String removeValue, String[] existingValues) {
		//if Option type is found
		if (option != null) {
			//remove value from existing values
			String[] newValues = removePathFromExistingPathList(existingValues, removeValue);
			//set new values array for the option for the given build configuration
			ManagedBuildManager.setOption(cf, cfTool, option, newValues);
		}
		else{
			//log error
		}
	}
	
	/**
	 * Returns LLVM assembler.
	 * 
	 * @param cf IConfiguration Build configuration
	 * @return ITool LLVM assembler
	 */
	private static ITool getLlvmAssembler(IConfiguration cf) {
		//get LLVM assembler
		return getIToolByInputType(cf, assemblerInputType);
	}
	
	/**
	 * Returns LLVM linker.
	 * 
	 * @param cf IConfiguration Build configuration
	 * @return ITool LLVM linker
	 */
	private static ITool getLlvmLinker(IConfiguration cf) {
		//get LLVM linker
		return getIToolByInputType(cf, linkerInputType);
	}
	
	/**
	 * Returns ITool associated with the input extension.
	 * 
	 * @param cf IConfiguration Build configuration
	 * @param ext input extension associated with ITool
	 * @return ITool Tool that matches input extension
	 */
	private static ITool getIToolByInputType(IConfiguration cf, String ext) {
		//get ITool associated with the input extension
		return cf.getToolFromInputExtension(ext);
	}
	
	/**
	 * Returns LLVM Assembler Include path Option type.
	 * 
	 * @param cf IConfiguration Project build configuration
	 * @return IOption Tool option type
	 */
	private static IOption getLlvmAssemblerIncludePathOption(IConfiguration cf) {
		//get ITool associated with the input extension
		ITool cfTool = cf.getToolFromInputExtension(assemblerInputType);
		//get option id for include paths
		String includeOptionId = getOptionId(cfTool, IOption.INCLUDE_PATH);
		return getIToolPathOption(cfTool, includeOptionId);
	}
	
	/**
	 * Returns LLVM Linker Libraries Option type.
	 * 
	 * @param cf IConfiguration Project build configuration
	 * @return IOption Tool option type
	 */
	private static IOption getLlvmLinkerLibrariesOption(IConfiguration cf) {
		//get ITool associated with the input extension
		ITool cfTool = cf.getToolFromInputExtension(linkerInputType);
		//get option id for libraries
		String libOptionId = getOptionId(cfTool, IOption.LIBRARIES);
		return getIToolPathOption(cfTool, libOptionId);
	}
	
	/**
	 * Returns LLVM Linker Library search path Option type.
	 * 
	 * @param cf IConfiguration Project build configuration
	 * @return IOption Tool option type
	 */
	private static IOption getLlvmLinkerLibrarySearchPathOption(IConfiguration cf) {
		//get ITool associated with the input extension
		ITool cfTool = cf.getToolFromInputExtension(linkerInputType);
		//get option id for library paths
		String libDirOptionId = getOptionId(cfTool, IOption.LIBRARY_PATHS);
		return getIToolPathOption(cfTool, libDirOptionId);
	}
	
	/**
	 * Returns Tool's option id.
	 * 
	 * @param cfTool ITool Tool
	 * @param optionValueType Option's value type.
	 * @return optionId Tool's option id.
	 */
	private static String getOptionId(ITool cfTool, int optionValueType) {
		String optionId = null;
		//get all Tool options.
		IOption[] options = cfTool.getOptions();
		for (IOption opt : options) {
			try {
				//try to match option value type
				if(opt.getValueType()==optionValueType) {
					//get option id
					optionId = opt.getId();
					break;
				}
			} catch (BuildException e) {
				//log error
			}
		}	
		return optionId;
	}
	
	/**
	 * Returns Tool's Option type by Id.
	 * 
	 * @param cfTool ITool Tool
	 * @param optionId String Tool option type id
	 * @return IOption Tool option type
	 */
	private static IOption getIToolPathOption(ITool cfTool, String optionId) {
		//get path option with specific id for the ITool
		return cfTool.getOptionById(optionId);
	}
	
	/**
	 * Adds one or more paths to the list of paths.
	 * 
	 * @param existingPaths Existing list of paths to add to
	 * @param newPath New path to add. May include multiple directories with a path delimiter java.io.File.pathSeparator 
	 * (usually semicolon (Win) or colon (Linux/Mac), OS specific)
	 * @return String[] List that includes existing paths as well as new paths.
	 */
	public static String[] addNewPathToExistingPathList(String[] existingPaths, String newPath) {
		String pathSep = java.io.File.pathSeparator;  // semicolon for windows, colon for Linux/Mac
		List<String> newPathList = new ArrayList<String>();
		String path;
		//adds existing paths to new paths list
		for (int i = 0; i < existingPaths.length; i++) {
			path = existingPaths[i];
			newPathList.add(path);
		}
		//separates new path if it has multiple paths separated by a path separator
		String[] newPathArray = newPath.split(pathSep);
		for (int i = 0; i < newPathArray.length; i++) {
			path = newPathArray[i];
			newPathList.add(path);
		}
		//creates a new list that includes all existing paths as well as new paths
		String[] newArray = newPathList.toArray(new String[0]);
		return newArray;
	}
	
	/**
	 * Removes one path from the list of paths.
	 * 
	 * @param existingPaths Existing list of paths to remove from
	 * @param removePath Path to be removed.
	 * @return String[] List that includes existing paths without the path that was removed.
	 */
	public static String[] removePathFromExistingPathList(String[] existingPaths, String removePath) {
		List<String> newPathList = new ArrayList<String>();
		String path;
		//adds existing paths to new paths list
		for (int i = 0; i < existingPaths.length; i++) {
			path = existingPaths[i];
			newPathList.add(path);
		}
		newPathList.remove(removePath);
		//creates a new list that includes all existing paths except the removed path
		String[] newArray = newPathList.toArray(new String[0]);
		return newArray;
	}
	
	/**
	 * Split paths to a String array.
	 * 
	 * @param str String of paths separated by a path separator.
	 * @return String array containing multiple paths.
	 */
	public static String[] stringToArray(String str) {
		return str.split(System.getProperty("path.separator")); //$NON-NLS-1$
	}
	
	/**
	 * Append an array of Strings to a String separated by a path separator.
	 * 
	 * @param array An array of Strings.
	 * @return string which contains all indexes of
	 * a String array separated by a path separator.
	 */
	public static String arrayToString(String[] array) {
		StringBuffer sB = new StringBuffer();
		//if array isn't empty and doesn't contain an empty String 
		if (array.length>0 /*&& !array[0].equals("")*/) {
			for (String i : array) {
				sB.append(i);
				sB.append(System.getProperty("path.separator")); //$NON-NLS-1$
			}			
		}
		return sB.toString();
	}

	/**
	 * Checks if a file path exists.
	 * 
	 * @return boolean True if the file exists.
	 */
	private static boolean pathExists(String path) {
		//return true if path exists.
		return new File(path).exists();
	}
	
}
