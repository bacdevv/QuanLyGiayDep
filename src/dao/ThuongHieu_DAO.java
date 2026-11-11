package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.ThuongHieu;

public class ThuongHieu_DAO {
    public ArrayList<ThuongHieu> getalltbThuongHieu(){
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
}
