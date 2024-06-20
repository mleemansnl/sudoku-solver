# load("@aspect_rules_lint//lint:golangci-lint.bzl", "golangci_lint_aspect")
load("@aspect_rules_lint//lint:pmd.bzl", "lint_pmd_aspect")

# Define Java linter: PMD
pmd = lint_pmd_aspect(
    binary = "@@//tools/lint:pmd",
    rulesets = ["@@//:pmd.xml"],
)

# TODO check aspect_rules_lint v1.0.0-rc4 or upwards for new support
# Define Golang linter: golangci-lint
# golangci_lint = golangci_lint_aspect(
#     binary = "@multitool//tools/golangci-lint",
#     config = "@@//:.golangci.yaml",
# )
