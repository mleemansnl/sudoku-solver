cc_library(
    name = "libsudoku",
    srcs = glob(
        ["*.cpp"],
        exclude = ["*-unittest.cpp"],
    ),
    hdrs = glob(["*.hpp"]),
    deps = ["//src/cc/libdlx"],
    visibility = ["//visibility:public"],
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
