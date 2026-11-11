package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.DanhMuc;
import entity.SanPham;
import entity.ThuongHieu;

public class SanPham_DAO {
    public SanPham_DAO() {
    }

    public ArrayList<SanPham> getallSanPham() {
        ArrayList<SanPham> dsSP = new ArrayList<SanPham>();
        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();
            String sql = "Select sp.maSP, sp.maDanhMuc, dm.tenDanhMuc, sp.maThuongHieu, th.tenThuongHieu, sp.tenSP, sp.hoatDong, sp.giaBan "
                    + "from SanPham sp "
                    + "join DanhMuc dm on sp.maDanhMuc = dm.maDanhMuc "
                    + "join ThuongHieu th on sp.maThuongHieu = th.maThuongHieu "
                    + "ORDER BY maSP";
            try (Statement statement = con.createStatement();
                    ResultSet rs = statement.executeQuery(sql)) {
                while (rs.next()) {
                    String maSP = rs.getString("maSP");
                    String maDanhMuc = rs.getString("maDanhMuc");
                    String maThuongHieu = rs.getString("maThuongHieu");
                    String tenSP = rs.getString("tenSP");
                    boolean hoatDong = rs.getBoolean("hoatDong");
                    BigDecimal giaBan = rs.getBigDecimal("giaBan");
                    DanhMuc dm = new DanhMuc(maDanhMuc, rs.getString("tenDanhMuc"));
                    ThuongHieu th = new ThuongHieu(maThuongHieu, rs.getString("tenThuongHieu"));
                    SanPham sp = new SanPham(maSP, dm, th, tenSP, hoatDong, giaBan);
                    dsSP.add(sp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsSP;
    }

    public boolean create(SanPham sp) {
        int n = 0;
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        try {

            stmt = con.prepareStatement("INSERT INTO SanPham (maSP, maDanhMuc, maThuongHieu, tenSP, hoatDong, giaBan) "
                    + "VALUES (?, ?, ?, ?, ?, ?)");
            stmt.setString(1, sp.getMaSP());
            stmt.setString(2, sp.getDanhMuc().getMaDanhMuc());
            stmt.setString(3, sp.getThuongHieu().getMaThuongHieu());
            stmt.setNString(4, sp.getTenSP());
            stmt.setBoolean(5, sp.isHoatDong());
            stmt.setBigDecimal(6, sp.getGiaBan());
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

    public ArrayList<SanPham> getSanPhamTheoMaSP(String id) {
        ArrayList<SanPham> dsSP = new ArrayList<SanPham>();
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement statement = null;
        try {
            String sql = "SELECT * FROM SanPham sp "
                    + "JOIN DanhMuc dm ON sp.maDanhMuc = dm.maDanhMuc "
                    + "JOIN ThuongHieu th ON sp.maThuongHieu = th.maThuongHieu "
                    + "WHERE sp.maSP = ?";
            statement = con.prepareStatement(sql);
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String maSP = rs.getString("maSP");
                String tenSP = rs.getString("tenSP");
                boolean hoatDong = rs.getBoolean("hoatDong");
                BigDecimal giaBan = rs.getBigDecimal("giaBan");
                DanhMuc dm = new DanhMuc(rs.getString("maDanhMuc"), rs.getString("tenDanhMuc"));
                ThuongHieu th = new ThuongHieu(rs.getString("maThuongHieu"), rs.getString("tenThuongHieu"));
                SanPham sp = new SanPham(maSP, dm, th, tenSP, hoatDong, giaBan);
                dsSP.add(sp);
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
        return dsSP;
    }

    public ArrayList<SanPham> getSanPhamTheoTenSP(String tenSP) {
        ArrayList<SanPham> dsSP = new ArrayList<SanPham>();
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement statement = null;
        try {
            String sql = "SELECT * FROM SanPham sp "
                    + "JOIN DanhMuc dm ON sp.maDanhMuc = dm.maDanhMuc "
                    + "JOIN ThuongHieu th ON sp.maThuongHieu = th.maThuongHieu "
                    + "WHERE sp.tenSP LIKE ? ORDER BY sp.maSP";
            statement = con.prepareStatement(sql);
            statement.setString(1, "%" + tenSP + "%");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String maSP = rs.getString("maSP");
                String tenSanPham = rs.getString("tenSP");
                boolean hoatDong = rs.getBoolean("hoatDong");
                BigDecimal giaBan = rs.getBigDecimal("giaBan");
                DanhMuc dm = new DanhMuc(rs.getString("maDanhMuc"), rs.getString("tenDanhMuc"));
                ThuongHieu th = new ThuongHieu(rs.getString("maThuongHieu"), rs.getString("tenThuongHieu"));
                SanPham sp = new SanPham(maSP, dm, th, tenSanPham, hoatDong, giaBan);
                dsSP.add(sp);
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
        return dsSP;
    }

    public boolean delete(String maSP) {
        int n = 0;
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("DELETE FROM SanPham WHERE maSP = ?");
            stmt.setString(1, maSP);
            n = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return n > 0;
    }

    public boolean update(SanPham sp) {
        int n = 0;
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = con.prepareStatement("UPDATE SanPham SET maDanhMuc = ?, maThuongHieu = ?, tenSP = ?, "
                    + "hoatDong = ?, giaBan = ? WHERE maSP = ?");

            stmt.setString(1, sp.getDanhMuc().getMaDanhMuc());
            stmt.setString(2, sp.getThuongHieu().getMaThuongHieu());
            stmt.setNString(3, sp.getTenSP());
            stmt.setBoolean(4, sp.isHoatDong());
            stmt.setBigDecimal(5, sp.getGiaBan());
            stmt.setString(6, sp.getMaSP());
            n = stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return n > 0;
    }

    public int getTongTon(String maSP) {
        int tongTon = 0;
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            stmt = con.prepareStatement("SELECT COALESCE(SUM(soLuongTon), 0) FROM ChiTietSanPham WHERE maSP = ?");
            stmt.setString(1, maSP);
            rs = stmt.executeQuery();

            if (rs.next()) {
                tongTon = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return tongTon;
    }
}
