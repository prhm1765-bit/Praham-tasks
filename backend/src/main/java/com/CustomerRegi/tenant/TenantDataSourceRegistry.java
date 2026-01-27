package com.CustomerRegi.tenant;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TenantDataSourceRegistry {

	// This map is used to map tenant database name -> datasource, ConcurrentHashMap is used because multiple requests
	private final Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();

	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.password}")
	private String password;

	/**
	 * @param tenantDbName name of tenant database
	 * @return datasource for given tenant
	 *
	 * If datasource already exists in map, it will return same one.
	 * If not, it will create new datasource and store it.
	 */
	public DataSource getDataSource(String tenantDbName) {
		return dataSourceMap.computeIfAbsent(tenantDbName, dbName -> {
			DataSource ds = createDataSource(dbName);
			return ds;
		});
	}

	/**
	 * @param dbName tenant database name
	 * @return newly created datasource for tenant
	 *
	 * This method creates a fresh Hikari datasource with connection pool settings.
	 */
	private DataSource createDataSource(String dbName) {
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/" + dbName + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setMaximumPoolSize(10);
		dataSource.setMinimumIdle(2);
		dataSource.setPoolName("TENANT-POOL-" + dbName);
		return dataSource;
	}

}
