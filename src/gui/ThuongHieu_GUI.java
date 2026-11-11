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
import dao.ThuongHieu_DAO;
import entity.DanhMuc;
import entity.ThuongHieu;

public class ThuongHieu_GUI extends JFrame implements ActionListener, MouseListener {
    private static final long serialVersionUID = 1L;

    private JTextField txtMaThuongHieu;
    private JTextField txtTenThuongHieu;
    private JButton btnTim;
    private JButton btnThem;
    private JButton btnLuu;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;
    private DefaultTableModel model;
    private JTable table;

    private ThuongHieu_DAO th_dao;

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
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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

        btnThem.addActionListener(this);
        btnLuu.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnLamMoi.addActionListener(this);
        table.addMouseListener(this);

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
    public void mouseClicked(MouseEvent e) {
        int row = table.getSelectedRow();
        txtMaThuongHieu.setText(model.getValueAt(row, 0).toString());
        txtTenThuongHieu.setText(model.getValueAt(row, 1).toString());
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

}
