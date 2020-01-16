cmake_minimum_required(VERSION 3.10)
project(library VERSION 1.0.0 LANGUAGES CXX)

# Configuration
option(${PROJECT_NAME}_BUILD_TESTS "Build ${PROJECT_NAME} tests" ON)
option(${PROJECT_NAME}_INSTALL "Generate CMake install for ${PROJECT_NAME}" ON)

# Executable
set(SOURCES
    src/lib.cpp
)
set(HEADERS
)
add_library(${PROJECT_NAME} ${SOURCES} ${HEADERS})

# Compile options
# Language
target_compile_features(${PROJECT_NAME} PUBLIC cxx_std_17)
set_target_properties(${PROJECT_NAME}
        PROPERTIES
            CXX_STANDARD_REQUIRED ON
            CXX_EXTENSIONS OFF)
# Compile warnings
if(CMAKE_CXX_COMPILER_ID STREQUAL "Clang" OR CMAKE_CXX_COMPILER_ID STREQUAL "GNU")
    target_compile_options(${PROJECT_NAME} PRIVATE -Wall -Wextra -pedantic -Wconversion -Wsign-conversion)
elseif(CMAKE_CXX_COMPILER_ID STREQUAL "MSVC")
    set(IGNORED_WARNINGS /wd4514 /wd4571 /wd4623 /wd4625 /wd4626 /wd4668 /wd4710 /wd4774 /wd4820 /wd5026 /wd5027 /wd5045)
    target_compile_options(${PROJECT_NAME} PRIVATE /Wall /Za /permissive- ${IGNORED_WARNINGS})
    if(CMAKE_CXX_FLAGS MATCHES "/W[0-4]")
        string(REGEX REPLACE "/W[0-4]" "/W4" CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS}")
    else()
        set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} /W4")
    endif()
    target_compile_definitions(${PROJECT_NAME} PRIVATE -D_SILENCE_ALL_CXX17_DEPRECATION_WARNINGS -D_SCL_SECURE_NO_WARNINGS -D_CRT_SECURE_NO_WARNINGS)
endif()

# Include directories
target_include_directories(${PROJECT_NAME}
PUBLIC
    $<BUILD_INTERFACE:${CMAKE_CURRENT_SOURCE_DIR}/include>
    $<INSTALL_INTERFACE:include>
PRIVATE
)

# Install configuration
if(${PROJECT_NAME}_INSTALL)
    include(GNUInstallDirs)

    # Project target
    install(TARGETS ${PROJECT_NAME}
            EXPORT ${PROJECT_NAME}-export
            RUNTIME DESTINATION ${CMAKE_INSTALL_BINDIR}
            LIBRARY DESTINATION ${CMAKE_INSTALL_LIBDIR}
            ARCHIVE DESTINATION ${CMAKE_INSTALL_LIBDIR})

    # Includes
    install(DIRECTORY ${PROJECT_SOURCE_DIR}/include/
            DESTINATION ${CMAKE_INSTALL_INCLUDEDIR}
            FILES_MATCHING
            PATTERN "*.hpp"
            PATTERN "*.h")
    
    # CMake package files
    file(WRITE
            ${PROJECT_BINARY_DIR}/${PROJECT_NAME}-config.cmake
            "include(\"\${CMAKE_CURRENT_LIST_DIR}/${PROJECT_NAME}-export.cmake\")")
    install(EXPORT ${PROJECT_NAME}-export
            DESTINATION lib/cmake/${PROJECT_NAME})
    install(FILES ${PROJECT_BINARY_DIR}/${PROJECT_NAME}-config.cmake
            DESTINATION lib/cmake/${PROJECT_NAME})
endif()