package com.labs64.netlicensing.util;

import java.io.StringReader;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.time.Clock;
import java.util.Base64;
import java.util.Objects;

import javax.xml.XMLConstants;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.labs64.netlicensing.exception.BadSignatureException;

/**
 * Verifies signed offline NetLicensing Agent validation responses.
 */
public final class AgentResponseSignatureVerifier {

    private static final String CONTEXT_NAMESPACE = "http://netlicensing.labs64.com/schema/context";
    private static final String XMLDSIG_NAMESPACE = "http://www.w3.org/2000/09/xmldsig#";
    private static final String XML_SIGNATURE_ELEMENT = "Signature";
    private static final String AGENT_SIGNING_ELEMENT = "agentSigning";
    private static final String CERTIFICATE_ELEMENT = "certificate";
    private static final String PUBLIC_KEY_ELEMENT = "publicKey";

    private AgentResponseSignatureVerifier() {
    }

    /**
     * Verify a signed Agent response using a Licensee API key public key.
     *
     * @param responseXml
     *                                raw Agent validation response XML
     * @param expectedLicensee
     *                                Licensee number used for validation
     * @param licenseeApiKeyPublicKey
     *                                public key corresponding to the Licensee API
     *                                key
     * @throws BadSignatureException
     *                               if any part of the response trust chain is
     *                               invalid
     */
    public static void verify(final String responseXml, final String expectedLicensee,
            final PublicKey licenseeApiKeyPublicKey) throws BadSignatureException {
        verify(responseXml, expectedLicensee, licenseeApiKeyPublicKey, Clock.systemUTC());
    }

    /**
     * Verify a signed Agent response using a PEM or Base64-encoded Licensee API key
     * public key.
     *
     * @param responseXml
     *                                raw Agent validation response XML
     * @param expectedLicensee
     *                                Licensee number used for validation
     * @param licenseeApiKeyPublicKey
     *                                PEM or Base64-encoded public key corresponding
     *                                to the Licensee API key
     * @throws BadSignatureException
     *                               if any part of the response trust chain is
     *                               invalid
     */
    public static void verify(final String responseXml, final String expectedLicensee,
            final String licenseeApiKeyPublicKey) throws BadSignatureException {
        try {
            final PublicKey publicKey = SignatureUtils.readPublicKey(
                    SignatureUtils.cleansePublicKey(licenseeApiKeyPublicKey));
            verify(responseXml, expectedLicensee, publicKey);
        } catch (final BadSignatureException e) {
            throw e;
        } catch (final Exception e) {
            throw failure("Licensee API key public key is malformed.", e);
        }
    }

    static void verify(final String responseXml, final String expectedLicensee,
            final PublicKey licenseeApiKeyPublicKey, final Clock clock) throws BadSignatureException {
        try {
            requireText(expectedLicensee, "Expected Licensee number");
            Objects.requireNonNull(licenseeApiKeyPublicKey, "Licensee API key public key is missing.");
            Objects.requireNonNull(clock, "Clock is missing.");

            final Document document = parseSecurely(responseXml);
            final Element root = document.getDocumentElement();
            if (!CONTEXT_NAMESPACE.equals(root.getNamespaceURI()) || !"netlicensing".equals(root.getLocalName())) {
                throw new IllegalArgumentException("Unexpected Agent validation response root.");
            }

            if (document.getElementsByTagNameNS(XMLDSIG_NAMESPACE, XML_SIGNATURE_ELEMENT).getLength() != 1) {
                throw new IllegalArgumentException("Agent validation response must contain exactly one signature.");
            }

            final Element signature = requireSingleDirectChild(root, XMLDSIG_NAMESPACE, XML_SIGNATURE_ELEMENT);
            enforceSignatureProfile(signature);

            final Element agentSigning = requireSingleDirectChild(root, CONTEXT_NAMESPACE, AGENT_SIGNING_ELEMENT);
            final String compactCertificate = requireSingleDirectChild(agentSigning, CONTEXT_NAMESPACE,
                    CERTIFICATE_ELEMENT).getTextContent().trim();
            final String encodedPublicKey = requireSingleDirectChild(agentSigning, CONTEXT_NAMESPACE,
                    PUBLIC_KEY_ELEMENT)
                    .getTextContent().trim();
            final AgentSigningCertificate certificate = AgentSigningCertificateVerifier.verify(compactCertificate,
                    licenseeApiKeyPublicKey);

            if (!expectedLicensee.equals(certificate.licenseeNumber())) {
                throw new IllegalArgumentException("Agent signing certificate Licensee scope does not match request.");
            }

            if (!certificate.isValidAt(clock.instant())) {
                throw new IllegalArgumentException("Agent signing certificate is not currently valid.");
            }

            final PublicKey agentPublicKey = decodeRsaPublicKey(encodedPublicKey);
            if (!MessageDigest.isEqual(decodeDigest(certificate.agentPublicKeyDigest()), digest(agentPublicKey))) {
                throw new IllegalArgumentException("Agent signing public key does not match its certificate.");
            }

            final DOMValidateContext validationContext = new DOMValidateContext(agentPublicKey, signature);
            validationContext.setProperty("org.jcp.xml.dsig.secureValidation", Boolean.TRUE);
            if (!XMLSignatureFactory.getInstance("DOM").unmarshalXMLSignature(validationContext)
                    .validate(validationContext)) {
                throw new IllegalArgumentException("Agent validation response signature is invalid.");
            }
        } catch (final Exception e) {
            throw failure("Agent validation response signature verification failed.", e);
        }
    }

