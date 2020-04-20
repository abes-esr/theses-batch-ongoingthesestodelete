
package fr.abes.testrestcontrollerinterface;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController

public class JobRestController {


    @Value("${pathJar}")
    private String pathJar;
    private JarExecutor jarExecutor;
    private List<String> args;

    private static final Logger logger = LoggerFactory
            .getLogger(JobRestController.class);


    @GetMapping("/startJobIdStepToDelete")
    public void startJobIdStepToDelete() throws Exception {

        /*if we want to use the config from the jar instead of executing the jar from the outside (runtime.exec)
        with this manner, we are not free from the jpabatchconfigurer, and so, we open a connection to the bdd
        so to implements this method, it's necessary to atowire the Applicationcontext Bean
        it's also necessary to scan in the application class the package containing jpabatchconfigurer
        and obviously, the pom must declare a dependency the jar
        JpaBatchConfigurer jpaBatchConfigurer = (JpaBatchConfigurer) ctx.getBean("JpaBatchConfigurer");
        jobExecution = jpaBatchConfigurer.createJobExecution("idStepToDeleteJobBean");
        */

        /*same observations as above
        JobLauncherCommandLineRunner runner = (JobLauncherCommandLineRunner) ctx.getBean("jobLauncherCommandLineRunner");
        runner.run("idStepToDeleteJob");*/

        args = new ArrayList<String>();
        args.add("idStepToDeleteJobBean");
        jarExecutor = new JarExecutor();
        jarExecutor.executeJar(pathJar, args );

    }

    @GetMapping("/startUnAutreJob")
    public void startUnAutreJob() throws Exception {

        args = new ArrayList<String>();
        args.add("unautrejobBean");
        jarExecutor = new JarExecutor();
        jarExecutor.executeJar(pathJar, args );

    }

    @GetMapping("/startTheTwoJobs")
    public void startTheTwoJobs() throws Exception {

        args = new ArrayList<String>();
        args.add("idStepToDeleteJobBean");
        args.add("unautrejobBean");
        jarExecutor = new JarExecutor();
        jarExecutor.executeJar(pathJar, args );

    }


}

