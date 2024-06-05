# Note: includes dependencies not converted to bzlmod yet

load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

# Dependencies for C++ code

# clang-tidy C++ linter
git_repository(
    name = "bazel_clang_tidy",
    commit = "43bef6852a433f3b2a6b001daecc8bc91d791b92",
    remote = "https://github.com/erenon/bazel_clang_tidy.git",
)

# Dependencies for Markdown files

# markdownlint linter
DWTJ_RULES_MARKDOWN_COMMIT = "f65f338a9e7ed15fe7b2630c7e1cbf47e81a2837"

DWTJ_RULES_MARKDOWN_SHA256 = "f2ea09f870045b0732002a20dd62ad838862f5a7b0f825b47939fc1c784daa3e"

http_archive(
    name = "dwtj_rules_markdown",
    sha256 = DWTJ_RULES_MARKDOWN_SHA256,
    strip_prefix = "dwtj_rules_markdown-{}".format(DWTJ_RULES_MARKDOWN_COMMIT),
    url = "https://github.com/dwtj/dwtj_rules_markdown/archive/{}.zip".format(DWTJ_RULES_MARKDOWN_COMMIT),
)

load("@dwtj_rules_markdown//markdown:repositories.bzl", "local_markdownlint_repository")

local_markdownlint_repository(
    name = "local_markdownlint",
    config = "@//:.markdownlint.json",
)

load("@local_markdownlint//:defs.bzl", "register_local_markdownlint_toolchain")

register_local_markdownlint_toolchain()
