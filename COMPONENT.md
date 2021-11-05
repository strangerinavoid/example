Component:

<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">

	<modelVersion>4.0.0</modelVersion>

	<parent>
		<groupId>com.inlabsoft.shared</groupId>
		<artifactId>sen-shared-bom</artifactId>
		<version>1.0.0-SNAPSHOT</version>
		<relativePath>../</relativePath>
	</parent>

	<name>Sen Shared Core</name>
	<artifactId>sen-shared-core</artifactId>
	<description>${project.name} Component</description>
	<packaging>jar</packaging>

	<dependencies>
		<dependency>
			<groupId>org.jacoco</groupId>
			<artifactId>org.jacoco.core</artifactId>
			<version>${jacoco.version}</version>
			<scope>test</scope>
		</dependency>
	</dependencies>

	<properties>
		<!-- maven plugins configuration -->
		<maven.surefire.extra>
			--add-opens sen.shared.core/com.inlabsoft.sen.shared.core=ALL-UNNAMED
			--add-opens sen.shared.core/com.inlabsoft.sen.shared.core.digest=ALL-UNNAMED
			--add-opens sen.shared.core/com.inlabsoft.sen.shared.core.property=ALL-UNNAMED
			--add-opens sen.shared.core/com.inlabsoft.sen.shared.core.type=ALL-UNNAMED
		</maven.surefire.extra>
	</properties>
</project>
