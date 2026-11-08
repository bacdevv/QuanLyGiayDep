package entity;

public class ChiTietSanPham {
    private String maCTSP;
    private SanPham sanPham;
    private String kichCo;
    private String mauSac;
    private int soLuongTon;
    private boolean hoatDong;


    public ChiTietSanPham() {
    }


    public ChiTietSanPham(String maCTSP) {
        this.maCTSP = maCTSP;
    }

    public ChiTietSanPham(String maCTSP, SanPham sanPham, String kichCo, String mauSac, int soLuongTon,
            boolean hoatDong) {
        this.maCTSP = maCTSP;
        this.sanPham = sanPham;
        this.kichCo = kichCo;
        this.mauSac = mauSac;
        this.soLuongTon = soLuongTon;
        this.hoatDong = hoatDong;
    }


    public String getMaCTSP() {
        return maCTSP;
    }


    public void setMaCTSP(String maCTSP) {
        this.maCTSP = maCTSP;
    }


    public SanPham getSanPham() {
        return sanPham;
    }


    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }


    public String getKichCo() {
        return kichCo;
    }


    public void setKichCo(String kichCo) {
        this.kichCo = kichCo;
    }


    public String getMauSac() {
        return mauSac;
    }


    public void setMauSac(String mauSac) {
        this.mauSac = mauSac;
    }


    public int getSoLuongTon() {
        return soLuongTon;
    }


    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }


    public boolean isHoatDong() {
        return hoatDong;
    }


    public void setHoatDong(boolean hoatDong) {
        this.hoatDong = hoatDong;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((maCTSP == null) ? 0 : maCTSP.hashCode());
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
        ChiTietSanPham other = (ChiTietSanPham) obj;
        if (maCTSP == null) {
            if (other.maCTSP != null)
                return false;
        } else if (!maCTSP.equals(other.maCTSP))
            return false;
        return true;
    }


    @Override
    public String toString() {
        return maCTSP + " " + sanPham + " " + soLuongTon;
    }

    
}
