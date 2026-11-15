package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;

public class HoaDon_DAO {
    public static final BigDecimal THUE_SUAT = new BigDecimal("8");

    public HoaDon_DAO() { }

    private Connection getConnection() throws SQLException {
        ConnectDB.getInstance().connect();
        return ConnectDB.getConnection();
    }

    // Dữ liệu bảng chọn giày/dép trong GUI.
    public static class SanPhamBan {
        private String maCTSP, tenSP, kichCo, mauSac;
        private BigDecimal giaBan;
        private int soLuongTon;

        public SanPhamBan(String maCTSP, String tenSP, String kichCo,
                String mauSac, BigDecimal giaBan, int soLuongTon) {
            this.maCTSP = maCTSP;
            this.tenSP = tenSP;
            this.kichCo = kichCo;
            this.mauSac = mauSac;
            this.giaBan = giaBan;
            this.soLuongTon = soLuongTon;
        }

        public String getMaCTSP() { return maCTSP; }
        public String getTenSP() { return tenSP; }
        public String getKichCo() { return kichCo; }
        public String getMauSac() { return mauSac; }
        public BigDecimal getGiaBan() { return giaBan; }
        public int getSoLuongTon() { return soLuongTon; }
    }

    public ArrayList<HoaDon> getAllTableHoaDon() throws SQLException {
        ArrayList<HoaDon> ds = new ArrayList<>();
        try (PreparedStatement stmt = getConnection().prepareStatement(
                "SELECT * FROM HoaDon ORDER BY ngayLap DESC");
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) ds.add(docHoaDon(rs));
        }
        return ds;
    }

    public HoaDon getHoaDonTheoMaHD(String maHD) throws SQLException {
        try (PreparedStatement stmt = getConnection().prepareStatement(
                "SELECT * FROM HoaDon WHERE maHD = ?")) {
            stmt.setString(1, maHD);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? docHoaDon(rs) : null;
            }
        }
    }

    private HoaDon docHoaDon(ResultSet rs) throws SQLException {
        String maKH = rs.getString("maKH");
        return new HoaDon(rs.getString("maHD"),
                new NhanVien(rs.getString("maNhanVien")),
                maKH == null ? null : new KhachHang(maKH),
                rs.getTimestamp("ngayLap").toLocalDateTime(),
                rs.getString("phuongThucThanhToan"),
                rs.getBigDecimal("thue"), rs.getBigDecimal("tongTien"));
    }

    public ArrayList<SanPhamBan> getDanhSachSanPham(String maCTSP, String tenSP)
            throws SQLException {
        ArrayList<SanPhamBan> ds = new ArrayList<>();
        String sql = "SELECT ct.maCTSP, sp.tenSP, ct.kichCo, ct.mauSac, "
                + "sp.giaBan, ct.soLuongTon FROM ChiTietSanPham ct "
                + "JOIN SanPham sp ON ct.maSP=sp.maSP "
                + "WHERE ct.hoatDong=1 AND sp.hoatDong=1 AND ct.soLuongTon>0 "
                + "AND ct.maCTSP LIKE ? AND sp.tenSP LIKE ? ORDER BY sp.tenSP";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + maCTSP.trim() + "%");
            stmt.setString(2, "%" + tenSP.trim() + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ds.add(new SanPhamBan(rs.getString("maCTSP"), rs.getString("tenSP"),
                            rs.getString("kichCo"), rs.getString("mauSac"),
                            rs.getBigDecimal("giaBan"), rs.getInt("soLuongTon")));
                }
            }
        }
        return ds;
    }

    public int getSoLuongTon(String maCTSP) throws SQLException {
        try (PreparedStatement stmt = getConnection().prepareStatement(
                "SELECT soLuongTon FROM ChiTietSanPham WHERE maCTSP=?")) {
            stmt.setString(1, maCTSP);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public KhachHang getKhachHangTheoSoDienThoai(String sdt) throws SQLException {
        try (PreparedStatement stmt = getConnection().prepareStatement(
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
        try (PreparedStatement stmt = getConnection().prepareStatement(
                "SELECT * FROM KhachHang WHERE maKH=?")) {
            stmt.setString(1, maKH);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) return null;
                return new KhachHang(rs.getString("maKH"), rs.getString("tenKH"),
                        rs.getString("soDienThoai"));
            }
        }
    }

    public KhachHang themKhachHang(String sdt, String tenKH) throws SQLException {
        if (!sdt.matches("0[0-9]{9}") || tenKH == null || tenKH.trim().isEmpty())
            throw new SQLException("Số điện thoại hoặc tên KH không hợp lệ");
        if (getKhachHangTheoSoDienThoai(sdt) != null)
            throw new SQLException("Số điện thoại đã tồn tại");
        String ma;
        try (PreparedStatement stmt = getConnection().prepareStatement(
                "SELECT COALESCE(MAX(TRY_CONVERT(INT,SUBSTRING(maKH,3,18))),0)+1 FROM KhachHang");
                ResultSet rs = stmt.executeQuery()) {
            rs.next();
            ma = String.format("KH%06d", rs.getInt(1));
        }
        try (PreparedStatement stmt = getConnection().prepareStatement(
                "INSERT INTO KhachHang(maKH,tenKH,soDienThoai) VALUES (?,?,?)")) {
            stmt.setString(1, ma);
            stmt.setNString(2, tenKH.trim());
            stmt.setString(3, sdt);
            stmt.executeUpdate();
        }
        return new KhachHang(ma, tenKH.trim(), sdt);
    }

    public String taoMaHoaDonMoi() throws SQLException {
        try (PreparedStatement stmt = getConnection().prepareStatement(
                "SELECT COALESCE(MAX(TRY_CONVERT(INT,SUBSTRING(maHD,3,18))),0)+1 FROM HoaDon");
                ResultSet rs = stmt.executeQuery()) {
            rs.next();
            return String.format("HD%06d", rs.getInt(1));
        }
    }

    // Luu hoa don, chi tiet va tru kho. Neu mot buoc loi thi rollback.
    public String create(HoaDon hd, ArrayList<ChiTietHoaDon> ds) throws SQLException {
        if (hd == null || hd.getNhanVien() == null || hd.getMaHD() == null || hd.getNgayLap() == null
                || ds == null || ds.isEmpty())
            throw new SQLException("Thiếu dữ liệu hóa đơn");
        Connection con = getConnection();
        boolean auto = con.getAutoCommit();
        try {
            con.setAutoCommit(false);
            hd.tinhTongTien(ds);
            String sql = "INSERT INTO HoaDon(maHD,maNhanVien,maKH,ngayLap,phuongThucThanhToan,thue,tongTien) "
                    + "VALUES (?,?,?,?,?,?,?)";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, hd.getMaHD());
                stmt.setString(2, hd.getNhanVien().getMaNhanVien());
                if (hd.getKhachHang() == null) stmt.setNull(3, Types.VARCHAR);
                else stmt.setString(3, hd.getKhachHang().getMaKH());
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
                    if (ct.getSoLuong() <= 0 || ct.getDonGia() == null ||
                            ct.getDonGia().compareTo(BigDecimal.ZERO) <= 0)
                        throw new SQLException("Chi tiết hóa đơn không hợp lệ");
                    stmt.setInt(1, ct.getSoLuong());
                    stmt.setString(2, ct.getChiTietSanPham().getMaCTSP());
                    stmt.setInt(3, ct.getSoLuong());
                    if (stmt.executeUpdate() != 1)
                        throw new SQLException("Sản phẩm không đủ tồn kho");
                    ChiTietHoaDon luu = new ChiTietHoaDon(new HoaDon(hd.getMaHD()),
                            ct.getChiTietSanPham(), ct.getSoLuong(), ct.getDonGia());
                    ctDao.create(con, luu);
                }
            }
            con.commit();
            return hd.getMaHD();
        } catch (SQLException | RuntimeException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(auto);
        }
    }
}
