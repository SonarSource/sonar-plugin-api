plugins {
  `kotlin-dsl`
}

repositories {
  maven {
    url = uri("https://repox.jfrog.io/repox/plugins.gradle.org/")
  }
}

dependencies {
  implementation("com.jfrog.artifactory:com.jfrog.artifactory.gradle.plugin:6.0.4")
}
