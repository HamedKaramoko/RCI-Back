/**
 * 
 */
package ci.projects.rci.config.datasource;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author hkaramok
 *
 */
@Configuration
@PropertySource("classpath:data-source.properties")
@EnableTransactionManagement
public class DataSourceConfig {

	@Autowired
	private Environment env;

	// For embedded database
	/*@Bean
	public DataSource dataSource() {
		return new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:schema.sql")
				//.addScript("classpath:test-data.sql")
				.build();
	}*/

	/*@Bean
	public DataSource h2DataSource() {
		BasicDataSource bds = new BasicDataSource();
		bds.setDriverClassName(env.getProperty("h2.embedded.driver_class_name"));
		bds.setUrl(env.getProperty("jdbc:h2:tcp://localhost/~/RCI_DB"));
		bds.setUsername(env.getProperty("h2.embedded.username"));
		bds.setPassword(env.getProperty("h2.embedded.password"));
		bds.setInitialSize(5);
		bds.setMaxActive(10);
		return bds;
	}*/

	@Bean
	public DataSource mySqlDataSource() {
		BasicDataSource bds = new BasicDataSource();
		bds.setDriverClassName(env.getProperty("mysql.datasource.driver-class-name"));
		bds.setUrl(env.getProperty("mysql.datasource.url"));
		bds.setUsername(env.getProperty("mysql.datasource.username"));
		bds.setPassword(env.getProperty("mysql.datasource.password"));
		bds.setInitialSize(5);
		bds.setMaxActive(10);
		return bds;
	}

	private Properties getJpaProperties(){
		Properties jpaProperties = new Properties();
		jpaProperties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		return jpaProperties;
	}

	@Bean
	public JpaVendorAdapter mySqlJpaVendorAdapter() {
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setDatabase(Database.MYSQL);
		adapter.setShowSql(true);
		adapter.setGenerateDdl(false);
		adapter.setDatabasePlatform("org.hibernate.dialect.MySQL5InnoDBDialect");
		return adapter;
	}

	/*@Bean
	//@Profile("DEV")
	public JpaVendorAdapter h2JpaVendorAdapter() {
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setDatabase(Database.H2);
		adapter.setShowSql(true);
		adapter.setGenerateDdl(false);
		adapter.setDatabasePlatform("org.hibernate.dialect.H2Dialect");
		return adapter;
	}*/

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
		LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
		emfb.setDataSource(dataSource);
		emfb.setJpaVendorAdapter(jpaVendorAdapter);
		emfb.setJpaProperties(getJpaProperties());
		emfb.setPackagesToScan("ci.projects.rci.model");
		return emfb;
	}

	// Allow the translation of JPA or whatever orm chosen exception into a spring exception.
	// It allows to keep the exception management decoupled from any orm tool.
	@Bean
	public BeanPostProcessor persistenceTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	@Bean
	public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean lcemfb){
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(lcemfb.getObject());
		return transactionManager;
	}
}
