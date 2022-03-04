if (NOT CMAKE_TOOLCHAIN_FILE)
    find_file(vcpkgPath vcpkg.exe PATHS ENV PATH)
    if (NOT vcpkgPath)
        find_file(vcpkgPath vcpkg PATHS ENV PATH)
    endif()

    if (NOT vcpkgPath)
        message(FATAL_ERROR "Could not find vcpkg.exe in PATH env variable. Please add vcpkg directory to PATH or specify toolchain to CMake directly with -DCMAKE_TOOLCHAIN_FILE=\"[vcpkg-dir]/scripts/buildsystems/vcpkg.cmake\"")
        return()
    endif()

    get_filename_component(vcpkgDir "${vcpkgPath}" DIRECTORY)
    set(CMAKE_TOOLCHAIN_FILE ${vcpkgDir}/scripts/buildsystems/vcpkg.cmake CACHE STRING "Vcpkg toolchain file")
endif()

message(STATUS "CMake Toolchain - ${CMAKE_TOOLCHAIN_FILE} ")
