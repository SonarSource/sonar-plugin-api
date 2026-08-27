import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.api.plugins.BasePluginExtension
import org.jfrog.gradle.plugin.artifactory.task.ArtifactoryTask
import org.w3c.dom.Element

plugins {
  id("sonar-plugin-api.artifactory-conventions")
  id("com.gradleup.shadow")
}

dependencies {
  api(libs.slf4j)
  api(project(":shared"))

  // please keep the list grouped by configuration and ordered by name
  implementation(libs.commons.io)
  implementation(libs.commons.lang3)
  implementation(libs.commons.text)
  implementation(libs.commons.validator)
  implementation(libs.gson)

  // shaded, but not relocated
  implementation(project(":check-api"))

  compileOnly(libs.jsr305)

  testImplementation(libs.junit4)
  testImplementation(libs.junit5)
  testImplementation(project(":test-fixtures"))
  testImplementation(libs.guava)
  testImplementation(libs.junit.dataprovider)
  testImplementation(libs.assertj)
  testImplementation(libs.mockito)

  testRuntimeOnly(libs.jupiter.vintage.engine)
  testRuntimeOnly(libs.jupiter.engine)
}

configurations.named("testImplementation") {
  // Make the compileOnly dependencies available when compiling/running tests
  extendsFrom(configurations.getByName("compileOnly"))
}

fun on3Digits(version: String): String {
  val stripped = version.replaceFirst(Regex("-\\w+"), "")
  return stripped.split(".").plus("0").take(3).joinToString(".")
}

// The build version is composed of 4 fields, including the semantic version and the build number provided by Travis.
val buildVersion = if (project.version.toString().endsWith("SNAPSHOT")) {
  project.version.toString()
} else {
  on3Digits(project.version.toString()) + "." + (System.getProperty("buildNumber") ?: "0")
}

tasks.named<ProcessResources>("processResources") {
  filter(mapOf("tokens" to mapOf("project.buildVersion" to buildVersion)), ReplaceTokens::class.java)
}

configure<BasePluginExtension> {
  archivesName.set("sonar-plugin-api")
}

tasks.named<ShadowJar>("shadowJar") {
  archiveClassifier.set("")
  configurations = listOf(project.configurations.getByName("runtimeClasspath"))
  minimize {
    exclude(project(":check-api"))
  }
  relocate("com.google", "org.sonar.api.internal.google")
  relocate("org.apache.commons", "org.sonar.api.internal.apachecommons")
  dependencies {
    exclude(dependency("org.slf4j:slf4j-api:.*"))
  }
  exclude("META-INF/**/module-info.class")
}

tasks.named("build") {
  dependsOn("shadowJar")
}

tasks.named<ArtifactoryTask>("artifactoryPublish") {
  isSkip = false
}

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      artifactId = "sonar-plugin-api"
      pom {
        name = "Sonar Plugin API"
        description = project.description
        url = "https://www.sonarsource.com/"
        organization {
          name = "SonarSource"
          url = "https://www.sonarsource.com/"
        }
        licenses {
          license {
            name = "GNU LGPL 3"
            url = "https://www.gnu.org/licenses/lgpl-3.0.txt"
            distribution = "repo"
          }
        }
        scm {
          url = "https://github.com/SonarSource/sonar-plugin-api"
        }
        developers {
          developer {
            id = "sonarsource-team"
            name = "SonarSource Team"
          }
        }
      }
      artifact(tasks.named<ShadowJar>("shadowJar")) {
        classifier = null
      }
      artifact(tasks["sourcesJar"])
      artifact(tasks["javadocJar"])
      pom.withXml {
        fun Element.addChild(name: String, value: String) {
          val child = ownerDocument.createElement(name)
          child.textContent = value
          appendChild(child)
        }
        val dependenciesElement = asElement().ownerDocument.createElement("dependencies")
        val dependencyElement = asElement().ownerDocument.createElement("dependency")
        dependencyElement.addChild("groupId", "org.slf4j")
        dependencyElement.addChild("artifactId", "slf4j-api")
        dependencyElement.addChild("version", libs.versions.slf4j.get())
        dependenciesElement.appendChild(dependencyElement)
        asElement().appendChild(dependenciesElement)
      }
    }
  }
}

tasks.named<Test>("test") {
  dependsOn(":plugin-api:shadowJar")
  // Enabling the JUnit Platform (see https://github.com/junit-team/junit5-samples/tree/master/junit5-migration-gradle)
  useJUnitPlatform()
}
