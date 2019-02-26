package com.gmail.breninsul.jd2.dao.db.data.decorators;

import com.gmail.breninsul.jd2.dao.db.CertificateDAO;
import com.gmail.breninsul.jd2.dao.db.data.ICertificateSpringDataDAO;
import com.gmail.breninsul.jd2.dao.db.data.IProductSpringDataDAO;
import com.gmail.breninsul.jd2.pojo.Certificate;
import com.gmail.breninsul.jd2.pojo.Product;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
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
public class CertificateSpringDataDAOImpl extends AbstractSpringDataDAO<Certificate, ICertificateSpringDataDAO> implements CertificateDAO {
    @Autowired
    ICertificateSpringDataDAO rep;
    @Override
    public int countCertificatesByProduct(Product product) {
        return rep.countCertificatesByProduct(product);
    }

    @Override
    public int countCertificatesByProductId(int id) {
        return rep.countCertificatesByProductId(id);
    }
}
