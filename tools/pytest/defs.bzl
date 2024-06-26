# Source: https://stackoverflow.com/a/67389568 / https://stackoverflow.com/questions/58326587/how-do-i-use-pytest-with-bazel
"""Wrap pytest"""

load("@rules_python//python:defs.bzl", "py_test")

# TODO fix loadin of pylint and black
def pytest_test(name, srcs, deps = [], args = [], data = [], **kwargs):
    """
        Call pytest
    """
    py_test(
        name = name,
        srcs = [
            "//tools/pytest:pytest_wrapper.py",
        ] + srcs,
        main = "//tools/pytest:pytest_wrapper.py",
        args = [
            "--capture=no",
            # "--black",
            # "--pylint",
            # "--pylint-rcfile=$(location //tools/pytest:.pylintrc)",
            # "--mypy",
        ] + args + ["$(location :%s)" % x for x in srcs],
        python_version = "PY3",
        srcs_version = "PY3",
        deps = deps + [
            "@pip//pytest:pkg",
            # "@pip//pytest-black:pkg",
            # "@pip//pytest-pylint:pkg",
        ],
        data = [
            # "//tools/pytest:.pylintrc",
        ] + data,
        **kwargs
    )
