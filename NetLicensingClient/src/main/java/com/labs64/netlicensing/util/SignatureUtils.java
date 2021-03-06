package com.labs64.netlicensing.util;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;

import com.helger.xmldsig.XMLDSigValidationResult;

import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.exception.BadSignatureException;
import com.labs64.netlicensing.schema.context.Netlicensing;

public class SignatureUtils {

    public static void check(final Context context, final Netlicensing response) throws BadSignatureException {
        if (StringUtils.isNotEmpty(context.getPublicKey())) {
            try {
                check(response, context.getPublicKey().getBytes());
            } catch (final Exception e) {
                throw new BadSignatureException(e.getMessage());
            }
        }
    }

    public static void check(final Netlicensing response, final byte[] publicKeyByteArray)
            throws JAXBException, ParserConfigurationException, SignatureException {

        final JAXBContext jc = JAXBContext.newInstance(Netlicensing.class);

        // Create the Document
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        final DocumentBuilder db = dbf.newDocumentBuilder();
        final Document doc = db.newDocument();
        doc.setXmlStandalone(true);

        // Marshal the NetLicensing to a Document
        final Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(response, doc);

        boolean isValidAssetsFile = false;
        try {
            final byte[] publicKeyByte = Base64.getDecoder().decode(publicKeyByteArray);
            final X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyByte);
            final KeyFactory kf = KeyFactory.getInstance("RSA");
            final PublicKey publicKey = kf.generatePublic(spec);

            final XMLDSigValidationResult validation = XMLDSigValidatorCustom.validateSignature(doc, publicKey);
            isValidAssetsFile = validation.isValid();
        } catch (final IllegalArgumentException | XMLSignatureException e) {
            throw new SignatureException("Bad or empty response signature", e);
        } catch (final NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new SignatureException("Bad response signature", e);
        }
        if (!isValidAssetsFile) {
            throw new SignatureException("Response signature verification failure");
        }
    }
}
