package com.CustomerRegi.config;

import com.CustomerRegi.tenant.TenantDataSourceRegistry;
import com.CustomerRegi.tenant.TenantRoutingDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 * This configuration class decides which datasource spring should use for database operations.
 */
@Configuration
public class DataSourceConfig {

	/**
	 * @param registry registry which manages tenant datasources
	 * @return datasource which dynamically switches between MASTER and TENANT DB
	 *
	 * This is the main datasource used by Spring JPA.
	 * Primary DataSource bean
	 */
	@Bean
	@Primary
	public DataSource dataSource(TenantDataSourceRegistry registry) {
		// master database which is set in application.properties
		DataSource masterDataSource = registry.getDataSource("customerregistrstiondb");
		// Routing datasource decides DB based on tenant id at runtime
		TenantRoutingDataSource routingDataSource = new TenantRoutingDataSource(registry, masterDataSource);
		routingDataSource.setTargetDataSources(new HashMap<>());
		// This will set database to master when tenant id is null
		routingDataSource.setDefaultTargetDataSource(masterDataSource);
		// Important to initialize routing datasource properly. Without this, Spring may throw startup errors
		routingDataSource.afterPropertiesSet();
		return routingDataSource;
	}

}