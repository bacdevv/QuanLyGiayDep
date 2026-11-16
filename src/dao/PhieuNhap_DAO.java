package dao;

import connectDB.ConnectDB;
import entity.NhaCungCap;
import entity.NhanVien;
import entity.PhieuNhap;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class PhieuNhap_DAO {

    public ArrayList<PhieuNhap> layphieunhap() {
        ArrayList<PhieuNhap> dsp = new ArrayList<>();

        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();

            String sql = """
            SELECT pn.maPN,
                   ncc.maNCC, ncc.tenNCC,
                   nv.maNhanVien, nv.tenNhanVien,
                   pn.ngayNhap
            FROM PhieuNhap pn
            INNER JOIN NhaCungCap ncc
                ON pn.maNCC = ncc.maNCC
            INNER JOIN NhanVien nv
                ON pn.maNhanVien = nv.maNhanVien
        """;

            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);

            while (rs.next()) {
                String map = rs.getString("maPN");

                NhaCungCap ncc = new NhaCungCap(
                        rs.getString("maNCC"),
                        rs.getString("tenNCC")
                );

                NhanVien nv = new NhanVien(
                        rs.getString("maNhanVien"),
                        rs.getString("tenNhanVien")
                );

                LocalDateTime nn = rs.getTimestamp("ngayNhap").toLocalDateTime();

                dsp.add(new PhieuNhap(map, ncc, nv, nn));
            }

            rs.close();
            stm.close();

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

    public String layTenNhanVien(String maNV) {

        String tenNV = "";

        try {
            String sql = "SELECT tenNhanVien FROM NhanVien WHERE maNhanVien = ?";

            PreparedStatement ps = ConnectDB.getConnection()
                    .prepareStatement(sql);

            ps.setString(1, maNV);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                tenNV = rs.getString("tenNhanVien");
            }

            ps.close();
            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tenNV;
    }
}
