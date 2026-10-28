package com.labs64.netlicensing.util;

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Instant;
import java.util.Base64;
import java.util.Set;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

/** Verification-only parser for compact JWS Agent signing certificates. */
final class AgentSigningCertificateVerifier {

    private static final int SHA_256_LENGTH = 32;
    private static final String VERSION = "version";
    private static final String AGENT_PUBLIC_KEY_DIGEST = "agentPublicKeyDigest";
    private static final String LICENSEE_NUMBER = "licenseeNumber";
    private static final String NOT_BEFORE = "notBefore";
    private static final String NOT_AFTER = "notAfter";
    private static final Set<String> ALLOWED_CLAIMS = Set.of(VERSION, AGENT_PUBLIC_KEY_DIGEST, LICENSEE_NUMBER,
            NOT_BEFORE, NOT_AFTER);
    private static final Set<String> ALLOWED_HEADER_PARAMETERS = Set.of("alg");

    private AgentSigningCertificateVerifier() {
    }

    static AgentSigningCertificate verify(final String compactJws, final PublicKey publicKey) {
        if (!(publicKey instanceof RSAPublicKey rsaPublicKey)) {
            throw new IllegalArgumentException("Agent signing certificate requires an RSA public key.");
        }

        final SignedJWT certificate = parse(compactJws);
        try {
            if (!certificate.verify(new RSASSAVerifier(rsaPublicKey))) {
                throw new IllegalArgumentException("Agent signing certificate signature is invalid.");
            }
            return readClaims(certificate.getJWTClaimsSet());
        } catch (final JOSEException | ParseException e) {
            throw new IllegalArgumentException("Agent signing certificate is invalid.", e);
        }
    }

    private static SignedJWT parse(final String compactJws) {
        requireText(compactJws, "Agent signing certificate");
        try {
            final SignedJWT certificate = SignedJWT.parse(compactJws);

            if (!JWSAlgorithm.RS256.equals(certificate.getHeader().getAlgorithm())) {
                throw new IllegalArgumentException("Unsupported Agent signing certificate algorithm.");
            }

            if (!ALLOWED_HEADER_PARAMETERS.equals(certificate.getHeader().getIncludedParams())) {
                throw new IllegalArgumentException("Agent signing certificate contains unexpected header parameters.");
            }

            return certificate;
        } catch (final ParseException e) {
            throw new IllegalArgumentException("Agent signing certificate is malformed.", e);
        }
    }

    private static AgentSigningCertificate readClaims(final JWTClaimsSet claims) throws ParseException {
        if (!ALLOWED_CLAIMS.equals(claims.getClaims().keySet())) {
            throw new IllegalArgumentException("Agent signing certificate contains unexpected claims.");
        }

        // Keep the signed format marker available for future evolution without introducing
        // version-specific behavior while only one certificate format exists.
        requireNumber(claims, VERSION);

        final Instant notBefore = Instant.ofEpochSecond(requireNumber(claims, NOT_BEFORE).longValue());
        final Instant notAfter = Instant.ofEpochSecond(requireNumber(claims, NOT_AFTER).longValue());
        if (!notAfter.isAfter(notBefore)) {
            throw new IllegalArgumentException("Agent signing certificate validity interval is invalid.");
        }

        return new AgentSigningCertificate(requireDigest(requireString(claims, AGENT_PUBLIC_KEY_DIGEST)),
                requireString(claims, LICENSEE_NUMBER), notBefore, notAfter);
    }

    private static Number requireNumber(final JWTClaimsSet claims, final String name) throws ParseException {
        final Object value = claims.getClaim(name);
        if (!(value instanceof Number number)) {
            throw new ParseException("Claim '" + name + "' is not a number.", 0);
        }
        return number;
    }

    private static String requireString(final JWTClaimsSet claims, final String name) throws ParseException {
        return requireText(claims.getStringClaim(name), "Claim '" + name + "'");
    }

    private static String requireDigest(final String digest) {
        requireText(digest, "Agent public key digest");
        final byte[] decoded;
        try {
            decoded = Base64.getUrlDecoder().decode(digest);
        } catch (final IllegalArgumentException e) {
            throw new IllegalArgumentException("Agent public key digest is not valid Base64URL.", e);
        }

        if (decoded.length != SHA_256_LENGTH
                || !digest.equals(Base64.getUrlEncoder().withoutPadding().encodeToString(decoded))) {
            throw new IllegalArgumentException("Agent public key digest is not a canonical SHA-256 digest.");
        }

        return digest;
    }

    private static String requireText(final String value, final String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " is missing.");
        }

        return value;
    }
}
