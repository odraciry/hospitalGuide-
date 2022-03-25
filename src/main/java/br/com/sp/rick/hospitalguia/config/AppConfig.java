package br.com.sp.rick.hospitalguia.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
public class AppConfig {
	@Bean // utilizado para q o springboot "instacie" o metodo
	//este metodo conecta com o DB
	public DataSource dataSource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost:3307/hospitalguia");
		ds.setUsername("root");
		ds.setPassword("root");
		return ds;
	}
	
	@Bean
	public JpaVendorAdapter jpavendorAdapter() {
		//mapeia o objeto e transforma em tabela no DB
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		//conecta a variavel ao banco de dados
		adapter.setDatabase(Database.MYSQL);
		//define alinguagem do DB
		adapter.setDatabasePlatform("org.hibernate.dialect.MySQL8Dialect");
		//prepara conexao para mandar instruçoes
		adapter.setPrepareConnection(true);
		//gera o DDL
		adapter.setGenerateDdl(true);
		//coloca a açoes no console(ajuda a debugar)
		adapter.setShowSql(true);
		return adapter;
	}
}
