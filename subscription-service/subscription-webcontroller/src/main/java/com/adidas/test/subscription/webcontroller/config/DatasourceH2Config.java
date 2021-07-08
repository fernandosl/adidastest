package com.adidas.test.subscription.webcontroller.config;

import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.adidas.test.subscription.domain.repository"},
    entityManagerFactoryRef = "subscriptionEntityManagerFactory",
    transactionManagerRef = "subscriptionTransactionManager")
//@PropertySource({"classpath:/application-h2.properties"})
public class DatasourceH2Config extends JPADatabaseConfigBase {

  private static final Logger LOG = LoggerFactory.getLogger(DatasourceH2Config.class.getName());

  private final static String[] ENTITIES_TO_SCAN = new String[]{
      "com.adidas.test.subscription.domain.entity" };

  @Autowired
  private Environment env;

  private String dbCode = "subscription-h2";

  @Bean
  public DataSource subscriptionDataSource() {
    LOG.info("H2 Init DataSource starting");
    DataSource dataSource = null;
    try {
      String prop = env.getProperty("spring." + dbCode + ".datasource.driverClassName");
      dataSource = super.createPooledDataSource(dbCode);
    } catch (Exception e) {
      LOG.error("Error configurating H2 Database", e);
      e.printStackTrace();
    }
    LOG.info("H2 Init DataSource finished");
    return dataSource;
  }

  @Primary
  @Bean(name = "subscriptionEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean subscriptionEntityManagerFactory() {
    DataSource dataSource = subscriptionDataSource();

    return super.createLocalContainerEntityManagerFactoryBean(dbCode, dataSource, ENTITIES_TO_SCAN);
  }

  @Primary
  @Bean
  public PlatformTransactionManager subscriptionTransactionManager(
      final @Qualifier("subscriptionEntityManagerFactory") LocalContainerEntityManagerFactoryBean subscriptionEntityManagerFactory) {
    return new JpaTransactionManager(subscriptionEntityManagerFactory.getObject());

  }

}