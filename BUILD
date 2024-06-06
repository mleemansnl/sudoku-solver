load("@dwtj_rules_markdown//markdown:defs.bzl", "markdown_library")

# Filegroup for using clang-tidy configuration file during C++ linting
filegroup(
    name = "clang_tidy_config",
    srcs = [".clang-tidy"],
    visibility = ["//visibility:public"],
)

# Target for linting Markdown files
markdown_library(
    name = "md",
    srcs = glob(["**/*.md"]),
)

# Export PMD settings for Java linting
exports_files(["pmd.xml"])
