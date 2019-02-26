package com.gmail.breninsul.jd2.dao.reg;

import com.gmail.breninsul.jd2.dao.registry.GetInfoFromArmeny;
import com.gmail.breninsul.jd2.dao.registry.GetInfoFromKirgiz;
import lombok.extern.log4j.Log4j2;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql(value = "classpath:Requirements.sql")
@Log4j2
public class GetInfoFromKirgTest extends AbstractRegTest<GetInfoFromKirgiz>{

}