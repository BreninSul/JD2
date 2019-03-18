package com.gmail.breninsul.jd2;

import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.persistence.SharedCacheMode;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
        , SessionAutoConfiguration.class, MultipartAutoConfiguration.class
})
@ComponentScan("com.gmail.breninsul.jd2")
@EntityScan("com.gmail.breninsul.jd2")
@EnableJpaRepositories("com.gmail.breninsul.jd2")
@EnableTransactionManagement
@EnableCaching
@EnableAspectJAutoProxy
public class Jd2Application extends SpringBootServletInitializer {
    private final static String PERSISTENCE_UNIT = "sql";
    private final static String SQL_PROPERTIES_PATH = "sql.properties";
    private final static Properties SQL_PROPERTIES = new Properties();
    private final static String JPA_HIBERNATE_PROPERTIES_PATH = "jpa-hibernate.properties";
    private final static Properties JPA_HIBERNATE_PROPERTIES = new Properties();
    public static final String MAIL_ADRESS = "certificatesjd2@gmail.com";
    public static final String PASSWORD = "Qq112233";

    public Jd2Application() {
        try {
            JPA_HIBERNATE_PROPERTIES.load(getClass().getClassLoader().getResourceAsStream(JPA_HIBERNATE_PROPERTIES_PATH));
            SQL_PROPERTIES.load(getClass().getClassLoader().getResourceAsStream(SQL_PROPERTIES_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Bean
    @Primary
    @Profile("!test")
    @Qualifier("props")
    public Map<String, String> hibernateJpaProperties() {
        HashMap<String, String> properties = new HashMap<>();
        for (Map.Entry entry : JPA_HIBERNATE_PROPERTIES.entrySet()) {
            properties.put(entry.getKey().toString(), entry.getValue().toString());
        }
        return properties;
    }

    @Bean
    @Profile("test")
    @Qualifier("props")
    public Map<String, String> hibernateJpaPropertiesTest() {
        HashMap<String, String> properties = new HashMap<>();
        //JPA_HIBERNATE_PROPERTIES.put("hibernate.hbm2ddl.auto","vertify");
        for (Map.Entry entry : JPA_HIBERNATE_PROPERTIES.entrySet()) {
            properties.put(entry.getKey().toString(), entry.getValue().toString());
        }
        properties.put("hibernate.hbm2ddl.auto", "create");
        return properties;
    }

    @Bean
    @Primary
    @Profile("!test")
    public HikariDataSource dataSource() {
        HikariDataSource basicDataSource = new HikariDataSource();
        basicDataSource.setDriverClassName(SQL_PROPERTIES.getProperty("DriverClassName"));
        basicDataSource.setJdbcUrl(SQL_PROPERTIES.getProperty("JdbcUrl"));
        basicDataSource.setUsername(SQL_PROPERTIES.getProperty("Username"));
        basicDataSource.setPassword(SQL_PROPERTIES.getProperty("Password"));
        return basicDataSource;
    }

    @Bean
    @Profile("test")
    HikariDataSource dataSourceTest() {
        HikariDataSource basicDataSource = new HikariDataSource();
        basicDataSource.setDriverClassName(SQL_PROPERTIES.getProperty("test.DriverClassName"));
        basicDataSource.setJdbcUrl(SQL_PROPERTIES.getProperty("test.JdbcUrl"));
        return basicDataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, @Qualifier("props") Map<String, String> props) {
        LocalContainerEntityManagerFactoryBean entityManagerFactory = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactory.setPersistenceProvider(new HibernatePersistenceProvider());
        entityManagerFactory.setDataSource(dataSource);
        entityManagerFactory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManagerFactory.setJpaDialect(new HibernateJpaDialect());
        entityManagerFactory.setPackagesToScan("com.gmail.breninsul.jd2.pojo");
        entityManagerFactory.setJpaPropertyMap(props);
        entityManagerFactory.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE);
        return entityManagerFactory;
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(MAIL_ADRESS);
        mailSender.setPassword(PASSWORD);
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        return mailSender;
    }

    public static void main(String[] args) {
        System.setProperty("path", System.getProperty("user.dir"));
        SpringApplication.run(Jd2Application.class, args);
    }

}

