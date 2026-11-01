package entity;
public class TaiKhoan {
	// Gợi ý khi đổi sang đối tượng: NhanVien nhanVien thay cho String maNhanVien.
	// Đổi entity thì sửa DAO và GUI tương ứng.

	private String maNhanVien;
	private String tenDangNhap;
	private String matKhauHash;
	// Hai thuộc tính dưới lấy từ JOIN NhanVien, không thêm cột TaiKhoan.
	private String tenNhanVien;
	private boolean hoatDong;
	public TaiKhoan() {
	}
	public TaiKhoan(String maNhanVien, String tenDangNhap, String matKhauHash) {
		this.maNhanVien = maNhanVien;
		this.tenDangNhap = tenDangNhap;
		this.matKhauHash = matKhauHash;
	}
	public String getMaNhanVien() {
		return maNhanVien;
	}
	public void setMaNhanVien(String maNhanVien) {
		this.maNhanVien = maNhanVien;
	}
	public String getTenDangNhap() {
		return tenDangNhap;
	}
	public void setTenDangNhap(String tenDangNhap) {
		this.tenDangNhap = tenDangNhap;
	}
	public String getMatKhauHash() {
		return matKhauHash;
	}
	public void setMatKhauHash(String matKhauHash) {
		this.matKhauHash = matKhauHash;
	}
	public String getTenNhanVien() {
		return tenNhanVien;
	}
	public void setTenNhanVien(String tenNhanVien) {
		this.tenNhanVien = tenNhanVien;
	}
	public boolean isHoatDong() {
		return hoatDong;
	}
	public void setHoatDong(boolean hoatDong) {
		this.hoatDong = hoatDong;
	}
}
