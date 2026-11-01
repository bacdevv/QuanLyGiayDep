package gui;
import connectDB.ConnectDB;
import dao.TaiKhoan_DAO;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;
public class DangNhap_GUI extends JFrame implements ActionListener {
	private JTextField txtTenDangNhap;
	private JPasswordField txtMatKhau;
	private JButton bttDangNhap;
	private TaiKhoan_DAO tk_dao = new TaiKhoan_DAO();
	public DangNhap_GUI() {
		setTitle("Đăng nhập - Quản lý giày dép");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(460, 260);
		setLocationRelativeTo(null);
		setResizable(false);
		JPanel pnl = new JPanel(new GridLayout(3, 2, 12, 12));
		pnl.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
		pnl.add(new JLabel("Tên đăng nhập:"));
		pnl.add(txtTenDangNhap = new JTextField());
		pnl.add(new JLabel("Mật khẩu:"));
		pnl.add(txtMatKhau = new JPasswordField());
		pnl.add(new JLabel(""));
		pnl.add(bttDangNhap = new JButton("Đăng nhập"));
		add(pnl);
		bttDangNhap.addActionListener(this);
		getRootPane().setDefaultButton(bttDangNhap);
		addWindowListener(new WindowAdapter() {
			@Override public void windowClosed(WindowEvent e) {
				if (!daDangNhap) ConnectDB.getInstance().disconnect();
			}
		}
		);
	}
	private boolean daDangNhap;
	@Override public void actionPerformed(ActionEvent e) {
		bttDangNhap.setEnabled(false);
		try {
			// Chỉ mở lại kết nối nếu kết nối cũ không còn dùng được.
			if (ConnectDB.getConnection() == null || ConnectDB.getConnection().isClosed())
			ConnectDB.getInstance().connect();
			tk_dao.dangNhap(txtTenDangNhap.getText(), new String(txtMatKhau.getPassword()));
			TrangChu_GUI trangChu = new TrangChu_GUI();
			daDangNhap = true;
			txtMatKhau.setText("");
			dispose();
			trangChu.setVisible(true);
		}
		catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Không truy cập được database. Kiểm tra cấu hình và chạy KiemTraKetNoi trước.\n" + ex.getMessage());
		}
		catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage());
		}
		finally {
			txtMatKhau.setText("");
			bttDangNhap.setEnabled(true);
		}
	}
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override public void run() {
				new DangNhap_GUI().setVisible(true);
			}
		}
		);
	}
}
