package entity;

public class TaiKhoan {
    private NhanVien nhanVien;
    private String tenDangNhap;
    private String matKhau;

    public TaiKhoan(NhanVien nhanVien, String tenDangNhap, String matKhau) {
        this.nhanVien = nhanVien;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
    }

    public TaiKhoan() {

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

    public String getmatKhau() {
        return matKhau;
    }

    public void setmatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    @Override
    public String toString() {
        return "TaiKhoan [nhanVien=" + nhanVien + ", tenDangNhap=" + tenDangNhap + ", matKhau=" + matKhau
                + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tenDangNhap == null) ? 0 : tenDangNhap.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TaiKhoan other = (TaiKhoan) obj;
        if (tenDangNhap == null) {
            if (other.tenDangNhap != null)
                return false;
        } else if (!tenDangNhap.equals(other.tenDangNhap))
            return false;
        return true;
    }

}
