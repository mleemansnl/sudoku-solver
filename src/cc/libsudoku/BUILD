cc_library(
    name = "libsudoku",
    srcs = glob(
        ["*.cpp"],
        exclude = ["*-unittest.cpp"],
    ),
    hdrs = glob(["*.hpp"]),
    visibility = ["//visibility:public"],
    deps = ["//src/cc/libdlx"],
)

cc_test(
    name = "unittest",
    size = "small",
    srcs = glob(["*-unittest.cpp"]),
    deps = [
        ":libsudoku",
        "@googletest//:gtest_main",
    ],
)
