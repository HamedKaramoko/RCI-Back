/**
 * 
 */
package ci.projects.rci.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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

/**
 * @author hkaramok
 *
 */
@Configuration
@PropertySource("classpath:data-source.properties")
public class DataSourceConfig {

	@Autowired
	private Environment env;

	// For embedded database
	@Bean(destroyMethod="shutdown")
	@Profile("TEST")
	public DataSource dataSource() {
		return new EmbeddedDatabaseBuilder()
				.setType(EmbeddedDatabaseType.H2)
				.addScript("classpath:schema.sql")
				//.addScript("classpath:test-data.sql")
				.build();
	}

	@Bean
	@Profile("DEV")
	public DataSource DataSourceDEV() {
		BasicDataSource bds = new BasicDataSource();
		bds.setDriverClassName("org.h2.Driver");
		bds.setUrl("jdbc:h2:tcp://localhost/~/RCI_DB;DB_CLOSE_DELAY=-1");
		bds.setUsername("sa");
		bds.setPassword("");
		bds.setInitialSize(5);
		bds.setMaxActive(10);
		return bds;
	}

	@Bean
	@Profile("QA")
	public DataSource DataSourceQA() {
		BasicDataSource bds = new BasicDataSource();
		bds.setDriverClassName(env.getProperty("mysql.datasource.driver-class-name"));
		bds.setUrl(env.getProperty("mysql.datasource.url"));
		bds.setUsername(env.getProperty("mysql.datasource.username"));
		bds.setPassword(env.getProperty("mysql.datasource.password"));
		bds.setInitialSize(5);
		bds.setMaxActive(10);
		return bds;
	}
	
	@Bean
	@Profile("TEST")
	public JpaVendorAdapter JpaVendorAdapterTEST() {
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setDatabase(Database.H2);
		adapter.setShowSql(true);
		adapter.setGenerateDdl(false);
		adapter.setDatabasePlatform("org.hibernate.dialect.H2Dialect");
		return adapter;
	}
	
	@Bean
	@Profile("DEV")
	public JpaVendorAdapter JpaVendorAdapterDEV() {
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setDatabase(Database.H2);
		adapter.setShowSql(true);
		adapter.setGenerateDdl(false);
		adapter.setDatabasePlatform("org.hibernate.dialect.H2Dialect");
		return adapter;
	}

	@Bean
	@Profile("QA")
	public JpaVendorAdapter mySqlJpaVendorAdapterQA() {
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setDatabase(Database.MYSQL);
		adapter.setShowSql(true);
		adapter.setGenerateDdl(false);
		adapter.setDatabasePlatform("org.hibernate.dialect.MySQL5InnoDBDialect");
		return adapter;
	}
	
	private Properties getJpaPropertiesTEST(){
		Properties jpaProperties = new Properties();
		jpaProperties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
		return jpaProperties;
	}
	
	private Properties getJpaPropertiesDEV(){
		Properties jpaProperties = new Properties();
		jpaProperties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
		return jpaProperties;
	}
	
	private Properties getJpaPropertiesQA(){
		Properties jpaProperties = new Properties();
		jpaProperties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		return jpaProperties;
	}
	
	@Bean
	@Profile("TEST")
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryTEST(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
		LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
		emfb.setDataSource(dataSource);
		emfb.setJpaVendorAdapter(jpaVendorAdapter);
		emfb.setJpaProperties(getJpaPropertiesTEST());
		emfb.setPackagesToScan("ci.projects.rci.model");
		return emfb;
	}
	
	@Bean
	@Profile("DEV")
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryDEV(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
		LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
		emfb.setDataSource(dataSource);
		emfb.setJpaVendorAdapter(jpaVendorAdapter);
		emfb.setJpaProperties(getJpaPropertiesDEV());
		emfb.setPackagesToScan("ci.projects.rci.model");
		return emfb;
	}

	@Bean
	@Profile("QA")
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryQA(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
		LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
		emfb.setDataSource(dataSource);
		emfb.setJpaVendorAdapter(jpaVendorAdapter);
		emfb.setJpaProperties(getJpaPropertiesQA());
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
