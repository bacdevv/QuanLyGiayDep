package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.ChiTietDoiTra;
import entity.ChiTietSanPham;
import entity.PhieuDoiTra;
import entity.SanPham;

public class ChiTietDoiTra_DAO {
    public ChiTietDoiTra_DAO() {
    }

    private Connection getConnection() throws SQLException {
        Connection con = ConnectDB.getConnection();
        if (con == null || con.isClosed()) {
            ConnectDB.getInstance().connect();
            con = ConnectDB.getConnection();
        }
        return con;
    }

    private String sqlDoc() {
        return "SELECT dt.maPDT, dt.maCTSP, dt.soLuong, dt.soLuongNhapLai, "
                + "ct.maSP, ct.kichCo, ct.mauSac, ct.soLuongTon, "
                + "ct.hoatDong AS ctHoatDong, sp.tenSP, sp.giaBan, "
                + "sp.hoatDong AS spHoatDong "
                + "FROM ChiTietDoiTra dt "
                + "JOIN ChiTietSanPham ct ON dt.maCTSP = ct.maCTSP "
                + "JOIN SanPham sp ON ct.maSP = sp.maSP ";
    }

    private ChiTietDoiTra docChiTiet(ResultSet rs) throws SQLException {
        SanPham sp = new SanPham(rs.getString("maSP"), null, null,
                rs.getString("tenSP"), rs.getBoolean("spHoatDong"), rs.getBigDecimal("giaBan"));
        ChiTietSanPham ctsp = new ChiTietSanPham(rs.getString("maCTSP"), sp,
                rs.getString("kichCo"), rs.getString("mauSac"),
                rs.getInt("soLuongTon"), rs.getBoolean("ctHoatDong"));
        return new ChiTietDoiTra(new PhieuDoiTra(rs.getString("maPDT")), ctsp,
                rs.getInt("soLuong"), rs.getInt("soLuongNhapLai"));
    }

    public ArrayList<ChiTietDoiTra> getAllTableChiTietDoiTra() throws SQLException {
        ArrayList<ChiTietDoiTra> ds = new ArrayList<>();
        try (PreparedStatement stmt = getConnection().prepareStatement(sqlDoc());
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) ds.add(docChiTiet(rs));
        }
        return ds;
    }

    public ArrayList<ChiTietDoiTra> getChiTietTheoMaPDT(String maPDT) throws SQLException {
        ArrayList<ChiTietDoiTra> ds = new ArrayList<>();
        try (PreparedStatement stmt = getConnection().prepareStatement(
                sqlDoc() + "WHERE dt.maPDT = ? ORDER BY dt.maCTSP")) {
            stmt.setString(1, maPDT);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) ds.add(docChiTiet(rs));
            }
        }
        return ds;
    }

    public boolean create(Connection con, ChiTietDoiTra ct) throws SQLException {
        String sql = "INSERT INTO ChiTietDoiTra "
                + "(maPDT, maCTSP, soLuong, soLuongNhapLai) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, ct.getPhieuDoiTra().getMaPDT());
            stmt.setString(2, ct.getChiTietSanPham().getMaCTSP());
            stmt.setInt(3, ct.getSoLuong());
            stmt.setInt(4, ct.getSoLuongNhapLai());
            return stmt.executeUpdate() == 1;
        }
    }
}
