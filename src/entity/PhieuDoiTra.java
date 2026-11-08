package entity;

import java.time.LocalDateTime;

public class PhieuDoiTra {
    private String maPDT;
    private HoaDon hoaDonGoc;
    private HoaDon hoaDonMoi;
    private NhanVien nhanVien;
    private LocalDateTime ngayLap;
    private String lyDo;

    public PhieuDoiTra() {

    }

    public PhieuDoiTra(String maPDT) {
        super();
        this.maPDT = maPDT;
    }

    public PhieuDoiTra(String maPDT, HoaDon hoaDonGoc, HoaDon hoaDonMoi, NhanVien nhanVien, LocalDateTime ngayLap,
            String lyDo) {
        this.maPDT = maPDT;
        this.hoaDonGoc = hoaDonGoc;
        this.hoaDonMoi = hoaDonMoi;
        this.nhanVien = nhanVien;
        this.ngayLap = ngayLap;
        setLyDo(lyDo);
    }

    public String getMaPDT() {
        return maPDT;
    }

    public void setMaPDT(String maPDT) {
        this.maPDT = maPDT;
    }

    public HoaDon getHoaDonGoc() {
        return hoaDonGoc;
    }

    public void setHoaDonGoc(HoaDon hoaDonGoc) {
        this.hoaDonGoc = hoaDonGoc;
    }

    public HoaDon getHoaDonMoi() {
        return hoaDonMoi;
    }

    public void setHoaDonMoi(HoaDon hoaDonMoi) {
        this.hoaDonMoi = hoaDonMoi;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public LocalDateTime getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(LocalDateTime ngayLap) {
        this.ngayLap = ngayLap;
    }

    public String getLyDo() {
        return lyDo;
    }

    public void setLyDo(String lyDo) {
        if (lyDo == null || lyDo.isBlank())
            throw new IllegalArgumentException("Lý do đổi trả không được rỗng");

        this.lyDo = lyDo.trim();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((maPDT == null) ? 0 : maPDT.hashCode());
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
        PhieuDoiTra other = (PhieuDoiTra) obj;
        if (maPDT == null) {
            if (other.maPDT != null)
                return false;
        } else if (!maPDT.equals(other.maPDT))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return maPDT + " " + lyDo;
    }

}
