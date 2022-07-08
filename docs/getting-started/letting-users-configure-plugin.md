# Letting users configure the plugin

In some cases it can be useful to let users of a plugin configure it. Some examples of configurations could be:

* source and test folders
* analysis scope (inclusions, exclusions)
* ...

The core component [`org.sonar.api.config.Configuration`](http://javadocs.sonarsource.org/latest/apidocs/index.html?org/sonar/api/config/Configuration.html) provides access to configuration. It deals with default values and decryption of values. It is available in all stacks (scanner, web server, Compute Engine). As recommended earlier, it must not be called from constructors.

MyExtension.java
```
public class MyRules implements RulesDefinition {
  private final Configuration config;
  
  public MyRules(Configuration config) {   
    this.config = config; 
  }
  
  @Override
  public void define(Context context) {
    int value = config.getInt("sonar.property").orElse(0);
  }
}
```
Scanner sensors can get config directly from SensorContext, without using constructor injection:

MySensor.java
```
public class MySensor extends Sensor {
  @Override
  public void execute(SensorContext context) {
    int value = context.config().getInt("sonar.property").orElse(0);
  }
}
```

In the scanner stack, properties are checked in the following order, and the first non-blank value is the one that is used:

1. System property
1. Scanner command-line (-Dsonar.property=foo for instance)
1. Scanner tool (<properties> of scanner for Maven for instance) 
1. Project configuration defined in the web UI 
1. Global configuration defined in the web UI 
1. Default value

Plugins can define their own properties so that they can be configured from web administration console. The extension point org.sonar.api.config.PropertyDefinition must be used :
```
public class ExamplePlugin implements Plugin {
  @Override
  public void define(Context context) {
    context.addExtension(
      PropertyDefinition.builder("sonar.my.property")
       .name("My Property")
       .description("This is the description displayed in web admin console")
       .defaultValue("42")
       .build()
    );
  }
}
```

[[info]]
| Values of the properties suffixed with `.secured` are not available to be read by any users. `.secured` is needed for passwords, for instance.

The annotation [`@org.sonar.api.Property`](http://javadocs.sonarsource.org/latest/apidocs/index.html?org/sonar/api/Property.html) can also be used on an extension to declare a property, but org.sonar.api.config.PropertyDefinition is preferred.
```
@Properties(
    @Property(key="sonar.my.property", name="My Property", defaultValue="42")
)
public class MySensor implements Sensor {
  // ...
}
  
public class ExamplePlugin implements Plugin {
  @Override
  public void define(Context context) {
    context.addExtension(MySensor.class);
  }
}
```
