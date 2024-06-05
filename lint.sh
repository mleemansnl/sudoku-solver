# Utility script to run all linters

# Lint C++ code using clang-tidy
bazel build //src/cc/... \
  --aspects @bazel_clang_tidy//clang_tidy:clang_tidy.bzl%clang_tidy_aspect \
  --output_groups=report \
  --@bazel_clang_tidy//:clang_tidy_config=//:clang_tidy_config

# Lint Markdown files using markdownlint
# Note: the markdown rule exits with only an error, not showing the markdownlint 
# violations, hence the `|| cat` to show the linter feedback upon violations.
bazel build //:md \
  --aspects @dwtj_rules_markdown//markdown:aspects.bzl%markdownlint_aspect \
   || cat bazel-bun/md.markdownlint.log