package util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** Chạy lớp này trước để xác nhận SQL Server và dữ liệu mẫu. */
public final class KiemTraKetNoi {
    private KiemTraKetNoi() {}

    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            System.out.println("Kết nối thành công: " + conn.getCatalog());

            try (ResultSet rs = stmt.executeQuery(
                    "SELECT COUNT(*) FROM sys.tables WHERE schema_id = SCHEMA_ID('dbo')")) {
                if (rs.next()) {
                    System.out.println("Số bảng: " + rs.getInt(1) + "/14");
                }
            }

            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM dbo.CHITIETSANPHAM")) {
                if (rs.next()) {
                    System.out.println("Số biến thể sản phẩm: " + rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối/kiểm tra: " + e.getMessage());
        }
    }
}
