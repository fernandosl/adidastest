package com.adidas.test.subscription.webcontroller.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * Base class for providing database connections. Provides common behaviour for child modules wich
 * need database connections.
 * <p>
 * This class can provide two different types of connections attending to the origin of them. First
 * connections are container connections, these are configured in the Application Server and
 * obtained via JNDI resources. Second connections are generated from connection data and provided
 * via Hikari pool datasource, these are needed when application is started standalone, for example
 * like a Spring Boot executable.
 */
public abstract class JPADatabaseConfigBase {

  @Autowired
  private Environment env;

  /**
   * Creates a database connection from JNDI resource
   *
   * @param dbCode
   * @return
   * @throws NamingException
   */
  protected DataSource getJndiContainerDataSource(final String dbCode) throws NamingException {
    String datasourceJndiName = env.getProperty("spring." + dbCode + ".datasource.jndi");
    return (DataSource) new JndiTemplate().lookup(datasourceJndiName);
  }

  /**
   * Creates a pooled data source from connection data, the connection is created with hikari
   * datasource for enabling enhaced database performance
   *
   * @param dbCode
   * @return
   */
  protected DataSource createPooledDataSource(final String dbCode) {
    DataSource dataSource = null;

    HikariConfig configuration = new HikariConfig();
    configuration
        .setDriverClassName(env.getProperty("spring." + dbCode + ".datasource.driverClassName"));
    configuration.setJdbcUrl(env.getProperty("spring." + dbCode + ".datasource.url"));
    configuration.setUsername(env.getProperty("spring." + dbCode + ".datasource.username"));
    String password = env.getProperty("spring." + dbCode + ".datasource.password");
    if (password != null) {
      configuration.setPassword(password);
      password = null;
    }

    String idleTimeout = env.getProperty("spring." + dbCode + ".datasource.minimum-idle");
    if (idleTimeout != null) {
      configuration.setIdleTimeout(Long.parseLong(idleTimeout));
    }
    String connectionTimeout = env
        .getProperty("spring" + dbCode + ".datasource.connection-timeout");
    if (connectionTimeout != null) {
      configuration.setConnectionTimeout(Long.parseLong(connectionTimeout));
    }
    String minimumIdle = env.getProperty("spring." + dbCode + ".datasource.idle-timeout");
    if (minimumIdle != null) {
      configuration.setMinimumIdle(Integer.parseInt(minimumIdle));
    }
    String maximumPoolSize = env.getProperty("spring." + dbCode + ".datasource.max-poolsize");
    if (maximumPoolSize != null) {
      configuration.setMaximumPoolSize(Integer.parseInt(maximumPoolSize));
    }
    String maxLifeTime = env.getProperty("spring." + dbCode + ".datasource.max-lifetime");
    if (maxLifeTime != null) {
      configuration.setMaxLifetime(Integer.parseInt(maxLifeTime));
    }
    String autoCommit = env.getProperty("spring." + dbCode + ".datasource.autocommit");
    if ("true".equalsIgnoreCase(autoCommit)) {
      configuration.setAutoCommit(Boolean.TRUE);
    } else {
      configuration.setAutoCommit(Boolean.FALSE);
    }
    dataSource = new HikariDataSource(configuration);

    return dataSource;
  }

  /**
   * Creates specific configuration for connection from values stored in .properties file. This is
   * used to customize and make fine configuration of the needed connections.
   *
   * @param dbCode
   * @param dataSource
   * @param packagesToScan
   * @return
   */
  protected LocalContainerEntityManagerFactoryBean createLocalContainerEntityManagerFactoryBean(
      final String dbCode, final DataSource dataSource, String[] packagesToScan
  ) {
    LocalContainerEntityManagerFactoryBean em
        = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource);
    em.setPackagesToScan(packagesToScan);

    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    String showSQL = env.getProperty("spring" + dbCode + "hibernate.show-sql");
    if ("true".equalsIgnoreCase(showSQL)) {
      vendorAdapter.setShowSql(Boolean.TRUE);
    }
    vendorAdapter.getJpaPropertyMap().put("logging.level.org.hibernate.SQL", "debug");
    vendorAdapter.getJpaPropertyMap()
        .put("logging.level.org.hibernate.type.descriptor.sql", "trace");

    String generateDDL = env.getProperty("spring." + dbCode + ".hibernate.generate-ddl");
    if ("true".equalsIgnoreCase(generateDDL)) {
      vendorAdapter.setGenerateDdl(Boolean.TRUE);
    }
    vendorAdapter.setGenerateDdl(Boolean.TRUE);
    String database = env.getProperty("spring." + dbCode + ".hibernate.database");
    if (database != null) {
      vendorAdapter.setDatabase(Database.valueOf(database));
    }
    String databasePlatform = env.getProperty("spring." + dbCode + ".hibernate.database-platform");
    if (databasePlatform != null) {
      vendorAdapter.setDatabasePlatform(databasePlatform);
    }
    em.setJpaVendorAdapter(vendorAdapter);
    HashMap<String, Object> properties = new HashMap<>();

    //properties.put("spring.jpa.hibernate.ddl-auto", "update");
    properties.put("hibernate.hbm2ddl.auto",
        env.getProperty("spring." + dbCode + ".hibernate.hbm2ddl.auto"));
    properties.put("hibernate.dialect", env.getProperty("spring." + dbCode + ".hibernate.dialect"));
    properties
        .put("hibernate.format_sql", env.getProperty("spring." + dbCode + ".hibernate.format_sql"));
    properties
        .put("hibernate.show_sql", env.getProperty("spring." + dbCode + ".hibernate.show_sql"));
    properties.put("hibernate.use_sql_comments",
        env.getProperty("spring." + dbCode + ".hibernate.use_sql_comments"));

    em.setJpaPropertyMap(properties);
    em.setJpaVendorAdapter(vendorAdapter);

    return em;
  }
}