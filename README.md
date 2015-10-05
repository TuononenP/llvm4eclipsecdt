# llvm4eclipsecdt

LLVM4EclipseCDT provides LLVM toolchain support for Eclipse CDT. It can compile C and C++ program code with Clang or llvm-gcc front-ends using LLVM as a back-end.

LLVM tools such as assembler, archiver, linker, static compiler (llc), JIT/Interpreter (lli) are featured and can be configured via graphical user interface (Project->Properties->C/C++ Build->Settings).

Header paths, library search paths and library files can be added via LLVM preference page (Window -> Preferences -> LLVM).

Plugin supports Linux, MacOSX and Windows with MinGW and Cygwin.
Eclipse 3.7 and CDT 8.0 or later.

<b>Note:</b>
Currently builds C and C++ projects on Linux and C projects on Windows. Building C++ projects on Windows is on the way. Support for Mac OS X is included but no testing has been done. LLVM static linker and JIT doesn't show up on new project wizard yet. 

<b>Does not yet work with LLVM 3.2. Works with older versions. This is, because llvm-ld (linker) tool was removed from 3.2.</b>
