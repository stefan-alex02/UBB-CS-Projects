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
CMAKE_COMMAND = /usr/bin/cmake

# The command to remove a file.
RM = /usr/bin/cmake -E rm -f

# Escaping for special characters.
EQUALS = =

# The top-level source directory on which CMake was run.
CMAKE_SOURCE_DIR = "/mnt/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 5/PPD/Laborator1/ConvolutionCpp"

# The top-level build directory on which CMake was run.
CMAKE_BINARY_DIR = "/mnt/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 5/PPD/Laborator1/ConvolutionCpp/cmake-build-debug"

# Include any dependencies generated for this target.
include CMakeFiles/static.dir/depend.make
# Include any dependencies generated by the compiler for this target.
include CMakeFiles/static.dir/compiler_depend.make

# Include the progress variables for this target.
include CMakeFiles/static.dir/progress.make

# Include the compile flags for this target's objects.
include CMakeFiles/static.dir/flags.make

CMakeFiles/static.dir/main_static.cpp.o: CMakeFiles/static.dir/flags.make
CMakeFiles/static.dir/main_static.cpp.o: /mnt/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul\ 5/PPD/Laborator1/ConvolutionCpp/main_static.cpp
CMakeFiles/static.dir/main_static.cpp.o: CMakeFiles/static.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green --progress-dir="/mnt/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 5/PPD/Laborator1/ConvolutionCpp/cmake-build-debug/CMakeFiles" --progress-num=$(CMAKE_PROGRESS_1) "Building CXX object CMakeFiles/static.dir/main_static.cpp.o"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -MD -MT CMakeFiles/static.dir/main_static.cpp.o -MF CMakeFiles/static.dir/main_static.cpp.o.d -o CMakeFiles/static.dir/main_static.cpp.o -c "/mnt/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 5/PPD/Laborator1/ConvolutionCpp/main_static.cpp"

CMakeFiles/static.dir/main_static.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green "Preprocessing CXX source to CMakeFiles/static.dir/main_static.cpp.i"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E "/mnt/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 5/PPD/Laborator1/ConvolutionCpp/main_static.cpp" > CMakeFiles/static.dir/main_static.cpp.i

CMakeFiles/static.dir/main_static.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green "Compiling CXX source to assembly CMakeFiles/static.dir/main_static.cpp.s"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S "/mnt/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 5/PPD/Laborator1/ConvolutionCpp/main_static.cpp" -o CMakeFiles/static.dir/main_static.cpp.s

CMakeFiles/static.dir/util/matrixUtils.cpp.o: CMakeFiles/static.dir/flags.make
CMakeFiles/static.dir/util/matrixUtils.cpp.o: /mnt/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul\ 5/PPD/Laborator1/ConvolutionCpp/util/matrixUtils.cpp
CMakeFiles/static.dir/util/matrixUtils.cpp.o: CMakeFiles/static.dir/compiler_depend.ts
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green --progress-dir="/mnt/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 5/PPD/Laborator1/ConvolutionCpp/cmake-build-debug/CMakeFiles" --progress-num=$(CMAKE_PROGRESS_2) "Building CXX object CMakeFiles/static.dir/util/matrixUtils.cpp.o"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -MD -MT CMakeFiles/static.dir/util/matrixUtils.cpp.o -MF CMakeFiles/static.dir/util/matrixUtils.cpp.o.d -o CMakeFiles/static.dir/util/matrixUtils.cpp.o -c "/mnt/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 5/PPD/Laborator1/ConvolutionCpp/util/matrixUtils.cpp"

CMakeFiles/static.dir/util/matrixUtils.cpp.i: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green "Preprocessing CXX source to CMakeFiles/static.dir/util/matrixUtils.cpp.i"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -E "/mnt/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 5/PPD/Laborator1/ConvolutionCpp/util/matrixUtils.cpp" > CMakeFiles/static.dir/util/matrixUtils.cpp.i

CMakeFiles/static.dir/util/matrixUtils.cpp.s: cmake_force
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green "Compiling CXX source to assembly CMakeFiles/static.dir/util/matrixUtils.cpp.s"
	/usr/bin/c++ $(CXX_DEFINES) $(CXX_INCLUDES) $(CXX_FLAGS) -S "/mnt/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 5/PPD/Laborator1/ConvolutionCpp/util/matrixUtils.cpp" -o CMakeFiles/static.dir/util/matrixUtils.cpp.s

# Object files for target static
static_OBJECTS = \
"CMakeFiles/static.dir/main_static.cpp.o" \
"CMakeFiles/static.dir/util/matrixUtils.cpp.o"

# External object files for target static
static_EXTERNAL_OBJECTS =

static: CMakeFiles/static.dir/main_static.cpp.o
static: CMakeFiles/static.dir/util/matrixUtils.cpp.o
static: CMakeFiles/static.dir/build.make
static: CMakeFiles/static.dir/link.txt
	@$(CMAKE_COMMAND) -E cmake_echo_color "--switch=$(COLOR)" --green --bold --progress-dir="/mnt/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 5/PPD/Laborator1/ConvolutionCpp/cmake-build-debug/CMakeFiles" --progress-num=$(CMAKE_PROGRESS_3) "Linking CXX executable static"
	$(CMAKE_COMMAND) -E cmake_link_script CMakeFiles/static.dir/link.txt --verbose=$(VERBOSE)

# Rule to build all files generated by this target.
CMakeFiles/static.dir/build: static
.PHONY : CMakeFiles/static.dir/build

CMakeFiles/static.dir/clean:
	$(CMAKE_COMMAND) -P CMakeFiles/static.dir/cmake_clean.cmake
.PHONY : CMakeFiles/static.dir/clean

CMakeFiles/static.dir/depend:
	cd "/mnt/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 5/PPD/Laborator1/ConvolutionCpp/cmake-build-debug" && $(CMAKE_COMMAND) -E cmake_depends "Unix Makefiles" "/mnt/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 5/PPD/Laborator1/ConvolutionCpp" "/mnt/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 5/PPD/Laborator1/ConvolutionCpp" "/mnt/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 5/PPD/Laborator1/ConvolutionCpp/cmake-build-debug" "/mnt/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 5/PPD/Laborator1/ConvolutionCpp/cmake-build-debug" "/mnt/c/Users/Stefan/Documents/GitHub/UBB-CS-Projects/Semestrul 5/PPD/Laborator1/ConvolutionCpp/cmake-build-debug/CMakeFiles/static.dir/DependInfo.cmake" "--color=$(COLOR)"
.PHONY : CMakeFiles/static.dir/depend

