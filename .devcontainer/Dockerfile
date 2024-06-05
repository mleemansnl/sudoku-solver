ARG VARIANT=1.3-focal
FROM mcr.microsoft.com/vscode/devcontainers/universal:${VARIANT}

# Install C++ linter and formatter clang-tidy and clang-format
# https://pypi.org/project/clang-tidy/18.1.1/
RUN pip install clang-format==18.1.5
RUN pip install clang-tidy==18.1.1

# Install Bazel linter and formatter Buildifier
RUN go install github.com/bazelbuild/buildtools/buildifier@latest

# Install Makrdown linter
RUN npm install -g markdownlint-cli
