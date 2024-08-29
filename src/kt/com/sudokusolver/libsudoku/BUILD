load("@rules_kotlin//kotlin:jvm.bzl", "kt_jvm_library", "kt_jvm_test")

kt_jvm_library(
    name = "libsudoku",
    srcs = glob(
        ["*.kt"],
        exclude = ["*Test.kt"],
    ),
    visibility = ["//visibility:public"],
    deps = [
        "//src/kt/com/sudokusolver/libdlx",
    ],
)

kt_jvm_test(
    name = "unittest",
    size = "small",
    srcs = glob(["*Test.kt"]),
    args = [
        "--select-package",
        "com.sudokusolver.libsudoku",
    ],
    main_class = "org.junit.platform.console.ConsoleLauncher",
    runtime_deps = [
        "@maven//:org_junit_platform_junit_platform_console",
    ],
    deps = [
        ":libsudoku",
        "@maven//:org_junit_jupiter_junit_jupiter_api",
        "@maven//:org_junit_jupiter_junit_jupiter_engine",
        "@maven//:org_junit_jupiter_junit_jupiter_params",
    ],
)
