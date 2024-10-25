<p align="center">
  <img src="https://raw.githubusercontent.com/PKief/vscode-material-icon-theme/ec559a9f6bfd399b82bb44393651661b08aaf7ba/icons/folder-markdown-open.svg" width="20%" alt="COCO-SHORT-LINK-BG-logo">
</p>
<p align="center">
    <h1 align="center">COCO-SHORT-LINK-BG</h1>
</p>
<p align="center">
    <em>Linking Innovation, Building Growth: Unleash the Power of coco-short-link-bg!</em>
</p>
<p align="center">
	<img src="https://img.shields.io/github/license/coco40725/coco-short-link-bg?style=default&logo=opensourceinitiative&logoColor=white&color=0080ff" alt="license">
	<img src="https://img.shields.io/github/last-commit/coco40725/coco-short-link-bg?style=default&logo=git&logoColor=white&color=0080ff" alt="last-commit">
	<img src="https://img.shields.io/github/languages/top/coco40725/coco-short-link-bg?style=default&color=0080ff" alt="repo-top-language">
	<img src="https://img.shields.io/github/languages/count/coco40725/coco-short-link-bg?style=default&color=0080ff" alt="repo-language-count">
</p>
<p align="center">
	<!-- default option, no dependency badges. -->
</p>
<br>

##  Table of Contents

