package entity;

import java.util.Objects;

public class NhaCungCap {
    private String maNCC;
    private String tenNCC;
    private String soDienThoai;
    private boolean hoatDong;

    public NhaCungCap(String maNCC, String tenNCC, String soDienThoai, boolean hoatDong) {
        this.hoatDong = hoatDong;
        this.maNCC = maNCC;
        this.soDienThoai = soDienThoai;
        this.tenNCC = tenNCC;
    }

    public NhaCungCap(String maNCC) {
        this.maNCC = maNCC;
    }

    public NhaCungCap() {

    }

    public String getMaNCC() {
        return maNCC;
    }

    public void setMaNCC(String maNCC) {
        this.maNCC = maNCC;
    }

    public String getTenNCC() {
        return tenNCC;
    }

    public void setTenNCC(String tenNCC) {
        this.tenNCC = tenNCC;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public boolean isHoatDong() {
        return hoatDong;
    }

    public void setHoatDong(boolean hoatDong) {
        this.hoatDong = hoatDong;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("NhaCungCap{");
        sb.append("maNCC=").append(maNCC);
        sb.append(", tenNCC=").append(tenNCC);
        sb.append(", soDienThoai=").append(soDienThoai);
        sb.append(", hoatDong=").append(hoatDong);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.maNCC);
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
        final NhaCungCap other = (NhaCungCap) obj;
        return Objects.equals(this.maNCC, other.maNCC);
    }

}
