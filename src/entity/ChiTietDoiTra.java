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
        this.soLuong = soLuong;
        this.soLuongNhapLai = soLuongNhapLai;
    }

    public PhieuDoiTra getPhieuDoiTra() {
        return phieuDoiTra;
    }

    public ChiTietSanPham getChiTietSanPham() {
        return chiTietSanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public int getSoLuongNhapLai() {
        return soLuongNhapLai;
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
