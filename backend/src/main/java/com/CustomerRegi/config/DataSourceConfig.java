package com.CustomerRegi.config;

import com.CustomerRegi.tenant.TenantDataSourceRegistry;
import com.CustomerRegi.tenant.TenantRoutingDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {


	@Value("${spring.datasource.url}")
	private String url;

	@Value("${spring.datasource.username}")
	private String username;

	@Value("${spring.datasource.password}")
	private String password;

	@Bean
	public DataSource masterDataSource() {
		HikariDataSource ds = new HikariDataSource();
		ds.setJdbcUrl(url);
		ds.setUsername(username);
		ds.setPassword(password);
		return ds;
	}

	@Bean
	@Primary
	public DataSource routingDataSource(TenantDataSourceRegistry registry) {

		DataSource master = masterDataSource();
		TenantRoutingDataSource routingDataSource = new TenantRoutingDataSource(registry, master);
		Map<Object, Object> targetDataSources = new HashMap<>();
		targetDataSources.put("MASTER", master);

		routingDataSource.setTargetDataSources(targetDataSources);

		//  MASTER DB is default
		routingDataSource.setDefaultTargetDataSource(master);

		routingDataSource.afterPropertiesSet();

		return routingDataSource;
	}

}


//	@Bean
//	@Primary
//	public DataSource dataSource(TenantDataSourceRegistry registry) {
//
//		TenantRoutingDataSource routingDataSource = new TenantRoutingDataSource();
//
//		routingDataSource.setTargetDataSources(new HashMap<>());
//
//		// default = MASTER DB
//		routingDataSource.setDefaultTargetDataSource(
//				registry.getDataSource("customerregistrstiondb")
//		);
//
//		routingDataSource.afterPropertiesSet();
//		return routingDataSource;







//		@Autowired
//		private TenantDataSourceRegistry tenantDataSourceRegistry;
//
//		@Autowired
//		private DataSource masterDataSource;
//
//		/**
//		 * This is the DEFAULT datasource used by JPA.
//		 * All repositories will use this.
//		 */
//		@Bean
//		@Primary
//		public DataSource routingDataSource () {
//
//			TenantRoutingDataSource routingDataSource = new TenantRoutingDataSource();
//
//			routingDataSource.setDefaultTargetDataSource(masterDataSource);
//
//			// tenant DBs will be resolved dynamically
//			routingDataSource.setTargetDataSources(new HashMap<>());
//
//			return routingDataSource;
//		}



