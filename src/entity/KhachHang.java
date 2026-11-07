package entity;

import java.util.Objects;

public class KhachHang {

    private String maKH;
    private String tenKH;
    private String soDienThoai;

    public KhachHang(String maKH, String tenKH, String soDienThoai) {
        super();
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.soDienThoai = soDienThoai;
    }

    public KhachHang(String maKH) {
        this(maKH, "", "");
    }

    public KhachHang() {
        this("", "", "");
    }

    public String getMaKH() {
        return maKH;
    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    @Override
    public String toString() {
        return "KhachHang [maKH=" + maKH + ", tenKH=" + tenKH + ", soDienThoai=" + soDienThoai + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(maKH);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        KhachHang other = (KhachHang) obj;
        return Objects.equals(maKH, other.maKH);
    }

}