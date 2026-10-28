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

## Verify signed Agent validation responses

Products using protected offline validation must verify the raw XML response before trusting its validation result.
The Licensee API key public key can be supplied as a `PublicKey` or as PEM:

```java
import com.labs64.netlicensing.util.AgentResponseSignatureVerifier;

AgentResponseSignatureVerifier.verify(
        responseXml,
        licenseeNumber,
        licenseeApiKeyPublicKeyPem);
```

The verifier checks the Licensee scope and validity of the Agent signing certificate, the binding between that
certificate and the Agent public key, and finally the signature of the complete XML response. It throws
`BadSignatureException` when any check fails; the Product must reject the response in that case. If the request contains
a nonce, the Product must additionally compare it with the signed `netlicensing@id` response attribute.

`LicenseeService.validate` performs this selection automatically when `Context.publicKey` is configured: responses
containing `agentSigning` use the Agent trust chain, while regular signed Core responses retain the existing direct
signature verification. Call `ValidationParameters.setNonce(...)` when request/response replay protection is required;
the client sends it and rejects a response whose signed `netlicensing@id` differs.
