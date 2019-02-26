package com.gmail.breninsul.jd2.dao.db.data;

import com.gmail.breninsul.jd2.pojo.Certificate;
import com.gmail.breninsul.jd2.pojo.Product;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.List;

public interface ICertificateSpringDataDAO extends  IAbstractSpringDataDAO<Certificate, Integer> , JpaRepository<Certificate, Integer> {

    Page<Certificate> findCertificatesByProduct(Product product,Pageable pageable);
    long count();
    Integer countCertificatesByProduct(Product product);
    Integer countCertificatesByProductId(Serializable id);
}
