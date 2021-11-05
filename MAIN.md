# Main POM

<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">

	<name>Sen Bom</name>
	<groupId>com.inlabsoft</groupId>
	<modelVersion>4.0.0</modelVersion>
	<artifactId>sen-bom</artifactId>
	<description>${project.name}</description>
	<packaging>pom</packaging>
	<version>1.0.0-SNAPSHOT</version>

	<organization>
		<name>InLabSoft LLC</name>
		<url>http://www.inlabsoft.com</url>
	</organization>
	<developers>
		<developer>
			<id>aochirov</id>
			<name>Andrey OCHIROV</name>
			<email>aochirov@inlabsoft.com</email>
		</developer>
		<developer>
			<id>dmelnik</id>
			<name>Dmitrii MELNIK</name>
			<email>dmelnik@inlabsoft.com</email>
		</developer>
		<developer>
			<id>pivanov</id>
			<name>Pavel IVANOV</name>
			<email>pivanov@inlabsoft.com</email>
		</developer>
	</developers>

	<issueManagement>
		<url>https://dev.azure.com/inlabsoft/Sen/_workitems/recentlyupdated/</url>
		<system>Microsoft Azure DevOps</system>
	</issueManagement>

	<inceptionYear>2020</inceptionYear>

	<!-- build number plugin requires properly formatted git connection in scm section -->
	<scm>
		<url>https://dev.azure.com/inlabsoft/_git/Sen</url>
		<connection>scm:git:ssh.dev.azure.com:v3/inlabsoft/Sen/Sen</connection>
	</scm>

	<licenses>
		<license>
			<name>Private License</name>
			<url>http://www.inlabsoft.com</url>
		</license>
	</licenses>

	<modules>
		<module>component</module>
	</modules>

	<properties>
		<!-- project configuration -->
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>

		<!-- platform configuration -->
		<component.artifact>${project.artifactId}-${project.version}</component.artifact>
		<component.code.coverage>0.0</component.code.coverage>
		<component.prefix>sen</component.prefix> <!-- original "сән" -->
		<component.prefix.shared>${component.prefix}-shared</component.prefix.shared>

		<!-- maven plugins configuration -->
		<maven.assembly.version>3.3.0</maven.assembly.version>
		<maven.assembly.descriptor>assembly.xml</maven.assembly.descriptor>

		<maven.build.plugin.version>3.2.0</maven.build.plugin.version>
		<maven.build.timestamp.format>yyyyMMddHHmmssSSS</maven.build.timestamp.format>
		<maven.clean.version>3.1.0</maven.clean.version>
		<maven.compiler.source>17</maven.compiler.source>
		<maven.compiler.target>${maven.compiler.source}</maven.compiler.target>
		<maven.compiler.showWarnings>true</maven.compiler.showWarnings>
		<maven.compiler.showDeprecation>true</maven.compiler.showDeprecation>
		<maven.compiler.version>3.8.1</maven.compiler.version>
		<maven.dependency.version>3.2.0</maven.dependency.version>
		<maven.enforcer.version>3.0.0</maven.enforcer.version>
		<maven.install.version>3.0.0-M1</maven.install.version>
		<maven.jar.version>3.2.0</maven.jar.version>
		<maven.javadoc.version>3.3.1</maven.javadoc.version>
		<maven.resources.version>3.2.0</maven.resources.version>
		<maven.site.version>3.9.1</maven.site.version>
		<maven.source.version>3.2.1</maven.source.version>
		<maven.surefire.opts>--illegal-access=permit --add-opens java.base/java.lang=ALL-UNNAMED</maven.surefire.opts>
		<maven.surefire.extra></maven.surefire.extra>
		<maven.surefire.args>
			-Xms256m
			-Xmx256m
			-Xshare:off

			-XX:+UseG1GC
			-XX:+AlwaysPreTouch
			-XX:MaxMetaspaceSize=64m
			-XX:+ScavengeBeforeFullGC

			-Dfile.encoding=UTF-8
			-Djava.net.preferIPv4Stack=true
			-Duser.language=en
			-Duser.language.format=en
			-Duser.timezone=GMT
		</maven.surefire.args>
		<maven.surefire.version>3.0.0-M5</maven.surefire.version>

		<maven.deploy.skip>false</maven.deploy.skip>
		<maven.deploy.version>3.0.0-M1</maven.deploy.version>

		<buildnumber.maven.version>1.4</buildnumber.maven.version>
		<versions.maven.version>2.8.1</versions.maven.version>

		<!-- external frameworks versions: java se / java ee -->
		<jakarta.version>8.0.1</jakarta.version>

		<!-- external frameworks versions: testing and coverage -->
		<junit.version>5.8.1</junit.version>
		<junit.jupiter.version>${junit.version}</junit.jupiter.version>
		<junit.platform.version>1.8.1</junit.platform.version>
		<junit.vintage.version>${junit.version}</junit.vintage.version>

		<jacoco.version>0.8.7</jacoco.version>

		<mockito.junit.jupiter.version>4.0.0</mockito.junit.jupiter.version>
	</properties>

	<pluginRepositories>
		<pluginRepository>
			<id>central</id>
			<url>https://repo1.maven.org/maven2/</url>
			<snapshots>
				<enabled>false</enabled>
			</snapshots>
		</pluginRepository>
	</pluginRepositories>

	<repositories>
		<repository>
			<releases>
				<enabled>true</enabled>
				<checksumPolicy>warn</checksumPolicy>
			</releases>
			<id>dev.java.net</id>
			<name>dev.java.net</name>
			<url>http://download.java.net/maven/2/</url>
		</repository>

		<repository>
			<id>localrepository</id>
			<url>file://${maven.multiModuleProjectDirectory}/.mvn/local</url>
		</repository>
	</repositories>

	<distributionManagement>
		<snapshotRepository>
			<id>local-snapshots</id>
			<name>Snapshot Releases</name>
			<url>file://${maven.multiModuleProjectDirectory}/.mvn/local</url>
		</snapshotRepository>
		<repository>
			<id>local-releases</id>
			<name>Internal Releases</name>
			<url>file://${maven.multiModuleProjectDirectory}/.mvn/local</url>
		</repository>
	</distributionManagement>

	<!-- Common project settings (for all projects) -->
	<build>
		<finalName>${component.artifact}</finalName>

		<resources>
			<resource>
				<directory>${basedir}/src/main/resources</directory>
			</resource>
			<resource>
				<directory>${basedir}/src/main/java</directory>
				<includes>
					<include>**/*.properties</include>
					<include>**/*.json</include>
					<include>**/*.tmpl</include>
					<include>**/*.xml</include>
					<include>**/*.xsd</include>
					<include>**/*.yml</include>
				</includes>
			</resource>
		</resources>
		<testResources>
			<testResource>
				<directory>${basedir}/src/test/resources</directory>
				<includes>
					<include>**/*</include>
				</includes>
			</testResource>
		</testResources>

		<!-- Default target to run on -->
		<defaultGoal>package</defaultGoal>

		<pluginManagement>
			<plugins>
				<plugin>
					<groupId>org.apache.maven.plugins</groupId>
					<artifactId>maven-assembly-plugin</artifactId>
					<version>${maven.assembly.version}</version>
					<executions>
						<execution>
							<id>make-assembly</id>
							<phase>package</phase>
							<goals>
								<goal>single</goal>
							</goals>
							<configuration>
								<descriptors>
									<descriptor>${maven.assembly.descriptor}</descriptor>
								</descriptors>
								<recompressZippedFiles>false</recompressZippedFiles>
							</configuration>
						</execution>
					</executions>
					<configuration>
						<descriptors>
							<descriptor>${maven.assembly.descriptor}</descriptor>
						</descriptors>
						<recompressZippedFiles>false</recompressZippedFiles>
					</configuration>
				</plugin>
				<plugin>
					<groupId>org.apache.maven.plugins</groupId>
					<artifactId>maven-clean-plugin</artifactId>
					<version>${maven.clean.version}</version>
				</plugin>
				<plugin>
					<groupId>org.apache.maven.plugins</groupId>
					<artifactId>maven-compiler-plugin</artifactId>
					<version>${maven.compiler.version}</version>
					<configuration>
						<release>11</release>
						<useIncrementalCompilation>false</useIncrementalCompilation>
						<optimize>true</optimize>
						<compilerArgs>
							<compilerArg>-parameters</compilerArg>
						</compilerArgs>
					</configuration>
				</plugin>
				<plugin>
					<artifactId>maven-dependency-plugin</artifactId>
					<version>${maven.dependency.version}</version>
				</plugin>
				<plugin>
					<artifactId>maven-deploy-plugin</artifactId>
					<version>${maven.deploy.version}</version>
					<executions>
						<execution>
							<id>default-deploy</id>
							<phase>deploy</phase>
							<goals>
								<goal>deploy</goal>
							</goals>
						</execution>
					</executions>
				</plugin>
				<plugin>
					<groupId>org.apache.maven.plugins</groupId>
					<artifactId>maven-enforcer-plugin</artifactId>
					<version>${maven.enforcer.version}</version>
					<executions>
						<execution>
							<id>enforce-maven</id>
							<goals>
								<goal>enforce</goal>
							</goals>
							<configuration>
								<rules>
									<requireMavenVersion>
										<version>3.6.0</version>
									</requireMavenVersion>
								</rules>
							</configuration>
						</execution>
					</executions>
				</plugin>
				<plugin>
					<groupId>org.apache.maven.plugins</groupId>
					<artifactId>maven-failsafe-plugin</artifactId>
					<version>${maven.surefire.version}</version>
					<configuration>
						<argLine>
							--illegal-access=permit
						</argLine>
					</configuration>
				</plugin>
				<plugin>
					<artifactId>maven-install-plugin</artifactId>
					<version>${maven.install.version}</version>
					<executions>
						<execution>
							<id>default-install</id>
							<phase>install</phase>
							<goals>
								<goal>install</goal>
							</goals>
						</execution>
					</executions>
				</plugin>
				<plugin>
					<artifactId>maven-jar-plugin</artifactId>
					<version>${maven.jar.version}</version>
					<configuration>
						<archive>
							<manifest>
								<mainClass>${component.application}</mainClass>
								<addDefaultImplementationEntries>true</addDefaultImplementationEntries>
							</manifest>
						</archive>
					</configuration>
				</plugin>
				<plugin>
					<artifactId>maven-javadoc-plugin</artifactId>
					<version>${maven.javadoc.version}</version>
				</plugin>
				<plugin>
					<groupId>org.apache.maven.plugins</groupId>
					<artifactId>maven-resources-plugin</artifactId>
					<version>${maven.resources.version}</version>
					<configuration>
						<encoding>${project.build.sourceEncoding}</encoding>
					</configuration>
				</plugin>
				<plugin>
					<groupId>org.apache.maven.plugins</groupId>
					<artifactId>maven-site-plugin</artifactId>
					<version>${maven.site.version}</version>
				</plugin>
				<plugin>
					<groupId>org.apache.maven.plugins</groupId>
					<artifactId>maven-surefire-plugin</artifactId>
					<version>${maven.surefire.version}</version>
					<configuration>
						<argLine>${argLine} ${maven.surefire.args} ${maven.surefire.opts} ${maven.surefire.extra}</argLine>
						<!-- argLine>${maven.surefire.opts} ${maven.surefire.extra} ${maven.surefire.args}</argLine -->
						<reuseForks>false</reuseForks>
					</configuration>
				</plugin>
				<plugin>
					<artifactId>maven-source-plugin</artifactId>
					<version>${maven.source.version}</version>
				</plugin>
				<plugin>
					<groupId>org.codehaus.mojo</groupId>
					<artifactId>build-helper-maven-plugin</artifactId>
					<version>${maven.build.plugin.version}</version>
					<configuration>
						<name>newVersion</name>
						<value>${project.version}</value>
						<regex>-SNAPSHOT</regex>
						<failIfNoMatch>false</failIfNoMatch>
					</configuration>
				</plugin>
				<plugin>
					<groupId>org.codehaus.mojo</groupId>
					<artifactId>buildnumber-maven-plugin</artifactId>
					<version>${buildnumber.maven.version}</version>
				</plugin>
				<plugin>
					<groupId>org.codehaus.mojo</groupId>
					<artifactId>versions-maven-plugin</artifactId>
					<version>${versions.maven.version}</version>
				</plugin>

				<plugin>
					<groupId>org.junit.jupiter</groupId>
					<artifactId>junit-jupiter</artifactId>
					<version>${junit.version}</version>
				</plugin>

				<plugin>
					<groupId>org.jacoco</groupId>
					<artifactId>jacoco-maven-plugin</artifactId>
					<version>${jacoco.version}</version>
					<executions>
						<execution>
							<goals>
								<goal>prepare-agent</goal>
							</goals>
						</execution>
						<execution>
							<id>report</id>
							<phase>prepare-package</phase>
							<goals>
								<goal>report</goal>
							</goals>
						</execution>
						<execution>
							<id>jacoco-check</id>
							<goals>
								<goal>check</goal>
							</goals>
							<configuration>
								<rules>
									<rule>
										<element>PACKAGE</element>
										<limits>
											<limit>
												<counter>LINE</counter>
												<value>COVEREDRATIO</value>
												<minimum>${component.code.coverage}</minimum>
											</limit>
										</limits>
									</rule>
								</rules>
							</configuration>
						</execution>
					</executions>
				</plugin>
			</plugins>
		</pluginManagement>

		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-enforcer-plugin</artifactId>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-failsafe-plugin</artifactId>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-jar-plugin</artifactId>
				<configuration>
					<archive>
						<addMavenDescriptor>true</addMavenDescriptor>
						<manifestEntries>
							<Specification-Title>${project.name}</Specification-Title>
							<Specification-Version>${project.version}</Specification-Version>
							<Specification-Vendor>${project.organization.name}</Specification-Vendor>
							<Implementation-Build>${buildNumber}</Implementation-Build>
							<Implementation-Title>${project.name}</Implementation-Title>
							<Implementation-Version>${maven.build.timestamp}</Implementation-Version>
							<Implementation-Vendor>${project.organization.name}</Implementation-Vendor>
							<Implementation-URL>${project.scm.url}</Implementation-URL>
						</manifestEntries>
					</archive>
				</configuration>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-javadoc-plugin</artifactId>
				<configuration>
					<additionalJOption>-Xdoclint:none</additionalJOption>
				</configuration>
				<executions>
					<execution>
						<id>attach-javadoc</id>
						<phase>release</phase>
						<goals>
							<goal>jar</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-surefire-plugin</artifactId>
			</plugin>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-source-plugin</artifactId>
				<executions>
					<execution>
						<id>attach-sources</id>
						<phase>verify</phase>
						<goals>
							<goal>jar</goal>
						</goals>
					</execution>
				</executions>
			</plugin>

			<plugin>
				<groupId>org.jacoco</groupId>
				<artifactId>jacoco-maven-plugin</artifactId>
				<executions>
					<execution>
						<id>pre-unit-test</id>
						<goals>
							<goal>prepare-agent</goal>
						</goals>
					</execution>
					<execution>
						<id>post-unit-test</id>
						<phase>test</phase>
						<goals>
							<goal>report</goal>
						</goals>
					</execution>
				</executions>
			</plugin>

			<plugin>
				<groupId>org.codehaus.mojo</groupId>
				<artifactId>buildnumber-maven-plugin</artifactId>
				<executions>
					<execution>
						<id>buildnumber</id>
						<phase>validate</phase>
						<goals>
							<goal>create</goal>
						</goals>
					</execution>
				</executions>
				<configuration>
					<format>{0,number}</format>
					<items>
						<item>buildNumber</item>
					</items>
					<doCheck>false</doCheck>
					<doUpdate>false</doUpdate>
					<shortRevisionLength>7</shortRevisionLength>
					<getRevisionOnlyOnce>true</getRevisionOnlyOnce>
					<revisionOnScmFailure>unknownbuild</revisionOnScmFailure>
					<buildNumberPropertiesFileLocation>
						${maven.multiModuleProjectDirectory}/.mvn/build/.${project.artifactId}-${project.version}
					</buildNumberPropertiesFileLocation>
				</configuration>
			</plugin>
		</plugins>
	</build>

	<dependencies>
		<!--dependency>
			<groupId>javax</groupId>
			<artifactId>javaee-api</artifactId>
			<version>${jakarta.version}</version>
			<scope>provided</scope>
		</dependency-->

		<!-- THE TEST SCOPE LIBRARIES -->
		<dependency>
			<groupId>org.junit.jupiter</groupId>
			<artifactId>junit-jupiter-api</artifactId>
			<version>${junit.jupiter.version}</version>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.junit.jupiter</groupId>
			<artifactId>junit-jupiter-engine</artifactId>
			<version>${junit.jupiter.version}</version>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.junit.platform</groupId>
			<artifactId>junit-platform-engine</artifactId>
			<version>${junit.platform.version}</version>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.junit.platform</groupId>
			<artifactId>junit-platform-commons</artifactId>
			<version>${junit.platform.version}</version>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.mockito</groupId>
			<artifactId>mockito-junit-jupiter</artifactId>
			<version>${mockito.junit.jupiter.version}</version>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.mockito</groupId>
			<artifactId>mockito-core</artifactId>
			<version>${mockito.junit.jupiter.version}</version>
			<scope>test</scope>
		</dependency>
	</dependencies>

	<profiles>
		<!-- This profile downloads from remote repository source jars for dependencies with groupId included in ${include.group.ids} -->
		<profile>
			<id>src</id>
			<properties>
				<!-- find source jars for artifacts with this groupIds -->
				<include.group.ids>${project.groupId}</include.group.ids>
			</properties>
			<build>
				<plugins>
					<plugin>
						<groupId>org.apache.maven.plugins</groupId>
						<artifactId>maven-dependency-plugin</artifactId>
						<configuration>
							<includeGroupIds>${include.group.ids}</includeGroupIds>
						</configuration>
						<executions>
							<execution>
								<id>get-sources</id>
								<phase>generate-sources</phase>
								<goals>
									<goal>sources</goal>
								</goals>
							</execution>
						</executions>
					</plugin>
				</plugins>
			</build>
		</profile>

		<!-- This profile shows new available versions for dependencies and plugins. Sets default goal to validate. Parameter ${show.snapshot.versions}
			defines if it should look for snapshot versions. Default value is false. -->
		<profile>
			<id>versions</id>
			<properties>
				<!-- Whether to allow snapshots when searching for the latest version of an artifact. -->
				<show.snapshot.versions>false</show.snapshot.versions>
			</properties>
			<build>
				<defaultGoal>validate</defaultGoal>
				<plugins>
					<plugin>
						<groupId>org.codehaus.mojo</groupId>
						<artifactId>versions-maven-plugin</artifactId>
						<configuration>
							<allowSnapshots>${show.snapshot.versions}</allowSnapshots>
						</configuration>
						<executions>
							<execution>
								<id>check-versions</id>
								<phase>validate</phase>
								<goals>
									<goal>display-dependency-updates</goal>
									<goal>display-plugin-updates</goal>
								</goals>
							</execution>
						</executions>
					</plugin>
				</plugins>
			</build>
		</profile>
	</profiles>
</project>
