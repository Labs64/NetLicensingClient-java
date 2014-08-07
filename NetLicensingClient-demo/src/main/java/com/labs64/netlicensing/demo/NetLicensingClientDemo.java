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

import org.apache.commons.lang3.StringUtils;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.labs64.netlicensing.domain.Constants;
import com.labs64.netlicensing.domain.entity.License;
import com.labs64.netlicensing.domain.entity.LicenseTemplate;
import com.labs64.netlicensing.domain.entity.Licensee;
import com.labs64.netlicensing.domain.entity.PaymentMethod;
import com.labs64.netlicensing.domain.entity.Product;
import com.labs64.netlicensing.domain.entity.ProductModule;
import com.labs64.netlicensing.domain.entity.Token;
import com.labs64.netlicensing.domain.entity.impl.LicenseImpl;
import com.labs64.netlicensing.domain.entity.impl.LicenseTemplateImpl;
import com.labs64.netlicensing.domain.entity.impl.LicenseeImpl;
import com.labs64.netlicensing.domain.entity.impl.ProductImpl;
import com.labs64.netlicensing.domain.entity.impl.ProductModuleImpl;
import com.labs64.netlicensing.domain.entity.impl.TokenImpl;
import com.labs64.netlicensing.domain.entity.impl.ValidationResult;
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Currency;
import com.labs64.netlicensing.domain.vo.LicenseType;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.domain.vo.SecurityMode;
import com.labs64.netlicensing.domain.vo.TokenType;
import com.labs64.netlicensing.exception.BaseCheckedException;
import com.labs64.netlicensing.service.LicenseService;
import com.labs64.netlicensing.service.LicenseTemplateService;
import com.labs64.netlicensing.service.LicenseeService;
import com.labs64.netlicensing.service.PaymentMethodService;
import com.labs64.netlicensing.service.ProductModuleService;
import com.labs64.netlicensing.service.ProductService;
import com.labs64.netlicensing.service.TokenService;
import com.labs64.netlicensing.service.UtilityService;

public class NetLicensingClientDemo {

    private final static String DEMO_PRODUCT_NUMBER = "P001-DEMO";
    private final static String DEMO_PRODUCT_MODULE_NUMBER = "PM001-DEMO";
    private final static String DEMO_LICENSE_TEMPLATE_NUMBER = "LT001-DEMO";
    private final static String DEMO_LICENSEE_NUMBER = "L001-DEMO";
    private final static String DEMO_LICENSE_NUMBER = "LC001-DEMO";

