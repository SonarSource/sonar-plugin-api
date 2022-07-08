# Contributing extensions conditionally

Plugins might want to contribute extensions only in some situations, for example only in specific products. There are different mechanisms to achieve that.

## Checking runtime context

The `context` object received as a parameter in the `Plugin.define(Context)` method contains information about the runtime in which the plugins are currently executing.

See `context.getRuntime().getProduct()`

See `context.getRuntime().getApiVersion()`

See `context.getRuntime().getSonarQubeSide()`

These methods determine which product, API version or stack within a product is running.
Based on that plugins can decide to add a given extension to the `Plugin.Context` instance if needed.

## Enabling extensions in specific products/contexts

The Plugin API offers another annotations-based mechanism that permits fine-grain control over the context in which extensions should be enabled.
Those annotations are:
* `org.sonar.api.ce.ComputeEngineSide`: enables the extension only for SonarQube or SonarCloud, in the Compute Engine stack
* `org.sonar.api.scanner.ScannerSide`: enables the extension only for SonarQube or SonarCloud, in the Scanner stack
* `org.sonar.api.server.ServerSide`: enables the extension only for SonarQube or SonarCloud, in the Server stack
* `org.sonarsource.api.sonarlint.SonarLintSide`: enables the extension only for the SonarLint product

These are class-level annotations that can be added to extensions as follows:

ExamplePlugin.java
```
package org.sonarqube.plugins.example;

import org.sonar.api.Plugin;
import org.sonar.api.scanner.ScannerSide;
import org.sonarsource.api.sonarlint.SonarLintSide;

@ScannerSide
public class MyExtension {
}
```

This extension can be unconditionally added in the Plugin's context, it will only be enabled in the Scanner stack.

Those annotations can be used together, if an extension is to be run in several contexts.

Most of the extension points present in the Plugin API are already marked with proper annotations, so an extension implementing an extension point will automatically inherit those annotations and be loaded in the appropriate context. For exemple a plugin implementing `org.sonar.api.batch.sensor.Sensor` will automatically be loaded in the Scanner and SonarLint sides.

### Controlling the extension lifespan with the SonarLintSide annotation

SonarLint is a plugin running in IDEs. Contrary to Scanners that run in build pipelines, analyzing the full project at once (batch mode), SonarLint lives for a long time in IDE, triggering many analyses. 
Sonar plugins might want to leverage this lifecycle and keep some data in cache between analyzes, for example to build a symbol table or to speed some computations up.
The `org.sonarsource.api.sonarlint.SonarLintSide` annotation contains a parameter that can be used to control the lifespan of an extension. There are 3 possible values:

* SINGLE_ANALYSIS: the component will be instantiated for each analysis (could be single or multiple files analysis).
* MODULE: the component will be instantiated when a module is opened and kept alive until the module is closed. A module is a project in Eclipse, a folder in VSCode and a module in IntelliJ.
* INSTANCE: the component will be instantiated once and kept alive as long as SonarLint is active in the IDE.

On the SonarLint side, one container is created for each lifespan that is responsible for keeping those extensions alive and properly manage dependency injection.

TODO: container inheritance