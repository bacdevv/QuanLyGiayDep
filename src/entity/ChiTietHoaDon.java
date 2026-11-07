package entity;

import java.math.BigDecimal;

public class ChiTietHoaDon {
    private HoaDon hoaDon;
    private ChiTietSanPham chiTietSanPham;
    private int soLuong;
    private BigDecimal donGia;

    public ChiTietHoaDon() {

    }

    public ChiTietHoaDon(HoaDon hoaDon) {
        super();
        this.hoaDon = hoaDon;
    }

    public ChiTietHoaDon(HoaDon hoaDon, ChiTietSanPham chiTietSanPham, int soLuong, BigDecimal donGia) {
        this.hoaDon = hoaDon;
        this.chiTietSanPham = chiTietSanPham;
        setSoLuong(soLuong);
        setDonGia(donGia);
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public ChiTietSanPham getChiTietSanPham() {
        return chiTietSanPham;
    }

    public void setChiTietSanPham(ChiTietSanPham chiTietSanPham) {
        this.chiTietSanPham = chiTietSanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        if (soLuong <= 0)
            throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        this.soLuong = soLuong;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        if (donGia == null || donGia.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Đơn giá phải lớn hơn 0");
        this.donGia = donGia;
    }

    public BigDecimal tinhThanhTien() {
        return donGia.multiply(BigDecimal.valueOf(soLuong));
    }

    @Override
    public String toString() {
        return hoaDon.getMaHD();
    }
}