- [ Overview](#-overview)
- [ Features](#-features)
- [ Project Structure](#-project-structure)
  - [ Project Index](#-project-index)
- [ Getting Started](#-getting-started)
  - [ Prerequisites](#-prerequisites)
  - [ Installation](#-installation)
  - [ Usage](#-usage)
  - [ Testing](#-testing)
- [ Project Roadmap](#-project-roadmap)
- [ Contributing](#-contributing)
- [ License](#-license)
- [ Acknowledgments](#-acknowledgments)

---

##  Overview

The coco-short-link-bg project is a comprehensive software solution that streamlines the process of creating and managing shortened URLs. By leveraging a combination of technologies such as Docker, Gradle, and Kubernetes, this project offers a robust infrastructure for deploying and scaling services like MongoDB and Redis. The deployment automation scripts and CI/CD pipelines ensure seamless integration and operation, making it ideal for teams looking to efficiently manage their URL shortening services. With a focus on reliability and performance, this project caters to developers and organizations seeking a scalable and reliable solution for URL shortening needs.

---

##  Features

|      | Feature         | Summary       |
| :--- | :---:           | :---          |
| ⚙️  | **Architecture**  | <ul><li>Microservices architecture using Quarkus and Kotlin</li><li>Utilizes Docker for containerization</li><li>Integration with Google Cloud services</li></ul> |
| 🔩 | **Code Quality**  | <ul><li>Well-structured codebase with 124 Kotlin files</li><li>Uses Gradle for build automation</li><li>Includes unit tests using MockK</li></ul> |
| 📄 | **Documentation** | <ul><li>Comprehensive documentation in Kotlin and various script languages</li><li>Includes installation and usage commands for Gradle and Docker</li><li>Test commands for running unit tests</li></ul> |
| 🔌 | **Integrations**  | <ul><li>Integration with Docker for containerization</li><li>Utilizes Redis and MongoDB for data storage</li><li>Integration with Google Cloud services</li></ul> |
| 🧩 | **Modularity**    | <ul><li>Modular design with separate modules for different functionalities</li><li>Follows best practices for modularity in Quarkus and Kotlin</li><li>Encourages code reusability and maintainability</li></ul> |
| 🧪 | **Testing**       | <ul><li>Includes unit tests using MockK</li><li>Test commands provided for running tests</li><li>Ensures code reliability and correctness</li></ul> |
| ⚡️  | **Performance**   | <ul><li>Optimized performance through Quarkus framework</li><li>Efficient data handling with Redis and MongoDB</li><li>Scalable architecture for high performance</li></ul> |
| 🛡️ | **Security**      | <ul><li>Secure coding practices in Kotlin</li><li>Utilizes secure communication protocols</li><li>Follows security best practices for Docker containers</li></ul> |
| 📦 | **Dependencies**  | <ul><li>Dependencies managed using Gradle</li><li>Includes dependencies for Quarkus, Redis, MongoDB, and Google Cloud services</li><li>Ensures proper dependency management for the project</li></ul> |

---

##  Project Structure

```sh
└── coco-short-link-bg/
    ├── README.md
    ├── build.gradle
    ├── cloudbuild_test.yaml
    ├── deploy_demo.sh
    ├── docker-compose.yaml
    ├── gradle
    │   └── wrapper
    ├── gradle.properties
    ├── gradlew
    ├── gradlew.bat
    ├── k8s
    │   ├── dev-cluster-ingress.yaml
    │   └── k8s-deploy-service.yaml
    ├── setVersion.sh
    ├── settings.gradle
    └── src
        ├── main
        ├── native-test
        └── test
```


###  Project Index
<details open>
	<summary><b><code>COCO-SHORT-LINK-BG/</code></b></summary>
	<details> <!-- __root__ Submodule -->
		<summary><b>__root__</b></summary>
		<blockquote>
			<table>
			<tr>
				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/gradlew.bat'>gradlew.bat</a></b></td>
				<td>- Initiate the Gradle startup script for Windows, setting up JVM options and locating the Java executable<br>- Ensure JAVA_HOME is correctly configured for Java installation<br>- Execute Gradle with specified options and arguments.</td>
			</tr>
			<tr>
				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/docker-compose.yaml'>docker-compose.yaml</a></b></td>
				<td>- Orchestrates Docker containers for a multi-service environment with MongoDB, Redis, and Gradle<br>- Manages dependencies and health checks for seamless integration and operation<br>- Facilitates running and testing services with defined ports and configurations.</td>
			</tr>
			<tr>
				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/deploy_demo.sh'>deploy_demo.sh</a></b></td>
				<td>- Automates deployment process by building, tagging, and pushing Docker image to Artifact Registry<br>- Ensures correct permissions and deploys new version to Kubernetes cluster.</td>
			</tr>
			<tr>
				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/cloudbuild_test.yaml'>cloudbuild_test.yaml</a></b></td>
				<td>- Implement a Cloud Build pipeline that automates version management, secret loading, submodule updates, Docker image building, and Kubernetes deployment<br>- The pipeline orchestrates these steps in a sequence, ensuring efficient and reliable CI/CD processes for the project.</td>
			</tr>
			<tr>
				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/build.gradle'>build.gradle</a></b></td>
				<td>Generates code coverage metrics by parsing Jacoco report CSV file, calculating instruction and branch coverage percentages for each class, and providing overall coverage percentages for the entire codebase.</td>
			</tr>
			<tr>
				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/settings.gradle'>settings.gradle</a></b></td>
				<td>- Manages plugin repositories and versions for the project, including Quarkus and Kotlin plugins<br>- Sets up the project name as 'coco-short-link-bg' and includes a toolchains resolver convention plugin.</td>
			</tr>
			<tr>
				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/setVersion.sh'>setVersion.sh</a></b></td>
				<td>Update project version in build.gradle using setVersion.sh script.</td>
			</tr>
			</table>
		</blockquote>
	</details>
	<details> <!-- src Submodule -->
		<summary><b>src</b></summary>
		<blockquote>
			<details>
				<summary><b>test</b></summary>
				<blockquote>
					<details>
						<summary><b>kotlin</b></summary>
						<blockquote>
							<details>
								<summary><b>com</b></summary>
								<blockquote>
									<details>
										<summary><b>coco</b></summary>
										<blockquote>
											<details>
												<summary><b>integration</b></summary>
												<blockquote>
													<details>
														<summary><b>infra</b></summary>
														<blockquote>
															<details>
																<summary><b>repo</b></summary>
																<blockquote>
																	<table>
																	<tr>
																		<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/test/kotlin/com/coco/integration/infra/repo/SetupData.kt'>SetupData.kt</a></b></td>
																		<td>- Manages setup and cleanup of test data for unit tests by interacting with MongoDB and Redis<br>- Initializes test DB with sample data and cleans up after tests<br>- Ensures data readiness by waiting before proceeding<br>- Facilitates smooth execution of unit tests by handling data setup and teardown operations efficiently.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/test/kotlin/com/coco/integration/infra/repo/LinkInfoExpireTTLRepoTest.kt'>LinkInfoExpireTTLRepoTest.kt</a></b></td>
																		<td>- Tests the functionality of creating, updating, and finding link information with expiration dates in the LinkInfoExpireTTLRepo class<br>- Validates successful and unsuccessful scenarios for finding and updating link information based on provided parameters.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/test/kotlin/com/coco/integration/infra/repo/LinkInfoRepoTest.kt'>LinkInfoRepoTest.kt</a></b></td>
																		<td>Tests the functionality of the LinkInfoRepo class by validating methods for converting data to documents, retrieving data by various criteria, inserting data, and removing expired dates.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/test/kotlin/com/coco/integration/infra/repo/ErrorLogRepoTest.kt'>ErrorLogRepoTest.kt</a></b></td>
																		<td>Tests the functionality of adding an error log to the repository using QuarkusTest.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/test/kotlin/com/coco/integration/infra/repo/RedisRepoTest.kt'>RedisRepoTest.kt</a></b></td>
																		<td>- Tests various RedisRepo methods for key-value operations, ensuring correct behavior in different scenarios<br>- Validates retrieval, setting, updating, and deleting keys, as well as working with lists<br>- Verifies handling of existing and non-existing keys and fields, returning expected results or exceptions.</td>
																	</tr>
																	</table>
																</blockquote>
															</details>
														</blockquote>
													</details>
													<details>
														<summary><b>application</b></summary>
														<blockquote>
															<details>
																<summary><b>cqrs</b></summary>
																<blockquote>
																	<details>
																		<summary><b>query</b></summary>
																		<blockquote>
																			<details>
																				<summary><b>getUserShortLinkInfo</b></summary>
																				<blockquote>
																					<table>
																					<tr>
																						<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/test/kotlin/com/coco/integration/application/cqrs/query/getUserShortLinkInfo/GetUserShortLinkInfoValidateTest.kt'>GetUserShortLinkInfoValidateTest.kt</a></b></td>
																						<td>- Validates user authentication tokens for accessing short link information, ensuring query integrity<br>- The code tests various scenarios, such as valid, null, and invalid tokens, returning appropriate exceptions<br>- This functionality enhances security measures within the project's query processing flow.</td>
																					</tr>
																					</table>
																				</blockquote>
																			</details>
																			<details>
																				<summary><b>getUserLinkStat</b></summary>
																				<blockquote>
																					<table>
																					<tr>
																						<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/test/kotlin/com/coco/integration/application/cqrs/query/getUserLinkStat/GetUserLinkStatValidateTest.kt'>GetUserLinkStatValidateTest.kt</a></b></td>
																						<td>- Validates user input for fetching link statistics, ensuring data integrity and security<br>- The code tests various scenarios like valid queries, blank or null inputs, and invalid tokens, handling exceptions appropriately<br>- This validation process is crucial for maintaining system reliability and preventing potential issues in the application.</td>
																					</tr>
																					</table>
																				</blockquote>
																			</details>
																		</blockquote>
																	</details>
																	<details>
																		<summary><b>command</b></summary>
																		<blockquote>
																			<details>
																				<summary><b>addLinkInfo</b></summary>
																				<blockquote>
																					<table>
																					<tr>
																						<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/test/kotlin/com/coco/integration/application/cqrs/command/addLinkInfo/AddLinkInfoValidateTest.kt'>AddLinkInfoValidateTest.kt</a></b></td>
																						<td>- Validates various scenarios for adding link information, ensuring correct short link formats, original link validity, and token verification<br>- Handles exceptions for existing short links and invalid tokens<br>- Integrates with external services for token verification and link management<br>- Maintains data integrity and security during the link addition process.</td>
																					</tr>
																					</table>
																				</blockquote>
																			</details>
																			<details>
																				<summary><b>changeExpireDate</b></summary>
																				<blockquote>
																					<table>
																					<tr>
																						<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/test/kotlin/com/coco/integration/application/cqrs/command/changeExpireDate/ChangeExpireDateValidateTest.kt'>ChangeExpireDateValidateTest.kt</a></b></td>
																						<td>- Validates user authentication and command data for changing expiration dates<br>- Tests various scenarios like valid command, invalid object ID, null JWT, and invalid token, ensuring proper validation exceptions are thrown<br>- This file plays a crucial role in maintaining data integrity and security within the application's command processing flow.</td>
																					</tr>
																					</table>
																				</blockquote>
																			</details>
																			<details>
																				<summary><b>changeOriginLink</b></summary>
																				<blockquote>
																					<table>
																					<tr>
																						<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/test/kotlin/com/coco/integration/application/cqrs/command/changeOriginLink/ChangeOriginLinkLinkValidateTest.kt'>ChangeOriginLinkLinkValidateTest.kt</a></b></td>
																						<td>- Validates user authentication and origin link for a change request, ensuring data integrity and security<br>- The code tests various scenarios to handle valid and invalid inputs, safeguarding the application against potential vulnerabilities.</td>
																					</tr>
																					</table>
																				</blockquote>
																			</details>
																			<details>
																				<summary><b>disabledLinkInfo</b></summary>
																				<blockquote>
																					<table>
																					<tr>
																						<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/test/kotlin/com/coco/integration/application/cqrs/command/disabledLinkInfo/DisabledLinkInfoValidateTest.kt'>DisabledLinkInfoValidateTest.kt</a></b></td>
																						<td>- Validates DisabledLinkInfoCommand inputs against user authentication and returns appropriate validation results<br>- The code ensures that the provided command contains valid user information and handles various scenarios such as invalid or null tokens, triggering exceptions when necessary.</td>
																					</tr>
																					</table>
																				</blockquote>
																			</details>
																			<details>
																				<summary><b>enabledLinkInfo</b></summary>
																				<blockquote>
																					<table>
																					<tr>
																						<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/test/kotlin/com/coco/integration/application/cqrs/command/enabledLinkInfo/EnabledLinkInfoValidateTest.kt'>EnabledLinkInfoValidateTest.kt</a></b></td>
																						<td>- Validates enabled link information commands, ensuring proper token verification and short link expiration checks<br>- Handles scenarios such as valid commands returning results, invalid object IDs triggering exceptions, and null or invalid tokens leading to validation failures<br>- This test suite guarantees the robustness of the enabled link information validation process within the codebase architecture.</td>
																					</tr>
																					</table>
																				</blockquote>
																			</details>
																		</blockquote>
																	</details>
																</blockquote>
															</details>
															<details>
																<summary><b>ctrl</b></summary>
																<blockquote>
																	<details>
																		<summary><b>userLinkInfoCtrl</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/test/kotlin/com/coco/integration/application/ctrl/userLinkInfoCtrl/GetUserShortLinkStatCtrlTest.kt'>GetUserShortLinkStatCtrlTest.kt</a></b></td>
																				<td>Tests the validation and response handling for user short link statistics, ensuring proper authentication and error messages are returned based on token validity and short link format.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/test/kotlin/com/coco/integration/application/ctrl/userLinkInfoCtrl/GetUserShortLinkInfoCtrlTest.kt'>GetUserShortLinkInfoCtrlTest.kt</a></b></td>
																				<td>- Tests the user link information controller to ensure valid and invalid token scenarios return the appropriate HTTP status codes and messages<br>- Validates user authentication using a mocked token verification service and asserts responses for different token conditions<br>- This test file contributes to maintaining the reliability and security of the user link information functionality within the project architecture.</td>
																			</tr>
																			</table>
																		</blockquote>
																	</details>
																	<details>
																		<summary><b>linkInfoCtrl</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/test/kotlin/com/coco/integration/application/ctrl/linkInfoCtrl/ChangeExpireDateCtrlTest.kt'>ChangeExpireDateCtrlTest.kt</a></b></td>
																				<td>- Tests the functionality of changing expiration dates for user IDs, handling various scenarios like existing and non-existing IDs, invalid IDs, and token verification<br>- The code ensures proper responses are returned based on the validity of the provided user ID and token, maintaining the integrity of the system's security and data management.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/test/kotlin/com/coco/integration/application/ctrl/linkInfoCtrl/ChangeOriginLinkCtrlTest.kt'>ChangeOriginLinkCtrlTest.kt</a></b></td>
																				<td>- Tests the functionality of changing the origin link information, ensuring proper handling of valid, non-existent, and invalid scenarios<br>- Validates user authentication, token verification, and repository interactions, maintaining data integrity and error handling within the application's link information control module.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/test/kotlin/com/coco/integration/application/ctrl/linkInfoCtrl/RedirectToOriginalLinkCtrlTest.kt'>RedirectToOriginalLinkCtrlTest.kt</a></b></td>
																				<td>- Tests the redirection behavior of existing and non-existing links, ensuring proper status codes are returned<br>- Integrates with setup data and a pub/sub service for link logging.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/test/kotlin/com/coco/integration/application/ctrl/linkInfoCtrl/DisabledLinkInfoCtrlTest.kt'>DisabledLinkInfoCtrlTest.kt</a></b></td>
																				<td>- Tests the functionality of disabling link information in the application by sending various requests with different scenarios and verifying the expected responses<br>- The test cases cover valid and invalid scenarios, token verification, handling non-existent links, and error cases related to repository updates.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/test/kotlin/com/coco/integration/application/ctrl/linkInfoCtrl/AddLinkInfoCtrlTest.kt'>AddLinkInfoCtrlTest.kt</a></b></td>
																				<td>- Tests the addition of link information, including validation for various scenarios, such as valid and invalid links, existing links, expiration dates, and tokens<br>- Handles exceptions for failures in inserting data into repositories and Redis<br>- Validates user authentication and token verification using mock data<br>- Overall, ensures the robustness of the link information addition functionality within the project architecture.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/test/kotlin/com/coco/integration/application/ctrl/linkInfoCtrl/EnabledLinkInfoCtrlTest.kt'>EnabledLinkInfoCtrlTest.kt</a></b></td>
																				<td>Tests the functionality of enabling and disabling link information, validating user tokens, and handling various error scenarios related to token verification and repository updates within the integration layer of the application.</td>
																			</tr>
																			</table>
																		</blockquote>
																	</details>
																</blockquote>
															</details>
															<details>
																<summary><b>service</b></summary>
																<blockquote>
																	<table>
																	<tr>
																		<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/test/kotlin/com/coco/integration/application/service/CompensationServiceTest.kt'>CompensationServiceTest.kt</a></b></td>
																		<td>- Tests the `CompensationService` by simulating successful and failed function executions, ensuring correct order and error logging functionality<br>- The test suite validates the service's ability to handle compensation actions effectively within the project's architecture.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/test/kotlin/com/coco/integration/application/service/LinkManagementServiceTest.kt'>LinkManagementServiceTest.kt</a></b></td>
																		<td>- Tests the functionality of adding, disabling, enabling, and changing original links in the Link Management Service<br>- Validates data persistence in MongoDB and Redis, ensuring successful operations and error handling.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/test/kotlin/com/coco/integration/application/service/UserLinkStatManagementServiceTest.kt'>UserLinkStatManagementServiceTest.kt</a></b></td>
																		<td>- Tests the UserLinkStatManagementService to ensure accurate statistics retrieval based on different scenarios<br>- Verifies the correct handling of zero log returns and multiple log returns, validating data such as total counts, refer counts, IP counts, and user agent counts.</td>
																	</tr>
																	</table>
																</blockquote>
															</details>
														</blockquote>
													</details>
												</blockquote>
											</details>
											<details>
												<summary><b>unit</b></summary>
												<blockquote>
													<details>
														<summary><b>domain</b></summary>
														<blockquote>
															<details>
																<summary><b>service</b></summary>
																<blockquote>
																	<table>
																	<tr>
																		<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/test/kotlin/com/coco/unit/domain/service/Base62ShortUrlGeneratorTest.kt'>Base62ShortUrlGeneratorTest.kt</a></b></td>
																		<td>Tests the functionality of generating short URLs with specific sizes and ensures uniqueness, validating the generated URLs against the configured website domain.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/test/kotlin/com/coco/unit/domain/service/LinkInfoSvcTest.kt'>LinkInfoSvcTest.kt</a></b></td>
																		<td>- Validates link formats, original link length, and expiration date for a service in the domain layer<br>- Uses mocks for dependencies and Quarkus testing.</td>
																	</tr>
																	</table>
																</blockquote>
															</details>
														</blockquote>
													</details>
												</blockquote>
											</details>
										</blockquote>
									</details>
								</blockquote>
							</details>
						</blockquote>
					</details>
				</blockquote>
			</details>
			<details>
				<summary><b>native-test</b></summary>
				<blockquote>
					<details>
						<summary><b>kotlin</b></summary>
						<blockquote>
							<details>
								<summary><b>com</b></summary>
								<blockquote>
									<details>
										<summary><b>coco</b></summary>
										<blockquote>
											<table>
											<tr>
												<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/native-test/kotlin/com/coco/GreetingResourceIT.kt'>GreetingResourceIT.kt</a></b></td>
												<td>Verifies the integration of the GreetingResource by extending the GreetingResourceTest class.</td>
											</tr>
											</table>
										</blockquote>
									</details>
								</blockquote>
							</details>
						</blockquote>
					</details>
				</blockquote>
			</details>
			<details>
				<summary><b>main</b></summary>
				<blockquote>
					<details>
						<summary><b>kotlin</b></summary>
						<blockquote>
							<details>
								<summary><b>com</b></summary>
								<blockquote>
									<details>
										<summary><b>coco</b></summary>
										<blockquote>
											<table>
											<tr>
												<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/GreetingResource.kt'>GreetingResource.kt</a></b></td>
												<td>- Defines a REST endpoint for greeting messages in the Quarkus application<br>- The GreetingResource class handles HTTP GET requests to "/hello" and responds with a plain text message<br>- This file plays a crucial role in exposing the greeting functionality to clients through the REST API.</td>
											</tr>
											</table>
											<details>
												<summary><b>infra</b></summary>
												<blockquote>
													<details>
														<summary><b>bigQuery</b></summary>
														<blockquote>
															<table>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/infra/bigQuery/ShortLinkBigQueryException.kt'>ShortLinkBigQueryException.kt</a></b></td>
																<td>Defines a custom exception class for handling errors related to short link operations in BigQuery within the project's infrastructure module.</td>
															</tr>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/infra/bigQuery/ShortLinkBigQuery.kt'>ShortLinkBigQuery.kt</a></b></td>
																<td>- Retrieves link log data from BigQuery based on a provided short link URL<br>- Utilizes the BigQuery service and configuration to execute a query, retrieve results, and map them to LinkLog objects<br>- Implements error handling and job monitoring for robust data retrieval.</td>
															</tr>
															</table>
														</blockquote>
													</details>
													<details>
														<summary><b>listener</b></summary>
														<blockquote>
															<table>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/infra/listener/LinkInfoExpireTTLListener.kt'>LinkInfoExpireTTLListener.kt</a></b></td>
																<td>- Implements a listener that monitors and handles expiration events for link information in the system<br>- Monitors changes in the "LinkInfoExpireTTL" collection, updating link information and cache accordingly when a deletion operation is detected<br>- This functionality ensures that expired link information is disabled and removed from the cache to maintain data integrity and system performance.</td>
															</tr>
															</table>
														</blockquote>
													</details>
													<details>
														<summary><b>grpc</b></summary>
														<blockquote>
															<table>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/infra/grpc/VerifyTokenGrpc.kt'>VerifyTokenGrpc.kt</a></b></td>
																<td>- Enables token verification via gRPC communication, handling exceptions and mapping response data to User model<br>- Integrates with VerifyTokenSvc client to verify tokens and recover from connection failures<br>- Supports the project's architecture by providing a scalable and reliable token verification service.</td>
															</tr>
															</table>
															<details>
																<summary><b>hello</b></summary>
																<blockquote>
																	<table>
																	<tr>
																		<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/infra/grpc/hello/HelloService.kt'>HelloService.kt</a></b></td>
																		<td>- Enables communication with a gRPC server to send a greeting message<br>- The code defines a service that interacts with the server to handle requests for saying hello to the server<br>- This functionality is crucial for facilitating client-server interactions within the project's architecture.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/infra/grpc/hello/HelloCtrl.kt'>HelloCtrl.kt</a></b></td>
																		<td>- Handles incoming HTTP requests to `/test/{name}` by invoking the `HelloService` to greet the user<br>- The `HelloCtrl` class defines a method to say hello to the server using the provided name parameter<br>- This file plays a crucial role in processing user requests and interacting with the underlying service in the project architecture.</td>
																	</tr>
																	</table>
																</blockquote>
															</details>
														</blockquote>
													</details>
													<details>
														<summary><b>pubsub</b></summary>
														<blockquote>
															<table>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/infra/pubsub/LinkLogPubSubSvc.kt'>LinkLogPubSubSvc.kt</a></b></td>
																<td>- Manages PubSub communication for LinkLog data, handling message reception and publication<br>- Subscribes to a topic to save data to MongoDB and publishes messages to another topic<br>- Converts data to JSON for publishing and vice versa for insertion.</td>
															</tr>
															</table>
														</blockquote>
													</details>
													<details>
														<summary><b>exception</b></summary>
														<blockquote>
															<table>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/infra/exception/RepoException.kt'>RepoException.kt</a></b></td>
																<td>Defines a base class for repository exceptions and increments a counter for tracking exceptions.</td>
															</tr>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/infra/exception/GrpcExceptionMapper.kt'>GrpcExceptionMapper.kt</a></b></td>
																<td>Maps gRPC connection exceptions to HTTP responses, ensuring graceful error handling in the project's infrastructure layer.</td>
															</tr>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/infra/exception/GrpcException.kt'>GrpcException.kt</a></b></td>
																<td>- The code file `GrpcException.kt` defines a base class and a specific exception class for handling gRPC connection exceptions<br>- It includes a method to increment a counter for tracking these exceptions<br>- This code contributes to the project's infrastructure by providing a structured approach to managing and monitoring gRPC-related errors.</td>
															</tr>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/infra/exception/RepoExceptionMapper.kt'>RepoExceptionMapper.kt</a></b></td>
																<td>- The `RepoExceptionMapper` class in the provided codebase handles exceptions specific to repository operations by mapping them to HTTP responses<br>- This component plays a crucial role in ensuring that any repository-related errors are appropriately handled and communicated to clients, contributing to the overall robustness and reliability of the system.</td>
															</tr>
															</table>
														</blockquote>
													</details>
													<details>
														<summary><b>util</b></summary>
														<blockquote>
															<table>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/infra/util/Log.kt'>Log.kt</a></b></td>
																<td>- Facilitates logging functionality across the codebase by defining different log levels and formatting log messages<br>- Logs include timestamps, class names, and customizable messages<br>- The code ensures consistent and structured logging for debugging and error tracking purposes.</td>
															</tr>
															</table>
														</blockquote>
													</details>
													<details>
														<summary><b>constant</b></summary>
														<blockquote>
															<table>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/infra/constant/RedisConstant.kt'>RedisConstant.kt</a></b></td>
																<td>Define essential constants for Redis operations in the project, such as the original link field and the white short link key.</td>
															</tr>
															</table>
														</blockquote>
													</details>
													<details>
														<summary><b>client</b></summary>
														<blockquote>
															<table>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/infra/client/VerifyTokenClient.kt'>VerifyTokenClient.kt</a></b></td>
																<td>Enables verification of user tokens through a client interface, contributing to the project's infrastructure layer.</td>
															</tr>
															</table>
														</blockquote>
													</details>
													<details>
														<summary><b>restClient</b></summary>
														<blockquote>
															<table>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/infra/restClient/VerifyTokenRestClient.kt'>VerifyTokenRestClient.kt</a></b></td>
																<td>Implements a REST client for verifying JWT tokens, facilitating communication with an external service.</td>
															</tr>
															</table>
														</blockquote>
													</details>
													<details>
														<summary><b>config</b></summary>
														<blockquote>
															<table>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/infra/config/WebConfig.kt'>WebConfig.kt</a></b></td>
																<td>Defines configuration settings for the web module, specifically the website domain.</td>
															</tr>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/infra/config/BigQueryConfig.kt'>BigQueryConfig.kt</a></b></td>
																<td>Define BigQuery configuration properties for the project to access BigQuery database and log table settings.</td>
															</tr>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/infra/config/MongoConfig.kt'>MongoConfig.kt</a></b></td>
																<td>Defines configuration for MongoDB database connection in the project architecture.</td>
															</tr>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/infra/config/PubSubConfig.kt'>PubSubConfig.kt</a></b></td>
																<td>Define PubSub configuration interface for linking and subscribing to topics in the project architecture.</td>
															</tr>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/infra/config/JwtConfig.kt'>JwtConfig.kt</a></b></td>
																<td>Define JWT configuration interface for handling secret key retrieval in the project architecture.</td>
															</tr>
															</table>
														</blockquote>
													</details>
													<details>
														<summary><b>repo</b></summary>
														<blockquote>
															<table>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/infra/repo/LinkInfoExpireTTLRepo.kt'>LinkInfoExpireTTLRepo.kt</a></b></td>
																<td>- Manages expiration time for link information in the MongoDB database, ensuring data integrity and efficient storage<br>- Implements methods to find and update link information based on specified criteria, utilizing reactive MongoDB client and session handling for optimal performance.</td>
															</tr>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/infra/repo/LinkLogRepo.kt'>LinkLogRepo.kt</a></b></td>
																<td>- Manages MongoDB interactions for LinkLog entities, converting objects to documents and vice versa<br>- Inserts a single LinkLog entity into the database and returns the inserted entity.</td>
															</tr>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/infra/repo/ErrorLogRepo.kt'>ErrorLogRepo.kt</a></b></td>
																<td>- Manages error log data persistence in MongoDB using reactive operations<br>- Handles adding a single error log entry to the database, ensuring acknowledgment upon successful insertion<br>- Integrated with Quarkus and SmallRye Mutiny for efficient and scalable error logging functionality within the project architecture.</td>
															</tr>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/infra/repo/RedisRepo.kt'>RedisRepo.kt</a></b></td>
																<td>- Manages Redis operations for caching data, including setting, updating, and deleting hash values, retrieving lists, and manipulating list elements<br>- Ensures data integrity and handles exceptions for cache operations<br>- Integrates with Quarkus Redis DataSource for reactive functionality.</td>
															</tr>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/infra/repo/LinkInfoRepo.kt'>LinkInfoRepo.kt</a></b></td>
																<td>- Manages MongoDB operations for storing and retrieving LinkInfo objects<br>- Handles creation, retrieval, updating, and deletion of LinkInfo entities based on various criteria like short link, user ID, and expiration date<br>- Implements error handling for data manipulation operations.</td>
															</tr>
															</table>
														</blockquote>
													</details>
												</blockquote>
											</details>
											<details>
												<summary><b>domain</b></summary>
												<blockquote>
													<details>
														<summary><b>vo</b></summary>
														<blockquote>
															<table>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/domain/vo/RequestHeaderData.kt'>RequestHeaderData.kt</a></b></td>
																<td>- Defines a data class to capture request header information for a specific HTTP request<br>- The class extracts user agent, referer, client IP, host, and request URL from the incoming request context, encapsulating this data for further processing within the application.</td>
															</tr>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/domain/vo/LinkStat.kt'>LinkStat.kt</a></b></td>
																<td>- Defines a data structure for tracking link statistics, including total counts, short link details, and various user interaction metrics<br>- The LinkStat class encapsulates essential data points for analyzing link performance within the project's domain model.</td>
															</tr>
															</table>
															<details>
																<summary><b>pubsub</b></summary>
																<blockquote>
																	<table>
																	<tr>
																		<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/domain/vo/pubsub/LinkLogData.kt'>LinkLogData.kt</a></b></td>
																		<td>- Defines a data class `LinkLogData` that encapsulates information about a short link and associated request header data<br>- This class plays a crucial role in representing and managing link log data within the project's domain model, facilitating seamless handling and processing of link-related information.</td>
																	</tr>
																	</table>
																</blockquote>
															</details>
														</blockquote>
													</details>
													<details>
														<summary><b>model</b></summary>
														<blockquote>
															<table>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/domain/model/User.kt'>User.kt</a></b></td>
																<td>- Defines the User data model for the project, encapsulating essential user information such as id, name, email, password, and email verification status<br>- This model plays a crucial role in representing and managing user data within the codebase architecture.</td>
															</tr>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/domain/model/ErrorLog.kt'>ErrorLog.kt</a></b></td>
																<td>- Defines a data structure for error logs in the project, capturing details like function name, parameters, and creation date<br>- This model plays a crucial role in representing and managing error information within the system's domain.</td>
															</tr>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/domain/model/LinkInfo.kt'>LinkInfo.kt</a></b></td>
																<td>- Defines a data structure for storing link information within the project's domain model<br>- The LinkInfo class encapsulates details such as short link, user ID, original link, expiration date, and metadata<br>- It plays a crucial role in representing and managing link-related data within the codebase architecture.</td>
															</tr>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/domain/model/LinkLog.kt'>LinkLog.kt</a></b></td>
																<td>- Defines a data structure for storing link log information, including details like short link, referer IP, user agent, and creation date<br>- This model encapsulates essential attributes for tracking link usage within the project's domain architecture.</td>
															</tr>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/domain/model/CompensationActions.kt'>CompensationActions.kt</a></b></td>
																<td>Define compensation actions structure for domain model with function name, parameters, and action to return a Uni.</td>
															</tr>
															</table>
														</blockquote>
													</details>
													<details>
														<summary><b>service</b></summary>
														<blockquote>
															<details>
																<summary><b>linkInfo</b></summary>
																<blockquote>
																	<table>
																	<tr>
																		<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/domain/service/linkInfo/ShortUrlGenerator.kt'>ShortUrlGenerator.kt</a></b></td>
																		<td>Generates short URLs of specified size for the link info service in the project architecture.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/domain/service/linkInfo/LinkInfoSvc.kt'>LinkInfoSvc.kt</a></b></td>
																		<td>- Validates and generates short URLs for the project's link information service<br>- Checks short link format, original link format, and expiration date validity<br>- Utilizes a short URL generator and web configuration for validation.</td>
																	</tr>
																	</table>
																	<details>
																		<summary><b>impl</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/domain/service/linkInfo/impl/Base62ShortUrlGenerator.kt'>Base62ShortUrlGenerator.kt</a></b></td>
																				<td>- Generates unique 7-character short URLs by converting random numbers to base62<br>- Utilizes a web configuration to construct short links with the website domain.</td>
																			</tr>
																			</table>
																		</blockquote>
																	</details>
																</blockquote>
															</details>
														</blockquote>
													</details>
												</blockquote>
											</details>
											<details>
												<summary><b>application</b></summary>
												<blockquote>
													<details>
														<summary><b>exception</b></summary>
														<blockquote>
															<table>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/exception/QueryExceptionMapper.kt'>QueryExceptionMapper.kt</a></b></td>
																<td>Handles various query-related exceptions by mapping them to appropriate HTTP responses, ensuring robust error handling in the application.</td>
															</tr>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/exception/CommandExceptionMapper.kt'>CommandExceptionMapper.kt</a></b></td>
																<td>- CommandExceptionMapper handles different types of exceptions related to commands in the application<br>- It maps NoSuchCommandException to an internal server error response, CommandHandlerCastException to an internal server error response, and CommandValidationException to a bad request response<br>- This class ensures proper error handling for command-related issues in the codebase architecture.</td>
															</tr>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/exception/ApplicationException.kt'>ApplicationException.kt</a></b></td>
																<td>- The code file `ApplicationException.kt` defines a base exception class and a specific exception class for application errors<br>- It includes a method to increment a counter for tracking exceptions<br>- This file plays a crucial role in managing and tracking application-specific exceptions within the project architecture.</td>
															</tr>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/exception/QueryException.kt'>QueryException.kt</a></b></td>
																<td>- Defines and handles query-related exceptions by incrementing a counter for each exception type<br>- The code contributes to monitoring and tracking exceptions in the application, enhancing observability and troubleshooting capabilities.</td>
															</tr>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/exception/ApplicationExceptionMapper.kt'>ApplicationExceptionMapper.kt</a></b></td>
																<td>Handles and maps application exceptions to HTTP responses within the project's architecture, ensuring a consistent error handling approach.</td>
															</tr>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/exception/CommandException.kt'>CommandException.kt</a></b></td>
																<td>- Defines and increments exception counters for various command-related exceptions in the application<br>- The code enhances observability by tracking exception occurrences, aiding in identifying and resolving issues within the system.</td>
															</tr>
															</table>
														</blockquote>
													</details>
													<details>
														<summary><b>middleware</b></summary>
														<blockquote>
															<details>
																<summary><b>header</b></summary>
																<blockquote>
																	<table>
																	<tr>
																		<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/middleware/header/RequestHeaderFilter.kt'>RequestHeaderFilter.kt</a></b></td>
																		<td>- Implements a middleware filter to extract and set request header data for the application<br>- This component integrates with the project's architecture to manage incoming requests and populate the necessary header information for downstream processing.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/middleware/header/GetHeader.kt'>GetHeader.kt</a></b></td>
																		<td>Defines a custom annotation `GetHeader` for binding to classes and functions, enhancing middleware functionality for handling HTTP headers in the project architecture.</td>
																	</tr>
																	</table>
																</blockquote>
															</details>
															<details>
																<summary><b>auth</b></summary>
																<blockquote>
																	<table>
																	<tr>
																		<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/middleware/auth/Logged.kt'>Logged.kt</a></b></td>
																		<td>- Defines a custom annotation for logging functionality in the authentication middleware of the project<br>- The `Logged` annotation is used to mark classes or functions where logging should be applied at runtime.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/middleware/auth/JwtRequest.kt'>JwtRequest.kt</a></b></td>
																		<td>Handles JWT token initialization for request authentication in the middleware layer of the application.</td>
																	</tr>
																	<tr>
																		<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/middleware/auth/JwtRequestFilter.kt'>JwtRequestFilter.kt</a></b></td>
																		<td>- Implements JWT authentication for incoming requests, validating tokens stored in cookies<br>- Prioritizes authorization checks and integrates with the application's middleware layer.</td>
																	</tr>
																	</table>
																</blockquote>
															</details>
														</blockquote>
													</details>
													<details>
														<summary><b>cqrs</b></summary>
														<blockquote>
															<table>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/DefaultActionExecutor.kt'>DefaultActionExecutor.kt</a></b></td>
																<td>- Facilitates command and query execution by mapping handlers and validators for each<br>- Handles command and query validation, ensuring proper casting and error handling<br>- Integrates with the project's CQRS architecture to streamline action execution.</td>
															</tr>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/ActionExecutor.kt'>ActionExecutor.kt</a></b></td>
																<td>- Facilitates execution and validation of commands and queries within the CQRS architecture, ensuring proper handling of business logic<br>- The ActionExecutor interface defines methods for executing and validating commands and queries, contributing to the seamless flow of data processing in the project's CQRS module.</td>
															</tr>
															</table>
															<details>
																<summary><b>query</b></summary>
																<blockquote>
																	<details>
																		<summary><b>base</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/query/base/QueryValidateResult.kt'>QueryValidateResult.kt</a></b></td>
																				<td>- Defines a base class for query validation results in the project's CQRS architecture<br>- This class plays a crucial role in handling and validating queries within the application, ensuring data integrity and consistency.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/query/base/QueryHandler.kt'>QueryHandler.kt</a></b></td>
																				<td>Defines a contract for handling queries in the project's CQRS architecture, ensuring separation of concerns and promoting code reusability.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/query/base/QueryValidator.kt'>QueryValidator.kt</a></b></td>
																				<td>- Validates queries for the application's CQRS architecture, ensuring data integrity and consistency<br>- The QueryValidator interface defines a method to validate incoming queries, returning a result indicating whether the query is valid or not<br>- This component plays a crucial role in maintaining the overall integrity of the system's query processing functionality.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/query/base/Query.kt'>Query.kt</a></b></td>
																				<td>Defines a base interface for queries in the project's CQRS architecture, facilitating the separation of read and write operations.</td>
																			</tr>
																			</table>
																		</blockquote>
																	</details>
																	<details>
																		<summary><b>getUserShortLinkInfo</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/query/getUserShortLinkInfo/GetUserShortLinkInfoResult.kt'>GetUserShortLinkInfoResult.kt</a></b></td>
																				<td>- Define the structure for retrieving user short link information, including enabled and disabled links<br>- The code file encapsulates data classes for user short link details, such as link ID, short link, original link, expiration date, and creation date.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/query/getUserShortLinkInfo/GetUserShortLinkInfoHandler.kt'>GetUserShortLinkInfoHandler.kt</a></b></td>
																				<td>- Handles retrieving short link information for a user by partitioning enabled and disabled links<br>- Maps the data to a structured result for presentation.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/query/getUserShortLinkInfo/GetUserShortLinkInfoQuery.kt'>GetUserShortLinkInfoQuery.kt</a></b></td>
																				<td>- Defines a query to retrieve short link information for a user based on their JWT token<br>- This code file encapsulates the logic for fetching user-specific short link details, contributing to the project's CQRS architecture by separating read operations from write operations.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/query/getUserShortLinkInfo/GetUserShortLinkInfoValidateResult.kt'>GetUserShortLinkInfoValidateResult.kt</a></b></td>
																				<td>- Defines a data class for validating user short link information in the project's CQRS query module<br>- It encapsulates the result of validating user data, contributing to the overall architecture's query functionality.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/query/getUserShortLinkInfo/GetUserShortLinkInfoValidate.kt'>GetUserShortLinkInfoValidate.kt</a></b></td>
																				<td>- Validates user short link information by verifying the JWT token's validity<br>- If the token is null or invalid, it throws a QueryValidationException<br>- The class implements QueryValidator for GetUserShortLinkInfoQuery and uses VerifyTokenClient for token verification.</td>
																			</tr>
																			</table>
																		</blockquote>
																	</details>
																	<details>
																		<summary><b>getUserLinkStat</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/query/getUserLinkStat/GetUserLinkStatResult.kt'>GetUserLinkStatResult.kt</a></b></td>
																				<td>Defines the data structure for storing user link statistics, including total count, short link, refer count, IP count, user agent count, and creation date.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/query/getUserLinkStat/GetUserLinkStatQuery.kt'>GetUserLinkStatQuery.kt</a></b></td>
																				<td>Facilitates querying user link statistics by handling requests and authentication through JWT tokens.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/query/getUserLinkStat/GetUserLinkStatValidate.kt'>GetUserLinkStatValidate.kt</a></b></td>
																				<td>- Validates user input for link statistics retrieval, ensuring the link format is valid and the JWT token is present and verified<br>- If the link or token is invalid, appropriate exceptions are raised.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/query/getUserLinkStat/GetUserLinkStatValidateResult.kt'>GetUserLinkStatValidateResult.kt</a></b></td>
																				<td>- Validates and returns user link statistics for the application's query functionality<br>- This data class encapsulates the validation result for fetching user link statistics, ensuring the integrity of the data retrieved<br>- It plays a crucial role in maintaining the accuracy and reliability of user-related information within the codebase architecture.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/query/getUserLinkStat/GetUserLinkStatHandler.kt'>GetUserLinkStatHandler.kt</a></b></td>
																				<td>Handles querying user link statistics by retrieving and formatting data from the UserLinkStatManagementService.</td>
																			</tr>
																			</table>
																		</blockquote>
																	</details>
																</blockquote>
															</details>
															<details>
																<summary><b>command</b></summary>
																<blockquote>
																	<details>
																		<summary><b>addLinkInfo</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/command/addLinkInfo/AddLinkInfoHandler.kt'>AddLinkInfoHandler.kt</a></b></td>
																				<td>- Handles adding new link information by validating, creating short links, and logging details in the Link Management Service<br>- It ensures proper user identification and generates results for the added link information.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/command/addLinkInfo/AddLinkInfoResult.kt'>AddLinkInfoResult.kt</a></b></td>
																				<td>- Defines a data structure for storing link information, including IDs, original and shortened links, user IDs, and creation/expiry dates<br>- This file encapsulates the result of adding link information within the project's CQRS command architecture.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/command/addLinkInfo/AddLinkInfoCommand.kt'>AddLinkInfoCommand.kt</a></b></td>
																				<td>Facilitates adding link information by handling command data and processing results within the CQRS architecture of the project.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/command/addLinkInfo/AddLinkInfoValidate.kt'>AddLinkInfoValidate.kt</a></b></td>
																				<td>- Validates and ensures the correctness of input data for adding link information<br>- Checks the format of the short link, original link, and expiration date<br>- Verifies the existence of the short link and the validity of the token before processing the command.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/command/addLinkInfo/AddLinkInfoValidateResult.kt'>AddLinkInfoValidateResult.kt</a></b></td>
																				<td>- Defines a data class for validating the addition of link information in the application's command layer<br>- This class encapsulates the validation result and includes the user information associated with the link.</td>
																			</tr>
																			</table>
																		</blockquote>
																	</details>
																	<details>
																		<summary><b>base</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/command/base/CommandValidateResult.kt'>CommandValidateResult.kt</a></b></td>
																				<td>Defines a base class for validating command results in the project's CQRS architecture.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/command/base/CommandHandler.kt'>CommandHandler.kt</a></b></td>
																				<td>- Defines a contract for handling commands in the project's CQRS architecture<br>- The CommandHandler interface specifies a method to process commands and return results, supporting validation if needed<br>- This abstraction facilitates decoupling of command execution logic from the rest of the system, promoting maintainability and extensibility.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/command/base/CommandValidator.kt'>CommandValidator.kt</a></b></td>
																				<td>- Defines a contract for validating commands in the CQRS architecture, ensuring data integrity and consistency<br>- The CommandValidator interface specifies a method to validate incoming commands and return validation results using asynchronous processing<br>- This component plays a crucial role in maintaining the integrity of command operations within the application.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/command/base/Command.kt'>Command.kt</a></b></td>
																				<td>Defines a base interface for commands in the CQRS architecture, facilitating the separation of command responsibilities within the project structure.</td>
																			</tr>
																			</table>
																		</blockquote>
																	</details>
																	<details>
																		<summary><b>changeExpireDate</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/command/changeExpireDate/ChangeExpireDateValidate.kt'>ChangeExpireDateValidate.kt</a></b></td>
																				<td>Validates and ensures the integrity of change requests for expiration dates by verifying IDs and tokens, enhancing security and data accuracy within the application.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/command/changeExpireDate/ChangeExpireDateCommand.kt'>ChangeExpireDateCommand.kt</a></b></td>
																				<td>- Facilitates changing expiration dates for a specific entity, handling authentication via JWT<br>- This command implements a base command interface, returning a boolean indicating the success of the operation.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/command/changeExpireDate/ChangeExpireDateHandler.kt'>ChangeExpireDateHandler.kt</a></b></td>
																				<td>- Handles changing expiration dates for links in the application by utilizing the LinkManagementService<br>- This code file serves as a command handler within the CQRS architecture, facilitating the modification of expiration dates for specific links based on incoming commands.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/command/changeExpireDate/ChangeExpireDateValidateResult.kt'>ChangeExpireDateValidateResult.kt</a></b></td>
																				<td>Defines a data class for validating changes to a user's expiration date within the project's CQRS command architecture.</td>
																			</tr>
																			</table>
																		</blockquote>
																	</details>
																	<details>
																		<summary><b>changeOriginLink</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/command/changeOriginLink/ChangeOriginLinkLinkValidate.kt'>ChangeOriginLinkLinkValidate.kt</a></b></td>
																				<td>- Validates and ensures the integrity of incoming data for changing the origin link in the application<br>- Verifies the provided ID, origin link format, and JWT token, ensuring they meet specified criteria<br>- Additionally, it validates the token with an external service to authorize the change.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/command/changeOriginLink/ChangeOriginLinkHandler.kt'>ChangeOriginLinkHandler.kt</a></b></td>
																				<td>- Implements a handler for changing the origin link in the application's command architecture<br>- Utilizes a CommandHandler to interact with the LinkManagementService, facilitating the modification of link origins<br>- The handler encapsulates the logic for processing ChangeOriginLinkCommand instances, ensuring the successful update of link information within the system.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/command/changeOriginLink/ChangeOriginLinkValidateResult.kt'>ChangeOriginLinkValidateResult.kt</a></b></td>
																				<td>- Defines a data class for validating changes to the origin link in the CQRS command module<br>- The class encapsulates the validation result for the user entity, contributing to maintaining data integrity and ensuring command execution accuracy within the project architecture.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/command/changeOriginLink/ChangeOriginLinkCommand.kt'>ChangeOriginLinkCommand.kt</a></b></td>
																				<td>- Facilitates changing the origin link for a specific entity by handling the corresponding command<br>- This file encapsulates the logic for processing the request and updating the origin link, contributing to the command-driven architecture of the project.</td>
																			</tr>
																			</table>
																		</blockquote>
																	</details>
																	<details>
																		<summary><b>disabledLinkInfo</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/command/disabledLinkInfo/DisabledLinkInfoCommand.kt'>DisabledLinkInfoCommand.kt</a></b></td>
																				<td>- Defines a command to disable link information, handling the ID and JWT request<br>- This file plays a crucial role in the project's CQRS architecture, facilitating the execution of commands to disable link information within the application.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/command/disabledLinkInfo/DisabledLinkValidateResult.kt'>DisabledLinkValidateResult.kt</a></b></td>
																				<td>- Defines a data class for validating disabled link information within the project's CQRS command architecture<br>- This class encapsulates the validation result for disabled links, including user information.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/command/disabledLinkInfo/DisabledLinkInfoValidate.kt'>DisabledLinkInfoValidate.kt</a></b></td>
																				<td>Validates and ensures the integrity of disabled link information by checking for a valid ID and JWT token, and verifying the token's authenticity using an external service.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/command/disabledLinkInfo/DisabledLinkInfoHandler.kt'>DisabledLinkInfoHandler.kt</a></b></td>
																				<td>- Handles disabling link information by utilizing the LinkManagementService<br>- Implements CommandHandler to process DisabledLinkInfoCommand, returning a Uni<Boolean> indicating success.</td>
																			</tr>
																			</table>
																		</blockquote>
																	</details>
																	<details>
																		<summary><b>enabledLinkInfo</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/command/enabledLinkInfo/EnabledLinkValidateResult.kt'>EnabledLinkValidateResult.kt</a></b></td>
																				<td>- Defines a data class for validating enabled links, part of the command architecture in the project<br>- It encapsulates validation results for user-enabled links, contributing to the overall robustness of the application's command handling capabilities.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/command/enabledLinkInfo/EnabledLinkInfoCommand.kt'>EnabledLinkInfoCommand.kt</a></b></td>
																				<td>- Enables retrieval of link information by handling commands with authentication using JWT tokens<br>- This file encapsulates the logic for processing requests related to link information, ensuring secure access through authentication mechanisms.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/command/enabledLinkInfo/EnabledLinkInfoHandler.kt'>EnabledLinkInfoHandler.kt</a></b></td>
																				<td>Enables the retrieval of link information by handling commands using the LinkManagementService.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/command/enabledLinkInfo/EnabledLinkInfoValidate.kt'>EnabledLinkInfoValidate.kt</a></b></td>
																				<td>- Validates enabled link information, ensuring the ID is valid, JWT token exists, and expiration date is correct<br>- Verifies the token with external services and returns validation results.</td>
																			</tr>
																			</table>
																		</blockquote>
																	</details>
																	<details>
																		<summary><b>generateWhiteShortLinkList</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/command/generateWhiteShortLinkList/GenerateWhiteShortLinkListCommand.kt'>GenerateWhiteShortLinkListCommand.kt</a></b></td>
																				<td>Generates a list of white short links using a command pattern in the CQRS architecture.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/command/generateWhiteShortLinkList/GenerateWhiteShortLinkListHandler.kt'>GenerateWhiteShortLinkListHandler.kt</a></b></td>
																				<td>- Generates a list of white short links based on a specified threshold size<br>- Handles the command to either retrieve existing links or generate new ones if the list is below the threshold.</td>
																			</tr>
																			</table>
																		</blockquote>
																	</details>
																	<details>
																		<summary><b>redirectToOriginalLink</b></summary>
																		<blockquote>
																			<table>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/command/redirectToOriginalLink/RedirectToOriginalLinkHandler.kt'>RedirectToOriginalLinkHandler.kt</a></b></td>
																				<td>Handles redirecting short links to their original destinations, logging user data, and publishing logs to a messaging service.</td>
																			</tr>
																			<tr>
																				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/cqrs/command/redirectToOriginalLink/RedirectToOriginalLinkCommand.kt'>RedirectToOriginalLinkCommand.kt</a></b></td>
																				<td>- Enables redirection to the original link based on a provided short link and request header data<br>- This command plays a crucial role in the CQRS architecture by handling the logic to retrieve and redirect users to the original link associated with a given short link.</td>
																			</tr>
																			</table>
																		</blockquote>
																	</details>
																</blockquote>
															</details>
														</blockquote>
													</details>
													<details>
														<summary><b>ctrl</b></summary>
														<blockquote>
															<table>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/ctrl/LinkInfoCtrl.kt'>LinkInfoCtrl.kt</a></b></td>
																<td>- Manages various link-related operations such as adding, disabling, enabling, changing origin links, and updating expiration dates<br>- Also includes functionality to generate a list of white short links<br>- Handles requests for redirecting to original links<br>- Uses middleware for authentication and header management.</td>
															</tr>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/ctrl/UserLinkInfoCtrl.kt'>UserLinkInfoCtrl.kt</a></b></td>
																<td>- Handles user link information retrieval and statistics queries by leveraging the DefaultActionExecutor for executing queries securely<br>- Implements endpoints for fetching user short link information and statistics, ensuring proper authentication via JwtRequest middleware.</td>
															</tr>
															</table>
														</blockquote>
													</details>
													<details>
														<summary><b>service</b></summary>
														<blockquote>
															<table>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/service/UserLinkStatManagementService.kt'>UserLinkStatManagementService.kt</a></b></td>
																<td>- Calculates and returns statistics for a given short link, including total count, referer count, IP count, and user agent count<br>- Uses data from BigQuery to generate insights on link usage.</td>
															</tr>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/service/CompensationService.kt'>CompensationService.kt</a></b></td>
																<td>- The `CompensationService` orchestrates compensation actions, handling errors and logging<br>- It ensures successful execution of actions and logs errors when necessary<br>- This service plays a crucial role in managing compensation processes within the application, providing robust error handling and logging capabilities.</td>
															</tr>
															<tr>
																<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/kotlin/com/coco/application/service/LinkManagementService.kt'>LinkManagementService.kt</a></b></td>
																<td>- Manages link information, enabling retrieval from Redis or database, adding new entries, disabling or enabling links, updating link details, and checking link status<br>- Handles transactions for MongoDB and Redis operations, with compensation actions for failures<br>- Supports generating and managing white-listed short links.</td>
															</tr>
															</table>
														</blockquote>
													</details>
												</blockquote>
											</details>
										</blockquote>
									</details>
								</blockquote>
							</details>
						</blockquote>
					</details>
					<details>
						<summary><b>proto</b></summary>
						<blockquote>
							<table>
							<tr>
								<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/proto/verifyToken.proto'>verifyToken.proto</a></b></td>
								<td>Defines gRPC service for verifying tokens with request and response message structures.</td>
							</tr>
							<tr>
								<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/proto/helloworld.proto'>helloworld.proto</a></b></td>
								<td>- Defines a gRPC service for sending greetings between clients and servers<br>- The service includes methods for sending and receiving greetings<br>- The defined message structures allow for passing user names and corresponding greetings<br>- This file serves as the contract for communication within the project's architecture.</td>
							</tr>
							</table>
						</blockquote>
					</details>
					<details>
						<summary><b>docker</b></summary>
						<blockquote>
							<table>
							<tr>
								<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/docker/Dockerfile.native-micro'>Dockerfile.native-micro</a></b></td>
								<td>- Enables building a container for running the Quarkus application in native mode using a micro base image, optimized for Quarkus native executables<br>- Reduces container image size by leveraging a specific base image<br>- Follow provided instructions to build and run the container efficiently.</td>
							</tr>
							<tr>
								<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/docker/Dockerfile.jvm'>Dockerfile.jvm</a></b></td>
								<td>- Enables building a Docker container for running a Quarkus application in JVM mode<br>- Facilitates building, running, and debugging the container image with specified configurations<br>- Utilizes a script for executing the Java application with memory/GC tuning options<br>- Allows customization of JVM behavior through environment properties.</td>
							</tr>
							<tr>
								<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/docker/Dockerfile.legacy-jar'>Dockerfile.legacy-jar</a></b></td>
								<td>- Enables building and running a Quarkus application in JVM mode within a Docker container<br>- Includes memory/GC tuning and configuration options for JVM behavior<br>- Utilizes a specific Dockerfile to build the container image and a script to run the application<br>- Key environment properties can be set to customize the application's behavior within the container.</td>
							</tr>
							<tr>
								<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/src/main/docker/Dockerfile.native'>Dockerfile.native</a></b></td>
								<td>- Facilitates building a container for running the Quarkus application in native mode<br>- Instructions for building and running the container are provided<br>- The Dockerfile sets up the necessary environment and configurations for the application to run successfully in a containerized environment.</td>
							</tr>
							</table>
						</blockquote>
					</details>
				</blockquote>
			</details>
		</blockquote>
	</details>
	<details> <!-- k8s Submodule -->
		<summary><b>k8s</b></summary>
		<blockquote>
			<table>
			<tr>
				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/k8s/k8s-deploy-service.yaml'>k8s-deploy-service.yaml</a></b></td>
				<td>- Defines a Kubernetes deployment and service for the Coco Short Link Background service, ensuring high availability and scalability<br>- Manages containerized application instances, sets resource limits, and configures environment variables for seamless operation.</td>
			</tr>
			<tr>
				<td><b><a href='https://github.com/coco40725/coco-short-link-bg/blob/master/k8s/dev-cluster-ingress.yaml'>dev-cluster-ingress.yaml</a></b></td>
				<td>Defines routing rules for multiple subdomains in the Kubernetes cluster to direct traffic to different backend services based on the specified paths.</td>
			</tr>
			</table>
		</blockquote>
	</details>
</details>

---
##  Getting Started

###  Prerequisites

Before getting started with coco-short-link-bg, ensure your runtime environment meets the following requirements:

- **Programming Language:** Kotlin
- **Package Manager:** Gradle
- **Container Runtime:** Docker


###  Installation

Install coco-short-link-bg using one of the following methods:

**Build from source:**

1. Clone the coco-short-link-bg repository:
```sh
❯ git clone https://github.com/coco40725/coco-short-link-bg
```

2. Navigate to the project directory:
```sh
❯ cd coco-short-link-bg
```

3. Install the project dependencies:


**Using `gradle`** &nbsp; [<img align="center" src="https://img.shields.io/badge/Kotlin-0095D5.svg?style={badge_style}&logo=kotlin&logoColor=white" />](https://kotlinlang.org/)

```sh
❯ gradle build
```


**Using `docker`** &nbsp; [<img align="center" src="https://img.shields.io/badge/Docker-2CA5E0.svg?style={badge_style}&logo=docker&logoColor=white" />](https://www.docker.com/)

```sh
❯ docker build -t coco40725/coco-short-link-bg .
```




###  Usage
Run coco-short-link-bg using the following command:
**Using `gradle`** &nbsp; [<img align="center" src="https://img.shields.io/badge/Kotlin-0095D5.svg?style={badge_style}&logo=kotlin&logoColor=white" />](https://kotlinlang.org/)

```sh
❯ gradle run
```


**Using `docker`** &nbsp; [<img align="center" src="https://img.shields.io/badge/Docker-2CA5E0.svg?style={badge_style}&logo=docker&logoColor=white" />](https://www.docker.com/)

```sh
❯ docker run -it {image_name}
```


###  Testing
Run the test suite using the following command:
**Using `gradle`** &nbsp; [<img align="center" src="https://img.shields.io/badge/Kotlin-0095D5.svg?style={badge_style}&logo=kotlin&logoColor=white" />](https://kotlinlang.org/)

```sh
❯ gradle test
```


---
##  Project Roadmap

- [X] **`Task 1`**: <strike>Implement feature one.</strike>
- [ ] **`Task 2`**: Implement feature two.
- [ ] **`Task 3`**: Implement feature three.

---

##  Contributing

- **💬 [Join the Discussions](https://github.com/coco40725/coco-short-link-bg/discussions)**: Share your insights, provide feedback, or ask questions.
- **🐛 [Report Issues](https://github.com/coco40725/coco-short-link-bg/issues)**: Submit bugs found or log feature requests for the `coco-short-link-bg` project.
- **💡 [Submit Pull Requests](https://github.com/coco40725/coco-short-link-bg/blob/main/CONTRIBUTING.md)**: Review open PRs, and submit your own PRs.

<details closed>
<summary>Contributing Guidelines</summary>

1. **Fork the Repository**: Start by forking the project repository to your github account.
2. **Clone Locally**: Clone the forked repository to your local machine using a git client.
   ```sh
   git clone https://github.com/coco40725/coco-short-link-bg
   ```
3. **Create a New Branch**: Always work on a new branch, giving it a descriptive name.
   ```sh
   git checkout -b new-feature-x
   ```
4. **Make Your Changes**: Develop and test your changes locally.
5. **Commit Your Changes**: Commit with a clear message describing your updates.
   ```sh
   git commit -m 'Implemented new feature x.'
   ```
6. **Push to github**: Push the changes to your forked repository.
   ```sh
   git push origin new-feature-x
   ```
7. **Submit a Pull Request**: Create a PR against the original project repository. Clearly describe the changes and their motivations.
8. **Review**: Once your PR is reviewed and approved, it will be merged into the main branch. Congratulations on your contribution!
</details>

<details closed>
<summary>Contributor Graph</summary>
<br>
<p align="left">
   <a href="https://github.com{/coco40725/coco-short-link-bg/}graphs/contributors">
      <img src="https://contrib.rocks/image?repo=coco40725/coco-short-link-bg">
   </a>
</p>
</details>

---

##  License

This project is protected under the [SELECT-A-LICENSE](https://choosealicense.com/licenses) License. For more details, refer to the [LICENSE](https://choosealicense.com/licenses/) file.

---

##  Acknowledgments

- List any resources, contributors, inspiration, etc. here.

---
