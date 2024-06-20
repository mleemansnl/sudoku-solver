java_binary(
    name = "Main",
    srcs = ["Main.java"],
    deps = [
        ":libapp",
    ],
)

java_library(
    name = "libapp",
    srcs = glob(
        ["*.java"],
        exclude = [
            "Main.java",
            "*Test.java",
        ],
    ),
    deps = [
        "//src/java/com/sudokusolver/libsudoku",
    ],
)

java_test(
    name = "unittest",
    size = "small",
    srcs = glob(["*Test.java"]),
    args = [
        "--select-package",
        "com.sudokusolver.main",
    ],
    main_class = "org.junit.platform.console.ConsoleLauncher",
    use_testrunner = False,
    runtime_deps = [
        "@maven//:org_junit_platform_junit_platform_console",
    ],
    deps = [
        ":libapp",
        "@maven//:org_junit_jupiter_junit_jupiter_api",
        "@maven//:org_junit_jupiter_junit_jupiter_engine",
        "@maven//:org_junit_jupiter_junit_jupiter_params",
    ],
)
