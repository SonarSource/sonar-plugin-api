= Plugin API Deprecation Policy

== Goal

The goal of the deprecation strategy is to **give the opportunity for plugins to adapt to breaking API changes, while not blocking the evolution of our platforms that implement the API**.

The Plugin API is implemented, at least partially, by 3 platforms: SonarCloud, SonarLint and SonarQube. This introduces a few constraints:

* SonarQube’s own deprecation policy states that breaking changes should only happen in the first releases of a new SonarQube major version (10.0, 11.0, etc). As a result, plugins should be able to be compatible with all versions within a major version;

* SonarLint’s connected mode must be compatible with SonarQube LTS+ and with the previous LTS for 12 months, in the connected mode. As a result, it must be able to run plugins that were designed for those versions, and are potentially up to 1.5 years old;

== Deprecation Process

A Jira ticket should be created with the details, including the reasoning for the deprecation.

Code is deprecated with the annotation `@java.lang.Deprecated`. The API version when the deprecation started should be in the corresponding javadoc. The javadoc needs to indicate what is the replacement, if there is one.

Deprecated API is included in the release notes, which can be found [here](https://github.com/SonarSource/sonar-plugin-api/releases).

The **deprecation can only happen when there’s already a replacement** or when the feature will be completely dropped.

== Dropping APIs

=== When

All breaking changes must be preceded by a **deprecation period of at least 2 years after the deprecation**. This gives time for everyone to adapt, and allows SonarLint to run, in connected mode, plugins that were designed for the current SonarQube LTS.

Plugins that don’t use deprecated APIs in a SonarQube LTS will be compatible with the next major version of SonarQube.

== How

* **Change major version of the API** when we have a breaking change

* **SonarQube should adopt breaking changes before SonarCloud**. This is safer because plugins typically run ITs against SonarQube

**Ask for a PR review**. One of the points to check is whether it’s safe to go ahead with the change and no internal plugins will be broken.

== Exceptions

* Any API (class/interface/method/…) that is annotated with the `@org.sonar.api.Beta` annotation **can be removed at any time without prior notice**. The 2 years deprecation period does not apply in this case.
