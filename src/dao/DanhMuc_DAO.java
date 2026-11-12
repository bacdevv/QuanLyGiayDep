package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.DanhMuc;
import entity.ThuongHieu;

public class DanhMuc_DAO {
	public ArrayList<DanhMuc> getalltbDanhMuc() {
		ArrayList<DanhMuc> dsDanhMuc = new ArrayList<DanhMuc>();
		try {
			ConnectDB.getInstance();
			Connection con = ConnectDB.getConnection();

			String sql = "Select * from DanhMuc";
			Statement statement = con.createStatement();

			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				String maDM = rs.getString("maDanhMuc");
				String tenDM = rs.getString(2);
				DanhMuc dm = new DanhMuc(maDM, tenDM);
				dsDanhMuc.add(dm);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dsDanhMuc;
	}

	public boolean create(DanhMuc dm) {
		int n = 0;
		ConnectDB.getInstance();
		Connection con = ConnectDB.getConnection();
		try {
			String sql = "INSERT INTO DanhMuc (maDanhMuc, tenDanhMuc) VALUES (?, ?)";
			java.sql.PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, dm.getMaDanhMuc());
			stmt.setString(2, dm.getTenDanhMuc());
			n = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return n > 0;
	}

	public boolean update(DanhMuc dm) {
		int n = 0;
		ConnectDB.getInstance();
		Connection con = ConnectDB.getConnection();
		try {
			String sql = "UPDATE DanhMuc SET tenDanhMuc = ? WHERE maDanhMuc = ?";
			java.sql.PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, dm.getTenDanhMuc());
			stmt.setString(2, dm.getMaDanhMuc());
			n = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return n > 0;
	}

	public boolean delete(String maDM) {
		int n = 0;
		ConnectDB.getInstance();
		Connection con = ConnectDB.getConnection();
		try {
			String sql = "DELETE FROM DanhMuc WHERE maDanhMuc = ?";
			java.sql.PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setString(1, maDM);
			n = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return n > 0;
	}

}
