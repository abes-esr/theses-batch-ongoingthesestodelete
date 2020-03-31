package fr.abes.testrestcontrollerinterface;

import fr.abes.idsteptodeletetest.JpaBatchConfigurer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@ComponentScan(basePackages = "fr.abes.idsteptodeletetest")

public class JobRestController {

    private JobLauncher jobLauncher;

    @Autowired
    ApplicationContext ctx;

    private JpaBatchConfigurer jpaBatchConfigurer;


    /*@GetMapping("/startJobIdStepToDelete")
    public BatchStatus load() throws Exception{

        Map<String, JobParameter> params = new HashMap<>();
        params.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(params);
        JobExecution jobExecution=jobLauncher.run(job,jobParameters);
        while(jobExecution.isRunning()){
            System.out.println(".......");
        }
        return jobExecution.getStatus();
    }*/
    @GetMapping("/startJobIdStepToDelete")
    public void load() throws Exception {
        jpaBatchConfigurer = (JpaBatchConfigurer) ctx.getBean("JpaBatchConfigurer");
        jobLauncher = jpaBatchConfigurer.getJobLauncher();
        Job job = (Job) ctx.getBean("idStepToDeleteJob");
        JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        JobExecution execution = jobLauncher.run(job, jobParameters);
        System.out.println("Exit Status : " + execution.getStatus());
        System.out.println("Exit Status : " + execution.getAllFailureExceptions());

    }

}
