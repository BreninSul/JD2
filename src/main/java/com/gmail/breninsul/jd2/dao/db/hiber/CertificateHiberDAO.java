package com.gmail.breninsul.jd2.dao.db.hiber;

import com.gmail.breninsul.jd2.dao.db.CertificateDAO;
import com.gmail.breninsul.jd2.dao.db.data.ICertificateSpringDataDAO;
import com.gmail.breninsul.jd2.dao.db.data.IProductSpringDataDAO;
import com.gmail.breninsul.jd2.pojo.Certificate;
import com.gmail.breninsul.jd2.pojo.Product;
import com.gmail.breninsul.jd2.pojo.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;


/**
 * Uses to interaction with DB info about Certificates
 */
@Data
@NoArgsConstructor
@Repository
@Qualifier("hiberDAO")
public class CertificateHiberDAO extends AbstarctHiberDAO<Certificate> implements CertificateDAO {
    @Autowired
    EntityManager em;
    @Override
    public int countCertificatesByProduct(Product product) {
        return countCertificatesByProductId(product.getId());
    }

    @Override
    public int countCertificatesByProductId(int id) {
        Query query;
        String jpql = "select count(p.certificates) from Product p where p.id=:id ";
        query = em.createQuery(jpql);
        query.setParameter("id", id);
        return (int) query.getSingleResult();
    }

    @Override
    public int count() {
        Query query;
        String jpql = "select count(c.id) from Certificate c";
        query = em.createQuery(jpql);
        return (int) query.getSingleResult();
    }
}
