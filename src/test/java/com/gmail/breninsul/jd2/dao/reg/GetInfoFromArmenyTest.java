package com.gmail.breninsul.jd2.dao.reg;

import com.gmail.breninsul.jd2.dao.registry.GetInfoFromArmeny;

import com.gmail.breninsul.jd2.dao.registry.GetInfoFromKirgiz;
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
public class GetInfoFromArmenyTest extends AbstractRegTest<GetInfoFromArmeny>{

}