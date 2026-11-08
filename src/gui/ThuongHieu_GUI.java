package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ThuongHieu_GUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private JTextField txtMaThuongHieu;
    private JTextField txtTenThuongHieu;
    private JButton btnThem;
    private JButton btnLuu;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;
    private JButton btnThoat;
    private DefaultTableModel model;
    private JTable table;

    public ThuongHieu_GUI() {
        setTitle("Thương hiệu");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel p = new JPanel(new BorderLayout(10, 15));
        p.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        add(p);

        JPanel pTren = new JPanel(new BorderLayout(0, 20));
        JLabel lblTieuDe = new JLabel("THƯƠNG HIỆU", SwingConstants.CENTER);
        lblTieuDe.setFont(new Font("Tahoma", Font.BOLD, 28));
        lblTieuDe.setForeground(Color.DARK_GRAY);
        pTren.add(lblTieuDe, BorderLayout.NORTH);

        Box bThongTin = Box.createHorizontalBox();
        JLabel lblMaThuongHieu = new JLabel("Mã thương hiệu: ");
        JLabel lblTenThuongHieu = new JLabel("Tên thương hiệu: ");
        txtMaThuongHieu = new JTextField(15);
        txtTenThuongHieu = new JTextField(20);
        bThongTin.add(lblMaThuongHieu);
        bThongTin.add(txtMaThuongHieu);
        bThongTin.add(Box.createHorizontalStrut(20));
        bThongTin.add(lblTenThuongHieu);
        bThongTin.add(txtTenThuongHieu);
        bThongTin.setPreferredSize(new Dimension(0, 28));
        pTren.add(bThongTin, BorderLayout.SOUTH);
        p.add(pTren, BorderLayout.NORTH);

        String[] cot = {"Mã thương hiệu", "Tên thương hiệu"};
        model = new DefaultTableModel(cot, 0);
        table = new JTable(model);
        JScrollPane srp = new JScrollPane(table);
        p.add(srp, BorderLayout.CENTER);

        JPanel pNut = new JPanel();
        btnThem = new JButton("Thêm");
        btnLuu = new JButton("Lưu");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");
        btnThoat = new JButton("Thoát");
        pNut.add(btnThem);
        pNut.add(btnLuu);
        pNut.add(btnSua);
        pNut.add(btnXoa);
        pNut.add(btnLamMoi);
        pNut.add(btnThoat);
        p.add(pNut, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ThuongHieu_GUI().setVisible(true));
    }
}
