package com.gmail.breninsul.jd2.service.impl;

import com.gmail.breninsul.jd2.dao.db.UserDAO;
import com.gmail.breninsul.jd2.pojo.Product;
import com.gmail.breninsul.jd2.pojo.User;
import com.gmail.breninsul.jd2.service.UserService;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.cache.Cache;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.List;
@Service
@Log4j2
@Transactional
public class UserServiceImpl extends AbstractService<User, UserDAO> implements UserService {
    private static final int CERTIFICATE_CACHE_LIFETIME_HOURS = 24;
    private static final String USER_CACHE_NAME = "userCache";
    private static final String PRODUCTS_LIST_CACHE_NAME = "userProductsListCache";
    private static final String USER_LIST_CACHE_NAME = "userListCache";
    private Cache<Integer, List<Product>> listCache;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    EntityManager entityManager;
    @PostConstruct
    public void init() {
        cache = createCache(User.class, STANDART_CACHE_LIFETIME_HOURS, USER_CACHE_NAME);
        listCache = createCache(List.class, STANDART_CACHE_LIFETIME_HOURS, PRODUCTS_LIST_CACHE_NAME);
    }

    protected void addToProdListCache(List<Product> type, int hash) {
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

    protected List<Product> getProdListFromCache(Integer hash) {
        List<Product> entity = listCache.get(hash);
        if (entity == null) {
            log.info("There is no list with hash=" + hash + "in cache, returning null");
        } else {
            log.info("There is list with hash=" + hash + " returning it");
        }
        return entity;
    }

    protected void removeProdListFromCache() {
        listCache.removeAll();
        log.info("Removing lists from cache");
    }


    @Override
    public List<Product> getProductsPagedAndSorted(User user, String sortType, String field, int paginationSize, int page) {
        int hash = user.getId() * 32 + sortType.hashCode() + field.hashCode() + paginationSize * 21 + page * 72;
        List<Product> entity = listCache.get(hash);
        if (entity != null) {
            return entity;
        }
        return (List<Product>) dao.getProductsPagedAndSorted(user, sortType, field, paginationSize, page);
    }

    @Override
    public List<Product> getProductsPagedAndSorted(User user, String sortType, int paginationSize, int page) {
        int hash = user.getId() * 13 + sortType.hashCode() + paginationSize * 22 + page * 71;
        List<Product> entity = listCache.get(hash);
        if (entity != null) {
            return entity;
        }
        return (List<Product>) dao.getProductsPagedAndSorted(user, sortType, paginationSize, page);
    }

    @Override
    public List<Product> getProductsPagedAndSorted(User user, int paginationSize, int page) {
        int hash = user.getId() * 33 + paginationSize * 24 + page * 37;
        List<Product> entity = listCache.get(hash);
        if (entity != null) {
            return entity;
        }
        return (List<Product>) dao.getProductsPagedAndSorted(user, paginationSize, page);
    }

    @Override
    public List<Product> getProductsByNamePagedAndSorted(User user, String name, String sortType, String field, int paginationSize, int page) {
        int hash = user.getId() * 32 + name.hashCode() + sortType.hashCode() + field.hashCode() + paginationSize * 21 + page * 72;
        List<Product> entity = listCache.get(hash);
        if (entity != null) {
            return entity;
        }
        return (List<Product>) dao.getProductsByNamePagedAndSorted(user,name, sortType, field, paginationSize, page);
    }


    @Override
    public List<Product> getProductsByNamePagedAndSorted(User user, String name, String sortType, int paginationSize, int page) {
        int hash = user.getId() * 12 + name.hashCode() + sortType.hashCode() + hashCode() + paginationSize * 51 + page * 42;
        List<Product> entity = listCache.get(hash);
        if (entity != null) {
            return entity;
        }
        return (List<Product>) dao.getProductsByNamePagedAndSorted(user,name, sortType, paginationSize, page);
    }


    @Override
    public List<Product> getProductsByNamePagedAndSorted(User user, String name, int paginationSize, int page) {
        int hash = user.getId() * 35 + name.hashCode() + paginationSize * 54 + page * 32;
        List<Product> entity = listCache.get(hash);
        if (entity != null) {
            return entity;
        }
        return (List<Product>) dao.getProductsByNamePagedAndSorted(user,name,  paginationSize, page);
    }

    @Override
    public User getUserByName(String name) {
       return dao.getUserByName(name);
    }
     @Override
    public User save(User user) {
        log.info("saving user id="+user.getId());
        if (user.getId()<2) {
            user.setPass(passwordEncoder.encode(user.getPass()));
        }
        dao.save(user);

        return user;
    }

}
