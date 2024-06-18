# Utility script to run all linters
set -e

# Lint C++ code using clang-tidy
bazel build //src/cc/... \
  --aspects @bazel_clang_tidy//clang_tidy:clang_tidy.bzl%clang_tidy_aspect \
  --output_groups=report \
  --@bazel_clang_tidy//:clang_tidy_config=//:clang_tidy_config

# Lint Java code using checkstyle
bazel build //src/java/... \
  --aspects //tools/lint:linters.bzl%pmd \
  --norun_validations \
  --output_groups=rules_lint_report \
  --remote_download_regex='.*AspectRulesLint.*' \
  --@aspect_rules_lint//lint:fail_on_violation

# Lint Markdown files using markdownlint

# Note: the markdown rule exits with only an error, not showing the markdownlint 
# violations, hence the `|| cat` to show the linter feedback upon violations.
bazel build //:md \
  --aspects @dwtj_rules_markdown//markdown:aspects.bzl%markdownlint_aspect \
   || cat bazel-bin/md.markdownlint.log

# Lint GitHub Actions Workflows using actionlint
actionlint
