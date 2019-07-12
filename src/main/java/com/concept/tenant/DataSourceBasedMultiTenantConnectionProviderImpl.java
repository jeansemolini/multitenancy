package com.concept.tenant;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.concept.master.model.Empresa;
import com.concept.master.repository.EmpresaRepository;
import com.concept.util.DataSourceUtil;

/**
 * Esta classe faz o trabalho de selecionar o banco de dados correto baseado no id do locatário encontrado pelo CurrentTenantIdentifierResolverImpl
 */
public class DataSourceBasedMultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {

	private static final Logger LOG = LoggerFactory.getLogger(DataSourceBasedMultiTenantConnectionProviderImpl.class);

	private static final long serialVersionUID = 1L;

	/**
	 * MasterTenantRepository injetado para acessar as informações do inquilino da tabela master_tenant
    */
	@Autowired
	private EmpresaRepository masterTenantRepository;

	private Map<String, DataSource> dataSourceMap = new TreeMap<>();

	@Override
	protected DataSource selectAnyDataSource() {

		if (dataSourceMap.isEmpty()) {
			List<Empresa> masterTenants = masterTenantRepository.findAll();
			LOG.info(">>>>>> selectAnyDataSource() -- Total tenants: " + masterTenants.size());
			for (Empresa masterTenant: masterTenants) {
				dataSourceMap.put(masterTenant.getRazaoSocial(), DataSourceUtil.createAndConfigureDataSource(masterTenant));

				/**
				 * Faz as verificações do Flyway via código
				 */
				try {
					Flyway flyway = new Flyway();
					flyway.setLocations("db/sistema");
					flyway.setDataSource(masterTenant.getUrl(),masterTenant.getUsername(),masterTenant.getPassword());
					flyway.setBaselineOnMigrate(true);
					flyway.migrate();
				} catch(Exception e) {
					System.err.println(e.getMessage());
					System.exit(0);
				}

			}
		}
		return this.dataSourceMap.values().iterator().next();
	}

	@Override
	protected DataSource selectDataSource(String tenantIdentifier) {
		// Se o ID do locatário solicitado não estiver presente, verifique-o no mestre
		// tabela de banco de dados 'master_tenant'

		//TODO: Fazer depois pois agora não será util
		//tenantIdentifier = initializeTenantIfLost(tenantIdentifier);

		if (!this.dataSourceMap.containsKey(tenantIdentifier)) {
			List<Empresa> masterTenants = masterTenantRepository.findAll();
			LOG.info(
					">>>>> selectDataSource() -- tenant:" + tenantIdentifier + " Total tenants:" + masterTenants.size());
			for (Empresa masterTenant: masterTenants) {
				dataSourceMap.put(masterTenant.getCnpj(), DataSourceUtil.createAndConfigureDataSource(masterTenant));
			}
		}
		return this.dataSourceMap.get(tenantIdentifier);
	}
}
