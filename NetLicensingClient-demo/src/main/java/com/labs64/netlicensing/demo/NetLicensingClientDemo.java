/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.labs64.netlicensing.demo;

import java.math.BigDecimal;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.Country;
import com.labs64.netlicensing.domain.entity.License;
import com.labs64.netlicensing.domain.entity.LicenseTemplate;
import com.labs64.netlicensing.domain.entity.Licensee;
import com.labs64.netlicensing.domain.entity.PaymentMethod;
import com.labs64.netlicensing.domain.entity.Product;
import com.labs64.netlicensing.domain.entity.ProductModule;
import com.labs64.netlicensing.domain.entity.Token;
import com.labs64.netlicensing.domain.entity.Transaction;
import com.labs64.netlicensing.domain.entity.impl.LicenseImpl;
import com.labs64.netlicensing.domain.entity.impl.LicenseTemplateImpl;
import com.labs64.netlicensing.domain.entity.impl.LicenseeImpl;
import com.labs64.netlicensing.domain.entity.impl.ProductImpl;
import com.labs64.netlicensing.domain.entity.impl.ProductModuleImpl;
import com.labs64.netlicensing.domain.entity.impl.TokenImpl;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Currency;
import com.labs64.netlicensing.domain.vo.LicenseType;
import com.labs64.netlicensing.domain.vo.LicenseeSecretMode;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.domain.vo.SecurityMode;
import com.labs64.netlicensing.domain.vo.TokenType;
import com.labs64.netlicensing.domain.vo.ValidationParameters;
import com.labs64.netlicensing.domain.vo.ValidationResult;
import com.labs64.netlicensing.exception.NetLicensingException;
import com.labs64.netlicensing.service.LicenseService;
import com.labs64.netlicensing.service.LicenseTemplateService;
import com.labs64.netlicensing.service.LicenseeService;
import com.labs64.netlicensing.service.PaymentMethodService;
import com.labs64.netlicensing.service.ProductModuleService;
import com.labs64.netlicensing.service.ProductService;
import com.labs64.netlicensing.service.TokenService;
import com.labs64.netlicensing.service.TransactionService;
import com.labs64.netlicensing.service.UtilityService;

public class NetLicensingClientDemo {

    /**
     * Exit codes
     */
    private final static int CODE_OK = 0;
    private final static int CODE_ERROR = 1;

    private final static String DEMO_NUMBER_PREFIX = "DEMO-";

    private static final String randomLicenseeSecret = UUID.randomUUID().toString();

