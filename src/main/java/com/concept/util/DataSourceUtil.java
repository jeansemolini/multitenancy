package com.concept.util;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.concept.master.model.Empresa;
import com.zaxxer.hikari.HikariDataSource;

public class DataSourceUtil {

	private static final Logger LOG = LoggerFactory
            .getLogger(DataSourceUtil.class);

	public static DataSource createAndConfigureDataSource(Empresa masterTenant) {
		HikariDataSource ds = new HikariDataSource();
		ds.setUsername(masterTenant.getUsername());
		ds.setPassword(masterTenant.getPassword());
		ds.setJdbcUrl(masterTenant.getUrl());
		ds.setDriverClassName("com.mysql.jdbc.Driver");

		// Configurações do HikariCP - podem vir da tabela master_tenant mas
		// codificado aqui para brevidade
		// Tempo máximo de espera por uma conexão do pool
		ds.setConnectionTimeout(20000);

		// Número mínimo de conexões inativas no pool
		ds.setMinimumIdle(10);

		// Número máximo de conexão real no pool
		ds.setMaximumPoolSize(20);

		// Tempo máximo que uma conexão pode ficar inativa no pool
		ds.setIdleTimeout(300000);
		ds.setConnectionTimeout(20000);

		//Configurando um nome de pool para cada origem de dados de inquilino
		String tenantNome = masterTenant.getRazaoSocial();
		String tenantConnectionPoolName = tenantNome + "-connection-pool";
		ds.setPoolName(tenantConnectionPoolName);
		LOG.info("Configurado datasource:" + masterTenant.getRazaoSocial()
        + ". Connection poolname:" + tenantConnectionPoolName);
		return ds;
	}



}
