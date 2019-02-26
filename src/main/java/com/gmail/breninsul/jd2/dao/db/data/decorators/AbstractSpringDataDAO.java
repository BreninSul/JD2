package com.gmail.breninsul.jd2.dao.db.data.decorators;


import com.gmail.breninsul.jd2.dao.db.DAO;
import com.gmail.breninsul.jd2.dao.db.GenericClassResolver;
import com.gmail.breninsul.jd2.pojo.BaseEntity;
import lombok.Data;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.NoSuchElementException;

@Log4j2
@Data
@Repository
public abstract class AbstractSpringDataDAO<EntityType extends BaseEntity, DAOType extends JpaRepository> extends GenericClassResolver<EntityType> implements DAO<EntityType> {

    @Autowired()
    DAOType rep;

    public EntityType save(EntityType entity) {
        return (EntityType) rep.save(entity);
    }

    public EntityType getOne(Serializable id) {
        try {
            return (EntityType) rep.findById(id).get();
        } catch (NoSuchElementException e) {
            log.info("There is no user with id=" + id);
        }
        return null;
    }

    public int count() {
        return (int) rep.count();
    }

    public List<EntityType> findAll() {
        return (List<EntityType>) rep.findAll();
    }

    public List<EntityType> findAll(PageRequest pageRequest) {
        return (List<EntityType>) rep.findAll(pageRequest);
    }

    /**
     * @param sortType use "DESC" or "ASC"
     */
    public List<EntityType> findAll(String sortType, String field, int paginationSize, int page) {
        List<EntityType> resultPage = null;
        if ("DESC".equals(sortType)) {
            resultPage = (rep).findAll(PageRequest.of(page, paginationSize, Sort.Direction.DESC, field)).getContent();
        } else {
            if ("ASC".equals(sortType)) {
                resultPage = (rep).findAll(PageRequest.of(page, paginationSize, Sort.Direction.ASC, field)).getContent();
            } else {
                resultPage = (rep).findAll(PageRequest.of(page, paginationSize, Sort.DEFAULT_DIRECTION, field)).getContent();
            }
        }
        return resultPage;
    }

    /**
     * @param sortType use "DESC" or "ASC"
     */
    public List<EntityType> findAll(String sortType, int paginationSize, int page) {
        return findAll(sortType, "updatedDate", paginationSize, page);
    }

    public List<EntityType> findAll(int paginationSize, int page) {
        return findAll(null, "updatedDate", paginationSize, page);
    }

    public void delete(EntityType entity) {
        rep.delete(entity);
    }
}
