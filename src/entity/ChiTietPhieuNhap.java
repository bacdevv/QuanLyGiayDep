package entity;

import java.math.BigDecimal;

public class ChiTietPhieuNhap {
    private PhieuNhap phieuNhap;
    private ChiTietSanPham chiTietSanPham;
    private int soLuong;
    private BigDecimal donGiaNhap;

    public ChiTietPhieuNhap() {
    }

    public ChiTietPhieuNhap(ChiTietSanPham chiTietSanPham, BigDecimal donGiaNhap, PhieuNhap phieuNhap, int soLuong) {
        this.chiTietSanPham = chiTietSanPham;
        this.donGiaNhap = donGiaNhap;
        this.phieuNhap = phieuNhap;
        this.soLuong = soLuong;
    }
    public ChiTietPhieuNhap() {}
  
    
    public PhieuNhap getPhieuNhap() {
        return phieuNhap;
    }
    public void setPhieuNhap(PhieuNhap phieuNhap) {
        this.phieuNhap = phieuNhap;
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
        this.soLuong = soLuong;
    }
    public BigDecimal getDonGiaNhap() {
        return donGiaNhap;
    }
    public void setDonGiaNhap(BigDecimal donGiaNhap) {
        this.donGiaNhap = donGiaNhap;
    }
    
    
}
