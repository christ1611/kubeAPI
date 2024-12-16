package com.kubeApi.core.config;

import com.kubeApi.core.data.mapper.OneQonRowMapper;
import com.kubeApi.core.transaction.OneQTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
@EnableJdbcAuditing
@EnableJdbcRepositories( basePackages = "com.kubeApi.core.data"
		               , jdbcOperationsRef = "namedParameterJdbcTemplate"
					   , transactionManagerRef = "transactionManager"
					   )
public class DatabaseConfig extends AbstractJdbcConfiguration {

	@Bean
	@Primary
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource userDataSource) {
		return new NamedParameterJdbcTemplate(userDataSource);
	}


	@Bean
	@Primary
	public OneQTransactionManager transactionManager(DataSource userDataSource) {
		return new OneQTransactionManager(userDataSource);
	}

	@Bean
	public OneQonRowMapper oneQonRowMapper()
	{
		return new OneQonRowMapper();
	}
}
