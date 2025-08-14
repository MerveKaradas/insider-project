package com.web.demo.context;

public class DbContextHolder {

    // ThreadLocal kullanmamızın sebebi, aynı anda gelen farklı isteklerin biribirlerini etkilememesi ve her isteğin kendi veritabanı tipini kullanabilmesi için.
    private static final ThreadLocal<DbType> contextHolder = new ThreadLocal<>();
    
    public static void setDbType(DbType dbType) {
        contextHolder.set(dbType); // Threade özel veritabanı tipi ayarlama
    }

    public static DbType getDbType() {
        return contextHolder.get() == null ? DbType.MASTER : contextHolder.get(); // eğer veritabanı tipi ayarlanamamışsa default olarak MASTER;
    }

    public static void clearDbType() {
        contextHolder.remove(); // threadLocal'den veritabanı tipini temizler
    }

}
