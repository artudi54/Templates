cmake_minimum_required(VERSION 3.10)
project(library VERSION 1.0.0 LANGUAGES CXX)

# Executable
set(SOURCES
    src/lib.cpp
)
set(HEADERS
)
add_library(${PROJECT_NAME} ${SOURCES} ${HEADERS})

# Compile options
target_compile_features(${PROJECT_NAME} PRIVATE cxx_std_17)
set_target_properties(${PROJECT_NAME} PROPERTIES
	CXX_STANDARD_REQUIRED ON
	CXX_EXTENSIONS OFF
)
if(CMAKE_CXX_COMPILER_ID STREQUAL "Clang" OR CMAKE_CXX_COMPILER_ID STREQUAL "GNU")
    target_compile_options(${PROJECT_NAME} PRIVATE -Wall -Wextra -pedantic -Wconversion -Wsign-conversion)
elseif(CMAKE_CXX_COMPILER_ID STREQUAL "MSVC")
    set(IGNORED_WARNINGS /wd4514 /wd4571 /wd4623 /wd4625 /wd4626 /wd4668 /wd4710 /wd4774 /wd4820 /wd5026 /wd5027 /wd5045)
    target_compile_options(${PROJECT_NAME} PRIVATE /W4 /Wall /Za /permissive- ${IGNORED_WARNINGS})
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
install(TARGETS ${PROJECT_NAME}
    EXPORT ${PROJECT_NAME}-export
    RUNTIME DESTINATION bin
    LIBRARY DESTINATION lib
    ARCHIVE DESTINATION lib
)
install(DIRECTORY  
  include/
  DESTINATION include
  FILES_MATCHING PATTERN "*.h"
)
