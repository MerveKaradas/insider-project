package com.web.demo.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.web.demo.context.DbType;

//Spring Boot default olarak tek spring.datasource.url bekler. Bizim hem master hem replica’ya bağlanmamız gerekiyor. Bu nedenle iki ayrı datasource tanımlar, sonra RoutingDataSource içine koyarız.


@Configuration
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class }) // DataSourceAutoConfiguration sınıfını devre dışı bırakır, çünkü biz kendi DataSource'larımızı tanımlayacağız.
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties("spring.datasource.master") // application.yml dosyasındaki spring.datasource.master ile eşleştirir
    public DataSourceProperties masterDataSourceProperties(){  // fiziksel olarak master datasource'u oluşturuyoruz.
        // Bu metot, master veritabanı için gerekli ayarları alır ve DataSourceProperties nesnesi döner.
        return new DataSourceProperties();
    }

    @Bean
    public DataSource masterDataSource(){  
        // masterDataSourceProperties() metodu ile alınan ayarları kullanarak DataSource nesnesi oluşturur.
        // Bu, veritabanı bağlantı havuzunu başlatır.
        return masterDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.replica") // application.yml’deki ayarları okuyoruz.
    public DataSourceProperties slaveDataSourceProperties(){ // fiziksel olarak slave datasource'u oluşturuyoruz.
        return new DataSourceProperties();
    }

    @Bean
    public DataSource slaveDataSource(){
        return slaveDataSourceProperties()
                .initializeDataSourceBuilder()
                .build();
    }

    @Bean
    @Primary 
    public DataSource routingDataSource(
            @Qualifier("masterDataSource") DataSource master,
            @Qualifier("slaveDataSource") DataSource slave ) {
                
        Map<Object, Object> dataSourceMap = new HashMap<>(); // Master ve slave datasource'ları tutacak bir harita oluşturuyoruz.
        dataSourceMap.put(DbType.MASTER, master); // Master veritabanı için DbType.MASTER anahtarını kullanır
        dataSourceMap.put(DbType.SLAVE, slave);

        ReplicationRoutingDataSource routingDataSource = new ReplicationRoutingDataSource(); // ReplicationRoutingDataSource sınıfı, AbstractRoutingDataSource sınıfından türetilmiştir ve veritabanı yönlendirmesini yapar.
        routingDataSource.setDefaultTargetDataSource(master); // Default olarak master datasource kullanılır
        routingDataSource.setTargetDataSources(dataSourceMap); // Master ve slave datasource'ları ekler

        return routingDataSource;
    
    
    }

    
}
