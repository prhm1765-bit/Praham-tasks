package com.CustomerRegi.constants;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ApiPathConstants {

	// Base path
	public static final String API = "/api";

	// Path for customer and admin portal
	public static final String CUSTOMER = API + "/customer";

	// Path for Reports api
	public static final String REPORTS = API + "/reports";

	// Path for Reports api - Customer List
	public static final String CUSTOMERDATA = "/customer-data";

	// Path for customer sign up
	public static final String SIGNUP = "/sign-up";

	// Path for customer and admin login
	public static final String LOGIN = "/login";

	// Path for Enums
	public static final String ENUM = API + "/enums";

	// Path for Enums-genders
	public static final String GENDERS = "/genders";

	// Path for Enums-AddressType
	public static final String ADDRESSTYPE = "/address-types";

	// Path for Tenant
	public static final String TENANT = API + "/tenants";

}
