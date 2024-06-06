java_library(
    name = "libsudoku",
    srcs = glob(
        ["*.java"],
        exclude = ["*Test.java"],
    ),
    deps = [
        "//src/java/com/sudokusolver/libdlx"
    ],
    visibility = ["//visibility:public"],
)

java_test(
    name = "unittest",
    size = "small",
    srcs = glob(["*Test.java"]),
    args = [
        "--select-package",
        "com.sudokusolver.libsudoku",
    ],
    main_class = "org.junit.platform.console.ConsoleLauncher",
    use_testrunner = False,
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
