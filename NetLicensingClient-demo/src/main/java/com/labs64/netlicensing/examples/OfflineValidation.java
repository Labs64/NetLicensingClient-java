package com.labs64.netlicensing.examples;

import java.io.IOException;
import java.io.StringReader;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.MetaInfo;
import com.labs64.netlicensing.domain.vo.ValidationResult;
import com.labs64.netlicensing.exception.NetLicensingException;
import com.labs64.netlicensing.schema.context.Netlicensing;
import com.labs64.netlicensing.service.ValidationService;
import com.labs64.netlicensing.utils.ConsoleWriter;
import com.labs64.netlicensing.utils.TestHelpers;

/**
 * Demonstrates how to work with offline validation file.
 * <p>
 * This example uses pre-generated validation file and key. In a real-world application you'll need to do the following
 * steps in order to obtain your own files:
 * </p>
 * <ul>
 * <li>Create NetLicensing configuration for your product and add the customer</li>
 * <li>Generate key pair</li>
 * <li>Create NetLicensing API key with private key</li>
 * <li>Download offline validation file using UI or API</li>
 * <li>Use your public key and the validation file from above with this code</li>
 * </ul>
 * <p>
 * Your application should take care of storing the public key securely, loading it from the file here is merely for
 * demonstration purposes.
 * </p>
 */
public class OfflineValidation implements NetLicensingExample {

    @Override
    public void execute() {
        final ConsoleWriter out = new ConsoleWriter();
        try {
            // 1. Create context, for offline validation you only need to provide the public key.
            final String publicKey = TestHelpers.loadFileContent("rsa_public.pem");
            final Context context = new Context();
            context.setPublicKey(publicKey);

            // 2. Read the validation file as 'Netlicensing' object.
            final String offlineValidation = TestHelpers.loadFileContent("Isb-DEMO.xml");
            final JAXBContext jaxbContext = JAXBContext.newInstance(Netlicensing.class);
            final Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            final StringReader reader = new StringReader(offlineValidation);
            final Netlicensing validationFile = (Netlicensing) jaxbUnmarshaller.unmarshal(reader);

            // 3. Validate. ValidationResult is same as if validation would be executed against the
            // NetLicensing service online.
            final MetaInfo meta = new MetaInfo();
            final ValidationResult validationResult = ValidationService.validateOffline(context, validationFile, meta);

            out.writeObject("Validation result (Offline / signed):", validationResult);

        } catch (final JAXBException | NetLicensingException | IOException e) {
            out.writeException("Failure:", e);
        }
    }

}
