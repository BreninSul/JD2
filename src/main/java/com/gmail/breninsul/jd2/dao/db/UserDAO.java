package com.gmail.breninsul.jd2.dao.db;

import com.gmail.breninsul.jd2.pojo.Certificate;
import com.gmail.breninsul.jd2.pojo.Product;
import com.gmail.breninsul.jd2.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserDAO extends DAO<User> {
    public static final String JPQL_PRODUCT_FIELD_PREFIX = " Product.";

    List<Product> getProductsPagedAndSorted(User user, String sortType, String field, int paginationSize, int page);

    List<Product> getProductsPagedAndSorted(User user, String sortType, int paginationSize, int page);

    List<Product> getProductsPagedAndSorted(User user, int paginationSize, int page);

    List<Product> getProductsByNamePagedAndSorted(User user, String name, String sortType, String field, int paginationSize, int page);

    List<Product> getProductsByNamePagedAndSorted(User user, String name, String sortType, int paginationSize, int page);

    List<Product> getProductsByNamePagedAndSorted(User user, String name, int paginationSize, int page);
    User getUserByName(String name);

}
