package fr.abes.idsteptodeletetest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.HashMap;
import java.util.Map;


/*
@SpringBootApplication(//scanBasePackageClasses = fr.abes.idsteptodeletetest.CommandLineJobRunner.class,
        //scanBasePackageClasses = {fr.abes.idsteptodeletetest.CommandLineJobRunner.class,fr.abes.idsteptodeletetest.JpaBatchConfigurer.class},
        exclude = { DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class,
                BatchAutoConfiguration.class}
                )

@EnableTransactionManagement
public class IdsteptodeletetestApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdsteptodeletetestApplication.class, args);
    }

}
*/





@SpringBootApplication(//scanBasePackages = {"fr.abes.idsteptodeletetest.JpaBatchConfigurer"},
        scanBasePackageClasses = fr.abes.idsteptodeletetest.JpaBatchConfigurer.class,
        exclude = { DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class,
                BatchAutoConfiguration.class}
)
@EnableTransactionManagement
public class IdsteptodeletetestApplication implements CommandLineRunner {

    /*@Autowired
    private MetricRegistry metricRegistry;


    @PostConstruct
    public void setUpHikariWithMetrics() {
        if(dataSource instanceof HikariDataSource) {
            ((HikariDataSource) dataSource).setMetricRegistry(metricRegistry);
        }*/

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;

    public static void main(String[] args) {
        SpringApplication.run(IdsteptodeletetestApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        /*JobParameters params = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(job, params);*/
        Map<String, JobParameter> params = new HashMap<>();
        params.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(params);
        JobExecution jobExecution = jobLauncher.run(job, jobParameters);
        while (jobExecution.isRunning()) {
            System.out.println(".......");
        }
        //return jobExecution.getStatus();

    }
}



/*@SpringBootApplication(//scanBasePackages = {"fr.abes.idsteptodeletetest.JpaBatchConfigurer"},
        scanBasePackageClasses = fr.abes.idsteptodeletetest.JpaBatchConfigurer.class,
        exclude = { DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class,
                BatchAutoConfiguration.class}
)
@EnableTransactionManagement
public class IdsteptodeletetestApplication implements ApplicationRunner, CommandLineRunner {


    @Value("${job.name}")
    private String jobName;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;

    public static void main(String[] args) {
        SpringApplication.run(IdsteptodeletetestApplication.class, args);
    }

    public void run( ApplicationArguments args ) throws Exception
    {
        System.out.println( "jobName: " + jobName );
    }

    @Override
    public void run(String... args) throws Exception {

        JobParameters params = new JobParametersBuilder()
                .addString(jobName, String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(job, params);

        //return jobExecution.getStatus();

    }
    }
    */


/*
@SpringBootApplication(//scanBasePackages = {"fr.abes.idsteptodeletetest.JpaBatchConfigurer"},
        scanBasePackageClasses = fr.abes.idsteptodeletetest.JpaBatchConfigurer.class,
        exclude = { DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class,
                BatchAutoConfiguration.class}
)
@EnableTransactionManagement
public class IdsteptodeletetestApplication implements CommandLineRunner {

    @Value("${job.name}")
    private String jobName;
    private static final Logger logger = LoggerFactory
            .getLogger(IdsteptodeletetestApplication.class);



    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    @Qualifier("idStepToDeleteJob")
    Job idStepToDeleteJob;

    @Autowired
    @Qualifier("uautrejob")
    Job unautrejob;

    public static void main(String[] args) {
        SpringApplication.run(fr.abes.theses.ThesesBatchApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(confMap);
        if (jobName.equals("diffuserThesesVersSudoc")) {
            try {
                jobLauncher.run(idStepToDeleteJob, jobParameters);
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
        }else{
            try {
                jobLauncher.run(unautrejob, jobParameters);
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }

        }*/
