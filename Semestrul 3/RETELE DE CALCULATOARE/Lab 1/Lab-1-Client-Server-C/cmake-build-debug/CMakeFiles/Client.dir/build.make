# CMAKE generated file: DO NOT EDIT!
# Generated by "Unix Makefiles" Generator, CMake Version 3.27

# Delete rule output on recipe failure.
.DELETE_ON_ERROR:

#=============================================================================
# Special targets provided by cmake.

# Disable implicit rules so canonical targets will work.
.SUFFIXES:

# Disable VCS-based implicit rules.
% : %,v

# Disable VCS-based implicit rules.
% : RCS/%

# Disable VCS-based implicit rules.
% : RCS/%,v

# Disable VCS-based implicit rules.
% : SCCS/s.%

# Disable VCS-based implicit rules.
% : s.%

.SUFFIXES: .hpux_make_needs_suffix_list

# Command-line flag to silence nested $(MAKE).
$(VERBOSE)MAKESILENT = -s

#Suppress display of executed commands.
$(VERBOSE).SILENT:

# A target that is always out of date.
cmake_force:
.PHONY : cmake_force

#=============================================================================
# Set environment variables for the build.

# The shell in which to execute make rules.
SHELL = /bin/sh

# The CMake executable.
CMAKE_COMMAND = /cygdrive/c/Users/Stefan/AppData/Local/JetBrains/CLion2023.3/cygwin_cmake/bin/cmake.exe

# The command to remove a file.
RM = /cygdrive/c/Users/Stefan/AppData/Local/JetBrains/CLion2023.3/cygwin_cmake/bin/cmake.exe -E rm -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = "/cygdrive/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 3/RETELE DE CALCULATOARE/Lab 1/Lab-1-Client-Server-C"

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = "/cygdrive/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 3/RETELE DE CALCULATOARE/Lab 1/Lab-1-Client-Server-C/cmake-build-debug"

# Include any dependencies generated for this target.
include CMakeFiles/Client.dir/depend.make
# Include any dependencies generated by the compiler for this target.
include CMakeFiles/Client.dir/compiler_depend.make

# Include the progress variables for this target.
include CMakeFiles/Client.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/Client.dir/flags.make

CMakeFiles/Client.dir/client.c.o: CMakeFiles/Client.dir/flags.make
CMakeFiles/Client.dir/client.c.o: /cygdrive/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul\ 3/RETELE\ DE\ CALCULATOARE/Lab\ 1/Lab-1-Client-Server-C/client.c
CMakeFiles/Client.dir/client.c.o: CMakeFiles/Client.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green --progress-dir="/cygdrive/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 3/RETELE DE CALCULATOARE/Lab 1/Lab-1-Client-Server-C/cmake-build-debug/CMakeFiles" --progress-num=$(CMAKE_PROGRESS_1) "Building C object CMakeFiles/Client.dir/client.c.o"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -MD -MT CMakeFiles/Client.dir/client.c.o -MF CMakeFiles/Client.dir/client.c.o.d -o CMakeFiles/Client.dir/client.c.o -c "/cygdrive/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 3/RETELE DE CALCULATOARE/Lab 1/Lab-1-Client-Server-C/client.c"

CMakeFiles/Client.dir/client.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green "Preprocessing C source to CMakeFiles/Client.dir/client.c.i"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E "/cygdrive/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 3/RETELE DE CALCULATOARE/Lab 1/Lab-1-Client-Server-C/client.c" > CMakeFiles/Client.dir/client.c.i

CMakeFiles/Client.dir/client.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green "Compiling C source to assembly CMakeFiles/Client.dir/client.c.s"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S "/cygdrive/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 3/RETELE DE CALCULATOARE/Lab 1/Lab-1-Client-Server-C/client.c" -o CMakeFiles/Client.dir/client.c.s

CMakeFiles/Client.dir/common.c.o: CMakeFiles/Client.dir/flags.make
CMakeFiles/Client.dir/common.c.o: /cygdrive/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul\ 3/RETELE\ DE\ CALCULATOARE/Lab\ 1/Lab-1-Client-Server-C/common.c
CMakeFiles/Client.dir/common.c.o: CMakeFiles/Client.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green --progress-dir="/cygdrive/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 3/RETELE DE CALCULATOARE/Lab 1/Lab-1-Client-Server-C/cmake-build-debug/CMakeFiles" --progress-num=$(CMAKE_PROGRESS_2) "Building C object CMakeFiles/Client.dir/common.c.o"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -MD -MT CMakeFiles/Client.dir/common.c.o -MF CMakeFiles/Client.dir/common.c.o.d -o CMakeFiles/Client.dir/common.c.o -c "/cygdrive/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 3/RETELE DE CALCULATOARE/Lab 1/Lab-1-Client-Server-C/common.c"

CMakeFiles/Client.dir/common.c.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green "Preprocessing C source to CMakeFiles/Client.dir/common.c.i"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -E "/cygdrive/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 3/RETELE DE CALCULATOARE/Lab 1/Lab-1-Client-Server-C/common.c" > CMakeFiles/Client.dir/common.c.i

CMakeFiles/Client.dir/common.c.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green "Compiling C source to assembly CMakeFiles/Client.dir/common.c.s"
	/usr/bin/cc $(C_DEFINES) $(C_INCLUDES) $(C_FLAGS) -S "/cygdrive/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 3/RETELE DE CALCULATOARE/Lab 1/Lab-1-Client-Server-C/common.c" -o CMakeFiles/Client.dir/common.c.s

# Object files for target Client
Client_OBJECTS = \
"CMakeFiles/Client.dir/client.c.o" \
"CMakeFiles/Client.dir/common.c.o"

# External object files for target Client
Client_EXTERNAL_OBJECTS =

Client.exe: CMakeFiles/Client.dir/client.c.o
Client.exe: CMakeFiles/Client.dir/common.c.o
Client.exe: CMakeFiles/Client.dir/build.make
Client.exe: CMakeFiles/Client.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green --bold --progress-dir="/cygdrive/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 3/RETELE DE CALCULATOARE/Lab 1/Lab-1-Client-Server-C/cmake-build-debug/CMakeFiles" --progress-num=$(CMAKE_PROGRESS_3) "Linking C executable Client.exe"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/Client.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/Client.dir/build: Client.exe
.PHONY : CMakeFiles/Client.dir/build

CMakeFiles/Client.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/Client.dir/cmake_clean.cmake
.PHONY : CMakeFiles/Client.dir/clean

CMakeFiles/Client.dir/depend:
	cd "/cygdrive/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 3/RETELE DE CALCULATOARE/Lab 1/Lab-1-Client-Server-C/cmake-build-debug" && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" "/cygdrive/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 3/RETELE DE CALCULATOARE/Lab 1/Lab-1-Client-Server-C" "/cygdrive/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 3/RETELE DE CALCULATOARE/Lab 1/Lab-1-Client-Server-C" "/cygdrive/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 3/RETELE DE CALCULATOARE/Lab 1/Lab-1-Client-Server-C/cmake-build-debug" "/cygdrive/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 3/RETELE DE CALCULATOARE/Lab 1/Lab-1-Client-Server-C/cmake-build-debug" "/cygdrive/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 3/RETELE DE CALCULATOARE/Lab 1/Lab-1-Client-Server-C/cmake-build-debug/CMakeFiles/Client.dir/DependInfo.cmake" "--color=$(COLOR)"
.PHONY : CMakeFiles/Client.dir/depend

