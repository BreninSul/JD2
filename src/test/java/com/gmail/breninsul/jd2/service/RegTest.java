package com.gmail.breninsul.jd2.service;

import com.gmail.breninsul.jd2.dao.registry.InfoEntity;
import com.gmail.breninsul.jd2.dao.registry.ServerNotAvailableException;
import com.gmail.breninsul.jd2.pojo.Certificate;
import lombok.extern.log4j.Log4j2;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(value = "classpath:Requirements.sql")
@Log4j2
public abstract class RegTest {
    public static final String TEST_VALUE = "товар";
    @Autowired
    RegestryService service;


    @Test
    public void testAll() {
        InfoEntity entity = service.get(TEST_VALUE, 6, 10000);
        Assert.assertNotNull(entity);
    }

}
