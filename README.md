# Releasetrain
It's an easy to use Tool, that integrates Parametrized Jenkins Jobs, Email Notification Actions and later on other Build and Release steps in one Simple Configurable Application.

Main goal ist to schedule Builds with custom Release Numbers and other Parameters. It can run as a standalone Spring Boot Webapp or it can generate a custom Jenkins Job as a trigger for the configured Buildevents.

## 2 Advantages
- It uses an existig Git Repo to store Action Configurations and the according Calendars with the custom Parameters
- A Calendar can have a custom amount of Coloums, that are mapped automattically on parametrized Jenkins Jobs
- Scheduled Jobs could be Executed from the Spring Boot app or from a generated Jenkins Job. The Runner for that is a Maven Mojo that will be installed automatically from the Maven Central Repository


# Travis-CI
[![Build Status](https://travis-ci.org/SchweizerischeBundesbahnen/releasetrain.svg?branch=master)](https://travis-ci.org/SchweizerischeBundesbahnen/releasetrain)

![](docs/2016-09-06%2018_13_23-localhost_8080_app.htm.png)
