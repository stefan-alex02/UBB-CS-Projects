cmake_minimum_required(VERSION 3.27)
project(ConvolutionCpp)

set(CMAKE_CXX_STANDARD 17)

# Check if we are using MSVC
if (MSVC)
    # MSVC equivalent to set the stack size (e.g., 1 GB)
    set(CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS} /STACK:1000000000")
else()
    # Use GCC/Clang specific linker flags
    set(CMAKE_EXE_LINKER_FLAGS "${CMAKE_EXE_LINKER_FLAGS} -Wl,--stack,1000000000")
endif()


set(PROJECT_HEADERS
        distributor/BlockDistributor.h
        distributor/CyclicDistributor.h
        distributor/LinearDistributor.h
        distributor/Distributor.h

        thread/BlockConvolutionThread.h
        thread/CyclicConvolutionThread.h
        thread/LinearConvolutionThread.h

        util/Convolution.h
        util/matrixFileHandler.h
        util/matrixUtils.h
        util/SequentialConvolution.h
        util/technique.h
)
set(PROJECT_SOURCES

        util/matrixUtils.cpp
)

add_executable(dynamic main_dynamic.cpp ${PROJECT_SOURCES} ${PROJECT_HEADERS})

add_executable(static main_static.cpp ${PROJECT_SOURCES} ${PROJECT_HEADERS})

# mkdir build
# cd build
# cmake ..
# cmake --build .

# cmake .. && cmake --build .