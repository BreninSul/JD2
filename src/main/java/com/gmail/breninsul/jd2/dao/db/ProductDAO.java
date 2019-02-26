package com.gmail.breninsul.jd2.dao.db;

import com.gmail.breninsul.jd2.pojo.Certificate;
import com.gmail.breninsul.jd2.pojo.Product;
import com.gmail.breninsul.jd2.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.cache.annotation.CacheResult;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

public interface ProductDAO extends DAO<Product> {
    @CacheResult()
    List<Certificate> getCertificatesPagedAndSorted(Product product, String sortType, String field, int paginationSize, int page);

    List<Certificate> getCertificatesPagedAndSorted(Product product, String sortType, int paginationSize, int page);

    List<Certificate> getCertificatesPagedAndSorted(Product product, int paginationSize, int page);
    int countProductsByUser(User user);
    int countProductsByUserId(int id);
}
