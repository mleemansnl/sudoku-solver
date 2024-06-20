# How Bazel is used

The reference implementations use [Bazel](https://bazel.build/) for
building, testing, and linting.

The following Bazel features are used:

| Language | Language rule | Dependencies | Linter aspect |
| -------- | ------------- | ------------ | ------------- |
| C++ | [rules_cc](https://bazel.build/versions/7.1.0/reference/be/c-cpp) | [native bzlmod](https://bazel.build/versions/7.1.0/external/overview#bzlmod) | [bazel_clang_tidy](https://github.com/erenon/bazel_clang_tidy) |
| Java | [rules_java](https://bazel.build/versions/7.1.0/reference/be/java) | [rules_jvm_external](https://github.com/bazelbuild/rules_jvm_external) | [rules_lint](https://github.com/aspect-build/rules_lint) |
| Golang | [rules_go](https://github.com/bazelbuild/rules_go) | [rules_go > gomod](https://github.com/bazelbuild/rules_go/blob/master/docs/go/core/bzlmod.md#external-dependencies) | [rules_go > nogo](https://github.com/bazelbuild/rules_go/blob/master/docs/go/core/bzlmod.md#configuring-nogo) |
| Markdown | | | [dwtj_rules_markdown](https://github.com/dwtj/dwtj_rules_markdown) |
