package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectDB.ConnectDB;
import java.math.BigDecimal;
import entity.SanPham;
import entity.DanhMuc;
import entity.ThuongHieu;

public class SanPham_DAO {
    public SanPham_DAO() {
    }

    public ArrayList<SanPham> getallSanPham() {
        ArrayList<SanPham> sp = new ArrayList<SanPham>();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT sp.maSP, sp.maDanhMuc, dm.tenDanhMuc, "
                    + "sp.maThuongHieu, th.tenThuongHieu, sp.tenSP, sp.hoatDong, sp.giaBan "
                    + "FROM SanPham sp "
                    + "JOIN DanhMuc dm ON sp.maDanhMuc = dm.maDanhMuc "
                    + "JOIN ThuongHieu th ON sp.maThuongHieu = th.maThuongHieu "
                    + "ORDER BY sp.maSP";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String maSP = rs.getString("maSP");
                        String tenSanPham = rs.getString("tenSP");
                        DanhMuc dm = new DanhMuc(rs.getString("maDanhMuc"), rs.getString("tenDanhMuc"));
                        ThuongHieu th = new ThuongHieu(rs.getString("maThuongHieu"), rs.getString("tenThuongHieu"));
                        boolean hoatDong = rs.getBoolean("hoatDong");
                        BigDecimal giaBan = rs.getBigDecimal("giaBan");
                        SanPham sPham = new SanPham(maSP, dm, th, tenSanPham, hoatDong, giaBan);
                        sp.add(sPham);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sp;
    }

    public boolean create(SanPham sp) {
        int n = 0;
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "INSERT INTO SanPham (maSP, maDanhMuc, maThuongHieu, tenSP, hoatDong, giaBan) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, sp.getMaSP());
                stmt.setString(2, sp.getDanhMuc().getMaDanhMuc());
                stmt.setString(3, sp.getThuongHieu().getMaThuongHieu());
                stmt.setNString(4, sp.getTenSP());
                stmt.setBoolean(5, sp.isHoatDong());
                stmt.setBigDecimal(6, sp.getGiaBan());
                n = stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public ArrayList<SanPham> getSanPhamTheoMaSP(String id) {
        ArrayList<SanPham> sp = new ArrayList<SanPham>();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT sp.maSP, sp.maDanhMuc, dm.tenDanhMuc, "
                    + "sp.maThuongHieu, th.tenThuongHieu, sp.tenSP, sp.hoatDong, sp.giaBan "
                    + "FROM SanPham sp "
                    + "JOIN DanhMuc dm ON sp.maDanhMuc = dm.maDanhMuc "
                    + "JOIN ThuongHieu th ON sp.maThuongHieu = th.maThuongHieu "
                    + "WHERE sp.maSP = ?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String maSP = rs.getString("maSP");
                        String tenSanPham = rs.getString("tenSP");
                        DanhMuc dm = new DanhMuc(rs.getString("maDanhMuc"), rs.getString("tenDanhMuc"));
                        ThuongHieu th = new ThuongHieu(rs.getString("maThuongHieu"), rs.getString("tenThuongHieu"));
                        boolean hoatDong = rs.getBoolean("hoatDong");
                        BigDecimal giaBan = rs.getBigDecimal("giaBan");
                        SanPham sPham = new SanPham(maSP, dm, th, tenSanPham, hoatDong, giaBan);
                        sp.add(sPham);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sp;
    }

    public ArrayList<SanPham> getSanPhamTheoTenSP(String tenSP) {
        ArrayList<SanPham> sp = new ArrayList<SanPham>();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT sp.maSP, sp.maDanhMuc, dm.tenDanhMuc, "
                    + "sp.maThuongHieu, th.tenThuongHieu, sp.tenSP, sp.hoatDong, sp.giaBan "
                    + "FROM SanPham sp "
                    + "JOIN DanhMuc dm ON sp.maDanhMuc = dm.maDanhMuc "
                    + "JOIN ThuongHieu th ON sp.maThuongHieu = th.maThuongHieu "
                    + "WHERE sp.tenSP LIKE ? ORDER BY sp.maSP";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setNString(1, "%" + tenSP + "%");
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String maSP = rs.getString("maSP");
                        String tenSanPham = rs.getString("tenSP");
                        DanhMuc dm = new DanhMuc(rs.getString("maDanhMuc"), rs.getString("tenDanhMuc"));
                        ThuongHieu th = new ThuongHieu(rs.getString("maThuongHieu"), rs.getString("tenThuongHieu"));
                        boolean hoatDong = rs.getBoolean("hoatDong");
                        BigDecimal giaBan = rs.getBigDecimal("giaBan");
                        SanPham sPham = new SanPham(maSP, dm, th, tenSanPham, hoatDong, giaBan);
                        sp.add(sPham);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sp;
    }

    public boolean delete(String maSP) {
        int n = 0;
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "DELETE FROM SanPham WHERE maSP = ?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, maSP);
                n = stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public boolean update(SanPham sp) {
        int n = 0;
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "UPDATE SanPham SET maDanhMuc = ?, maThuongHieu = ?, tenSP = ?, hoatDong = ?, giaBan = ? "
                    + "WHERE maSP = ?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, sp.getDanhMuc().getMaDanhMuc());
                stmt.setString(2, sp.getThuongHieu().getMaThuongHieu());
                stmt.setNString(3, sp.getTenSP());
                stmt.setBoolean(4, sp.isHoatDong());
                stmt.setBigDecimal(5, sp.getGiaBan());
                stmt.setString(6, sp.getMaSP());
                n = stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public int getTongTon(String maSP) {
        int tongTon = 0;
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT COALESCE(SUM(soLuongTon), 0) AS tongTon FROM ChiTietSanPham WHERE maSP = ?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, maSP);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        tongTon = rs.getInt("tongTon");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tongTon;
    }
}
