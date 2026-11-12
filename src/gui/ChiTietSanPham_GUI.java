package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.List;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import connectDB.ConnectDB;
import dao.ChiTietSanPham_DAO;

import dao.SanPham_DAO;

import entity.ChiTietSanPham;
import entity.SanPham;

public class ChiTietSanPham_GUI extends JFrame implements ActionListener, MouseListener {
    private static final long serialVersionUID = 1L;

    private JTextField txtMaSP;
    private JTextField txtTenSP;
    private JTextField txtKichCo;
    private JTextField txtMauSac;
    private JTextField txtTonKho;
    private JTextField txtMaCTSP;
    private JComboBox<String> cboTrangThai;
    private JComboBox<String> cboLocTheo;
    private JTextField txtTimMaCTSP;
    private JButton btnTim;
    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnLamMoi;
    private DefaultTableModel model;
    private JTable table;
    private TableRowSorter<DefaultTableModel> sorter;

    private SanPham_DAO sp_dao;
    private ChiTietSanPham_DAO ctsp_dao;

    public ChiTietSanPham_GUI() {

        try {
            ConnectDB.getInstance().connect();
            System.out.println("Kết nối thành công!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        sp_dao = new SanPham_DAO();
        ctsp_dao = new ChiTietSanPham_DAO();

        setTitle("Chi tiết sản phẩm");
        setSize(1150, 650);
        setMinimumSize(new java.awt.Dimension(950, 600));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        add(p);

        JPanel pTren = new JPanel();
        p.add(pTren, BorderLayout.NORTH);
        JLabel lblTieuDe = new JLabel("CHI TIẾT SẢN PHẨM");
        lblTieuDe.setFont(new Font("Tahoma", Font.BOLD, 28));
        lblTieuDe.setForeground(Color.DARK_GRAY);
        pTren.add(lblTieuDe);

        JPanel pGiua = new JPanel(new BorderLayout());
        p.add(pGiua, BorderLayout.CENTER);
        JPanel pThongTin = new JPanel(new GridLayout(1, 2, 20, 0));
        pThongTin.setBorder(BorderFactory.createTitledBorder("Sản phẩm đang xem"));
        txtMaSP = new JTextField(30);
        txtMaSP.setEditable(false);
        txtTenSP = new JTextField(30);
        txtTenSP.setEditable(false);
        JPanel pMa = new JPanel();
        pMa.add(new JLabel("Mã sản phẩm:"));
        pMa.add(txtMaSP);
        pThongTin.add(pMa);
        JPanel pTen = new JPanel();
        pTen.add(new JLabel("Tên sản phẩm:"));
        pTen.add(txtTenSP);
        pThongTin.add(pTen);

        JPanel pNhap = new JPanel(new BorderLayout(0, 4));
        pNhap.setBorder(BorderFactory.createTitledBorder("Thông tin"));
        JPanel pTruongNhap = new JPanel(new BorderLayout(0, 6));
        pTruongNhap.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        txtKichCo = new JTextField(26);
        txtMauSac = new JTextField(26);
        cboTrangThai = new JComboBox<>(new String[] { "Đang kinh doanh", "Ngừng kinh doanh" });
        JLabel lblKichCo = new JLabel("Kích cỡ:");
        JLabel lblMauSac = new JLabel("Màu sắc:");
        JLabel lblTrangThai = new JLabel("Trạng thái:");

        lblKichCo.setPreferredSize(new Dimension(85, 28));
        lblMauSac.setPreferredSize(new Dimension(85, 28));
        lblTrangThai.setPreferredSize(new Dimension(85, 28));
        txtKichCo.setPreferredSize(new Dimension(260, 28));
        txtMauSac.setPreferredSize(new Dimension(260, 28));
        cboTrangThai.setPreferredSize(new Dimension(260, 28));

        JPanel pKichCo = new JPanel(new BorderLayout());
        pKichCo.add(lblKichCo, BorderLayout.WEST);
        pKichCo.add(txtKichCo, BorderLayout.CENTER);

        JPanel pMauSac = new JPanel(new BorderLayout());
        pMauSac.add(lblMauSac, BorderLayout.WEST);
        pMauSac.add(txtMauSac, BorderLayout.CENTER);

        JPanel pTrangThai = new JPanel(new BorderLayout());
        pTrangThai.add(lblTrangThai, BorderLayout.WEST);
        pTrangThai.add(cboTrangThai, BorderLayout.CENTER);

        JPanel pKichCoMauSac = new JPanel(new GridLayout(1, 2, 20, 0));
        pKichCoMauSac.add(pKichCo);
        pKichCoMauSac.add(pMauSac);

        pTruongNhap.add(pKichCoMauSac, BorderLayout.CENTER);

        txtTonKho = new JTextField(26);
        txtMaCTSP = new JTextField(26);

        txtTonKho.setEditable(false);
        txtMaCTSP.setEditable(false);

        txtTonKho.setPreferredSize(new Dimension(260, 28));
        txtMaCTSP.setPreferredSize(new Dimension(260, 28));

        JLabel lblTonKho = new JLabel("Tồn kho (đôi):");
        JLabel lblMaCTSP = new JLabel("Mã CTSP:");

        lblTonKho.setPreferredSize(new Dimension(85, 28));
        lblMaCTSP.setPreferredSize(new Dimension(85, 28));

        JPanel pTonKho = new JPanel(new BorderLayout());
        pTonKho.add(lblTonKho, BorderLayout.WEST);
        pTonKho.add(txtTonKho, BorderLayout.CENTER);

        JPanel pMaCTSP = new JPanel(new BorderLayout());
        pMaCTSP.add(lblMaCTSP, BorderLayout.WEST);
        pMaCTSP.add(txtMaCTSP, BorderLayout.CENTER);

        JPanel pTonKhoMaCTSP = new JPanel(new GridLayout(1, 2, 20, 0));
        pTonKhoMaCTSP.add(pMaCTSP);
        pTonKhoMaCTSP.add(pTonKho);

        pTruongNhap.add(pTonKhoMaCTSP, BorderLayout.NORTH);

        JPanel pDongTrangThai = new JPanel(new BorderLayout());
        pDongTrangThai.add(pTrangThai, BorderLayout.WEST);
        pTruongNhap.add(pDongTrangThai, BorderLayout.SOUTH);
        JPanel pCanTrai = new JPanel(new BorderLayout());
        pCanTrai.add(pTruongNhap, BorderLayout.WEST);
        pNhap.add(pCanTrai, BorderLayout.CENTER);

        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");

        JPanel pThaoTac = new JPanel(new FlowLayout());
        pThaoTac.add(btnThem);
        pThaoTac.add(btnSua);
        pThaoTac.add(btnXoa);
        pThaoTac.add(btnLamMoi);
        cboLocTheo = new JComboBox<>(new String[] {
                "-- Lọc theo --", "Kích cỡ", "Màu sắc", "Trạng thái"
        });
        pThaoTac.add(cboLocTheo);
        pNhap.add(pThaoTac, BorderLayout.SOUTH);

        JPanel pNoiDungTren = new JPanel(new BorderLayout());
        pNoiDungTren.add(pThongTin, BorderLayout.NORTH);
        pNoiDungTren.add(pNhap, BorderLayout.CENTER);
        pGiua.add(pNoiDungTren, BorderLayout.NORTH);

        String[] cot = {
                "Mã chi tiết SP", "Mã sản phẩm", "Kích cỡ", "Màu sắc", "Tồn kho (đôi)", "Trạng thái"
        };
        model = new DefaultTableModel(cot, 0);
        table = new JTable(model);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        table.setRowHeight(25);
        table.setDefaultEditor(Object.class, null);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting())
                return;
            boolean daChon = table.getSelectedRow() >= 0;
            btnSua.setEnabled(daChon);
            btnXoa.setEnabled(daChon);
            if (daChon) {
                int row = table.convertRowIndexToModel(table.getSelectedRow());
                txtMaCTSP.setText(String.valueOf(model.getValueAt(row, 0)));
                txtTonKho.setText(String.valueOf(model.getValueAt(row, 4)));
                txtKichCo.setText(String.valueOf(model.getValueAt(row, 2)));
                txtMauSac.setText(String.valueOf(model.getValueAt(row, 3)));
                cboTrangThai.setSelectedItem(model.getValueAt(row, 5));
            } else {
                txtMaCTSP.setText("");
                txtTonKho.setText("");
            }
        });
        JScrollPane srp = new JScrollPane(table);
        srp.setBorder(BorderFactory.createTitledBorder("Danh sách phiên bản theo kích cỡ và màu sắc"));
        pGiua.add(srp, BorderLayout.CENTER);