    public static void main(final String[] args) {

        // configure J.U.L. to Slf4j bridge for Jersey
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        final Context context = new Context();
        context.setBaseUrl("https://go.netlicensing.io/core/v2/rest");
        context.setSecurityMode(SecurityMode.BASIC_AUTHENTICATION);
        context.setUsername("demo");
        context.setPassword("demo");

        final String randomNumber = RandomStringUtils.randomAlphanumeric(8);
        final String productNumber = numberWithPrefix("P", randomNumber);
        final String productModuleNumber = numberWithPrefix("PM", randomNumber);
        final String licenseTemplateNumber = numberWithPrefix("LT", randomNumber);
        final String licenseeNumber = numberWithPrefix("L", randomNumber);
        final String licenseNumber = numberWithPrefix("LC", randomNumber);
        final String licenseeName = numberWithPrefix("Licensee ", RandomStringUtils.randomAlphanumeric(8));

        final ConsoleWriter out = new ConsoleWriter();

        int exitCode = CODE_OK;
        try {

            // region ********* Lists

            final Page<String> licenseTypes = UtilityService.listLicenseTypes(context);
            out.writePage("License Types:", licenseTypes);

            final Page<String> licensingModels = UtilityService.listLicensingModels(context);
            out.writePage("Licensing Models:", licensingModels);

            final Page<Country> countries = UtilityService.listCountries(context, null);
            out.writePage("Countries:", countries);

            // endregion

            // region ********* Product

            final Product newProduct = new ProductImpl();
            newProduct.setNumber(productNumber);
            newProduct.setName("Demo product");
            Product product = ProductService.create(context, newProduct);
            out.writeObject("Added product:", product);

            product = ProductService.get(context, productNumber);
            out.writeObject("Got product:", product);

            Page<Product> products = ProductService.list(context, null);
            out.writePage("Got the following products:", products);

            final Product updateProduct = new ProductImpl();
            updateProduct.addProperty("Updated property name", "Updated value");
            updateProduct.addProperty(Constants.Product.PROP_LICENSEE_SECRET_MODE,
                    LicenseeSecretMode.PREDEFINED.toString());
            product = ProductService.update(context, productNumber, updateProduct);
            out.writeObject("Updated product:", product);

            ProductService.delete(context, productNumber, true);
            out.writeMessage("Deleted Product!");

            products = ProductService.list(context, null);
            out.writePage("Got the following Products:", products);

            product = ProductService.create(context, newProduct);
            out.writeObject("Added product again:", product);

            products = ProductService.list(context, null);
            out.writePage("Got the following Products:", products);

            // endregion

            // region ********* ProductModule

            final ProductModule newProductModule = new ProductModuleImpl();
            newProductModule.setNumber(productModuleNumber);
            newProductModule.setName("Demo product module");
            newProductModule.setLicensingModel(Constants.LicensingModel.TryAndBuy.NAME);
            ProductModule productModule = ProductModuleService.create(context, productNumber, newProductModule);
            out.writeObject("Added product module:", productModule);

            productModule = ProductModuleService.get(context, productModuleNumber);
            out.writeObject("Got product module:", productModule);

            Page<ProductModule> productModules = ProductModuleService.list(context, null);
            out.writePage("Got the following product modules:", productModules);

            final ProductModule updateProductModule = new ProductModuleImpl();
            updateProductModule.addProperty("Updated property name", "Updated property value");
            productModule = ProductModuleService.update(context, productModuleNumber, updateProductModule);
            out.writeObject("Updated product module:", productModule);

            ProductModuleService.delete(context, productModuleNumber, true);
            out.writeMessage("Deleted product module!");

            productModules = ProductModuleService.list(context, null);
            out.writePage("Got the following product modules:", productModules);

            productModule = ProductModuleService.create(context, productNumber, newProductModule);
            out.writeObject("Added product module again:", productModule);

            productModules = ProductModuleService.list(context, null);
            out.writePage("Got the following product modules:", productModules);

            // endregion

            // region ********* LicenseTemplate

            final LicenseTemplate newLicenseTemplate = new LicenseTemplateImpl();
            newLicenseTemplate.setNumber(licenseTemplateNumber);
            newLicenseTemplate.setName("Demo Evaluation Period");
            newLicenseTemplate.setLicenseType(LicenseType.FEATURE);
            newLicenseTemplate.setPrice(new BigDecimal(12.5));
            newLicenseTemplate.setCurrency(Currency.EUR);
            newLicenseTemplate.setAutomatic(false);
            newLicenseTemplate.setHidden(false);
            out.writeObject("Adding license template:", newLicenseTemplate);
            LicenseTemplate licenseTemplate = LicenseTemplateService.create(context, productModuleNumber,
                    newLicenseTemplate);
            out.writeObject("Added license template:", licenseTemplate);

            licenseTemplate = LicenseTemplateService.get(context, licenseTemplateNumber);
            out.writeObject("Got licenseTemplate:", licenseTemplate);

            Page<LicenseTemplate> licenseTemplates = LicenseTemplateService.list(context, null);
            out.writePage("Got the following license templates:", licenseTemplates);

            final LicenseTemplate updateLicenseTemplate = new LicenseTemplateImpl();
            updateLicenseTemplate.addProperty("Updated property name", "Updated value");
            licenseTemplate = LicenseTemplateService.update(context, licenseTemplateNumber,
                    updateLicenseTemplate);
            out.writeObject("Updated license template:", licenseTemplate);

            LicenseTemplateService.delete(context, licenseTemplateNumber, true);
            out.writeMessage("Deleted license template!");

            licenseTemplates = LicenseTemplateService.list(context, null);
            out.writePage("Got the following license templates:", licenseTemplates);

            licenseTemplate = LicenseTemplateService.create(context, productModuleNumber, newLicenseTemplate);
            out.writeObject("Added license template again:", licenseTemplate);

            licenseTemplates = LicenseTemplateService.list(context, null);
            out.writePage("Got the following license templates:", licenseTemplates);

            // endregion

            // region ********* Licensee

            final Licensee newLicensee = new LicenseeImpl();
            newLicensee.setNumber(licenseeNumber);
            Licensee licensee = LicenseeService.create(context, productNumber, newLicensee);
            out.writeObject("Added licensee:", licensee);

            Page<Licensee> licensees = LicenseeService.list(context, null);
            out.writePage("Got the following licensees:", licensees);

            LicenseeService.delete(context, licenseeNumber, true);
            out.writeMessage("Deleted licensee!");

            licensees = LicenseeService.list(context, null);
            out.writePage("Got the following licensees after delete:", licensees);

            licensee = LicenseeService.create(context, productNumber, newLicensee);
            out.writeObject("Added licensee again:", licensee);

            licensee = LicenseeService.get(context, licenseeNumber);
            out.writeObject("Got licensee:", licensee);

            final Licensee updateLicensee = new LicenseeImpl();
            updateLicensee.addProperty("Updated property name", "Updated value");
            updateLicensee.addProperty(Constants.Licensee.PROP_LICENSEE_SECRET, randomLicenseeSecret);

            licensee = LicenseeService.update(context, licenseeNumber, updateLicensee);
            out.writeObject("Updated licensee:", licensee);

            licensees = LicenseeService.list(context, null);
            out.writePage("Got the following licensees:", licensees);

            // endregion

            // region ********* License

            final License newLicense = new LicenseImpl();
            newLicense.setNumber(licenseNumber);
            License license = LicenseService.create(context, licenseeNumber, licenseTemplateNumber, null,
                    newLicense);
            out.writeObject("Added license:", license);

            Page<License> licenses = LicenseService.list(context, null);
            out.writePage("Got the following licenses:", licenses);

            LicenseService.delete(context, licenseNumber, true);
            out.writeMessage("Deleted license!");

            licenses = LicenseService.list(context, null);
            out.writePage("Got the following licenses:", licenses);

            license = LicenseService.create(context, licenseeNumber, licenseTemplateNumber, null,
                    newLicense);
            out.writeObject("Added license again:", license);

            license = LicenseService.get(context, licenseNumber);
            out.writeObject("Got license:", license);

            final License updateLicense = new LicenseImpl();
            updateLicense.addProperty("Updated property name", "Updated value");
            license = LicenseService.update(context, licenseNumber, null, updateLicense);
            out.writeObject("Updated license:", license);

            // endregion

            // region ********* PaymentMethod

            final Page<PaymentMethod> paymentMethods = PaymentMethodService.list(context, null);
            out.writePage("Got the following payment methods:", paymentMethods);

            // endregion

            // region ********* Token

            final Token newToken = new TokenImpl();
            newToken.setTokenType(TokenType.APIKEY);
            newToken.addProperty(Constants.Token.TOKEN_PROP_PRIVATE_KEY,
                    "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAhleNJf9h+aZ9KlkLLIUiqt4p3O8kAijzvEUSG4CuS95"
                            + "VsUC6iVnpTlepyLB4ZImyWBjcNme4DLufbwCKi0iPzQIDAQABAkBSv7sBnL0MubB/VTm8woUIGrBOlj7n1bHMVf9"
                            + "BUZIKyI/2qOVmFKtlxXXe8i5XHcg0pZukICTSWB4htxXqs8ABAiEA4JUTuq9yl2jy4aAyyrOQyPJ9s2a449tsiw3"
                            + "VNIcS8wECIQCZIrcE1FxNKZLgE4mrMnfZwXJ4MqO2WH6QGznMHLP4zQIgCxwUz8ViG89bRIISQSjE3svwH/HS76K"
                            + "pKe/TPjf4XgECIQCRE5pgMO/hCmnjb58VWZLB8csIpLEEp4H/9EslXGwEYQIgVw4LJk0EINngF2qSv0z12Q29WMr"
                            + "aaNNcwvc3k5g5kqc=");
            final Token apiKey = TokenService.create(context, newToken);
            out.writeObject("Created APIKey:", apiKey);

            context.setApiKey(apiKey.getNumber());
            newToken.setTokenType(TokenType.SHOP);
            newToken.addProperty(Constants.Licensee.LICENSEE_NUMBER, licenseeNumber);
            context.setSecurityMode(SecurityMode.APIKEY_IDENTIFICATION);
            final Token shopToken = TokenService.create(context, newToken);
            context.setSecurityMode(SecurityMode.BASIC_AUTHENTICATION);
            out.writeObject("Got the following shop token:", shopToken);

            final String filter = Constants.Token.TOKEN_TYPE + "=" + TokenType.SHOP.name();
            Page<Token> tokens = TokenService.list(context, filter);
            out.writePage("Got the following shop tokens:", tokens);

            TokenService.delete(context, shopToken.getNumber());
            out.writeMessage("Deleted shop token!");

            tokens = TokenService.list(context, filter);
            out.writePage("Got the following shop tokens after delete:", tokens);

            // endregion

            // region ********* Validate

            final ValidationParameters validationParameters = new ValidationParameters();
            validationParameters.put(productModuleNumber, "paramKey", "paramValue");
            validationParameters.setLicenseeSecret(randomLicenseeSecret);
            validationParameters.setLicenseeName(licenseeName);
            validationParameters.setProductNumber(productNumber);

            ValidationResult validationResult = null;

            // Validate using Basic Auth
            context.setSecurityMode(SecurityMode.BASIC_AUTHENTICATION);
            validationResult = LicenseeService.validate(context, licenseeNumber, validationParameters);
            out.writeObject("Validation result (Basic Auth):", validationResult);

            // Validate using APIKey
            context.setSecurityMode(SecurityMode.APIKEY_IDENTIFICATION);
            validationResult = LicenseeService.validate(context, licenseeNumber, validationParameters);
            out.writeObject("Validation result (APIKey):", validationResult);

            // Validate using APIKey signed
            context.setSecurityMode(SecurityMode.APIKEY_IDENTIFICATION);
            validationParameters.setPublicKey("MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIZXjSX/YfmmfSpZCyyFIqreKdzvJAIo87xFEhuA"
                    + "rkveVbFAuolZ6U5XqciweGSJslgY3DZnuAy7n28AiotIj80CAwEAAQ==");
            validationResult = LicenseeService.validate(context, licenseeNumber, validationParameters);
            out.writeObject("Validation result (APIKey / signed):", validationResult);

            // reset context for futher use
            context.setSecurityMode(SecurityMode.BASIC_AUTHENTICATION);

            // endregion

            // region ********* Transfer
            Licensee transferLicensee = new LicenseeImpl();
            transferLicensee.setNumber("TR" + licenseeNumber);
            transferLicensee.getProperties().put(Constants.Licensee.PROP_MARKED_FOR_TRANSFER, Boolean.toString(true));
            transferLicensee = LicenseeService.create(context, productNumber, transferLicensee);
            out.writeObject("Added transfer licensee:", transferLicensee);

            final License transferLicense = new LicenseImpl();
            transferLicense.setNumber("LTR" + licenseNumber);
            final License newTransferLicense = LicenseService.create(context, transferLicensee.getNumber(),
                    licenseTemplateNumber, null, transferLicense);
            out.writeObject("Added license for transfer:", newTransferLicense);

            LicenseeService.transfer(context, licensee.getNumber(), transferLicensee.getNumber());

            licenses = LicenseService.list(context, "licenseeNumber=" + licensee.getNumber());
            out.writePage("Got the following licenses after transfer:", licenses);

            Licensee transferLicenseeWithApiKey = new LicenseeImpl();
            transferLicenseeWithApiKey.setNumber("Key" + licenseeNumber);
            transferLicenseeWithApiKey.getProperties().put(Constants.Licensee.PROP_MARKED_FOR_TRANSFER,
                    Boolean.toString(true));
            transferLicenseeWithApiKey = LicenseeService.create(context, productNumber, transferLicenseeWithApiKey);

            final License transferLicenseWithApiKey = new LicenseImpl();
            transferLicenseWithApiKey.setNumber("Key" + licenseNumber);
            LicenseService.create(context, transferLicenseeWithApiKey.getNumber(), licenseTemplateNumber, null,
                    transferLicenseWithApiKey);

            context.setSecurityMode(SecurityMode.APIKEY_IDENTIFICATION);
            LicenseeService.transfer(context, licensee.getNumber(), transferLicenseeWithApiKey.getNumber());

            context.setSecurityMode(SecurityMode.BASIC_AUTHENTICATION);

            licenses = LicenseService.list(context, "licenseeNumber=" + licensee.getNumber());
            out.writePage("Got the following licenses after transfer:", licenses);
            // endregion

            // region ********* Transactions
            Page<Transaction> transactions = TransactionService.list(context,
                    Constants.Transaction.SOURCE_SHOP_ONLY + "=" + Boolean.TRUE.toString());
            out.writePage("Got the following transactions shop only:", transactions);

            transactions = TransactionService.list(context, null);
            out.writePage("Got the following transactions after transfer:", transactions);
            // endregion

            out.writeMessage("All done.");

        } catch (final NetLicensingException e) {
            out.writeException("Got NetLicensing exception:", e);
            exitCode = CODE_ERROR;
        } catch (final Exception e) {
            out.writeException("Got exception:", e);
            exitCode = CODE_ERROR;
        } finally {
            // Cleanup
            try {
                // delete APIKey in case it was used (exists)
                if (StringUtils.isNotBlank(context.getApiKey())) {
                    TokenService.delete(context, context.getApiKey());
                    context.setApiKey(null);
                }

                // delete test product with all its related items
                ProductService.delete(context, productNumber, true);

            } catch (final NetLicensingException e) {
                out.writeException("Got NetLicensing exception during cleanup:", e);
                exitCode = CODE_ERROR;
            } catch (final Exception e) {
                out.writeException("Got exception during cleanup:", e);
                exitCode = CODE_ERROR;
            }
        }

        if (exitCode == CODE_ERROR) {
            System.exit(exitCode);
        }
    }

    private static String numberWithPrefix(final String prefix, final String number) {
        return String.format("%s%s%s", DEMO_NUMBER_PREFIX, prefix, number);
    }

}
