package entity;

public class DanhMuc {
    private String maDanhMuc;
    private String tenDanhMuc;

    public DanhMuc() {
    }

    public DanhMuc(String maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }

    public DanhMuc(String maDanhMuc, String tenDanhMuc) {
        this.maDanhMuc = maDanhMuc;
        this.tenDanhMuc = tenDanhMuc;
    }

    public String getMaDanhMuc() {
        return maDanhMuc;
    }

    public void setMaDanhMuc(String maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((maDanhMuc == null) ? 0 : maDanhMuc.hashCode());
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
        DanhMuc other = (DanhMuc) obj;
        if (maDanhMuc == null) {
            if (other.maDanhMuc != null)
                return false;
        } else if (!maDanhMuc.equals(other.maDanhMuc))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return maDanhMuc + " " + tenDanhMuc;
    }

}
