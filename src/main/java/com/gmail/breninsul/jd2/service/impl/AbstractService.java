package com.gmail.breninsul.jd2.service.impl;

import com.gmail.breninsul.jd2.dao.db.DAO;
import com.gmail.breninsul.jd2.dao.db.GenericClassResolver;
import com.gmail.breninsul.jd2.pojo.BaseEntity;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.annotation.CacheResult;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.spi.CachingProvider;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;
@Transactional
@Log4j2

public abstract class AbstractService<EntityType extends BaseEntity, DAOType extends DAO> implements DAO<EntityType> {
    protected static CachingProvider provider = Caching.getCachingProvider();
    protected Cache<Integer, EntityType> cache;
    protected static CacheManager cacheManager = provider.getCacheManager();
    protected static final int STANDART_CACHE_LIFETIME_HOURS = 2;
    @Autowired
    protected DAOType dao;
    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    EntityManager entityManager;
    protected Cache createCache(Class clazz, long hours, String name) {
        MutableConfiguration config = new MutableConfiguration<>();
        config.setTypes(Integer.class, clazz)
                .setStoreByValue(false)
                .setStatisticsEnabled(true)
                .setExpiryPolicyFactory(FactoryBuilder.factoryOf(
                        new AccessedExpiryPolicy(new Duration(TimeUnit.HOURS, hours))));
        return cacheManager.createCache(name, config);
    }

    protected void addToCache(EntityType type) {
        try {
            if (cache == null) {
                throw new NullPointerException();
            } else {
                try {
                    cache.put(type.getId(), type);
                } catch (NullPointerException e) {
                    log.error("Trying to put in cache not persisted entity" + type.getClass());
                }
            }
        } catch (NullPointerException e) {
            log.error("Cache has not been initialised for" + type.getClass());
        }
    }

    protected EntityType getFromCache(Integer id) {
        EntityType entity = cache.get(id);
        if (entity == null) {
            log.info("There is no entity with id=" + id + "in cache, returning null");
        } else {
            log.info("There is entity with id=" + id + " returning it");
        }
        return entity;
    }

    protected void removeFromCache(Integer id) {
        cache.remove(id);
        log.info("Removing entity with id=" + id + "from cache");
    }

    public EntityType save(EntityType entity) {
        EntityType e = (EntityType) dao.save(entity);
        addToCache(e);
        return e;
    }

    public EntityType getOne(Serializable id) {
        EntityType e = getFromCache((Integer) id);
        if (e == null) {
            e = (EntityType) dao.getOne(id);
            log.info("No entity in cache, trying to get it from db using DAO, id=" + id);
        }
        return e;
    }

    public List<EntityType> findAll() {
        return (List<EntityType>) dao.findAll();
    }

    public void delete(EntityType entity) {
        try {
            removeFromCache(entity.getId());
            dao.delete(entity);
        } catch (NullPointerException e) {
            log.info("Entity has'nt been persisted, nothing to delite", e);
        }
    }

    @Override
    public int count() {
        return dao.count();
    }

    @CacheResult
    public List<EntityType> findAll(PageRequest pageRequest) {
        return (List<EntityType>) dao.findAll(pageRequest);
    }

    @CacheResult
    public List<EntityType> findAll(String sortType, String field, int paginationSize, int page) {
        return (List<EntityType>) dao.findAll(sortType, field, paginationSize, page);
    }

    @CacheResult
    public List<EntityType> findAll(String sortType, int paginationSize, int page) {
        return (List<EntityType>) dao.findAll(sortType, paginationSize, page);

    }

    @Override
    public List<EntityType> findAll(int paginationSize, int page) {
        return (List<EntityType>) dao.findAll(paginationSize, page);
    }

}
