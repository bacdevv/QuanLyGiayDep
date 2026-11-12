package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.ChiTietHoaDon;
import entity.ChiTietSanPham;
import entity.HoaDon;
import entity.SanPham;

public class ChiTietHoaDon_DAO {
    public ChiTietHoaDon_DAO() {
    }

    private Connection getConnection() throws SQLException {
        Connection con = ConnectDB.getConnection();
        if (con == null || con.isClosed()) {
            ConnectDB.getInstance().connect();
            con = ConnectDB.getConnection();
        }
        return con;
    }

    private ChiTietHoaDon docChiTiet(ResultSet rs) throws SQLException {
        SanPham sp = new SanPham(rs.getString("maSP"), null, null,
                rs.getString("tenSP"), rs.getBoolean("spHoatDong"), rs.getBigDecimal("giaBan"));
        ChiTietSanPham ctsp = new ChiTietSanPham(rs.getString("maCTSP"), sp,
                rs.getString("kichCo"), rs.getString("mauSac"),
                rs.getInt("soLuongTon"), rs.getBoolean("ctHoatDong"));
        return new ChiTietHoaDon(new HoaDon(rs.getString("maHD")), ctsp,
                rs.getInt("soLuong"), rs.getBigDecimal("donGia"));
    }

    private String sqlDoc() {
        return "SELECT hd.maHD, hd.maCTSP, hd.soLuong, hd.donGia, "
                + "ct.maSP, ct.kichCo, ct.mauSac, ct.soLuongTon, "
                + "ct.hoatDong AS ctHoatDong, sp.tenSP, sp.giaBan, "
                + "sp.hoatDong AS spHoatDong "
                + "FROM ChiTietHoaDon hd "
                + "JOIN ChiTietSanPham ct ON hd.maCTSP = ct.maCTSP "
                + "JOIN SanPham sp ON ct.maSP = sp.maSP ";
    }

    public ArrayList<ChiTietHoaDon> getAllTableChiTietHoaDon() throws SQLException {
        ArrayList<ChiTietHoaDon> ds = new ArrayList<>();
        try (PreparedStatement stmt = getConnection().prepareStatement(sqlDoc());
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) ds.add(docChiTiet(rs));
        }
        return ds;
    }

    public ArrayList<ChiTietHoaDon> getChiTietTheoMaHD(String maHD) throws SQLException {
        ArrayList<ChiTietHoaDon> ds = new ArrayList<>();
        try (PreparedStatement stmt = getConnection().prepareStatement(
                sqlDoc() + "WHERE hd.maHD = ? ORDER BY hd.maCTSP")) {
            stmt.setString(1, maHD);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) ds.add(docChiTiet(rs));
            }
        }
        return ds;
    }

    // Goi trong transaction cua HoaDon_DAO, khong tu commit.
    public boolean create(Connection con, ChiTietHoaDon ct) throws SQLException {
        String sql = "INSERT INTO ChiTietHoaDon (maHD, maCTSP, soLuong, donGia) "
                + "VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, ct.getHoaDon().getMaHD());
            stmt.setString(2, ct.getChiTietSanPham().getMaCTSP());
            stmt.setInt(3, ct.getSoLuong());
            stmt.setBigDecimal(4, ct.getDonGia());
            return stmt.executeUpdate() == 1;
        }
    }
}
