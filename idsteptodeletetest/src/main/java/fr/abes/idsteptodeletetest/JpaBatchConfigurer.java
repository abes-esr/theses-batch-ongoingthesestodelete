



package fr.abes.idsteptodeletetest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.BatchConfigurationException;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.configuration.ListableJobLocator;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.MapJobRegistry;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.explore.support.MapJobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.autoconfigure.batch.JobLauncherCommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.SyncTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Component("JpaBatchConfigurer")
public class JpaBatchConfigurer implements BatchConfigurer {
    private static final Logger logger = LoggerFactory
            .getLogger(JpaBatchConfigurer.class);

    @Inject
    private DataSource dataSource;

    @Inject
    private PlatformTransactionManager transactionManager;

    private JobRepository jobRepository;
    private JobLauncher jobLauncher;
    private JobExplorer jobExplorer;
    private ListableJobLocator listableJobLocator;
    private JobExecution jobExecution;

    @Autowired
    private ApplicationArguments applicationArguments;


    protected JpaBatchConfigurer () {}

    public JpaBatchConfigurer(Job job, JobLauncher jobLauncher, DataSource dataSource) {}


    @Override
    @Bean(name="jobrepository")
    public JobRepository getJobRepository() {
        return jobRepository;
    }

    @Override
    @Bean
    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    @Override
    @Bean(name="joblauncher")
    public JobLauncher getJobLauncher() {
        return jobLauncher;
    }

    @Override
    @Bean(name="jobexplorer")
    public JobExplorer getJobExplorer() {
        return jobExplorer;
    }

    @Bean
    public ListableJobLocator getListableJobLocator() {
        return listableJobLocator;
    }

    @Bean(name="applicationArguments")
    public ApplicationArguments getApplicationArguments() {
        return applicationArguments;
    }

    @Bean(name="jobExecution")
    public JobExecution getJobExecution() {
        return jobExecution;
    }

    @Autowired
    ApplicationContext ctx;

    @PostConstruct
    public void initialize() {
        try {
            if (dataSource == null) {
                logger.warn("No datasource was provided...using a Map based JobRepository");

                if (this.transactionManager == null) {
                    this.transactionManager = new ResourcelessTransactionManager();
                }

                MapJobRepositoryFactoryBean jobRepositoryFactory = new MapJobRepositoryFactoryBean(
                        this.transactionManager);
                jobRepositoryFactory.afterPropertiesSet();
                this.jobRepository = jobRepositoryFactory.getObject();

                MapJobExplorerFactoryBean jobExplorerFactory = new MapJobExplorerFactoryBean(
                        jobRepositoryFactory);
                jobExplorerFactory.afterPropertiesSet();
                this.jobExplorer = jobExplorerFactory.getObject();
            } else {
                this.jobRepository = createJobRepository();

                JobExplorerFactoryBean jobExplorerFactoryBean = new JobExplorerFactoryBean();
                jobExplorerFactoryBean.setDataSource(this.dataSource);
                jobExplorerFactoryBean.afterPropertiesSet();
                this.jobExplorer = jobExplorerFactoryBean.getObject();
            }
            this.jobLauncher = createJobLauncher();
            List<String> jobListFromTerminal = applicationArguments.getNonOptionArgs();
            for(String jobName:jobListFromTerminal) {
                this.jobExecution = createJobExecution(jobName);
            }

        } catch (Exception e) {
            throw new BatchConfigurationException(e);
        }
    }

    public JobExecution createJobExecution(String jobName) throws Exception {
        Job job = (Job) ctx.getBean(jobName);
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        JobExecution execution = jobLauncher.run(job, jobParameters);
        System.out.println("Exit Status : " + execution.getStatus());
        System.out.println("Exit Status : " + execution.getAllFailureExceptions());
        return execution;
    }

    private JobLauncher createJobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor( new SimpleAsyncTaskExecutor());
        //jobLauncher.setTaskExecutor( new SyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    protected JobRepository createJobRepository() throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setIsolationLevelForCreate("ISOLATION_SERIALIZABLE");
        //factory.setIsolationLevelForCreate("ISOLATION_DEFAULT");
        factory.setDataSource(dataSource);
        factory.setTransactionManager(transactionManager);
        factory.setValidateTransactionState(false);
        factory.afterPropertiesSet();
        return factory.getObject();
    }


    @Bean
    public JobBuilderFactory jobBuilderFactory(JobRepository jobRepository){
        return new JobBuilderFactory(jobRepository);
    }

    @Bean
    public StepBuilderFactory stepBuilderFactory(JobRepository jobRepository, PlatformTransactionManager transactionManager){
        return new StepBuilderFactory(jobRepository, transactionManager);
    }
    @Bean("joblocator")
    public JobLocator jobLocator() {
        return new MapJobRegistry();
    }





/* good but not allowing to get a list of job; we can only lauch one job; in this case, the log output says : skipped thisjob, and this job...
    the JobLauncherCommandLineRunner uses different objects ( the JobRegistry map so it is necessary to register the job before using it)
    @Bean("jobLauncherCommandLineRunner")
    public JobLauncherCommandLineRunner jobLauncherCommandLineRunner(JobLauncher jobLauncher, JobExplorer jobExplorer) throws JobExecutionException {
        JobLauncherCommandLineRunner runner = new JobLauncherCommandLineRunner(jobLauncher, jobExplorer, jobRepository);
        ApplicationArguments a = ctx.getBean(ApplicationArguments.class);
        List<String> jobList = a.getNonOptionArgs();
        logger.info("jobList = " + jobList.toString());
        for(String jobName:jobList) {
            logger.info("jobName = " + jobName);
            runner.setJobNames(jobName);
            //runner.run(jobName);
            return runner;
        }return null;
    }*/






}



