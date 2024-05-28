/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.labs64.netlicensing.util;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class XMLDSigValidatorCustomTest {

  private static InputStream loadFileContent(final String fileName) {
    final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    final InputStream inputStream = classLoader.getResourceAsStream(fileName);
    return inputStream;
  }

  private static Document loadXmlDocument(final String fileName) throws Exception {
    final DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
    dFactory.setNamespaceAware(true);
    final DocumentBuilder dBuilder = dFactory.newDocumentBuilder();
    final Document aDoc = dBuilder.parse(loadFileContent(fileName));
    assertNotNull(aDoc);
    return aDoc;
  }

  @Test
  public void testNotSignedXml() throws Exception {
    final Document aDoc = loadXmlDocument("xmldsig/xml-not-signed.xml");
    assertFalse(XMLDSigValidatorCustom.containsSignature(aDoc));
  }

  @Test
  public void testSignedXml01() throws Exception {
    final Document aDoc = loadXmlDocument("xmldsig/xml-signed-inline-01.xml");
    assertTrue(XMLDSigValidatorCustom.containsSignature(aDoc));
    assertTrue(XMLDSigValidatorCustom.validateSignature(aDoc).isValid());
  }

  @Test
  public void testSignedXml02() throws Exception {
    final Document aDoc = loadXmlDocument("xmldsig/xml-signed-inline-02.xml");
    assertTrue(XMLDSigValidatorCustom.containsSignature(aDoc));
    assertTrue(XMLDSigValidatorCustom.validateSignature(aDoc).isValid());
  }

  @Test
  public void testSignedXmlNLIC01() throws Exception {
    final Document aDoc = loadXmlDocument("xmldsig/xml-signed-nlic-01.xml");
    assertTrue(XMLDSigValidatorCustom.containsSignature(aDoc));

    final String publicKeyContent = IOUtils.toString(loadFileContent("xmldsig/rsa_public.pem"),
        StandardCharsets.UTF_8.name());
    final PublicKey publicKey = SignatureUtils.readPublicKey(SignatureUtils.cleansePublicKey(publicKeyContent));
    assertTrue(XMLDSigValidatorCustom.validateSignature(aDoc, publicKey).isValid());
  }

  @Test
  public void testSignedXmlNLIC01_content_mismatch() throws Exception {
    final Document aDoc = loadXmlDocument("xmldsig/xml-signed-nlic-02-mismatch.xml");
    assertTrue(XMLDSigValidatorCustom.containsSignature(aDoc));

    final String publicKeyContent = IOUtils.toString(loadFileContent("xmldsig/rsa_public.pem"),
        StandardCharsets.UTF_8.name());
    final PublicKey publicKey = SignatureUtils.readPublicKey(SignatureUtils.cleansePublicKey(publicKeyContent));
    assertFalse(XMLDSigValidatorCustom.validateSignature(aDoc, publicKey).isValid());
  }

  @Test
  public void testSignedXmlNLIC01_wrong_key() throws Exception {
    final Document aDoc = loadXmlDocument("xmldsig/xml-signed-nlic-01.xml");
    assertTrue(XMLDSigValidatorCustom.containsSignature(aDoc));

    final String publicKeyContent = IOUtils.toString(loadFileContent("xmldsig/rsa_public_wrong.pem"),
        StandardCharsets.UTF_8.name());
    final PublicKey publicKey = SignatureUtils.readPublicKey(SignatureUtils.cleansePublicKey(publicKeyContent));
    assertFalse(XMLDSigValidatorCustom.validateSignature(aDoc, publicKey).isValid());
  }

}
