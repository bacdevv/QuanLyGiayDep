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
import java.util.HashSet;

import connectDB.ConnectDB;
import entity.ChiTietDoiTra;
import entity.HoaDon;
import entity.NhanVien;
import entity.PhieuDoiTra;

public class PhieuDoiTra_DAO {
    public PhieuDoiTra_DAO() {
    }

    private Connection getConnection() throws SQLException {
        Connection con = ConnectDB.getConnection();
        if (con == null || con.isClosed()) {
            ConnectDB.getInstance().connect();
            con = ConnectDB.getConnection();
        }
        return con;
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

        public String getMaCTSP() {
            return maCTSP;
        }

        public String getTenSP() {
            return tenSP;
        }

        public String getKichCo() {
            return kichCo;
        }

        public String getMauSac() {
            return mauSac;
        }

        public int getDaMua() {
            return daMua;
        }

        public int getDaTra() {
            return daTra;
        }

        public int getConLai() {
            return daMua - daTra;
        }

        public BigDecimal getDonGia() {
            return donGia;
        }
    }

    private String sqlDoc() {
        return "SELECT maPDT, hoaDonGoc, hoaDonMoi, maNhanVien, "
                + "ngayLap, lyDo, trangThai FROM PhieuDoiTra ";
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

    public ArrayList<PhieuDoiTra> getAllTablePhieuDoiTra() throws SQLException {
        ArrayList<PhieuDoiTra> ds = new ArrayList<>();
        try (PreparedStatement stmt = getConnection().prepareStatement(
                sqlDoc() + "ORDER BY ngayLap DESC");
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next())
                ds.add(docPhieu(rs));
        }
        return ds;
    }

