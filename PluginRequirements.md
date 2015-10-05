# Introduction #

LLVM toolchain plugin for Eclipse CDT has certain requirements that must be met. The plugin development is aimed for the newest CDT release and the sources depend on the newest CDT build.

# Details #

  * Eclipse CDT 8.x is the minimum CDT version
  * Java 5 JRE or newer (also Eclipse requisition)
  * LLVM with Clang (follow the build instructions on http://clang.llvm.org/get_started.html)
  * llvm-gcc compiler (if you're planning to use LLVM with GCC toolchain)
  * PATH system environment variable containing path to LLVM with Clang binaries (in order for LLVM tool executables to be found)