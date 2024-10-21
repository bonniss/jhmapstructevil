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
            createCache(cm, xyz.jhmapstruct.domain.MasterTenant.class.getName());
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
