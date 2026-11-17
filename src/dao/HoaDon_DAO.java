package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.ChiTietHoaDon;
import entity.ChiTietSanPham;
import entity.SanPham;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;

public class HoaDon_DAO {
    public static final BigDecimal THUE_SUAT = new BigDecimal("8");

    public HoaDon_DAO() { }


    public ArrayList<HoaDon> getAllTableHoaDon() throws SQLException {
        ArrayList<HoaDon> ds = new ArrayList<>();
        try (PreparedStatement stmt = ConnectDB.getConnection().prepareStatement(
                "SELECT h.*, nv.tenNhanVien, k.tenKH, k.soDienThoai FROM HoaDon h "
                + "JOIN NhanVien nv ON h.maNhanVien = nv.maNhanVien "
                + "LEFT JOIN KhachHang k ON h.maKH = k.maKH "
                + "ORDER BY h.ngayLap DESC");
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) ds.add(docHoaDon(rs));
        }
        return ds;
    }

    // Tìm các hóa đơn của khách theo SĐT khi khách làm mất hóa đơn giấy.
    public ArrayList<String> getMaHoaDonTheoSoDienThoai(String sdt) throws SQLException {
        ArrayList<String> ds = new ArrayList<>();
        String sql = "SELECT h.maHD FROM HoaDon h JOIN KhachHang k ON h.maKH=k.maKH "
                + "WHERE k.soDienThoai=? ORDER BY h.ngayLap DESC";
        try (PreparedStatement stmt = ConnectDB.getConnection().prepareStatement(sql)) {
            stmt.setString(1, sdt.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) ds.add(rs.getString("maHD"));
            }
        }
        return ds;
    }

    public HoaDon getHoaDonTheoMaHD(String maHD) throws SQLException {
        try (PreparedStatement stmt = ConnectDB.getConnection().prepareStatement(
                "SELECT h.*, nv.tenNhanVien, k.tenKH, k.soDienThoai FROM HoaDon h "
                + "JOIN NhanVien nv ON h.maNhanVien = nv.maNhanVien "
                + "LEFT JOIN KhachHang k ON h.maKH = k.maKH "
                + "WHERE h.maHD = ?")) {
            stmt.setString(1, maHD);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? docHoaDon(rs) : null;
            }
        }
    }

    private HoaDon docHoaDon(ResultSet rs) throws SQLException {
        String maKH = rs.getString("maKH");
        return new HoaDon(rs.getString("maHD"),
                new NhanVien(rs.getString("maNhanVien"), rs.getString("tenNhanVien")),
                maKH == null ? null : new KhachHang(maKH, rs.getString("tenKH"), rs.getString("soDienThoai")),
                rs.getTimestamp("ngayLap").toLocalDateTime(),
                rs.getString("phuongThucThanhToan"),
                rs.getBigDecimal("thue"), rs.getBigDecimal("tongTien"));
    }

    // Lay san pham con hang de ban. Dung lai Entity cua du an.
    public ArrayList<ChiTietSanPham> getDanhSachSanPham(String maCTSP, String tenSP)
            throws SQLException {
        ArrayList<ChiTietSanPham> ds = new ArrayList<>();
        String sql = "SELECT ct.maCTSP, sp.maSP, sp.tenSP, ct.kichCo, ct.mauSac, "
                + "sp.giaBan, ct.soLuongTon FROM ChiTietSanPham ct "
                + "JOIN SanPham sp ON ct.maSP = sp.maSP "
                + "WHERE ct.hoatDong = 1 AND sp.hoatDong = 1 AND ct.soLuongTon > 0 "
                + "AND ct.maCTSP LIKE ? AND sp.tenSP LIKE ? ORDER BY sp.tenSP";
        try (PreparedStatement stmt = ConnectDB.getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + maCTSP.trim() + "%");
            stmt.setString(2, "%" + tenSP.trim() + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    SanPham sp = new SanPham(rs.getString("maSP"), null, null,
                            rs.getString("tenSP"), true, rs.getBigDecimal("giaBan"));
                    ChiTietSanPham ct = new ChiTietSanPham(rs.getString("maCTSP"), sp,
                            rs.getDouble("kichCo"), rs.getString("mauSac"),
                            rs.getInt("soLuongTon"), true);
                    ds.add(ct);
                }
            }
        }
        return ds;
    }

    public int getSoLuongTon(String maCTSP) throws SQLException {
        try (PreparedStatement stmt = ConnectDB.getConnection().prepareStatement(
                "SELECT soLuongTon FROM ChiTietSanPham WHERE maCTSP=?")) {
            stmt.setString(1, maCTSP);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public KhachHang getKhachHangTheoSoDienThoai(String sdt) throws SQLException {
        try (PreparedStatement stmt = ConnectDB.getConnection().prepareStatement(
                "SELECT * FROM KhachHang WHERE soDienThoai=?")) {
            stmt.setString(1, sdt.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) return null;
                return new KhachHang(rs.getString("maKH"), rs.getString("tenKH"),
                        rs.getString("soDienThoai"));
            }
        }
    }

    public KhachHang getKhachHangTheoMaKH(String maKH) throws SQLException {
        try (PreparedStatement stmt = ConnectDB.getConnection().prepareStatement(
                "SELECT * FROM KhachHang WHERE maKH=?")) {
            stmt.setString(1, maKH);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) return null;
                return new KhachHang(rs.getString("maKH"), rs.getString("tenKH"),
                        rs.getString("soDienThoai"));
            }
        }
    }

    // Một nhân viên thao tác một thời điểm: lấy mã lớn nhất + 1.
    private String capMa(Connection con, String bang, String cot, String tienTo) throws SQLException {
        String sql = "SELECT ISNULL(MAX(TRY_CONVERT(INT, SUBSTRING(" + cot
                + ", LEN(?) + 1, 8))), 0) + 1 FROM " + bang
                + " WHERE " + cot + " LIKE ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, tienTo);
            stmt.setString(2, tienTo + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return String.format("%s%08d", tienTo, rs.getInt(1));
            }
        }
    }

    public KhachHang themKhachHang(String sdt, String tenKH) throws SQLException {
        if (!sdt.matches("0[0-9]{9}") || tenKH.trim().isEmpty())
            throw new SQLException("Nhập tên và số điện thoại 10 chữ số");
        if (getKhachHangTheoSoDienThoai(sdt) != null)
            throw new SQLException("Số điện thoại khách hàng đã tồn tại");
        Connection con = ConnectDB.getConnection();
        String maKH = capMa(con, "KhachHang", "maKH", "KH");
        String sql = "INSERT INTO KhachHang(maKH, tenKH, soDienThoai) VALUES (?,?,?)";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maKH);
            stmt.setNString(2, tenKH.trim());
            stmt.setString(3, sdt);
            stmt.executeUpdate();
        }
        return new KhachHang(maKH, tenKH.trim(), sdt);
    }

    public String taoMaHoaDonMoi() throws SQLException {
        return capMa(ConnectDB.getConnection(), "HoaDon", "maHD", "HD");
    }

    // Luu hoa don, chi tiet va tru kho. Neu mot buoc loi thi rollback.
    public String create(HoaDon hd, ArrayList<ChiTietHoaDon> ds) throws SQLException {
        if (hd == null || hd.getNhanVien() == null || ds == null || ds.isEmpty())
            throw new SQLException("Thiếu dữ liệu hóa đơn");
        if (hd.getKhachHang() == null || hd.getKhachHang().getMaKH() == null)
            throw new SQLException("Bắt buộc chọn khách hàng có số điện thoại trước khi lưu hóa đơn");
        Connection con = ConnectDB.getConnection();
        boolean auto = con.getAutoCommit();
        try {
            con.setAutoCommit(false);
            String maHD = capMa(con, "HoaDon", "maHD", "HD");
            hd.tinhTongTien(ds);
            String sql = "INSERT INTO HoaDon(maHD,maNhanVien,maKH,ngayLap,phuongThucThanhToan,thue,tongTien) "
                    + "VALUES (?,?,?,?,?,?,?)";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, maHD);
                stmt.setString(2, hd.getNhanVien().getMaNhanVien());
                stmt.setString(3, hd.getKhachHang().getMaKH());
                stmt.setTimestamp(4, Timestamp.valueOf(hd.getNgayLap()));
                stmt.setNString(5, hd.getPhuongThucThanhToan());
                stmt.setBigDecimal(6, hd.getThue());
                stmt.setBigDecimal(7, hd.getTongTien());
                stmt.executeUpdate();
            }
            ChiTietHoaDon_DAO ctDao = new ChiTietHoaDon_DAO();
            String sqlTon = "UPDATE ct SET soLuongTon=soLuongTon-? FROM ChiTietSanPham ct "
                    + "JOIN SanPham sp ON ct.maSP=sp.maSP "
                    + "WHERE ct.maCTSP=? AND ct.soLuongTon>=? "
                    + "AND ct.hoatDong=1 AND sp.hoatDong=1";
            try (PreparedStatement stmt = con.prepareStatement(sqlTon)) {
                for (ChiTietHoaDon ct : ds) {
                    if (ct.getSoLuong() <= 0)
                        throw new SQLException("Chi tiết hóa đơn không hợp lệ");
                    stmt.setInt(1, ct.getSoLuong());
                    stmt.setString(2, ct.getChiTietSanPham().getMaCTSP());
                    stmt.setInt(3, ct.getSoLuong());
                    if (stmt.executeUpdate() == 0)
                        throw new SQLException("Sản phẩm không đủ tồn kho");
                    ChiTietHoaDon luu = new ChiTietHoaDon(new HoaDon(maHD),
                            ct.getChiTietSanPham(), ct.getSoLuong(), ct.getDonGia());
                    ctDao.create(con, luu);
                }
            }
            con.commit();
            return maHD;
        } catch (SQLException | RuntimeException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(auto);
        }
    }
}
