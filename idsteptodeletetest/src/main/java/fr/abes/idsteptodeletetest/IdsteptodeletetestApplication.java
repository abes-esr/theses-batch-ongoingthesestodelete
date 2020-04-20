package fr.abes.idsteptodeletetest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@SpringBootApplication(//scanBasePackages = {"fr.abes.idsteptodeletetest.JpaBatchConfigurer"},
        scanBasePackageClasses = fr.abes.idsteptodeletetest.JpaBatchConfigurer.class,
        exclude = { DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class,
                BatchAutoConfiguration.class}
)
@EnableTransactionManagement
public class IdsteptodeletetestApplication {

    private Logger logger = LogManager.getLogger(IdsteptodeletetestApplication.class);

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(IdsteptodeletetestApplication.class, args);

    }
}



