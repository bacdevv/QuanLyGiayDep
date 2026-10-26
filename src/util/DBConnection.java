package util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/** Kết nối JDBC; chỉ dành cho cấu hình và mở kết nối. */
public final class DBConnection {
    private DBConnection() {}

    public static Connection getConnection() throws SQLException {
        Path file = Path.of("config", "db.properties");
        Properties config = new Properties();
        try (InputStream in = Files.newInputStream(file)) {
            config.load(in);
        } catch (IOException e) {
            throw new SQLException("Thiếu config/db.properties. Hãy sao chép từ db.properties.example.", e);
        }

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Chưa thêm tệp mssql-jdbc-*.jre11.jar vào thư mục lib.", e);
        }

        return DriverManager.getConnection(
                config.getProperty("db.url"),
                config.getProperty("db.user"),
                config.getProperty("db.password"));
    }
}
