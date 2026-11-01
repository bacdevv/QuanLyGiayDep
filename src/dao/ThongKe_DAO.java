package dao;
import connectDB.ConnectDB;
import dao.TaiKhoan_DAO;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
public class ThongKe_DAO {
	public ArrayList<Object[]> thongKeDoanhThu(LocalDate tuNgay, LocalDate denNgay) throws SQLException {
		String sql = "Select cast(h.ngayLap as date),count(*),sum(t.tongTien) from dbo.HoaDon h "
		+ "join dbo.v_TongTienHoaDon t on h.maHD=t.maHD where h.ngayLap>=? and h.ngayLap<? "
		+ "group by cast(h.ngayLap as date) order by cast(h.ngayLap as date)";
		return docThongKe(sql, tuNgay, denNgay, true);
	}
	public ArrayList<Object[]> thongKeBanChay(LocalDate tuNgay, LocalDate denNgay) throws SQLException {
		String sql = "Select sp.maSP,sp.tenSP,sum(cast(ct.soLuong as bigint)), "
		+ "sum(cast(ct.soLuong as decimal(10,0))*ct.donGia) from dbo.ChiTietHoaDon ct "
		+ "join dbo.HoaDon h on h.maHD=ct.maHD join dbo.ChiTietSanPham spct on spct.maCTSP=ct.maCTSP "
		+ "join dbo.SanPham sp on sp.maSP=spct.maSP where h.ngayLap>=? and h.ngayLap<? "
		+ "group by sp.maSP,sp.tenSP order by sum(cast(ct.soLuong as bigint)) desc,sp.maSP";
		return docThongKe(sql, tuNgay, denNgay, true);
	}
	public ArrayList<Object[]> thongKeTonKho() throws SQLException {
		String sql = "Select ct.maCTSP,sp.tenSP,ct.kichCo,ct.mauSac,ct.soLuongTon from dbo.ChiTietSanPham ct "
		+ "join dbo.SanPham sp on sp.maSP=ct.maSP order by ct.maCTSP";
		return docThongKe(sql, null, null, false);
	}
	public void kiemTraNgay(LocalDate tuNgay, LocalDate denNgay) {
		if (tuNgay == null || denNgay == null || tuNgay.isAfter(denNgay))
		throw new IllegalArgumentException("Từ ngày phải trước hoặc bằng đến ngày.");
	}
	private ArrayList<Object[]> docThongKe(String sql, LocalDate tuNgay, LocalDate denNgay, boolean locNgay) throws SQLException {
		TaiKhoan_DAO.kiemTraDangNhap();
		if (locNgay) kiemTraNgay(tuNgay, denNgay);
		ArrayList<Object[]> dsThongKe = new ArrayList<Object[]>();
		try (PreparedStatement statement = ConnectDB.getConnection().prepareStatement(sql)) {
			if (locNgay) {
				statement.setTimestamp(1, Timestamp.valueOf(tuNgay.atStartOfDay()));
				statement.setTimestamp(2, Timestamp.valueOf(denNgay.plusDays(1).atStartOfDay()));
			}
			try (ResultSet rs = statement.executeQuery()) {
				int soCot = rs.getMetaData().getColumnCount();
				while (rs.next()) {
					Object[] dong = new Object[soCot];
					for (int i = 0; i < soCot; i++) dong[i] = rs.getObject(i + 1);
					dsThongKe.add(dong);
				}
			}
		}
		return dsThongKe;
	}
}
