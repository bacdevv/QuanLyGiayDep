package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.KhachHang;
import entity.NhanVien;

public class KhachHang_DAO {

    public KhachHang_DAO() {

    }

    public ArrayList<KhachHang> gettalltbNhanVien() {
        ArrayList<KhachHang> dskh = new ArrayList<KhachHang>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "Select * from khachhang";
            Statement statement = con.createStatement();

            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String maNV = rs.getString(1);
                String hoNV = rs.getString(2);
                String soDienThoai = rs.getString(3);
                KhachHang kh = new KhachHang(maNV, hoNV, soDienThoai);
                dskh.add(kh);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dskh;
    }
}
