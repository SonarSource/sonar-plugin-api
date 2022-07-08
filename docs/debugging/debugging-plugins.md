# Debugging plugins

**Debugging web server extensions**

1. Edit conf/sonar.properties and set: `sonar.web.javaAdditionalOpts=-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=8000`
2. Install your plugin by copying its JAR file to extensions/plugins
3. Start the server. The line `Listening for transport dt_socket at address: 5005` is logged in  `logs/sonar.log`.
4. Attach your IDE to the debug process (listening on port 8000 in the example)

**Debugging compute engine extensions**  
Same procedure as for web server extensions (see previous paragraph), but with the property: `sonar.ce.javaAdditionalOpts=-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=8000`

**Debugging scanner extensions**
```
export SONAR_SCANNER_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=8000"
cd /path/to/project
sonar-scanner 
```
When using the Scanner for Maven, then simply execute:
```
cd /path/to/project
mvnDebug sonar:sonar
# debug port is 8000
```
