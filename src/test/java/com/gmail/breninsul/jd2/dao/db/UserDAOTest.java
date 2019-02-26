package com.gmail.breninsul.jd2.dao.db;

import com.gmail.breninsul.jd2.dao.db.CertificateDAO;
import com.gmail.breninsul.jd2.dao.db.ProductDAO;
import com.gmail.breninsul.jd2.dao.db.UserDAO;
import com.gmail.breninsul.jd2.dao.db.hiber.UserHiberDAO;
import com.gmail.breninsul.jd2.dao.db.data.decorators.UserSpringDataDAOImpl;
import com.gmail.breninsul.jd2.pojo.Certificate;
import com.gmail.breninsul.jd2.pojo.Image;
import com.gmail.breninsul.jd2.pojo.Product;
import com.gmail.breninsul.jd2.pojo.User;
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
public class UserDAOTest {
    private static final String IMAGE_PATH ="default_product.jpg";
    @Autowired
    EntityManager em;
    @Autowired
    UserHiberDAO hiberDAO;
    @Autowired
    UserSpringDataDAOImpl dataDAO;
    List<Integer> iDs = new ArrayList<>();
    List<User> entities = new ArrayList<>();
    @Autowired
    UserDAO dao;
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
    public void loadAllPaged() {
        daoLoadAllPaged(hiberDAO);
        daoLoadAllPaged(dataDAO);
        daoLoadAllPaged(dao);
        deliteAll();
    }
    @Test
    @Transactional
    public void loadProdsPaged() {
        daoLoadPaged(hiberDAO);
        daoLoadPaged(dataDAO);
        daoLoadPaged(dao);
    }
    private void daoLoadPaged(UserDAO dao) {
        User user=daoSave(dao);
        for (int i = 0; i < 5; i++) {
            User.addProduct(user,new Product());
        }
        Assert.assertEquals(1,dao.getProductsPagedAndSorted(user,1,1).size());
        Assert.assertEquals(3,dao.getProductsPagedAndSorted(user,3,1).size());

    }
    private void daoLoadAllPaged(UserDAO dao) {
        save();
        save();
        em.clear();
        Assert.assertEquals(1, dao.findAll(1,1).size());
        Assert.assertEquals(2, dao.findAll(2,1).size());
    }
    private User daoSave(UserDAO dao) {
        User entity = create();
        dao.save(entity);
        iDs.add(entity.getId());
        Assert.assertTrue(entity.getId() > 0);
        entities.add(entity);
        em.flush();
        entity.setRole("user");
        dao.save(entity);
        em.flush();
        Product product = new Product();
        product.setType(1);
        User.addProduct(entity,product);
        InputStream in = getClass().getClassLoader().getResourceAsStream(IMAGE_PATH);
        entity.setImage(new Image(in));
        dao.save(entity);
        em.flush();
        return entity;
    }

    private void save() {
        daoSave(hiberDAO);
        daoSave(dataDAO);
        daoSave(dao);
    }

    private void daoLoad(UserDAO dao) {
        save();
        em.clear();
        int size = entities.size() - 1;
        Assert.assertEquals(entities.get(size).getId(), dao.getOne(iDs.get(size)).getId());
        Assert.assertNotNull(dao.getOne(iDs.get(size)).getImage());
    }

    private void daoLoadAll(UserDAO dao) {
        save();
        save();
        em.clear();
        Assert.assertEquals(entities, dao.findAll());
    }

    private void daoDeliteAll(UserDAO dao) {
        save();
        save();
        em.clear();
        for (User user : entities) {
            hiberDAO.delete(user);
        }
        em.flush();
        Assert.assertEquals(0, dao.findAll().size());
        iDs.clear();
        entities.clear();
        em.clear();
    }

    private User create() {
        User entity = new User();
        entity.setEmail("1");
        entity.setRole(new Random().nextInt()+"");
        return entity;
    }

}
