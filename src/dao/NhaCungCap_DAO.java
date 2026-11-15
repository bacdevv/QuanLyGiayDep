package dao;

import connectDB.ConnectDB;
import entity.NhaCungCap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class NhaCungCap_DAO {

    public ArrayList<NhaCungCap> getalltbNhacc() {
        ArrayList<NhaCungCap> dsncc = new ArrayList<NhaCungCap>();
        try {
            Connection con = ConnectDB.getConnection();

            String sql = "Select * from NhaCungCap";
            Statement statement = con.createStatement();

            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                String maNCC = rs.getString("maNCC");
                String tenNCC = rs.getString("tenNCC");
                String diaChi = rs.getString("soDienTHoai");
                boolean trangThai = rs.getBoolean("hoatDong");

                NhaCungCap ncc = new NhaCungCap(
                        maNCC, tenNCC, diaChi, trangThai
                );

                dsncc.add(ncc);
            }

            rs.close();
            statement.close();

            return dsncc;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dsncc;
    }

    // maNCC , tenNCC, diaChi, trangThai
    public boolean createNCC(NhaCungCap a) {
        ConnectDB.getInstance();
        int n = 0;
        Connection con = ConnectDB.getConnection();
        PreparedStatement stmt = null;
        try {

            stmt = con.prepareStatement("insert into NhaCungCap values(?,?,?,?)");
            stmt.setString(1, a.getMaNCC());
            stmt.setString(2, a.getTenNCC());
            stmt.setString(3, a.getSoDienThoai());
            stmt.setBoolean(4, a.isHoatDong());
            n = stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return n > 0;
    }

    // String tenNCC = rs.getString("tenNCC");
    // String diaChi = rs.getString("soDienTHoai");
    // boolean trangThai = rs.getBoolean("hoatDong");
    public boolean updateNCC(NhaCungCap a) {
        ConnectDB.getInstance();
        Connection con = ConnectDB.getConnection();

        PreparedStatement stmt = null;
        int n = 0;

        try {

            String sql = "UPDATE NhaCungCap "
                    + "SET tenNCC=?, soDienTHoai=?, hoatDong=? "
                    + "WHERE maNCC=?";

            stmt = con.prepareStatement(sql);

            stmt.setString(1, a.getTenNCC());
            stmt.setString(2, a.getSoDienThoai());
            stmt.setBoolean(3, a.isHoatDong());
            stmt.setString(4, a.getMaNCC());

            n = stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return n > 0;
    }

}
