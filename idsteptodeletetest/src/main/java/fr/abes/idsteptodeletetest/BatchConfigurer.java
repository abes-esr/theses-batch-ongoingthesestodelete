package fr.abes.idsteptodeletetest;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class BatchConfigurer extends DefaultBatchConfigurer implements ApplicationRunner {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    ApplicationContext ctx;

    public BatchConfigurer(){}

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<String> jobListFromTerminal = args.getNonOptionArgs();
        for(String jobName:jobListFromTerminal) {
            try {
                JobExecution jobExecution = createJobExecution(jobName);
            } catch (Exception e) {
                e.printStackTrace();
            }
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



}