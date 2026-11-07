package entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class HoaDon {
    private String maHD;
    private NhanVien nhanVien;
    private KhachHang khachHang;
    private LocalDateTime ngayLap;
    private String phuongThucThanhToan;
    private BigDecimal tongTien;

    public HoaDon() {

    }

    public HoaDon(String maHD) {
        super();
        this.maHD = maHD;
    }

    public HoaDon(String maHD, NhanVien nhanVien, KhachHang khachHang, LocalDateTime ngayLap,
            String phuongThucThanhToan,
            BigDecimal tongTien) {
        this.maHD = maHD;
        this.nhanVien = nhanVien;
        this.khachHang = khachHang;
        this.ngayLap = ngayLap;
        setPhuongThucThanhToan(phuongThucThanhToan);
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
        if (phuongThucThanhToan == null ||
                (!phuongThucThanhToan.equalsIgnoreCase("Tiền mặt")
                        && !phuongThucThanhToan.equalsIgnoreCase("Chuyển khoản"))) {
            throw new IllegalArgumentException("Phương thức thanh toán không hợp lệ");
        }

        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        if (tongTien == null || tongTien.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Tổng tiền không hợp lệ");
        this.tongTien = tongTien;
    }

    public BigDecimal tinhTongTien(ArrayList<ChiTietHoaDon> dsChiTiet) {
        BigDecimal tong = BigDecimal.ZERO;

        for (ChiTietHoaDon ct : dsChiTiet) {
            tong = tong.add(ct.tinhThanhTien());
        }

        this.tongTien = tong;
        return tong;
    }

    @Override
    public String toString() {
        return maHD + " " + tongTien;
    }
}
