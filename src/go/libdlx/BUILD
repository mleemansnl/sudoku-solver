load("@rules_go//go:def.bzl", "go_library", "go_test")

go_library(
    name = "libdlx",
    srcs = [
        "DancingLinkHeader.go",
        "DancingLinkNode.go",
    ],
    importpath = "github.com/mleemansnl/sudoku-solver/src/go/libdlx",
    visibility = ["//visibility:public"],
)

go_test(
    name = "libdlx_test",
    size = "small",
    srcs = ["DancingLinkNode_test.go"],
    deps = [
        ":libdlx",
        "@com_github_stretchr_testify//suite",
    ],
)
