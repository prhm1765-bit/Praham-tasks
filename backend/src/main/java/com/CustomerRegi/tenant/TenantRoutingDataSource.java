package com.CustomerRegi.tenant;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import javax.sql.DataSource;

/**
 * This class decides which datasource should be used for current request based on tenant id.
 * Spring calls this internally before running any DB query. We never call this class directly.
 */
@RequiredArgsConstructor
public class TenantRoutingDataSource extends AbstractRoutingDataSource {

	// This registry gives datasource for a tenant. It creates datasource if it does not exist
	private final TenantDataSourceRegistry tenantDataSourceRegistry;
	// This datasource always points to MASTER database. Used when no tenant is found in context
	private final DataSource masterDataSource;

	/**
	 * @return tenant id for current request
	 * {@inheritDoc}
	 *
	 * Spring calls this method internally.
	 * The returned value is used to decide which DB to use.
	 */
	@Override
	protected Object determineCurrentLookupKey() {
		return TenantContext.getTenant();
	}

	/**
	 * @return datasource which should be used for current request
	 ** {@inheritDoc}
	 *
	 * If tenant id is null, MASTER DB is used. Otherwise tenant database is selected.
	 */
	@Override
	protected DataSource determineTargetDataSource() {
		String tenantId = (String) determineCurrentLookupKey();
		// No tenant then use MASTER DB
		if (tenantId == null) {
			return masterDataSource;
		}
		return tenantDataSourceRegistry.getDataSource("tenant_" + tenantId);
	}

}


