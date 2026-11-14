package entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class HoaDon {

    private String maHD;
    private NhanVien nhanVien;
    private KhachHang khachHang;
    private LocalDateTime ngayLap;
    private String phuongThucThanhToan;
    private BigDecimal thue = new BigDecimal("8");
    private BigDecimal tongTien = BigDecimal.ZERO;

    public HoaDon() {
    }

    public HoaDon(String maHD) {
        this.maHD = maHD;
    }

    public HoaDon(String maHD, NhanVien nhanVien, KhachHang khachHang, LocalDateTime ngayLap,
            String phuongThucThanhToan, BigDecimal thue, BigDecimal tongTien) {
        this.maHD = maHD;
        this.nhanVien = nhanVien;
        this.khachHang = khachHang;
        this.ngayLap = ngayLap;
        this.phuongThucThanhToan = phuongThucThanhToan;
        this.thue = thue;
        this.tongTien = tongTien;
    }

    public String getMaHD() {
        return maHD;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public LocalDateTime getNgayLap() {
        return ngayLap;
    }

    public String getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }

    public BigDecimal getThue() {
        return thue;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    // Don gia chi tiet la gia chua thue
    public BigDecimal tinhTienHang(ArrayList<ChiTietHoaDon> dsChiTiet) {
        BigDecimal tienHang = BigDecimal.ZERO;
        for (ChiTietHoaDon ct : dsChiTiet) {
            tienHang = tienHang.add(ct.tinhThanhTien());
        }

        return tienHang.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal tinhTienThue(ArrayList<ChiTietHoaDon> dsChiTiet) {
        return tinhTienHang(dsChiTiet).multiply(thue).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal tinhTongTien(ArrayList<ChiTietHoaDon> dsChiTiet) {
        tongTien = tinhTienHang(dsChiTiet).add(tinhTienThue(dsChiTiet));
        return tongTien;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maHD);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof HoaDon))
            return false;

        HoaDon other = (HoaDon) obj;
        return Objects.equals(maHD, other.maHD);
    }

    @Override
    public String toString() {
        return maHD + " " + tongTien;
    }
}
