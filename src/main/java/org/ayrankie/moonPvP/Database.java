package org.ayrankie.moonPvP;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.SQLException;

public class Database {
    private static HikariDataSource dataSource;

    // Havuz sadece bir kez başlatılır (Singleton)
    public static synchronized void initDBPool() {
        if (dataSource == null || dataSource.isClosed()) {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://72.60.129.142:3306/moonpvp?allowPublicKeyRetrieval=true&useSSL=false");
            config.setUsername("admin");
            config.setPassword("cokgizlidatabasesifresi1008");
            config.setMaximumPoolSize(20);
            config.setMaxLifetime(1800000); // 30 dakika
            config.setIdleTimeout(600000);  // 10 dakika
            config.setMinimumIdle(2);
            config.setValidationTimeout(5000); // 5 saniye
            config.setConnectionTestQuery("SELECT 1"); // EN KRİTİK AYAR
            dataSource = new HikariDataSource(config);
        }
    }

    // Poolu kapatmak için
    public static synchronized void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            dataSource = null;
            Bukkit.getLogger().info("[MoonPvP] HikariCP pool kapatıldı.");
        }
    }

    // Poola erişim
    public static HikariDataSource getDataSource() {
        return dataSource;
    }

    // Havuzun aktif olup olmadığını kontrol eder ve sadece hata durumunda resetler
    public static synchronized void checkAndReconnectPool() {
        try {
            if (dataSource == null || dataSource.isClosed()) {
                initDBPool();
                Bukkit.getLogger().info("[MoonPvP] HikariCP pool yeniden başlatıldı (kapalıydı).");
                return;
            }
            // Bağlantı test ediliyor
            try (Connection conn = dataSource.getConnection()) {
                if (!conn.isValid(2)) {
                    closePool();
                    initDBPool();
                    Bukkit.getLogger().info("[MoonPvP] HikariCP pool yeniden başlatıldı (bağlantı geçersiz).");
                }
            }
        } catch (SQLException e) {
            Bukkit.getLogger().warning("[MoonPvP] Pool kontrolünde hata: " + e.getMessage());
            try {
                closePool();
            } catch (Exception ignored) {}
            initDBPool();
        }
    }
}