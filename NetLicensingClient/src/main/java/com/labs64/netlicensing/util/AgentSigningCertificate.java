package com.labs64.netlicensing.util;

import java.time.Instant;

/** Verified authorization of one Agent signing key for one Licensee. */
record AgentSigningCertificate(String agentPublicKeyDigest, String licenseeNumber, Instant notBefore,
        Instant notAfter) {

    boolean isValidAt(final Instant instant) {
        return !instant.isBefore(notBefore) && instant.isBefore(notAfter);
    }
}
