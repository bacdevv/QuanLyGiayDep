package dao;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import connectDB.ConnectDB;
import entity.TaiKhoan;
import java.sql.*;
import java.util.ArrayList;
public class TaiKhoan_DAO {
	private static TaiKhoan taiKhoanDangNhap;
	public static TaiKhoan getTaiKhoan() {
		return taiKhoanDangNhap;
	}
	public static void dangXuat() {
		taiKhoanDangNhap = null;
	}
	public static void kiemTraDangNhap() {
		if (taiKhoanDangNhap == null) {
			throw new SecurityException("Bạn cần đăng nhập trước.");
		}
	}
	private String sqlSelect = "Select tk.maNhanVien, tk.tenDangNhap, tk.matKhauHash, nv.tenNhanVien, nv.hoatDong "
	+ "from dbo.TaiKhoan tk join dbo.NhanVien nv on tk.maNhanVien=nv.maNhanVien ";
	private TaiKhoan docTaiKhoan(ResultSet rs) throws SQLException {
		TaiKhoan tk = new TaiKhoan(rs.getString("maNhanVien"), rs.getString("tenDangNhap"), rs.getString("matKhauHash"));
		tk.setTenNhanVien(rs.getString("tenNhanVien"));
		tk.setHoatDong(rs.getBoolean("hoatDong"));
		return tk;
	}
	public TaiKhoan dangNhap(String tenDangNhap, String matKhau) throws SQLException {
		TaiKhoan_DAO.dangXuat();
		if (tenDangNhap == null || tenDangNhap.trim().isEmpty() || matKhau == null || matKhau.isEmpty())
		throw new IllegalArgumentException("Nhập đủ tên đăng nhập và mật khẩu.");
		Connection con = ConnectDB.getConnection();
		String sql = sqlSelect + "where tk.tenDangNhap=?";
		try (PreparedStatement statement = con.prepareStatement(sql)) {
			statement.setString(1, tenDangNhap.trim());
			try (ResultSet rs = statement.executeQuery()) {
				if (rs.next()) {
					TaiKhoan tk = docTaiKhoan(rs);
					if (tk.isHoatDong() && kiemTraMatKhau(matKhau, tk.getMatKhauHash())) {
						// Phiên không cần mang theo hash mật khẩu.
						tk.setMatKhauHash(null);
						taiKhoanDangNhap = tk;
						return tk;
					}
				}
			}
		}
		throw new IllegalArgumentException("Sai thông tin đăng nhập hoặc nhân viên đã ngừng hoạt động.");
	}
	public ArrayList<TaiKhoan> getAlltbTaiKhoan() throws SQLException {
		return timTaiKhoan("", "Tất cả");
	}
	public ArrayList<TaiKhoan> timTaiKhoan(String tuKhoa, String trangThai) throws SQLException {
		TaiKhoan_DAO.kiemTraDangNhap();
		ArrayList<TaiKhoan> dsTaiKhoan = new ArrayList<TaiKhoan>();
		String sql = sqlSelect + "where (tk.maNhanVien like ? or tk.tenDangNhap like ? or nv.tenNhanVien like ?) ";
		if ("Đang làm".equals(trangThai)) sql += "and nv.hoatDong=1 ";
		if ("Đã nghỉ".equals(trangThai)) sql += "and nv.hoatDong=0 ";
		sql += "order by tk.maNhanVien";
		try (PreparedStatement statement = ConnectDB.getConnection().prepareStatement(sql)) {
			String mau = "%" + tuKhoa.trim().replace("[", "[[]").replace("%", "[%]").replace("_", "[_]") + "%";
			for (int i = 1; i <= 3; i++) statement.setString(i, mau);
			try (ResultSet rs = statement.executeQuery()) {
				while (rs.next()) {
					TaiKhoan tk = docTaiKhoan(rs);
					tk.setMatKhauHash(null);
					dsTaiKhoan.add(tk);
				}
			}
		}
		return dsTaiKhoan;
	}
	public ArrayList<String[]> getNhanVienChuaCoTaiKhoan() throws SQLException {
		TaiKhoan_DAO.kiemTraDangNhap();
		ArrayList<String[]> dsNhanVien = new ArrayList<String[]>();
		String sql = "Select maNhanVien,tenNhanVien from dbo.NhanVien nv where hoatDong=1 "
		+ "and not exists (Select 1 from dbo.TaiKhoan tk where tk.maNhanVien=nv.maNhanVien) order by maNhanVien";
		try (Statement statement = ConnectDB.getConnection().createStatement(); ResultSet rs = statement.executeQuery(sql)) {
			while (rs.next()) dsNhanVien.add(new String[]{
				rs.getString(1), rs.getString(2)
			}
			);
		}
		return dsNhanVien;
	}
	public void kiemTraDuLieu(String maNhanVien, String tenDangNhap, String matKhau, boolean them) {
		if (maNhanVien == null || !maNhanVien.matches("[A-Za-z0-9_-]{1,12}"))
		throw new IllegalArgumentException("Chọn nhân viên hợp lệ.");
		if (tenDangNhap == null || !tenDangNhap.matches("[A-Za-z0-9_.-]{1,50}"))
		throw new IllegalArgumentException("Tên đăng nhập: 1–50 chữ không dấu, số, dấu chấm, _ hoặc -.");
		if (matKhau == null || (them && matKhau.isEmpty()) || (!matKhau.isEmpty() && (matKhau.length() < 6 || matKhau.length() > 128)))
		throw new IllegalArgumentException("Mật khẩu cần 6–128 ký tự; khi sửa có thể để trống để giữ mật khẩu cũ.");
	}
	public void themTaiKhoan(String maNhanVien, String tenDangNhap, String matKhau) throws SQLException {
		TaiKhoan_DAO.kiemTraDangNhap();
		kiemTraDuLieu(maNhanVien, tenDangNhap, matKhau, true);
		String hash = bamMatKhau(matKhau);
		String sql = "Insert into dbo.TaiKhoan(maNhanVien,tenDangNhap,matKhauHash) "
		+ "Select maNhanVien,?,? from dbo.NhanVien where maNhanVien=? and hoatDong=1";
		try (PreparedStatement statement = ConnectDB.getConnection().prepareStatement(sql)) {
			statement.setString(1, tenDangNhap);
			statement.setString(2, hash);
			statement.setString(3, maNhanVien);
			if (statement.executeUpdate() != 1) throw new IllegalArgumentException("Nhân viên không tồn tại hoặc đã nghỉ.");
		}
	}
	public void capNhatTaiKhoan(String maNhanVien, String tenDangNhap, String matKhau) throws SQLException {
		TaiKhoan_DAO.kiemTraDangNhap();
		kiemTraDuLieu(maNhanVien, tenDangNhap, matKhau, false);
		boolean doiMatKhau = !matKhau.isEmpty();
		String sql = "Update dbo.TaiKhoan set tenDangNhap=?" + (doiMatKhau ? ",matKhauHash=?" : "") + " where maNhanVien=?";
		try (PreparedStatement statement = ConnectDB.getConnection().prepareStatement(sql)) {
			statement.setString(1, tenDangNhap);
			int i = 2;
			if (doiMatKhau) statement.setString(i++, bamMatKhau(matKhau));
			statement.setString(i, maNhanVien);
			if (statement.executeUpdate() != 1) throw new IllegalArgumentException("Tài khoản không còn tồn tại. Hãy làm mới.");
		}
		if (maNhanVien.equals(TaiKhoan_DAO.getTaiKhoan().getMaNhanVien()))
		TaiKhoan_DAO.getTaiKhoan().setTenDangNhap(tenDangNhap);
	}
	public void xoaTaiKhoan(String maNhanVien) throws SQLException {
		TaiKhoan_DAO.kiemTraDangNhap();
		if (maNhanVien.equals(TaiKhoan_DAO.getTaiKhoan().getMaNhanVien()))
		throw new IllegalArgumentException("Không xóa tài khoản đang đăng nhập.");
		try (PreparedStatement statement = ConnectDB.getConnection().prepareStatement("Delete from dbo.TaiKhoan where maNhanVien=?")) {
			statement.setString(1, maNhanVien);
			if (statement.executeUpdate() != 1) throw new IllegalArgumentException("Tài khoản không còn tồn tại.");
		}
	}
	private static final int ITERATIONS = 600_000;
	private static String bamMatKhau(String password) {
		if (password == null || password.length() < 6 || password.length() > 128) {
			throw new IllegalArgumentException("Mật khẩu cần từ 6 đến 128 ký tự.");
		}
		byte[] salt = new byte[16];
		new SecureRandom().nextBytes(salt);
		return "pbkdf2$" + ITERATIONS + "$" + Base64.getEncoder().encodeToString(salt)
		+ "$" + Base64.getEncoder().encodeToString(derive(password, salt, ITERATIONS));
	}
	private static boolean kiemTraMatKhau(String password, String encoded) {
		if (password == null || password.length() > 128 || encoded == null) return false;
		try {
			String[] parts = encoded.split("\\$");
			if (parts.length != 4 || !"pbkdf2".equals(parts[0])) return false;
			int iterations = Integer.parseInt(parts[1]);
			if (iterations < 100_000 || iterations > 1_000_000) return false;
			return MessageDigest.isEqual(Base64.getDecoder().decode(parts[3]),
			derive(password, Base64.getDecoder().decode(parts[2]), iterations));
		}
		catch (IllegalArgumentException ex) {
			return false;
		}
	}
	private static byte[] derive(String password, byte[] salt, int iterations) {
		char[] chars = password.toCharArray();
		PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 256);
		try {
			return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
		}
		catch (GeneralSecurityException ex) {
			throw new IllegalStateException("JDK không hỗ trợ PBKDF2.", ex);
		}
		finally {
			spec.clearPassword();
			Arrays.fill(chars, '\0');
		}
	}
}
