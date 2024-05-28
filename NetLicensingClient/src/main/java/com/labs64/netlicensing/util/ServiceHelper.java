package com.labs64.netlicensing.util;

//import java.util.AbstractMap;
//import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.labs64.netlicensing.domain.entity.BaseEntity;
import com.labs64.netlicensing.domain.entity.Country;
import com.labs64.netlicensing.domain.entity.License;
import com.labs64.netlicensing.domain.entity.LicenseTemplate;
import com.labs64.netlicensing.domain.entity.Licensee;
import com.labs64.netlicensing.domain.entity.PaymentMethod;
import com.labs64.netlicensing.domain.entity.Product;
import com.labs64.netlicensing.domain.entity.ProductModule;
import com.labs64.netlicensing.domain.entity.Token;
import com.labs64.netlicensing.domain.entity.Transaction;
/*
import com.labs64.netlicensing.domain.entity.impl.CountryImpl;
import com.labs64.netlicensing.domain.entity.impl.LicenseImpl;
import com.labs64.netlicensing.domain.entity.impl.LicenseTemplateImpl;
import com.labs64.netlicensing.domain.entity.impl.LicenseeImpl;
import com.labs64.netlicensing.domain.entity.impl.PaymentMethodImpl;
import com.labs64.netlicensing.domain.entity.impl.ProductImpl;
import com.labs64.netlicensing.domain.entity.impl.ProductModuleImpl;
import com.labs64.netlicensing.domain.entity.impl.TokenImpl;
import com.labs64.netlicensing.domain.entity.impl.TransactionImpl;
*/
import com.labs64.netlicensing.domain.vo.Context;
import com.labs64.netlicensing.domain.vo.Page;
import com.labs64.netlicensing.exception.ConversionException;
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

public class ServiceHelper {

    private static String PAGE = "page";
    private static String FILTER_DELIMITER = ";";
    private static String FILTER_PAIR_DELIMITER = "=";

    /*
    // @formatter:off
    private static final Map<Class<?>, Visitable> entityTypeDispatch = Stream.of(
            new AbstractMap.SimpleEntry<>(Product.class,         new ProductImpl()),
            new AbstractMap.SimpleEntry<>(ProductModule.class,   new ProductModuleImpl()),
            new AbstractMap.SimpleEntry<>(LicenseTemplate.class, new LicenseTemplateImpl()),
            new AbstractMap.SimpleEntry<>(Licensee.class,        new LicenseeImpl()),
            new AbstractMap.SimpleEntry<>(License.class,         new LicenseImpl()),
            new AbstractMap.SimpleEntry<>(Transaction.class,     new TransactionImpl()),
            new AbstractMap.SimpleEntry<>(Token.class,           new TokenImpl()),
            new AbstractMap.SimpleEntry<>(PaymentMethod.class,   new PaymentMethodImpl()),
            new AbstractMap.SimpleEntry<>(Country.class,         new CountryImpl())
        ).collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));
    // @formatter:on

    private static Visitable getVisitableByType(final Class<?> entityClass) throws ConversionException {
        if (entityTypeDispatch.containsKey(entityClass)) {
            return entityTypeDispatch.get(entityClass);
        }
        throw new ConversionException("Provided type is not applicable");
    }

    public static class PageGetDispatch<T> extends Visitor {
        final Context context;
        final String filter;

        private int page = 0;
        private List<T> data = null;
        private boolean hasNext = false;

        public PageGetDispatch(final Context context, final String filter) {
            this.context = context;
            this.filter = filter;
        }

        public void setPage(final int page) {
            this.page = page;
        }

        public boolean hasNext() {
            return hasNext;
        }

        public List<T> getData() {
            return data;
        }

        @Override
        public void visitDefault(final Object object) throws ConversionException {
            throw new ConversionException("Paginated list operation is not availble for provided type");
        }

        private String getFilter() {
            String pageAndFilter = PAGE + FILTER_PAIR_DELIMITER + String.valueOf(page);
            if (StringUtils.isNotBlank(filter)) {
                pageAndFilter += FILTER_DELIMITER + filter;
            }
            return pageAndFilter;
        }

        private <U> void processPageResult(final Page<U> pageResult) {
            hasNext = pageResult.hasNext();
            // T is same as U by visitor design
            @SuppressWarnings("unchecked")
            final List<T> pageContent = (List<T>) pageResult.getContent();
            this.data = pageContent;
        }

        public void visit(final Product e) throws NetLicensingException {
            processPageResult(ProductService.list(context, getFilter()));
        }

        public void visit(final ProductModule e) throws NetLicensingException {
            processPageResult(ProductModuleService.list(context, getFilter()));
        }

        public void visit(final LicenseTemplate e) throws NetLicensingException {
            processPageResult(LicenseTemplateService.list(context, getFilter()));
        }

        public void visit(final Licensee e) throws NetLicensingException {
            processPageResult(LicenseeService.list(context, getFilter()));
        }

        public void visit(final License e) throws NetLicensingException {
            processPageResult(LicenseService.list(context, getFilter()));
        }

        public void visit(final Transaction e) throws NetLicensingException {
            processPageResult(TransactionService.list(context, getFilter()));
        }

        public void visit(final Token e) throws NetLicensingException {
            processPageResult(TokenService.list(context, getFilter()));
        }

        public void visit(final PaymentMethod e) throws NetLicensingException {
            processPageResult(PaymentMethodService.list(context, getFilter()));
        }

        public void visit(final Country e) throws NetLicensingException {
            processPageResult(UtilityService.listCountries(context, getFilter()));
        }
    }
    */

