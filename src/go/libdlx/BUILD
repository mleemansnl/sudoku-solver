load("@rules_go//go:def.bzl", "go_library", "go_test")

go_library(
    name = "libdlx",
    srcs = [
        "DLXSolver.go",
        "DancingLinkHeader.go",
        "DancingLinkNode.go",
        "DancingLinksMatrix.go",
    ],
    importpath = "github.com/mleemansnl/sudoku-solver/src/go/libdlx",
    visibility = ["//visibility:public"],
)

go_test(
    name = "libdlx_test",
    size = "small",
    srcs = [
        "DLXSolver_test.go",
        "DancingLinkNode_test.go",
    ],
    deps = [
        ":libdlx",
        "@com_github_stretchr_testify//suite",
    ],
)
