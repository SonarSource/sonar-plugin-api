pluginManagement {
  repositories {
    maven {
      url = uri("https://repox.jfrog.io/repox/plugins.gradle.org/")
      // The environment variables ARTIFACTORY_PRIVATE_USERNAME and ARTIFACTORY_PRIVATE_PASSWORD are used on QA env (Jenkins)
      // On local box, please add artifactoryUsername and artifactoryPassword to ~/.gradle/gradle.properties
      val artifactoryUsername = providers.environmentVariable("ARTIFACTORY_PRIVATE_USERNAME")
        .orElse(providers.gradleProperty("artifactoryUsername"))
        .getOrElse("")
      val artifactoryPassword = providers.environmentVariable("ARTIFACTORY_PRIVATE_PASSWORD")
        .orElse(providers.gradleProperty("artifactoryPassword"))
        .getOrElse("")
      if (artifactoryUsername.isNotEmpty() && artifactoryPassword.isNotEmpty()) {
        credentials {
          username = artifactoryUsername
          password = artifactoryPassword
        }
      }
    }
  }
}

rootProject.name = "sonar-plugin-api"
include("plugin-api")
include("check-api")
include("test-fixtures")
include("manifest")
include("shared")
