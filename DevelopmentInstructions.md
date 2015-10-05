# Introduction #

This page instructs how to set up the LLVM plugin development environment.

# Details #

  * Install Java JDK (preferably version 5 to ensure downward compability).
  * Download and install newest Eclipse (http://download.eclipse.org/eclipse/downloads).
  * Make sure that PDE (Plug-in Development Environment) is installed. Should be included in Eclipse SDK.
  * Download and install (preferably latest) C/C++ Development Tools and C/C++ Development Tools SDK which are included in CDT master feature pack (http://download.eclipse.org/tools/cdt/builds).
> In Eclipse navigate Help -> Install New Software... -> Add -> Archive... and select the CDT master zip file. Name the software site and click ok. Now select the newly created software site and install C/C++ Development Tools and C/C++ Development Tools SDK. It's very important to install SDK as well.
  * Install Subversion revision control system for your operating system.
  * Install Subversive or Subclipse Subversion client plugin for Eclipse. Subversive is already included in Eclipse update site, but for Subclipse one must add the software site manually which can be found from the Subclipse website (http://subclipse.tigris.org).
  * Install SVN Connector.
  * Open SVN repositories view and checkout llvm4eclipsecdt.
  * Go to Plug-ins window and select all org.eclipse.cdt packages and right click and select Import As -> Source Project. After that go back to Package Explorer window.
  * Recommendation is to use Java 5 JDK to ensure compatibility. To add JRE, select llvm4eclipsecdt project and press Alt+Enter -> Java Build Path -> Libraries -> Add Library... -> JRE System Library -> Alternate JRE -> Installed JREs... -> Add. Or Window -> Preferences -> Java -> Installed JREs -> Add.
  * Build LLVM and Clang and llvm-gcc front-ends.
  * Set PATH system environment variable to contain LLVM and compilder (clang & llvm-gcc) binaries.
  * Set LD\_LIBRARY\_PATH system environment variable to contain stdc++ library path.
  * In Eclipse navigate to Window -> Preferences -> LLVM and add Library path that contains libstdc++ if linker can't find stdc++ library when trying to build the project.