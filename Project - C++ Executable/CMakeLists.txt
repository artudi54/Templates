cmake_minimum_required(VERSION 3.10)
include(cmake/ProjectSettings.cmake)
project(executable VERSION 1.0.0 LANGUAGES ASM C CXX)

# Configuration
option(${PROJECT_NAME}_BUILD_TESTS "Build ${PROJECT_NAME} tests" ON)
option(${PROJECT_NAME}_INSTALL "Generate CMake install for ${PROJECT_NAME}" ON)

# Executable
file(GLOB_RECURSE HEADERS CONFIGURE_DEPENDS include/*.h include/*.h src/*.h src/*.hpp)
file(GLOB_RECURSE SOURCES CONFIGURE_DEPENDS src/*.c src/*.cpp src/*.S)
add_executable(${PROJECT_NAME} ${SOURCES} ${HEADERS})
set_target_compilation_options(${PROJECT_NAME})
set_target_warnings(${PROJECT_NAME})

# Include directories
target_include_directories(${PROJECT_NAME}
PUBLIC
    include
PRIVATE
    src
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
endif()
