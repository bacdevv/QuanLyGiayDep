package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import connectDB.ConnectDB;

import dao.SanPham_DAO;
import dao.ThuongHieu_DAO;
import dao.DanhMuc_DAO;
import entity.DanhMuc;
import entity.SanPham;
import entity.ThuongHieu;

public class SanPham_GUI extends JFrame implements ActionListener, MouseListener {
        private JButton btnThuongHieu;
        private JButton btnDanhMuc;
        private JButton btnChiTietSanPham;
        private JTextField txtmaSP;
        private JTextField txttenSP;
        private JTextField txtGiaBan;
        private JComboBox<String> cboTrangThai;
        private JComboBox<String> cboThuongHieu;
        private JComboBox<String> cboDanhMuc;
        private JButton btnThem;
        private JButton btnLuu;
        private JButton btnSua;
        private JButton btnXoa;
        private JButton btnLamMoi;
        private JButton btnTim;
        private JTextField txtTimMaSP;
        private JTextField txtTimTenSP;
        private JComboBox<String> cboTimDanhMuc;
        private JComboBox<String> cboTimTrangThai;
        private DefaultTableModel model;
        private JTable table;

        private SanPham_DAO sp_dao;
        private ThuongHieu_DAO th_dao;
        private DanhMuc_DAO dm_dao;

        private ArrayList<ThuongHieu> listTH;
        private ArrayList<DanhMuc> listDM;

