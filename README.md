<a href="https://netlicensing.io"><img src="https://netlicensing.io/img/netlicensing-stage-twitter.jpg" alt="Innovative License Management Solution"></a>

# [Labs64 NetLicensing](https://netlicensing.io) Java Client

[![Stories in Ready](https://badge.waffle.io/Labs64/NetLicensingClient-java.svg?label=ready&title=Ready)](http://waffle.io/Labs64/NetLicensingClient-java)
[![Build Status](https://travis-ci.org/Labs64/NetLicensingClient-java.svg?branch=master)](https://travis-ci.org/Labs64/NetLicensingClient-java)
[![Dependency Status](https://www.versioneye.com/user/projects/53e5e1d735080d5aa50000c6/badge.svg?style=flat)](https://www.versioneye.com/user/projects/53e5e1d735080d5aa50000c6)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.labs64.netlicensing/netlicensing-client/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.labs64.netlicensing/netlicensing-client)

Java wrapper for Labs64 NetLicensing [RESTful API](http://l64.cc/nl10)

Visit Labs64 NetLicensing at https://netlicensing.io

## Quick Start

The recommended way to get started using [`netlicensing-client`](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.labs64.netlicensing%22) in your project is with a dependency management system – the Maven snippet below can be copied and pasted into your build.

Maven:
```xml
<dependencies>
  <dependency>
    <groupId>com.labs64.netlicensing</groupId>
    <artifactId>netlicensing-client</artifactId>
    <version>2.4.1</version>
  </dependency>
</dependencies>
```
Gradle:
```gradle
dependencies {
    compile 'com.labs64.netlicensing:netlicensing-client:2.4.1'
}
```
OSGi:
```
Require-Bundle: com.labs64.netlicensing.client;bundle-version="2.4.1"
```
