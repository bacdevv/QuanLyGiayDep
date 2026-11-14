package entity;

import java.math.BigDecimal;

public class ChiTietPhieuNhap {
    private PhieuNhap phieuNhap;
    private ChiTietSanPham chiTietSanPham;
    private int soLuong;
    private BigDecimal donGiaNhap;

    public ChiTietPhieuNhap() {
    }

    public ChiTietPhieuNhap(PhieuNhap phieuNhap) {
        this.phieuNhap = phieuNhap;
    }

    public ChiTietPhieuNhap(ChiTietSanPham chiTietSanPham, BigDecimal donGiaNhap, PhieuNhap phieuNhap, int soLuong) {
        this.chiTietSanPham = chiTietSanPham;
        this.donGiaNhap = donGiaNhap;
        this.phieuNhap = phieuNhap;
        this.soLuong = soLuong;
    }

    public ChiTietPhieuNhap(PhieuNhap phieuNhap, ChiTietSanPham chiTietSanPham, int soLuong, BigDecimal donGiaNhap) {
        this.phieuNhap = phieuNhap;
        this.chiTietSanPham = chiTietSanPham;
        this.soLuong = soLuong;
        this.donGiaNhap = donGiaNhap;
    }

    public BigDecimal tinhThanhTien() {
        if (donGiaNhap == null) {
            return BigDecimal.ZERO;
        }
        return donGiaNhap.multiply(BigDecimal.valueOf(soLuong));
    }
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (this.phieuNhap != null ? this.phieuNhap.hashCode() : 0);
        hash = 97 * hash + (this.chiTietSanPham != null ? this.chiTietSanPham.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ChiTietPhieuNhap other = (ChiTietPhieuNhap) obj;
        if (this.phieuNhap != null && other.phieuNhap != null) {
            if (!this.phieuNhap.equals(other.phieuNhap)) return false;
        } else if (this.phieuNhap != other.phieuNhap) {
            return false;
        }
        if (this.chiTietSanPham != null && other.chiTietSanPham != null) {
            return this.chiTietSanPham.equals(other.chiTietSanPham);
        }
        return this.chiTietSanPham == other.chiTietSanPham;
    }

    @Override
    public String toString() {
        return "ChiTietPhieuNhap{" + "phieuNhap=" + phieuNhap + ", chiTietSanPham=" + chiTietSanPham + ", soLuong=" + soLuong + ", donGiaNhap=" + donGiaNhap + '}';
    }
}