    private static Document parseSecurely(final String responseXml) throws Exception {
        requireText(responseXml, "Agent validation response");

        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        return factory.newDocumentBuilder().parse(new InputSource(new StringReader(responseXml)));
    }

    private static PublicKey decodeRsaPublicKey(final String encodedPublicKey) {
        try {
            return KeyFactory.getInstance("RSA").generatePublic(
                    new X509EncodedKeySpec(Base64.getDecoder().decode(encodedPublicKey)));
        } catch (final GeneralSecurityException | RuntimeException e) {
            throw new IllegalArgumentException("Agent signing public key is malformed.", e);
        }
    }

    private static byte[] digest(final PublicKey publicKey) {
        try {
            return MessageDigest.getInstance("SHA-256").digest(publicKey.getEncoded());
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is unavailable.", e);
        }
    }

    private static byte[] decodeDigest(final String digest) {
        requireText(digest, "Agent public key digest");
        try {
            return Base64.getUrlDecoder().decode(digest);
        } catch (final IllegalArgumentException e) {
            throw new IllegalArgumentException("Agent public key digest is not valid Base64URL.", e);
        }
    }

    private static void enforceSignatureProfile(final Element signature) {
        final Element signatureMethod = requireSingleDescendant(signature, XMLDSIG_NAMESPACE, "SignatureMethod");
        if (!SignatureMethod.RSA_SHA256.equals(signatureMethod.getAttribute("Algorithm"))) {
            throw new IllegalArgumentException("Unexpected Agent response signature algorithm.");
        }

        final Element reference = requireSingleDescendant(signature, XMLDSIG_NAMESPACE, "Reference");
        if (!"".equals(reference.getAttribute("URI"))) {
            throw new IllegalArgumentException("Agent response signature must reference the document root.");
        }

        final Element digestMethod = requireSingleDescendant(reference, XMLDSIG_NAMESPACE, "DigestMethod");
        if (!DigestMethod.SHA256.equals(digestMethod.getAttribute("Algorithm"))) {
            throw new IllegalArgumentException("Unexpected Agent response digest algorithm.");
        }

        final NodeList transforms = reference.getElementsByTagNameNS(XMLDSIG_NAMESPACE, "Transform");
        if (transforms.getLength() != 1
                || !Transform.ENVELOPED.equals(((Element) transforms.item(0)).getAttribute("Algorithm"))) {
            throw new IllegalArgumentException("Unexpected Agent response signature transforms.");
        }
    }

    private static String requireText(final String value, final String name) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(name + " is missing.");
        }
        return value;
    }

    private static Element requireSingleDirectChild(final Element parent, final String namespace,
            final String localName) {
        Element found = null;
        for (Node node = parent.getFirstChild(); node != null; node = node.getNextSibling()) {
            if (node instanceof Element element && namespace.equals(element.getNamespaceURI())
                    && localName.equals(element.getLocalName())) {
                if (found != null) {
                    throw new IllegalArgumentException("Duplicate '" + localName + "' element.");
                }
                found = element;
            }
        }
        if (found == null) {
            throw new IllegalArgumentException("Missing '" + localName + "' element.");
        }
        return found;
    }

    private static Element requireSingleDescendant(final Element parent, final String namespace,
            final String localName) {
        final NodeList nodes = parent.getElementsByTagNameNS(namespace, localName);
        if (nodes.getLength() != 1) {
            throw new IllegalArgumentException("Expected exactly one '" + localName + "' element.");
        }
        return (Element) nodes.item(0);
    }

    private static BadSignatureException failure(final String message, final Exception cause) {
        if (cause instanceof BadSignatureException badSignatureException) {
            return badSignatureException;
        }
        return new BadSignatureException(message, cause);
    }

}
