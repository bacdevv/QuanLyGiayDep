package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import connectDB.ConnectDB;
import dao.DanhMuc_DAO;
import entity.DanhMuc;

public class DanhMuc_GUI extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    private JTextField txtMaDanhMuc;
    private JTextField txtTenDanhMuc;
    private JButton btnTim;
    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;
    private DefaultTableModel model;
    private JTable table;

    private DanhMuc_DAO dm_dao;
    private SanPham_GUI sanPhamGUI;

    public void setSanPhamGUI(SanPham_GUI sanPhamGUI) {
        this.sanPhamGUI = sanPhamGUI;
    }

    public DanhMuc_GUI() {

        try {
            ConnectDB.getInstance().connect();
            System.out.println("Kết nối thành công!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dm_dao = new DanhMuc_DAO();

        setTitle("Quản lý danh mục");
        setSize(800, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel p = new JPanel(new BorderLayout(10, 15));
        p.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        add(p);

        JPanel pTren = new JPanel(new BorderLayout(0, 10));
        JLabel lblTieuDe = new JLabel("DANH MỤC", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Tahoma", Font.BOLD, 28));
        lblTieuDe.setForeground(Color.DARK_GRAY);
        pTren.add(lblTieuDe, BorderLayout.NORTH);

        Box bThongTin = Box.createHorizontalBox();
        JLabel lblMaDanhMuc = new JLabel("Mã danh mục: ");
        JLabel lblTenDanhMuc = new JLabel("Tên danh mục: ");
        txtMaDanhMuc = new JTextField(15);
        txtTenDanhMuc = new JTextField(20);
        bThongTin.add(lblMaDanhMuc);
        bThongTin.add(txtMaDanhMuc);
        bThongTin.add(Box.createHorizontalStrut(20));
        bThongTin.add(lblTenDanhMuc);
        bThongTin.add(txtTenDanhMuc);
        bThongTin.add(Box.createHorizontalStrut(15));
        btnTim = new JButton("Tìm");
        bThongTin.add(btnTim);
        bThongTin.setPreferredSize(new Dimension(0, 28));
        pTren.add(bThongTin, BorderLayout.SOUTH);
        p.add(pTren, BorderLayout.NORTH);

        String[] cot = { "Mã danh mục", "Tên danh mục" };
        model = new DefaultTableModel(cot, 0);
        table = new JTable(model);
        table.setDefaultEditor(Object.class, null);
        JScrollPane srp = new JScrollPane(table);
        p.add(srp, BorderLayout.CENTER);

        DocDuLieuDatabaseVaoTable();

        JPanel pNut = new JPanel();
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    boolean daChon = table.getSelectedRow() >= 0;
                    btnSua.setEnabled(daChon);
                    btnXoa.setEnabled(daChon);
                    fillForm(table.getSelectedRow());
                }
            }
        });
        btnLamMoi = new JButton("Làm mới");

        pNut.add(btnThem);
        pNut.add(btnSua);
        pNut.add(btnXoa);
        pNut.add(btnLamMoi);

        p.add(pNut, BorderLayout.SOUTH);

        btnTim.addActionListener(this);
        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnLamMoi.addActionListener(this);

    }

    public static void main(String[] args) {
        new DanhMuc_GUI().setVisible(true);
    }

    public void DocDuLieuDatabaseVaoTable() {
        model.setRowCount(0);

        List<DanhMuc> list = dm_dao.getalltbDanhMuc();
        for (DanhMuc dm : list) {
            model.addRow(new Object[] { dm.getMaDanhMuc(), dm.getTenDanhMuc() });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o.equals(btnLamMoi)) {
            clearTextField();
            DocDuLieuDatabaseVaoTable();
        } else if (o.equals(btnThem)) {
            if (valiData()) {
                DanhMuc dm = reverDanhMuc();
                if (dm_dao.create(dm)) {
                    DocDuLieuDatabaseVaoTable();
                    clearTextField();
                    if (sanPhamGUI != null) {
                        sanPhamGUI.updateCombobox();
                    }
                    JOptionPane.showMessageDialog(this, "Đã thêm danh mục " + dm.getMaDanhMuc() + ".",
                            "Thêm thành công", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Chưa thêm được danh mục.\nMã danh mục có thể đã tồn tại.\nVui lòng kiểm tra mã và thử lại.",
                            "Không thể thêm danh mục", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else if (o.equals(btnXoa)) {
            int row = table.getSelectedRow();
            if (row < 0) {
                return;
            }
            String maDanhMuc = model.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn xóa danh mục này?",
                    "Xác nhận xóa danh mục", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                if (dm_dao.delete(maDanhMuc)) {
                    DocDuLieuDatabaseVaoTable();
                    clearTextField();
                    if (sanPhamGUI != null) {
                        sanPhamGUI.updateCombobox();
                    }
                    JOptionPane.showMessageDialog(this, "Đã xóa danh mục " + maDanhMuc + ".",
                            "Xóa thành công", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Không thể xóa danh mục.\nDanh mục có thể đang được sản phẩm sử dụng hoặc dữ liệu đã thay đổi.\nVui lòng kiểm tra và thử lại.",
                            "Không thể xóa danh mục", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else if (o.equals(btnSua)) {
            int row = table.getSelectedRow();
            if (row < 0) {
                return;
            }
            String maDanhMucOld = model.getValueAt(row, 0).toString();
            if (valiData()) {
                DanhMuc dmNew = reverDanhMuc();
                if (!maDanhMucOld.equals(dmNew.getMaDanhMuc())) {
                    JOptionPane.showMessageDialog(this,
                            "Không được thay đổi mã danh mục khi sửa.",
                            "Không thể đổi mã", JOptionPane.WARNING_MESSAGE);
                    txtMaDanhMuc.requestFocus();
                    return;
                }
                if (dm_dao.update(dmNew)) {
                    DocDuLieuDatabaseVaoTable();
                    clearTextField();
                    if (sanPhamGUI != null) {
                        sanPhamGUI.updateCombobox();
                    }
                    JOptionPane.showMessageDialog(this, "Cập nhật danh mục thành công. " + maDanhMucOld + ".",
                            "Cập nhật thành công", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Không thể cập nhật danh mục.\nDữ liệu có thể đã thay đổi hoặc kết nối dữ liệu gặp lỗi.\nVui lòng kiểm tra và thử lại.",
                            "Không thể cập nhật danh mục", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else if (o.equals(btnTim)) {
            String ma = txtMaDanhMuc.getText().trim();
            String ten = txtTenDanhMuc.getText().trim();

            List<DanhMuc> list = dm_dao.getalltbDanhMuc();
            model.setRowCount(0);

            for (DanhMuc dm : list) {
                if (dm.getMaDanhMuc().toLowerCase().contains(ma.toLowerCase())
                        && dm.getTenDanhMuc().toLowerCase().contains(ten.toLowerCase())) {

                    model.addRow(new Object[] {
                            dm.getMaDanhMuc(),
                            dm.getTenDanhMuc()
                    });
                }
            }
        }
    }

    private void fillForm(int row) {
        if (row < 0) {
            return;
        }
        txtMaDanhMuc.setText(model.getValueAt(row, 0).toString());
        txtTenDanhMuc.setText(model.getValueAt(row, 1).toString());
        txtMaDanhMuc.setEditable(false);
    }

    public void clearTextField() {
        table.clearSelection();
        txtMaDanhMuc.setText("");
        txtTenDanhMuc.setText("");
        txtMaDanhMuc.setEditable(true);
        txtMaDanhMuc.requestFocus();
    }

    public boolean valiData() {
        String maDanhMuc = txtMaDanhMuc.getText().trim();
        String tenDanhMuc = txtTenDanhMuc.getText().trim();

        if (maDanhMuc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã danh mục không được để trống.",
                    "Thiếu mã danh mục", JOptionPane.WARNING_MESSAGE);
            txtMaDanhMuc.requestFocus();
            return false;
        }

        if (maDanhMuc.length() > 20) {
            JOptionPane.showMessageDialog(this, "Mã danh mục không được quá 20 ký tự.");
            txtMaDanhMuc.requestFocusInWindow();
            return false;
        }

        if (tenDanhMuc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên danh mục không được để trống.",
                    "Thiếu tên danh mục", JOptionPane.WARNING_MESSAGE);
            txtTenDanhMuc.requestFocus();
            return false;
        }

        if (tenDanhMuc.length() > 100) {
            JOptionPane.showMessageDialog(this, "Tên danh mục không được quá 100 ký tự.");
            txtTenDanhMuc.requestFocusInWindow();
            return false;
        }

        return true;
    }

    public DanhMuc reverDanhMuc() {
        String maDanhMuc = txtMaDanhMuc.getText().trim();
        String tenDanhMuc = txtTenDanhMuc.getText().trim();
        return new DanhMuc(maDanhMuc, tenDanhMuc);
    }
}
