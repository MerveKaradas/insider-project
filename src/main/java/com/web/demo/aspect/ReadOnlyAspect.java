package com.web.demo.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.web.demo.context.DbContextHolder;
import com.web.demo.context.DbType;

@Aspect
@Component
public class ReadOnlyAspect {
    
    @Before("@annotation(ReadOnly)")
    public void setReadOnlyDataSource() {
        DbContextHolder.setDbType(DbType.SLAVE); // Okuma için slave kullan
    }

    @After("@annotation(ReadOnly)")
    public void clearDataSourceType() {
        DbContextHolder.clearDbType(); // İşlem bitince temizle
    }
}
