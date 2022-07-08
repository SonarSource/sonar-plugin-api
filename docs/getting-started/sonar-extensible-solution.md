# Sonar: an extensible solution

## Sonar products

The Sonar ecosystem is composed of different products:
* SonarQube
* SonarCloud
* SonarLint
* Scanners

Each of them uses plugins to extend its core functionality.

## What is a Sonar plugin ?

A Sonar plugin is a JAR file that can contribute functionality at various places and stages in the Sonar ecosystem.
The added functionality can be of different kind:
* support for a new programming language
* support for a custom SCM
* new pages in the web application
* localization of the products
* ...

Each Sonar product decides which plugins or which plugin parts they are willing to use.

All the Sonar products and plugins form the Sonar ecosystem.
