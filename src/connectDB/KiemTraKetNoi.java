package connectDB;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class KiemTraKetNoi {
	public static void main(String[] args) {
		Statement statement = null;
		ResultSet rs = null;
		try {
			ConnectDB.getInstance().connect();
			Connection con = ConnectDB.getConnection();
			System.out.println("Ket noi thanh cong: " + con.getCatalog());
			String[] dsBang = {
				"NhanVien", "TaiKhoan", "DanhMuc", "ThuongHieu",
				"SanPham", "ChiTietSanPham", "KhachHang", "HoaDon", "ChiTietHoaDon"
			}
			;
			statement = con.createStatement();
			for (int i = 0; i < dsBang.length; i++) {
				// Ten bang lay tu danh sach co dinh, khong nhan tu nguoi dung.
				String sql = "Select count(*) from dbo." + dsBang[i];
				rs = statement.executeQuery(sql);
				if (rs.next()) {
					System.out.println(dsBang[i] + ": " + rs.getInt(1) + " dong");
				}
				rs.close();
				rs = null;
			}
			System.out.println("Da kiem tra du 9 bang.");
		}
		catch (SQLException e) {
			System.out.println("Kiem tra that bai. Xem chi tiet loi ben duoi:");
			e.printStackTrace();
		}
		finally {
			try {
				if (rs != null) {
					rs.close();
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (statement != null) {
					statement.close();
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			ConnectDB.getInstance().disconnect();
		}
	}
}
