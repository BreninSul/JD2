package com.gmail.breninsul.jd2.dao.reg;

import com.gmail.breninsul.jd2.dao.registry.GetInfoFromRB;
import com.gmail.breninsul.jd2.dao.registry.ServerNotAvailableException;
import com.gmail.breninsul.jd2.pojo.Certificate;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

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

public class GetInfoFromRBTest {
    public static final String TEST_VALUE = "товар";
    @Autowired
    GetInfoFromRB dao;
    @Test
    public void getCertificates() {
        List<Certificate> certificates= null;
        boolean timeOut=false;
        try {
            certificates = dao.getCertificatesAndDeclorations(TEST_VALUE,6,1000);
        } catch (ServerNotAvailableException e) {
            timeOut=true;
            log.warn("No response from server", e);
        } if (!timeOut){
            Assert.assertNotNull(certificates);
            Assert.assertNotEquals(0, certificates.size());
            log.warn(certificates.toString());
        }
    }


}