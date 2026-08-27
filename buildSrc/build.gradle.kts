plugins {
  `kotlin-dsl`
}

repositories {
  maven {
    url = uri("https://repox.jfrog.io/repox/plugins.gradle.org/")
  }
}

dependencies {
  implementation(libs.artifactory.plugin)
}
