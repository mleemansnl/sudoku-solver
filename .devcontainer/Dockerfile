ARG VARIANT=1.3-focal
FROM mcr.microsoft.com/vscode/devcontainers/universal:${VARIANT}

# https://pypi.org/project/clang-tidy/18.1.1/
RUN pip install clang-format==18.1.5
RUN pip install clang-tidy==18.1.1

RUN go install github.com/bazelbuild/buildtools/buildifier@latest
