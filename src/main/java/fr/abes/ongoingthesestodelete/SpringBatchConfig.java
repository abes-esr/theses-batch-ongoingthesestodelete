package fr.abes.ongoingthesestodelete;

import fr.abes.ongoingthesestodelete.portail.entities.DocumentPortail;
import fr.abes.ongoingthesestodelete.sujets.entities.DocumentSujets;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;


@Configuration
public class SpringBatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    JpaBatchConfigurer jpaBatchConfigurer;

    //--------------------attributs sujets job--------------------------------------
    @Autowired
    private ItemReader<DocumentSujets> documentSujetsToDeleteReader;
    @Autowired
    private ItemWriter<DocumentSujets> documentSujetsToDeleteWriter;
    @Autowired
    private ItemProcessor<DocumentSujets,DocumentSujets> documentSujetsToDeleteProcessor; //si on a un seul processor
    @Qualifier("sujetsDataSource")
    private DataSource dataSourceSujets;
    @Qualifier("sujetsDataSource")
    private ItemPreparedStatementSetter<DocumentSujets> setter;


    //--------------------attributs portail job--------------------------------------

    @Autowired
    private Tasklet documentPortailToDeleteDuplications;
    @Qualifier("portailDataSource")
    private DataSource dataSourcePortail;
    @Qualifier("portailDataSource")
    private ItemPreparedStatementSetter<DocumentPortail> setter2;


    //--------------------sujets job--------------------------------------
    @Bean("idStepToDeleteJobBean")
    public Job idStepToDeleteJob() {


        DataSource datasourceSujets;
        //JobRepository jb = jpaBatchConfigurer.getJobRepository();

        Step step1=stepBuilderFactory.get("step-delete-data")
                .<DocumentSujets,DocumentSujets>chunk(100)
                .reader(documentSujetsToDeleteReader)
                .processor(documentSujetsToDeleteProcessor) //si on a un seul processor
                .writer(documentSujetsToDeleteWriter)
                .faultTolerant()
                .retry(Exception.class)
                .retryLimit(6)
                .build();

        return jobBuilderFactory.get("idStepToDeleteJobName")
                .start(step1).build();
    }

    @Bean
    public FlatFileItemReader<DocumentSujets> flatFileItemReader(@Value("${inputFile}") Resource inputFile) {
        FlatFileItemReader<DocumentSujets>fileItemReader=new FlatFileItemReader<>();
        fileItemReader.setName(("FFIR1"));
        //fileItemReader.setLinesToSkip(1);
        fileItemReader.setResource(inputFile);
        fileItemReader.setLineMapper(lineMappe());
        return fileItemReader;
    }
    @Bean
    public LineMapper<DocumentSujets> lineMappe() {
        DefaultLineMapper<DocumentSujets> lineMapper=new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter("\r");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("iddoc");
        lineMapper.setLineTokenizer(lineTokenizer);
        BeanWrapperFieldSetMapper fieldSetMapper=new BeanWrapperFieldSetMapper();
        fieldSetMapper.setTargetType(DocumentSujets.class);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;

    }


    //--------------------portail job--------------------------------------

    @Bean("duplicatesNntPortailToDeleteJobBean")
    public Job duplicatesNntPortailToDeleteJob() {


        DataSource datasource;

        Step step1=stepBuilderFactory.get("duplicatesNnt-delete-data")
                .tasklet(documentPortailToDeleteDuplications)
                .build();

        return jobBuilderFactory.get("duplicatesNntPortailToDeleteJobName")
                .start(step1).build();
    }
}
