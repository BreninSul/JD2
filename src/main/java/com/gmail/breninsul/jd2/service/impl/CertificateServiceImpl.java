package com.gmail.breninsul.jd2.service.impl;

import com.gmail.breninsul.jd2.dao.db.CertificateDAO;
import com.gmail.breninsul.jd2.pojo.Certificate;
import com.gmail.breninsul.jd2.pojo.Product;
import com.gmail.breninsul.jd2.service.CertificateService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
@Service
public class CertificateServiceImpl extends AbstractService<Certificate, CertificateDAO> implements CertificateService {
    private static final int CERTIFICATE_CACHE_LIFETIME_HOURS = 24;
    public static final String CERTIFICATES_CACHE_NAME = "certificatesCache";

    @PostConstruct
    public void init() {
        cache = createCache(Certificate.class, CERTIFICATE_CACHE_LIFETIME_HOURS, CERTIFICATES_CACHE_NAME);
    }

    @Override
    public int countCertificatesByProduct(Product product) {
        return dao.countCertificatesByProduct(product);
    }

    @Override
    public int countCertificatesByProductId(int id) {
        return dao.countCertificatesByProductId(id);
    }
}
