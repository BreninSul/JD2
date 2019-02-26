package com.gmail.breninsul.jd2.dao.db.hiber;

import com.gmail.breninsul.jd2.dao.db.ProductDAO;
import com.gmail.breninsul.jd2.pojo.Certificate;
import com.gmail.breninsul.jd2.pojo.Product;
import com.gmail.breninsul.jd2.pojo.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Uses to interaction with DB info about Product
 */

@NoArgsConstructor
@Repository
@Data
@Qualifier("hiberDAO")
public class ProductHiberDAO extends AbstarctHiberDAO<Product> implements ProductDAO {

    @Autowired
    EntityManager em;

    @Override
    public List<Certificate> getCertificatesPagedAndSorted(Product product, String sortType, String field, int paginationSize, int page) {
        int id = product.getId();
        Query query;
        String jpql = "select c from Certificate as c where c.product.id=:id ";
        if ("DESC".equals(sortType) || "ASC".equals(sortType)) {
            jpql = jpql + "ORDER BY :field " + sortType;
            query = em.createQuery(jpql);
            query.setParameter("field", " c." + field);
        } else {
            query = em.createQuery(jpql);
        }
        query.setParameter("id", id);
        query.setFirstResult(page * paginationSize - paginationSize);
        query.setMaxResults(page * paginationSize);
        return query.getResultList();
    }

    @Override
    public List<Certificate> getCertificatesPagedAndSorted(Product product, String sortType, int paginationSize, int page) {
        return getCertificatesPagedAndSorted(product, sortType, STANDARD_SORT_FIELD, paginationSize, page);
    }

    @Override
    public List<Certificate> getCertificatesPagedAndSorted(Product product, int paginationSize, int page) {
        return getCertificatesPagedAndSorted(product, null, STANDARD_SORT_FIELD, paginationSize, page);
    }

    @Override
    public int countProductsByUser(User user) {
        return countProductsByUserId(user.getId());
    }

    @Override
    public int countProductsByUserId(int id) {
        Query query;
        String jpql = "select count(u.products) from User u where u.id=:id ";
        query = em.createQuery(jpql);
        query.setParameter("id", id);
        return (int) query.getSingleResult();
    }


    @Override
    public int count() {
        Query query;
        String jpql = "select count(p.id) from Product p";
        query = em.createQuery(jpql);
        return (int) query.getSingleResult();
    }
}
