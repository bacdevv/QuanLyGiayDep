package entity;

public class ThuongHieu {
    private String maThuongHieu;
    private String tenThuongHieu;

    public ThuongHieu() {

    }

    public ThuongHieu(String maThuongHieu) {
        this.maThuongHieu = maThuongHieu;
    }

    public ThuongHieu(String maThuongHieu, String tenThuongHieu) {
        this.maThuongHieu = maThuongHieu;
        this.tenThuongHieu = tenThuongHieu;
    }

    public String getMaThuongHieu() {
        return maThuongHieu;
    }

    public void setMaThuongHieu(String maThuongHieu) {
        this.maThuongHieu = maThuongHieu;
    }

    public String getTenThuongHieu() {
        return tenThuongHieu;
    }

    public void setTenThuongHieu(String tenThuongHieu) {
        this.tenThuongHieu = tenThuongHieu;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((maThuongHieu == null) ? 0 : maThuongHieu.hashCode());
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
        ThuongHieu other = (ThuongHieu) obj;
        if (maThuongHieu == null) {
            if (other.maThuongHieu != null)
                return false;
        } else if (!maThuongHieu.equals(other.maThuongHieu))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return maThuongHieu + "" + tenThuongHieu;
    }

}
