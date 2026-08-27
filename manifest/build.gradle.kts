import org.jfrog.gradle.plugin.artifactory.task.ArtifactoryTask

plugins {
  id("sonar-plugin-api.artifactory-conventions")
}

description = "Sonar Plugin API - Manifest"

dependencies {
  compileOnly(libs.jsr305)

  api(project(":shared"))
  implementation(libs.commons.lang3)

  testImplementation(libs.junit5)
  testImplementation(libs.assertj)
  testImplementation(libs.mockito)
  testRuntimeOnly(libs.jupiter.engine)
}

tasks.named<ArtifactoryTask>("artifactoryPublish") {
  isSkip = false
}

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      artifactId = "sonar-plugin-manifest"
      from(components["java"])
      pom {
        name = "Sonar Plugin API - Manifest"
        description = "Sonar Plugin API - Manifest"
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
      artifact(tasks["sourcesJar"])
      artifact(tasks["javadocJar"])
    }
  }
}

tasks.named<Test>("test") {
  useJUnitPlatform()
}
