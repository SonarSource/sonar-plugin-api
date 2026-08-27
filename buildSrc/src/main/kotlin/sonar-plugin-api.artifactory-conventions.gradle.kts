plugins {
  id("com.jfrog.artifactory")
  `maven-publish`
}

artifactory {
  clientConfig.isIncludeEnvVars = true
  clientConfig.setEnvVarsExcludePatterns("*password*,*PASSWORD*,*secret*,*MAVEN_CMD_LINE_ARGS*,sun.java.command,*token*,*TOKEN*,*LOGIN*,*login*,*key*,*KEY*,*signing*")
  setContextUrl(System.getenv("ARTIFACTORY_URL"))
  publish {
    repository {
      repoKey = System.getenv("ARTIFACTORY_DEPLOY_REPO")
      username = System.getenv("ARTIFACTORY_DEPLOY_USERNAME") ?: project.findProperty("artifactoryUsername") as String?
      password = System.getenv("ARTIFACTORY_DEPLOY_PASSWORD") ?: project.findProperty("artifactoryPassword") as String?
    }
    defaults {
      setProperties(mapOf<String, String?>(
        "build.name" to "sonar-plugin-api",
        "build.number" to System.getenv("BUILD_NUMBER"),
        "pr.branch.target" to System.getenv("PULL_REQUEST_BRANCH_TARGET"),
        "pr.number" to System.getenv("PULL_REQUEST_NUMBER"),
        "vcs.branch" to System.getenv("GITHUB_BRANCH"),
        "vcs.revision" to System.getenv("GIT_COMMIT"),
        "version" to version.toString()
      ))
      publications("mavenJava")
      setPublishPom(true)
      setPublishIvy(false)
    }
  }
  clientConfig.info.buildName = "sonar-plugin-api"
  clientConfig.info.buildNumber = System.getenv("BUILD_NUMBER")
  // The name of this variable is important because it's used by the delivery process when extracting version from Artifactory build info.
  clientConfig.info.addEnvironmentProperty("PROJECT_VERSION", "$version")
}
