package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.ChiTietDoiTra;
import entity.HoaDon;
import entity.NhanVien;
import entity.PhieuDoiTra;

public class PhieuDoiTra_DAO {
    public PhieuDoiTra_DAO() { }

    private Connection getConnection() throws SQLException {
        ConnectDB.getInstance().connect();
        return ConnectDB.getConnection();
    }

    public static class HangDaMua {
        private String maCTSP, tenSP, kichCo, mauSac;
        private int daMua, daTra;
        private BigDecimal donGia;

        public HangDaMua(String maCTSP, String tenSP, String kichCo,
                String mauSac, int daMua, int daTra, BigDecimal donGia) {
            this.maCTSP = maCTSP;
            this.tenSP = tenSP;
            this.kichCo = kichCo;
            this.mauSac = mauSac;
            this.daMua = daMua;
            this.daTra = daTra;
            this.donGia = donGia;
        }

        public String getMaCTSP() { return maCTSP; }
        public String getTenSP() { return tenSP; }
        public String getKichCo() { return kichCo; }
        public String getMauSac() { return mauSac; }
        public int getDaMua() { return daMua; }
        public int getDaTra() { return daTra; }
        public int getConLai() { return daMua - daTra; }
        public BigDecimal getDonGia() { return donGia; }
    }

