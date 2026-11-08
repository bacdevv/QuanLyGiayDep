package entity;

import java.math.BigDecimal;

public class SanPham {
    private String maSP;
    private DanhMuc danhMuc;
    private ThuongHieu thuongHieu;
    private String tenSP;
    private boolean hoatDong;
    private BigDecimal giaBan;


    public SanPham(){

    }


    public SanPham(String maSP) {
        this.maSP = maSP;
    }


    public SanPham(String maSP, DanhMuc danhMuc, ThuongHieu thuongHieu, String tenSP, boolean hoatDong,
            BigDecimal giaBan) {
        this.maSP = maSP;
        this.danhMuc = danhMuc;
        this.thuongHieu = thuongHieu;
        this.tenSP = tenSP;
        this.hoatDong = hoatDong;
        this.giaBan = giaBan;
    }


    public String getMaSP() {
        return maSP;
    }


    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }


    public DanhMuc getDanhMuc() {
        return danhMuc;
    }


    public void setDanhMuc(DanhMuc danhMuc) {
        this.danhMuc = danhMuc;
    }


    public ThuongHieu getThuongHieu() {
        return thuongHieu;
    }


    public void setThuongHieu(ThuongHieu thuongHieu) {
        this.thuongHieu = thuongHieu;
    }


    public String getTenSP() {
        return tenSP;
    }


    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }


    public boolean isHoatDong() {
        return hoatDong;
    }


    public void setHoatDong(boolean hoatDong) {
        this.hoatDong = hoatDong;
    }


    public BigDecimal getGiaBan() {
        return giaBan;
    }


    public void setGiaBan(BigDecimal giaBan) {
        this.giaBan = giaBan;
    }

    


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((maSP == null) ? 0 : maSP.hashCode());
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
        SanPham other = (SanPham) obj;
        if (maSP == null) {
            if (other.maSP != null)
                return false;
        } else if (!maSP.equals(other.maSP))
            return false;
        return true;
    }


    @Override
    public String toString() {
        return maSP +" "+  danhMuc +" "+  thuongHieu +" "+  tenSP;
    }

    
}
