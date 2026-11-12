package entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class PhieuDoiTra {

    private String maPDT;
    private HoaDon hoaDonGoc;
    private HoaDon hoaDonMoi;
    private NhanVien nhanVien;
    private LocalDateTime ngayLap;
    private String lyDo;
    private String trangThai;

    public PhieuDoiTra() {
    }

    public PhieuDoiTra(String maPDT) {
        this.maPDT = maPDT;
    }

    public PhieuDoiTra(String maPDT, HoaDon hoaDonGoc, HoaDon hoaDonMoi,
            NhanVien nhanVien, LocalDateTime ngayLap, String lyDo,
            String trangThai) {
        this.maPDT = maPDT;
        this.hoaDonGoc = hoaDonGoc;
        this.hoaDonMoi = hoaDonMoi;
        this.nhanVien = nhanVien;
        this.ngayLap = ngayLap;
        this.lyDo = lyDo;
        this.trangThai = trangThai;
    }

    public String getMaPDT() {
        return maPDT;
    }

    public HoaDon getHoaDonGoc() {
        return hoaDonGoc;
    }

    public HoaDon getHoaDonMoi() {
        return hoaDonMoi;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public LocalDateTime getNgayLap() {
        return ngayLap;
    }

    public String getLyDo() {
        return lyDo;
    }

    public String getTrangThai() {
        return trangThai;
    }

    @Override
    public int hashCode() {
        return Objects.hash(maPDT);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof PhieuDoiTra))
            return false;
        PhieuDoiTra other = (PhieuDoiTra) obj;
        return Objects.equals(maPDT, other.maPDT);
    }

    @Override
    public String toString() {
        return maPDT + " " + lyDo;
    }
}
