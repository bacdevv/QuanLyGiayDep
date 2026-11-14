package dao;

import java.sql.*;
import connectDB.ConnectDB;
import entity.NhanVien;

public class TaiKhoan_DAO {
    public TaiKhoan_DAO() {}

    // Lay dung nhan vien cua tai khoan dang nhap.
    public NhanVien dangNhap(String tenDangNhap, String matKhau) throws SQLException {
        ConnectDB.getInstance().connect();
        Connection con = ConnectDB.getConnection();
        String sql = "SELECT nv.maNhanVien, nv.tenNhanVien, nv.soDienThoai, nv.hoatDong "
                + "FROM TaiKhoan tk JOIN NhanVien nv ON tk.maNhanVien = nv.maNhanVien "
                + "WHERE tk.tenDangNhap = ? AND tk.taiKhoanHash = ? AND nv.hoatDong = 1";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, tenDangNhap);
            stmt.setString(2, matKhau);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return new NhanVien(rs.getString(1), rs.getString(2),
                        rs.getString(3), rs.getBoolean(4));
            }
        }
        return null;
    }

    public boolean kiemTraDangNhap(String tenDangNhap, String matKhau) throws SQLException {
        return dangNhap(tenDangNhap, matKhau) != null;
    }
}
