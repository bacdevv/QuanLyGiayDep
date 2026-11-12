package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import connectDB.ConnectDB;
import dao.DanhMuc_DAO;
import entity.DanhMuc;

public class DanhMuc_GUI extends JFrame implements ActionListener, MouseListener {
    private static final long serialVersionUID = 1L;

    private JTextField txtMaDanhMuc;
    private JTextField txtTenDanhMuc;
    private JButton btnTim;
    private JButton btnThem;
    private JButton btnLuu;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;
    private DefaultTableModel model;
    private JTable table;

    private DanhMuc_DAO dm_dao;

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
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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
        JScrollPane srp = new JScrollPane(table);
        p.add(srp, BorderLayout.CENTER);

        DocDuLieuDatabaseVaoTable();

        JPanel pNut = new JPanel();
        btnThem = new JButton("Thêm");
        btnLuu = new JButton("Lưu");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");

        pNut.add(btnThem);
        pNut.add(btnLuu);
        pNut.add(btnSua);
        pNut.add(btnXoa);
        pNut.add(btnLamMoi);

        p.add(pNut, BorderLayout.SOUTH);

        btnTim.addActionListener(this);
        btnThem.addActionListener(this);
        btnLuu.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnLamMoi.addActionListener(this);
        table.addMouseListener(this);
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
    public void mouseClicked(MouseEvent e) {
        int row = table.getSelectedRow();
        txtMaDanhMuc.setText(model.getValueAt(row, 0).toString());
        txtTenDanhMuc.setText(model.getValueAt(row, 1).toString());
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o.equals(btnLamMoi)) {
            clearTextField();
        } else if (o.equals(btnThem)) {
            if (valiData()) {
                DanhMuc dm = reverDanhMuc();
                if (dm_dao.create(dm)) {
                    DocDuLieuDatabaseVaoTable();
                    clearTextField();
                    JOptionPane.showMessageDialog(this, "Thêm danh mục thành công.");
                } else {
                    JOptionPane.showMessageDialog(this, "Thêm danh mục thất bại.");
                }
            }
        } else if (o.equals(btnXoa)) {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn danh mục để xóa.");
                return;
            }
            String maDanhMuc = model.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa danh mục này?",
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (dm_dao.delete(maDanhMuc)) {
                    DocDuLieuDatabaseVaoTable();
                    clearTextField();
                    JOptionPane.showMessageDialog(this, "Xóa danh mục thành công.");
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa danh mục thất bại.");
                }
            }
        } else if (o.equals(btnSua)) {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn danh mục để cập nhật.");
                return;
            }
            String maDanhMucOld = model.getValueAt(row, 0).toString();
            if (valiData()) {
                DanhMuc dmNew = reverDanhMuc();
                if (!maDanhMucOld.equals(dmNew.getMaDanhMuc())) {
                    JOptionPane.showMessageDialog(this, "Không được thay đổi mã danh mục khi sửa.");
                    txtMaDanhMuc.requestFocus();
                    return;
                }
                if (dm_dao.update(dmNew)) {
                    DocDuLieuDatabaseVaoTable();
                    clearTextField();
                    JOptionPane.showMessageDialog(this, "Cập nhật danh mục thành công.");
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật danh mục thất bại.");
                }
            }
        }
    }

    public void clearTextField() {
        txtMaDanhMuc.setText("");
        txtTenDanhMuc.setText("");
        txtMaDanhMuc.setEditable(true);
        txtMaDanhMuc.requestFocus();
    }

    public boolean valiData() {
        String maDanhMuc = txtMaDanhMuc.getText().trim();
        String tenDanhMuc = txtTenDanhMuc.getText().trim();

        if (maDanhMuc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã danh mục không được rỗng.");
            txtMaDanhMuc.requestFocus();
            return false;
        }

        if (tenDanhMuc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên danh mục không được rỗng.");
            txtTenDanhMuc.requestFocus();
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
