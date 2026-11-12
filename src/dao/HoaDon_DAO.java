package dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;

import connectDB.ConnectDB;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;

public class HoaDon_DAO {
    public static final BigDecimal THUE_SUAT = new BigDecimal("8");

    public HoaDon_DAO() {
    }

    private Connection getConnection() throws SQLException {
        Connection con = ConnectDB.getConnection();
        if (con == null || con.isClosed()) {
            ConnectDB.getInstance().connect();
            con = ConnectDB.getConnection();
        }
        return con;
    }

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

        public BigDecimal getGiaBan() {
            return giaBan;
        }

        public int getSoLuongTon() {
            return soLuongTon;
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

    private String sqlHoaDon() {
        return "SELECT maHD, maNhanVien, maKH, ngayLap, "
                + "phuongThucThanhToan, thue, tongTien FROM HoaDon ";
    }

    public ArrayList<HoaDon> getAllTableHoaDon() throws SQLException {
        ArrayList<HoaDon> ds = new ArrayList<>();
        try (PreparedStatement stmt = getConnection().prepareStatement(
                sqlHoaDon() + "ORDER BY ngayLap DESC");
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next())
                ds.add(docHoaDon(rs));
        }
        return ds;
    }

    public HoaDon getHoaDonTheoMaHD(String maHD) throws SQLException {
        try (PreparedStatement stmt = getConnection().prepareStatement(
                sqlHoaDon() + "WHERE maHD = ?")) {
            stmt.setString(1, maHD);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return docHoaDon(rs);
            }
        }
        return null;
    }

    public KhachHang getKhachHangTheoSoDienThoai(String soDienThoai) throws SQLException {
        if (soDienThoai == null || soDienThoai.trim().isEmpty())
            return null;
        String sql = "SELECT maKH, tenKH, soDienThoai FROM KhachHang WHERE soDienThoai = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, soDienThoai.trim());
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next())
                    return null;
                KhachHang kh = new KhachHang(rs.getString("maKH"),
                        rs.getString("tenKH"), rs.getString("soDienThoai"));
                if (rs.next())
                    throw new SQLException(
                            "Có nhiều khách hàng dùng cùng số điện thoại; cần xác minh thủ công.");
                return kh;
            }
        }
    }

    // Them khach hang ngay tren man hinh ban hang, khong sua KhachHang_DAO cua
    // nhom.
    public KhachHang themKhachHang(String sdt, String tenKH) throws SQLException {
        if (sdt == null || !sdt.matches("0[0-9]{9}") || tenKH == null
                || tenKH.trim().isEmpty() || tenKH.trim().length() > 100)
            throw new SQLException("SĐT hoặc tên khách hàng không hợp lệ");
        Connection con = getConnection();
        boolean autoCu = con.getAutoCommit();
        try {
            con.setAutoCommit(false);
            // Khoa bang de tranh 2 thu ngan dang ky trung SĐT / maKH.
            try (PreparedStatement stmt = con.prepareStatement(
                    "SELECT maKH FROM KhachHang WITH (TABLOCKX, HOLDLOCK) WHERE soDienThoai = ?")) {
                stmt.setString(1, sdt);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next())
                        throw new SQLException("SĐT đã được đăng ký, hãy tra cứu lại");
                }
            }
            int so;
            try (PreparedStatement stmt = con.prepareStatement(
                    "SELECT COALESCE(MAX(TRY_CONVERT(INT, SUBSTRING(maKH, 3, LEN(maKH)-2))),0) "
                            + "FROM KhachHang WHERE maKH LIKE 'KH%' ");
                    ResultSet rs = stmt.executeQuery()) {
                rs.next();
                so = rs.getInt(1) + 1;
            }
            if (so <= 0 || so > 999999)
                throw new SQLException("Hết mã khách hàng");
            String maKH = String.format("KH%06d", so);
            try (PreparedStatement stmt = con.prepareStatement(
                    "INSERT INTO KhachHang(maKH, tenKH, soDienThoai) VALUES (?, ?, ?)")) {
                stmt.setString(1, maKH);
                stmt.setString(2, tenKH.trim());
                stmt.setString(3, sdt);
                if (stmt.executeUpdate() != 1)
                    throw new SQLException("Không thêm được khách hàng");
            }
            con.commit();
            return new KhachHang(maKH, tenKH.trim(), sdt);
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

    public KhachHang getKhachHangTheoMaKH(String maKH) throws SQLException {
        if (maKH == null || maKH.trim().isEmpty())
            return null;
        String sql = "SELECT maKH, tenKH, soDienThoai FROM KhachHang WHERE maKH = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, maKH);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return new KhachHang(rs.getString("maKH"),
                            rs.getString("tenKH"), rs.getString("soDienThoai"));
            }
        }
        return null;
    }

    public ArrayList<SanPhamBan> getDanhSachSanPham(String maCTSP, String tenSP)
            throws SQLException {
        ArrayList<SanPhamBan> ds = new ArrayList<>();
        String sql = "SELECT ct.maCTSP, sp.tenSP, ct.kichCo, ct.mauSac, "
                + "sp.giaBan, ct.soLuongTon FROM ChiTietSanPham ct "
                + "JOIN SanPham sp ON ct.maSP = sp.maSP "
                + "WHERE ct.hoatDong = 1 AND sp.hoatDong = 1 "
                + "AND ct.soLuongTon > 0 AND ct.maCTSP LIKE ? AND sp.tenSP LIKE ? "
                + "ORDER BY sp.tenSP, ct.kichCo";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, "%" + (maCTSP == null ? "" : maCTSP.trim()) + "%");
            stmt.setString(2, "%" + (tenSP == null ? "" : tenSP.trim()) + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next())
                    ds.add(new SanPhamBan(rs.getString("maCTSP"),
                            rs.getString("tenSP"), rs.getString("kichCo"),
                            rs.getString("mauSac"), rs.getBigDecimal("giaBan"),
                            rs.getInt("soLuongTon")));
            }
        }
        return ds;
    }

    public int getSoLuongTon(String maCTSP) throws SQLException {
        String sql = "SELECT soLuongTon FROM ChiTietSanPham WHERE maCTSP = ? "
                + "AND hoatDong = 1 AND EXISTS (SELECT 1 FROM SanPham sp "
                + "WHERE sp.maSP = ChiTietSanPham.maSP AND sp.hoatDong = 1)";
        try (PreparedStatement stmt = getConnection().prepareStatement(sql)) {
            stmt.setString(1, maCTSP);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return rs.getInt(1);
            }
        }
        return -1;
    }

    // Moi ma nhap tu SEQUENCE duoc cap DUY NHAT. Du kien ma HD ngay khi mo form.
    // Phai chay SQL_TAO_SEQUENCE_HD.sql mot lan truoc khi su dung.
    public String taoMaHoaDonMoi() throws SQLException {
        Connection con = getConnection();
        String sql = "SELECT NEXT VALUE FOR dbo.SeqHoaDon";
        String sqlTonTai = "SELECT 1 FROM HoaDon WHERE maHD = ?";
        for (int lan = 0; lan < 10; lan++) {
            long so;
            try (PreparedStatement stmt = con.prepareStatement(sql);
                    ResultSet rs = stmt.executeQuery()) {
                if (!rs.next())
                    throw new SQLException("Không cấp được mã hóa đơn");
                so = rs.getLong(1);
            } catch (SQLException e) {
                throw new SQLException("Không thể cấp mã HD. Kiểm tra đã chạy "
                        + "SQL_TAO_SEQUENCE_HD.sql: " + e.getMessage(), e);
            }
            if (so > 999999 || so < 1)
                throw new SQLException("Mã hóa đơn đã vượt quá HD999999");
            String ma = String.format("HD%06d", so);
            try (PreparedStatement kiemTra = con.prepareStatement(sqlTonTai)) {
                kiemTra.setString(1, ma);
                try (ResultSet rs = kiemTra.executeQuery()) {
                    if (!rs.next())
                        return ma;
                }
            }
        }
        throw new SQLException("Không thể cấp mã HD không trùng, kiểm tra SEQUENCE");
    }

    private void kiemTraSoTienSQL(BigDecimal tien, String ten) throws SQLException {
        BigDecimal max = new BigDecimal("9999999999999999.99");
        if (tien == null || tien.compareTo(BigDecimal.ZERO) < 0
                || tien.compareTo(max) > 0
                || tien.stripTrailingZeros().scale() > 2) {
            throw new SQLException(ten + " vượt giới hạn DECIMAL(18,2) của SQL Server");
        }
    }

    // Tao hoa don va tru kho trong 1 transaction. Ma HD da duoc cap o GUI.
    public String create(HoaDon hd, ArrayList<ChiTietHoaDon> dsChiTiet)
            throws SQLException {
        if (hd == null || hd.getNhanVien() == null || hd.getNgayLap() == null
                || hd.getPhuongThucThanhToan() == null || dsChiTiet == null
                || dsChiTiet.isEmpty()) {
            throw new SQLException("Thiếu thông tin hóa đơn");
        }
        if (hd.getKhachHang() == null || hd.getKhachHang().getMaKH() == null)
            throw new SQLException("Hóa đơn bắt buộc có khách hàng đăng ký SĐT");
        String pttt = hd.getPhuongThucThanhToan();
        if (!pttt.equals("Tiền mặt") && !pttt.equals("Chuyển khoản")) {
            throw new SQLException("Phương thức thanh toán không hợp lệ");
        }
        BigDecimal thue = hd.getThue();
        if (thue == null || thue.compareTo(THUE_SUAT) != 0) {
            throw new SQLException("Thuế suất cố định cho hóa đơn mới là 8%");
        }
        if (hd.getMaHD() == null || !hd.getMaHD().matches("HD[0-9]{6}"))
            throw new SQLException("Mã HD phải có dạng HD000001 và được cấp trước khi lưu");
        BigDecimal tienHang = BigDecimal.ZERO;
        HashSet<String> maDaCo = new HashSet<>();
        for (ChiTietHoaDon ct : dsChiTiet) {
            if (ct == null || ct.getChiTietSanPham() == null
                    || ct.getChiTietSanPham().getMaCTSP() == null
                    || !maDaCo.add(ct.getChiTietSanPham().getMaCTSP())
                    || ct.getSoLuong() <= 0 || ct.getDonGia() == null
                    || ct.getDonGia().compareTo(BigDecimal.ZERO) <= 0) {
                throw new SQLException("Chi tiết hóa đơn không hợp lệ hoặc trùng biến thể");
            }
            kiemTraSoTienSQL(ct.getDonGia(), "Đơn giá");
            tienHang = tienHang.add(ct.tinhThanhTien());
        }
        tienHang = tienHang.setScale(2, RoundingMode.HALF_UP);
        BigDecimal tong = tienHang.add(tienHang.multiply(THUE_SUAT).divide(
                new BigDecimal("100"), 2, RoundingMode.HALF_UP));
        kiemTraSoTienSQL(tienHang, "Tiền hàng");
        kiemTraSoTienSQL(tong, "Tổng tiền");
        if (hd.getTongTien() == null || hd.getTongTien().compareTo(tong) != 0) {
            throw new SQLException("Tổng tiền không khớp với các dòng hóa đơn");
        }

        Connection con = getConnection();
        boolean autoCu = con.getAutoCommit();
        try {
            con.setAutoCommit(false);
            String maHD = hd.getMaHD();
            String sqlKH = "SELECT soDienThoai FROM KhachHang WHERE maKH = ?";
            try (PreparedStatement stmt = con.prepareStatement(sqlKH)) {
                stmt.setString(1, hd.getKhachHang().getMaKH());
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next() || rs.getString(1) == null
                            || !rs.getString(1).matches("0[0-9]{9}"))
                        throw new SQLException("Khách hàng chưa có SĐT hợp lệ");
                }
            }
            String sqlHD = "INSERT INTO HoaDon (maHD, maNhanVien, maKH, ngayLap, "
                    + "phuongThucThanhToan, thue, tongTien) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = con.prepareStatement(sqlHD)) {
                stmt.setString(1, maHD);
                stmt.setString(2, hd.getNhanVien().getMaNhanVien());
                stmt.setString(3, hd.getKhachHang().getMaKH());
                stmt.setTimestamp(4, Timestamp.valueOf(hd.getNgayLap()));
                stmt.setString(5, pttt);
                stmt.setBigDecimal(6, THUE_SUAT);
                stmt.setBigDecimal(7, tong);
                if (stmt.executeUpdate() != 1)
                    throw new SQLException("Không lưu được hóa đơn");
            }

            ChiTietHoaDon_DAO ctDao = new ChiTietHoaDon_DAO();
            String sqlGia = "SELECT sp.giaBan FROM ChiTietSanPham ct WITH (UPDLOCK, HOLDLOCK) "
                    + "JOIN SanPham sp WITH (HOLDLOCK) ON ct.maSP = sp.maSP "
                    + "WHERE ct.maCTSP = ? AND ct.hoatDong = 1 AND sp.hoatDong = 1";
            String sqlTon = "UPDATE ChiTietSanPham SET soLuongTon = soLuongTon - ? "
                    + "WHERE maCTSP = ? AND soLuongTon >= ? AND hoatDong = 1";
            try (PreparedStatement stmtGia = con.prepareStatement(sqlGia);
                    PreparedStatement stmtTon = con.prepareStatement(sqlTon)) {
                for (ChiTietHoaDon ct : dsChiTiet) {
                    String maCTSP = ct.getChiTietSanPham().getMaCTSP();
                    stmtGia.setString(1, maCTSP);
                    try (ResultSet rs = stmtGia.executeQuery()) {
                        if (!rs.next())
                            throw new SQLException("Sản phẩm đã ngừng bán: " + maCTSP);
                        if (rs.getBigDecimal(1).compareTo(ct.getDonGia()) != 0) {
                            throw new SQLException("Giá bán đã thay đổi, vui lòng tải lại: " + maCTSP);
                        }
                    }
                    stmtTon.setInt(1, ct.getSoLuong());
                    stmtTon.setString(2, maCTSP);
                    stmtTon.setInt(3, ct.getSoLuong());
                    if (stmtTon.executeUpdate() != 1) {
                        throw new SQLException("Không đủ tồn kho: " + maCTSP);
                    }
                    ChiTietHoaDon ctLuu = new ChiTietHoaDon(
                            new HoaDon(maHD), ct.getChiTietSanPham(),
                            ct.getSoLuong(), ct.getDonGia());
                    if (!ctDao.create(con, ctLuu)) {
                        throw new SQLException("Không lưu được chi tiết: " + maCTSP);
                    }
                }
            }
            con.commit();
            return maHD;
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
