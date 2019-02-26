package com.gmail.breninsul.jd2.dao.db.data.decorators;

import com.gmail.breninsul.jd2.dao.db.ProductDAO;
import com.gmail.breninsul.jd2.dao.db.data.ICertificateSpringDataDAO;
import com.gmail.breninsul.jd2.dao.db.data.IProductSpringDataDAO;
import com.gmail.breninsul.jd2.pojo.Certificate;
import com.gmail.breninsul.jd2.pojo.Product;
import com.gmail.breninsul.jd2.pojo.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Uses to interaction with DB info about Certificates
 */
@Data
@NoArgsConstructor
@Repository
@Qualifier("dataDAO")
public class ProductSpringDataDAOImpl extends AbstractSpringDataDAO<Product, IProductSpringDataDAO> implements ProductDAO {
    @Autowired
    IProductSpringDataDAO rep;

    @Override
    public List<Certificate> getCertificatesPagedAndSorted(Product product, String sortType, String field, int paginationSize, int page) {
        if ("ASC".equals(sortType)) {
            return getCertificatesPagedAndSorted(product.getId(), PageRequest.of(page, paginationSize, Sort.Direction.ASC, field));
        }else {
            return getCertificatesPagedAndSorted(product.getId(), PageRequest.of(page, paginationSize, Sort.Direction.DESC, field));

        }
    }

    @Override
    public List<Certificate> getCertificatesPagedAndSorted(Product product, String sortType, int paginationSize, int page) {
        if ("ASC".equals(sortType)) {
            return getCertificatesPagedAndSorted(product.getId(), PageRequest.of(page, paginationSize, Sort.Direction.ASC, STANDARD_SORT_FIELD));
        }else {
            return getCertificatesPagedAndSorted(product.getId(), PageRequest.of(page, paginationSize, Sort.Direction.DESC, STANDARD_SORT_FIELD));

        }
    }

    @Override
    public List<Certificate> getCertificatesPagedAndSorted(Product product, int paginationSize, int page) {
        return getCertificatesPagedAndSorted(product.getId(),PageRequest.of(page,paginationSize));

    }

    @Override
    public int countProductsByUser(User user) {
       return rep.countProductsByUser(user);
    }

    @Override
    public int countProductsByUserId(int id) {
        return rep.countProductsByUserId(id);
    }

    private List<Certificate> getCertificatesPagedAndSorted(int id, Pageable pageable) {
        return rep.getCertificatesPagedAndSorted(id,pageable).getContent();

    }

}
