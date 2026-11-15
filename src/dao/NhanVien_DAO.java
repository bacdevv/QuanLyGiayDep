package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.NhanVien;

public class NhanVien_DAO {

    public NhanVien_DAO() {

    }

    public ArrayList<NhanVien> gettalltbNhanVien() {
        ArrayList<NhanVien> dsnv = new ArrayList<NhanVien>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from nhanvien";
            Statement statement = con.createStatement();

            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String maNV = rs.getString(1);
                String tenNV = rs.getString(2);
                String soDienThoai = rs.getString(3);
                boolean hoatDong = rs.getBoolean(4);
                NhanVien nv = new NhanVien(maNV, tenNV, soDienThoai, hoatDong);
                dsnv.add(nv);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsnv;
    }
}
