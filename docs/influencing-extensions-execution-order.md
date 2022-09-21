# Influencing extensions execution order

## Introduction

In the Plugin API, there are 3 special extension points:
* `org.sonar.api.batch.sensor.Sensor`
* `org.sonar.api.scanner.sensor.ProjectSensor`
* `org.sonar.api.batch.postjob.PostJob`

Classes implementing those extension points (interfaces) are called extensions.

In Sonar products, extensions coming from those 3 extension points are executed one after another. In some situations, it can be useful for an extension to always be executed after another one; one use case is that one Sensor builds an intermediate representation of the code, and other Sensors can compute issues from it. There are different mechanisms in the API for plugins to influence the execution order.

## Registering for an execution phase

Those extensions are executed by products during a _Phase_. There are 3 Phases:

* PRE
* DEFAULT
* POST

The Phases execute in this order: PRE -> DEFAULT -> POST.

Each extension can be registered in a specific Phase, enforcing their order of excution. By default, new extentions belong to the `DEFAULT` phase. This can be changed by annotating the extension class with the `Phase` annotation as follows:

```
import org.sonar.api.batch.Phase;
import org.sonar.api.batch.sensor.Sensor;


@Phase(name = Phase.Name.PRE)
public class MySensor implements Sensor {

}
```

## Ordering extensions within a phase

By default, extensions within each Phase are executed in a random order; this means that from one execution to another, the order is not guaranteed.

The order of execution can be enforced by explicitly marking the dependency between extentions using the `@DependedUpon` and `@DependsUpon` annotations:


```
import org.sonar.api.batch.DependedUpon;
import org.sonar.api.batch.DependsUpon;
import org.sonar.api.batch.sensor.Sensor;


@DependedUpon(value = "UniqueKey")
public class SensorToBeExecutedFirst implements Sensor {

}

@DependsUpon(value = "UniqueKey")
public class SensorToBeExecutedSecond implements Sensor {

}
```

The `DependedUpon` and `DependsUpon` annotations enforce a dependency between extensions. The execution order is guaranteed: extensions annotated with `DependedUpon` will be executed before extensions annotated with `DependsUpon` for extensions who share the same dependency value.

The `value` parameter acts as a unique identifier for the dependency and helps products control the order of extensions. This name must be unique across all plugins installed in a product; it is best to use a very specific name, non-prone to collisions between plugins.

This dependency mechanism also works across multiple plugins. As a consequence, using these annotations could be considered as a contract with other plugins. This can have an impact if developers change the plugin in the future and want to ensure backwards compatibility.

Extension ordering only works for extensions _in the same Phase_ and _between extensions of the same kind_ (see the different extension types in the Introduction). The different types of extensions are loaded at different times and using annotations in this case would have no effect. Note that `org.sonar.api.batch.sensor.Sensor` extends `org.sonar.api.scanner.sensor.ProjectSensor` therefore, ordering can be defined by the classes which implement them. The extension will be applied only when scanning the 'root' module.

There is a special case when ordering extensions: A `org.sonar.api.batch.sensor.Sensor` can be marked `global` by calling `org.sonar.api.batch.sensor.SensorDescriptor#global()` from it's _describe method_ and will be executed only once on the 'root' module. In this case, non-global sensors are executed first and each global sensor is executed in it's respective phase. It is not possible to control the orders of execution between non-global and global sensors.