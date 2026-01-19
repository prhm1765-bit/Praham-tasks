package com.CustomerRegi.tenant;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TenantDataSourceRegistry {

	private final Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();

	@Value("${spring.datasource.username}")
	private String username;

	@Value("${spring.datasource.password}")
	private String password;

	public DataSource getDataSource(String tenantDbName) {
		return dataSourceMap.computeIfAbsent(tenantDbName, this::createDataSource);
	}

	private DataSource createDataSource(String dbName) {
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/" + dbName +
				"?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");

		dataSource.setUsername(username);
		dataSource.setPassword(password);

		dataSource.setMaximumPoolSize(10);
		dataSource.setMinimumIdle(2);
		dataSource.setPoolName("TENANT-POOL-" + dbName);

		return dataSource;
	}
}