        public SanPham_GUI() {

                try {
                        ConnectDB.getInstance().connect();
                        System.out.println("Kết nối thành công!");
                } catch (SQLException e) {
                        e.printStackTrace();
                }

                sp_dao = new SanPham_DAO();
                th_dao = new ThuongHieu_DAO();
                dm_dao = new DanhMuc_DAO();

                setTitle("Sản phẩm");
                setSize(1250, 750);
                setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                setLocationRelativeTo(null);

                JPanel p = new JPanel(new BorderLayout(10, 15));
                p.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
                add(p);

                JPanel pTren = new JPanel();

                JLabel lblTieuDe = new JLabel("SẢN PHẨM");
                lblTieuDe.setFont(new Font("Tahoma", Font.BOLD, 28));
                lblTieuDe.setForeground(Color.DARK_GRAY);

                pTren.add(lblTieuDe);
                p.add(pTren, BorderLayout.NORTH);

                JPanel pGiua = new JPanel(new BorderLayout(0, 15));
                p.add(pGiua, BorderLayout.CENTER);

                JPanel pNhap = new JPanel(new BorderLayout(25, 0));
                pGiua.add(pNhap, BorderLayout.NORTH);

                JPanel pBenPhai = new JPanel(new BorderLayout(0, 10));
                pNhap.add(pBenPhai, BorderLayout.CENTER);

                JPanel pQuanLy = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
                btnThuongHieu = new JButton("Thương hiệu");
                btnDanhMuc = new JButton("Danh mục");
                btnChiTietSanPham = new JButton("Chi tiết sản phẩm");
                pQuanLy.add(btnThuongHieu);
                pQuanLy.add(btnDanhMuc);
                pQuanLy.add(btnChiTietSanPham);
                pBenPhai.add(pQuanLy, BorderLayout.NORTH);

                JPanel pThongTin = new JPanel(new BorderLayout(0, 15));
                pThongTin.setBorder(
                                BorderFactory.createTitledBorder("Thông tin"));
                pBenPhai.add(pThongTin, BorderLayout.CENTER);

                Box b = Box.createVerticalBox();
                Box b1 = Box.createHorizontalBox();
                Box b2 = Box.createHorizontalBox();
                Box b3 = Box.createHorizontalBox();

                JLabel lblmaSP = new JLabel("Mã sản phẩm: ");
                JLabel lbltenSP = new JLabel("Tên sản phẩm: ");
                JLabel lblThuongHieu = new JLabel("Thương hiệu: ");
                JLabel lblDanhMuc = new JLabel("Danh mục: ");
                JLabel lblGiaBan = new JLabel("Giá bán: ");
                JLabel lblTrangThai = new JLabel("Trạng thái: ");

                Dimension dim = new Dimension(105, 28);

                JLabel[] nhan = {
                                lblmaSP, lbltenSP, lblThuongHieu, lblDanhMuc,
                                lblGiaBan, lblTrangThai
                };

                for (JLabel lbl : nhan) {
                        lbl.setPreferredSize(dim);
                }

                txtmaSP = new JTextField(20);
                txttenSP = new JTextField(20);
                txtGiaBan = new JTextField(18);
                cboTrangThai = new JComboBox<>(
                                new String[] { "-- Chọn trạng thái --", "Đang kinh doanh", "Ngừng kinh doanh" });

                cboThuongHieu = new JComboBox<>(new String[] {
                                "-- Chọn thương hiệu --"
                });

                // đưa thương hiệu lên
                listTH = th_dao.getalltbThuongHieu();
                for (ThuongHieu th : listTH) {
                        cboThuongHieu.addItem(th.getTenThuongHieu());
                }

                cboDanhMuc = new JComboBox<>(new String[] {
                                "-- Chọn danh mục --"
                });

                // đưa danh mục lên
                listDM = dm_dao.getalltbDanhMuc();
                for (DanhMuc dm : listDM) {
                        cboDanhMuc.addItem(dm.getTenDanhMuc());
                }

                b1.add(lblmaSP);
                b1.add(txtmaSP);
                b1.add(Box.createHorizontalStrut(20));
                b1.add(lbltenSP);
                b1.add(txttenSP);

                b2.add(lblThuongHieu);
                b2.add(cboThuongHieu);
                b2.add(Box.createHorizontalStrut(20));
                b2.add(lblDanhMuc);
                b2.add(cboDanhMuc);

                b3.add(lblGiaBan);
                b3.add(txtGiaBan);
                b3.add(Box.createHorizontalStrut(20));
                b3.add(lblTrangThai);
                b3.add(cboTrangThai);

                Dimension kichThuoc = new Dimension(Integer.MAX_VALUE, 28);
                b1.setMaximumSize(kichThuoc);
                b2.setMaximumSize(kichThuoc);
                b3.setMaximumSize(kichThuoc);

                b.add(b1);
                b.add(Box.createVerticalStrut(10));
                b.add(b2);
                b.add(Box.createVerticalStrut(10));
                b.add(b3);
                b.add(Box.createVerticalStrut(10));
                b.add(Box.createVerticalGlue());

                pThongTin.add(b, BorderLayout.CENTER);

                JPanel pNut = new JPanel();

                btnThem = new JButton("Thêm");
                btnLuu = new JButton("Lưu");
                btnSua = new JButton("Sửa");
                btnXoa = new JButton("Xoá");
                btnLamMoi = new JButton("Làm mới");

                pNut.add(btnThem);
                pNut.add(btnLuu);
                pNut.add(btnSua);
                pNut.add(btnXoa);
                pNut.add(btnLamMoi);

                pThongTin.add(pNut, BorderLayout.SOUTH);

                JPanel pDanhSach = new JPanel(new BorderLayout(0, 15));
                pGiua.add(pDanhSach, BorderLayout.CENTER);

                JPanel pTimKiem = new JPanel(new BorderLayout());
                pTimKiem.setBorder(BorderFactory.createTitledBorder("Tìm kiếm"));
                pDanhSach.add(pTimKiem, BorderLayout.NORTH);

                Box bTim = Box.createHorizontalBox();

                txtTimMaSP = new JTextField(10);
                txtTimTenSP = new JTextField(10);

                cboTimDanhMuc = new JComboBox<>(new String[] {
                                "-- Tất cả danh mục --"
                });

                cboTimTrangThai = new JComboBox<>(new String[] {
                                "-- Tất cả trạng thái --"
                });

                bTim.add(new JLabel("Mã sản phẩm: "));
                bTim.add(txtTimMaSP);
                bTim.add(Box.createHorizontalStrut(15));

                bTim.add(new JLabel("Tên sản phẩm: "));
                bTim.add(txtTimTenSP);
                bTim.add(Box.createHorizontalStrut(15));

                bTim.add(new JLabel("Danh mục: "));
                bTim.add(cboTimDanhMuc);
                bTim.add(Box.createHorizontalStrut(15));

                bTim.add(new JLabel("Trạng thái: "));
                bTim.add(cboTimTrangThai);
                bTim.add(Box.createHorizontalStrut(15));
                btnTim = new JButton("Tìm");
                bTim.add(btnTim);

                pTimKiem.add(bTim, BorderLayout.CENTER);

                String[] cot = { "Mã sản phẩm", "Tên sản phẩm", "Thương hiệu", "Danh mục", "Giá bán", "Tổng tồn (đôi)",
                                "Trạng thái" };

                model = new DefaultTableModel(cot, 0);
                table = new JTable(model);
                table.setRowHeight(25);
                table.setDefaultEditor(Object.class, null);
                table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                btnChiTietSanPham.setEnabled(false);
                table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                        @Override
                        public void valueChanged(ListSelectionEvent e) {
                                if (!e.getValueIsAdjusting()) {
                                        int row = table.getSelectedRow();
                                        btnChiTietSanPham.setEnabled(row >= 0);
                                }
                        }
                });

                DocDuLieuDatabaseVaoTable();

                JScrollPane srp = new JScrollPane(table);
                pDanhSach.add(srp, BorderLayout.CENTER);

                btnThem.addActionListener(this);
                btnLuu.addActionListener(this);
                btnSua.addActionListener(this);
                btnXoa.addActionListener(this);
                btnLamMoi.addActionListener(this);
                btnTim.addActionListener(this);
                btnThuongHieu.addActionListener(this);
                btnDanhMuc.addActionListener(this);
                btnChiTietSanPham.addActionListener(this);
                cboTimDanhMuc.addActionListener(this);
                table.addMouseListener(this);

        }

        public static void main(String[] args) {
                new SanPham_GUI().setVisible(true);
        }

        public void DocDuLieuDatabaseVaoTable() {
                model.setRowCount(0);

                List<SanPham> list = sp_dao.getallSanPham();
                for (SanPham sp : list) {
                        int tongTon = sp_dao.getTongTon(sp.getMaSP());
                        model.addRow(new Object[] {
                                        sp.getMaSP(), sp.getTenSP(), sp.getThuongHieu().getTenThuongHieu(),
                                        sp.getDanhMuc().getTenDanhMuc(), sp.getGiaBan(), tongTon,
                                        sp.isHoatDong() ? "Đang kinh doanh" : "Ngừng kinh doanh"
                        });
                }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                txtmaSP.setText(model.getValueAt(row, 0).toString());
                txttenSP.setText(model.getValueAt(row, 1).toString());
                cboThuongHieu.setSelectedItem(model.getValueAt(row, 2).toString());
                cboDanhMuc.setSelectedItem(model.getValueAt(row, 3).toString());

                txtGiaBan.setText(model.getValueAt(row, 4).toString());
                cboTrangThai.setSelectedItem(model.getValueAt(row, 6).toString());

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
                Object o = e.getSource();
                if (o.equals(btnThem)) {
                        String ma = txtmaSP.getText().trim();
                        String ten = txttenSP.getText().trim();

                        String thuonghieu = cboThuongHieu.getSelectedItem().toString();
                        String danhmuc = cboDanhMuc.getSelectedItem().toString();
                        BigDecimal giaBan = new BigDecimal(txtGiaBan.getText().trim());
                        boolean hoatDong = cboTrangThai.getSelectedItem().toString().equals("Đang kinh doanh");

                        ThuongHieu thieu = new ThuongHieu(thuonghieu);
                        DanhMuc dmuc = new DanhMuc(danhmuc);
                        SanPham sp = new SanPham(ma, dmuc, thieu, ten, hoatDong, giaBan);
                        int tongTon = sp_dao.getTongTon(sp.getMaSP());

                        try {
                                sp_dao.create(sp);
                                model.addRow(new Object[] {
                                                sp.getMaSP(), sp.getTenSP(), sp.getThuongHieu().getMaThuongHieu(),
                                                sp.getDanhMuc().getMaDanhMuc(), sp.getGiaBan(), tongTon,
                                                sp.isHoatDong() ? "Đang kinh doanh" : "Ngừng kinh doanh"
                                });
                        } catch (Exception e1) {
                                JOptionPane.showMessageDialog(this, "Trung ma");
                        }
                } else if (o.equals(btnLamMoi)) {
                        clearTextField();
                } else if (o.equals(btnThuongHieu)) {
                        ThuongHieu_GUI gui = new ThuongHieu_GUI();
                        gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        gui.setLocationRelativeTo(this);
                        gui.setVisible(true);
                } else if (o.equals(btnDanhMuc)) {
                        DanhMuc_GUI gui = new DanhMuc_GUI();
                        gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        gui.setLocationRelativeTo(this);
                        gui.setVisible(true);
                } else if (o.equals(btnChiTietSanPham)) {
                        int row = table.getSelectedRow();
                        if (row < 0) {
                                JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm.");
                                return;
                        }
                        String maSP = model.getValueAt(row, 0).toString();
                        String tenSP = model.getValueAt(row, 1).toString();
                        ChiTietSanPham_GUI gui = new ChiTietSanPham_GUI();
                        gui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        gui.setSanPham(maSP, tenSP);
                        gui.setLocationRelativeTo(this);
                        gui.setVisible(true);
                }
        }

        public void clearTextField() {

                table.clearSelection();

                txtmaSP.setText("");
                txttenSP.setText("");
                cboDanhMuc.setSelectedIndex(0);
                cboThuongHieu.setSelectedIndex(0);
                txtGiaBan.setText("");
                cboTrangThai.setSelectedIndex(0);
                txtmaSP.setEditable(true);
                txtmaSP.requestFocus();
        }
}
