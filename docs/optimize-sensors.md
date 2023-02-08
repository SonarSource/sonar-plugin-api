## Introduction

We have been optimising all parts of the analysis as part of our effort to reduce the total scan times. Some of those optimizations depend on additional information exchanged
between analyzers and the platform. Here we list some of the APIs introduced specifically to allow certain optimizations to be performed. Note the implementation of these APIs are
optional.

## Analyzer cache

Analyzers can persist data in a cache that will be made available to it in a later analysis. The cached data is stored on the server side and the analyzer can store and retrieve data using any key.
Analyzers must make sure that there’s no risk of key collision between different analyzers.

Note that cached data doesn’t necessarily come from the previous analysis of the same branch. In the current SQ’s implementation:

| Case | Origin of the cache
| --- |---------------------|
| 1st analysis of a branch | Main branch         |
| Subsequent Analysis of a branch | Analyzed  branch    |
| Pull Request | Target branch if exists, otherwise main branch |

It's the plugin's responsibility to invalidate cached data. Plugins shouldn't rely on file statuses (`InputFile.status()`) to determine if a file was changed since data was cached. The reason is that in some situations, the computation of the file statuses is not done with the same branch from where the cached data is downloaded from.

### APIs

```
WriteCache SensorContext.nextCache()
ReadCache SensorContext.previousCache()
```

## Skipping Unchanged Files

Currently, the strategy when analyzing pull requests is to only send files to the server that were detected as changed. In that situation, some analyzers can skip the analysis of
unchanged files. For example, if no cross file analysis is performed. Note that the API is generic (not specifically about PRs) to allow for changes in strategy in the future.

### APIs

```
boolean SensorContext.canSkipUnchangedFiles
```

## Flagging files without changed data

While the platform can detect if the source code of a file wasn’t changed, it can’t know if the data related to that file has changed or not. For example, with cross file analysis,
a changed in File A can modify issues in the unchanged File B. Other data reported for file such as measures or highlighting can change too depending on scan properties, analyzer
upgrades or other changes in the environment.

We introduced an experimental API for analyzers to flag files for which data is unchanged. Analyzers can rely on `InputFile.status`, and on their analyzer cache to conclude that
nothing has changed for a given file.

Note that **the analyzer still has to report all data for the file**.

> :warning: currently, in SonarQube, this optimization is only enabled for the CFamily and Cobol sensors.
### APIs

```
InputFile.markAsUnchanged()
```

