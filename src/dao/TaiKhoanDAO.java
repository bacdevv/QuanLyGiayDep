package dao;

import entity.TaiKhoan;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaiKhoanDAO {
    public record EmployeeOption(String id, String name) {
        @Override public String toString() { return id + " - " + name; }
    }

    private static final String SELECT = """
            SELECT tk.MaTK, tk.MaNhanVien, tk.TenDangNhap, tk.MatKhau, tk.VaiTro,
                   tk.HoatDong, tk.NgayTao, nv.TenNhanVien, nv.TrangThai
            FROM TAIKHOAN tk JOIN NHANVIEN nv ON nv.MaNhanVien = tk.MaNhanVien
            """;

    public TaiKhoan findForLogin(String username) throws SQLException {
        String sql = SELECT + " WHERE tk.TenDangNhap = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<TaiKhoan> findAll(String keyword) throws SQLException {
        String sql = SELECT + """
                WHERE (? = '' OR tk.TenDangNhap LIKE ? OR tk.MaNhanVien LIKE ? OR nv.TenNhanVien LIKE ?)
                ORDER BY tk.TenDangNhap
                """;
        String value = keyword == null ? "" : keyword.trim();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, value);
            ps.setString(2, "%" + value + "%");
            ps.setString(3, "%" + value + "%");
            ps.setString(4, "%" + value + "%");
            try (ResultSet rs = ps.executeQuery()) {
                List<TaiKhoan> result = new ArrayList<>();
                while (rs.next()) result.add(map(rs));
                return result;
            }
        }
    }

    public boolean isActiveEmployeeAvailable(String employeeId, String accountId) throws SQLException {
        String sql = """
                SELECT COUNT(*) FROM NHANVIEN nv
                WHERE nv.MaNhanVien=? AND nv.TrangThai='DANG_LAM'
                  AND NOT EXISTS (
                      SELECT 1 FROM TAIKHOAN tk
                      WHERE tk.MaNhanVien=nv.MaNhanVien AND (? IS NULL OR tk.MaTK<>?)
                  )
                """;
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, employeeId);
            ps.setString(2, accountId);
            ps.setString(3, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    public List<EmployeeOption> findActiveEmployees() throws SQLException {
        String sql = "SELECT MaNhanVien, TenNhanVien FROM NHANVIEN WHERE TrangThai='DANG_LAM' ORDER BY TenNhanVien";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<EmployeeOption> result = new ArrayList<>();
            while (rs.next()) result.add(new EmployeeOption(rs.getString(1), rs.getString(2)));
            return result;
        }
    }

    public void insert(TaiKhoan account) throws SQLException {
        String sql = "INSERT INTO TAIKHOAN(MaTK,MaNhanVien,TenDangNhap,MatKhau,VaiTro,HoatDong) VALUES(?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            bind(ps, account);
            ps.executeUpdate();
        }
    }

    public void update(TaiKhoan account, boolean changePassword) throws SQLException {
        String sql = changePassword
                ? "UPDATE TAIKHOAN SET MaNhanVien=?, TenDangNhap=?, MatKhau=?, VaiTro=?, HoatDong=? WHERE MaTK=?"
                : "UPDATE TAIKHOAN SET MaNhanVien=?, TenDangNhap=?, VaiTro=?, HoatDong=? WHERE MaTK=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, account.getMaNhanVien());
            ps.setString(2, account.getTenDangNhap());
            int index = 3;
            if (changePassword) ps.setString(index++, account.getMatKhau());
            ps.setString(index++, account.getVaiTro());
            ps.setBoolean(index++, account.isHoatDong());
            ps.setString(index, account.getMaTK());
            ps.executeUpdate();
        }
    }

    public void delete(String id) throws SQLException {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM TAIKHOAN WHERE MaTK=?")) {
            ps.setString(1, id);
            ps.executeUpdate();
        }
    }

    private static void bind(PreparedStatement ps, TaiKhoan a) throws SQLException {
        ps.setString(1, a.getMaTK());
        ps.setString(2, a.getMaNhanVien());
        ps.setString(3, a.getTenDangNhap());
        ps.setString(4, a.getMatKhau());
        ps.setString(5, a.getVaiTro());
        ps.setBoolean(6, a.isHoatDong());
    }

    private static TaiKhoan map(ResultSet rs) throws SQLException {
        TaiKhoan a = new TaiKhoan();
        a.setMaTK(rs.getString("MaTK"));
        a.setMaNhanVien(rs.getString("MaNhanVien"));
        a.setTenDangNhap(rs.getString("TenDangNhap"));
        a.setMatKhau(rs.getString("MatKhau"));
        a.setVaiTro(rs.getString("VaiTro"));
        a.setHoatDong(rs.getBoolean("HoatDong"));
        Timestamp created = rs.getTimestamp("NgayTao");
        if (created != null) a.setNgayTao(created.toLocalDateTime());
        a.setTenNhanVien(rs.getString("TenNhanVien"));
        a.setTrangThaiNhanVien(rs.getString("TrangThai"));
        return a;
    }
}
