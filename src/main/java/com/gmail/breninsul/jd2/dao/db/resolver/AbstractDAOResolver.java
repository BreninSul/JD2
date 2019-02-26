package com.gmail.breninsul.jd2.dao.db.resolver;

import com.gmail.breninsul.jd2.config.Properties;
import com.gmail.breninsul.jd2.dao.db.DAO;
import com.gmail.breninsul.jd2.pojo.BaseEntity;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;

@Repository
@Primary
@Data
public abstract class AbstractDAOResolver<EntityType extends BaseEntity, DAOType extends DAO> {
    @Autowired(required = Properties.USE_SPRING_DATA)
    @Qualifier("dataDAO")
    private DAOType dataDAO;
    @Autowired(required = !Properties.USE_SPRING_DATA)
    @Qualifier("hiberDAO")
    private DAOType hiberDAO;
    protected DAOType dao;

    @PostConstruct
    private void initDAO() {
        if (!Properties.USE_SPRING_DATA) {
            dao = hiberDAO;
        } else {
            dao = dataDAO;
        }
    }

    public EntityType save(EntityType entity) {
        return (EntityType) dao.save(entity);
    }

    public EntityType getOne(Serializable id) {
        return (EntityType) dao.getOne(id);
    }

    public List<EntityType> findAll() {
        return (List<EntityType>) dao.findAll();
    }

    public void delete(EntityType entity) {
        dao.delete(entity);
    }

    public List<EntityType> findAll(PageRequest pageRequest) {
        return (List<EntityType>) dao.findAll(pageRequest);
    }

    public List<EntityType> findAll(String sortType, String field, int paginationSize, int page) {
        return (List<EntityType>) dao.findAll(sortType, field, paginationSize, page);
    }

    public List<EntityType> findAll(String sortType, int paginationSize, int page) {
        return (List<EntityType>) dao.findAll(sortType, paginationSize, page);

    }

    public List<EntityType> findAll(int paginationSize, int page) {
        return (List<EntityType>) dao.findAll(paginationSize, page);
    }
    public int count(){
        return  (int) dao.count();
    }

}
