package com.CustomerRegi.tenant;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class TenantContext {

	// ThreadLocal holds tenant id of current request thread
	private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

	/**
	 * @param tenantId unique id of tenant which we got from request (JWT / header / subdomain)
	 * This method stores tenant id in ThreadLocal so later database routing can use it.
	 */
	public static void setTenant(String tenantId) {
		CURRENT_TENANT.set(tenantId);
	}

	/**
	 * @return tenant id which is currently active for this request
	 * This is used by datasource routing to decide which tenant database to connect.
	 */
	public static String getTenant() {
		return CURRENT_TENANT.get();
	}

	/**
	 * This method is used to remove tenentId from the ThreadLocal
	 * It must be called after at end of request to clear the tenantId otherwise it may leak current tenant data to next request
	 */
	public static void clear() {
		CURRENT_TENANT.remove();
	}

}
