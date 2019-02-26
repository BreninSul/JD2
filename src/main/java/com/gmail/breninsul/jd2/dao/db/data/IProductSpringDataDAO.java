package com.gmail.breninsul.jd2.dao.db.data;

import com.gmail.breninsul.jd2.pojo.Certificate;
import com.gmail.breninsul.jd2.pojo.Product;
import com.gmail.breninsul.jd2.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;


public interface IProductSpringDataDAO extends  IAbstractSpringDataDAO<Product, Integer> , PagingAndSortingRepository<Product, Integer> {
    @Query("select c from Certificate c where c.product.id=:id")
    Page<Certificate> getCertificatesPagedAndSorted(@Param("id") int id, Pageable pageable);
    Integer countProductsByUser(User user);
    Integer countProductsByUserId(Serializable id);
}
