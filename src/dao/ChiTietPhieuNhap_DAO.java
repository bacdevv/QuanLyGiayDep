package dao;

import connectDB.ConnectDB;
import entity.ChiTietPhieuNhap;
import entity.ChiTietSanPham;
import entity.PhieuNhap;
import entity.SanPham;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ChiTietPhieuNhap_DAO {

    public ChiTietPhieuNhap_DAO() {
    }
// Lấy chi tiết phiếu nhập theo mã phiếu nhập
    public ArrayList<ChiTietPhieuNhap> layChiTietTheoMaPN(String maPN) {
        ArrayList<ChiTietPhieuNhap> ds = new ArrayList<>();

        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();

            String sql = "SELECT ct.maPN, ct.maCTSP, ct.soLuong, ct.donGiaNhap, "
                    + "ctsp.maSP, ctsp.kichCo, ctsp.mauSac, ctsp.soLuongTon, ctsp.hoatDong AS ctHoatDong, "
                    + "sp.tenSP, sp.giaBan, sp.hoatDong AS spHoatDong "
                    + "FROM ChiTietPhieuNhap ct "
                    + "JOIN ChiTietSanPham ctsp ON ct.maCTSP = ctsp.maCTSP "
                    + "JOIN SanPham sp ON ctsp.maSP = sp.maSP "
                    + "WHERE ct.maPN = ? ORDER BY ct.maCTSP";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, maPN);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String maCTSP = rs.getString("maCTSP");
                int soLuong = rs.getInt("soLuong");
                BigDecimal donGiaNhap = rs.getBigDecimal("donGiaNhap");

                SanPham sp = new SanPham(rs.getString("maSP"), null, null,
                        rs.getString("tenSP"), rs.getBoolean("spHoatDong"),
                        rs.getBigDecimal("giaBan"));

                ChiTietSanPham ctsp = new ChiTietSanPham(maCTSP, sp,
                        rs.getString("kichCo"), rs.getString("mauSac"),
                        rs.getInt("soLuongTon"), rs.getBoolean("ctHoatDong"));

                ds.add(new ChiTietPhieuNhap(new PhieuNhap(maPN), ctsp,
                        soLuong, donGiaNhap));
            }

            rs.close();
            stmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

    // Thêm chi tiết phiếu nhập
    public boolean themChiTietPhieuNhap(ChiTietPhieuNhap ct) {
        int n = 0;
        Connection con = null;

        try {
            ConnectDB.getInstance();
            con = ConnectDB.getConnection();
            con.setAutoCommit(false);

            String sql = "INSERT INTO ChiTietPhieuNhap (maPN, maCTSP, soLuong, donGiaNhap) "
                    + "VALUES (?, ?, ?, ?)";

            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, ct.getPhieuNhap().getMaPN());
            stmt.setString(2, ct.getChiTietSanPham().getMaCTSP());
            stmt.setInt(3, ct.getSoLuong());
            stmt.setBigDecimal(4, ct.getDonGiaNhap());

            n = stmt.executeUpdate();

            stmt.close();

            // Cập nhật tăng số lượng tồn
            String sqlTon = "UPDATE ChiTietSanPham SET soLuongTon = soLuongTon + ? WHERE maCTSP = ?";

            PreparedStatement stmtTon = con.prepareStatement(sqlTon);
            stmtTon.setInt(1, ct.getSoLuong());
            stmtTon.setString(2, ct.getChiTietSanPham().getMaCTSP());

            stmtTon.executeUpdate();
            stmtTon.close();

            con.commit();

        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            n = 0;
            e.printStackTrace();

        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return n > 0;
    }

    // Cập nhật chi tiết phiếu nhập
    public boolean capNhatChiTietPhieuNhap(ChiTietPhieuNhap ct, int soLuongCu) {
        int n = 0;
        Connection con = null;

        try {
            ConnectDB.getInstance();
            con = ConnectDB.getConnection();
            con.setAutoCommit(false);

            String sql = "UPDATE ChiTietPhieuNhap SET soLuong = ?, donGiaNhap = ? "
                    + "WHERE maPN = ? AND maCTSP = ?";

            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, ct.getSoLuong());
            stmt.setBigDecimal(2, ct.getDonGiaNhap());
            stmt.setString(3, ct.getPhieuNhap().getMaPN());
            stmt.setString(4, ct.getChiTietSanPham().getMaCTSP());

            n = stmt.executeUpdate();
            stmt.close();

            // Tính chênh lệch số lượng mới và số lượng cũ
            int chenhLech = ct.getSoLuong() - soLuongCu;

            String sqlTon = "UPDATE ChiTietSanPham SET soLuongTon = soLuongTon + ? WHERE maCTSP = ?";

            PreparedStatement stmtTon = con.prepareStatement(sqlTon);
            stmtTon.setInt(1, chenhLech);
            stmtTon.setString(2, ct.getChiTietSanPham().getMaCTSP());

            stmtTon.executeUpdate();
            stmtTon.close();

            con.commit();

        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            n = 0;
            e.printStackTrace();

        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return n > 0;
    }

    // Xóa tất cả chi tiết theo mã phiếu nhập
    public boolean xoaHetChiTietTheoMaPN(String maPN) {
        Connection con = null;

        try {
            ConnectDB.getInstance();
            con = ConnectDB.getConnection();
            con.setAutoCommit(false);

            // Lấy danh sách chi tiết để giảm tồn kho
            String sqlCheck = "SELECT maCTSP, soLuong FROM ChiTietPhieuNhap WHERE maPN = ?";

            PreparedStatement stmtCheck = con.prepareStatement(sqlCheck);
            stmtCheck.setString(1, maPN);

            ResultSet rs = stmtCheck.executeQuery();

            String sqlTon = "UPDATE ChiTietSanPham "
                    + "SET soLuongTon = CASE WHEN soLuongTon >= ? THEN soLuongTon - ? ELSE 0 END "
                    + "WHERE maCTSP = ?";

            PreparedStatement stmtTon = con.prepareStatement(sqlTon);

            while (rs.next()) {
                String maCTSP = rs.getString("maCTSP");
                int sl = rs.getInt("soLuong");

                stmtTon.setInt(1, sl);
                stmtTon.setInt(2, sl);
                stmtTon.setString(3, maCTSP);

                stmtTon.executeUpdate();
            }

            rs.close();
            stmtCheck.close();
            stmtTon.close();

            // Xóa tất cả chi tiết phiếu nhập
            String sqlDel = "DELETE FROM ChiTietPhieuNhap WHERE maPN = ?";

            PreparedStatement stmtDel = con.prepareStatement(sqlDel);
            stmtDel.setString(1, maPN);

            stmtDel.executeUpdate();
            stmtDel.close();

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
                if (con != null) {
                    con.setAutoCommit(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Xóa một chi tiết phiếu nhập
    public boolean xoaChiTietPhieuNhap(String maPN, String maCTSP) {
        int n = 0;
        Connection con = null;

        try {
            ConnectDB.getInstance();
            con = ConnectDB.getConnection();
            con.setAutoCommit(false);

         
            int soLuong = 0;

            String sqlCheck = "SELECT soLuong FROM ChiTietPhieuNhap WHERE maPN = ? AND maCTSP = ?";

            PreparedStatement stmtCheck = con.prepareStatement(sqlCheck);
            stmtCheck.setString(1, maPN);
            stmtCheck.setString(2, maCTSP);

            ResultSet rs = stmtCheck.executeQuery();

            if (rs.next()) {
                soLuong = rs.getInt("soLuong");
            }

            rs.close();
            stmtCheck.close();

            // Xóa chi tiết phiếu nhập
            String sqlDel = "DELETE FROM ChiTietPhieuNhap WHERE maPN = ? AND maCTSP = ?";

            PreparedStatement stmtDel = con.prepareStatement(sqlDel);
            stmtDel.setString(1, maPN);
            stmtDel.setString(2, maCTSP);

            n = stmtDel.executeUpdate();
            stmtDel.close();

            // Trừ lại số lượng tồn kho
            String sqlTon = "UPDATE ChiTietSanPham "
                    + "SET soLuongTon = CASE WHEN soLuongTon >= ? THEN soLuongTon - ? ELSE 0 END "
                    + "WHERE maCTSP = ?";

            PreparedStatement stmtTon = con.prepareStatement(sqlTon);
            stmtTon.setInt(1, soLuong);
            stmtTon.setInt(2, soLuong);
            stmtTon.setString(3, maCTSP);

            stmtTon.executeUpdate();
            stmtTon.close();

            con.commit();

        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            n = 0;
            e.printStackTrace();

        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return n > 0;
    }

    // Kiểm tra chi tiết phiếu nhập đã tồn tại hay chưa
    public boolean kiemTraTonTai(String maPN, String maCTSP) {
        boolean tonTai = false;

        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();

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

    // Lấy tổng tiền theo mã phiếu nhập
    public BigDecimal layTongTienTheoMaPN(String maPN) {
        BigDecimal tong = BigDecimal.ZERO;

        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();

            String sql = "SELECT SUM(soLuong * donGiaNhap) AS tongTien "
                    + "FROM ChiTietPhieuNhap WHERE maPN = ?";

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