    public static void main(String[] args) {

        // configure J.U.L. to Slf4j bridge for Jersey
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        final Context context = new Context();
        context.setBaseUrl("https://netlicensing.labs64.com/core/v2/rest");
        context.setSecurityMode(SecurityMode.BASIC_AUTHENTICATION);
        context.setUsername("demo");
        context.setPassword("demo");

        final ConsoleWriter out = new ConsoleWriter();

        try {

            // region ********* Lists

            final Page<String> licenseTypes = UtilityService.listLicenseTypes(context);
            out.writePage("License Types:", licenseTypes);

            final Page<String> licensingModels = UtilityService.listLicensingModels(context);
            out.writePage("Licensing Models:", licensingModels);

            // endregion

            // region ********* Product

            final Product newProduct = new ProductImpl();
            newProduct.setNumber(DEMO_PRODUCT_NUMBER);
            newProduct.setName("Demo product");
            Product product = ProductService.create(context, newProduct);
            out.writeObject("Added product:", product);

            product = ProductService.get(context, DEMO_PRODUCT_NUMBER);
            out.writeObject("Got product:", product);

            Page<Product> products = ProductService.list(context);
            out.writePage("Got the following products:", products);

            final Product updateProduct = new ProductImpl();
            updateProduct.addProperty("Updated property name", "Updated value");
            product = ProductService.update(context, DEMO_PRODUCT_NUMBER, updateProduct);
            out.writeObject("Updated product:", product);

            ProductService.delete(context, DEMO_PRODUCT_NUMBER, true);
            out.writeMessage("Deleted Product!");

            products = ProductService.list(context);
            out.writePage("Got the following Products:", products);

            product = ProductService.create(context, newProduct);
            out.writeObject("Added product again:", product);

            products = ProductService.list(context);
            out.writePage("Got the following Products:", products);

            // endregion

            // region ********* ProductModule

            final ProductModule newProductModule = new ProductModuleImpl();
            newProductModule.setNumber(DEMO_PRODUCT_MODULE_NUMBER);
            newProductModule.setName("Demo product module");
            newProductModule.setLicensingModel(Constants.LicensingModel.TimeLimitedEvaluation.NAME);
            ProductModule productModule = ProductModuleService.create(context, DEMO_PRODUCT_NUMBER, newProductModule);
            out.writeObject("Added product module:", productModule);

            productModule = ProductModuleService.get(context, DEMO_PRODUCT_MODULE_NUMBER);
            out.writeObject("Got product module:", productModule);

            Page<ProductModule> productModules = ProductModuleService.list(context);
            out.writePage("Got the following ProductModules:", productModules);

            final ProductModule updateProductModule = new ProductModuleImpl();
            updateProductModule.addProperty("Updated property name", "Updated property value");
            productModule = ProductModuleService.update(context, DEMO_PRODUCT_MODULE_NUMBER, updateProductModule);
            out.writeObject("Updated product module:", productModule);

            ProductModuleService.delete(context, DEMO_PRODUCT_MODULE_NUMBER, true);
            out.writeMessage("Deleted ProductModule!");

            productModules = ProductModuleService.list(context);
            out.writePage("Got the following ProductModules:", productModules);

            productModule = ProductModuleService.create(context, DEMO_PRODUCT_NUMBER, newProductModule);
            out.writeObject("Added product module again:", productModule);

            productModules = ProductModuleService.list(context);
            out.writePage("Got the following ProductModules:", productModules);

            // endregion

            // region ********* LicenseTemplate

            final LicenseTemplate newLicenseTemplate = new LicenseTemplateImpl();
            newLicenseTemplate.setNumber(DEMO_LICENSE_TEMPLATE_NUMBER);
            newLicenseTemplate.setName("Demo Evaluation Period");
            newLicenseTemplate.setLicenseType(LicenseType.FEATURE);
            newLicenseTemplate.setPrice(new BigDecimal(12.5));
            newLicenseTemplate.setCurrency(Currency.EUR);
            newLicenseTemplate.setAutomatic(false);
            newLicenseTemplate.setHidden(false);
            out.writeObject("Adding license template:", newLicenseTemplate);
            LicenseTemplate licenseTemplate = LicenseTemplateService.create(context, DEMO_PRODUCT_MODULE_NUMBER,
                    newLicenseTemplate);
            out.writeObject("Added license template:", licenseTemplate);

            licenseTemplate = LicenseTemplateService.get(context, DEMO_LICENSE_TEMPLATE_NUMBER);
            out.writeObject("Got licenseTemplate:", licenseTemplate);

            Page<LicenseTemplate> licenseTemplates = LicenseTemplateService.list(context);
            out.writePage("Got the following license templates:", licenseTemplates);

            final LicenseTemplate updateLicenseTemplate = new LicenseTemplateImpl();
            updateLicenseTemplate.addProperty("Updated property name", "Updated value");
            licenseTemplate = LicenseTemplateService.update(context, DEMO_LICENSE_TEMPLATE_NUMBER,
                    updateLicenseTemplate);
            out.writeObject("Updated license template:", licenseTemplate);

            LicenseTemplateService.delete(context, DEMO_LICENSE_TEMPLATE_NUMBER, true);
            out.writeMessage("Deleted LicenseTemplate!");

            licenseTemplates = LicenseTemplateService.list(context);
            out.writePage("Got the following license templates:", licenseTemplates);

            licenseTemplate = LicenseTemplateService.create(context, DEMO_PRODUCT_MODULE_NUMBER, newLicenseTemplate);
            out.writeObject("Added license template again:", licenseTemplate);

            licenseTemplates = LicenseTemplateService.list(context);
            out.writePage("Got the following license templates:", licenseTemplates);

            // endregion

            // region ********* Licensee

            final Licensee newLicensee = new LicenseeImpl();
            newLicensee.setNumber(DEMO_LICENSEE_NUMBER);
            Licensee licensee = LicenseeService.create(context, DEMO_PRODUCT_NUMBER, newLicensee);
            out.writeObject("Added licensee:", licensee);

            Page<Licensee> licensees = LicenseeService.list(context);
            out.writePage("Got the following licensees:", licensees);

            LicenseeService.delete(context, DEMO_LICENSEE_NUMBER, true);
            out.writeMessage("Deleted licensee!");

            licensees = LicenseeService.list(context);
            out.writePage("Got the following licensees after delete:", licensees);

            licensee = LicenseeService.create(context, DEMO_PRODUCT_NUMBER, newLicensee);
            out.writeObject("Added licensee again:", licensee);

            licensee = LicenseeService.get(context, DEMO_LICENSEE_NUMBER);
            out.writeObject("Got licensee:", licensee);

            final Licensee updateLicensee = new LicenseeImpl();
            updateLicensee.addProperty("Updated property name", "Updated value");
            licensee = LicenseeService.update(context, DEMO_LICENSEE_NUMBER, updateLicensee);
            out.writeObject("Updated licensee:", licensee);

            licensees = LicenseeService.list(context);
            out.writePage("Got the following licensees:", licensees);

            // endregion

            // region ********* License

            final License newLicense = new LicenseImpl();
            newLicense.setNumber(DEMO_LICENSE_NUMBER);
            License license = LicenseService.create(context, DEMO_LICENSEE_NUMBER, DEMO_LICENSE_TEMPLATE_NUMBER, null,
                    newLicense);
            out.writeObject("Added license:", license);

            Page<License> licenses = LicenseService.list(context);
            out.writePage("Got the following license templates:", licenses);

            LicenseService.delete(context, DEMO_LICENSE_NUMBER, true);
            out.writeMessage("Deleted license!");

            licenses = LicenseService.list(context);
            out.writePage("Got the following license templates:", licenses);

            license = LicenseService.create(context, DEMO_LICENSEE_NUMBER, DEMO_LICENSE_TEMPLATE_NUMBER, null,
                    newLicense);
            out.writeObject("Added license again:", license);

            license = LicenseService.get(context, DEMO_LICENSE_NUMBER);
            out.writeObject("Got license:", license);

            final License updateLicense = new LicenseImpl();
            updateLicense.addProperty("Updated property name", "Updated value");
            license = LicenseService.update(context, DEMO_LICENSE_NUMBER, null, updateLicense);
            out.writeObject("Updated license:", license);

            // endregion

            // region ********* PaymentMethod

            final Page<PaymentMethod> paymentMethods = PaymentMethodService.list(context);
            out.writePage("Got the following payment methods:", paymentMethods);

            // endregion

            // region ********* Token

            final Token newToken = new TokenImpl();
            newToken.setTokenType(TokenType.APIKEY);
            final Token apiKey = TokenService.create(context, newToken);
            out.writeObject("Created API Key:", apiKey);

            context.setApiKey(apiKey.getNumber());
            newToken.setTokenType(TokenType.SHOP);
            newToken.addProperty(Constants.Licensee.LICENSEE_NUMBER, DEMO_LICENSEE_NUMBER);
            context.setSecurityMode(SecurityMode.APIKEY_IDENTIFICATION);
            final Token shopToken = TokenService.create(context, newToken);
            context.setSecurityMode(SecurityMode.BASIC_AUTHENTICATION);
            out.writeObject("Got the following shop token:", shopToken);

            Page<Token> tokens = TokenService.list(context, TokenType.SHOP);
            out.writePage("Got the following shop tokens:", tokens);

            TokenService.delete(context, shopToken.getNumber());
            out.writeMessage("Deleted shop token!");

            tokens = TokenService.list(context, TokenType.SHOP);
            out.writePage("Got the following shop tokens after delete:", tokens);

            // endregion

            // region ********* Validate

            ValidationResult validationResult = LicenseeService.validate(context, DEMO_LICENSEE_NUMBER,
                    DEMO_PRODUCT_NUMBER, null);
            out.writeObject("Validation result for created licensee:", validationResult);

            context.setSecurityMode(SecurityMode.APIKEY_IDENTIFICATION);
            validationResult = LicenseeService.validate(context, DEMO_LICENSEE_NUMBER, DEMO_PRODUCT_NUMBER, null);
            context.setSecurityMode(SecurityMode.BASIC_AUTHENTICATION);
            out.writeObject("Validation repeated with API Key:", validationResult);

            // endregion

            out.writeMessage("All done.");

        } catch (BaseCheckedException e) {
            out.writeException("Got NetLicensing exception:", e);
        } catch (Exception e) {
            out.writeException("Got exception:", e);
        } finally {
            try {
                // Cleanup:

                // delete API key in case it was used (exists)
                if (!StringUtils.isEmpty(context.getApiKey())) {
                    TokenService.delete(context, context.getApiKey());
                    context.setApiKey(null);
                }

                // delete test product with all its related items
                ProductService.delete(context, DEMO_PRODUCT_NUMBER, true);

            } catch (BaseCheckedException e) {
                out.writeException("Got NetLicensing exception during cleanup:", e);
            } catch (Exception e) {
                out.writeException("Got exception during cleanup:", e);
            }
        }
    }

}
