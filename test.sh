# Utility script to run all tests
set -e

bazel test --test_output=all //...
