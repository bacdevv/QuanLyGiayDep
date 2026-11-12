package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.ChiTietSanPham;
import entity.SanPham;

public class ChiTietSanPham_DAO {
    public ChiTietSanPham_DAO() {
    }

    public ArrayList<ChiTietSanPham> getalltbChiTietSanPham() {
        ArrayList<ChiTietSanPham> dsCTSP = new ArrayList<ChiTietSanPham>();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "SELECT * FROM ChiTietSanPham ORDER BY maCTSP";
            try (Statement statement = con.createStatement();
                    ResultSet rs = statement.executeQuery(sql)) {
                while (rs.next()) {
                    String maCTSP = rs.getString("maCTSP");
                    SanPham sp = new SanPham(rs.getString("maSP"));
                    String kichCo = rs.getString("kichCo");
                    String mauSac = rs.getString("mauSac");
                    int soLuongTon = rs.getInt("soLuongTon");
                    boolean hoatDong = rs.getBoolean("hoatDong");
                    ChiTietSanPham ctsp = new ChiTietSanPham(maCTSP, sp, kichCo, mauSac, soLuongTon, hoatDong);
                    dsCTSP.add(ctsp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsCTSP;
    }

    public boolean create(ChiTietSanPham ctsp) {
        int n = 0;
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        try {

            stmt = con.prepareStatement("INSERT INTO ChiTietSanPham " + "VALUES (?, ?, ?, ?, 0, ?)");
            stmt.setString(1, ctsp.getMaCTSP());
            stmt.setString(2, ctsp.getSanPham().getMaSP());
            stmt.setString(3, ctsp.getKichCo());
            stmt.setNString(4, ctsp.getMauSac());
            stmt.setBoolean(5, ctsp.isHoatDong());
            n = stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return n > 0;
    }

    public ArrayList<ChiTietSanPham> getChiTietSanPhamTheoMaCTSP(String id) {
        ArrayList<ChiTietSanPham> dsCTSP = new ArrayList<ChiTietSanPham>();
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement statement = null;
        try {
            String sql = "SELECT * FROM ChiTietSanPham WHERE maCTSP = ?";
            statement = con.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String maCTSP = rs.getString("maCTSP");
                SanPham sp = new SanPham(rs.getString("maSP"));
                String kichCo = rs.getString("kichCo");
                String mauSac = rs.getString("mauSac");
                int soLuongTon = rs.getInt("soLuongTon");
                boolean hoatDong = rs.getBoolean("hoatDong");
                ChiTietSanPham ctsp = new ChiTietSanPham(maCTSP, sp, kichCo,
                        mauSac, soLuongTon, hoatDong);
                dsCTSP.add(ctsp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return dsCTSP;
    }

    public ArrayList<ChiTietSanPham> getChiTietSanPhamTheoMaSP(String maSP) {
        ArrayList<ChiTietSanPham> dsCTSP = new ArrayList<ChiTietSanPham>();
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement statement = null;
        try {
            String sql = "SELECT * FROM ChiTietSanPham WHERE maSP = ? ORDER BY maCTSP";
            statement = con.prepareStatement(sql);
            statement.setString(1, maSP);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String maCTSP = rs.getString("maCTSP");
                SanPham sp = new SanPham(rs.getString("maSP"));
                String kichCo = rs.getString("kichCo");
                String mauSac = rs.getString("mauSac");
                int soLuongTon = rs.getInt("soLuongTon");
                boolean hoatDong = rs.getBoolean("hoatDong");
                ChiTietSanPham ctsp = new ChiTietSanPham(maCTSP, sp, kichCo,
                        mauSac, soLuongTon, hoatDong);
                dsCTSP.add(ctsp);

            }
        } catch (

        SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return dsCTSP;
    }

    public boolean update(ChiTietSanPham ctsp) {
        int n = 0;
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("UPDATE ChiTietSanPham SET kichCo = ?, mauSac = ?, "
                    + "hoatDong = ? WHERE maCTSP = ? AND maSP = ?");
            stmt.setString(1, ctsp.getKichCo());
            stmt.setNString(2, ctsp.getMauSac());
            stmt.setBoolean(3, ctsp.isHoatDong());
            stmt.setString(4, ctsp.getMaCTSP());
            stmt.setString(5, ctsp.getSanPham().getMaSP());
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return n > 0;
    }
}
