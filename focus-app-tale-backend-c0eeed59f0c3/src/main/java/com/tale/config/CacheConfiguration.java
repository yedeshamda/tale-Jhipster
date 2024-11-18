package com.tale.config;

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

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
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
            createCache(cm, com.tale.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.tale.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.tale.domain.User.class.getName());
            createCache(cm, com.tale.domain.Authority.class.getName());
            createCache(cm, com.tale.domain.User.class.getName() + ".authorities");
            createCache(cm, com.tale.domain.NormalUser.class.getName());
            createCache(cm, com.tale.domain.NormalUser.class.getName() + ".responses");
            createCache(cm, com.tale.domain.NormalUser.class.getName() + ".notifications");
            createCache(cm, com.tale.domain.NormalUser.class.getName() + ".participatedSurveys");
            createCache(cm, com.tale.domain.AdminUser.class.getName());
            createCache(cm, com.tale.domain.AdminUser.class.getName() + ".campaigns");
            createCache(cm, com.tale.domain.Campaign.class.getName());
            createCache(cm, com.tale.domain.Campaign.class.getName() + ".surveys");
            createCache(cm, com.tale.domain.Campaign.class.getName() + ".analytics");
            createCache(cm, com.tale.domain.Campaign.class.getName() + ".users");
            createCache(cm, com.tale.domain.Survey.class.getName());
            createCache(cm, com.tale.domain.Survey.class.getName() + ".sections");
            createCache(cm, com.tale.domain.Survey.class.getName() + ".responseSurveys");
            createCache(cm, com.tale.domain.Survey.class.getName() + ".participants");
            createCache(cm, com.tale.domain.Section.class.getName());
            createCache(cm, com.tale.domain.Section.class.getName() + ".questions");
            createCache(cm, com.tale.domain.Section.class.getName() + ".analytics");
            createCache(cm, com.tale.domain.Question.class.getName());
            createCache(cm, com.tale.domain.Question.class.getName() + ".responses");
            createCache(cm, com.tale.domain.Responses.class.getName());
            createCache(cm, com.tale.domain.Responses.class.getName() + ".analytics");
            createCache(cm, com.tale.domain.OurDatabases.class.getName());
            createCache(cm, com.tale.domain.OurDatabases.class.getName() + ".surveyDatabases");
            createCache(cm, com.tale.domain.Notification.class.getName());
            createCache(cm, com.tale.domain.Analyticss.class.getName());
            createCache(cm, com.tale.domain.SurveyParticipant.class.getName());
            createCache(cm, com.tale.domain.SurveyParticipant.class.getName() + ".surveys");
            createCache(cm, com.tale.domain.NormalUser.class.getName() + ".focusGroups");
            createCache(cm, com.tale.domain.NormalUser.class.getName() + ".pointTransactions");
            createCache(cm, com.tale.domain.NormalUser.class.getName() + ".quizProgresses");
            createCache(cm, com.tale.domain.NormalUser.class.getName() + ".redemptionOptions");
            createCache(cm, com.tale.domain.FocusGroup.class.getName());
            createCache(cm, com.tale.domain.PointTransaction.class.getName());
            createCache(cm, com.tale.domain.Company.class.getName());
            createCache(cm, com.tale.domain.Company.class.getName() + ".createdSurveys");
            createCache(cm, com.tale.domain.Company.class.getName() + ".createdFocusGroups");
            createCache(cm, com.tale.domain.Company.class.getName() + ".surveyInsights");
            createCache(cm, com.tale.domain.Company.class.getName() + ".companyUsers");
            createCache(cm, com.tale.domain.Company.class.getName() + ".surveyRequirements");
            createCache(cm, com.tale.domain.CompanyUser.class.getName());
            createCache(cm, com.tale.domain.CompanyUser.class.getName() + ".userRoles");
            createCache(cm, com.tale.domain.SurveyInsight.class.getName());
            createCache(cm, com.tale.domain.UserCompanyRole.class.getName());
            createCache(cm, com.tale.domain.UserCompanyRole.class.getName() + ".companyUsers");
            createCache(cm, com.tale.domain.SurveyRequirement.class.getName());
            createCache(cm, com.tale.domain.QuizProgress.class.getName());
            createCache(cm, com.tale.domain.PointSpendOption.class.getName());
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
