# For SonarSourcers:

- [ ] Create a [JIRA](http://jira.sonarsource.com/browse/PLUGINAPI) ticket if the API is impacted
- [ ] Prefix the commit message with the ticket number
- [ ] Document the change in CHANGELOG.md
- [ ] When adding a new API:
  - [ ] Explain in the JavaDoc the purpose of the new API
  - [ ] Add a `@since X.Y` in the JavaDoc
- [ ] When deprecating an API:
  - [ ] Annotate the deprecated element with `@Deprecated`
  - [ ] Add a `@deprecated since X.Y` in the JavaDoc
  - [ ] Document the replacement in the JavaDoc (if any)
- [ ] When dropping an API:
  - [ ] Make sure it respects the [deprecation policy](https://github.com/SonarSource/sonar-plugin-api/blob/master/docs/deprecation-policy.md)
  - [ ] Bump the major version (breaking change)
- [ ] Make sure checks are green: build passes, Quality Gate is green
- [ ] Merge after getting approval by at least one member of the guild
  - If no review is made within 3 days, gently ping the reviewers
  - The guild member reviewing the code can explicitly request someone else (typically another guild member representing another team) to check the impact on the specific product 
  - In some cases, the guild may deem it necessary that multiple or even all members approve a PR. This is more likely in complex changes or changes directly impacting all teams using the API.


# For external contributors:

Please be aware that we are not actively looking for feature contributions. The truth is that it's extremely difficult for someone outside SonarSource to comply with our roadmap and expectations. Therefore, we typically only accept minor cosmetic changes and typo fixes. If you would like to see a new feature, please create a new thread in the forum "Suggest new features".

With that in mind, if you would like to submit a code contribution, make sure that you adhere to the following guidelines and all tests are passing:

Please explain your motives to contribute this change: what problem you are trying to fix, what improvement you are trying to make
Use the following formatting style: SonarSource/sonar-developer-toolset
Provide a unit test for any code you changed
If there is a JIRA ticket available, please make your commits and pull request start with the ticket ID (PLUGINAPI-XXXX)
We will try to give you feedback on your contribution as quickly as possible.

Thank You! The SonarSource Team
