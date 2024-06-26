load("//tools/pytest:defs.bzl", "pytest_test")

py_library(
    name = "libdlx",
    srcs = glob(
        ["*.py"],
        exclude = ["*_test.py"],
    ),
    visibility = ["//visibility:public"],
)

pytest_test(
    name = "unittest",
    size = "small",
    srcs = glob(["*_test.py"]),
    deps = [
        ":libdlx",
    ],
)
