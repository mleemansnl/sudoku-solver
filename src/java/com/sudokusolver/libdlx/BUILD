java_library(
    name = "libdlx",
    srcs = glob(
        ["*.java"],
        exclude = ["Test*.java"],
    ),
    visibility = ["//visibility:public"],
)

java_test(
    name = "unittest",
    size = "small",
    srcs = glob(["Test*.java"]),
    deps = [
        ":libdlx",
        "@maven//:junit_junit",
    ],
)
