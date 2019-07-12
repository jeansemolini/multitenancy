package com.concept.master.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("app.master.datasource")
public class MasterDataBaseConfigProperties {

	private String url;
	private String username;
	private String password;
	private String driverClassName;

	/**
    * Número máximo de milissegundos que um cliente aguardará por uma conexão
    * da piscina. Se este tempo for excedido sem uma conexão
    * disponível, uma SQLException será lançada de
    * javax.sql.DataSource.getConnection ().
    */
	private long connectionTimeout;

	/**
    * A propriedade controla o tamanho máximo permitido para o pool,
    * incluindo conexões inativas e em uso. Basicamente este valor
    * determinar o número máximo de conexões reais para o banco de dados
    * backend.
    *
    * Quando o pool atinge esse tamanho e não há conexões ociosas disponíveis,
    * chamadas para getConnection () serão bloqueadas por até connectionTimeout
    * Milissegundos antes do tempo limite.
    */
	private int maxPoolSize;

	/**
    * Esta propriedade controla o tempo máximo (em milissegundos) que
    * uma conexão pode ficar inativa na piscina. Se uma conexão é
    * aposentado como ocioso ou não está sujeito a uma variação máxima de +30 segundos,
    * e variação média de +15 segundos. Uma conexão nunca será retirada
    * como ocioso antes desse tempo limite. Um valor de 0 significa que as conexões inativas são
    * nunca removido da piscina.
    */
	private long idleTimeout;

	/**
    * A propriedade controla o número mínimo de conexões ociosas que
    * HikariCP tenta manter no pool, incluindo ocioso e em uso
    * conexões. Se as conexões ociosas ficarem abaixo desse valor, o HikariCP
    * Faça o melhor esforço para restaurá-los de forma rápida e eficiente.
    */
	private int minIdle;

	 /**
     * O nome do pool de conexão do banco de dados mestre
     */
	private String poolName;

	@Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("MasterDatabaseConfigProperties [url=");
        builder.append(url);
        builder.append(", username=");
        builder.append(username);
        builder.append(", password=");
        builder.append(password);
        builder.append(", driverClassName=");
        builder.append(driverClassName);
        builder.append(", connectionTimeout=");
        builder.append(connectionTimeout);
        builder.append(", maxPoolSize=");
        builder.append(maxPoolSize);
        builder.append(", idleTimeout=");
        builder.append(idleTimeout);
        builder.append(", minIdle=");
        builder.append(minIdle);
        builder.append(", poolName=");
        builder.append(poolName);
        builder.append("]");
        return builder.toString();
    }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public long getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(long connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public long getIdleTimeout() {
		return idleTimeout;
	}

	public void setIdleTimeout(long idleTimeout) {
		this.idleTimeout = idleTimeout;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public String getPoolName() {
		return poolName;
	}

	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}
}
