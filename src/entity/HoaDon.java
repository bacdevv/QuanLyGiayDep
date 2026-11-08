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
    private BigDecimal thue = BigDecimal.ZERO;
    private BigDecimal tongTien = BigDecimal.ZERO;

    public HoaDon() {
    }

    public HoaDon(String maHD) {
        this.maHD = maHD;
    }

    public HoaDon(String maHD, NhanVien nhanVien, KhachHang khachHang,
            LocalDateTime ngayLap, String phuongThucThanhToan, BigDecimal tongTien) {
        this(maHD, nhanVien, khachHang, ngayLap, phuongThucThanhToan,
                BigDecimal.ZERO, tongTien);
    }

    public HoaDon(String maHD, NhanVien nhanVien, KhachHang khachHang,
            LocalDateTime ngayLap, String phuongThucThanhToan,
            BigDecimal thue, BigDecimal tongTien) {
        this.maHD = maHD;
        this.nhanVien = nhanVien;
        this.khachHang = khachHang;
        this.ngayLap = ngayLap;
        setPhuongThucThanhToan(phuongThucThanhToan);
        setThue(thue);
        setTongTien(tongTien);
    }

    public String getMaHD() {
        return maHD;
    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public LocalDateTime getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDateTime ngayLap) {
        this.ngayLap = ngayLap;
    }

    public String getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }

    public void setPhuongThucThanhToan(String phuongThucThanhToan) {
        if (phuongThucThanhToan == null
                || (!phuongThucThanhToan.equalsIgnoreCase("Tiền mặt")
                        && !phuongThucThanhToan.equalsIgnoreCase("Chuyển khoản")))
            throw new IllegalArgumentException("Phương thức thanh toán không hợp lệ");
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    public BigDecimal getThue() {
        return thue;
    }

    public void setThue(BigDecimal thue) {
        if (thue == null || thue.compareTo(BigDecimal.ZERO) < 0
                || thue.compareTo(new BigDecimal("100")) > 0)
            throw new IllegalArgumentException("Thuế phải từ 0 đến 100");
        if (thue.stripTrailingZeros().scale() > 2)
            throw new IllegalArgumentException("Thuế chỉ có tối đa hai chữ số thập phân");
        this.thue = thue;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        if (tongTien == null || tongTien.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Tổng tiền không hợp lệ");
        this.tongTien = tongTien;
    }

    // Đơn giá trong chi tiết là giá chưa thuế. Thuế suất lưu theo phần trăm.
    public BigDecimal tinhTienHang(ArrayList<ChiTietHoaDon> dsChiTiet) {
        BigDecimal tienHang = BigDecimal.ZERO;
        for (ChiTietHoaDon ct : dsChiTiet) {
            tienHang = tienHang.add(ct.tinhThanhTien());
        }
        return tienHang.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal tinhTienThue(ArrayList<ChiTietHoaDon> dsChiTiet) {
        return tinhTienHang(dsChiTiet).multiply(thue)
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
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
