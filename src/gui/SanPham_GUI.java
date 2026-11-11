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
        private JTextField txtThongBao;
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

                txtThongBao = new JTextField();
                txtThongBao.setEditable(false);
                txtThongBao.setFocusable(false);
                txtThongBao.setForeground(Color.RED);
                txtThongBao.setOpaque(false);
                txtThongBao.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
                txtThongBao.setPreferredSize(new Dimension(0, 24));
                JPanel pThongBaoVaNut = new JPanel(new BorderLayout(0, 3));
                pThongBaoVaNut.add(txtThongBao, BorderLayout.NORTH);
                pThongBaoVaNut.add(pNut, BorderLayout.CENTER);
                pThongTin.add(pThongBaoVaNut, BorderLayout.SOUTH);

                JPanel pDanhSach = new JPanel(new BorderLayout(0, 15));
                pGiua.add(pDanhSach, BorderLayout.CENTER);

                JPanel pTimKiem = new JPanel(new BorderLayout());
                pTimKiem.setBorder(BorderFactory.createTitledBorder("Tìm kiếm"));
                pDanhSach.add(pTimKiem, BorderLayout.NORTH);

                Box bTim = Box.createHorizontalBox();

                txtTimMaSP = new JTextField(10);
                txtTimTenSP = new JTextField(10);

                bTim.add(new JLabel("Mã sản phẩm: "));
                bTim.add(txtTimMaSP);
                bTim.add(Box.createHorizontalStrut(15));

                bTim.add(new JLabel("Tên sản phẩm: "));
                bTim.add(txtTimTenSP);
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
                txtmaSP.setEditable(false);

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
                        if (!valiDate())
                                return;
                        SanPham sp = reverSanPhamFromTextField();
                        try {
                                if (sp_dao.create(sp)) {
                                        model.addRow(new Object[] {
                                                        sp.getMaSP(), sp.getTenSP(),
                                                        sp.getThuongHieu().getTenThuongHieu(),
                                                        sp.getDanhMuc().getTenDanhMuc(), sp.getGiaBan(), 0,
                                                        sp.isHoatDong() ? "Đang kinh doanh" : "Ngừng kinh doanh"
                                        });
                                        clearTextField();
                                } else {
                                        txtThongBao.setText(
                                                        "Không thêm được sản phẩm. Kiểm tra mã trùng hoặc xem lỗi SQL trong Console.");
                                }
                        } catch (Exception e1) {
                                txtThongBao.setText("Không thể thêm sản phẩm: " + e1.getMessage());
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
                } else if (o.equals(btnTim)) {
                        String maSP = txtTimMaSP.getText().trim();
                        String tenSP = txtTimTenSP.getText().trim();

                        List<SanPham> dsSP;

                        if (!maSP.isEmpty()) {
                                dsSP = sp_dao.getSanPhamTheoMaSP(maSP);
                        } else if (!tenSP.isEmpty()) {
                                dsSP = sp_dao.getSanPhamTheoTenSP(tenSP);
                        } else {
                                dsSP = sp_dao.getallSanPham();
                        }
                        model.setRowCount(0);
                        for (SanPham sp : dsSP) {
                                int tongTon = sp_dao.getTongTon(sp.getMaSP());
                                model.addRow(new Object[] {
                                                sp.getMaSP(), sp.getTenSP(), sp.getThuongHieu().getTenThuongHieu(),
                                                sp.getDanhMuc().getTenDanhMuc(), sp.getGiaBan(), tongTon,
                                                sp.isHoatDong() ? "Đang kinh doanh" : "Ngừng kinh doanh"
                                });
                        }
                } else if (o.equals(btnXoa)) {
                        int row = table.getSelectedRow();
                        if (row < 0) {
                                JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để xóa.");
                                return;
                        }
                        String maSP = model.getValueAt(row, 0).toString();
                        int BD = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa sản phẩm này?",
                                        "Xác nhận", JOptionPane.YES_NO_OPTION);
                        if (BD == JOptionPane.YES_OPTION) {
                                boolean TC = sp_dao.delete(maSP);
                                if (TC) {
                                        model.removeRow(row);
                                        clearTextField();
                                        JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công.");
                                } else {
                                        JOptionPane.showMessageDialog(this, "Xóa sản phẩm thất bại.");
                                }
                        }
                } else if (o.equals(btnSua)) {
                        int row = table.getSelectedRow();

                        if (row < 0) {
                                JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm để cập nhật.");
                                return;
                        }

                        String maSPOld = table.getValueAt(row, 0).toString();

                        if (valiDate()) {
                                SanPham spNew = reverSanPhamFromTextField();
                                if (!maSPOld.equals(spNew.getMaSP())) {
                                        JOptionPane.showMessageDialog(this, "Không được thay đổi mã sản phẩm khi sửa.");
                                        txtmaSP.requestFocus();
                                        return;
                                }
                                if (sp_dao.update(spNew)) {
                                        DocDuLieuDatabaseVaoTable();
                                        for (int i = 0; i < table.getRowCount(); i++) {
                                                if (maSPOld.equals(table.getValueAt(i, 0).toString())) {
                                                        table.setRowSelectionInterval(i, i);
                                                        table.scrollRectToVisible(table.getCellRect(i, 0, true));
                                                        break;
                                                }
                                        }
                                        JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công.");
                                } else {
                                        JOptionPane.showMessageDialog(this,
                                                        "Cập nhật thất bại. Kiểm tra lỗi SQL trong Console.");
                                }
                        }
                }
        }

        public void clearTextField() {

                table.clearSelection();
                txtThongBao.setText("");

                txtmaSP.setText("");
                txttenSP.setText("");
                cboDanhMuc.setSelectedIndex(0);
                cboThuongHieu.setSelectedIndex(0);
                txtGiaBan.setText("");
                cboTrangThai.setSelectedIndex(0);
                txtmaSP.setEditable(true);
                txtmaSP.requestFocus();
        }

        private boolean valiDate() {
                txtThongBao.setText("");
                String maSP = txtmaSP.getText().trim();
                String tenSP = txttenSP.getText().trim();
                String gia = txtGiaBan.getText().trim();

                // SQL: maSP VARCHAR(20), tenSP NVARCHAR(300).
                if (maSP.isEmpty()) {
                        txtThongBao.setText("Mã sản phẩm không được rỗng");
                        txtmaSP.requestFocusInWindow();
                        return false;
                }
                if (maSP.length() > 20) {
                        txtThongBao.setText("Mã sản phẩm có tối đa 20 ký tự.");
                        txtmaSP.requestFocusInWindow();
                        return false;
                }
                if (tenSP.isEmpty()) {
                        txtThongBao.setText("Tên sản phẩm không được rỗng ");
                        txttenSP.requestFocusInWindow();
                        return false;
                }
                if (tenSP.length() > 300) {
                        txtThongBao.setText("Tên sản phẩm có tối đa 300 ký tự.");
                        txttenSP.requestFocusInWindow();
                        return false;
                }
                if (cboThuongHieu.getSelectedIndex() <= 0 || cboThuongHieu.getSelectedIndex() > listTH.size()) {
                        txtThongBao.setText("Vui lòng chọn thương hiệu.");
                        cboThuongHieu.requestFocusInWindow();
                        return false;
                }
                if (cboDanhMuc.getSelectedIndex() <= 0 || cboDanhMuc.getSelectedIndex() > listDM.size()) {
                        txtThongBao.setText("Vui lòng chọn danh mục.");
                        cboDanhMuc.requestFocusInWindow();
                        return false;
                }
                // Nhập giá bằng chữ số, có thể có phần thập phân sau dấu chấm.
                if (!gia.matches("[0-9]+(\\.[0-9]{1,2})?")) {
                        txtThongBao.setText("Giá bán phải là số dương, tối đa 2 số thập phân.(VD: 1000.00)");
                        txtGiaBan.requestFocusInWindow();
                        return false;
                }
                // SQL: giaBan DECIMAL(18,2), CHECK (giaBan >= 0).
                if (new BigDecimal(gia).compareTo(new BigDecimal("9999999999999999.99")) > 0) {
                        txtThongBao.setText("Giá bán không được vượt quá 9999999999999999.99.");
                        txtGiaBan.requestFocusInWindow();
                        return false;
                }
                if (cboTrangThai.getSelectedIndex() <= 0) {
                        txtThongBao.setText("Vui lòng chọn trạng thái.");
                        cboTrangThai.requestFocusInWindow();
                        return false;
                }
                return true;
        }

        private SanPham reverSanPhamFromTextField() {
                String maSP = txtmaSP.getText().trim();
                String tenSP = txttenSP.getText().trim();

                String gia = txtGiaBan.getText().trim();
                BigDecimal giaBan = gia.isEmpty() ? BigDecimal.ZERO : new BigDecimal(gia);

                ThuongHieu thuongHieu = listTH.get(cboThuongHieu.getSelectedIndex() - 1);

                DanhMuc danhMuc = listDM.get(cboDanhMuc.getSelectedIndex() - 1);

                boolean hoatDong = "Đang kinh doanh".equals(cboTrangThai.getSelectedItem());

                return new SanPham(maSP, danhMuc, thuongHieu, tenSP, hoatDong, giaBan);
        }
}
