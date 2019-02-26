package com.gmail.breninsul.jd2.dao.db;

import com.gmail.breninsul.jd2.dao.db.CertificateDAO;
import com.gmail.breninsul.jd2.dao.db.hiber.CertificateHiberDAO;
import com.gmail.breninsul.jd2.dao.db.data.decorators.CertificateSpringDataDAOImpl;
import com.gmail.breninsul.jd2.pojo.Certificate;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(value = "classpath:Requirements.sql")
public class CertificateDAOTest {

    @Autowired
    EntityManager em;
    @Autowired
    CertificateHiberDAO hiberDAO;
    @Autowired
    CertificateSpringDataDAOImpl dataDAO;
    @Autowired
    CertificateDAO dao;
    List<Integer> iDs = new ArrayList<>();
    List<Certificate> entities = new ArrayList<>();


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
    public void loadAllPaged() {
        daoLoadAllPaged(hiberDAO);
        daoLoadAllPaged(dataDAO);
        daoLoadAllPaged(dao);
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

    private Certificate create() {
        Certificate entity = new Certificate();
        entity.setCertType(1);
        entity.setAlive(new Random().nextInt());
        return entity;
    }

    private void daoSave(CertificateDAO dao) {
        Certificate entity = create();
        dao.save(entity);
        iDs.add(entity.getId());
        Assert.assertTrue(entity.getId() > 0);
        entities.add(entity);
        em.flush();
        entity.setAlive(5);
        dao.save(entity);
        em.flush();
    }

    private void save() {
        daoSave(hiberDAO);
        daoSave(dataDAO);
        daoSave(dao);
    }

    private void daoLoad(CertificateDAO dao) {
        save();
        em.clear();
        int size = entities.size() - 1;
        Assert.assertEquals(entities.get(size).getId(), dao.getOne(iDs.get(size)).getId());
    }

    private void daoLoadAll(CertificateDAO dao) {
        save();
        save();
        em.clear();
        Assert.assertEquals(entities, dao.findAll());
    }
    private void daoLoadAllPaged(CertificateDAO dao) {
        save();
        save();
        em.clear();
        Assert.assertEquals(1, dao.findAll(1,1).size());
    }
    private void daoDeliteAll(CertificateDAO dao) {
        save();
        save();
        em.clear();
        for (Certificate certificate : entities) {
            hiberDAO.delete(certificate);
        }
        em.flush();
        Assert.assertEquals(0, dao.findAll().size());
        iDs.clear();
        entities.clear();
        em.clear();
    }
}
