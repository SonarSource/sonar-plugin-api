# Logging

The class [`org.sonar.api.utils.log.Logger`](http://javadocs.sonarsource.org/latest/apidocs/index.html?org/sonar/api/utils/log/Logger.html) is used to log messages. The output will depend on the context in which the plugin is executed:

* standard output for the Scanner
* `logs/sonar.log` file for the web server
* administration web console for Compute Engine 
* Log view for SonarLint

To get a `Logger` instance and use it, do the following:

```
import org.sonar.api.utils.log.Logger;

public class MyClass {
  private static final Logger LOGGER = Loggers.get(MyClass.class);
 
  public void doSomething() {
    LOGGER.info("foo");
  }
}
```

Internally [SLF4J](http://www.slf4j.org/) is used as a facade of various logging frameworks (log4j, commons-log, logback, java.util.logging). That allows all these frameworks to work at runtime, such as when they are required for a 3rd party library. SLF4J loggers can also be used instead of org.sonar.api.utils.log.Logger. Read the [SLF4J manual](http://www.slf4j.org/manual.html) for more details.

As an exception, plugins must not package logging libraries. Dependencies like SLF4J or log4j must be declared with scope "provided".

For testing purposes, it might be convenient to verify what logs were generated. The [`LogTester`](http://javadocs.sonarsource.org/latest/apidocs/index.html?org/sonar/api/utils/log/LogTester.html)) class can be used to access previously logged details.
