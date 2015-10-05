# Introduction #
Compiles C/C++ program code with Clang and LLVM-GCC using LLVM as a backend. Takes advantage of LLVM e.g. faster compile time. Makes C/C++ developers more eager to take advantage of Eclipse CDT.

# Why LLVM? #
<a href='http://llvm4eclipsecdt.googlecode.com/files/Benefits%20of%20the%20LLVM%20compiler%20infrastructure.pdf'>The benefits of LLVM</a>

# Features #
  * LLVM tools included in the plugin: assembler, archiver, linker, compilers Clang, llvm-gcc, llvm static linker and JIT.

  * CDT Internal builder and GNU make builder work in co-operation with LLVM toolchain.

  * Plugin has an user interface for LLVM specific configuration (e.g. adding include and library paths and library files).

## Documentation ##
<a href='http://llvm4eclipsecdt.googlecode.com/files/Thesis%20-%20Petri%20Tuononen.pdf'>Link to documentation</a>

## Mailing list ##
http://groups.google.com/group/llvm4eclipsecdt <br />
llvm4eclipsecdt@googlegroups.com

### Note ###
Currently builds C and C++ projects on Linux and C projects on Windows. Building C++ projects on Windows is on the way. Support for Mac OS X is included but no testing has been done. LLVM static linker and JIT doesn't show up on new project wizard yet.

<b>Does not yet work with LLVM 3.2. Works with older versions. This is, because llvm-ld (linker) tool was removed from 3.2.</b>

### Update ###
Now after the release of LLVM 2.9 it is much easier and faster to setup LLVM compiler for Windows (MinGW binaries are provided for LLVM and front-ends).

## HOW TO GET ##
CDT 8.x supported version only<br />
Add update site to Eclipse: http://petrituononen.com/llvm4eclipsecdt/update <br />
or via Eclipse Marketplace <br />
http://marketplace.eclipse.org/content/llvm-toolchain-eclipse-cdt