package entity;

public class ChiTietDoiTra {
    private PhieuDoiTra phieuDoiTra;
    private ChiTietSanPham chiTietSanPham;
    private int soLuong;
    private int soLuongNhapLai;

    public ChiTietDoiTra() {

    }

    public ChiTietDoiTra(PhieuDoiTra phieuDoiTra) {
        super();
        this.phieuDoiTra = phieuDoiTra;
    }

    public ChiTietDoiTra(PhieuDoiTra phieuDoiTra, ChiTietSanPham chiTietSanPham, int soLuong, int soLuongNhapLai) {
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
    public String toString() {
        return phieuDoiTra.getMaPDT() + " " + soLuongNhapLai;
    }
}
