package fr.abes.idsteptodelete;


import fr.abes.idsteptodelete.sujets.entities.DocumentSujets;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
    @Autowired private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;
    @Autowired private ItemReader<DocumentSujets> documentSujetsToDeleteReader;
    @Autowired private ItemWriter<DocumentSujets> documentSujetsToDeleteWriter;
    @Autowired private ItemProcessor<DocumentSujets,DocumentSujets> documentSujetsToDeleteProcessor; //si on a un seul processor
    @Qualifier("sujetsDataSource")
    private DataSource dataSource;
    @Qualifier("sujetsDataSource")
    private ItemPreparedStatementSetter<DocumentSujets> setter;


   /* public ItemReader documentSujetsToDeleteReader(@Qualifier("sujetsDb") final DataSource dataSource) {
        JdbcCursorItemReader<DocumentSujets> reader = new JdbcCursorItemReader<>();

        //String FETCH_SQL_QUERY = " SELECT query"
        //reader.setSql(FETCH_SQL_QUERY);
        //reader.setDataSource(dataSource);
        // more code
        return documentSujetsToDeleteReader(dataSource);
    }

    public ItemWriter documentSujetsToDeleteWriter(@Qualifier("sujetsDb") final DataSource dataSource,
                                 ItemPreparedStatementSetter<DocumentSujets> setter) {
        JdbcBatchItemWriter<MessageUsage> writer = new JdbcBatchItemWriter<>();

        String INSERT_QUERY = "INSERT query";

        writer.setSql(INSERT_QUERY);
        writer.setDataSource(dataSource);
        return documentSujetsToDeleteWriter(dataSource, setter);
    }*/

    @Bean
    public Job idStepToDeleteJob() {


        DataSource datasource;

        Step step1=stepBuilderFactory.get("step-delete-data")
                .<DocumentSujets,DocumentSujets>chunk(100)
                //.reader(documentSujetsToDeleteReader(dataSource))
                .reader(documentSujetsToDeleteReader)
                .processor(documentSujetsToDeleteProcessor) //si on a un seul processor
                //.processor(compositeItemProcessor()) composite donc plusieurs processor
                .writer(documentSujetsToDeleteWriter)
                //.writer(documentSujetsToDeleteWriter(dataSource, setter ))
                .build();

        return jobBuilderFactory.get("idStepToDelete-job")
                .start(step1).build();
    }


    /*@Bean
    public ItemProcessor<DocumentSujets,DocumentSujets> compositeItemProcessor(){
        List<ItemProcessor<DocumentSujets,DocumentSujets>> itemProcessors=new ArrayList<>();
        itemProcessors.add(itemProcessor1());
        itemProcessors.add(itemProcessor2());

        CompositeItemProcessor<DocumentSujets,DocumentSujets> compositeItemProcessor=new
                CompositeItemProcessor<>();
        compositeItemProcessor.setDelegates(itemProcessors);
        return compositeItemProcessor;
    }*/


    /*soit on declare ici une methode qui retourne le bean avec @Bean, soit on cree une classe qui implemente
    l'interface ItemProcessor + @Component

    public ItemProcessor<BankTransaction,BankTransaction> itemProcessor() {
        return new ItemProcessor<BankTransaction, BankTransaction>() {
            @Override
            public BankTransaction process(BankTransaction bankTransaction) throws Exception {
                return null;
            }
        }
    }*/


/*
    @Bean
    DocumentSujetsToDeleteProcessor itemProcessor1(){
        return new DocumentSujetsToDeleteProcessor();
    }

    @Bean
    BankTransactionItemAnalyticsProcessor itemProcessor2(){
        return new BankTransactionItemAnalyticsProcessor();
    }
*/
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
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("iddoc");
        lineMapper.setLineTokenizer(lineTokenizer);
        BeanWrapperFieldSetMapper fieldSetMapper=new BeanWrapperFieldSetMapper();
        fieldSetMapper.setTargetType(DocumentSujets.class);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;

    }

}
