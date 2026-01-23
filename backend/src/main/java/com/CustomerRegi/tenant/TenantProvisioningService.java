package com.CustomerRegi.tenant;

import com.CustomerRegi.enums.Role;
import com.CustomerRegi.model.Tenant;
import com.CustomerRegi.repository.TenantRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Service
@RequiredArgsConstructor
public class TenantProvisioningService {

	// This datasource is used to point master database
	private final DataSource masterDataSource;
	private final TenantRepo tenantRepo;
	private final PasswordEncoder passwordEncoder;

	/**
	 * @param tenantId is the tenant Id of the tenant database
	 * @param email tenant email
	 * @param rawPassword  plain password received from user
	 * @return saved tenant entity
	 *
	 * This method creates tenant database, creates tables and then stores tenant details in MASTER DB.
	 */

	public Tenant registerTenant(String tenantId,String email, String rawPassword) {
		String dbName = "tenant_" + tenantId;
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

	/**
	 * @param dbName name of tenant database
	 *
	 * This method creates a new database if it does not exist.
	 */
	private void createDatabase(String dbName) {
		try (Connection connection = masterDataSource.getConnection();
			Statement statement = connection.createStatement()) {
			statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
		} catch (Exception e) {
			throw new RuntimeException("Failed to create database for tenant", e);
		}
	}

	/**
	 * @param dbName tenant database name
	 *
	 * This method runs SQL script to create all required tables inside tenant database.
	 */
	private void createTenantTables(String dbName) {
		try (
			Connection connection = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dbName + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", "root", "root");
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

	/**
	 * @param tenantId unique tenant identifier
	 *
	 * This method deletes tenant record from MASTER DB and then drops tenant database completely.
	 */
	@Transactional
	public void deleteTenantDatabase(String tenantId) {
		Tenant tenant = tenantRepo.findByTenantId(tenantId).orElseThrow(() -> new RuntimeException("Tenant not found"));
		String dbName = tenant.getDbName();
		tenantRepo.delete(tenant);
		dropDatabase(dbName);
	}

	/**
	 * @param dbName tenant database name
	 *
	 * This method permanently deletes tenant database.
	 */
	private void dropDatabase(String dbName) {
		try (Connection connection = masterDataSource.getConnection();
			Statement statement = connection.createStatement()) {
			statement.executeUpdate("DROP DATABASE IF EXISTS " + dbName);
		} catch (Exception e) {
			throw new RuntimeException("Failed to drop tenant database", e);
		}
	}

}


