# Supporting SCM providers

SonarQube Scanner uses information from the project's SCM, if available, to:

* Assign a new issue to the person who introduced it. The last committer on the related line of code is considered to be the author of the issue. 
* Estimate the coverage on New Code, including added and changed code since in your New Code.
* Display the most recent commit on each line the code viewer.
![Commit info is available from the margin of the code viewer](/images/commit-info-in-code-viewer.png)

To achieve this the Scanner relies on the "blame" SCM command, which gets the last committer of each line for a given file. 

SonarQube comes with out-of-the-box support for Git and Subversion. See the [embedded SCM integrations](https://docs.sonarqube.org/latest/analysis/scm-integration/) page for more details.

To introduce support for another SCM provider, please have a look at the `org.sonar.api.batch.scm.ScmProvider` extension point. 
