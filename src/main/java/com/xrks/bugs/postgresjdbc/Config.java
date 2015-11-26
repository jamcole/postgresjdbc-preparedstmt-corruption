package com.xrks.bugs.postgresjdbc;

import javax.sql.DataSource;

import org.postgresql.jdbc2.optional.SimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
public class Config {
	@Bean
	DataSource dataSource() {
		SimpleDataSource dataSource = new SimpleDataSource();

		dataSource.setUrl("jdbc:postgresql://localhost/postgres?prepareThreshold=3");
		dataSource.setUser("postgres");
		dataSource.setPassword("postgres");

		return dataSource;
	}

	@Bean
	JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();

		jpaVendorAdapter.setDatabase(Database.POSTGRESQL);

		return jpaVendorAdapter;
	}

	@Bean
	LocalContainerEntityManagerFactoryBean entityManagerFactory(JpaVendorAdapter jpaVendorAdapter, DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

		entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
		entityManagerFactoryBean.setDataSource(dataSource);
		entityManagerFactoryBean.setPackagesToScan();

		return entityManagerFactoryBean;
	}
}
