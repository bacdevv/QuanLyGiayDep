package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.awt.Dimension;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import connectDB.ConnectDB;
import dao.ChiTietSanPham_DAO;

import entity.ChiTietSanPham;
import entity.SanPham;

public class ChiTietSanPham_GUI extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;

    private JTextField txtMaSP;
    private JTextField txtTenSP;
    private JTextField txtKichCo;
    private JTextField txtMauSac;
    private JTextField txtTonKho;
    private JTextField txtMaCTSP;
    private JTextField txtThongBao;
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

    private ChiTietSanPham_DAO ctsp_dao;

    public ChiTietSanPham_GUI() {

        try {
            ConnectDB.getInstance().connect();
            System.out.println("Kết nối thành công!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ctsp_dao = new ChiTietSanPham_DAO();

        setTitle("Chi tiết sản phẩm");
        setSize(1150, 650);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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
        txtMaCTSP.setEditable(true);

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
        txtThongBao = new JTextField();
        txtThongBao.setEditable(false);
        txtThongBao.setForeground(Color.RED);
        txtThongBao.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
        txtThongBao.setOpaque(false);

        JPanel pThongBaoThaoTac = new JPanel(new BorderLayout());
        pThongBaoThaoTac.add(txtThongBao, BorderLayout.NORTH);
        pThongBaoThaoTac.add(pThaoTac, BorderLayout.SOUTH);
        pNhap.add(pThongBaoThaoTac, BorderLayout.SOUTH);

        JPanel pNoiDungTren = new JPanel(new BorderLayout());
        pNoiDungTren.add(pThongTin, BorderLayout.NORTH);
        pNoiDungTren.add(pNhap, BorderLayout.CENTER);
        pGiua.add(pNoiDungTren, BorderLayout.NORTH);

        String[] cot = {
                "Mã chi tiết SP", "Mã sản phẩm", "Kích cỡ", "Màu sắc", "Tồn kho (đôi)", "Trạng thái"
        };
        model = new DefaultTableModel(cot, 0);
        table = new JTable(model);
        table.setRowHeight(25);
        table.setDefaultEditor(Object.class, null);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        btnSua.setEnabled(false);
        btnXoa.setEnabled(false);

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int row = table.getSelectedRow();
                    btnSua.setEnabled(row >= 0);
                    btnXoa.setEnabled(row >= 0);
                    fillForm(row);
                }
            }

        });
        JScrollPane srp = new JScrollPane(table);
        srp.setBorder(BorderFactory.createTitledBorder("Danh sách chi tiết sản phẩm"));
        pGiua.add(srp, BorderLayout.CENTER);

        JPanel pTimKiem = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        pTimKiem.add(new JLabel("Mã CTSP:"));
        txtTimMaCTSP = new JTextField(12);
        btnTim = new JButton("Tìm");
        pTimKiem.add(txtTimMaCTSP);
        pTimKiem.add(btnTim);
        pGiua.add(pTimKiem, BorderLayout.SOUTH);

        btnTim.addActionListener(this);
        btnLamMoi.addActionListener(this);
        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        cboLocTheo.addActionListener(this);

    }

    public static void main(String[] args) {
        new ChiTietSanPham_GUI().setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o.equals(btnTim)) {
            String maCTSP = txtTimMaCTSP.getText().trim();
            if (maCTSP.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mã CTSP để tìm kiếm.", "Chưa nhập mã CTSP",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            List<ChiTietSanPham> list = ctsp_dao.getChiTietSanPhamTheoMaSP(txtMaSP.getText().trim());
            for (int i = 0; i < list.size(); i++) {
                if (!list.get(i).getMaCTSP().equalsIgnoreCase(maCTSP)) {
                    list.remove(i);
                    i--;
                }
            }
            if (list.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy chi tiết sản phẩm với mã CTSP: " + maCTSP,
                        "Không tìm thấy", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            txtThongBao.setText("");
            cboLocTheo.removeActionListener(this);
            cboLocTheo.setSelectedIndex(0);
            cboLocTheo.addActionListener(this);
            model.setRowCount(0);
            for (ChiTietSanPham ctsp : list) {
                model.addRow(new Object[] {
                        ctsp.getMaCTSP(), ctsp.getSanPham().getMaSP(),
                        ctsp.getKichCo(), ctsp.getMauSac(), ctsp.getSoLuongTon(),
                        ctsp.isHoatDong() ? "Đang kinh doanh" : "Ngừng kinh doanh"
                });
            }
        } else if (o.equals(btnLamMoi)) {
            clearTextField();
            DocDuLieuDatabaseVaoTable();
        } else if (o.equals(btnThem)) {
            if (validateInput()) {
                ChiTietSanPham ctsp = reverChiTietSanPham();
                ctsp.setSoLuongTon(0);

                if (ctsp_dao.create(ctsp)) {
                    model.addRow(new Object[] {
                            ctsp.getMaCTSP(), ctsp.getSanPham().getMaSP(),
                            ctsp.getKichCo(), ctsp.getMauSac(), ctsp.getSoLuongTon(),
                            ctsp.isHoatDong() ? "Đang kinh doanh" : "Ngừng kinh doanh"
                    });

                    clearTextField();
                    JOptionPane.showMessageDialog(this,
                            "Thêm chi tiết sản phẩm thành công.",
                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Không thể thêm. Hãy kiểm tra mã chi tiết, kích cỡ và màu sắc có bị trùng không.",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (o.equals(btnSua)) {
            int row = table.getSelectedRow();
            if (row < 0) {
                return;
            }
            String maCTSPOld = table.getValueAt(row, 0).toString();
            if (validateInput()) {
                ChiTietSanPham ctspNew = reverChiTietSanPham();
                if (!maCTSPOld.equals(ctspNew.getMaCTSP())) {
                    JOptionPane.showMessageDialog(this,
                            "Không được thay đổi mã chi tiết sản phẩm khi sửa.",
                            "Không thể đổi mã", JOptionPane.WARNING_MESSAGE);
                    txtMaCTSP.requestFocusInWindow();
                    return;
                }
                if (ctsp_dao.update(ctspNew)) {
                    DocDuLieuDatabaseVaoTable();
                    for (int i = 0; i < table.getRowCount(); i++) {
                        if (maCTSPOld.equals(table.getValueAt(i, 0).toString())) {
                            table.setRowSelectionInterval(i, i);
                            table.scrollRectToVisible(table.getCellRect(i, 0, true));
                            break;
                        }
                    }
                    JOptionPane.showMessageDialog(this,
                            "Cập nhật chi tiết sản phẩm thành công.",
                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Không thể cập nhật. Hãy kiểm tra mã chi tiết, kích cỡ và màu sắc có bị trùng không.",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (o.equals(btnXoa)) {
            int row = table.getSelectedRow();
            if (row < 0) {
                return;
            }

            String maCTSP = model.getValueAt(row, 0).toString();
            int TH = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn xóa chi tiết sản phẩm với mã CTSP: " + maCTSP + "?",
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (TH == JOptionPane.YES_OPTION) {
                if (ctsp_dao.delete(maCTSP, txtMaSP.getText())) {
                    model.removeRow(row);
                    clearTextField();
                    JOptionPane.showMessageDialog(this,
                            "Xóa chi tiết sản phẩm thành công.",
                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Không thể xóa chi tiết sản phẩm."
                                    + "Hãy kiểm tra chi tiết sản phẩm có tồn tại trong các đơn hàng hay không.",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        else if (o.equals(cboLocTheo)) {
            txtThongBao.setText("");

            if (cboLocTheo.getSelectedIndex() == 1) {
                String kichCo = txtKichCo.getText().trim();

                if (kichCo.isEmpty()) {
                    txtThongBao.setText("Vui lòng nhập kích cỡ cần lọc.");
                    txtKichCo.requestFocusInWindow();
                    return;
                }

                DocDuLieuDatabaseVaoTable();

                for (int i = 0; i < model.getRowCount(); i++) {
                    String kichCoTrongBang = model.getValueAt(i, 2).toString();

                    if (!kichCoTrongBang.equalsIgnoreCase(kichCo)) {
                        model.removeRow(i);
                        i--;
                    }
                }

            } else if (cboLocTheo.getSelectedIndex() == 2) {
                String mauSac = txtMauSac.getText().trim();

                if (mauSac.isEmpty()) {
                    txtThongBao.setText("Vui lòng nhập màu sắc cần lọc.");
                    txtMauSac.requestFocusInWindow();
                    return;
                }

                DocDuLieuDatabaseVaoTable();

                for (int i = 0; i < model.getRowCount(); i++) {
                    String mauSacTrongBang = model.getValueAt(i, 3).toString();

                    if (!mauSacTrongBang.equalsIgnoreCase(mauSac)) {
                        model.removeRow(i);
                        i--;
                    }
                }

            } else if (cboLocTheo.getSelectedIndex() == 3) {
                String trangThai = (String) cboTrangThai.getSelectedItem();

                if (trangThai == null) {
                    txtThongBao.setText("Vui lòng chọn trạng thái cần lọc.");
                    return;
                }

                DocDuLieuDatabaseVaoTable();

                for (int i = 0; i < model.getRowCount(); i++) {
                    String trangThaiTrongBang = model.getValueAt(i, 5).toString();

                    if (!trangThaiTrongBang.equalsIgnoreCase(trangThai)) {
                        model.removeRow(i);
                        i--;
                    }
                }

            } else {
                DocDuLieuDatabaseVaoTable();
            }
        }

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

    private void fillForm(int row) {
        if (row < 0) {
            txtMaCTSP.setText("");
            txtTonKho.setText("");
            txtMaCTSP.setEditable(true);
            return;
        }
        txtMaCTSP.setText(model.getValueAt(row, 0).toString());
        txtTonKho.setText(model.getValueAt(row, 4).toString());
        txtKichCo.setText(model.getValueAt(row, 2).toString());
        txtMauSac.setText(model.getValueAt(row, 3).toString());
        cboTrangThai.setSelectedItem(model.getValueAt(row, 5));
        txtMaCTSP.setEditable(false);
    }

    public void clearTextField() {
        txtThongBao.setText("");
        txtTimMaCTSP.setText("");
        cboLocTheo.removeActionListener(this);
        cboLocTheo.setSelectedIndex(0);
        cboLocTheo.addActionListener(this);
        table.clearSelection();
        txtMaCTSP.setEditable(true);
        txtKichCo.setText("");
        txtMauSac.setText("");
        cboTrangThai.setSelectedIndex(0);
        txtMaCTSP.setText("");
        txtTonKho.setText("");
    }

    private ChiTietSanPham reverChiTietSanPham() {
        String maCTSP = txtMaCTSP.getText().trim();
        SanPham sp = new SanPham(txtMaSP.getText().trim());
        String kichCo = txtKichCo.getText().trim();
        String mauSac = txtMauSac.getText().trim();
        String tonKho = txtTonKho.getText().trim();
        int soLuongTon = tonKho.isEmpty() ? 0 : Integer.parseInt(tonKho);
        boolean hoatDong = cboTrangThai.getSelectedIndex() == 0;
        return new ChiTietSanPham(maCTSP, sp, kichCo, mauSac, soLuongTon, hoatDong);
    }

    public boolean validateInput() {
        txtThongBao.setText("");

        String maCTSP = txtMaCTSP.getText().trim();
        String kichCo = txtKichCo.getText().trim();
        String mauSac = txtMauSac.getText().trim();

        if (maCTSP.isEmpty()) {
            txtThongBao.setText("Mã chi tiết sản phẩm không được để trống.");
            txtMaCTSP.requestFocusInWindow();
            return false;
        }

        if (maCTSP.length() > 20) {
            txtThongBao.setText("Mã chi tiết sản phẩm không được quá 20 ký tự.");
            txtMaCTSP.requestFocusInWindow();
            return false;
        }

        if (kichCo.isEmpty()) {
            txtThongBao.setText("Kích cỡ không được để trống.");
            txtKichCo.requestFocusInWindow();
            return false;
        }
        if (kichCo.length() > 10) {
            txtThongBao.setText("Kích cỡ không được quá 10 ký tự.");
            txtKichCo.requestFocusInWindow();
            return false;
        }

        if (mauSac.isEmpty()) {
            txtThongBao.setText("Màu sắc không được để trống.");
            txtMauSac.requestFocusInWindow();
            return false;
        }
        if (mauSac.length() > 50) {
            txtThongBao.setText("Màu sắc không được quá 50 ký tự.");
            txtMauSac.requestFocusInWindow();
            return false;
        }

        if (cboTrangThai.getSelectedIndex() < 0) {
            txtThongBao.setText("Vui lòng chọn trạng thái.");
            cboTrangThai.requestFocusInWindow();
            return false;
        }

        return true;
    }

}
