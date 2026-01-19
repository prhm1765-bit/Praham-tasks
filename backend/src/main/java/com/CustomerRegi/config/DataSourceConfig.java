package com.CustomerRegi.config;

import com.CustomerRegi.tenant.TenantDataSourceRegistry;
import com.CustomerRegi.tenant.TenantRoutingDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class DataSourceConfig {

	@Bean
	@Primary
	public DataSource dataSource(TenantDataSourceRegistry registry) {

		TenantRoutingDataSource routingDataSource = new TenantRoutingDataSource();

		routingDataSource.setTargetDataSources(new HashMap<>());

		// default = MASTER DB
		routingDataSource.setDefaultTargetDataSource(
				registry.getDataSource("customerregistrstiondb")
		);

		routingDataSource.afterPropertiesSet();
		return routingDataSource;
	}
}
