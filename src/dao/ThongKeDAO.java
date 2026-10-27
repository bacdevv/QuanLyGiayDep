package dao;

import util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ThongKeDAO {
    public record RevenueRow(String paymentMethod, BigDecimal revenue, long invoiceCount) {
    }

    public record ProductRow(String sku, String productName, int stock, int soldQuantity, BigDecimal revenue) {
    }

    public record CustomerRow(String customerId, String customerName, int invoiceCount, BigDecimal spending) {
    }

    public BigDecimal netRevenue(LocalDate from, LocalDate to) throws SQLException {
        String sql = """
                SELECT COALESCE(SUM(hd.TongThanhToan - COALESCE(pdt.SoTienHoan,0) + COALESCE(pdt.SoTienThuThem,0)),0)
                FROM HOADON hd
                LEFT JOIN (
                    SELECT MaHDCu, SUM(SoTienHoan) SoTienHoan, SUM(SoTienThuThem) SoTienThuThem
                    FROM PHIEUDOITRA WHERE TrangThai='HOAN_TAT' GROUP BY MaHDCu
                ) pdt ON pdt.MaHDCu=hd.MaHD
                WHERE hd.TrangThai='DA_THANH_TOAN' AND hd.NgayThanhToan>=? AND hd.NgayThanhToan<?
                """;
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            setRange(ps, from, to);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getBigDecimal(1);
            }
        }
    }

    public List<RevenueRow> revenueByPayment(LocalDate from, LocalDate to) throws SQLException {
        String sql = """
                SELECT hd.PhuongThucThanhToan,
                       SUM(hd.TongThanhToan), COUNT(*)
                FROM HOADON hd
                WHERE hd.TrangThai='DA_THANH_TOAN' AND hd.NgayThanhToan>=? AND hd.NgayThanhToan<?
                GROUP BY hd.PhuongThucThanhToan ORDER BY SUM(hd.TongThanhToan) DESC
                """;
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            setRange(ps, from, to);
            try (ResultSet rs = ps.executeQuery()) {
                List<RevenueRow> result = new ArrayList<>();
                while (rs.next())
                    result.add(new RevenueRow(rs.getString(1), rs.getBigDecimal(2), rs.getLong(3)));
                return result;
            }
        }
    }

    public List<ProductRow> bestSelling(LocalDate from, LocalDate to, int limit) throws SQLException {
        String sql = """
                SELECT TOP (?) ct.SKU, sp.TenSP, ct.SoLuongTon, SUM(cthd.SoLuong),
                       SUM(cthd.ThanhTien)
                FROM CHITIETHOADON cthd
                JOIN HOADON hd ON hd.MaHD=cthd.MaHD
                JOIN CHITIETSANPHAM ct ON ct.MaCTSP=cthd.MaCTSP
                JOIN SANPHAM sp ON sp.MaSP=ct.MaSP
                WHERE hd.TrangThai='DA_THANH_TOAN' AND hd.NgayThanhToan>=? AND hd.NgayThanhToan<?
                GROUP BY ct.SKU, sp.TenSP, ct.SoLuongTon
                ORDER BY SUM(cthd.SoLuong) DESC, SUM(cthd.ThanhTien) DESC
                """;
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            setRange(ps, from, to, 2);
            try (ResultSet rs = ps.executeQuery()) {
                List<ProductRow> result = new ArrayList<>();
                while (rs.next())
                    result.add(new ProductRow(rs.getString(1), rs.getString(2),
                            rs.getInt(3), rs.getInt(4), rs.getBigDecimal(5)));
                return result;
            }
        }
    }

    public List<ProductRow> lowStock(int limit) throws SQLException {
        String sql = """
                SELECT TOP (?) ct.SKU, sp.TenSP, ct.SoLuongTon, 0, CAST(0 AS decimal(18,0))
                FROM CHITIETSANPHAM ct JOIN SANPHAM sp ON sp.MaSP=ct.MaSP
                WHERE ct.HoatDong=1 AND ct.SoLuongTon<=ct.MucTonToiThieu
                ORDER BY ct.SoLuongTon ASC, ct.SKU
                """;
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                List<ProductRow> result = new ArrayList<>();
                while (rs.next())
                    result.add(new ProductRow(rs.getString(1), rs.getString(2),
                            rs.getInt(3), 0, rs.getBigDecimal(5)));
                return result;
            }
        }
    }

    public List<CustomerRow> topCustomers(LocalDate from, LocalDate to, int limit) throws SQLException {
        String sql = """
                SELECT TOP (?) kh.MaKH, kh.TenKH, COUNT(DISTINCT hd.MaHD), SUM(hd.TongThanhToan)
                FROM HOADON hd JOIN KHACHHANG kh ON kh.MaKH=hd.MaKH
                WHERE hd.TrangThai='DA_THANH_TOAN' AND hd.NgayThanhToan>=? AND hd.NgayThanhToan<?
                GROUP BY kh.MaKH, kh.TenKH ORDER BY SUM(hd.TongThanhToan) DESC
                """;
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, limit);
            setRange(ps, from, to, 2);
            try (ResultSet rs = ps.executeQuery()) {
                List<CustomerRow> result = new ArrayList<>();
                while (rs.next())
                    result.add(new CustomerRow(rs.getString(1), rs.getString(2),
                            rs.getInt(3), rs.getBigDecimal(4)));
                return result;
            }
        }
    }

    private static void setRange(PreparedStatement ps, LocalDate from, LocalDate to) throws SQLException {
        setRange(ps, from, to, 1);
    }

    private static void setRange(PreparedStatement ps, LocalDate from, LocalDate to, int start) throws SQLException {
        ps.setTimestamp(start, Timestamp.valueOf(from.atStartOfDay()));
        ps.setTimestamp(start + 1, Timestamp.valueOf(to.plusDays(1).atStartOfDay()));
    }
}
