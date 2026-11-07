package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ChiTietSanPham_GUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTextField txtTimKiem;
    private JButton btnTim;
    private JButton btnXoa;
    private JButton btnLamMoi;
    private JButton btnThoat;
    private DefaultTableModel model;
    private JTable table;

    public ChiTietSanPham_GUI() {
        setTitle("Chi tiết sản phẩm");
        setSize(1150, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel p = new JPanel(new BorderLayout(10, 15));
        p.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        add(p);

        JPanel pTren = new JPanel(new BorderLayout(0, 15));
        JLabel lblTieuDe = new JLabel("CHI TIẾT SẢN PHẨM", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Tahoma", Font.BOLD, 28));
        lblTieuDe.setForeground(Color.DARK_GRAY);
        pTren.add(lblTieuDe, BorderLayout.NORTH);

        JPanel pTimKiem = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel lblTimKiem = new JLabel("Mã hoặc tên sản phẩm: ");
        txtTimKiem = new JTextField(25);
        btnTim = new JButton("Tìm");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");
        btnThoat = new JButton("Thoát");
        pTimKiem.add(lblTimKiem);
        pTimKiem.add(txtTimKiem);
        pTimKiem.add(btnTim);
        pTimKiem.add(btnXoa);
        pTimKiem.add(btnLamMoi);
        pTimKiem.add(btnThoat);
        pTren.add(pTimKiem, BorderLayout.SOUTH);
        p.add(pTren, BorderLayout.NORTH);

        String[] cot = {
            "Mã chi tiết SP", "Mã sản phẩm", "Tên sản phẩm", "Danh mục",
            "Thương hiệu", "Giá bán", "Kích cỡ", "Màu sắc", "Tồn kho (đôi)", "Trạng thái"
        };
        model = new DefaultTableModel(cot, 0);
        table = new JTable(model);
        table.setRowHeight(25);
        table.setDefaultEditor(Object.class, null);
        JScrollPane srp = new JScrollPane(table);
        p.add(srp, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        new ChiTietSanPham_GUI().setVisible(true);
    }
}