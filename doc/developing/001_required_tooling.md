# Required Tooling

Development at Wall Brew currently spans two primary programming languages: [clojure](https://clojure.org/) and [javascript.](https://www.javascript.com/)
To build libraries and applications in those languages we use the two following build tools:

* [Leiningen](https://leiningen.org/)
* [npm](https://www.npmjs.com/)

We highly recommend using your operating system's preferred package manager to install the following dependencies.
If you do not have a preferred package manager, we currently recommend [Homebrew.](https://brew.sh/)

For developers working on Windows machines, we strongly recommend the [Windows Subsystem for Linux (WSL).](https://learn.microsoft.com/en-us/windows/wsl/install)

## Clojure Development

### The Java Development Kit

Since clojure is a hosted language within the JVM, a recent JDK is required.
We recommend and develop against [OpenJDK](https://openjdk.org/) distributions.
The following Homebrew command will install the most recent distribution:

```sh
brew install openjdk
```

You can verify the installation with the following command:

```sh
$ java --version
openjdk 21.0.2 2024-01-16
OpenJDK Runtime Environment Homebrew (build 21.0.2)
OpenJDK 64-Bit Server VM Homebrew (build 21.0.2, mixed mode, sharing)
```

If you plan on developing against or regression testing functionality against older JDK versions, we recommend installing [jenv.](https://www.jenv.be/)
jenv can be installed with the following homebrew command:

```sh
brew install jenv
```

You can verify the installation with the following command:

```sh
$ jenv versions
* system (set by /some/local/path/.jenv/version)
```

### Leiningen

Leiningen manages clojure dependencies and tooling, and can be installed with the following homebrew command:

```sh
brew install leiningen
```

You can verify the installation with the following command:

```sh
$ lein -v
Leiningen 2.11.2 on Java 21.0.2 OpenJDK 64-Bit Server VM
```

When working in a clojure repository, Leiningen will use a file named `project.clj` in the repository's root to declare library dependencies and tooling plugins.
Leiningen will automatically download these as-needed while using default commands; however, they can be pre-fetched with the following:

```sh
lein deps
```

Individual developer tools should be installed in `.lein/profiles.clj`
A sample profile is provided below, but can be customized to your needs:

```clj
{:user {:plugin-repositories [["clojars" {:url "https://clojars.org/repo"}]]
        :git-dependencies    [["https://github.com/tobyhede/monger.git"]]
        :plugins             [[com.jakemccrary/lein-test-refresh "0.23.0"]
                              [lein-cloverage "1.2.4"]
                              [com.github.liquidz/antq "RELEASE"]
                              [ns-sort "1.0.3"]]
        :test-refresh        {:notify-command ["terminal-notifier" "-title" "Tests" "-message"]}}}
```

Default developer tools will be installed in the `project.clj` file for each Wall Brew repository.

## Clojurescript and Javascript Development

### Node.js

The Node Package Manager manages dependencies and tooling for our javascript projects and our clojurescript testing dependencies.
It can be installed with the following homebrew command:

```sh
brew install node
```

You can verify the installation with the following command:

```sh
$ npm version
{
  npm: '10.2.4',
  node: '21.6.2',
  ...
}
```

<!-- This file was automatically copied and populated by rebroadcast -->
<!-- Do not edit this file directly, instead modify the source at https://github.com/Wall-Brew-Co/rebroadcast/blob/master/sources/community/documentation/developing/001_required_tooling.md -->