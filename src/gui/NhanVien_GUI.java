
package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import connectDB.ConnectDB;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;

import dao.TaiKhoan_DAO;

public class NhanVien_GUI extends JFrame implements ActionListener {

    private JTextField txtTenDN;
    private JCheckBox chkHienMatKhau;
    private JPasswordField txtMatKhau;

    private JButton btnDangNhap;
    private JButton btnThoat;

    private TaiKhoan_DAO tk_dao;

    public NhanVien_GUI() {

        // Khoi tao DAO
        tk_dao = new TaiKhoan_DAO();

        try {
            ConnectDB.getInstance().connect();
            Connection con = ConnectDB.getConnection();

            if (con != null) {
                System.out.println("KET NOI DATABASE THANH CONG!");
            } else {
                System.out.println("KET NOI DATABASE THAT BAI!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("Quản lý giày dép");
        setSize(600, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel p = new JPanel(new BorderLayout(10, 20));
        p.setBorder(BorderFactory.createEmptyBorder(30, 35, 25, 35));
        add(p);

        JPanel pTren = new JPanel(new GridLayout(2, 1, 0, 10));

        JPanel plb = new JPanel();
        JLabel lblTieuDe = new JLabel("QUẢN LÝ GIÀY DÉP");
        lblTieuDe.setFont(new Font("Arial", Font.BOLD, 25));

        JPanel plb2 = new JPanel();

        JLabel lblDangNhap = new JLabel("ĐĂNG NHẬP");
        lblDangNhap.setFont(new Font("Arial", Font.BOLD, 18));

        plb.add(lblTieuDe);
        plb2.add(lblDangNhap);

        pTren.add(plb);
        pTren.add(plb2);

        p.add(pTren, BorderLayout.NORTH);

        JPanel pGiua = new JPanel();
        pGiua.setLayout(new BoxLayout(pGiua, BoxLayout.Y_AXIS));

        // Ten dang nhap
        JLabel lblTenDN = new JLabel("Tên đăng nhập");
        lblTenDN.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel ptxt1 = new JPanel();
        txtTenDN = new JTextField();
        txtTenDN.setFont(new Font("Arial", Font.PLAIN, 16));
        txtTenDN.setPreferredSize(new Dimension(500, 38));
        ptxt1.add(txtTenDN);

        // Mat khau
        JLabel lblMatKhau = new JLabel("Mật khẩu");
        lblMatKhau.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel ptxt2 = new JPanel();
        txtMatKhau = new JPasswordField();
        txtMatKhau.setFont(new Font("Arial", Font.PLAIN, 16));
        txtMatKhau.setPreferredSize(new Dimension(500, 38));

        ptxt2.add(txtMatKhau);

        // Hien thi mat khau
        chkHienMatKhau = new JCheckBox("Hiển thị mật khẩu");
        chkHienMatKhau.setFont(new Font("Arial", Font.PLAIN, 15));

        // Them thanh phan vao panel giua
        pGiua.add(lblTenDN);
        pGiua.add(Box.createVerticalStrut(8));
        pGiua.add(ptxt1);
        pGiua.add(Box.createVerticalStrut(15));

        pGiua.add(lblMatKhau);
        pGiua.add(Box.createVerticalStrut(8));
        pGiua.add(ptxt2);
        pGiua.add(Box.createVerticalStrut(5));

        pGiua.add(chkHienMatKhau);

        p.add(pGiua, BorderLayout.CENTER);

        JPanel pCuoi = new JPanel();

        btnDangNhap = new JButton("ĐĂNG NHẬP");
        btnDangNhap.setFont(new Font("Arial", Font.BOLD, 16));

        btnThoat = new JButton("THOÁT");
        btnThoat.setFont(new Font("Arial", Font.BOLD, 16));

        pCuoi.add(btnDangNhap);
        pCuoi.add(btnThoat);

        p.add(pCuoi, BorderLayout.SOUTH);

        // Dang ky su kien
        btnDangNhap.addActionListener(this);
        btnThoat.addActionListener(this);
        chkHienMatKhau.addActionListener(this);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Xu ly hien thi mat khau
        if (e.getSource() == chkHienMatKhau) {

            if (chkHienMatKhau.isSelected()) {
                txtMatKhau.setEchoChar((char) 0);
            } else {
                txtMatKhau.setEchoChar('•');
            }
        }

        // Xu ly nut dang nhap
        if (e.getSource() == btnDangNhap) {

            String tenDangNhap = txtTenDN.getText().trim();

            String matKhau = new String(txtMatKhau.getPassword());

            // Kiem tra thong tin nhap
            if (tenDangNhap.isEmpty() || matKhau.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!",
                        "Thông báo",
                        JOptionPane.WARNING_MESSAGE);

                return;
            }

            // Goi ham kiem tra dang nhap trong DAO
            boolean ketQua = tk_dao.kiemTraDangNhap(
                    tenDangNhap,
                    matKhau);

            if (ketQua) {

                JOptionPane.showMessageDialog(
                        this,
                        "Đăng nhập thành công!",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);

                //
                // Them chuc nang chuyen sang giao dien quan ly tai day

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Tên đăng nhập hoặc mật khẩu không chính xác!",
                        "Lỗi đăng nhập",
                        JOptionPane.ERROR_MESSAGE);

                txtMatKhau.setText("");
                txtMatKhau.requestFocus();
            }
        }

        // Xu ly nut thoat
        if (e.getSource() == btnThoat) {

            int chon = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn có chắc chắn muốn thoát?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION);

            if (chon == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        new NhanVien_GUI();
    }

}
