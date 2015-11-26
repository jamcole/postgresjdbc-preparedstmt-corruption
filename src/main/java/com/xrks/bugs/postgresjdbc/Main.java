package com.xrks.bugs.postgresjdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public final class Main {
	final static Logger log = LoggerFactory.getLogger(Main.class);

	private Main() {
		super();
	}

	public static void main(String[] args) throws SQLException {
		Thread.currentThread().setName("Main");

		int to = 123;

		try (ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(Config.class)) {
			EntityManagerFactory entityManagerFactory = ctx.getBean(EntityManagerFactory.class);
			EntityManager em = entityManagerFactory.createEntityManager();
			DataSource dataSource = ctx.getBean(DataSource.class);

			String countQuery = "SELECT CAST(456 AS bigint) AS col1, count(1) AS col2 FROM generate_series(1, " + to + ")";
			log.info("Using Query: {}", countQuery);
			

			log.info("Running query via EntityManager createNativeQuery...");
			for (int v = 1; v <= 10; v++) {
				Object row = em.createNativeQuery(countQuery).getSingleResult();

				log.info("Result {} = {}", v, row);
			}

			em.close();

			log.info("Running query via JDBC PreparedStatement executeQuery into BigDecimal...");
			try (Connection con = dataSource.getConnection()) {
				try (PreparedStatement st = con.prepareStatement(countQuery)) {
					for (int v = 1; v <= 10; v++) {
						try (ResultSet rs = st.executeQuery()) {
							rs.next();

							Object[] row = { rs.getBigDecimal(1), rs.getBigDecimal(2) };

							log.info("BigDecimal {} = {}", v, row);
						}
					}
				}
			}

			log.info("Running query via JDBC PreparedStatement executeQuery into String...");
			try (Connection con = dataSource.getConnection()) {
				try (PreparedStatement st = con.prepareStatement(countQuery)) {
					for (int v = 1; v <= 10; v++) {
						try (ResultSet rs = st.executeQuery()) {
							rs.next();

							Object[] row = { rs.getString(1), rs.getString(2) };

							log.info("String {} = {}", v, row);
						}
					}
				}
			}
		}
	}
}
