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
import java.math.RoundingMode;

import connectDB.ConnectDB;
import entity.ChiTietDoiTra;
import entity.HoaDon;
import entity.NhanVien;
import entity.PhieuDoiTra;

public class PhieuDoiTra_DAO {
    public PhieuDoiTra_DAO() { }


    public ArrayList<PhieuDoiTra> getAllTablePhieuDoiTra() throws SQLException {
        ArrayList<PhieuDoiTra> ds = new ArrayList<>();
        try (PreparedStatement stmt = ConnectDB.getConnection().prepareStatement(
                "SELECT p.maPDT, p.hoaDonGoc, p.hoaDonMoi, p.maNhanVien, "
                + "p.ngayLap, p.lyDo, nv.tenNhanVien, "
                + "CASE WHEN p.hoaDonMoi IS NULL THEN N'Trả hàng' ELSE N'Đổi size' END AS trangThai "
                + "FROM PhieuDoiTra p JOIN NhanVien nv ON p.maNhanVien = nv.maNhanVien "
                + "ORDER BY p.ngayLap DESC");
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) ds.add(docPhieu(rs));
        }
        return ds;
    }

    public PhieuDoiTra getPhieuTheoMaPDT(String maPDT) throws SQLException {
        try (PreparedStatement stmt = ConnectDB.getConnection().prepareStatement(
                "SELECT p.maPDT, p.hoaDonGoc, p.hoaDonMoi, p.maNhanVien, "
                + "p.ngayLap, p.lyDo, nv.tenNhanVien, "
                + "CASE WHEN p.hoaDonMoi IS NULL THEN N'Trả hàng' ELSE N'Đổi size' END AS trangThai "
                + "FROM PhieuDoiTra p JOIN NhanVien nv ON p.maNhanVien = nv.maNhanVien "
                + "WHERE p.maPDT=?")) {
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
                new NhanVien(rs.getString("maNhanVien"), rs.getString("tenNhanVien")),
                rs.getTimestamp("ngayLap").toLocalDateTime(),
                rs.getString("lyDo"), rs.getString("trangThai"));
    }

    // So luong khach da doi/tra truoc do, theo hoa don goc va ma CTSP.
    public int getSoLuongDaTra(String maHD, String maCTSP) throws SQLException {
        return soLuongDaTra(ConnectDB.getConnection(), maHD, maCTSP);
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

    public String taoMaPhieuDoiTraMoi() throws SQLException {
        return capMa(ConnectDB.getConnection(), "PhieuDoiTra", "maPDT", "DT");
    }

    // Tao phieu va cap nhat kho trong mot transaction.
    // Tra hang: khong tao HD moi. Doi size: tao HD moi va tru size thay the.
    public String create(PhieuDoiTra phieu, ArrayList<ChiTietDoiTra> ds,
            ArrayList<String> sizeMoi) throws SQLException {
        if (phieu == null || phieu.getHoaDonGoc() == null || ds == null || ds.isEmpty())
            throw new SQLException("Phiếu chưa có hàng đổi/trả");
        boolean doiSize = "Đổi size".equals(phieu.getTrangThai());
        Connection con = ConnectDB.getConnection();
        boolean autoCommit = con.getAutoCommit();
        try {
            con.setAutoCommit(false);
            String maHDGoc = phieu.getHoaDonGoc().getMaHD();
            HoaDon hdGoc = new HoaDon_DAO().getHoaDonTheoMaHD(maHDGoc);
            if (hdGoc == null)
                throw new SQLException("Không tìm thấy hóa đơn gốc");
            if (hdGoc.getKhachHang() == null)
                throw new SQLException("Hóa đơn gốc chưa có thông tin khách hàng; cần xử lý dữ liệu cũ trước khi đổi/trả");
            LocalDateTime ngayLap = LocalDateTime.now();
            if (ngayLap.isAfter(hdGoc.getNgayLap().plusDays(7)))
                throw new SQLException("Hóa đơn đã quá 7 ngày, không được đổi/trả");

            // Kiem tra so luong va tim dung ma hang theo size moi.
            ArrayList<String> maHangMoi = new ArrayList<>();
            ArrayList<BigDecimal> giaHangMoi = new ArrayList<>();
            BigDecimal tienHang = BigDecimal.ZERO;
            for (int i = 0; i < ds.size(); i++) {
                ChiTietDoiTra ct = ds.get(i);
                String maCTSP = ct.getChiTietSanPham().getMaCTSP();
                int soLuong = ct.getSoLuong();
                if (soLuong <= 0 || ct.getSoLuongNhapLai() < 0 || ct.getSoLuongNhapLai() > soLuong)
                    throw new SQLException("Số lượng đổi/trả không hợp lệ");
                int conLai = soLuongDaMua(con, maHDGoc, maCTSP)
                        - soLuongDaTra(con, maHDGoc, maCTSP);
                if (soLuong > conLai)
                    throw new SQLException("Số lượng còn được đổi/trả của " + maCTSP + ": " + conLai);
                if (doiSize) {
                    String size = sizeMoi.get(i);
                    if (size == null || size.trim().isEmpty())
                        throw new SQLException("Chưa nhập size mới cho " + maCTSP);
                    String sql = "SELECT moi.maCTSP, hd.donGia FROM ChiTietSanPham cu "
                            + "JOIN ChiTietSanPham moi ON moi.maSP=cu.maSP AND moi.mauSac=cu.mauSac "
                            + "JOIN ChiTietHoaDon hd ON hd.maCTSP=cu.maCTSP "
                            + "WHERE cu.maCTSP=? AND hd.maHD=? AND moi.kichCo=? "
                            + "AND moi.kichCo<>cu.kichCo AND moi.hoatDong=1 AND moi.soLuongTon>=?";
                    try (PreparedStatement stmt = con.prepareStatement(sql)) {
                        stmt.setString(1, maCTSP);
                        stmt.setString(2, maHDGoc);
                        stmt.setString(3, size.trim());
                        stmt.setInt(4, soLuong);
                        try (ResultSet rs = stmt.executeQuery()) {
                            if (!rs.next())
                                throw new SQLException("Không có size " + size + " cùng mẫu, màu và đủ hàng");
                            maHangMoi.add(rs.getString("maCTSP"));
                            BigDecimal donGia = rs.getBigDecimal("donGia");
                            giaHangMoi.add(donGia);
                            tienHang = tienHang.add(donGia.multiply(BigDecimal.valueOf(soLuong)));
                        }
                    }
                }
            }

            String maHDMoi = null;
            if (doiSize) {
                maHDMoi = capMa(con, "HoaDon", "maHD", "HD");
                BigDecimal tienThue = tienHang.multiply(hdGoc.getThue())
                        .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
                String sqlHD = "INSERT INTO HoaDon(maHD,maNhanVien,maKH,ngayLap,phuongThucThanhToan,thue,tongTien) "
                        + "VALUES (?,?,?,?,?,?,?)";
                try (PreparedStatement stmt = con.prepareStatement(sqlHD)) {
                    stmt.setString(1, maHDMoi);
                    stmt.setString(2, phieu.getNhanVien().getMaNhanVien());
                    stmt.setString(3, hdGoc.getKhachHang().getMaKH());
                    stmt.setTimestamp(4, Timestamp.valueOf(ngayLap));
                    stmt.setNString(5, hdGoc.getPhuongThucThanhToan());
                    stmt.setBigDecimal(6, hdGoc.getThue());
                    stmt.setBigDecimal(7, tienHang.add(tienThue));
                    stmt.executeUpdate();
                }
                for (int i = 0; i < ds.size(); i++) {
                    ChiTietDoiTra ct = ds.get(i);
                    String maCTMoi = maHangMoi.get(i);
                    try (PreparedStatement stmt = con.prepareStatement(
                            "UPDATE ChiTietSanPham SET soLuongTon=soLuongTon-? "
                            + "WHERE maCTSP=? AND soLuongTon>=?")) {
                        stmt.setInt(1, ct.getSoLuong());
                        stmt.setString(2, maCTMoi);
                        stmt.setInt(3, ct.getSoLuong());
                        if (stmt.executeUpdate() == 0)
                            throw new SQLException("Size mới không đủ tồn kho: " + maCTMoi);
                    }
                    new ChiTietHoaDon_DAO().create(con, new entity.ChiTietHoaDon(
                            new HoaDon(maHDMoi), new entity.ChiTietSanPham(maCTMoi),
                            ct.getSoLuong(), giaHangMoi.get(i)));
                }
            }

            String maPDT = capMa(con, "PhieuDoiTra", "maPDT", "DT");
            String sqlPDT = "INSERT INTO PhieuDoiTra(maPDT,hoaDonGoc,hoaDonMoi,maNhanVien,ngayLap,lyDo) "
                    + "VALUES (?,?,?,?,?,?)";
            try (PreparedStatement stmt = con.prepareStatement(sqlPDT)) {
                stmt.setString(1, maPDT);
                stmt.setString(2, maHDGoc);
                if (maHDMoi == null) stmt.setNull(3, Types.VARCHAR);
                else stmt.setString(3, maHDMoi);
                stmt.setString(4, phieu.getNhanVien().getMaNhanVien());
                stmt.setTimestamp(5, Timestamp.valueOf(ngayLap));
                stmt.setNString(6, phieu.getLyDo());
                stmt.executeUpdate();
            }
            ChiTietDoiTra_DAO ct_dao = new ChiTietDoiTra_DAO();
            for (ChiTietDoiTra ct : ds) {
                ChiTietDoiTra luu = new ChiTietDoiTra(new PhieuDoiTra(maPDT),
                        ct.getChiTietSanPham(), ct.getSoLuong(), ct.getSoLuongNhapLai());
                ct_dao.create(con, luu);
                if (ct.getSoLuongNhapLai() > 0) {
                    try (PreparedStatement stmt = con.prepareStatement(
                            "UPDATE ChiTietSanPham SET soLuongTon=soLuongTon+? WHERE maCTSP=?")) {
                        stmt.setInt(1, ct.getSoLuongNhapLai());
                        stmt.setString(2, ct.getChiTietSanPham().getMaCTSP());
                        stmt.executeUpdate();
                    }
                }
            }
            con.commit();
            return maPDT;
        } catch (SQLException | RuntimeException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(autoCommit);
        }
    }

    public String create(PhieuDoiTra phieu, ArrayList<ChiTietDoiTra> ds) throws SQLException {
        return create(phieu, ds, new ArrayList<>());
    }
}
