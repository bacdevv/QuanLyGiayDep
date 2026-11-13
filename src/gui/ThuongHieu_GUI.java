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
import dao.ThuongHieu_DAO;
import entity.ThuongHieu;

public class ThuongHieu_GUI extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    private JTextField txtMaThuongHieu;
    private JTextField txtTenThuongHieu;
    private JButton btnTim;
    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;
    private DefaultTableModel model;
    private JTable table;

    private ThuongHieu_DAO th_dao;
    private SanPham_GUI sanPhamGUI;

    public void setSanPhamGUI(SanPham_GUI sanPhamGUI) {
        this.sanPhamGUI = sanPhamGUI;
    }

    public ThuongHieu_GUI() {

        try {
            ConnectDB.getInstance().connect();
            System.out.println("Kết nối thành công!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        th_dao = new ThuongHieu_DAO();

        setTitle("Quản lý thương hiệu");
        setSize(800, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel p = new JPanel(new BorderLayout(10, 15));
        p.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        add(p);

        JPanel pTren = new JPanel(new BorderLayout(0, 10));
        JLabel lblTieuDe = new JLabel("THƯƠNG HIỆU", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Tahoma", Font.BOLD, 28));
        lblTieuDe.setForeground(Color.DARK_GRAY);
        pTren.add(lblTieuDe);

        Box bThongTin = Box.createHorizontalBox();
        JLabel lblMaThuongHieu = new JLabel("Mã thương hiệu: ");
        JLabel lblTenThuongHieu = new JLabel("Tên thương hiệu: ");
        txtMaThuongHieu = new JTextField(20);
        txtTenThuongHieu = new JTextField(20);
        bThongTin.add(lblMaThuongHieu);
        bThongTin.add(txtMaThuongHieu);
        bThongTin.add(Box.createHorizontalStrut(20));
        bThongTin.add(lblTenThuongHieu);
        bThongTin.add(txtTenThuongHieu);
        bThongTin.add(Box.createHorizontalStrut(15));
        btnTim = new JButton("Tìm");
        bThongTin.add(btnTim);
        bThongTin.setPreferredSize(new Dimension(0, 28));
        pTren.add(bThongTin, BorderLayout.SOUTH);
        p.add(pTren, BorderLayout.NORTH);

        String[] cot = { "Mã thương hiệu", "Tên thương hiệu" };
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

        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnLamMoi.addActionListener(this);

        btnTim.addActionListener(this);

    }

    public void DocDuLieuDatabaseVaoTable() {
        model.setRowCount(0);

        List<ThuongHieu> list = th_dao.getalltbThuongHieu();
        for (ThuongHieu th : list) {
            model.addRow(new Object[] { th.getMaThuongHieu(), th.getTenThuongHieu() });
        }
    }

    public static void main(String[] args) {
        new ThuongHieu_GUI().setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o.equals(btnLamMoi)) {
            clearTextField();
            DocDuLieuDatabaseVaoTable();
        } else if (o.equals(btnThem)) {
            if (valiData()) {
                ThuongHieu th = reverThuongHieu();
                if (th_dao.create(th)) {
                    DocDuLieuDatabaseVaoTable();
                    clearTextField();
                    if (sanPhamGUI != null) {
                        sanPhamGUI.updateCombobox();
                    }
                    JOptionPane.showMessageDialog(this, "Đã thêm thương hiệu " + th.getMaThuongHieu() + ".",
                            "Thêm thành công", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Không thể thêm thương hiệu.\nMã thương hiệu có thể đã tồn tại hoặc kết nối dữ liệu gặp lỗi.\nVui lòng kiểm tra mã và thử lại.",
                            "Không thể thêm thương hiệu", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else if (o.equals(btnXoa)) {
            int row = table.getSelectedRow();
            if (row < 0) {
                return;
            }
            String maThuongHieu = model.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn xóa thương hiệu này?",
                    "Xác nhận xóa thương hiệu", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                if (th_dao.delete(maThuongHieu)) {
                    DocDuLieuDatabaseVaoTable();
                    clearTextField();
                    if (sanPhamGUI != null) {
                        sanPhamGUI.updateCombobox();
                    }
                    JOptionPane.showMessageDialog(this, "Đã xóa thương hiệu " + maThuongHieu + ".",
                            "Xóa thành công", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Không thể xóa thương hiệu.\nThương hiệu có thể đang được sản phẩm sử dụng hoặc dữ liệu đã thay đổi.\nVui lòng kiểm tra sản phẩm liên quan và thử lại.",
                            "Không thể xóa thương hiệu", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else if (o.equals(btnSua)) {
            int row = table.getSelectedRow();
            if (row < 0) {
                return;
            }
            String maThuongHieuOld = model.getValueAt(row, 0).toString();
            if (valiData()) {
                ThuongHieu thNew = reverThuongHieu();
                if (!maThuongHieuOld.equals(thNew.getMaThuongHieu())) {
                    JOptionPane.showMessageDialog(this,
                            "Không được thay đổi mã thương hiệu khi sửa.",
                            "Không thể đổi mã", JOptionPane.WARNING_MESSAGE);
                    txtMaThuongHieu.requestFocus();
                    return;
                }
                if (th_dao.update(thNew)) {
                    DocDuLieuDatabaseVaoTable();
                    clearTextField();
                    if (sanPhamGUI != null) {
                        sanPhamGUI.updateCombobox();
                    }
                    JOptionPane.showMessageDialog(this, "Cập nhật thương hiệu thành công. " + maThuongHieuOld + ".",
                            "Cập nhật thành công", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Không thể cập nhật thương hiệu.\nDữ liệu có thể đã thay đổi hoặc kết nối dữ liệu gặp lỗi.\nVui lòng kiểm tra và thử lại.",
                            "Không thể cập nhật thương hiệu", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else if (o.equals(btnTim)) {
            String ma = txtMaThuongHieu.getText().trim();
            String ten = txtTenThuongHieu.getText().trim();

            List<ThuongHieu> list = th_dao.getalltbThuongHieu();
            model.setRowCount(0);

            for (ThuongHieu th : list) {
                if (th.getMaThuongHieu().toLowerCase().contains(ma.toLowerCase())
                        && th.getTenThuongHieu().toLowerCase().contains(ten.toLowerCase())) {

                    model.addRow(new Object[] {
                            th.getMaThuongHieu(),
                            th.getTenThuongHieu()
                    });
                }
            }
        }

    }

    private void fillForm(int row) {
        if (row < 0) {
            return;
        }
        txtMaThuongHieu.setText(model.getValueAt(row, 0).toString());
        txtTenThuongHieu.setText(model.getValueAt(row, 1).toString());
        txtMaThuongHieu.setEditable(false);
    }

    public void clearTextField() {
        table.clearSelection();
        txtMaThuongHieu.setText("");
        txtTenThuongHieu.setText("");
        txtMaThuongHieu.setEditable(true);
        txtMaThuongHieu.requestFocus();
    }

    public boolean valiData() {
        String maThuongHieu = txtMaThuongHieu.getText().trim();
        String tenThuongHieu = txtTenThuongHieu.getText().trim();

        if (maThuongHieu.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã thương hiệu không được để trống.",
                    "Thiếu mã thương hiệu", JOptionPane.WARNING_MESSAGE);
            txtMaThuongHieu.requestFocus();
            return false;
        }

        if (maThuongHieu.length() > 20) {
            JOptionPane.showMessageDialog(this, "Mã thương hiệu không được quá 20 ký tự.");
            txtMaThuongHieu.requestFocusInWindow();
            return false;
        }

        if (tenThuongHieu.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên thương hiệu không được để trống.",
                    "Thiếu tên thương hiệu", JOptionPane.WARNING_MESSAGE);
            txtTenThuongHieu.requestFocus();
            return false;
        }

        if (tenThuongHieu.length() > 100) {
            JOptionPane.showMessageDialog(this, "Tên thương hiệu không được quá 100 ký tự.");
            txtTenThuongHieu.requestFocusInWindow();
            return false;
        }

        return true;
    }

    public ThuongHieu reverThuongHieu() {
        String maThuongHieu = txtMaThuongHieu.getText().trim();
        String tenThuongHieu = txtTenThuongHieu.getText().trim();
        return new ThuongHieu(maThuongHieu, tenThuongHieu);
    }
}
