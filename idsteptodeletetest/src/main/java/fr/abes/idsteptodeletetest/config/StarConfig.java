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

/**
 * @author Ramesh Fadatare
 *
 */
@Configuration
@EnableJpaRepositories(
        basePackages = "fr.abes.idsteptodeletetest.star.repositories",
        entityManagerFactoryRef = "starEntityManagerFactory",
        transactionManagerRef = "starTransactionManager"
)
public class StarConfig {

    @Autowired
    private Environment env;

    @Bean
    @ConfigurationProperties(prefix = "star.datasource")
    public DataSourceProperties starDataSourceProperties() {
        return new DataSourceProperties();
    }

    /*@Bean
    public DataSource starDataSource() {
        DataSourceProperties primaryDataSourceProperties = starDataSourceProperties();
        return DataSourceBuilder.create()
                .driverClassName(primaryDataSourceProperties.getDriverClassName())
                .url(primaryDataSourceProperties.getUrl())
                .username(primaryDataSourceProperties.getUsername())
                .password(primaryDataSourceProperties.getPassword())
                .build();
    }*/

    @Bean
    public DataSource starDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(env.getProperty("star.datasource.driver-class-name"));
        //config.setDataSourceClassName(env.getProperty("star.datasource.class-name"));
        config.setJdbcUrl(env.getProperty("star.datasource.url"));
        config.setUsername(env.getProperty("star.datasource.username"));
        config.setPassword(env.getProperty("star.datasource.password"));
        //Frequently used
        config.setAutoCommit(false);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(35000);
        config.setMaxLifetime(45000);
        config.setMinimumIdle(1);
        config.setMaximumPoolSize(2);
        config.getMetricsTrackerFactory();
        config.getMetricRegistry();
        config.getHealthCheckProperties();
        config.setPoolName("poolStarOnGoingToDelete");
        //Infrequently used
        config.setInitializationFailTimeout(0);
        //config.setIsolateInternalQueries(true);
        config.setAllowPoolSuspension(true);
        config.setLeakDetectionThreshold(40000);
        config.setValidationTimeout(2500);
        config.setConnectionTestQuery("SELECT 1 FROM DUAL");

        //ne marche pas avec la classe
        //config.addDataSourceProperty("validationInterval", env.getProperty("star.datasource.validationInterval"));
        //config.addDataSourceProperty("testOnBorrow", env.getProperty("star.datasource.testOnBorrow"));
        //config.addDataSourceProperty("testWhileIdle", env.getProperty("star.datasource.testWhileIdle"));
        //config.addDataSourceProperty("testOnReturn", env.getProperty("star.datasource.testOnReturn"));
        //config.addDataSourceProperty("timeBetweenEvictionRunsMillis", env.getProperty("star.datasource.timeBetweenEvictionRunsMillis"));
        //config.addDataSourceProperty("validationQuery", env.getProperty("star.datasource.validationQuery"));
        config.addDataSourceProperty("implicitCachingEnabled", "true"); //spec oracle
        config.addDataSourceProperty("maxStatements", "250"); //spec oracle
        //config.addDataSourceProperty("prepStmtCacheSize", "250");
        //config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        return new HikariDataSource(config);

    }

    @Bean
    public PlatformTransactionManager starTransactionManager() {
        EntityManagerFactory factory = starEntityManagerFactory().getObject();
        return new JpaTransactionManager(factory);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean starEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(starDataSource());
        factory.setPackagesToScan(new String[] {
                "fr.abes.idsteptodeletetest.star.entities"
        });
        factory.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Properties jpaProperties = new Properties();
        //jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
        jpaProperties.put("hibernate.show-sql", env.getProperty("spring.jpa.show-sql"));
        factory.setJpaProperties(jpaProperties);

        return factory;

    }

    @Bean
    public DataSourceInitializer starDataSourceInitializer() {
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(starDataSource());
        /*ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.addScript(new ClassPathResource("star-data.sql"));
        dataSourceInitializer.setDatabasePopulator(databasePopulator);*/
        dataSourceInitializer.setEnabled(env.getProperty("star.datasource.initialize", Boolean.class, false));
        return dataSourceInitializer;
    }
}