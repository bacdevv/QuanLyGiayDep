package gui;

import dao.TaiKhoan_DAO;
import entity.TaiKhoan;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TaiKhoan_GUI extends JPanel
		implements ActionListener, MouseListener, javax.swing.event.ListSelectionListener {
	private JComboBox<String> cboNhanVien, cboTrangThai;
	private JTextField txtTenDangNhap, txtTim;
	private JPasswordField txtMatKhau;
	private JButton bttThem, bttLuu, bttXoa, bttXoaTrang, bttTim, bttLamMoi, bttChiTiet;
	private JTable tableTaiKhoan;
	private DefaultTableModel modelTaiKhoan;
	private TaiKhoan_DAO tk_dao = new TaiKhoan_DAO();
	private ArrayList<TaiKhoan> dsTaiKhoan = new ArrayList<TaiKhoan>();
	private String maDangChon;

	public TaiKhoan_GUI() {
		setLayout(new BorderLayout(10, 10));
		JPanel pnlTren = new JPanel(new BorderLayout());
		JPanel pnlNhap = new JPanel(new GridLayout(3, 2, 8, 8));
		pnlNhap.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
		pnlNhap.add(new JLabel("Nhân viên:"));
		pnlNhap.add(cboNhanVien = new JComboBox<String>());
		pnlNhap.add(new JLabel("Tên đăng nhập:"));
		pnlNhap.add(txtTenDangNhap = new JTextField());
		pnlNhap.add(new JLabel("Mật khẩu mới (sửa: trống = giữ):"));
		pnlNhap.add(txtMatKhau = new JPasswordField());
		pnlTren.add(pnlNhap, BorderLayout.NORTH);
		JPanel pnlTim = new JPanel();
		pnlTim.add(new JLabel("Mã / tên nhân viên / tên đăng nhập:"));
		pnlTim.add(txtTim = new JTextField(16));
		pnlTim.add(cboTrangThai = new JComboBox<String>(new String[] {
				"Tất cả", "Đang làm", "Đã nghỉ"
		}));
		pnlTim.add(bttTim = new JButton("Tìm / Lọc"));
		pnlTim.add(bttLamMoi = new JButton("Làm mới"));
		pnlTren.add(pnlTim);
		add(pnlTren, BorderLayout.NORTH);
		String[] colHeader = {
				"Mã NV", "Tên nhân viên", "Tên đăng nhập", "Trạng thái nhân viên"
		};
		modelTaiKhoan = new DefaultTableModel(colHeader, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableTaiKhoan = new JTable(modelTaiKhoan);
		tableTaiKhoan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableTaiKhoan.setAutoCreateRowSorter(true);
		tableTaiKhoan.addMouseListener(this);
		tableTaiKhoan.getSelectionModel().addListSelectionListener(this);
		JScrollPane scroll = new JScrollPane(tableTaiKhoan);
		scroll.setColumnHeaderView(tableTaiKhoan.getTableHeader());
		add(scroll);
		JPanel pnlNut = new JPanel();
		pnlNut.add(bttThem = new JButton("Thêm"));
		pnlNut.add(bttLuu = new JButton("Lưu sửa"));
		pnlNut.add(bttXoa = new JButton("Xóa"));
		pnlNut.add(bttXoaTrang = new JButton("Xóa trắng"));
		pnlNut.add(bttChiTiet = new JButton("Chi tiết"));
		add(pnlNut, BorderLayout.SOUTH);
		JButton[] dsNut = {
				bttThem, bttLuu, bttXoa, bttXoaTrang, bttTim, bttLamMoi, bttChiTiet
		};
		for (JButton btt : dsNut)
			btt.addActionListener(this);
		txtTim.addActionListener(this);
		// Bấm Làm mới khi cần cập nhật nhân viên vừa được Ngần thêm.
	}

	public void DocDuLieuDatabaseVaoTable() throws SQLException {
		dsTaiKhoan = tk_dao.timTaiKhoan(txtTim.getText(), cboTrangThai.getSelectedItem().toString());
		modelTaiKhoan.setRowCount(0);
		for (TaiKhoan tk : dsTaiKhoan)
			modelTaiKhoan.addRow(new Object[] {
					tk.getMaNhanVien(), tk.getTenNhanVien(), tk.getTenDangNhap(),
					tk.isHoatDong() ? "Đang làm" : "Đã nghỉ"
			});
	}

	private void docNhanVien() throws SQLException {
		cboNhanVien.removeAllItems();
		for (String[] nv : tk_dao.getNhanVienChuaCoTaiKhoan())
			cboNhanVien.addItem(nv[0] + " - " + nv[1]);
	}

	private void xoaTrang() throws SQLException {
		maDangChon = null;
		tableTaiKhoan.clearSelection();
		docNhanVien();
		cboNhanVien.setEnabled(true);
		txtTenDangNhap.setText("");
		txtMatKhau.setText("");
	}

	private TaiKhoan getTaiKhoanDangChon() {
		int row = tableTaiKhoan.getSelectedRow();
		if (row < 0)
			throw new IllegalArgumentException("Chọn một tài khoản trong bảng.");
		return dsTaiKhoan.get(tableTaiKhoan.convertRowIndexToModel(row));
	}

	private void chonDong() {
		TaiKhoan tk = getTaiKhoanDangChon();
		maDangChon = tk.getMaNhanVien();
		cboNhanVien.removeAllItems();
		cboNhanVien.addItem(maDangChon + " - " + tk.getTenNhanVien());
		cboNhanVien.setEnabled(false);
		txtTenDangNhap.setText(tk.getTenDangNhap());
		txtMatKhau.setText("");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			Object o = e.getSource();
			if (o == bttThem) {
				if (maDangChon != null)
					throw new IllegalArgumentException("Bấm Xóa trắng trước khi thêm tài khoản.");
				if (cboNhanVien.getSelectedItem() == null)
					throw new IllegalArgumentException("Không có nhân viên đang làm chưa được cấp tài khoản.");
				String ma = cboNhanVien.getSelectedItem().toString().split(" - ", 2)[0];
				tk_dao.themTaiKhoan(ma, txtTenDangNhap.getText().trim(), new String(txtMatKhau.getPassword()));
				JOptionPane.showMessageDialog(this, "Đã thêm tài khoản.");
				xoaTrang();
				DocDuLieuDatabaseVaoTable();
			} else if (o == bttLuu) {
				if (maDangChon == null)
					throw new IllegalArgumentException("Chọn tài khoản cần sửa.");
				tk_dao.capNhatTaiKhoan(maDangChon, txtTenDangNhap.getText().trim(),
						new String(txtMatKhau.getPassword()));
				JOptionPane.showMessageDialog(this, "Đã cập nhật tài khoản.");
				xoaTrang();
				DocDuLieuDatabaseVaoTable();
			} else if (o == bttXoa) {
				TaiKhoan tk = getTaiKhoanDangChon();
				if (JOptionPane.showConfirmDialog(this,
						"Xóa quyền đăng nhập của " + tk.getTenDangNhap() + "? Không xóa nhân viên hoặc hóa đơn.",
						"Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					tk_dao.xoaTaiKhoan(tk.getMaNhanVien());
					xoaTrang();
					DocDuLieuDatabaseVaoTable();
				}
			} else if (o == bttXoaTrang)
				xoaTrang();
			else if (o == bttLamMoi) {
				txtTim.setText("");
				cboTrangThai.setSelectedIndex(0);
				xoaTrang();
				DocDuLieuDatabaseVaoTable();
			} else if (o == bttTim || o == txtTim) {
				xoaTrang();
				DocDuLieuDatabaseVaoTable();
			} else if (o == bttChiTiet) {
				TaiKhoan tk = getTaiKhoanDangChon();
				JOptionPane.showMessageDialog(this,
						"Mã NV: " + tk.getMaNhanVien() + "\nNhân viên: " + tk.getTenNhanVien()
								+ "\nTên đăng nhập: " + tk.getTenDangNhap() + "\nTrạng thái: "
								+ (tk.isHoatDong() ? "Đang làm" : "Đã nghỉ"));
			}
		} catch (SQLException ex) {
			String loi = ex.getErrorCode() == 2627 || ex.getErrorCode() == 2601
					? "Nhân viên đã có tài khoản hoặc tên đăng nhập đã tồn tại."
					: ex.getMessage();
			JOptionPane.showMessageDialog(this, loi, "Lỗi database", JOptionPane.ERROR_MESSAGE);
		} catch (IllegalArgumentException | SecurityException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage());
		} finally {
			txtMatKhau.setText("");
		}
	}

	public void lamMoi() {
		try {
			xoaTrang();
			DocDuLieuDatabaseVaoTable();
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "Không tải được tài khoản: " + ex.getMessage());
		}
	}

	@Override
	public void valueChanged(javax.swing.event.ListSelectionEvent e) {
		if (!e.getValueIsAdjusting() && tableTaiKhoan.getSelectedRow() >= 0)
			chonDong();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (tableTaiKhoan.getSelectedRow() >= 0)
			chonDong();
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
