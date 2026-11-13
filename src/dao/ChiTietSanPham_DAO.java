package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.ChiTietSanPham;
import entity.SanPham;

public class ChiTietSanPham_DAO {
    public ChiTietSanPham_DAO() {
    }

    public ArrayList<ChiTietSanPham> getalltbChiTietSanPham() {
        ArrayList<ChiTietSanPham> ctsp = new ArrayList<ChiTietSanPham>();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT maCTSP, maSP, kichCo, mauSac, soLuongTon, hoatDong FROM ChiTietSanPham "
                    + "ORDER BY maCTSP";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String maCTSP = rs.getString("maCTSP");
                        SanPham sp = new SanPham(rs.getString("maSP"));
                        String kichCo = rs.getString("kichCo");
                        String mauSac = rs.getString("mauSac");
                        int soLuongTon = rs.getInt("soLuongTon");
                        boolean hoatDong = rs.getBoolean("hoatDong");
                        ChiTietSanPham ctSP = new ChiTietSanPham(maCTSP, sp, kichCo, mauSac, soLuongTon, hoatDong);
                        ctsp.add(ctSP);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ctsp;
    }

    public boolean create(ChiTietSanPham ctsp) {
        int n = 0;
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "INSERT INTO ChiTietSanPham (maCTSP, maSP, kichCo, mauSac, soLuongTon, hoatDong) "
                    + "VALUES (?, ?, ?, ?, 0, ?)";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, ctsp.getMaCTSP());
                stmt.setString(2, ctsp.getSanPham().getMaSP());
                stmt.setString(3, ctsp.getKichCo());
                stmt.setNString(4, ctsp.getMauSac());
                stmt.setBoolean(5, ctsp.isHoatDong());
                n = stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public ArrayList<ChiTietSanPham> getChiTietSanPhamTheoMaCTSP(String id) {
        ArrayList<ChiTietSanPham> ctsp = new ArrayList<ChiTietSanPham>();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT maCTSP, maSP, kichCo, mauSac, soLuongTon, hoatDong FROM ChiTietSanPham "
                    + "WHERE maCTSP = ?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String maCTSP = rs.getString("maCTSP");
                        SanPham sp = new SanPham(rs.getString("maSP"));
                        String kichCo = rs.getString("kichCo");
                        String mauSac = rs.getString("mauSac");
                        int soLuongTon = rs.getInt("soLuongTon");
                        boolean hoatDong = rs.getBoolean("hoatDong");
                        ChiTietSanPham ctSP = new ChiTietSanPham(maCTSP, sp, kichCo, mauSac, soLuongTon, hoatDong);
                        ctsp.add(ctSP);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ctsp;
    }

    public ArrayList<ChiTietSanPham> getChiTietSanPhamTheoMaSP(String maSP) {
        ArrayList<ChiTietSanPham> ctsp = new ArrayList<ChiTietSanPham>();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT maCTSP, maSP, kichCo, mauSac, soLuongTon, hoatDong FROM ChiTietSanPham "
                    + "WHERE maSP = ? ORDER BY maCTSP";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, maSP);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String maCTSP = rs.getString("maCTSP");
                        SanPham sp = new SanPham(rs.getString("maSP"));
                        String kichCo = rs.getString("kichCo");
                        String mauSac = rs.getString("mauSac");
                        int soLuongTon = rs.getInt("soLuongTon");
                        boolean hoatDong = rs.getBoolean("hoatDong");
                        ChiTietSanPham ctSP = new ChiTietSanPham(maCTSP, sp, kichCo, mauSac, soLuongTon, hoatDong);
                        ctsp.add(ctSP);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ctsp;
    }

    public boolean update(ChiTietSanPham ctsp) {
        int n = 0;
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "UPDATE ChiTietSanPham SET kichCo = ?, mauSac = ?, hoatDong = ? "
                    + "WHERE maCTSP = ? AND maSP = ?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, ctsp.getKichCo());
                stmt.setNString(2, ctsp.getMauSac());
                stmt.setBoolean(3, ctsp.isHoatDong());
                stmt.setString(4, ctsp.getMaCTSP());
                stmt.setString(5, ctsp.getSanPham().getMaSP());
                n = stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public boolean delete(String maCTSP, String maSP) {
        int n = 0;
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "DELETE FROM ChiTietSanPham WHERE maCTSP = ? AND maSP = ?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, maCTSP);
                stmt.setString(2, maSP);
                n = stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }
}
