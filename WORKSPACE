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
DWTJ_RULES_MARKDOWN_COMMIT = "5fcac481becb6e57c7240ea42652ad9ddef36181"

DWTJ_RULES_MARKDOWN_SHA256 = "b4250fc7c13b55df27507e8d53da4fab61cfdc7c3f4b6002a8a312796f678a91"

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
