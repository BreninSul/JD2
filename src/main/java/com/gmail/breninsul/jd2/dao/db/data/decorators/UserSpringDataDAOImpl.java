package com.gmail.breninsul.jd2.dao.db.data.decorators;

import com.gmail.breninsul.jd2.dao.db.UserDAO;
import com.gmail.breninsul.jd2.dao.db.data.IProductSpringDataDAO;
import com.gmail.breninsul.jd2.dao.db.data.IUserSpringDataDAO;
import com.gmail.breninsul.jd2.pojo.Certificate;
import com.gmail.breninsul.jd2.pojo.Product;
import com.gmail.breninsul.jd2.pojo.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;


/**
 * Uses to interaction with DB info about Certificates
 */
@Data
@NoArgsConstructor
@Repository
@Qualifier("dataDAO")
public class UserSpringDataDAOImpl extends AbstractSpringDataDAO<User, IUserSpringDataDAO> implements UserDAO {


    @Autowired
    IUserSpringDataDAO rep;

    @Override
    public List<Product> getProductsPagedAndSorted(User user, String sortType, String field, int paginationSize, int page) {
        if ("ASC".equals(sortType)) {
            return getProductsPagedAndSorted(user.getId(), PageRequest.of(page, paginationSize, Sort.Direction.ASC, field));
        } else {
            return getProductsPagedAndSorted(user.getId(), PageRequest.of(page, paginationSize, Sort.Direction.DESC, field));
        }
    }

    @Override
    public List<Product> getProductsPagedAndSorted(User user, String sortType, int paginationSize, int page) {
        if ("ASC".equals(sortType)) {
            return getProductsPagedAndSorted(user.getId(), PageRequest.of(page, paginationSize, Sort.Direction.ASC, STANDARD_SORT_FIELD));
        } else{
            return getProductsPagedAndSorted(user.getId(), PageRequest.of(page, paginationSize, Sort.Direction.DESC, STANDARD_SORT_FIELD));
        }
    }

    @Override
    public List<Product> getProductsPagedAndSorted(User user, int paginationSize, int page) {
        return getProductsPagedAndSorted(user.getId(), PageRequest.of(page, paginationSize));

    }

    @Override
    public List<Product> getProductsByNamePagedAndSorted(User user, String name, String sortType, String field, int paginationSize, int page) {
        if ("ASC".equals(sortType)) {
            return getProductsByNamePagedAndSorted(user.getId(), name, PageRequest.of(page, paginationSize, Sort.Direction.ASC, field));
        } else {
            return getProductsByNamePagedAndSorted(user.getId(), name, PageRequest.of(page, paginationSize, Sort.Direction.DESC, field));

        }
    }

    @Override
    public List<Product> getProductsByNamePagedAndSorted(User user, String name, String sortType, int paginationSize, int page) {
        if ("ASC".equals(sortType)) {
            return getProductsByNamePagedAndSorted(user.getId(), name, PageRequest.of(page, paginationSize, Sort.Direction.ASC, STANDARD_SORT_FIELD));
        } else {
            return getProductsByNamePagedAndSorted(user.getId(), name, PageRequest.of(page, paginationSize, Sort.Direction.DESC, STANDARD_SORT_FIELD));

        }
    }

    @Override
    public List<Product> getProductsByNamePagedAndSorted(User user, String name, int paginationSize, int page) {
        return getProductsByNamePagedAndSorted(user.getId(), name, PageRequest.of(page, paginationSize));

    }

    @Override
    public User getUserByName(String name) {
        return rep.getUserByName(name);
    }

    private List<Product> getProductsPagedAndSorted(int id, Pageable pageable) {
        return rep.getCertificatesPagedAndSorted(id, pageable).getContent();
    }

    private List<Product> getProductsByNamePagedAndSorted(int id, String name, Pageable pageable) {
        return rep.getProductsByNamePagedAndSorted(id, name, pageable).getContent();
    }
}
