package entity;

public class TaiKhoan {
    private NhanVien nhanVien;
    private String tenDangNhap;
    private String taiKhoanHash;

    public TaiKhoan(NhanVien nhanVien, String tenDangNhap, String taiKhoanHash) {
        this.nhanVien = nhanVien;
        this.tenDangNhap = tenDangNhap;
        this.taiKhoanHash = taiKhoanHash;
    }

    public TaiKhoan(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getTaiKhoanHash() {
        return taiKhoanHash;
    }

    public void setTaiKhoanHash(String taiKhoanHash) {
        this.taiKhoanHash = taiKhoanHash;
    }

    @Override
    public String toString() {
        return "TaiKhoan [nhanVien=" + nhanVien + ", tenDangNhap=" + tenDangNhap + ", taiKhoanHash=" + taiKhoanHash
                + "]";
    }

}