    public PhieuDoiTra getPhieuTheoMaPDT(String maPDT) throws SQLException {
        try (PreparedStatement stmt = getConnection().prepareStatement(
                sqlDoc() + "WHERE maPDT = ?")) {
            stmt.setString(1, maPDT);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return docPhieu(rs);
            }
        }
        return null;
    }

    public ArrayList<HangDaMua> getHangDaMua(String maHD) throws SQLException {
        ArrayList<HangDaMua> ds = new ArrayList<>();
        String sql = "SELECT c.maCTSP, sp.tenSP, ct.kichCo, ct.mauSac, "
                + "c.soLuong AS daMua, COALESCE(dt.daTra,0) AS daTra, c.donGia "
                + "FROM ChiTietHoaDon c "
                + "JOIN ChiTietSanPham ct ON c.maCTSP = ct.maCTSP "
                + "JOIN SanPham sp ON ct.maSP = sp.maSP "
                + "LEFT JOIN (SELECT d.maCTSP, p.hoaDonGoc, SUM(d.soLuong) AS daTra "
                + "FROM ChiTietDoiTra d JOIN PhieuDoiTra p ON d.maPDT = p.maPDT "
                + "GROUP BY d.maCTSP, p.hoaDonGoc) dt "
                + "ON dt.maCTSP = c.maCTSP AND dt.hoaDonGoc = c.maHD "
                + "WHERE c.maHD = ? ORDER BY c.maCTSP";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, maHD);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next())
                    ds.add(new HangDaMua(
                            rs.getString("maCTSP"), rs.getString("tenSP"),
                            rs.getString("kichCo"), rs.getString("mauSac"),
                            rs.getInt("daMua"), rs.getInt("daTra"),
                            rs.getBigDecimal("donGia")));
            }
        }
        return ds;
    }

    // Ma moi tao khi LUU, khong cap ma khi chi bam Lam moi.
    private String taoMaPhieuDoiTra(Connection con) throws SQLException {
        String sql = "SELECT COALESCE(MAX(TRY_CONVERT(INT, "
                + "SUBSTRING(maPDT, 3, LEN(maPDT)-2))), 0) "
                + "FROM PhieuDoiTra WITH (TABLOCKX, HOLDLOCK) "
                + "WHERE maPDT LIKE 'DT%' AND LEN(maPDT) BETWEEN 3 AND 10 "
                + "AND PATINDEX('%[^0-9]%', SUBSTRING(maPDT, 3, LEN(maPDT)-2)) = 0";
        try (PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            rs.next();
            int so = rs.getInt(1) + 1;
            if (so <= 0 || so > 99999999)
                throw new SQLException("Mã DT đã hết");
            return String.format("DT%08d", so);
        }
    }

    private int soLuongDaBan(Connection con, String maHD, String maCTSP)
            throws SQLException {
        String sql = "SELECT soLuong FROM ChiTietHoaDon WHERE maHD = ? AND maCTSP = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maHD);
            stmt.setString(2, maCTSP);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private int soLuongDaTra(Connection con, String maHD, String maCTSP)
            throws SQLException {
        // Khoa bang chi tiet de tranh 2 giao dich cung tra vuot so luong.
        String sql = "SELECT COALESCE(SUM(d.soLuong),0) FROM ChiTietDoiTra d "
                + "WITH (TABLOCKX, HOLDLOCK) "
                + "JOIN PhieuDoiTra p ON d.maPDT = p.maPDT "
                + "WHERE p.hoaDonGoc = ? AND d.maCTSP = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maHD);
            stmt.setString(2, maCTSP);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    private boolean coSanPhamDoiSize(Connection con, String hoaDonMoi,
            String maCTSPCu, int soLuong) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ChiTietHoaDon cm "
                + "JOIN ChiTietSanPham moi ON cm.maCTSP = moi.maCTSP "
                + "JOIN ChiTietSanPham cu ON cu.maCTSP = ? "
                + "WHERE cm.maHD = ? AND moi.maSP = cu.maSP "
                + "AND moi.kichCo <> cu.kichCo AND cm.soLuong >= ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, maCTSPCu);
            stmt.setString(2, hoaDonMoi);
            stmt.setInt(3, soLuong);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1) > 0;
            }
        }
    }

    // Luu phieu, chi tiet va nhap lai kho trong 1 transaction.
    public String create(PhieuDoiTra phieu, ArrayList<ChiTietDoiTra> ds)
            throws SQLException {
        if (phieu == null || phieu.getHoaDonGoc() == null
                || phieu.getNhanVien() == null || phieu.getLyDo() == null
                || phieu.getLyDo().trim().isEmpty() || phieu.getLyDo().length() > 300
                || ds == null || ds.isEmpty()) {
            throw new SQLException("Thiếu thông tin phiếu đổi trả");
        }
        String trangThai = phieu.getTrangThai();
        boolean doiSize = "Đổi size".equals(trangThai);
        if (!doiSize && !"Trả hàng".equals(trangThai)) {
            throw new SQLException("Trạng thái chỉ là Đổi size hoặc Trả hàng");
        }
        if (doiSize && (phieu.getHoaDonMoi() == null
                || phieu.getHoaDonMoi().getMaHD() == null)) {
            throw new SQLException("Đổi size cần có hóa đơn mới đã thanh toán");
        }
        if (!doiSize && phieu.getHoaDonMoi() != null) {
            throw new SQLException("Trả hàng không có hóa đơn mới");
        }
        String maHDGoc = phieu.getHoaDonGoc().getMaHD();
        String maHDMoi = doiSize ? phieu.getHoaDonMoi().getMaHD() : null;
        if (maHDGoc == null || maHDGoc.trim().isEmpty()
                || maHDGoc.equals(maHDMoi)) {
            throw new SQLException("Mã hóa đơn gốc/mới không hợp lệ");
        }
        HashSet<String> maDaCo = new HashSet<>();
        for (ChiTietDoiTra ct : ds) {
            if (ct == null || ct.getChiTietSanPham() == null
                    || ct.getChiTietSanPham().getMaCTSP() == null
                    || !maDaCo.add(ct.getChiTietSanPham().getMaCTSP())
                    || ct.getSoLuong() <= 0 || ct.getSoLuongNhapLai() < 0
                    || ct.getSoLuongNhapLai() > ct.getSoLuong()) {
                throw new SQLException("Chi tiết đổi trả không hợp lệ hoặc trùng mã CTSP");
            }
        }
        Connection con = getConnection();
        boolean autoCu = con.getAutoCommit();
        try {
            con.setAutoCommit(false);
            // Khoa hoa don goc de xac thuc ngay lap khi ghi phieu.
            LocalDateTime ngayGoc;
            String sqlGoc = "SELECT h.ngayLap FROM HoaDon h WITH (UPDLOCK, HOLDLOCK) "
                    + "JOIN KhachHang kh ON h.maKH = kh.maKH "
                    + "WHERE h.maHD = ? AND kh.soDienThoai IS NOT NULL";
            try (PreparedStatement stmt = con.prepareStatement(sqlGoc)) {
                stmt.setString(1, maHDGoc);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next())
                        throw new SQLException("Không tìm thấy hóa đơn gốc");
                    ngayGoc = rs.getTimestamp(1).toLocalDateTime();
                }
            }
            LocalDateTime hienTai = LocalDateTime.now();
            if (hienTai.isBefore(ngayGoc) || hienTai.isAfter(ngayGoc.plusDays(7))) {
                throw new SQLException("Đã quá 7 ngày kể từ thời điểm mua; không được đổi/trả");
            }
            if (doiSize) {
                String sqlHDMoi = "SELECT COUNT(*) FROM HoaDon WHERE maHD = ?";
                try (PreparedStatement stmt = con.prepareStatement(sqlHDMoi)) {
                    stmt.setString(1, maHDMoi);
                    try (ResultSet rs = stmt.executeQuery()) {
                        rs.next();
                        if (rs.getInt(1) == 0)
                            throw new SQLException(
                                    "Hóa đơn mới chưa tồn tại. Hãy lập hóa đơn đổi size trước.");
                    }
                }
            }
            String maPDT = taoMaPhieuDoiTra(con);
            String sqlPhieu = "INSERT INTO PhieuDoiTra "
                    + "(maPDT, hoaDonGoc, hoaDonMoi, maNhanVien, ngayLap, lyDo, trangThai) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = con.prepareStatement(sqlPhieu)) {
                stmt.setString(1, maPDT);
                stmt.setString(2, maHDGoc);
                if (maHDMoi == null)
                    stmt.setNull(3, Types.VARCHAR);
                else
                    stmt.setString(3, maHDMoi);
                stmt.setString(4, phieu.getNhanVien().getMaNhanVien());
                stmt.setTimestamp(5, Timestamp.valueOf(hienTai));
                stmt.setString(6, phieu.getLyDo().trim());
                stmt.setString(7, trangThai);
                if (stmt.executeUpdate() != 1)
                    throw new SQLException("Không lưu được phiếu");
            }
            ChiTietDoiTra_DAO ctDao = new ChiTietDoiTra_DAO();
            String sqlNhap = "UPDATE ChiTietSanPham SET soLuongTon = soLuongTon + ? "
                    + "WHERE maCTSP = ?";
            try (PreparedStatement stmtNhap = con.prepareStatement(sqlNhap)) {
                for (ChiTietDoiTra ct : ds) {
                    String maCTSP = ct.getChiTietSanPham().getMaCTSP();
                    int mua = soLuongDaBan(con, maHDGoc, maCTSP);
                    int daTra = soLuongDaTra(con, maHDGoc, maCTSP);
                    if (mua == 0 || ct.getSoLuong() > mua - daTra) {
                        throw new SQLException("Vượt số lượng có thể trả của " + maCTSP);
                    }
                    if (doiSize && !coSanPhamDoiSize(con, maHDMoi,
                            maCTSP, ct.getSoLuong())) {
                        throw new SQLException("Hóa đơn mới phải có cùng sản phẩm, "
                                + "size khác và đủ số lượng: " + maCTSP);
                    }
                    ChiTietDoiTra ctLuu = new ChiTietDoiTra(
                            new PhieuDoiTra(maPDT), ct.getChiTietSanPham(),
                            ct.getSoLuong(), ct.getSoLuongNhapLai());
                    if (!ctDao.create(con, ctLuu)) {
                        throw new SQLException("Không lưu được chi tiết đổi trả: " + maCTSP);
                    }
                    if (ct.getSoLuongNhapLai() > 0) {
                        // Cho nhap lai ca mat hang NGUNG HOAT DONG.
                        // Luong ban hang loc hoatDong=1 nen hang do khong the ban.
                        stmtNhap.setInt(1, ct.getSoLuongNhapLai());
                        stmtNhap.setString(2, maCTSP);
                        if (stmtNhap.executeUpdate() != 1) {
                            throw new SQLException("Không cập nhật được tồn: " + maCTSP);
                        }
                    }
                }
            }
            con.commit();
            return maPDT;
        } catch (SQLException | RuntimeException e) {
            try {
                con.rollback();
            } catch (SQLException ex) {
                e.addSuppressed(ex);
            }
            throw e;
        } finally {
            con.setAutoCommit(autoCu);
        }
    }
}
