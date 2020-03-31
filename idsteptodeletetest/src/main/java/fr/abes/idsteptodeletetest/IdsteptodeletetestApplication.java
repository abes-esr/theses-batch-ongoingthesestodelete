package fr.abes.idsteptodeletetest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(
        //exclude = { DataSourceAutoConfiguration.class,
              //  HibernateJpaAutoConfiguration.class,
              //  DataSourceTransactionManagerAutoConfiguration.class,
              //  BatchAutoConfiguration.class})
)
@EnableTransactionManagement
public class IdsteptodeletetestApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdsteptodeletetestApplication.class, args);
    }

}
