package com.gmail.breninsul.jd2.dao.db.data;

import com.gmail.breninsul.jd2.pojo.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public  interface IAbstractSpringDataDAO<EntityType extends BaseEntity, IdType> extends JpaRepository<EntityType, IdType> {
    long count();
    <S extends EntityType> S save(S s);
    Optional<EntityType> findById(IdType idType);
    List<EntityType> findAll();
    void delete(EntityType entity);
    Page <EntityType> findAll(Pageable pageable);
}
