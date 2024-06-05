ARG VARIANT=debian-12
ARG USERNAME=vscode
# https://mcr.microsoft.com/en-us/product/devcontainers/base/about
FROM mcr.microsoft.com/vscode/devcontainers/base:1.0-${VARIANT}

###
# Install C++ clang
###
# https://github.com/devcontainers/images/blob/main/src/cpp/.devcontainer/Dockerfile
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install build-essential cmake cppcheck valgrind clang lldb llvm gdb \
    && apt-get autoremove -y && apt-get clean -y && rm -rf /var/lib/apt/lists/*

RUN gcc --version
RUN clang --version

###
# Install Python
###
# https://github.com/devcontainers/images/blob/main/src/python/.devcontainer/Dockerfile
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install python3 python3-pip \
    && apt-get autoremove -y && apt-get clean -y && rm -rf /var/lib/apt/lists/*

# Upgrade python packages
ENV PIP_BREAK_SYSTEM_PACKAGES 1
RUN pip install --upgrade \
    setuptools==69.0.3 \
    gitpython==3.1.41

RUN python3 --version

###
# Install Golang via GVM
###

RUN curl -s -S -L https://go.dev/dl/go1.22.4.linux-amd64.tar.gz | tar xzf - -C /usr/local
ENV GOBIN="/usr/local/go/bin"
ENV PATH="${PATH}:/usr/local/go/bin"

RUN go version

###
# Install Node via NVM
###

# https://github.com/nvm-sh/nvm
ENV NODE_VERSION=20.14.0
RUN curl -fsSLO --compressed "https://nodejs.org/dist/v20.14.0/node-v20.14.0-linux-x64.tar.xz" \
    && tar -xJf "node-v20.14.0-linux-x64.tar.xz" -C /usr/local --strip-components=1 --no-same-owner \
    && rm "node-v20.14.0-linux-x64.tar.xz" \
    && ln -s /usr/local/bin/node /usr/local/bin/nodejs \
    && node --version \
    && npm --version

###
# Install Java JDK
###

RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install openjdk-17-jre ant maven \
    && apt-get autoremove -y && apt-get clean -y && rm -rf /var/lib/apt/lists/*

###
# Install Bazelisk for Bazel
###

RUN go install github.com/bazelbuild/bazelisk@latest
RUN sudo ln -s /usr/local/go/bin/bazelisk /usr/local/bin/bazel

###
# Install additional tools
###

# Install C++ linter and formatter clang-tidy and clang-format
# https://pypi.org/project/clang-tidy/18.1.1/
RUN pip install clang-format==18.1.5
RUN pip install clang-tidy==18.1.1

# Install Bazel linter and formatter Buildifier
RUN go install github.com/bazelbuild/buildtools/buildifier@latest

# Install Markdown linter
RUN npm install -g markdownlint-cli
