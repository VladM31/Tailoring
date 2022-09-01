package sigma.nure.tailoring.tailoring.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@ComponentScan("sigma.nure.tailoring.tailoring")
public class ConnectorConfig {

    @Bean
    public DataSource dataSource(
            @Value("${bd.info.url}") String url,
            @Value("${bd.info.username}") String username,
            @Value("${bd.info.password}") String password
    ){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);


        return dataSource;
    }

    @Bean
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure().dataSource(dataSource).load();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource){
        return  new JdbcTemplate(dataSource);
    }
}
