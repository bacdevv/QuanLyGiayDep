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
     public ArrayList<DanhMuc> getalltbDanhMuc(){
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
}
