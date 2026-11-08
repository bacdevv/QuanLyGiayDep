
package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class NhanVien_GUI extends JFrame implements ActionListener {

	private JTextField txtTenDangNhap;
	private JPasswordField txtMatKhau;

	private JCheckBox chkHienMatKhau;

	private JButton btnDangNhap, btnThoat;

	public NhanVien_GUI() {

		setTitle("Đăng Nhập Hệ Thống");
		setSize(900, 550);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		JPanel pnlMain = new JPanel(new GridLayout(1, 2));

		JPanel pnlLeft = new JPanel();
		pnlLeft.setBackground(new Color(225, 237, 250));
		pnlLeft.setLayout(new BoxLayout(pnlLeft, BoxLayout.Y_AXIS));
		pnlLeft.setBorder(new EmptyBorder(70, 30, 30, 30));

		JLabel lblIcon = new JLabel("👟");
		lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 75));
		lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel lblLogo = new JLabel("ShoeStore");
		lblLogo.setFont(new Font("Arial", Font.BOLD, 38));
		lblLogo.setForeground(new Color(24, 72, 130));
		lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel lblSlogan = new JLabel("Thời trang giày - Phong cách của bạn");
		lblSlogan.setFont(new Font("Arial", Font.ITALIC, 14));
		lblSlogan.setForeground(new Color(70, 90, 120));
		lblSlogan.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel lblImage = new JLabel("GIÀY THỂ THAO");
		lblImage.setFont(new Font("Arial", Font.BOLD, 24));
		lblImage.setForeground(new Color(35, 85, 145));
		lblImage.setAlignmentX(Component.CENTER_ALIGNMENT);

		pnlLeft.add(lblIcon);
		pnlLeft.add(Box.createVerticalStrut(15));
		pnlLeft.add(lblLogo);
		pnlLeft.add(Box.createVerticalStrut(10));
		pnlLeft.add(lblSlogan);
		pnlLeft.add(Box.createVerticalStrut(100));
		pnlLeft.add(lblImage);

		JPanel pnlRight = new JPanel();
		pnlRight.setBackground(Color.WHITE);
		pnlRight.setLayout(new BoxLayout(pnlRight, BoxLayout.Y_AXIS));
		pnlRight.setBorder(new EmptyBorder(65, 45, 40, 45));

		JLabel lblTitle = new JLabel("ĐĂNG NHẬP");
		lblTitle.setFont(new Font("Arial", Font.BOLD, 30));
		lblTitle.setForeground(new Color(24, 72, 130));
		lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

		JLabel lblSubTitle = new JLabel("Tài khoản nhân viên");
		lblSubTitle.setFont(new Font("Arial", Font.PLAIN, 17));
		lblSubTitle.setForeground(Color.GRAY);
		lblSubTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Tên đăng nhập
		JPanel pDangNhap = new JPanel(new GridLayout(2, 1));
		JLabel lblTenDangNhap = new JLabel("Tên đăng nhập");
		lblTenDangNhap.setFont(new Font("Arial", Font.BOLD, 14));
		lblTenDangNhap.setAlignmentX(Component.LEFT_ALIGNMENT);

		txtTenDangNhap = new JTextField();
		txtTenDangNhap.setFont(new Font("Arial", Font.PLAIN, 16));
		txtTenDangNhap.setPreferredSize(new Dimension(350, 42));
		txtTenDangNhap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
		txtTenDangNhap.setAlignmentX(Component.LEFT_ALIGNMENT);

		txtTenDangNhap.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(new Color(190, 205, 225)),
						new EmptyBorder(5, 10, 5, 10)));

		// Mật khẩu
		JPanel pMatKhau = new JPanel(new GridLayout(2, 1));
		JLabel lblMatKhau = new JLabel("Mật khẩu");
		lblMatKhau.setFont(new Font("Arial", Font.BOLD, 14));
		lblMatKhau.setAlignmentX(Component.LEFT_ALIGNMENT);

		txtMatKhau = new JPasswordField();
		txtMatKhau.setFont(new Font("Arial", Font.PLAIN, 16));
		txtMatKhau.setPreferredSize(new Dimension(350, 42));
		txtMatKhau.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
		txtMatKhau.setAlignmentX(Component.LEFT_ALIGNMENT);

		txtMatKhau.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(new Color(190, 205, 225)),
						new EmptyBorder(5, 10, 5, 10)));

		// Hiển thị mật khẩu
		chkHienMatKhau = new JCheckBox("Hiển thị mật khẩu");
		chkHienMatKhau.setBackground(Color.WHITE);
		chkHienMatKhau.setAlignmentX(Component.LEFT_ALIGNMENT);

		chkHienMatKhau.addActionListener(e -> {
			if (chkHienMatKhau.isSelected()) {
				txtMatKhau.setEchoChar((char) 0);
			} else {
				txtMatKhau.setEchoChar('•');
			}
		});

		// Nút đăng nhập
		btnDangNhap = new JButton("ĐĂNG NHẬP");
		btnDangNhap.setFont(new Font("Arial", Font.BOLD, 15));
		btnDangNhap.setForeground(Color.WHITE);
		btnDangNhap.setBackground(new Color(35, 100, 180));
		btnDangNhap.setFocusPainted(false);
		btnDangNhap.setBorderPainted(false);
		btnDangNhap.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnDangNhap.addActionListener(this);

		// Nút thoát
		btnThoat = new JButton("THOÁT");
		btnThoat.setFont(new Font("Arial", Font.BOLD, 15));
		btnThoat.setForeground(Color.WHITE);
		btnThoat.setBackground(new Color(130, 145, 165));
		btnThoat.setFocusPainted(false);
		btnThoat.setBorderPainted(false);
		btnThoat.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnThoat.addActionListener(this);

		JPanel pnlButton = new JPanel(new GridLayout(1, 2, 15, 0));
		pnlButton.setBackground(Color.WHITE);
		pnlButton.setMaximumSize(new Dimension(350, 48));
		pnlButton.setAlignmentX(Component.LEFT_ALIGNMENT);

		pnlButton.add(btnDangNhap);
		pnlButton.add(btnThoat);

		JLabel lblFooter = new JLabel("Hệ Thống Quản Lý Bán Hàng Giày");
		lblFooter.setFont(new Font("Arial", Font.ITALIC, 13));
		lblFooter.setForeground(new Color(50, 90, 140));
		lblFooter.setAlignmentX(Component.CENTER_ALIGNMENT);

		pnlRight.add(lblTitle);
		pnlRight.add(Box.createVerticalStrut(10));

		pnlRight.add(lblSubTitle);
		pnlRight.add(Box.createVerticalStrut(45));

		pDangNhap.add(lblTenDangNhap);
		pDangNhap.add(txtTenDangNhap);
		pnlRight.add(pDangNhap);

		pMatKhau.add(lblMatKhau);
		pMatKhau.add(txtMatKhau);
		pnlRight.add(pMatKhau);

		pnlRight.add(chkHienMatKhau);
		pnlRight.add(Box.createVerticalStrut(30));

		pnlRight.add(pnlButton);
		pnlRight.add(Box.createVerticalStrut(40));

		pnlRight.add(lblFooter);

		pnlMain.add(pnlLeft);
		pnlMain.add(pnlRight);

		add(pnlMain);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() == btnDangNhap) {

			String tenDangNhap = txtTenDangNhap.getText().trim();

			String matKhau = new String(txtMatKhau.getPassword());

			if (tenDangNhap.isEmpty() || matKhau.isEmpty()) {

				JOptionPane.showMessageDialog(
						this,
						"Vui lòng nhập đầy đủ tài khoản và mật khẩu!");

			} else if (tenDangNhap.equals("nhanvien")
					&& matKhau.equals("123456")) {

				JOptionPane.showMessageDialog(
						this,
						"Đăng nhập thành công!");

				// TODO: Mở giao diện quản lý bán hàng tại đây

			} else {

				JOptionPane.showMessageDialog(
						this,
						"Tên đăng nhập hoặc mật khẩu không đúng!",
						"Thông báo",
						JOptionPane.ERROR_MESSAGE);
			}
		}

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

		new NhanVien_GUI().setVisible(true);
	}
}
