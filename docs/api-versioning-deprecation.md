# Versioning and API Deprecation
## Versioning Strategy
The goal of this versioning strategy is both to:

* Release often, release early in order to get quick feedback from the SonarQube community
* Release stable versions of the SonarQube platform for companies whose main priority is to set up a very stable environment. Even if the price for such stable environments is missing out on the latest, sexy SonarQube features
* Support the API deprecation strategy (see next section)

The rules are:

* Every ~two months a new version of SonarQube is released. This version should increment the minor digit of the previous version (ex: 8.2 -> 8.3)
* Every ~eighteen months, a bug-fix version is released, and becomes the new LTS. The major digit of the subsequent version is incremented to start a new cycle (ex: 7.9 -> 8.0)

And here is the strategy in action:
```
7.8 -> 7.9 -> 8.0 -> 8.1 -> 8.2 -> ... -> 8.9 -> 9.0 -> ...     <- New release every ~2 months
        |                                  |
      7.9.1 -> 7.9.2 -> ...              8.9.1 -> 8.9.2 -> ...  <- New LTS
```

## API Deprecation Strategy
The goal of this deprecation strategy is to make sure that deprecated APIs will be dropped without side-effects at a given planned date. The expected consequence of such strategy is to ease the evolution of the SonarQube API by making such refactoring painless.

The rules are:

* An API must be deprecated before being dropped. Furthermore, if the underlying feature is not being dropped, a replacement API must immediately be provided.
* A deprecated API must be fully supported until its drop (For instance the implementation of a deprecated method can't be replaced by `throw new UnsupportedOperationException()`)
* If an API is deprecated in version X.Y, this API is planned to be dropped in version (X+1).0. Example: an API deprecated in 9.1 is supported in 9.2, 9.3, and so forth for the entire 9.N cycle; it will be dropped in version 10.0
* According to this versioning strategy, an API can remain deprecated for up to 18 months, and for as short as 2 months
* Any release of a SonarQube plugin must at least depend on the latest LTS version of the SonarQube API
* For each SonarQube plugin there must at least one release on each LTS version of SonarQube, which means at least one release each ~18 months.
* An API is marked as deprecated with both:
   * the annotation @Deprecated
   * the javadoc tag @deprecated whose message must start with "in x.y", for example:
   
    ```
    /**
     * @deprecated in 4.2. Replaced by {@link #newMethod()}.
     */
    @Deprecated
    public void foo() {
    ```
