package com.gmail.breninsul.jd2.view;

import com.gmail.breninsul.jd2.pojo.Product;
import com.vaadin.external.org.slf4j.LoggerFactory;
import com.vaadin.flow.i18n.I18NProvider;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Log4j2
@Component
public class i18 implements I18NProvider {
    private static CachingProvider provider = Caching.getCachingProvider();
    private static CacheManager cacheManager = provider.getCacheManager();
    private static Cache<Locale, ResourceBundle> myCache;

    private static final String BUNDLE_PREFIX = "translate";
    private static final Locale LOCALE_BY = new Locale("by");
    private static final Locale LOCALE_EN = new Locale("en");
    private static final Locale LOCALE_RU = new Locale("ru");
    private static final int CACHE_EXPERATION_HOURS = 24;
    private List<Locale> locales = Collections
            .unmodifiableList(Arrays.asList(LOCALE_BY, LOCALE_EN, LOCALE_RU));
    private List<String> locales_lang = Collections
            .unmodifiableList(Arrays.asList(LOCALE_BY.getLanguage(), LOCALE_EN.getLanguage(), LOCALE_RU.getLanguage()));

    private static Cache createCache() {
        MutableConfiguration<Locale, ResourceBundle> config = new MutableConfiguration<>();
        config.setTypes(Locale.class, ResourceBundle.class)
                .setStoreByValue(false)
                .setStatisticsEnabled(true)
                .setExpiryPolicyFactory(FactoryBuilder.factoryOf(
                        new AccessedExpiryPolicy(new Duration(TimeUnit.HOURS, CACHE_EXPERATION_HOURS))));
        return cacheManager.createCache("InternatinalisationCache", config);
    }

    public List<Locale> getProvidedLocales() {
        return locales;
    }

    @PostConstruct
    public void init() {
        myCache = createCache();
    }

    private ResourceBundle readProperties(Locale locale) {
        if (myCache == null) {
            throw new NullPointerException("There is no cache for internatialisation!!!!");
        }
        if (!locales_lang.contains(locale.getLanguage()))
            locale = LOCALE_EN;

        if (myCache.get(locale) == null) {
            try {
                ResourceBundle propertiesBundle = ResourceBundle.getBundle(BUNDLE_PREFIX, locale);
                myCache.put(locale, propertiesBundle);
                return propertiesBundle;
            } catch (MissingResourceException e) {
                LoggerFactory.getLogger(i18.class.getName())
                        .warn("Missing resource", e);
            }
        }
        return myCache.get(locale);
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        if (key == null) {
            LoggerFactory.getLogger(i18.class.getName())
                    .warn("Got lang request for key with null value!");
            return "";
        }
        final ResourceBundle bundle = readProperties(locale);
        String value;
        try {
            value = bundle.getString(key);
        } catch (final MissingResourceException e) {
            LoggerFactory.getLogger(i18.class.getName())
                    .warn("Missing resource", e);
            return "!" + locale.getLanguage() + ": " + key;
        }
        if (params.length > 0) {
            value = MessageFormat.format(value, params);
        }
        return value;
    }
}

