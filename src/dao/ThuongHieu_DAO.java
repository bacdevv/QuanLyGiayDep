package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.ThuongHieu;

public class ThuongHieu_DAO {
	public ArrayList<ThuongHieu> getalltbThuongHieu() {
		ArrayList<ThuongHieu> listth = new ArrayList<ThuongHieu>();
		try {
			ConnectDB.getInstance().connect();
			Connection con = ConnectDB.getConnection();
			String sql = "SELECT maThuongHieu, tenThuongHieu FROM ThuongHieu";
			try (PreparedStatement stmt = con.prepareStatement(sql)) {
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						String ma = rs.getString("maThuongHieu");
						String ten = rs.getString("tenThuongHieu");
						ThuongHieu th = new ThuongHieu(ma, ten);
						listth.add(th);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listth;
	}

	public boolean create(ThuongHieu th) {
		int n = 0;
		try {
			ConnectDB.getInstance().connect();
			Connection con = ConnectDB.getConnection();
			String sql = "INSERT INTO ThuongHieu (maThuongHieu, tenThuongHieu) VALUES (?, ?)";
			try (PreparedStatement stmt = con.prepareStatement(sql)) {
				stmt.setString(1, th.getMaThuongHieu());
				stmt.setNString(2, th.getTenThuongHieu());
				n = stmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return n > 0;
	}

	public boolean update(ThuongHieu th) {
		int n = 0;
		try {
			ConnectDB.getInstance().connect();
			Connection con = ConnectDB.getConnection();
			String sql = "UPDATE ThuongHieu SET tenThuongHieu = ? WHERE maThuongHieu = ?";
			try (PreparedStatement stmt = con.prepareStatement(sql)) {
				stmt.setNString(1, th.getTenThuongHieu());
				stmt.setString(2, th.getMaThuongHieu());
				n = stmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return n > 0;
	}

	public boolean delete(String maTH) {
		int n = 0;
		try {
			ConnectDB.getInstance().connect();
			Connection con = ConnectDB.getConnection();
			String sql = "DELETE FROM ThuongHieu WHERE maThuongHieu = ?";
			try (PreparedStatement stmt = con.prepareStatement(sql)) {
				stmt.setString(1, maTH);
				n = stmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return n > 0;
	}
}
