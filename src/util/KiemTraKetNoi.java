package util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Kiểm tra JDBC và dữ liệu mẫu, không thay đổi CSDL.
 * Chạy trực tiếp bằng nút Run Java trong VS Code.
 */
public final class KiemTraKetNoi {
    private KiemTraKetNoi() {}

    public static void main(String[] args) {
        String[] tables = {
            "NHANVIEN", "TAIKHOAN", "DANHMUC", "THUONGHIEU",
            "SANPHAM", "CHITIETSANPHAM", "NHACUNGCAP", "KHACHHANG",
            "PHIEUNHAP", "CHITIETPHIEUNHAP", "HOADON", "CHITIETHOADON",
            "PHIEUDOITRA", "CHITIETDOITRA"
        };

        try (Connection conn = DBConnection.getConnection();
             Statement statement = conn.createStatement()) {
            System.out.println("Kết nối thành công: " + conn.getCatalog());

            int tablesFound = 0;
            int totalRows = 0;
            for (String table : tables) {
                // Danh sách tên bảng cố định từ mã nguồn, không nhận từ người dùng.
                try (ResultSet result = statement.executeQuery(
                        "SELECT COUNT(*) FROM dbo." + table)) {
                    if (result.next()) {
                        int count = result.getInt(1);
                        tablesFound++;
                        totalRows += count;
                        System.out.println(table + ": " + count + " bản ghi");
                    }
                }
            }

            System.out.println("Số bảng kiểm tra: " + tablesFound + "/14");
            System.out.println("Tổng bản ghi: " + totalRows + " (dữ liệu mẫu ban đầu: 641)");
            System.out.println("Lưu ý: số bản ghi thay đổi sau khi nhóm thêm/sửa dữ liệu.");
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối hoặc thiếu bảng: " + e.getMessage());
        }
    }
}
