package com.concept.master.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.concept.master.model.Empresa;
import com.concept.master.repository.EmpresaRepository;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = { "com.concept.master.model", "com.concept.master.repository" },
			entityManagerFactoryRef = "masterEntityManagerFactory", transactionManagerRef = "masterTransactionManager" )
public class MasterDataBaseConfig {

	private static final Logger LOG = LoggerFactory
            .getLogger(MasterDataBaseConfig.class);

	@Autowired
	private MasterDataBaseConfigProperties masterDbProperties;

	@Bean(name = "masterDataSource")
	public DataSource masterDataSource() {

		LOG.info("Configurando masterDataSource com: "
				+ "" );

		HikariDataSource ds = new HikariDataSource();

		ds.setUsername(masterDbProperties.getUsername());
		ds.setPassword(masterDbProperties.getPassword());
		ds.setJdbcUrl(masterDbProperties.getUrl());
		ds.setDriverClassName(masterDbProperties.getDriverClassName());
		ds.setPoolName(masterDbProperties.getPoolName());

		// HikariCP
		ds.setMaximumPoolSize(masterDbProperties.getMaxPoolSize());
		ds.setMinimumIdle(masterDbProperties.getMinIdle());
		ds.setConnectionTimeout(masterDbProperties.getConnectionTimeout());

		LOG.info("A instalação do masterDataSource foi bem-sucedida.");
		return ds;
	}

	/**
     * Cria o bean de fábrica do gerenciador de entidades que é necessário para acessar o
     * Funcionalidades JPA fornecidas pelo provedor de persistência JPA, ou seja,
     * Hibernar neste caso. <br/>
     * <br/>
     * Observe a anotação <b> {@ literal @} Primária </ b> que informa a inicialização do Spring para
     * criar este gerenciador de entidades como a primeira coisa ao iniciar o
     * aplicação.
     *
     *
    */
	@Primary
	@Bean(name = "masterEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean masterEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

		// Definir a fonte de dados mestre
		em.setDataSource(masterDataSource());

		// A entidade de locatário principal e o repositório precisam ser verificados
		em.setPackagesToScan(new String[] { Empresa.class.getPackage().getName(),
								EmpresaRepository.class.getPackage().getName() });

		// Definir um nome para a unidade de persistência como Spring define como
		// 'default' se não estiver definido
		em.setPersistenceUnitName("masterdb-persistence-unit");

		// Configurando o Hibernate como o provedor JPA
		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);

		// Definir as propriedades de hibernate
		em.setJpaProperties(hibernateProperties());
		LOG.info("A configuração do masterEntityManagerFactory foi bem-sucedida.");
		
		/**
		 * Faz as verificações do Flyway via código
		 */
		try {
			Flyway flyway = new Flyway();
			flyway.setLocations("db/master");
			flyway.setDataSource(masterDbProperties.getUrl(),masterDbProperties.getUsername(),masterDbProperties.getPassword());
			flyway.setBaselineOnMigrate(true);
			flyway.migrate();			
		} catch(Exception e) {
			System.err.println(e.getMessage());
			System.exit(0);
		}
		
		
		
		return em;
	}

	/**
     * Este gerenciador de transações é apropriado para aplicativos que usam um
     * JPA EntityManagerFactory único para acesso a dados transacionais. <br/>
     * <br/>
     * Observe a anotação <b> {@ literal @} Qualifier </ b> para garantir que
     * <tt> masterEntityManagerFactory </ tt> é usado para configurar o
     * gerente de transação.
     *
	*/
	@Bean(name = "masterTransactionManager")
	public JpaTransactionManager mastertransactionManager(
			@Qualifier("masterEntityManagerFactory") EntityManagerFactory emf) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(emf);
		return transactionManager;
	}

	/**
     * Pós-processador de bean que aplica automaticamente exceção de persistência
     * tradução para qualquer bean marcado com a anotação @Repository do Spring,
     * adicionar um PersistenceExceptionTranslationAdvisor correspondente ao
     * proxy exposto (um proxy AOP existente ou um proxy recém gerado
     * que implementa todas as interfaces do alvo.
     *
    */
	@Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

	/**
     * As propriedades para configurar o provedor JPA do Hibernate.     
	*/
	private Properties hibernateProperties() {
		Properties properties = new Properties();
		properties.put(org.hibernate.cfg.Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
		properties.put(org.hibernate.cfg.Environment.SHOW_SQL, true);
        properties.put(org.hibernate.cfg.Environment.FORMAT_SQL, true);
        properties.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, "update");
        return properties;
	}
}
