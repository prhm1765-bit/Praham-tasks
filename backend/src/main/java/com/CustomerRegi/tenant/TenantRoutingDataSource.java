package com.CustomerRegi.tenant;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import javax.sql.DataSource;

@RequiredArgsConstructor
public class TenantRoutingDataSource extends AbstractRoutingDataSource {

	private final TenantDataSourceRegistry registry;
	private final DataSource masterDataSource;

	@Override
	protected Object determineCurrentLookupKey() {
		return TenantContext.getTenant();
	}

	@Override
	protected DataSource determineTargetDataSource() {
		String tenantId = (String) determineCurrentLookupKey();
		// No tenant then use MASTER DB
		if (tenantId == null) {
			return masterDataSource;
		}
		// tenant DB
		return registry.getDataSource("tenant_" + tenantId);
	}

}


