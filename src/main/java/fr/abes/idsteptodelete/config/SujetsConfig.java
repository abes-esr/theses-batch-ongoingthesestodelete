package fr.abes.idsteptodelete.config;

import fr.abes.idsteptodelete.MessageUsage;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;


@Configuration
//
@EnableJpaRepositories(
        basePackages = "fr.abes.idsteptodelete.sujets.repositories",
        entityManagerFactoryRef = "sujetsEntityManagerFactory",
        transactionManagerRef = "sujetsTransactionManager"
)
public class SujetsConfig
{
    @Autowired
    private Environment env;

    @Primary
    @Bean(name= "sujetsDb")
    @ConfigurationProperties(prefix="sujets.datasource")
    public DataSourceProperties sujetsDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name= "sujetsDataSource")
    public DataSource sujetsDataSource() {
        DataSourceProperties sujetsDataSourceProperties = sujetsDataSourceProperties();
        return DataSourceBuilder.create()
                .driverClassName(sujetsDataSourceProperties.getDriverClassName())
                .url(sujetsDataSourceProperties.getUrl())
                .username(sujetsDataSourceProperties.getUsername())
                .password(sujetsDataSourceProperties.getPassword())
                .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager sujetsTransactionManager()
    {
        EntityManagerFactory factory = sujetsEntityManagerFactory().getObject();
        return new JpaTransactionManager(factory);
    }

    //@Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean sujetsEntityManagerFactory()
    {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(sujetsDataSource());
        factory.setPackagesToScan(new String[]{"fr.abes.idsteptodelete.sujets.entities"});
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpaProperties = new Properties();
        //jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        jpaProperties.put("hibernate.show-sql", env.getProperty("spring.jpa.show-sql"));
        factory.setJpaProperties(jpaProperties);

        return factory;
    }

    @Primary
    @Bean
    public DataSourceInitializer sujetsDataSourceInitializer()
    {
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(sujetsDataSource());
        /*ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.addScript(new ClassPathResource("sujets-data.sql"));
        dataSourceInitializer.setDatabasePopulator(databasePopulator);*/
        dataSourceInitializer.setEnabled(env.getProperty("sujets.datasource.initialize", Boolean.class, false));
        return dataSourceInitializer;
    }









/*
    @Autowired private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;
    @Autowired private ItemProcessor<DocumentSujets,DocumentSujets> documentSujetsToDeleteProcessor; //si on a un seul processor
    @Qualifier("sujetsDataSource")
    private DataSource dataSource;
    @Qualifier("sujetsDataSource")
    private ItemPreparedStatementSetter<DocumentSujets> setter;


    public ItemReader documentSujetsToDeleteReader(@Qualifier("sujetsDb") final DataSource dataSource) {
        JdbcCursorItemReader<DocumentSujets> reader = new JdbcCursorItemReader<>();

        return documentSujetsToDeleteReader(dataSource);
    }

    public ItemWriter documentSujetsToDeleteWriter(@Qualifier("sujetsDb") final DataSource dataSource,
                                                   ItemPreparedStatementSetter<DocumentSujets> setter) {
        JdbcBatchItemWriter<MessageUsage> writer = new JdbcBatchItemWriter<>();

        String INSERT_QUERY = "INSERT query";

        writer.setSql(INSERT_QUERY);
        writer.setDataSource(dataSource);
        return documentSujetsToDeleteWriter(dataSource, setter);
    }

    @Bean
    public Job idStepToDeleteJob() {


        DataSource datasource;

        Step step1=stepBuilderFactory.get("step-load-data")
                .<DocumentSujets,DocumentSujets>chunk(100)
                .reader(documentSujetsToDeleteReader(dataSource))
                .processor(documentSujetsToDeleteProcessor) //si on a un seul processor
                //.processor(compositeItemProcessor()) composite donc plusieurs processor
                .writer(documentSujetsToDeleteWriter(dataSource, setter ))
                .build();

        return jobBuilderFactory.get("idStepToDelete-job")
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
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("iddoc");
        lineMapper.setLineTokenizer(lineTokenizer);
        BeanWrapperFieldSetMapper fieldSetMapper=new BeanWrapperFieldSetMapper();
        fieldSetMapper.setTargetType(DocumentSujets.class);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;

    }*/



}