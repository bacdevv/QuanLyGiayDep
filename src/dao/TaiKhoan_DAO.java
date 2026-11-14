
package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.NhanVien;
import entity.TaiKhoan;

public class TaiKhoan_DAO {

    public TaiKhoan_DAO() {

    }

    public ArrayList<TaiKhoan> gettalltbNhanVien() {

        ArrayList<TaiKhoan> dstk = new ArrayList<TaiKhoan>();

        try {
            ConnectDB.getInstance().connect();

            Connection con = ConnectDB.getConnection();

            String sql = "Select * from taikhoan";
            Statement statement = con.createStatement();

            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                NhanVien nv = new NhanVien(rs.getString("maNV"));

                String tenNV = rs.getString(2);
                String soDienThoai = rs.getString(3);

                TaiKhoan tk = new TaiKhoan(nv, tenNV, soDienThoai);
                dstk.add(tk);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dstk;
    }

    public boolean kiemTraDangNhap(String tenDangNhap, String matKhau) {

        try {
            ConnectDB.getInstance();

            Connection con = ConnectDB.getConnection();

            if (con == null) {
                System.out.println("Khong ket noi duoc database!");
                return false;
            }

            String sql = "SELECT * FROM TaiKhoan "
                    + "WHERE tenDangNhap = ? AND taiKhoanHash = ?";

            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, tenDangNhap);
            stmt.setString(2, matKhau);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                rs.close();
                stmt.close();
                return true;
            }

            rs.close();
            stmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
