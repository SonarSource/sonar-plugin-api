# Deploy on a local SonarQube instance

**"Cold" Deploy**  

The standard way to install the plugin for regular users is to copy the JAR artifact, from the `target/` directory to the `extensions/plugins/` directory of your SonarQube installation then start the server. The file `logs/web.log` will then contain a log line similar to:  
`Deploy plugin Example Plugin / 0.1-SNAPSHOT`  
Scanner extensions such as sensors are immediately retrieved and loaded when scanning source code.

