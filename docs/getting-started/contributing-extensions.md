# Contributing extensions

## What is an extension ?

The Sonar Plugin API is made of dozens of Java interfaces that are called "extension points".
Those interfaces are usually annotated with the `@ExtensionPoint` annotation.
Plugins can contribute functionality by providing "extensions", which are implementations of those extension points.

## Registering extensions

Plugins can register those extensions by adding them to the Plugin Context, so that Sonar products can at runtime search, load and execute those extensions:

ExamplePlugin.java
```
package org.sonarqube.plugins.example;

import org.sonar.api.Plugin;
 
public class ExamplePlugin implements Plugin {
  @Override
  public void define(Plugin.Context context) {
    // declare extensions in the plugin's context
    context.addExtension(FooLanguage.class);
  }
}
```

## What is dependency injection

TODO explain the theory

## How does it work in Sonar products

TODO how to add stuff in the container
TODO how to get it injected in components
TODO what can they expect to be present in the container
TODO example

## Building extensions

The `Plugin.Context` `addExtension` or `addExtensions` methods accept Java `Object`s as parameters. Two kind of arguments can be supplied:
* a `java.lang.Class` object, representing the class of the extension
* the actual extension instance

In the first case, products loading the extension will take care of instantiating it.
All products support dependency injection for extensions, and are managing object containers.

