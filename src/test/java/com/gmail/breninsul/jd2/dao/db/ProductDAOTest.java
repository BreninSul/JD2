package com.gmail.breninsul.jd2.dao.db;

import com.gmail.breninsul.jd2.dao.db.CertificateDAO;
import com.gmail.breninsul.jd2.dao.db.ProductDAO;
import com.gmail.breninsul.jd2.dao.db.data.ICertificateSpringDataDAO;
import com.gmail.breninsul.jd2.dao.db.data.decorators.CertificateSpringDataDAOImpl;
import com.gmail.breninsul.jd2.dao.db.hiber.ProductHiberDAO;
import com.gmail.breninsul.jd2.dao.db.data.decorators.ProductSpringDataDAOImpl;
import com.gmail.breninsul.jd2.pojo.Certificate;
import com.gmail.breninsul.jd2.pojo.Image;
import com.gmail.breninsul.jd2.pojo.Product;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(value = "classpath:Requirements.sql")
public class ProductDAOTest {
    private static final String IMAGE_PATH ="default_product.jpg";
    @Autowired
    EntityManager em;
    @Autowired
    ProductHiberDAO hiberDAO;
    @Autowired
    ProductSpringDataDAOImpl dataDAO;
    @Autowired
    ProductDAO dao;
    List<Integer> iDs = new ArrayList<>();
    List<Product> entities = new ArrayList<>();
    @Autowired
    CertificateSpringDataDAOImpl dao2;

    @Transactional
    @Test
    public void saveTest() {
        save();
        deliteAll();
    }

    @Test
    @Transactional
    public void loadAll() {
        daoLoadAll(hiberDAO);
        daoLoadAll(dataDAO);
        daoLoadAll(dao);
        deliteAll();
    }


    @Test
    @Transactional
    public void deliteAll() {
        daoDeliteAll(hiberDAO);
        daoDeliteAll(dataDAO);
        daoDeliteAll(dao);
    }

    @Test
    @Transactional
    public void load() {
        daoLoad(hiberDAO);
        daoLoad(dataDAO);
        daoLoad(dao);
        deliteAll();
    }
    @Test
    @Transactional
    public void loadCertsPaged() {
         daoLoadPaged(hiberDAO);
         daoLoadPaged(dataDAO);
        daoLoadPaged(dao);
    }
    private void daoLoadPaged(ProductDAO dao) {
        Product entity=daoSaveCerts(dao);
        Assert.assertEquals(entity.getCertificates().size(),dao.getOne(entity.getId()).getCertificates().size());
        int size = entities.size() - 1;
        em.clear();
        Assert.assertEquals(1,dao.getCertificatesPagedAndSorted(entity,1,1).size());
        Assert.assertEquals(3,dao.getCertificatesPagedAndSorted(entity,3,1).size());

    }
    private void daoSave(ProductDAO dao) {
        Product entity = create();
        dao.save(entity);
        iDs.add(entity.getId());
        Assert.assertTrue(entity.getId() > 0);
        entities.add(entity);
        em.flush();
        entity.setType(5);
        dao.save(entity);
        em.flush();
        Certificate certificate = new Certificate();
        certificate.setAlive(1);
        certificate.setProduct(entity);
        Product.addCertificate(entity,certificate);
        InputStream in = getClass().getClassLoader().getResourceAsStream(IMAGE_PATH);
        entity.setImage(new Image("http://www.deplaque.com/Portals/0/CVStoreImages/default_product_image_400.jpg"));
        dao.save(entity);
        em.flush();

    }
    private Product daoSaveCerts(ProductDAO dao) {
        Product entity = create();
        dao.save(entity);
        iDs.add(entity.getId());
        Assert.assertTrue(entity.getId() > 0);
        entities.add(entity);
        em.flush();
        for (int i = 0; i <10 ; i++) {
            Certificate certificate = new Certificate();
            certificate.setAlive(1);
            entity.getCertificates().add(certificate);
        //    Product.addCertificate(entity,certificate);
        }
        dao.save(entity);
        em.flush();
        return entity;
    }
    private void save() {
        daoSave(hiberDAO);
        daoSave(dataDAO);
        daoSave(dao);
    }

    private void daoLoad(ProductDAO dao) {
        save();
        em.clear();
        int size = entities.size() - 1;
        Assert.assertEquals(entities.get(size).getId(), dao.getOne(iDs.get(size)).getId());
        Assert.assertNotNull(dao.getOne(iDs.get(size)).getImage());
        Assert.assertEquals(entities.get(size).getImage(),dao.getOne(iDs.get(size)).getImage());
        List<Certificate> list1=entities.get(size).getCertificates();
        List<Certificate> list2=dao.getOne(iDs.get(size)).getCertificates();
        Assert.assertTrue(list1.containsAll(list2));
        Assert.assertTrue(dao.getOne(iDs.get(size)).getCertificates().size()>0);

    }

    private void daoLoadAll(ProductDAO dao) {
        save();
        save();
        em.clear();
        Assert.assertEquals(entities, dao.findAll());
    }

    private void daoDeliteAll(ProductDAO dao) {
        save();
        save();
        em.clear();
        for (Product product : entities) {
            hiberDAO.delete(product);
        }
        em.flush();
        Assert.assertEquals(0, dao.findAll().size());
        iDs.clear();
        entities.clear();
        em.clear();
    }

    private Product create() {
        Product entity = new Product();
        entity.setType(1);
        entity.setStatus(new Random().nextInt());
        return entity;
    }

}
