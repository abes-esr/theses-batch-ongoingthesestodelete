package fr.abes.testrestcontrollerinterface;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication(//scanBasePackages = {"fr.abes.idsteptodeletetest.JpaBatchConfigurer"},
        //scanBasePackageClasses = fr.abes.idsteptodeletetest.JpaBatchConfigurer.class,
        exclude = { DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class,
        BatchAutoConfiguration.class})
@EnableTransactionManagement

public class TestrestcontrollerinterfaceApplication  {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(TestrestcontrollerinterfaceApplication.class, args);
        //SpringApplication.run(TestrestcontrollerinterfaceApplication.class, args);
    }







/*
    @SpringBootApplication(//scanBasePackages = {"fr.abes.idsteptodeletetest.JpaBatchConfigurer"},
            scanBasePackageClasses = fr.abes.idsteptodeletetest.JpaBatchConfigurer.class,
            exclude = { DataSourceAutoConfiguration.class,
                    HibernateJpaAutoConfiguration.class,
                    DataSourceTransactionManagerAutoConfiguration.class})
    @EnableTransactionManagement
    public class TestrestcontrollerinterfaceApplication implements CommandLineRunner {



    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;

    public static void main(String[] args)
    {
        SpringApplication.run(TestrestcontrollerinterfaceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception
    {
        *//*JobParameters params = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        jobLauncher.run(job, params);*//*
        Map<String, JobParameter> params = new HashMap<>();
        params.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(params);
        JobExecution jobExecution=jobLauncher.run(job,jobParameters);
        while(jobExecution.isRunning()){
            System.out.println(".......");
        }
        //return jobExecution.getStatus();

    }*/
}


