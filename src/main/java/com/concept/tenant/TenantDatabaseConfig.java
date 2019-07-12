package com.concept.tenant;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.concept.sistema.model.Produto;
import com.concept.sistema.repository.ProdutoRepository;
import com.concept.sistema.resource.ProdutoResource;

/**
 * Essa é a configuração das fontes de dados do inquilino que configura a ocupação variada.
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = { "com.concept.sistema.repository", "com.concept.sistema.model" })
@EnableJpaRepositories(basePackages = { "com.concept.sistema.repository" },
		entityManagerFactoryRef = "tenantEntityManagerFactory",
		transactionManagerRef = "tenantTransactionManager")
public class TenantDatabaseConfig {

	 private static final Logger LOG = LoggerFactory
	            .getLogger(TenantDatabaseConfig.class);

	 @Bean(name = "tenantJpaVendorAdapter")
	 public JpaVendorAdapter jpaVendorAdapter() {
		 return new HibernateJpaVendorAdapter();
	 }

	 @Bean(name = "tenantTransactionManager")
	 public JpaTransactionManager transactionManager(EntityManagerFactory tenantEntityManager) {
		 JpaTransactionManager transactionManager = new JpaTransactionManager();
		 transactionManager.setEntityManagerFactory(tenantEntityManager);
		 return transactionManager;
	 }

	 /**
	  * O provedor de conexão multilocatário
	 */
	 @Bean(name = "datasourceBasedMultitenantConnectionProvider")
	 @ConditionalOnBean(name = "masterEntityManagerFactory")
	 public MultiTenantConnectionProvider multiTenantConnectionProvider() {
		// Autowires o provedor de conexão múltipla
		 return new DataSourceBasedMultiTenantConnectionProviderImpl();
	 }

	 /**
	  * O resolvedor de identificador de inquilino atual
	 */
	 @Bean(name = "currentTenantIdentifierResolver")
	 public CurrentTenantIdentifierResolver currentTenantIdentifierResolver() {
		 return new CurrentTenantIdentifierResolverImpl();
	 }

	 /**
	  * Cria o bean de fábrica do gerenciador de entidades que é necessário para acessar o
	  * Funcionalidades JPA fornecidas pelo provedor de persistência JPA, ou seja,
	  * Hibernate neste caso.
	 */
	 @Bean(name = "tenantEntityManagerFactory")
	 @ConditionalOnBean(name = "datasourceBasedMultitenantConnectionProvider")
	 public LocalContainerEntityManagerFactoryBean entityManagerFactory(
			 @Qualifier("datasourceBasedMultitenantConnectionProvider") MultiTenantConnectionProvider connectionProvider,
			 @Qualifier("currentTenantIdentifierResolver") CurrentTenantIdentifierResolver tenantResolver) {

		 LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
		 // Todas as entidades relacionadas a inquilinos, repositórios e classes de serviço devem ser verificadas
		 emfBean.setPackagesToScan(
				 new String[] { Produto.class.getPackage().getName(),
				 		ProdutoRepository.class.getPackage().getName(),
				 		ProdutoResource.class.getPackage().getName() });
		 emfBean.setJpaVendorAdapter(jpaVendorAdapter());
		 emfBean.setPersistenceUnitName("tenantdb-persistence-unit");
		 Map<String, Object> properties = new HashMap<>();
		 properties.put(org.hibernate.cfg.Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
		 properties.put(org.hibernate.cfg.Environment.MULTI_TENANT_CONNECTION_PROVIDER, connectionProvider);
		 properties.put(org.hibernate.cfg.Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantResolver);
		 properties.put(org.hibernate.cfg.Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
         properties.put(org.hibernate.cfg.Environment.SHOW_SQL, true);
         properties.put(org.hibernate.cfg.Environment.FORMAT_SQL, true);
         properties.put(org.hibernate.cfg.Environment.HBM2DDL_AUTO, "update");

	     emfBean.setJpaPropertyMap(properties);
	     LOG.info("tenantEntityManagerFactory configurado com sucesso!");
	     return emfBean;
	 }
}
