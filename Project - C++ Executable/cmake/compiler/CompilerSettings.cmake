if(CMAKE_CXX_COMPILER_ID STREQUAL "MSVC")
    if(CMAKE_CXX_FLAGS MATCHES "/W[0-4]")
        string(REGEX REPLACE "/W[0-4]" "" CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS}")
    endif()
endif()

function(set_target_compilation_options target)
    target_compile_features(${target} PUBLIC cxx_std_20 c_std_11)
    set_target_properties(${target} PROPERTIES
            CXX_STANDARD_REQUIRED ON
            CXX_EXTENSIONS OFF)
endfunction()

function(set_target_warnings target)
    if(CMAKE_CXX_COMPILER_ID STREQUAL "Clang" OR CMAKE_CXX_COMPILER_ID STREQUAL "GNU")
        target_compile_options(${target} PRIVATE -Wall -Wextra -pedantic -Wconversion -Wsign-conversion)
    elseif(CMAKE_CXX_COMPILER_ID STREQUAL "MSVC")
        set(ignoredWarnings /wd4514 /wd4571 /wd4623 /wd4625 /wd4626 /wd4668 /wd4710 /wd4774 /wd4820 /wd5026 /wd5027 /wd5045)
        target_compile_options(${target} PRIVATE /Zc:__cplusplus /W4 /permissive- ${ignoredWarnings})
        target_compile_definitions(${target} PRIVATE
                -D_SILENCE_ALL_CXX17_DEPRECATION_WARNINGS
                -D_SCL_SECURE_NO_WARNINGS
                -D_CRT_SECURE_NO_WARNINGS
                -D_HAS_FEATURES_REMOVED_IN_CXX20
        )
    endif()
endfunction()
