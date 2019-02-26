package com.gmail.breninsul.jd2.dao.db.data;

import com.gmail.breninsul.jd2.pojo.Certificate;
import com.gmail.breninsul.jd2.pojo.Product;
import com.gmail.breninsul.jd2.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface IUserSpringDataDAO extends IAbstractSpringDataDAO<User, Integer> {
    @Query("select p from Product p where p.user.id=:id")
    Page<Product> getCertificatesPagedAndSorted(@Param("id") int id, Pageable pageable);

    @Query("select p from Product p where p.user.id=:id and p.productName=:name")
    Page<Product> getProductsByNamePagedAndSorted(@Param("id") int id, @Param("name") String names, Pageable pageable);
    User getUserByName(String name);
}
