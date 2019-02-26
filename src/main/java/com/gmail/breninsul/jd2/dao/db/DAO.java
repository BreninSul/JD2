package com.gmail.breninsul.jd2.dao.db;

import com.gmail.breninsul.jd2.pojo.BaseEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.cache.Cache;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.AccessedExpiryPolicy;
import javax.cache.expiry.Duration;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

public interface DAO<EntityType extends BaseEntity> {
    public static final String STANDARD_SORT_FIELD = "updatedDate";

    EntityType save(EntityType type);

    EntityType getOne(Serializable id);

    List<EntityType> findAll();

    void delete(EntityType type);

    List<EntityType> findAll(PageRequest pageRequest);

    List<EntityType> findAll(String sortType, String field, int paginationSize, int page);

    List<EntityType> findAll(String sortType, int paginationSize, int page);

    List<EntityType> findAll(int paginationSize, int page);

    int count();
}
