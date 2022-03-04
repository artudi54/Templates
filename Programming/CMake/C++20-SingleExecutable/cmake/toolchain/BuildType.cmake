# Set build type if absent
if (NOT CMAKE_BUILD_TYPE)
    set(CMAKE_BUILD_TYPE "RelWithDebInfo")
    message(STATUS "Build type absent, defaulting to RelWithDebInfo")
endif()

string(TOLOWER "${CMAKE_BUILD_TYPE}" CMAKE_BUILD_TYPE_LOWER)
message(STATUS "Build type - ${CMAKE_BUILD_TYPE}")