        JPanel pTimKiem = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        pTimKiem.add(new JLabel("Mã CTSP:"));
        txtTimMaCTSP = new JTextField(12);
        btnTim = new JButton("Tìm");
        pTimKiem.add(txtTimMaCTSP);
        pTimKiem.add(btnTim);
        pGiua.add(pTimKiem, BorderLayout.SOUTH);

        table.addMouseListener(this);

    }

    public static void main(String[] args) {
        new ChiTietSanPham_GUI().setVisible(true);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = table.getSelectedRow();
        row = table.convertRowIndexToModel(row);
        txtMaSP.setText(model.getValueAt(row, 1).toString());
        List<SanPham> dsSP = sp_dao.getSanPhamTheoMaSP(model.getValueAt(row, 1).toString());
        txtTenSP.setText(dsSP.isEmpty() ? "" : dsSP.get(0).getTenSP());
        txtMaCTSP.setText(model.getValueAt(row, 0).toString());
        txtTonKho.setText(model.getValueAt(row, 4).toString());
        txtKichCo.setText(model.getValueAt(row, 2).toString());
        txtMauSac.setText(model.getValueAt(row, 3).toString());
        cboTrangThai.setSelectedItem(model.getValueAt(row, 5).toString());
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

    public void setSanPham(String maSP, String tenSP) {
        txtMaSP.setText(maSP);
        txtTenSP.setText(tenSP);
        DocDuLieuDatabaseVaoTable();
    }

    public void DocDuLieuDatabaseVaoTable() {
        model.setRowCount(0);

        List<ChiTietSanPham> list = ctsp_dao.getChiTietSanPhamTheoMaSP(txtMaSP.getText());
        for (ChiTietSanPham ctsp : list) {
            model.addRow(new Object[] {

                    ctsp.getMaCTSP(), ctsp.getSanPham().getMaSP(),
                    ctsp.getKichCo(), ctsp.getMauSac(), ctsp.getSoLuongTon(),
                    ctsp.isHoatDong() ? "Đang kinh doanh" : "Ngừng kinh doanh"
            });
        }
    }

}
