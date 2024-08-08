package com.yeonieum.memberservice.global.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.yeonieum.memberservice.domain",
        entityManagerFactoryRef = "pentityManagerFactory",
        transactionManagerRef = "pjpaTransactionManager"
)
public class PrimaryDataSourceConfig {

    @Value("${spring.datasource.primary.url}")
    private String url;

    @Value("${spring.datasource.primary.username}")
    private String username;

    @Value("${spring.datasource.primary.password}")
    private String password;

    @Value("${spring.datasource.primary.driver-class-name}")
    private String driverClassName;

    @Primary
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create()
                .url(url)
                .username(username)
                .password(password)
                .driverClassName(driverClassName)
                .build();
    }
    @Primary
    @Bean(name = "pentityManagerFactory")
    public LocalContainerEntityManagerFactoryBean pentityManagerFactory(EntityManagerFactoryBuilder builder) {
        LocalContainerEntityManagerFactoryBean emfb =builder.dataSource(this.primaryDataSource())
                .packages("com.yeonieum.memberservice.domain") // 패키지 경로를 실제 경로로 수정하세요
                .build();
        emfb.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        // Hibernate 속성 설정
        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        jpaProperties.put("sql.show", "true");
        emfb.setJpaProperties(jpaProperties);

        return emfb;


    }
    @Primary
    @Bean(name = "pjpaTransactionManager")
    public PlatformTransactionManager pjpaTransactionManager(@Qualifier("pentityManagerFactory") EntityManagerFactory pentityManagerFactory) {
        return new JpaTransactionManager(pentityManagerFactory);
    }
}
