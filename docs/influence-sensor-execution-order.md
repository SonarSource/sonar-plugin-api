# Influencing the Sensors execution order

## Preamble

The following document applies for 3 kinds of classes in plugins, the ones implementing:
* `org.sonar.api.batch.sensor.Sensor`
* `org.sonar.api.scanner.sensor.ProjectSensor`
* `org.sonar.api.batch.postjob.PostJob`

For the rest of the document we will refer to them using the generic term "Sensors", except if explicitly stated otherwise.

## Introduction

In Sonar products, Sensors are executed one after another. In some situations, it can be useful for a Sensor to always be executed after another one; one use case is that one Sensor builds an intermediate representation of the code, and other Sensors can compute issues from it. There are different mechanisms in the API for plugins to influence the execution order.

## Registering for an execution phase

Sensors are executed by products during a `Phase`. There are 3 of them:

* PRE
* DEFAULT
* POST

The phases execute in this order: PRE -> DEFAULT -> POST.

Each Sensor can register for a specific phase. By default, they belong to the `DEFAULT` phase. This can be changed by annotating the Sensor class with the `Phase` annotation, as follows:

```
import org.sonar.api.batch.Phase;
import org.sonar.api.batch.sensor.Sensor;


@Phase(name = Phase.Name.PRE)
public class MySensor implements Sensor {

}
```

As Phases are executed in a predefined order, registering Sensors in different phases enforces their order of execution.

## Ordering Sensors within a phase

Within those phases, by default Sensors are executed in a random order. It means that from one execution to another, the order is not guaranteed.

There is a mechanism to enforce Sensors to be executed in a defined order, by explicitly marking the dependency between Sensors. To achieve that, the two `@DependsUpon` and `@DependedUpon` annotations can be used:


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

The `DependedUpon` and `DependsUpon` annotations serve to enforce a dependency between sensors. The execution order is guaranteed: Sensors annotated with `DependedUpon` will be executed before Sensors annotated with `DependsUpon`, for Sensors who share the same dependency `value`.

The role of the `value` parameter on the annotation is to act as a unique identifier for the dependency. This is what helps products order the Sensors appropriately. This name has to be unique across all plugins installed in a product, so make sure to use a very specific, non-prone to collision name.

This dependency mechanism not only works within a plugin but also across plugins.

Sensor ordering works only for Sensors in the same Phase and between sensors of the same kind (see the different types in the preamble). Those different types of sensors are loaded at different times so it would not make sense to be able to order them. If in this case, using the annotations won't have any effect. Note that `org.sonar.api.batch.sensor.Sensor` extends `org.sonar.api.scanner.sensor.ProjectSensor` so ordering can be defined between classes implementing them. It will be applied only when scanning the 'root' module.

Finally, there is a special case with this mechanism. A `org.sonar.api.batch.sensor.Sensor` can be marked `global` by calling `org.sonar.api.batch.sensor.SensorDescriptor#global()` from its `describe` method. It will be executed only once on the 'root' module. In this case each global Sensor is executed in its respective phase after the non-global ones. It is possible to order global Sensors only between them.