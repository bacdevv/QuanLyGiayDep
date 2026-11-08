
package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;

public class NhanVien_GUI extends JFrame implements ActionListener {

    private JTextField txtTenDN;
    private JCheckBox chkHienMatKhau;
    private JPasswordField txtMatKhau;

    private JButton btnDangNhap;
    private JButton btnThoat;

    public NhanVien_GUI() {

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

        // Tên đăng nhập
        JLabel lblTenDN = new JLabel("Tên đăng nhập");
        lblTenDN.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel ptxt1 = new JPanel();
        txtTenDN = new JTextField();
        txtTenDN.setFont(new Font("Arial", Font.PLAIN, 16));
        txtTenDN.setPreferredSize(new Dimension(500, 38));
        ptxt1.add(txtTenDN);

        // Mật khẩu
        JLabel lblMatKhau = new JLabel("Mật khẩu");
        lblMatKhau.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel ptxt2 = new JPanel();
        txtMatKhau = new JPasswordField();
        txtMatKhau.setFont(new Font("Arial", Font.PLAIN, 16));
        txtMatKhau.setPreferredSize(new Dimension(500, 38));

        ptxt2.add(txtMatKhau);

        // Hiển thị mật khẩu
        chkHienMatKhau = new JCheckBox("Hiển thị mật khẩu");
        chkHienMatKhau.setFont(new Font("Arial", Font.PLAIN, 15));

        // Thêm thành phần vào panel giữa
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

        // Đăng ký sự kiện
        btnDangNhap.addActionListener(this);
        btnThoat.addActionListener(this);
        chkHienMatKhau.addActionListener(this);

        setVisible(true);
    }

    public static void main(String[] args) {
        new NhanVien_GUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == chkHienMatKhau) {

            if (chkHienMatKhau.isSelected()) {
                txtMatKhau.setEchoChar((char) 0);
            } else {
                txtMatKhau.setEchoChar('•');
            }
        }
    }
}