    public static class PageGetDispatchNoReflection {
        final Context context;
        final String filter;

        private int page = 0;
        private boolean hasNext = false;

        public PageGetDispatchNoReflection(final Context context, final String filter) {
            this.context = context;
            this.filter = filter;
        }

        public void setPage(final int page) {
            this.page = page;
        }

        public boolean hasNext() {
            return hasNext;
        }

        private String getFilter() {
            String pageAndFilter = PAGE + FILTER_PAIR_DELIMITER + String.valueOf(page);
            if (StringUtils.isNotBlank(filter)) {
                pageAndFilter += FILTER_DELIMITER + filter;
            }
            return pageAndFilter;
        }

        private <T> List<T> processPageResult(final Page<T> pageResult) {
            hasNext = pageResult.hasNext();
            return pageResult.getContent();
        }

        private List<? extends BaseEntity> visit(final Class<?> entityClass) throws NetLicensingException {
            if (entityClass == Product.class)
                return processPageResult(ProductService.list(context, getFilter()));
            else if (entityClass == ProductModule.class)
                return processPageResult(ProductModuleService.list(context, getFilter()));
            else if (entityClass == LicenseTemplate.class)
                return processPageResult(LicenseTemplateService.list(context, getFilter()));
            else if (entityClass == Licensee.class)
                return processPageResult(LicenseeService.list(context, getFilter()));
            else if (entityClass == License.class)
                return processPageResult(LicenseService.list(context, getFilter()));
            else if (entityClass == Transaction.class)
                return processPageResult(TransactionService.list(context, getFilter()));
            else if (entityClass == Token.class)
                return processPageResult(TokenService.list(context, getFilter()));
            else if (entityClass == PaymentMethod.class)
                return processPageResult(PaymentMethodService.list(context, getFilter()));
            else if (entityClass == Country.class)
                return processPageResult(UtilityService.listCountries(context, getFilter()));
            throw new ConversionException("Provided type is not applicable");
        }
    }

    /**
     * Returns all entities of specified type, automatically retrieving all pages.
     *
     * @param <T>
     *            derived automatically from entityClass
     * @param context
     *            determines the vendor on whose behalf the call is performed
     * @param filter
     *            reserved for the future use, must be omitted / set to NULL
     * @param entityClass
     *            class of requested NetLicensing Entity type, e.g. License.class
     * @return The list of all entities of the specified type
     * @throws NetLicensingException
     */
    public static <T> List<T> listAll(final Context context, final String filter, final Class<T> entityClass)
            throws NetLicensingException {
        //final Visitable dispatching = getVisitableByType(entityClass);
        //final PageGetDispatch<T> dispatcher = new PageGetDispatch<>(context, filter);

        final PageGetDispatchNoReflection dispatcher = new PageGetDispatchNoReflection(context, filter);

        final List<T> list = new ArrayList<>();
        int page = 0;
        try {
            do {
                dispatcher.setPage(page);
                @SuppressWarnings("unchecked")
                List<T> data = (List<T>) dispatcher.visit(entityClass);
                if (data != null) {
                    list.addAll(data);
                }
                //dispatching.accept(dispatcher);
                //if (dispatcher.getData() != null) {
                //    list.addAll(dispatcher.getData());
                //}
                ++page;
            } while (dispatcher.hasNext());
        } catch (final NetLicensingException e2) {
            throw e2;
        } catch (final Exception e3) {
            throw new ConversionException("Collecting entities failed", e3);
        }

        return list;
    }
}
