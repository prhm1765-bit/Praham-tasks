package com.CustomerRegi.tenant;

import com.CustomerRegi.enums.Role;
import com.CustomerRegi.model.Tenant;
import com.CustomerRegi.repository.TenantRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TenantProvisioningService {

	private final DataSource dataSource;// MASTER DB connection
	private final TenantRepo tenantRepo;
	private final PasswordEncoder passwordEncoder;

	public Tenant registerTenant(String tenantId,String email, String rawPassword) {

		//String tenantId = UUID.randomUUID().toString().replace("-", "");
		// Create database name
		String dbName = "tenant_" + tenantId;

		// Create tenant database
		createDatabase(dbName);

		createTenantTables(dbName);

		TenantContext.clear();

		//  Save tenant metadata in MASTER DB
		Tenant tenant = Tenant.builder()
				.tenantId(tenantId)
				.dbName(dbName)
				.email(email)
				.password(passwordEncoder.encode(rawPassword))
				.role(Role.CUSTOMER)
				.status("ACTIVE")
				.build();

		return tenantRepo.save(tenant);
	}

	private void createDatabase(String dbName) {
		try (Connection connection = dataSource.getConnection();
			 Statement statement = connection.createStatement()) {

			statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);

		} catch (Exception e) {
			throw new RuntimeException("Failed to create database for tenant", e);
		}
	}

	private void createTenantTables(String dbName) {
		try (
				Connection connection = java.sql.DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/" + dbName +
								"?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
						"root",
						"root"
				);
				Statement statement = connection.createStatement()
		) {

			InputStream inputStream = getClass()
					.getClassLoader()
					.getResourceAsStream("tenant-schema.sql");

			if (inputStream == null) {
				throw new RuntimeException("tenant-schema.sql not found");
			}

			String sql = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

			for (String query : sql.split(";")) {
				if (!query.trim().isEmpty()) {
					statement.execute(query);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Failed to create tenant tables", e);
		} catch (Exception e) {
			throw new RuntimeException("Unexpected error", e);
		}
	}

}


