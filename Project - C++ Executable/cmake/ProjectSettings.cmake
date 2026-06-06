# Build type and toolchain
include(${CMAKE_CURRENT_LIST_DIR}/toolchain/BuildType.cmake)
include(${CMAKE_CURRENT_LIST_DIR}/toolchain/VCPKGToolchain.cmake)

# Compilation options
include(${CMAKE_CURRENT_LIST_DIR}/compiler/CompilerSettings.cmake)
