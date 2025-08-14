package com.web.demo.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.web.demo.context.DbContextHolder;

public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {
    
    @Override
    protected Object determineCurrentLookupKey(){

        // DbContextHolderdan veritabanı tipini alır ve ona göre yönlendirme yapar.
        return DbContextHolder.getDbType();
    }
}
