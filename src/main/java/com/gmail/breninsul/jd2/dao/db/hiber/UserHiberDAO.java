package com.gmail.breninsul.jd2.dao.db.hiber;

import com.gmail.breninsul.jd2.dao.db.UserDAO;
import com.gmail.breninsul.jd2.pojo.Product;
import com.gmail.breninsul.jd2.pojo.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Uses to interaction with DB info about Users
 */

@NoArgsConstructor
@Repository
@Data
@Qualifier("hiberDAO")
public class UserHiberDAO extends AbstarctHiberDAO<User> implements UserDAO {

    @Autowired
    EntityManager em;

    @Override
    public List<Product> getProductsPagedAndSorted(User user, String sortType, String field, int paginationSize, int page) {
        int id = user.getId();
        Query query;
        String jpql = "select p from Product as p where p.user.id=:id ";
        if ("DESC".equals(sortType) || "ASC".equals(sortType)) {
            jpql = jpql + "ORDER BY :field " + sortType;
            query = em.createQuery(jpql);
            query.setParameter("field", " p." + field);
        } else {
            query = em.createQuery(jpql);
        }
        query.setParameter("id", id);
        query.setFirstResult(page * paginationSize - paginationSize);
        query.setMaxResults(page * paginationSize);
        return query.getResultList();
    }

    @Override
    public List<Product> getProductsPagedAndSorted(User user, String sortType, int paginationSize, int page) {
        return getProductsPagedAndSorted(user, sortType, STANDARD_SORT_FIELD, paginationSize, page);
    }

    @Override
    public List<Product> getProductsPagedAndSorted(User user, int paginationSize, int page) {
        return getProductsPagedAndSorted(user, null, STANDARD_SORT_FIELD, paginationSize, page);
    }


    @Override
    public List<Product> getProductsByNamePagedAndSorted(User user, String name, String sortType, String field, int paginationSize, int page) {
        int id = user.getId();
        Query query;
        String jpql = "select p from Product p where p.user.id=:id and p.productName=:name ";
        if ("DESC".equals(sortType) || "ASC".equals(sortType)) {
            jpql = jpql + "ORDER BY :field " + sortType;
            query = em.createQuery(jpql);
            query.setParameter("field", " p." + field);
        } else {
            query = em.createQuery(jpql);
        }
        query.setParameter("id", id);
        query.setParameter("name", name);
        query.setFirstResult(page * paginationSize - paginationSize);
        query.setMaxResults(page * paginationSize);
        return query.getResultList();
    }

    @Override
    public List<Product> getProductsByNamePagedAndSorted(User user, String name, String sortType, int paginationSize, int page) {
        return getProductsByNamePagedAndSorted(user, name, sortType, STANDARD_SORT_FIELD, paginationSize, page);
    }

    @Override
    public List<Product> getProductsByNamePagedAndSorted(User user, String name, int paginationSize, int page) {
        return getProductsByNamePagedAndSorted(user, name, null, STANDARD_SORT_FIELD, paginationSize, page);
    }

    @Override
    public User getUserByName(String name) {
        Query query;
        String jpql = "select u from User u where u.name=:name ";
        query = em.createQuery(jpql);
        query.setParameter("name", name);
        return (User) query.getSingleResult();
    }

    @Override
    public int count() {
        Query query;
        String jpql = "select count (u.id) from User u";
        query = em.createQuery(jpql);
        return (int) query.getSingleResult();
    }
}
