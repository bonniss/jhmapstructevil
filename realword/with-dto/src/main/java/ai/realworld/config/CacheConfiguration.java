package ai.realworld.config;

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
            createCache(cm, ai.realworld.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, ai.realworld.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, ai.realworld.domain.User.class.getName());
            createCache(cm, ai.realworld.domain.Authority.class.getName());
            createCache(cm, ai.realworld.domain.User.class.getName() + ".authorities");
            createCache(cm, ai.realworld.domain.HandCraft.class.getName());
            createCache(cm, ai.realworld.domain.AlAlexType.class.getName());
            createCache(cm, ai.realworld.domain.AlAlexType.class.getName() + ".asSuppliers");
            createCache(cm, ai.realworld.domain.AlAlexType.class.getName() + ".asCustomers");
            createCache(cm, ai.realworld.domain.AlAlexType.class.getName() + ".agencies");
            createCache(cm, ai.realworld.domain.AlBetonamuRelation.class.getName());
            createCache(cm, ai.realworld.domain.AlBetonamuRelation.class.getName() + ".discounts");
            createCache(cm, ai.realworld.domain.AlApple.class.getName());
            createCache(cm, ai.realworld.domain.AlApple.class.getName() + ".agents");
            createCache(cm, ai.realworld.domain.Rihanna.class.getName());
            createCache(cm, ai.realworld.domain.Rihanna.class.getName() + ".agentRoles");
            createCache(cm, ai.realworld.domain.EdSheeran.class.getName());
            createCache(cm, ai.realworld.domain.EdSheeran.class.getName() + ".agentRoles");
            createCache(cm, ai.realworld.domain.AlPacino.class.getName());
            createCache(cm, ai.realworld.domain.AlPacinoAndreiRightHand.class.getName());
            createCache(cm, ai.realworld.domain.AlActiso.class.getName());
            createCache(cm, ai.realworld.domain.AlGore.class.getName());
            createCache(cm, ai.realworld.domain.AlGore.class.getName() + ".conditions");
            createCache(cm, ai.realworld.domain.AlGoreCondition.class.getName());
            createCache(cm, ai.realworld.domain.AlLeandro.class.getName());
            createCache(cm, ai.realworld.domain.AlLeandro.class.getName() + ".awards");
            createCache(cm, ai.realworld.domain.AlDesire.class.getName());
            createCache(cm, ai.realworld.domain.AlLeandroPlayingTime.class.getName());
            createCache(cm, ai.realworld.domain.AndreiRightHand.class.getName());
            createCache(cm, ai.realworld.domain.AntonioBanderas.class.getName());
            createCache(cm, ai.realworld.domain.AntonioBanderas.class.getName() + ".children");
            createCache(cm, ai.realworld.domain.HexChar.class.getName());
            createCache(cm, ai.realworld.domain.HashRoss.class.getName());
            createCache(cm, ai.realworld.domain.Initium.class.getName());
            createCache(cm, ai.realworld.domain.Magharetti.class.getName());
            createCache(cm, ai.realworld.domain.Metaverse.class.getName());
            createCache(cm, ai.realworld.domain.Metaverse.class.getName() + ".alProPros");
            createCache(cm, ai.realworld.domain.Metaverse.class.getName() + ".alProties");
            createCache(cm, ai.realworld.domain.PamelaLouis.class.getName());
            createCache(cm, ai.realworld.domain.SicilyUmeto.class.getName());
            createCache(cm, ai.realworld.domain.SaisanCog.class.getName());
            createCache(cm, ai.realworld.domain.AlLexFerg.class.getName());
            createCache(cm, ai.realworld.domain.AlLexFerg.class.getName() + ".tags");
            createCache(cm, ai.realworld.domain.AlCatalina.class.getName());
            createCache(cm, ai.realworld.domain.AlCatalina.class.getName() + ".children");
            createCache(cm, ai.realworld.domain.AlBestTooth.class.getName());
            createCache(cm, ai.realworld.domain.AlBestTooth.class.getName() + ".articles");
            createCache(cm, ai.realworld.domain.AlMenity.class.getName());
            createCache(cm, ai.realworld.domain.AlMenity.class.getName() + ".propertyProfiles");
            createCache(cm, ai.realworld.domain.AlPedroTax.class.getName());
            createCache(cm, ai.realworld.domain.AlPedroTax.class.getName() + ".attributeTerms");
            createCache(cm, ai.realworld.domain.AlPounder.class.getName());
            createCache(cm, ai.realworld.domain.AlPowerShell.class.getName());
            createCache(cm, ai.realworld.domain.AlPyuDjibril.class.getName());
            createCache(cm, ai.realworld.domain.AlPyuThomasWayne.class.getName());
            createCache(cm, ai.realworld.domain.AlPyuJoker.class.getName());
            createCache(cm, ai.realworld.domain.AlPyuJoker.class.getName() + ".properties");
            createCache(cm, ai.realworld.domain.AlLadyGaga.class.getName());
            createCache(cm, ai.realworld.domain.AlProPro.class.getName());
            createCache(cm, ai.realworld.domain.AlProPro.class.getName() + ".amenities");
            createCache(cm, ai.realworld.domain.AlProPro.class.getName() + ".images");
            createCache(cm, ai.realworld.domain.AlProPro.class.getName() + ".children");
            createCache(cm, ai.realworld.domain.AlProty.class.getName());
            createCache(cm, ai.realworld.domain.AlProty.class.getName() + ".images");
            createCache(cm, ai.realworld.domain.AlProty.class.getName() + ".children");
            createCache(cm, ai.realworld.domain.AlProty.class.getName() + ".bookings");
            createCache(cm, ai.realworld.domain.AlSherMale.class.getName());
            createCache(cm, ai.realworld.domain.AlInquiry.class.getName());
            createCache(cm, ai.realworld.domain.AlGoogleMeet.class.getName());
            createCache(cm, ai.realworld.domain.AlMemTier.class.getName());
            createCache(cm, ai.realworld.domain.AlPacinoPointHistory.class.getName());
            createCache(cm, ai.realworld.domain.AlVueVue.class.getName());
            createCache(cm, ai.realworld.domain.AlVueVue.class.getName() + ".conditions");
            createCache(cm, ai.realworld.domain.AlVueVueCondition.class.getName());
            createCache(cm, ai.realworld.domain.AlPacinoVoucher.class.getName());
            createCache(cm, ai.realworld.domain.AlVueVueUsage.class.getName());
            createCache(cm, ai.realworld.domain.AlVueVueUsage.class.getName() + ".vouchers");
            createCache(cm, ai.realworld.domain.AlVueVueUsage.class.getName() + ".customers");
            createCache(cm, ai.realworld.domain.AppMessageTemplate.class.getName());
            createCache(cm, ai.realworld.domain.AppZnsTemplate.class.getName());
            createCache(cm, ai.realworld.domain.OlAlmantinoMilo.class.getName());
            createCache(cm, ai.realworld.domain.OlAlmantinoMilo.class.getName() + ".applications");
            createCache(cm, ai.realworld.domain.JohnLennon.class.getName());
            createCache(cm, ai.realworld.domain.OlMaster.class.getName());
            createCache(cm, ai.realworld.domain.OlMaster.class.getName() + ".applications");
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
