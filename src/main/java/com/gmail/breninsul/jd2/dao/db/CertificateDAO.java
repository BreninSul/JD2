package com.gmail.breninsul.jd2.dao.db;

import com.gmail.breninsul.jd2.pojo.BaseEntity;
import com.gmail.breninsul.jd2.pojo.Certificate;
import com.gmail.breninsul.jd2.pojo.Product;

import java.io.Serializable;
import java.util.List;

public interface CertificateDAO extends DAO<Certificate>{
    int countCertificatesByProduct(Product product);
    int countCertificatesByProductId(int id);
}
