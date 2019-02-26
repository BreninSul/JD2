package com.gmail.breninsul.jd2.dao.db.resolver;

import com.gmail.breninsul.jd2.config.Properties;
import com.gmail.breninsul.jd2.dao.db.CertificateDAO;
import com.gmail.breninsul.jd2.dao.db.ProductDAO;
import com.gmail.breninsul.jd2.dao.db.UserDAO;
import com.gmail.breninsul.jd2.pojo.Certificate;
import com.gmail.breninsul.jd2.pojo.Product;
import com.gmail.breninsul.jd2.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

@Repository
@Primary
public class ProductDAOResolver extends AbstractDAOResolver<Product, ProductDAO> implements ProductDAO {

    @Override
    public List<Certificate> getCertificatesPagedAndSorted(Product product, String sortType, String field, int paginationSize, int page) {
        return getDao().getCertificatesPagedAndSorted(product, sortType, field, paginationSize, page);
    }

    @Override
    public List<Certificate> getCertificatesPagedAndSorted(Product product, String sortType, int paginationSize, int page) {
        return getDao().getCertificatesPagedAndSorted(product, sortType, STANDARD_SORT_FIELD, paginationSize, page);
    }

    @Override
    public List<Certificate> getCertificatesPagedAndSorted(Product product, int paginationSize, int page) {
        return getDao().getCertificatesPagedAndSorted(product, paginationSize, page);
    }

    @Override
    public int countProductsByUser(User user) {
       return getDao().countProductsByUser(user);
    }

    @Override
    public int countProductsByUserId(int id) {
        return getDao().countProductsByUserId(id);
    }


}
