package entity;

import java.time.LocalDateTime;

public class TaiKhoan {
    private String maTK;
    private String maNhanVien;
    private String tenDangNhap;
    private String matKhau;
    private String vaiTro;
    private boolean hoatDong;
    private LocalDateTime ngayTao;
    private String tenNhanVien;
    private String trangThaiNhanVien;

    public TaiKhoan() {
    }

    public TaiKhoan(String maTK, String maNhanVien, String tenDangNhap,
                    String matKhau, String vaiTro, boolean hoatDong) {
        this.maTK = maTK;
        this.maNhanVien = maNhanVien;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro;
        this.hoatDong = hoatDong;
    }

    public String getMaTK() { return maTK; }
    public void setMaTK(String value) { maTK = value; }
    public String getMaNhanVien() { return maNhanVien; }
    public void setMaNhanVien(String value) { maNhanVien = value; }
    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(String value) { tenDangNhap = value; }
    public String getMatKhau() { return matKhau; }
    public void setMatKhau(String value) { matKhau = value; }
    public String getVaiTro() { return vaiTro; }
    public void setVaiTro(String value) { vaiTro = value; }
    public boolean isHoatDong() { return hoatDong; }
    public void setHoatDong(boolean value) { hoatDong = value; }
    public LocalDateTime getNgayTao() { return ngayTao; }
    public void setNgayTao(LocalDateTime value) { ngayTao = value; }
    public String getTenNhanVien() { return tenNhanVien; }
    public void setTenNhanVien(String value) { tenNhanVien = value; }
    public String getTrangThaiNhanVien() { return trangThaiNhanVien; }
    public void setTrangThaiNhanVien(String value) { trangThaiNhanVien = value; }

    @Override
    public String toString() {
        return tenDangNhap + " (" + vaiTro + ")";
    }
}
