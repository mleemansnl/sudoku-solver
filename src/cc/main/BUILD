cc_binary(
    name = "sudoku",
    srcs = ["main.cpp"],
    deps = [
        ":libapp",
    ],
)

cc_library(
    name = "libapp",
    srcs = glob(
        ["*.cpp"],
        exclude = [
            "main.cpp",
            "*-unittest.cpp",
        ],
    ),
    hdrs = glob(["*.hpp"]),
    deps = ["//src/cc/libsudoku"],
)

cc_test(
    name = "unittest",
    size = "small",
    srcs = glob(["*-unittest.cpp"]),
    deps = [
        ":libapp",
        "@googletest//:gtest_main",
    ],
)
