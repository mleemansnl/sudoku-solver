
Coding standards:
 - https://stackoverflow.com/questions/2360734/whats-a-good-directory-structure-for-larger-c-projects-using-makefile
 - https://www.open-std.org/jtc1/sc22/wg21/docs/papers/2018/p1204r0.html
 - https://github.com/vector-of-bool/pitchfork
 
 - https://google.github.io/styleguide/cppguide.html

 https://github.com/aspect-build/rules_lint?tab=readme-ov-file
 https://github.com/erenon/bazel_clang_tidy

 >  bazel build //src/cc/libsudoku --aspects @bazel_clang_tidy//clang_tidy:clang_tidy.bzl%clang_tidy_aspect --output_groups=report