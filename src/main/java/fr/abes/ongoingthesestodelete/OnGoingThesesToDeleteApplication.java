package fr.abes.ongoingthesestodelete;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication(
        exclude = { DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class,
                BatchAutoConfiguration.class //if not, all the jobs are launched with NOOP results at the end of each process
        }
)
@EnableTransactionManagement
public class OnGoingThesesToDeleteApplication {

    private Logger logger = LogManager.getLogger(OnGoingThesesToDeleteApplication.class);

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(OnGoingThesesToDeleteApplication.class, args);

    }
}



