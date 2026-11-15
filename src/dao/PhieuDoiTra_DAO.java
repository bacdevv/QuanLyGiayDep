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
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.math.RoundingMode;

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
                "SELECT maPDT, hoaDonGoc, hoaDonMoi, maNhanVien, ngayLap, lyDo, "
                + "CASE WHEN hoaDonMoi IS NULL THEN N'Trả hàng' ELSE N'Đổi size' END AS trangThai "
                + "FROM PhieuDoiTra ORDER BY ngayLap DESC");
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) ds.add(docPhieu(rs));
        }
        return ds;
    }

    public PhieuDoiTra getPhieuTheoMaPDT(String maPDT) throws SQLException {
        try (PreparedStatement stmt = getConnection().prepareStatement(
                "SELECT maPDT, hoaDonGoc, hoaDonMoi, maNhanVien, ngayLap, lyDo, "
                + "CASE WHEN hoaDonMoi IS NULL THEN N'Trả hàng' ELSE N'Đổi size' END AS trangThai "
                + "FROM PhieuDoiTra WHERE maPDT=?")) {
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

    private String capMa(Connection con, String bang, String cot, String tienTo) throws SQLException {
        try (PreparedStatement st = con.prepareStatement(
                "DECLARE @k INT; EXEC @k = sp_getapplock @Resource=?, "
                + "@LockMode='Exclusive', @LockOwner='Transaction', @LockTimeout=10000; SELECT @k")) {
            st.setString(1, "QLGD_" + bang + "_Ma");
            try (ResultSet rs = st.executeQuery()) {
                if (!rs.next() || rs.getInt(1) < 0)
                    throw new SQLException("Không thể khóa bộ đếm mã " + tienTo);
            }
        }
        try (PreparedStatement st = con.prepareStatement(
                "SELECT COALESCE(MAX(TRY_CONVERT(INT,SUBSTRING(" + cot
                + ",3,18))),0)+1 FROM " + bang + " WHERE " + cot + " LIKE ?")) {
            st.setString(1, tienTo + "%");
            try (ResultSet rs = st.executeQuery()) {
                rs.next();
                int so = rs.getInt(1);
                if (so < 1 || so > 99999999)
                    throw new SQLException("Đã hết mã " + tienTo + " gồm 8 chữ số");
                return String.format("%s%08d", tienTo, so);
            }
        }
    }

    // Hiển thị trước mã phiếu đổi/trả; chưa ghi gì vào database.
    public String taoMaPhieuDoiTraMoi() throws SQLException {
        String sql = "SELECT COALESCE(MAX(TRY_CONVERT(INT, SUBSTRING(maPDT,3,18))),0)+1 "
                + "FROM PhieuDoiTra WHERE maPDT LIKE 'DT%'";
        try (PreparedStatement st = getConnection().prepareStatement(sql);
                ResultSet rs = st.executeQuery()) {
            rs.next();
            int so = rs.getInt(1);
            if (so < 1 || so > 99999999)
                throw new SQLException("Đã hết mã DT gồm 8 chữ số");
            return String.format("DT%08d", so);
        }
    }

    // Lưu phiếu, tự tạo hóa đơn đổi size và cập nhật tồn kho trong cùng transaction.
    public String create(PhieuDoiTra p, ArrayList<ChiTietDoiTra> ds,
            Map<String, String> sizeMoi) throws SQLException {
        if (p == null || p.getHoaDonGoc() == null || p.getNhanVien() == null
                || p.getLyDo() == null || p.getLyDo().trim().isEmpty()
                || ds == null || ds.isEmpty())
            throw new SQLException("Thiếu dữ liệu phiếu đổi trả");
        boolean doi = "Đổi size".equals(p.getTrangThai());
        if (!doi && !"Trả hàng".equals(p.getTrangThai()))
            throw new SQLException("Loại phiếu không hợp lệ");
        if (doi && (sizeMoi == null || sizeMoi.isEmpty()))
            throw new SQLException("Chưa nhập size mới");

        Connection con = getConnection();
        boolean auto = con.getAutoCommit();
        try {
            con.setAutoCommit(false);
            String maGoc = p.getHoaDonGoc().getMaHD();
            // Chặn hai nhân viên cùng trả vượt số lượng của một hóa đơn.
            try (PreparedStatement lock = con.prepareStatement(
                    "DECLARE @k INT; EXEC @k = sp_getapplock @Resource=?, "
                    + "@LockMode='Exclusive', @LockOwner='Transaction', @LockTimeout=10000; SELECT @k")) {
                lock.setString(1, "QLGD_TraHang_" + maGoc);
                try (ResultSet rs = lock.executeQuery()) {
                    if (!rs.next() || rs.getInt(1) < 0)
                        throw new SQLException("Không thể khóa hóa đơn gốc để đổi trả");
                }
            }
            HoaDon hdGoc = new HoaDon_DAO().getHoaDonTheoMaHD(maGoc);
            if (hdGoc == null) throw new SQLException("Không tìm thấy hóa đơn gốc");
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(hdGoc.getNgayLap()) || now.isAfter(hdGoc.getNgayLap().plusDays(7)))
                throw new SQLException("Chỉ được đổi trả trong 7 ngày sau khi mua");

            Set<String> daChon = new HashSet<>();
            Set<String> daChonMoi = new HashSet<>();
            ArrayList<String> ctMoi = new ArrayList<>();
            ArrayList<BigDecimal> donGia = new ArrayList<>();
            BigDecimal tienHang = BigDecimal.ZERO;
            for (ChiTietDoiTra ct : ds) {
                if (ct == null || ct.getChiTietSanPham() == null)
                    throw new SQLException("Chi tiết đổi trả bị thiếu");
                String ctCu = ct.getChiTietSanPham().getMaCTSP();
                int sl = ct.getSoLuong();
                if (!daChon.add(ctCu)) throw new SQLException("Trùng sản phẩm đổi trả");
                if (sl <= 0 || ct.getSoLuongNhapLai() < 0 || ct.getSoLuongNhapLai() > sl
                        || sl > soLuongDaMua(con, maGoc, ctCu) - soLuongDaTra(con, maGoc, ctCu))
                    throw new SQLException("Số lượng đổi trả vượt quá số được phép: " + ctCu);
                if (!doi) continue;
                String size = sizeMoi.get(ctCu);
                if (size == null || size.trim().isEmpty())
                    throw new SQLException("Thiếu size mới cho sản phẩm " + ctCu);
                String sql = "SELECT TOP 1 moi.maCTSP, hd.donGia FROM ChiTietSanPham cu "
                        + "JOIN ChiTietSanPham moi ON moi.maSP=cu.maSP AND moi.mauSac=cu.mauSac "
                        + "JOIN SanPham sp ON sp.maSP=moi.maSP "
                        + "JOIN ChiTietHoaDon hd ON hd.maCTSP=cu.maCTSP "
                        + "WHERE cu.maCTSP=? AND hd.maHD=? AND moi.kichCo=? "
                        + "AND moi.kichCo<>cu.kichCo AND moi.hoatDong=1 "
                        + "AND sp.hoatDong=1 AND moi.soLuongTon>=?";
                try (PreparedStatement st = con.prepareStatement(sql)) {
                    st.setString(1, ctCu);
                    st.setString(2, maGoc);
                    st.setString(3, size.trim());
                    st.setInt(4, sl);
                    try (ResultSet rs = st.executeQuery()) {
                        if (!rs.next())
                            throw new SQLException("Không có size " + size + " cùng mẫu, cùng màu, đủ số lượng");
                        String maMoi = rs.getString(1);
                        if (!daChonMoi.add(maMoi))
                            throw new SQLException("Các dòng đổi không được trùng mã size mới");
                        ctMoi.add(maMoi);
                        donGia.add(rs.getBigDecimal(2));
                        tienHang = tienHang.add(rs.getBigDecimal(2).multiply(BigDecimal.valueOf(sl)));
                    }
                }
            }

            String maHDMoi = null;
            if (doi) {
                maHDMoi = capMa(con, "HoaDon", "maHD", "HD");
                BigDecimal thue = hdGoc.getThue();
                BigDecimal tong = tienHang.add(tienHang.multiply(thue)
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                try (PreparedStatement st = con.prepareStatement(
                        "INSERT INTO HoaDon(maHD,maNhanVien,maKH,ngayLap,phuongThucThanhToan,thue,tongTien) "
                        + "VALUES (?,?,?,?,?,?,?)")) {
                    st.setString(1, maHDMoi);
                    st.setString(2, p.getNhanVien().getMaNhanVien());
                    if (hdGoc.getKhachHang() == null) st.setNull(3, Types.VARCHAR);
                    else st.setString(3, hdGoc.getKhachHang().getMaKH());
                    st.setTimestamp(4, Timestamp.valueOf(now));
                    st.setNString(5, hdGoc.getPhuongThucThanhToan());
                    st.setBigDecimal(6, thue);
                    st.setBigDecimal(7, tong);
                    st.executeUpdate();
                }
                try (PreparedStatement tru = con.prepareStatement(
                        "UPDATE ChiTietSanPham SET soLuongTon=soLuongTon-? "
                        + "WHERE maCTSP=? AND hoatDong=1 AND soLuongTon>=?");
                     PreparedStatement them = con.prepareStatement(
                        "INSERT INTO ChiTietHoaDon(maHD,maCTSP,soLuong,donGia) VALUES (?,?,?,?)")) {
                    for (int i = 0; i < ds.size(); i++) {
                        ChiTietDoiTra ct = ds.get(i);
                        tru.setInt(1, ct.getSoLuong());
                        tru.setString(2, ctMoi.get(i));
                        tru.setInt(3, ct.getSoLuong());
                        if (tru.executeUpdate() != 1)
                            throw new SQLException("Size mới đã hết hàng: " + ctMoi.get(i));
                        them.setString(1, maHDMoi);
                        them.setString(2, ctMoi.get(i));
                        them.setInt(3, ct.getSoLuong());
                        them.setBigDecimal(4, donGia.get(i));
                        them.executeUpdate();
                    }
                }
            }
            String maPDT = capMa(con, "PhieuDoiTra", "maPDT", "DT");
            // SqlQuery.sql bản gốc KHÔNG có cột trangThai: suy ra từ hoaDonMoi.
            try (PreparedStatement st = con.prepareStatement(
                    "INSERT INTO PhieuDoiTra(maPDT,hoaDonGoc,hoaDonMoi,maNhanVien,ngayLap,lyDo) "
                    + "VALUES (?,?,?,?,?,?)")) {
                st.setString(1, maPDT);
                st.setString(2, maGoc);
                if (maHDMoi == null) st.setNull(3, Types.VARCHAR);
                else st.setString(3, maHDMoi);
                st.setString(4, p.getNhanVien().getMaNhanVien());
                st.setTimestamp(5, Timestamp.valueOf(now));
                st.setNString(6, p.getLyDo().trim());
                st.executeUpdate();
            }
            ChiTietDoiTra_DAO daoCt = new ChiTietDoiTra_DAO();
            try (PreparedStatement st = con.prepareStatement(
                    "UPDATE ChiTietSanPham SET soLuongTon=soLuongTon+? WHERE maCTSP=?")) {
                for (ChiTietDoiTra ct : ds) {
                    String maCT = ct.getChiTietSanPham().getMaCTSP();
                    if (!daoCt.create(con, new ChiTietDoiTra(new PhieuDoiTra(maPDT),
                            ct.getChiTietSanPham(), ct.getSoLuong(), ct.getSoLuongNhapLai())))
                        throw new SQLException("Không lưu được chi tiết đổi trả " + maCT);
                    if (ct.getSoLuongNhapLai() > 0) {
                        st.setInt(1, ct.getSoLuongNhapLai());
                        st.setString(2, maCT);
                        if (st.executeUpdate() != 1)
                            throw new SQLException("Không thể nhập lại kho " + maCT);
                    }
                }
            }
            con.commit();
            return maPDT;
        } catch (SQLException | RuntimeException e) {
            con.rollback();
            throw e;
        } finally { con.setAutoCommit(auto); }
    }

    public String create(PhieuDoiTra p, ArrayList<ChiTietDoiTra> ds) throws SQLException {
        return create(p, ds, java.util.Collections.emptyMap());
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