    public ArrayList<PhieuDoiTra> getAllTablePhieuDoiTra() throws SQLException {
        ArrayList<PhieuDoiTra> ds = new ArrayList<>();
        try (PreparedStatement stmt = getConnection().prepareStatement(
                "SELECT * FROM PhieuDoiTra ORDER BY ngayLap DESC");
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) ds.add(docPhieu(rs));
        }
        return ds;
    }

    public PhieuDoiTra getPhieuTheoMaPDT(String maPDT) throws SQLException {
        try (PreparedStatement stmt = getConnection().prepareStatement(
                "SELECT * FROM PhieuDoiTra WHERE maPDT=?")) {
            stmt.setString(1, maPDT);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? docPhieu(rs) : null;
            }
        }
    }

    private PhieuDoiTra docPhieu(ResultSet rs) throws SQLException {
        String hdMoi = rs.getString("hoaDonMoi");
        return new PhieuDoiTra(rs.getString("maPDT"),
                new HoaDon(rs.getString("hoaDonGoc")),
                hdMoi == null ? null : new HoaDon(hdMoi),
                new NhanVien(rs.getString("maNhanVien")),
                rs.getTimestamp("ngayLap").toLocalDateTime(),
                rs.getString("lyDo"), rs.getString("trangThai"));
    }

    public ArrayList<HangDaMua> getHangDaMua(String maHD) throws SQLException {
        ArrayList<HangDaMua> ds = new ArrayList<>();
        String sql = "SELECT c.maCTSP, sp.tenSP, ct.kichCo, ct.mauSac, "
                + "c.soLuong AS daMua, COALESCE(dt.daTra,0) AS daTra, c.donGia "
                + "FROM ChiTietHoaDon c "
                + "JOIN ChiTietSanPham ct ON c.maCTSP=ct.maCTSP "
                + "JOIN SanPham sp ON ct.maSP=sp.maSP "
                + "LEFT JOIN (SELECT d.maCTSP, p.hoaDonGoc, SUM(d.soLuong) AS daTra "
                + "FROM ChiTietDoiTra d JOIN PhieuDoiTra p ON d.maPDT=p.maPDT "
                + "GROUP BY d.maCTSP,p.hoaDonGoc) dt "
                + "ON dt.maCTSP=c.maCTSP AND dt.hoaDonGoc=c.maHD "
                + "WHERE c.maHD=? ORDER BY c.maCTSP";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, maHD);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ds.add(new HangDaMua(rs.getString("maCTSP"), rs.getString("tenSP"),
                            rs.getString("kichCo"), rs.getString("mauSac"),
                            rs.getInt("daMua"), rs.getInt("daTra"), rs.getBigDecimal("donGia")));
                }
            }
        }
        return ds;
    }

    private int soLuongDaMua(Connection con, String maHD, String maCTSP) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(
                "SELECT soLuong FROM ChiTietHoaDon WHERE maHD=? AND maCTSP=?")) {
            stmt.setString(1, maHD);
            stmt.setString(2, maCTSP);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private int soLuongDaTra(Connection con, String maHD, String maCTSP) throws SQLException {
        String sql = "SELECT COALESCE(SUM(c.soLuong),0) FROM ChiTietDoiTra c "
                + "JOIN PhieuDoiTra p ON p.maPDT=c.maPDT "
                + "WHERE p.hoaDonGoc=? AND c.maCTSP=?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maHD);
            stmt.setString(2, maCTSP);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    private String taoMa(Connection con) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(
                "SELECT COALESCE(MAX(TRY_CONVERT(INT,SUBSTRING(maPDT,3,18))),0)+1 "
                        + "FROM PhieuDoiTra WHERE maPDT LIKE 'DT%'");
                ResultSet rs = stmt.executeQuery()) {
            rs.next();
            return String.format("DT%08d", rs.getInt(1));
        }
    }

    // Phieu, chi tiet va nhap lai kho duoc luu chung mot transaction.
    public String create(PhieuDoiTra p, ArrayList<ChiTietDoiTra> ds) throws SQLException {
        if (p == null || p.getHoaDonGoc() == null || p.getNhanVien() == null
                || p.getLyDo() == null || p.getLyDo().trim().isEmpty()
                || ds == null || ds.isEmpty())
            throw new SQLException("Thiếu dữ liệu phiếu đổi trả");
        boolean doi = "Đổi size".equals(p.getTrangThai());
        if (!doi && !"Trả hàng".equals(p.getTrangThai()))
            throw new SQLException("Trạng thái không hợp lệ");
        if (doi && p.getHoaDonMoi() == null)
            throw new SQLException("Đổi size cần có hóa đơn mới");
        if (!doi && p.getHoaDonMoi() != null)
            throw new SQLException("Trả hàng không cần hóa đơn mới");

        Connection con = getConnection();
        boolean auto = con.getAutoCommit();
        try {
            con.setAutoCommit(false);
            String maHD = p.getHoaDonGoc().getMaHD();
            String maHDMoi = doi ? p.getHoaDonMoi().getMaHD() : null;
            HoaDon hdGoc = new HoaDon_DAO().getHoaDonTheoMaHD(maHD);
            if (hdGoc == null) throw new SQLException("Không tìm thấy hóa đơn gốc");
            if (doi && (maHD.equals(maHDMoi) || new HoaDon_DAO().getHoaDonTheoMaHD(maHDMoi) == null))
                throw new SQLException("Hóa đơn mới không hợp lệ");
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(hdGoc.getNgayLap()) || now.isAfter(hdGoc.getNgayLap().plusDays(7)))
                throw new SQLException("Hóa đơn đã quá 7 ngày hoặc ngày lập không hợp lệ");

            String ma = taoMa(con);
            String sql = "INSERT INTO PhieuDoiTra(maPDT,hoaDonGoc,hoaDonMoi,maNhanVien,"
                    + "ngayLap,lyDo,trangThai) VALUES (?,?,?,?,?,?,?)";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, ma);
                stmt.setString(2, maHD);
                if (maHDMoi == null) stmt.setNull(3, Types.VARCHAR);
                else stmt.setString(3, maHDMoi);
                stmt.setString(4, p.getNhanVien().getMaNhanVien());
                stmt.setTimestamp(5, Timestamp.valueOf(now));
                stmt.setNString(6, p.getLyDo().trim());
                stmt.setNString(7, p.getTrangThai());
                stmt.executeUpdate();
            }
            ChiTietDoiTra_DAO ctDao = new ChiTietDoiTra_DAO();
            try (PreparedStatement stmt = con.prepareStatement(
                    "UPDATE ChiTietSanPham SET soLuongTon=soLuongTon+? WHERE maCTSP=?")) {
                for (ChiTietDoiTra ct : ds) {
                    String maCTSP = ct.getChiTietSanPham().getMaCTSP();
                    int sl = ct.getSoLuong();
                    if (sl <= 0 || ct.getSoLuongNhapLai() < 0 || ct.getSoLuongNhapLai() > sl
                            || sl > soLuongDaMua(con, maHD, maCTSP) - soLuongDaTra(con, maHD, maCTSP))
                        throw new SQLException("Số lượng trả không hợp lệ: " + maCTSP);
                    if (doi && !coSizeMoi(con, maHDMoi, maCTSP, sl))
                        throw new SQLException("Hóa đơn mới phải chứa size khác của cùng sản phẩm: " + maCTSP);
                    if (!ctDao.create(con, new ChiTietDoiTra(new PhieuDoiTra(ma),
                            ct.getChiTietSanPham(), sl, ct.getSoLuongNhapLai())))
                        throw new SQLException("Không lưu được chi tiết " + maCTSP);
                    if (ct.getSoLuongNhapLai() > 0) {
                        stmt.setInt(1, ct.getSoLuongNhapLai());
                        stmt.setString(2, maCTSP);
                        if (stmt.executeUpdate() != 1) throw new SQLException("Không cập nhật được kho");
                    }
                }
            }
            con.commit();
            return ma;
        } catch (SQLException | RuntimeException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(auto);
        }
    }

    private boolean coSizeMoi(Connection con, String maHDMoi, String maCTSP, int soLuong)
            throws SQLException {
        String sql = "SELECT COUNT(*) FROM ChiTietHoaDon hd "
                + "JOIN ChiTietSanPham moi ON hd.maCTSP=moi.maCTSP "
                + "JOIN ChiTietSanPham cu ON cu.maCTSP=? "
                + "WHERE hd.maHD=? AND moi.maSP=cu.maSP "
                + "AND moi.kichCo<>cu.kichCo AND hd.soLuong>=?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maCTSP);
            stmt.setString(2, maHDMoi);
            stmt.setInt(3, soLuong);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }
}
