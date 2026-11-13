package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.DanhMuc;

public class DanhMuc_DAO {
	public ArrayList<DanhMuc> getalltbDanhMuc() {
		ArrayList<DanhMuc> listdm = new ArrayList<DanhMuc>();
		try {
			ConnectDB.getInstance().connect();
			Connection con = ConnectDB.getConnection();
			String sql = "SELECT maDanhMuc, tenDanhMuc FROM DanhMuc";
			try (PreparedStatement stmt = con.prepareStatement(sql)) {
				try (ResultSet rs = stmt.executeQuery()) {
					while (rs.next()) {
						String ma = rs.getString("maDanhMuc");
						String ten = rs.getString("tenDanhMuc");
						DanhMuc dm = new DanhMuc(ma, ten);
						listdm.add(dm);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listdm;
	}

	public boolean create(DanhMuc dm) {
		int n = 0;
		try {
			ConnectDB.getInstance().connect();
			Connection con = ConnectDB.getConnection();
			String sql = "INSERT INTO DanhMuc (maDanhMuc, tenDanhMuc) VALUES (?, ?)";
			try (PreparedStatement stmt = con.prepareStatement(sql)) {
				stmt.setString(1, dm.getMaDanhMuc());
				stmt.setNString(2, dm.getTenDanhMuc());
				n = stmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return n > 0;
	}

	public boolean update(DanhMuc dm) {
		int n = 0;
		try {
			ConnectDB.getInstance().connect();
			Connection con = ConnectDB.getConnection();
			String sql = "UPDATE DanhMuc SET tenDanhMuc = ? WHERE maDanhMuc = ?";
			try (PreparedStatement stmt = con.prepareStatement(sql)) {
				stmt.setNString(1, dm.getTenDanhMuc());
				stmt.setString(2, dm.getMaDanhMuc());
				n = stmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return n > 0;
	}

	public boolean delete(String maDM) {
		int n = 0;
		try {
			ConnectDB.getInstance().connect();
			Connection con = ConnectDB.getConnection();
			String sql = "DELETE FROM DanhMuc WHERE maDanhMuc = ?";
			try (PreparedStatement stmt = con.prepareStatement(sql)) {
				stmt.setString(1, maDM);
				n = stmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return n > 0;
	}
}
