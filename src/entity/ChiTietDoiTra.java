package entity;

import java.util.Objects;

public class ChiTietDoiTra {

    private PhieuDoiTra phieuDoiTra;
    private ChiTietSanPham chiTietSanPham;
    private int soLuong;
    private int soLuongNhapLai;

    public ChiTietDoiTra() {
    }

    public ChiTietDoiTra(PhieuDoiTra phieuDoiTra) {
        this.phieuDoiTra = phieuDoiTra;
    }

    public ChiTietDoiTra(PhieuDoiTra phieuDoiTra, ChiTietSanPham chiTietSanPham,
            int soLuong, int soLuongNhapLai) {
        this.phieuDoiTra = phieuDoiTra;
        this.chiTietSanPham = chiTietSanPham;
        setSoLuong(soLuong);
        setSoLuongNhapLai(soLuongNhapLai);
    }

    public PhieuDoiTra getPhieuDoiTra() {
        return phieuDoiTra;
    }

    public void setPhieuDoiTra(PhieuDoiTra phieuDoiTra) {
        this.phieuDoiTra = phieuDoiTra;
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
        if (soLuong <= 0 || soLuong < soLuongNhapLai)
            throw new IllegalArgumentException("Số lượng không hợp lệ");
        this.soLuong = soLuong;
    }

    public int getSoLuongNhapLai() {
        return soLuongNhapLai;
    }

    public void setSoLuongNhapLai(int soLuongNhapLai) {
        if (soLuongNhapLai < 0 || soLuongNhapLai > soLuong)
            throw new IllegalArgumentException("Số lượng nhập lại không hợp lệ");
        this.soLuongNhapLai = soLuongNhapLai;
    }

    @Override
    public int hashCode() {
        return Objects.hash(phieuDoiTra, chiTietSanPham);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof ChiTietDoiTra))
            return false;
        ChiTietDoiTra other = (ChiTietDoiTra) obj;
        return Objects.equals(phieuDoiTra, other.phieuDoiTra)
                && Objects.equals(chiTietSanPham, other.chiTietSanPham);
    }

    @Override
    public String toString() {
        return phieuDoiTra == null ? "" : phieuDoiTra.getMaPDT() + " " + soLuongNhapLai;
    }
}
