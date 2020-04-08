package fr.abes.idsteptodeletetest.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
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
@EnableJpaRepositories(
        basePackages = "fr.abes.idsteptodeletetest.portail.repositories",
        entityManagerFactoryRef = "portailEntityManagerFactory",
        transactionManagerRef = "portailTransactionManager"
)
public class PortailConfig
{
    @Autowired
    private Environment env;

    @Bean
    @ConfigurationProperties(prefix="portail.datasource")
    public DataSourceProperties portailDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource portailDataSource() {
        DataSourceProperties portailDataSourceProperties = portailDataSourceProperties();
        return DataSourceBuilder.create()
                .driverClassName(portailDataSourceProperties.getDriverClassName())
                .url(portailDataSourceProperties.getUrl())
                .username(portailDataSourceProperties.getUsername())
                .password(portailDataSourceProperties.getPassword())
                .build();
    }

    /*@Bean
    public DataSource portailDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(env.getProperty("portail.datasource.url"));
        config.setUsername(env.getProperty("portail.datasource.username"));
        config.setPassword(env.getProperty("portail.datasource.password"));
        config.setDriverClassName(env.getProperty("portail.datasource.driver-class-name"));
        config.setIdleTimeout(230000);
        config.setMaxLifetime(240000);
        config.setInitializationFailTimeout(0);
        config.setMinimumIdle(2);
        config.setAllowPoolSuspension(true);
        config.setConnectionTimeout(30000);
        config.getMetricsTrackerFactory();
        config.getMetricRegistry();
        config.setMaximumPoolSize(5);
        *//*config.setDataSourceProperties(portailDataSourceProperties);
        portailDataSourceProperties.setProperty("validationInterval", env.getProperty("portail.datasource.validationInterval"));
        portailDataSourceProperties.setProperty("testOnBorrow", env.getProperty("portail.datasource.testOnBorrow"));
        portailDataSourceProperties.setProperty("testWhileIdle", env.getProperty("portail.datasource.testWhileIdle"));
        portailDataSourceProperties.setProperty("testOnReturn", env.getProperty("portail.datasource.testOnReturn"));
        portailDataSourceProperties.setProperty("timeBetweenEvictionRunsMillis", env.getProperty("portail.datasource.timeBetweenEvictionRunsMillis"));
        portailDataSourceProperties.setProperty("validationQuery", env.getProperty("portail.datasource.validationQuery"));*//*

        config.addDataSourceProperty("validationInterval", env.getProperty("portail.datasource.validationInterval"));
        config.addDataSourceProperty("testOnBorrow", env.getProperty("portail.datasource.testOnBorrow"));
        config.addDataSourceProperty("testWhileIdle", env.getProperty("portail.datasource.testWhileIdle"));
        config.addDataSourceProperty("testOnReturn", env.getProperty("portail.datasource.testOnReturn"));
        config.addDataSourceProperty("timeBetweenEvictionRunsMillis", env.getProperty("portail.datasource.timeBetweenEvictionRunsMillis"));
        config.addDataSourceProperty("validationQuery", env.getProperty("portail.datasource.validationQuery"));
        //config.addDataSourceProperty("cachePrepStmts", "true");
        //config.addDataSourceProperty("prepStmtCacheSize", "250");
        //config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        return new HikariDataSource(config);

    }*/

    @Bean
    public PlatformTransactionManager portailTransactionManager()
    {
        EntityManagerFactory factory = portailEntityManagerFactory().getObject();
        return new JpaTransactionManager(factory);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean portailEntityManagerFactory()
    {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(portailDataSource());
        factory.setPackagesToScan(new String[]{"fr.abes.idsteptodeletetest.portail.entities"});
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpaProperties = new Properties();
        //jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        jpaProperties.put("hibernate.show-sql", env.getProperty("spring.jpa.show-sql"));
        factory.setJpaProperties(jpaProperties);

        return factory;
    }

    @Bean
    public DataSourceInitializer portailDataSourceInitializer()
    {
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(portailDataSource());
        /*ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.addScript(new ClassPathResource("portail-data.sql"));
        dataSourceInitializer.setDatabasePopulator(databasePopulator);*/
        dataSourceInitializer.setEnabled(env.getProperty("portail.datasource.initialize", Boolean.class, false));
        return dataSourceInitializer;
    }
}