package com.gmail.breninsul.jd2.dao.db.resolver;

import com.gmail.breninsul.jd2.dao.db.CertificateDAO;
import com.gmail.breninsul.jd2.pojo.Certificate;
import com.gmail.breninsul.jd2.pojo.Product;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class CertificateDAOResolver extends AbstractDAOResolver <Certificate,CertificateDAO> implements CertificateDAO{
    @Override
    public int countCertificatesByProduct(Product product) {
       return getDao().countCertificatesByProduct(product);
    }

    @Override
    public int countCertificatesByProductId(int id) {
        return getDao().countCertificatesByProductId(id);
    }
}
