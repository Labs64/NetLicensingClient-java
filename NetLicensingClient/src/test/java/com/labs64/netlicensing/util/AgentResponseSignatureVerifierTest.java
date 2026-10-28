package com.labs64.netlicensing.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.StringReader;
import java.io.StringWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import com.labs64.netlicensing.exception.BadSignatureException;

public class AgentResponseSignatureVerifierTest {

    private static final String NONCE = "00112233445566778899aabbccddeeff";
    private static final String LICENSEE_NUMBER = "I-001";

    private KeyPair licenseeKeyPair;
    private KeyPair agentKeyPair;

    @BeforeEach
    public void setUp() throws Exception {
        final KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        licenseeKeyPair = generator.generateKeyPair();
        agentKeyPair = generator.generateKeyPair();
    }

    @Test
    public void verifiesCompleteAgentResponseTrustChain() throws Exception {
        final String response = createSignedResponse(Instant.now().minusSeconds(1),
                Instant.now().plus(1, ChronoUnit.DAYS));

        assertDoesNotThrow(() -> AgentResponseSignatureVerifier.verify(response, LICENSEE_NUMBER,
                licenseeKeyPair.getPublic()));
    }

    @Test
    public void acceptsPemEncodedLicenseePublicKey() throws Exception {
        final String response = createSignedResponse(Instant.now().minusSeconds(1),
                Instant.now().plus(1, ChronoUnit.DAYS));
        final String publicKey = "-----BEGIN PUBLIC KEY-----\n"
                + Base64.getEncoder().encodeToString(licenseeKeyPair.getPublic().getEncoded())
                + "\n-----END PUBLIC KEY-----";

        assertDoesNotThrow(() -> AgentResponseSignatureVerifier.verify(response, LICENSEE_NUMBER, publicKey));
    }

    @Test
    public void rejectsModifiedResponse() throws Exception {
        final String response = createSignedResponse(Instant.now().minusSeconds(1),
                Instant.now().plus(1, ChronoUnit.DAYS));
        final String modifiedResponse = response.replace("valid=\"true\"", "valid=\"false\"");

        assertThrows(BadSignatureException.class, () -> AgentResponseSignatureVerifier.verify(modifiedResponse,
                LICENSEE_NUMBER, licenseeKeyPair.getPublic()));
    }

    @Test
    public void rejectsCertificateOutsideItsValidityPeriod() throws Exception {
        final String response = createSignedResponse(Instant.now().minus(2, ChronoUnit.DAYS),
                Instant.now().minus(1, ChronoUnit.DAYS));

        assertThrows(BadSignatureException.class, () -> AgentResponseSignatureVerifier.verify(response,
                LICENSEE_NUMBER, licenseeKeyPair.getPublic()));
    }

    @Test
    public void rejectsCertificateSignedByUnexpectedLicenseeKey() throws Exception {
        final String response = createSignedResponse(Instant.now().minusSeconds(1),
                Instant.now().plus(1, ChronoUnit.DAYS));
        final KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        final KeyPair unexpectedLicenseeKeyPair = generator.generateKeyPair();

        assertThrows(BadSignatureException.class, () -> AgentResponseSignatureVerifier.verify(response,
                LICENSEE_NUMBER, unexpectedLicenseeKeyPair.getPublic()));
    }

    @Test
    public void acceptsNumericCertificateVersionWithoutVersionSpecificHandling() throws Exception {
        final String response = createSignedResponse(Instant.now().minusSeconds(1),
                Instant.now().plus(1, ChronoUnit.DAYS), 2);

        assertDoesNotThrow(() -> AgentResponseSignatureVerifier.verify(response, LICENSEE_NUMBER,
                licenseeKeyPair.getPublic()));
    }

    @Test
    public void rejectsUnexpectedLicensee() throws Exception {
        final String response = createSignedResponse(Instant.now().minusSeconds(1),
                Instant.now().plus(1, ChronoUnit.DAYS));

        assertThrows(BadSignatureException.class, () -> AgentResponseSignatureVerifier.verify(response, "I-002",
                licenseeKeyPair.getPublic()));
    }

    private String createSignedResponse(final Instant notBefore, final Instant notAfter) throws Exception {
        return createSignedResponse(notBefore, notAfter, 1);
    }

    private String createSignedResponse(final Instant notBefore, final Instant notAfter, final int version)
            throws Exception {
        final String digest = Base64.getUrlEncoder().withoutPadding().encodeToString(
                java.security.MessageDigest.getInstance("SHA-256").digest(agentKeyPair.getPublic().getEncoded()));
        final JWTClaimsSet claims = new JWTClaimsSet.Builder().claim("version", version)
                .claim("agentPublicKeyDigest", digest).claim("licenseeNumber", LICENSEE_NUMBER)
                .claim("notBefore", notBefore.getEpochSecond()).claim("notAfter", notAfter.getEpochSecond()).build();
        final SignedJWT certificate = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256).build(), claims);
        certificate.sign(new RSASSASigner(licenseeKeyPair.getPrivate()));

        final String encodedAgentPublicKey = Base64.getEncoder().encodeToString(agentKeyPair.getPublic().getEncoded());
        final String unsignedResponse = "<netlicensing xmlns=\"http://netlicensing.labs64.com/schema/context\" id=\""
                + NONCE + "\"><agentSigning><certificate>" + certificate.serialize()
                + "</certificate><publicKey>" + encodedAgentPublicKey
                + "</publicKey></agentSigning><items><item valid=\"true\"/></items></netlicensing>";

        final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        final Document document = documentBuilderFactory.newDocumentBuilder()
                .parse(new InputSource(new StringReader(unsignedResponse)));
        final XMLSignatureFactory signatureFactory = XMLSignatureFactory.getInstance("DOM");
        final Transform transform = signatureFactory.newTransform(Transform.ENVELOPED,
                (javax.xml.crypto.dsig.spec.TransformParameterSpec) null);
        final Reference reference = signatureFactory.newReference("",
                signatureFactory.newDigestMethod(DigestMethod.SHA256, null), java.util.List.of(transform), null, null);
        final SignedInfo signedInfo = signatureFactory.newSignedInfo(
                signatureFactory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE,
                        (javax.xml.crypto.dsig.spec.C14NMethodParameterSpec) null),
                signatureFactory.newSignatureMethod(SignatureMethod.RSA_SHA256, null), java.util.List.of(reference));
        final XMLSignature signature = signatureFactory.newXMLSignature(signedInfo, null);
        final DOMSignContext signContext = new DOMSignContext(agentKeyPair.getPrivate(), document.getDocumentElement(),
                document.getDocumentElement().getFirstChild());
        signContext.setDefaultNamespacePrefix("");
        signature.sign(signContext);

        final StringWriter writer = new StringWriter();
        final javax.xml.transform.Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.transform(new DOMSource(document), new StreamResult(writer));
        return writer.toString();
    }
}
