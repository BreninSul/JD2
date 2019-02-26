package com.gmail.breninsul.jd2.dao.db.hiber;


import com.gmail.breninsul.jd2.dao.db.DAO;
import com.gmail.breninsul.jd2.dao.db.GenericClassResolver;
import com.gmail.breninsul.jd2.pojo.BaseEntity;
import lombok.Data;
import lombok.extern.java.Log;
import org.hibernate.Hibernate;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Uses as dao singletone fabric ass well
 *
 * @param <EntityType>
 * @param <DAOType>
 */
@Log
@Data
@Repository
public abstract class AbstarctHiberDAO<EntityType extends BaseEntity> extends GenericClassResolver<EntityType> implements DAO<EntityType>{
    @PersistenceContext
    EntityManager em;


    public EntityType save(EntityType entity) {
        if (entity.getId() > 0) {
            update(entity);
        } else {
            entity = saveNew(entity);
        }
        return entity;
    }

    private EntityType saveNew(EntityType entity) {
        log.info("Saving " + entity.getClass().getSimpleName() + " " + entity);
        em.persist(entity);
        em.flush();
        log.info("Saved " + entity.getClass().getSimpleName() + " " + entity);
        return entity;
    }

    public EntityType getOne(Serializable id) {
        Class clazz = getEntityGenericClass();
        log.info("Trying getOne " + clazz.getSimpleName() + " " + id);
        EntityType entity = null;
        entity = (EntityType) em.find(clazz, id);
        log.info("Got " + entity);
        return entity;
    }

    public List<EntityType> findAll() {
        Class clazz = getEntityGenericClass();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<EntityType> criteria = cb.createQuery(clazz);
        Root<EntityType> root = criteria.from(clazz);
        List<EntityType> entitys = new ArrayList<>();
        criteria.select(root);
        return em.createQuery(criteria).getResultList();
    }

    public List<EntityType> findAll(PageRequest pageRequest) {
        String[] sort =pageRequest.getSort().toString().split(": ");
        String sortType= sort[1];
        String field=sort[0];
        int paginationSize=pageRequest.getPageSize();
        int page=pageRequest.getPageNumber();
       return findAll(sortType,field,paginationSize,page);
    }


    public List<EntityType> findAll(String sortType, String field, int paginationSize, int page) {
        Class clazz = getEntityGenericClass();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<EntityType> criteria = cb.createQuery(clazz);
        Root<EntityType> root = criteria.from(clazz);
        if ("DESC".equals(sortType)) {
            criteria.select(root).orderBy(cb.desc(root.get(field)));
        } else {
            if ("ASC".equals(sortType)) {
                criteria.select(root).orderBy(cb.asc(root.get(field)));
            } else {
                criteria.select(root);
            }
        }
        TypedQuery<EntityType> query =em.createQuery(criteria);
        query.setFirstResult(page * paginationSize-paginationSize);
        query.setMaxResults(paginationSize);
        return  query.getResultList();
    }

    /**
     * @param sortType use "DESC" or "ASC"
     */
    public List<EntityType> findAll(String sortType, int paginationSize, int page) {
        return findAll(sortType, STANDARD_SORT_FIELD, paginationSize, page);
    }

    public List<EntityType> findAll(int paginationSize, int page) {
        return findAll(null, STANDARD_SORT_FIELD, paginationSize, page);
    }
    /**
     * @param id
     * @return proxy of our entity, or entity itselt, if it has been initialized earlier
     */
    @Deprecated
    public EntityType getLazy(Serializable id) {
        Class clazz = getEntityGenericClass();
        log.info("Trying getOne proxy" + clazz.getSimpleName() + " " + id);
        EntityType entity = null;
        entity = (EntityType) em.getReference(clazz, id);
        if (Hibernate.isInitialized(entity)) {
            log.info("Got, alredy initialized " + entity);
        } else {
            log.info("Got proxy of" + clazz.getSimpleName() + " with id= " + id);
        }
        return entity;
    }

    private void update(EntityType entity) {
        log.info("Updating" + entity.getClass().getSimpleName());
        em.flush();
        log.info("Updated " + entity.getClass().getSimpleName());
    }

    public void delete(EntityType entity) {
        log.info("Trying to delete " + entity.getClass().getSimpleName());
        entity = em.merge(entity);
        em.remove(entity);
        em.flush();

    }

}
