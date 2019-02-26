package com.gmail.breninsul.jd2.dao.db.resolver;

import com.gmail.breninsul.jd2.config.Properties;
import com.gmail.breninsul.jd2.dao.db.CertificateDAO;
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

import java.util.List;

@Repository
@Primary
public class UserDAOResolver extends AbstractDAOResolver<User, UserDAO> implements UserDAO {
    @Override
    public List<Product> getProductsPagedAndSorted(User user, String sortType, String field, int paginationSize, int page) {
        return getDao().getProductsPagedAndSorted(user, sortType, field, paginationSize, page);
    }

    @Override
    public List<Product> getProductsPagedAndSorted(User user, String sortType, int paginationSize, int page) {
        return getDao().getProductsPagedAndSorted(user, sortType, paginationSize, page);
    }

    @Override
    public List<Product> getProductsPagedAndSorted(User user, int paginationSize, int page) {
        return getDao().getProductsPagedAndSorted(user, paginationSize, page);
    }

    @Override
    public List<Product> getProductsByNamePagedAndSorted(User user, String name, String sortType, String field, int paginationSize, int page) {
        return getDao().getProductsByNamePagedAndSorted(user,name,sortType,field,paginationSize,page);
    }

    @Override
    public List<Product> getProductsByNamePagedAndSorted(User user, String name, String sortType, int paginationSize, int page) {
        return getDao().getProductsByNamePagedAndSorted(user,name,sortType,paginationSize,page);
    }

    @Override
    public List<Product> getProductsByNamePagedAndSorted(User user, String name, int paginationSize, int page) {
        return getDao().getProductsByNamePagedAndSorted(user,name,paginationSize,page);
    }

    @Override
    public User getUserByName(String name) {
       return getDao().getUserByName(name);
    }


}
