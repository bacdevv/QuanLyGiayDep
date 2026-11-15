package dao;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.NhaCungCap;
import entity.NhanVien;
import entity.PhieuNhap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;

public class PhieuNhap_DAO {

    public ArrayList<PhieuNhap> layphieunhap() {
        ArrayList<PhieuNhap> dsp = new ArrayList<>();
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();
            String sql = "select * from PhieuNhap";
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            while (rs.next()) {
                String map = rs.getString(1);
                NhaCungCap ncc = new NhaCungCap(rs.getString(2));
                NhanVien nv = new NhanVien(rs.getString(3));
                LocalDateTime nn = rs.getDate(4).toLocalDate().atStartOfDay();
                dsp.add(new PhieuNhap(map, ncc, nv, nn));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dsp;
    }

    public boolean taopn(PhieuNhap a) {
        int n = 0;

        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();

            String sql = "INSERT INTO PhieuNhap (MaPN, MaNCC, MaNhanVien, NgayNhap) "
                    + "VALUES (?, ?, ?, ?)";

            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, a.getMaPN());
            stmt.setString(2, a.getNhaCungCap().getMaNCC());
            stmt.setString(3, a.getNhanVien().getMaNhanVien());
            stmt.setTimestamp(4, Timestamp.valueOf(a.getNgayNhap()));

            n = stmt.executeUpdate();

            stmt.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return n > 0;
    }

    public boolean xoapn(String a) {
        int n = 0;
        try {
            ConnectDB.getInstance();
            Connection con = ConnectDB.getConnection();

            String sql = "delete from PhieuNhap where MaPN =?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, a);
            n = stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return n > 0;

    }
}
