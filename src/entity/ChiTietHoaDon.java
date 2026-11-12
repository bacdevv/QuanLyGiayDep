package entity;

import java.math.BigDecimal;
import java.util.Objects;

public class ChiTietHoaDon {

    private HoaDon hoaDon;
    private ChiTietSanPham chiTietSanPham;
    private int soLuong;
    private BigDecimal donGia;

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public ChiTietHoaDon(HoaDon hoaDon, ChiTietSanPham chiTietSanPham,
            int soLuong, BigDecimal donGia) {
        this.hoaDon = hoaDon;
        this.chiTietSanPham = chiTietSanPham;
        this.soLuong = soLuong;
        this.donGia = donGia;
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public ChiTietSanPham getChiTietSanPham() {
        return chiTietSanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public BigDecimal tinhThanhTien() {
        return donGia.multiply(BigDecimal.valueOf(soLuong));
    }

    @Override
    public int hashCode() {
        return Objects.hash(hoaDon, chiTietSanPham);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof ChiTietHoaDon))
            return false;
        ChiTietHoaDon other = (ChiTietHoaDon) obj;
        return Objects.equals(hoaDon, other.hoaDon)
                && Objects.equals(chiTietSanPham, other.chiTietSanPham);
    }

    @Override
    public String toString() {
        return hoaDon == null ? "" : hoaDon.getMaHD();
    }
}
