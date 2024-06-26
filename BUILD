load("@dwtj_rules_markdown//markdown:defs.bzl", "markdown_library")
load("@gazelle//:def.bzl", "gazelle")
load("@rules_go//go:def.bzl", "TOOLS_NOGO", "nogo")
load("@rules_python//python:pip.bzl", "compile_pip_requirements")

###
# Targets for C++ code
###

# Filegroup for using clang-tidy configuration file during C++ linting
filegroup(
    name = "clang_tidy_config",
    srcs = [".clang-tidy"],
    visibility = ["//visibility:public"],
)

###
# Targets for Markdown files
###

# Target for linting Markdown files
markdown_library(
    name = "md",
    srcs = glob(["**/*.md"]),
)

###
# Targets for Java code
###

# Export PMD settings for Java linting
exports_files(["pmd.xml"])

###
# Targets for Golang code
###

# Top-level target for Golang Gazelle Bazel BUILD file generation
gazelle(name = "gazelle")

# gazelle:prefix github.com/mleemansnl/sudoku-solver

# nogo linter target

nogo(
    name = "nogo",
    # vet = True,
    visibility = ["//visibility:public"],
    deps = TOOLS_NOGO,
)

###
# Targets for Python code
###

# This rule adds a convenient way to update the requirements file.
compile_pip_requirements(
    name = "requirements",
    src = "requirements.in",
    requirements_txt = "requirements.txt",
)
