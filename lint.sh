# Utility script to run all linters

# Lint C++ code using clang-tidy
bazel build //src/cc/... \
  --aspects @bazel_clang_tidy//clang_tidy:clang_tidy.bzl%clang_tidy_aspect \
  --output_groups=report \
  --@bazel_clang_tidy//:clang_tidy_config=//:clang_tidy_config

# Lint Markdown files using markdownlint
# \todo fix markdownlint-cli by upgrading node to >= 15.0 in .devcontainer
#bazel build //:all_markdown \
#  --aspects @dwtj_rules_markdown//markdown:aspects.bzl%markdownlint_aspect
