load("@aspect_rules_lint//lint:pmd.bzl", "lint_pmd_aspect")

# Define Java linter: PMD
pmd = lint_pmd_aspect(
    binary = "@@//tools/lint:pmd",
    rulesets = ["@@//:pmd.xml"],
)
