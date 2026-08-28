import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.plugins.signing.Sign
import org.jfrog.gradle.plugin.artifactory.task.ArtifactoryTask
import nl.javadude.gradle.plugins.license.LicenseExtension

plugins {
  alias(libs.plugins.license)
  alias(libs.plugins.shadow) apply false
  alias(libs.plugins.sonarqube)
  id("sonar-plugin-api.artifactory-conventions")
}

allprojects {
  val buildNumber = System.getProperty("buildNumber")
  // Replaces the version defined in sources, usually x.y-SNAPSHOT, by a version identifying the build.
  if (version.toString().endsWith("-SNAPSHOT") && buildNumber != null) {
    val versionSuffix = if (version.toString().count { it == '.' } == 1) ".0.$buildNumber" else ".$buildNumber"
    version = version.toString().replace("-SNAPSHOT", versionSuffix)
  }

  repositories {
    val repository = if (project.hasProperty("qa")) "sonarsource-qa" else "sonarsource"
    maven {
      url = uri("https://repox.jfrog.io/repox/$repository")
      // The environment variables ARTIFACTORY_PRIVATE_USERNAME and ARTIFACTORY_PRIVATE_PASSWORD are used on QA env (Jenkins)
      // On local box, please add artifactoryUsername and artifactoryPassword to ~/.gradle/gradle.properties
      val artifactoryUsername = System.getenv("ARTIFACTORY_PRIVATE_USERNAME")
        ?: (project.findProperty("artifactoryUsername") as String? ?: "")
      val artifactoryPassword = System.getenv("ARTIFACTORY_PRIVATE_PASSWORD")
        ?: (project.findProperty("artifactoryPassword") as String? ?: "")
      if (artifactoryUsername.isNotEmpty() && artifactoryPassword.isNotEmpty()) {
        credentials {
          username = artifactoryUsername
          password = artifactoryPassword
        }
      }
    }
  }
}

sonar {
  properties {
    property("sonar.buildString", version)
  }
}

subprojects {
  apply(plugin = "com.github.hierynomus.license")
  apply(plugin = "jacoco")
  apply(plugin = "java-library")

  configure<JavaPluginExtension> {
    toolchain {
      languageVersion.set(JavaLanguageVersion.of(17))
    }
  }

  val libs = rootProject.extensions.getByType<VersionCatalogsExtension>().named("libs")
  dependencies {
    "testRuntimeOnly"(libs.findLibrary("junit-platform-launcher").get())
  }

  configure<JacocoPluginExtension> {
    toolVersion = libs.findVersion("jacoco").get().requiredVersion
  }

  tasks.named<JacocoReport>("jacocoTestReport") {
    reports {
      xml.required = true
      csv.required = false
      html.required = false
    }
  }

  configure<LicenseExtension> {
    header = rootProject.file("HEADER")
    strictCheck = true
    encoding = "UTF-8"
    mapping(mapOf(
      "java" to "SLASHSTAR_STYLE",
      "js" to "SLASHSTAR_STYLE",
      "ts" to "SLASHSTAR_STYLE",
      "tsx" to "SLASHSTAR_STYLE",
      "css" to "SLASHSTAR_STYLE"
    ))
    includes(listOf("**/*.java", "**/*.js", "**/*.ts", "**/*.tsx", "**/*.css"))
  }

  tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
    doFirst {
      (options as StandardJavadocDocletOptions).addBooleanOption("-no-module-directories", true)
    }
  }

  val mainSourceSet = the<SourceSetContainer>()["main"]
  tasks.register<Jar>("sourcesJar") {
    group = "build"
    description = "Assembles a jar archive containing the main sources of this project."
    dependsOn("classes")
    archiveClassifier.set("sources")
    from(mainSourceSet.allSource)
  }

  tasks.register<Jar>("javadocJar") {
    group = "build"
    description = "Assembles a jar archive containing the javadoc of this project."
    dependsOn("javadoc")
    archiveClassifier.set("javadoc")
    from(tasks.named<Javadoc>("javadoc").map { it.destinationDir!! })
  }

  rootProject.tasks["sonar"].dependsOn(tasks.named("jacocoTestReport"))

  apply(plugin = "signing")

  configure<SigningExtension> {
    val signingKeyId = project.findProperty("signingKeyId") as String?
    val signingKey = project.findProperty("signingKey") as String?
    val signingPassword = project.findProperty("signingPassword") as String?
    useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
    setRequired(provider {
      val branch = System.getenv("GITHUB_REF_NAME")
      (branch == "master" || (branch != null && branch.matches(Regex("branch-[\\d.]+")))) &&
        gradle.taskGraph.hasTask(":artifactoryPublish")
    })
  }

  // Deferred to afterEvaluate: this project's own plugins {} block (applying maven-publish via
  // the artifactory-conventions convention plugin) runs after this subprojects {} configuration.
  afterEvaluate {
    configure<SigningExtension> {
      sign(the<PublishingExtension>().publications)
    }
  }

  tasks.withType<Sign> {
    onlyIf {
      val branch = System.getenv("GITHUB_REF_NAME")
      !(tasks.named<ArtifactoryTask>("artifactoryPublish").get().isSkip) &&
        (branch == "master" || (branch != null && branch.matches(Regex("branch-[\\d.]+")))) &&
        gradle.taskGraph.hasTask(":artifactoryPublish")
    }
    dependsOn("jar")
  }
}
