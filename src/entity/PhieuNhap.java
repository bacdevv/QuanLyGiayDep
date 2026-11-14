package entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class PhieuNhap {

    private String maPN;
    private NhaCungCap nhaCungCap;
    private NhanVien nhanVien;
    private LocalDateTime ngayNhap;

    public PhieuNhap(String maPN) {
        this.maPN = maPN;

    }

    public PhieuNhap() {
    }

    public PhieuNhap(String maPN, NhaCungCap nhaCungCap, NhanVien nhanVien, LocalDateTime ngayNhap) {
        this.maPN = maPN;
        this.nhaCungCap = nhaCungCap;
        this.nhanVien = nhanVien;
        this.ngayNhap = ngayNhap;
    }

    public BigDecimal tinhTongTien(ArrayList<ChiTietPhieuNhap> dsChiTiet) {
        BigDecimal tongTien = BigDecimal.ZERO;

        for (ChiTietPhieuNhap ct : dsChiTiet) {
            tongTien = tongTien.add(ct.tinhThanhTien());
        }

        return tongTien;
    }

    public String getMaPN() {
        return maPN;
    }

    public void setMaPN(String maPN) {
        this.maPN = maPN;
    }

    public NhaCungCap getNhaCungCap() {
        return nhaCungCap;
    }

    public void setNhaCungCap(NhaCungCap nhaCungCap) {
        this.nhaCungCap = nhaCungCap;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public LocalDateTime getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(LocalDateTime ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PhieuNhap");
        sb.append("maPN=").append(maPN);
        sb.append(", nhaCungCap=").append(nhaCungCap);
        sb.append(", nhanVien=").append(nhanVien);
        sb.append(", ngayNhap=").append(ngayNhap);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.maPN);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PhieuNhap other = (PhieuNhap) obj;
        return Objects.equals(this.maPN, other.maPN);
    }

}
