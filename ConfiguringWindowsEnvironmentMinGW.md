# How to setup LLVM compiler environment with MinGW #

## LLVM 2.9 ##

This is by far the easiest and fasted method to setup LLVM compiler for Windows. It only takes couple of minutes by following these steps.

1. Download mingw-get-inst (http://www.mingw.org -> Downloads)

2. Run MinGW installer (mingw-get-inst)

3. You can select to download packages from the latest repositories

4. Install to location with no spaces

5. Select to install C++ compiler

6. Add MinGW bin directory path to a system environment variable PATH (e.g. C:\MinGW\bin)

7. Download LLVM, Clang and LLVM-GCC binaries

8. Uncompress downloaded binaries into MinGW directory

9. Answer Yes when asked to merge folders

## LLVM 2.8 ##

These are older instructions how to setup LLVM 2.8 on Windows with MinGW(For more specific instructions, read http://llvm.org/docs/GettingStarted.html carefully.)

Step 1. Download LLVM and Clang source code and MinGW binary of LLVM-GCC. Download site: http://llvm.org/releases/download.html#2.8

Step 2. Uncompress LLVM sources e.g. C:/llvm-2.8

Step 3. Uncompress Clang sources to tools directory inside LLVM source directory e.g. C:/llvm-2.8/tools and check/rename that the added directory is simple called 'clang' with no suffix e.g. version number.

Step 4. Download MinGW compiler suite. Download site: http://sourceforge.net/projects/mingw/

Step 5. Install MinGW (Download latest repository catalogies) with MSYS and C & C++ compiler which are checked in Wizard type installation.

Step 6. Add MinGW's "bin" directory to the PATH environment variable. e.g. C:\MinGW\bin (Run sysdm.cpl and click Environment Variables... button)

Step 7. Download Perl MSYS binaries. Download link: http://sourceforge.net/projects/mingw/files/MSYS/perl/perl-5.6.1_2-2/perl-5.6.1_2-2-msys-1.0.13-bin.tar.lzma/download

Step 8. Uncompress Perl binary package into MSYS/1.0 directory (merge bin & lib folders).

Step 9. Download libcrypt MSYS binaries (Perl requires it). Download link: http://sourceforge.net/projects/mingw/files/MSYS/crypt/crypt-1.1_1-3/libcrypt-1.1_1-3-msys-1.0.13-dll-0.tar.lzma/download

Step 10. Uncompress libcrypt binary package into MSYS/1.0 directory. (merge bin folders).

Step 11. Open MSYS command prompt (e.g. C:/MinGW/msys/1.0/msys.bat) and navigate to directory where your LLVM sources are located e.g. cd C:/llvm-2.8

Step 12. Create folder for build files. e.g. mkdir C:/llvm-2.8/BUILD

Step 13. Go to BUILD directory. e.g. cd BUILD

Step 14. Run configure script: ../configure

Step 15. Build the LLVM suite: make -j# ENABLE\_OPTIMIZED=1
> (-j number\_of\_processor\_cores for parallel compilation) (ENABLE\_OPTIMIZED=1 to perform a Release (Optimized) build and ENABLE\_OPTIMIZED=0 to perform a Debug build)

> If building fails for some reason, try building it again or alternatively run 'make clean' command first.

Step 16. Add ../Release/bin (or ../Debug../bin) directory to your PATH. e.g. C:/llvm-2.8/BUILD/Release/bin

Step 17. Uncompress MinGW LLVM-GCC binary files to some directory e.g. C:/llvm-gcc

Step 18. Uncompress the binary binutils MinGW package into your LLVM-GCC binary directory. Download link: http://sourceforge.net/projects/mingw/files/MinGW/BaseSystem/GNU-Binutils/binutils-2.21/binutils-2.21-2-mingw32-bin.tar.lzma/download

Step 19. Add LLVM-GCC's "bin" directory to your PATH environment variable. e.g. C:/llvm-gcc/bin

Now LLVM toolchains should be visible in Eclipse CDT as long as LLVM, Clang, LLVM-GCC and MinGW binaries are in PATH.

# Links #
http://llvm.org/docs/GettingStarted.html <br />
http://www.mingw.org/wiki/Getting_Started <br />
http://www.mingw.org/wiki/InstallationHOWTOforMinGW