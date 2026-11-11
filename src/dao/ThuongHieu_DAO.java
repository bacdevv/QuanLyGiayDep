package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.ThuongHieu;

public class ThuongHieu_DAO {
	public ArrayList<ThuongHieu> getalltbThuongHieu() {
		ArrayList<ThuongHieu> dsThuongHieu = new ArrayList<ThuongHieu>();
		try {
			ConnectDB.getInstance();
			Connection con = ConnectDB.getConnection();

			String sql = "Select * from ThuongHieu";
			Statement statement = con.createStatement();

			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				String maTH = rs.getString("maThuongHieu");
				String tenTH = rs.getString(2);
				ThuongHieu th = new ThuongHieu(maTH, tenTH);
				dsThuongHieu.add(th);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dsThuongHieu;
	}

	public boolean create(ThuongHieu th) {
		int n = 0;
		ConnectDB.getInstance();
		Connection con = ConnectDB.getConnection();
		try {
			String sql = "INSERT INTO ThuongHieu (maThuongHieu, tenThuongHieu) VALUES (?, ?)";
			java.sql.PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, th.getMaThuongHieu());
			stmt.setString(2, th.getTenThuongHieu());
			n = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return n > 0;
	}

	public boolean update(ThuongHieu th) {
		int n = 0;
		ConnectDB.getInstance();
		Connection con = ConnectDB.getConnection();
		try {
			String sql = "UPDATE ThuongHieu SET tenThuongHieu = ? WHERE maThuongHieu = ?";
			java.sql.PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, th.getTenThuongHieu());
			stmt.setString(2, th.getMaThuongHieu());
			n = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return n > 0;
	}

	public boolean delete(String maTH) {
		int n = 0;
		ConnectDB.getInstance();
		Connection con = ConnectDB.getConnection();
		try {
			String sql = "DELETE FROM ThuongHieu WHERE maThuongHieu = ?";
			java.sql.PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, maTH);
			n = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return n > 0;
	}

}
