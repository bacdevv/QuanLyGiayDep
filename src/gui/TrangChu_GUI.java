package gui;
import connectDB.ConnectDB;
import dao.TaiKhoan_DAO;
import entity.TaiKhoan;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
public class TrangChu_GUI extends JFrame implements ActionListener, ChangeListener {
	private JButton bttDangXuat;
	private JTabbedPane tabChucNang;
	private TaiKhoan_GUI taiKhoanGUI;
	private JLabel lblNguoiDung;
	public TrangChu_GUI() {
		TaiKhoan_DAO.kiemTraDangNhap();
		setTitle("Quản lý giày dép");
		setSize(1200, 720);
		setMinimumSize(new Dimension(1000, 600));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		JPanel pnlTren = new JPanel(new BorderLayout());
		TaiKhoan tk = TaiKhoan_DAO.getTaiKhoan();
		lblNguoiDung = new JLabel("  Xin chào " + tk.getTenNhanVien() + " | Mã NV: " + tk.getMaNhanVien());
		pnlTren.add(lblNguoiDung);
		pnlTren.add(bttDangXuat = new JButton("Đăng xuất"), BorderLayout.EAST);
		add(pnlTren, BorderLayout.NORTH);
		bttDangXuat.addActionListener(this);
		tabChucNang = new JTabbedPane();
		tabChucNang.addTab("Trang chủ", new JLabel("Chọn tab để sử dụng. Các phần chưa làm được ghi rõ bên dưới.", SwingConstants.CENTER));
		taiKhoanGUI = new TaiKhoan_GUI();
		tabChucNang.addTab("Tài khoản", taiKhoanGUI);
		tabChucNang.addTab("Thống kê", new ThongKe_GUI());
		// Khi nhóm làm xong GUI extends JPanel, thay dòng choTichHop bằng new NhanVien_GUI(), ...
		tabChucNang.addTab("Nhân viên", choTichHop("Ngần", "NhanVien_GUI"));
		tabChucNang.addTab("Khách hàng", choTichHop("Ngần", "KhachHang_GUI"));
		tabChucNang.addTab("Danh mục / thương hiệu", choTichHop("Thi", "DanhMuc_GUI"));
		tabChucNang.addTab("Sản phẩm / size - màu", choTichHop("Thi", "SanPham_GUI"));
		tabChucNang.addTab("Bán hàng / hóa đơn", choTichHop("Phúc", "HoaDon_GUI"));
		tabChucNang.addChangeListener(this);
		add(tabChucNang);
		addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent e) {
				if (JOptionPane.showConfirmDialog(TrangChu_GUI.this, "Thoát chương trình?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					TaiKhoan_DAO.dangXuat(); ConnectDB.getInstance().disconnect(); dispose();
				}
			}
		}
		);
	}
	private JPanel choTichHop(String ten, String lop) {
		JPanel pnl = new JPanel(new BorderLayout());
		pnl.add(new JLabel("Phần " + ten + " chưa triển khai: " + lop + ". Chưa có chức năng để dùng.", SwingConstants.CENTER));
		return pnl;
	}
	@Override public void stateChanged(ChangeEvent e) {
		if (tabChucNang.getSelectedComponent() == taiKhoanGUI) taiKhoanGUI.lamMoi();
	}
	@Override public void actionPerformed(ActionEvent e) {
		if (JOptionPane.showConfirmDialog(this, "Đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			TaiKhoan_DAO.dangXuat();
			ConnectDB.getInstance().disconnect();
			dispose();
			new DangNhap_GUI().setVisible(true);
		}
	}
}
