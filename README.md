<a href="https://netlicensing.io"><img src="https://netlicensing.io/img/netlicensing-stage-twitter.jpg" alt="Innovative License Management Solution"></a>

# [Labs64 NetLicensing](https://netlicensing.io) Client (Java)

[![NetLicensing Client - CI](https://github.com/Labs64/NetLicensingClient-java/actions/workflows/netlicesning-client-ci.yml/badge.svg)](https://github.com/Labs64/NetLicensingClient-java/actions/workflows/netlicesning-client-ci.yml)
[![Maven Central Version](https://img.shields.io/maven-central/v/com.labs64.netlicensing/netlicensing-client)](https://central.sonatype.com/artifact/com.labs64.netlicensing/netlicensing-client)
[![Apache License 2.0](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/Labs64/NetLicensingClient-java/blob/master/LICENSE)
[![📖 Documentation](https://img.shields.io/badge/📖%20Documentation-Wiki-AB6543.svg)](https://netlicensing.io/wiki/restful-api)

Java wrapper for Labs64 NetLicensing [RESTful API](https://netlicensing.io/wiki/restful-api)

Visit Labs64 NetLicensing at https://netlicensing.io

## Quick Start

The recommended way to get started using [`netlicensing-client`](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.labs64.netlicensing%22) in your project is with a dependency management system – the snippets below can be copied and pasted into your build configuration.

Maven:
```xml
<dependencies>
  <dependency>
    <groupId>com.labs64.netlicensing</groupId>
    <artifactId>netlicensing-client</artifactId>
    <version>X.Y.Z</version>
  </dependency>
</dependencies>
```
Gradle:
```gradle
dependencies {
    compile 'com.labs64.netlicensing:netlicensing-client:X.Y.Z'
}
```

OSGi:
```
Require-Bundle: com.labs64.netlicensing.client;bundle-version="X.Y.Z"
```
