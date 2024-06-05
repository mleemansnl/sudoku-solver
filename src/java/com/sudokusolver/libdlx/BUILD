java_library(
    name = "libdlx",
    srcs = glob(
        ["*.java"],
        exclude = ["*Test.java"],
    ),
    visibility = ["//visibility:public"],
)

java_test(
    name = "DancingLinkNodeTest",
    size = "small",
    srcs = ["DancingLinkNodeTest.java"],
    test_class = "com.sudokusolver.libdlx.DancingLinkNodeTest",
    deps = [
        ":libdlx",
        "@maven//:junit_junit",
    ],
)
