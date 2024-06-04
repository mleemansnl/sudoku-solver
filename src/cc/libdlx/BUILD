cc_library(
    name = "libdlx",
    srcs = glob(
        ["*.cpp"],
        exclude = ["*-unittest.cpp"],
    ),
    hdrs = glob(["*.hpp"]),
    visibility = ["//visibility:public"],
)

cc_test(
    name = "unittest",
    size = "small",
    srcs = glob(["*-unittest.cpp"]),
    deps = [
        ":libdlx",
        "@googletest//:gtest_main",
    ],
)
