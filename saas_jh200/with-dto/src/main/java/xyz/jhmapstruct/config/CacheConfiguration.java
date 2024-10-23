package xyz.jhmapstruct.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, xyz.jhmapstruct.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, xyz.jhmapstruct.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, xyz.jhmapstruct.domain.User.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.Authority.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.User.class.getName() + ".authorities");
            createCache(cm, xyz.jhmapstruct.domain.MasterTenant.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.Customer.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.Customer.class.getName() + ".orders");
            createCache(cm, xyz.jhmapstruct.domain.Product.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.Product.class.getName() + ".suppliers");
            createCache(cm, xyz.jhmapstruct.domain.Order.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.Order.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.Employee.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.Category.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.Review.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.Payment.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.Invoice.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.Shipment.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.Supplier.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.Supplier.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.CustomerVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.CustomerVi.class.getName() + ".orders");
            createCache(cm, xyz.jhmapstruct.domain.ProductVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.ProductVi.class.getName() + ".suppliers");
            createCache(cm, xyz.jhmapstruct.domain.OrderVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.OrderVi.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.EmployeeVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.CategoryVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.ReviewVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.PaymentVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.InvoiceVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.ShipmentVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.SupplierVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.SupplierVi.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.CustomerViVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.CustomerViVi.class.getName() + ".orders");
            createCache(cm, xyz.jhmapstruct.domain.ProductViVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.ProductViVi.class.getName() + ".suppliers");
            createCache(cm, xyz.jhmapstruct.domain.OrderViVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.OrderViVi.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.EmployeeViVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.CategoryViVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.ReviewViVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.PaymentViVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.InvoiceViVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.ShipmentViVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.SupplierViVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.SupplierViVi.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.CustomerMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.CustomerMi.class.getName() + ".orders");
            createCache(cm, xyz.jhmapstruct.domain.ProductMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.ProductMi.class.getName() + ".suppliers");
            createCache(cm, xyz.jhmapstruct.domain.OrderMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.OrderMi.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.EmployeeMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.CategoryMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.ReviewMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.PaymentMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.InvoiceMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.ShipmentMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.SupplierMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.SupplierMi.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.CustomerMiMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.CustomerMiMi.class.getName() + ".orders");
            createCache(cm, xyz.jhmapstruct.domain.ProductMiMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.ProductMiMi.class.getName() + ".suppliers");
            createCache(cm, xyz.jhmapstruct.domain.OrderMiMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.OrderMiMi.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.EmployeeMiMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.CategoryMiMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.ReviewMiMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.PaymentMiMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.InvoiceMiMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.ShipmentMiMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.SupplierMiMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.SupplierMiMi.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.CustomerAlpha.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.CustomerAlpha.class.getName() + ".orders");
            createCache(cm, xyz.jhmapstruct.domain.ProductAlpha.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.ProductAlpha.class.getName() + ".suppliers");
            createCache(cm, xyz.jhmapstruct.domain.OrderAlpha.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.OrderAlpha.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.EmployeeAlpha.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.CategoryAlpha.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.ReviewAlpha.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.PaymentAlpha.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.InvoiceAlpha.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.ShipmentAlpha.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.SupplierAlpha.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.SupplierAlpha.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.CustomerBeta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.CustomerBeta.class.getName() + ".orders");
            createCache(cm, xyz.jhmapstruct.domain.ProductBeta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.ProductBeta.class.getName() + ".suppliers");
            createCache(cm, xyz.jhmapstruct.domain.OrderBeta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.OrderBeta.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.EmployeeBeta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.CategoryBeta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.ReviewBeta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.PaymentBeta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.InvoiceBeta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.ShipmentBeta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.SupplierBeta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.SupplierBeta.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.CustomerGamma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.CustomerGamma.class.getName() + ".orders");
            createCache(cm, xyz.jhmapstruct.domain.ProductGamma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.ProductGamma.class.getName() + ".suppliers");
            createCache(cm, xyz.jhmapstruct.domain.OrderGamma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.OrderGamma.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.EmployeeGamma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.CategoryGamma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.ReviewGamma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.PaymentGamma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.InvoiceGamma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.ShipmentGamma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.SupplierGamma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.SupplierGamma.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.CustomerSigma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.CustomerSigma.class.getName() + ".orders");
            createCache(cm, xyz.jhmapstruct.domain.ProductSigma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.ProductSigma.class.getName() + ".suppliers");
            createCache(cm, xyz.jhmapstruct.domain.OrderSigma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.OrderSigma.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.EmployeeSigma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.CategorySigma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.ReviewSigma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.PaymentSigma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.InvoiceSigma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.ShipmentSigma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.SupplierSigma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.SupplierSigma.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.CustomerTheta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.CustomerTheta.class.getName() + ".orders");
            createCache(cm, xyz.jhmapstruct.domain.ProductTheta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.ProductTheta.class.getName() + ".suppliers");
            createCache(cm, xyz.jhmapstruct.domain.OrderTheta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.OrderTheta.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.EmployeeTheta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.CategoryTheta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.ReviewTheta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.PaymentTheta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.InvoiceTheta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.ShipmentTheta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.SupplierTheta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.SupplierTheta.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.NextCustomer.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextCustomer.class.getName() + ".orders");
            createCache(cm, xyz.jhmapstruct.domain.NextProduct.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextProduct.class.getName() + ".suppliers");
            createCache(cm, xyz.jhmapstruct.domain.NextOrder.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextOrder.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.NextEmployee.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextCategory.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextReview.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextPayment.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextInvoice.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextShipment.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextSupplier.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextSupplier.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.NextCustomerVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextCustomerVi.class.getName() + ".orders");
            createCache(cm, xyz.jhmapstruct.domain.NextProductVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextProductVi.class.getName() + ".suppliers");
            createCache(cm, xyz.jhmapstruct.domain.NextOrderVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextOrderVi.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.NextEmployeeVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextCategoryVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextReviewVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextPaymentVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextInvoiceVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextShipmentVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextSupplierVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextSupplierVi.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.NextCustomerViVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextCustomerViVi.class.getName() + ".orders");
            createCache(cm, xyz.jhmapstruct.domain.NextProductViVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextProductViVi.class.getName() + ".suppliers");
            createCache(cm, xyz.jhmapstruct.domain.NextOrderViVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextOrderViVi.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.NextEmployeeViVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextCategoryViVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextReviewViVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextPaymentViVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextInvoiceViVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextShipmentViVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextSupplierViVi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextSupplierViVi.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.NextCustomerMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextCustomerMi.class.getName() + ".orders");
            createCache(cm, xyz.jhmapstruct.domain.NextProductMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextProductMi.class.getName() + ".suppliers");
            createCache(cm, xyz.jhmapstruct.domain.NextOrderMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextOrderMi.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.NextEmployeeMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextCategoryMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextReviewMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextPaymentMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextInvoiceMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextShipmentMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextSupplierMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextSupplierMi.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.NextCustomerMiMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextCustomerMiMi.class.getName() + ".orders");
            createCache(cm, xyz.jhmapstruct.domain.NextProductMiMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextProductMiMi.class.getName() + ".suppliers");
            createCache(cm, xyz.jhmapstruct.domain.NextOrderMiMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextOrderMiMi.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.NextEmployeeMiMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextCategoryMiMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextReviewMiMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextPaymentMiMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextInvoiceMiMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextShipmentMiMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextSupplierMiMi.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextSupplierMiMi.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.NextCustomerAlpha.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextCustomerAlpha.class.getName() + ".orders");
            createCache(cm, xyz.jhmapstruct.domain.NextProductAlpha.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextProductAlpha.class.getName() + ".suppliers");
            createCache(cm, xyz.jhmapstruct.domain.NextOrderAlpha.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextOrderAlpha.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.NextEmployeeAlpha.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextCategoryAlpha.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextReviewAlpha.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextPaymentAlpha.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextInvoiceAlpha.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextShipmentAlpha.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextSupplierAlpha.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextSupplierAlpha.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.NextCustomerBeta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextCustomerBeta.class.getName() + ".orders");
            createCache(cm, xyz.jhmapstruct.domain.NextProductBeta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextProductBeta.class.getName() + ".suppliers");
            createCache(cm, xyz.jhmapstruct.domain.NextOrderBeta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextOrderBeta.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.NextEmployeeBeta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextCategoryBeta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextReviewBeta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextPaymentBeta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextInvoiceBeta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextShipmentBeta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextSupplierBeta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextSupplierBeta.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.NextCustomerGamma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextCustomerGamma.class.getName() + ".orders");
            createCache(cm, xyz.jhmapstruct.domain.NextProductGamma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextProductGamma.class.getName() + ".suppliers");
            createCache(cm, xyz.jhmapstruct.domain.NextOrderGamma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextOrderGamma.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.NextEmployeeGamma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextCategoryGamma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextReviewGamma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextPaymentGamma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextInvoiceGamma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextShipmentGamma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextSupplierGamma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextSupplierGamma.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.NextCustomerSigma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextCustomerSigma.class.getName() + ".orders");
            createCache(cm, xyz.jhmapstruct.domain.NextProductSigma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextProductSigma.class.getName() + ".suppliers");
            createCache(cm, xyz.jhmapstruct.domain.NextOrderSigma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextOrderSigma.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.NextEmployeeSigma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextCategorySigma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextReviewSigma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextPaymentSigma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextInvoiceSigma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextShipmentSigma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextSupplierSigma.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextSupplierSigma.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.NextCustomerTheta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextCustomerTheta.class.getName() + ".orders");
            createCache(cm, xyz.jhmapstruct.domain.NextProductTheta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextProductTheta.class.getName() + ".suppliers");
            createCache(cm, xyz.jhmapstruct.domain.NextOrderTheta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextOrderTheta.class.getName() + ".products");
            createCache(cm, xyz.jhmapstruct.domain.NextEmployeeTheta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextCategoryTheta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextReviewTheta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextPaymentTheta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextInvoiceTheta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextShipmentTheta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextSupplierTheta.class.getName());
            createCache(cm, xyz.jhmapstruct.domain.NextSupplierTheta.class.getName() + ".products");
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
