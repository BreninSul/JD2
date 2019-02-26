package com.gmail.breninsul.jd2.service.impl;

import com.gmail.breninsul.jd2.dao.db.ProductDAO;
import com.gmail.breninsul.jd2.pojo.Certificate;
import com.gmail.breninsul.jd2.pojo.Product;
import com.gmail.breninsul.jd2.pojo.User;
import com.gmail.breninsul.jd2.service.ProductService;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.annotation.CacheResult;
import javax.cache.spi.CachingProvider;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.List;

@Service
@Log4j2
@Transactional
public class ProductServiceImpl extends AbstractService<Product, ProductDAO> implements ProductService {
    public static final String PRODUCTS_CACHE_NAME = "productsCache";
    public static final String CERTIFICATES_LIST_CACHE_NAME = "certificatesListCache";
    public static final int STANDART_CACHE_LIFETIME_HOURS = 2;
    private Cache<Integer, List<Certificate>> listCache;
    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    EntityManager entityManager;
    protected void addToListCache(List<Certificate> type, int hash) {
        try {
            if (listCache == null) {
                throw new NullPointerException();
            } else {
                try {
                    listCache.put(hash, type);
                } catch (NullPointerException e) {
                    log.error("Trying to put in cache list" + type.getClass());
                }
            }
        } catch (NullPointerException e) {
            log.error("Cache has not been initialised for" + type.getClass());
        }
    }

    protected List<Certificate> getListFromCache(Integer hash) {
        List<Certificate> entity = listCache.get(hash);
        if (entity == null) {
            log.info("There is no list with hash=" + hash + "in cache, returning null");
        } else {
            log.info("There is list with hash=" + hash + " returning it");
        }
        return entity;
    }

    protected void removeListFromCache(Integer hash) {
        listCache.removeAll();
        log.info("Removing lists from cache");
    }

    @PostConstruct
    public void init() {
        cache = createCache(Product.class, STANDART_CACHE_LIFETIME_HOURS, PRODUCTS_CACHE_NAME);
        listCache = createCache(List.class, STANDART_CACHE_LIFETIME_HOURS, CERTIFICATES_LIST_CACHE_NAME);
    }

    @Override
    public List<Certificate> getCertificatesPagedAndSorted(Product product, String sortType, String field, int paginationSize, int page) {
        int hash = product.getId() * 35 + sortType.hashCode() + field.hashCode() + paginationSize * 36 + page * 11;
        List<Certificate> entity = listCache.get(hash);
        if (entity != null) {
            return entity;
        }
        return (List<Certificate>) dao.getCertificatesPagedAndSorted(product, sortType, field, paginationSize, page);
    }

    @Override
    public List<Certificate> getCertificatesPagedAndSorted(Product product, String sortType, int paginationSize, int page) {
        int hash = product.getId() * 26 + sortType.hashCode() + paginationSize * 32 + page * 43;
        List<Certificate> entity = listCache.get(hash);
        if (entity != null) {
            return entity;
        }
        return (List<Certificate>) dao.getCertificatesPagedAndSorted(product, sortType, paginationSize, page);
    }

    @Override
    public List<Certificate> getCertificatesPagedAndSorted(Product product, int paginationSize, int page) {
        int hash = product.getId() * 15 + paginationSize * 46 + page * 21;
        List<Certificate> entity = listCache.get(hash);
        if (entity != null) {
            return entity;
        }
        return (List<Certificate>) dao.getCertificatesPagedAndSorted(product, paginationSize, page);
    }

    @Override
    public int countProductsByUser(User user) {
      return dao.countProductsByUser(user);
    }

    @Override
    public int countProductsByUserId(int id) {
        return dao.countProductsByUserId(id);
    }
}
