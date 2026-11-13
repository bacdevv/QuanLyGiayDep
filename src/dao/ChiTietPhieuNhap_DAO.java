package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.ChiTietPhieuNhap;
import entity.ChiTietSanPham;
import entity.PhieuNhap;
import entity.SanPham;

public class ChiTietPhieuNhap_DAO {

    public ChiTietPhieuNhap_DAO() {
    }

    private Connection getConnection() throws SQLException {
        Connection con = ConnectDB.getConnection();
        if (con == null || con.isClosed()) {
            ConnectDB.getInstance().connect();
            con = ConnectDB.getConnection();
        }
        return con;
    }

    public ArrayList<ChiTietPhieuNhap> layHetChiTietPhieuNhap() {
        ArrayList<ChiTietPhieuNhap> ds = new ArrayList<>();
        String sql = "SELECT ct.maPN, ct.maCTSP, ct.soLuong, ct.donGiaNhap, "
                + "ctsp.maSP, ctsp.kichCo, ctsp.mauSac, ctsp.soLuongTon, ctsp.hoatDong AS ctHoatDong, "
                + "sp.tenSP, sp.giaBan, sp.hoatDong AS spHoatDong "
                + "FROM ChiTietPhieuNhap ct "
                + "JOIN ChiTietSanPham ctsp ON ct.maCTSP = ctsp.maCTSP "
                + "JOIN SanPham sp ON ctsp.maSP = sp.maSP";
        try {
            Connection con = getConnection();
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            while (rs.next()) {
                String maPN = rs.getString("maPN");
                String maCTSP = rs.getString("maCTSP");
                int soLuong = rs.getInt("soLuong");
                BigDecimal donGiaNhap = rs.getBigDecimal("donGiaNhap");

                SanPham sp = new SanPham(rs.getString("maSP"), null, null,
                        rs.getString("tenSP"), rs.getBoolean("spHoatDong"), rs.getBigDecimal("giaBan"));
                ChiTietSanPham ctsp = new ChiTietSanPham(maCTSP, sp,
                        rs.getString("kichCo"), rs.getString("mauSac"),
                        rs.getInt("soLuongTon"), rs.getBoolean("ctHoatDong"));

                ds.add(new ChiTietPhieuNhap(new PhieuNhap(maPN), ctsp, soLuong, donGiaNhap));
            }
            rs.close();
            stm.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    public ArrayList<ChiTietPhieuNhap> layChiTietTheoMaPN(String maPN) {
        ArrayList<ChiTietPhieuNhap> ds = new ArrayList<>();
        String sql = "SELECT ct.maPN, ct.maCTSP, ct.soLuong, ct.donGiaNhap, "
                + "ctsp.maSP, ctsp.kichCo, ctsp.mauSac, ctsp.soLuongTon, ctsp.hoatDong AS ctHoatDong, "
                + "sp.tenSP, sp.giaBan, sp.hoatDong AS spHoatDong "
                + "FROM ChiTietPhieuNhap ct "
                + "JOIN ChiTietSanPham ctsp ON ct.maCTSP = ctsp.maCTSP "
                + "JOIN SanPham sp ON ctsp.maSP = sp.maSP "
                + "WHERE ct.maPN = ? ORDER BY ct.maCTSP";
        try {
            Connection con = getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maPN);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String maCTSP = rs.getString("maCTSP");
                int soLuong = rs.getInt("soLuong");
                BigDecimal donGiaNhap = rs.getBigDecimal("donGiaNhap");

                SanPham sp = new SanPham(rs.getString("maSP"), null, null,
                        rs.getString("tenSP"), rs.getBoolean("spHoatDong"), rs.getBigDecimal("giaBan"));
                ChiTietSanPham ctsp = new ChiTietSanPham(maCTSP, sp,
                        rs.getString("kichCo"), rs.getString("mauSac"),
                        rs.getInt("soLuongTon"), rs.getBoolean("ctHoatDong"));

                ds.add(new ChiTietPhieuNhap(new PhieuNhap(maPN), ctsp, soLuong, donGiaNhap));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    public boolean themChiTietPhieuNhap(ChiTietPhieuNhap ct) {
        int n = 0;
        Connection con = null;
        PreparedStatement stmt = null;
        PreparedStatement stmtTon = null;
        try {
            con = getConnection();
            con.setAutoCommit(false);

            String sql = "INSERT INTO ChiTietPhieuNhap (maPN, maCTSP, soLuong, donGiaNhap) "
                    + "VALUES (?, ?, ?, ?)";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, ct.getPhieuNhap().getMaPN());
            stmt.setString(2, ct.getChiTietSanPham().getMaCTSP());
            stmt.setInt(3, ct.getSoLuong());
            stmt.setBigDecimal(4, ct.getDonGiaNhap());

            n = stmt.executeUpdate();

            // Cập nhật tăng số lượng tồn trong ChiTietSanPham
            String sqlTon = "UPDATE ChiTietSanPham SET soLuongTon = soLuongTon + ? WHERE maCTSP = ?";
            stmtTon = con.prepareStatement(sqlTon);
            stmtTon.setInt(1, ct.getSoLuong());
            stmtTon.setString(2, ct.getChiTietSanPham().getMaCTSP());
            stmtTon.executeUpdate();

            con.commit();
        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (stmtTon != null) stmtTon.close();
                if (con != null) con.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return n > 0;
    }

    public boolean capNhatChiTietPhieuNhap(ChiTietPhieuNhap ct, int soLuongCu) {
        int n = 0;
        Connection con = null;
        PreparedStatement stmt = null;
        PreparedStatement stmtTon = null;
        try {
            con = getConnection();
            con.setAutoCommit(false);

            String sql = "UPDATE ChiTietPhieuNhap SET soLuong = ?, donGiaNhap = ? "
                    + "WHERE maPN = ? AND maCTSP = ?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, ct.getSoLuong());
            stmt.setBigDecimal(2, ct.getDonGiaNhap());
            stmt.setString(3, ct.getPhieuNhap().getMaPN());
            stmt.setString(4, ct.getChiTietSanPham().getMaCTSP());

            n = stmt.executeUpdate();

            // Độ chênh lệch số lượng tồn = số lượng mới - số lượng cũ
            int chenhLech = ct.getSoLuong() - soLuongCu;
            String sqlTon = "UPDATE ChiTietSanPham SET soLuongTon = soLuongTon + ? WHERE maCTSP = ?";
            stmtTon = con.prepareStatement(sqlTon);
            stmtTon.setInt(1, chenhLech);
            stmtTon.setString(2, ct.getChiTietSanPham().getMaCTSP());
            stmtTon.executeUpdate();

            con.commit();
        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (stmtTon != null) stmtTon.close();
                if (con != null) con.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return n > 0;
    }

    public boolean xoaHetChiTietTheoMaPN(String maPN) {
        Connection con = null;
        PreparedStatement stmtCheck = null;
        PreparedStatement stmtTon = null;
        PreparedStatement stmtDel = null;
        try {
            con = getConnection();
            con.setAutoCommit(false);

            // Giảm tồn kho tương ứng
            String sqlCheck = "SELECT maCTSP, soLuong FROM ChiTietPhieuNhap WHERE maPN = ?";
            stmtCheck = con.prepareStatement(sqlCheck);
            stmtCheck.setString(1, maPN);
            ResultSet rs = stmtCheck.executeQuery();
            String sqlTon = "UPDATE ChiTietSanPham SET soLuongTon = CASE WHEN soLuongTon >= ? THEN soLuongTon - ? ELSE 0 END WHERE maCTSP = ?";
            stmtTon = con.prepareStatement(sqlTon);
            while (rs.next()) {
                String maCTSP = rs.getString("maCTSP");
                int sl = rs.getInt("soLuong");
                stmtTon.setInt(1, sl);
                stmtTon.setInt(2, sl);
                stmtTon.setString(3, maCTSP);
                stmtTon.executeUpdate();
            }
            rs.close();

            String sqlDel = "DELETE FROM ChiTietPhieuNhap WHERE maPN = ?";
            stmtDel = con.prepareStatement(sqlDel);
            stmtDel.setString(1, maPN);
            stmtDel.executeUpdate();

            con.commit();
            return true;
        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (stmtCheck != null) stmtCheck.close();
                if (stmtTon != null) stmtTon.close();
                if (stmtDel != null) stmtDel.close();
                if (con != null) con.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean xoaChiTietPhieuNhap(String maPN, String maCTSP) {
        int n = 0;
        Connection con = null;
        PreparedStatement stmtCheck = null;
        PreparedStatement stmtDel = null;
        PreparedStatement stmtTon = null;
        try {
            con = getConnection();
            con.setAutoCommit(false);

            // Lấy số lượng trước khi xóa để trừ lại tồn kho
            int soLuong = 0;
            String sqlCheck = "SELECT soLuong FROM ChiTietPhieuNhap WHERE maPN = ? AND maCTSP = ?";
            stmtCheck = con.prepareStatement(sqlCheck);
            stmtCheck.setString(1, maPN);
            stmtCheck.setString(2, maCTSP);
            ResultSet rs = stmtCheck.executeQuery();
            if (rs.next()) {
                soLuong = rs.getInt("soLuong");
            }
            rs.close();

            String sqlDel = "DELETE FROM ChiTietPhieuNhap WHERE maPN = ? AND maCTSP = ?";
            stmtDel = con.prepareStatement(sqlDel);
            stmtDel.setString(1, maPN);
            stmtDel.setString(2, maCTSP);
            n = stmtDel.executeUpdate();

            // Trừ lại số lượng tồn kho
            String sqlTon = "UPDATE ChiTietSanPham SET soLuongTon = CASE WHEN soLuongTon >= ? THEN soLuongTon - ? ELSE 0 END WHERE maCTSP = ?";
            stmtTon = con.prepareStatement(sqlTon);
            stmtTon.setInt(1, soLuong);
            stmtTon.setInt(2, soLuong);
            stmtTon.setString(3, maCTSP);
            stmtTon.executeUpdate();

            con.commit();
        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            try {
                if (stmtCheck != null) stmtCheck.close();
                if (stmtDel != null) stmtDel.close();
                if (stmtTon != null) stmtTon.close();
                if (con != null) con.setAutoCommit(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return n > 0;
    }

    public boolean kiemTraTonTai(String maPN, String maCTSP) {
        boolean tonTai = false;
        try {
            Connection con = getConnection();
            String sql = "SELECT 1 FROM ChiTietPhieuNhap WHERE maPN = ? AND maCTSP = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maPN);
            stmt.setString(2, maCTSP);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                tonTai = true;
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tonTai;
    }

    public BigDecimal layTongTienTheoMaPN(String maPN) {
        BigDecimal tong = BigDecimal.ZERO;
        try {
            Connection con = getConnection();
            String sql = "SELECT SUM(soLuong * donGiaNhap) AS tongTien FROM ChiTietPhieuNhap WHERE maPN = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maPN);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                BigDecimal val = rs.getBigDecimal("tongTien");
                if (val != null) {
                    tong = val;
                }
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tong;
    }
}
