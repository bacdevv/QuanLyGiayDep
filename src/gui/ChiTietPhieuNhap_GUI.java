package gui;

import connectDB.ConnectDB;
import dao.ChiTietPhieuNhap_DAO;
import dao.ChiTietSanPham_DAO;
import dao.PhieuNhap_DAO;
import dao.SanPham_DAO;
import entity.ChiTietPhieuNhap;
import entity.ChiTietSanPham;
import entity.PhieuNhap;
import entity.SanPham;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ChiTietPhieuNhap_GUI extends JFrame implements MouseListener {

    private static final long serialVersionUID = 1L;

    private JTextField txtMaPN;
    private JTextField txtNhaCungCap;
    private JTextField txtTenNhaCungCap;
    private JTextField txtNhanVien;
    private JTextField txtNgayNhap;

    private JTextField txtTimMaSP;
    private JTextField txtTimMaCTSP;
    private JTextField txtTimMauSac;
    private JTextField txtTimKichCo;

    private JComboBox<String> cboSanPham;
    private JComboBox<String> cboChiTietSanPham;

    private JTextField txtSoLuong;
    private JTextField txtDonGiaNhap;
    private JTextField txtThanhTien;

    private JButton btnThem;
    private JButton btnXoaRong;
    private JButton btnXoa;
    private JButton btnSua;
    private JButton btnXuatDanhSach;

    private JTextField txtTongTien;

    private JTable table;
    private DefaultTableModel tableModel;

    private ChiTietPhieuNhap_DAO ctpn_dao;
    private SanPham_DAO sp_dao;
    private ChiTietSanPham_DAO ctsp_dao;
    private PhieuNhap_DAO pn_dao;

    private PhieuNhap phieuNhapHienTai;
    private PhieuNhap_GUI parentGUI;

    private ArrayList<SanPham> dsSanPham = new ArrayList<>();
    private ArrayList<SanPham> dsSanPhamTimKiem = new ArrayList<>();
    private ArrayList<ChiTietSanPham> dsCTSPTheoSP = new ArrayList<>();
    private ArrayList<ChiTietPhieuNhap> dsChiTiet = new ArrayList<>();

    public ChiTietPhieuNhap_GUI() {

        ctpn_dao = new ChiTietPhieuNhap_DAO();
        sp_dao = new SanPham_DAO();
        ctsp_dao = new ChiTietSanPham_DAO();
        pn_dao = new PhieuNhap_DAO();

        setTitle("Quản lý chi tiết phiếu nhập");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        try {
            ConnectDB.getInstance().connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        buildUI();
        khoiTaoDuLieu();
    }

    public ChiTietPhieuNhap_GUI(PhieuNhap phieuNhap) {
        this();

        setPhieuNhap(phieuNhap);
    }

    public ChiTietPhieuNhap_GUI(PhieuNhap phieuNhap, PhieuNhap_GUI parentGUI) {
        this();
        this.parentGUI = parentGUI;
        setPhieuNhap(phieuNhap);
    }

    public void setParentGUI(PhieuNhap_GUI parentGUI) {
        this.parentGUI = parentGUI;
    }

    public void setPhieuNhap(PhieuNhap phieuNhap) {

        this.phieuNhapHienTai = phieuNhap;
        System.out.println("Mã PN: " + phieuNhap.getMaPN());

        System.out.println("NCC: " + phieuNhap.getNhaCungCap());
        if (phieuNhap != null) {

            txtMaPN.setText(phieuNhap.getMaPN());

            // Hiển thị mã nhà cung cấp
            txtNhaCungCap.setText(
                    phieuNhap.getNhaCungCap() != null
                    ? phieuNhap.getNhaCungCap().getMaNCC() : ""
            );

            // Hiển thị tên nhà cung cấp
            txtTenNhaCungCap.setText(
                    phieuNhap.getNhaCungCap() != null
                    ? phieuNhap.getNhaCungCap().getTenNCC() : ""
            );

            txtNhanVien.setText(
                    phieuNhap.getNhanVien() != null
                    ? phieuNhap.getNhanVien().getMaNhanVien() : ""
            );

            if (phieuNhap.getNgayNhap() != null) {
                txtNgayNhap.setText(
                        phieuNhap.getNgayNhap().format(
                                DateTimeFormatter.ofPattern("dd/MM/yyyy")
                        )
                );
            }

            setTitle("Quản lý chi tiết phiếu nhập - " + phieuNhap.getMaPN());

            docDuLieuVaoBang(phieuNhap.getMaPN());

            lamRong();
        }
    }

    private void buildUI() {

        JPanel pnlNorth = new JPanel();
        add(pnlNorth, BorderLayout.NORTH);
        pnlNorth.setPreferredSize(new Dimension(0, 125));
        pnlNorth.setBorder(BorderFactory.createTitledBorder("Thông tin phiếu nhập"));
        pnlNorth.setLayout(null);

        JLabel lblMaPN = new JLabel("Mã phiếu nhập:");
        JLabel lblNhaCungCap = new JLabel("Mã NCC:");
        JLabel lblTenNCC = new JLabel("Tên nhà cung cấp:");
        JLabel lblNhanVien = new JLabel("Nhân viên lập:");
        JLabel lblNgayNhap = new JLabel("Ngày nhập:");

        txtMaPN = new JTextField();
        txtNhaCungCap = new JTextField();
        txtTenNhaCungCap = new JTextField();
        txtNhanVien = new JTextField();
        txtNgayNhap = new JTextField();

        pnlNorth.add(lblMaPN);
        pnlNorth.add(lblNhaCungCap);
        pnlNorth.add(lblTenNCC);
        pnlNorth.add(lblNhanVien);
        pnlNorth.add(lblNgayNhap);

        pnlNorth.add(txtMaPN);
        pnlNorth.add(txtNhaCungCap);
        pnlNorth.add(txtTenNhaCungCap);
        pnlNorth.add(txtNhanVien);
        pnlNorth.add(txtNgayNhap);

        // Không cho phép chỉnh sửa thông tin phiếu nhập
        txtMaPN.setEditable(false);
        txtNhaCungCap.setEditable(false);
        txtTenNhaCungCap.setEditable(false);
        txtNhanVien.setEditable(false);
        txtNgayNhap.setEditable(false);

        int h = 25;

        // Dòng 1
        lblMaPN.setBounds(20, 25, 105, h);
        txtMaPN.setBounds(130, 25, 155, h);

        lblNhaCungCap.setBounds(305, 25, 65, h);
        txtNhaCungCap.setBounds(375, 25, 120, h);

        lblTenNCC.setBounds(510, 25, 125, h);
        txtTenNhaCungCap.setBounds(640, 25, 315, h);

        // Dòng 2
        lblNhanVien.setBounds(20, 65, 105, h);
        txtNhanVien.setBounds(130, 65, 200, h);

        lblNgayNhap.setBounds(480, 65, 100, h);
        txtNgayNhap.setBounds(585, 65, 200, h);

        JPanel pnlCenter = new JPanel(new BorderLayout());
        add(pnlCenter, BorderLayout.CENTER);

        JPanel pnlTimKiem = new JPanel(new GridLayout(2, 4, 10, 5));
        pnlTimKiem.setBorder(BorderFactory.createTitledBorder("Tìm kiếm sản phẩm"));

        pnlTimKiem.add(new JLabel("Mã sản phẩm:"));
        pnlTimKiem.add(new JLabel("Mã chi tiết sản phẩm:"));
        pnlTimKiem.add(new JLabel("Màu sắc:"));
        pnlTimKiem.add(new JLabel("Kích cỡ:"));

        txtTimMaSP = new JTextField();
        txtTimMaCTSP = new JTextField();
        txtTimMauSac = new JTextField();
        txtTimKichCo = new JTextField();

        pnlTimKiem.add(txtTimMaSP);
        pnlTimKiem.add(txtTimMaCTSP);
        pnlTimKiem.add(txtTimMauSac);
        pnlTimKiem.add(txtTimKichCo);

        pnlCenter.add(pnlTimKiem, BorderLayout.NORTH);

        JPanel pnlThongTin = new JPanel(new GridLayout(3, 4, 10, 12));
        pnlThongTin.setBorder(BorderFactory.createTitledBorder("Thông tin sản phẩm nhập"));

        pnlThongTin.add(new JLabel("Sản phẩm:"));

        cboSanPham = new JComboBox<>();
        pnlThongTin.add(cboSanPham);

        pnlThongTin.add(new JLabel("Chi tiết sản phẩm:"));

        cboChiTietSanPham = new JComboBox<>();
        pnlThongTin.add(cboChiTietSanPham);

        pnlThongTin.add(new JLabel("Số lượng:"));

        txtSoLuong = new JTextField("1");
        pnlThongTin.add(txtSoLuong);

        pnlThongTin.add(new JLabel("Đơn giá nhập:"));

        txtDonGiaNhap = new JTextField();
        txtDonGiaNhap.setEditable(false);
        pnlThongTin.add(txtDonGiaNhap);

        pnlThongTin.add(new JLabel("Thành tiền:"));

        txtThanhTien = new JTextField();
        txtThanhTien.setEditable(false);
        pnlThongTin.add(txtThanhTien);

        pnlThongTin.add(new JLabel(""));
        pnlThongTin.add(new JLabel(""));

        pnlCenter.add(pnlThongTin, BorderLayout.CENTER);

        JPanel pnlButton = new JPanel();

        btnThem = new JButton("Thêm");
        btnXoaRong = new JButton("Làm rỗng");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnXuatDanhSach = new JButton("Xuất danh sách");

        pnlButton.add(btnThem);
        pnlButton.add(btnXoaRong);
        pnlButton.add(btnSua);
        pnlButton.add(btnXoa);
        pnlButton.add(btnXuatDanhSach);

        pnlCenter.add(pnlButton, BorderLayout.SOUTH);

        JPanel pnlSouth = new JPanel(new BorderLayout());
        add(pnlSouth, BorderLayout.SOUTH);

        String[] headers = {
            "Mã CTSP",
            "Tên sản phẩm",
            "Kích cỡ",
            "Màu sắc",
            "Số lượng",
            "Đơn giá nhập",
            "Thành tiền"
        };

        tableModel = new DefaultTableModel(headers, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(22);

        JScrollPane scroll = new JScrollPane(
                table,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách chi tiết phiếu nhập"));
        pnlSouth.add(scroll, BorderLayout.CENTER);

        JPanel pnlTongTien = new JPanel();
        pnlTongTien.add(new JLabel("Tổng tiền phiếu nhập:"));

        txtTongTien = new JTextField(15);
        txtTongTien.setEditable(false);
        pnlTongTien.add(txtTongTien);

        pnlSouth.add(pnlTongTien, BorderLayout.SOUTH);
        pnlSouth.setPreferredSize(new Dimension(0, 280));

        btnThem.addActionListener(e -> themChiTiet());
        btnXoaRong.addActionListener(e -> lamRong());
        btnSua.addActionListener(e -> suaChiTiet());
        btnXoa.addActionListener(e -> xoaChiTiet());
        btnXuatDanhSach.addActionListener(e -> xuatrafileXML());

        table.addMouseListener(this);
        cboSanPham.addActionListener(e -> onSanPhamChanged());

        ganSuKienTimKiem(txtTimMaSP);
        ganSuKienTimKiem(txtTimMaCTSP);
        ganSuKienTimKiem(txtTimMauSac);
        ganSuKienTimKiem(txtTimKichCo);

        DocumentListener tinhTienListener = new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                tinhThanhTien();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                tinhThanhTien();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                tinhThanhTien();
            }
        };

        txtSoLuong.getDocument().addDocumentListener(tinhTienListener);
        txtDonGiaNhap.getDocument().addDocumentListener(tinhTienListener);
    }

    private void ganSuKienTimKiem(JTextField txt) {

        txt.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                timKiemSanPham();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                timKiemSanPham();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                timKiemSanPham();
            }
        });
    }

    private void khoiTaoDuLieu() {
        dsSanPham = sp_dao.getallSanPham();
        timKiemSanPham();
    }

    private void timKiemSanPham() {

        String maSPCanTim = txtTimMaSP.getText().trim().toLowerCase();
        String maCTSPCanTim = txtTimMaCTSP.getText().trim().toLowerCase();
        String mauSacCanTim = txtTimMauSac.getText().trim().toLowerCase();
        String kichCoCanTim = txtTimKichCo.getText().trim().toLowerCase();

        dsSanPhamTimKiem.clear();
        cboSanPham.removeAllItems();

        for (SanPham sp : dsSanPham) {

            String maSP = sp.getMaSP() != null
                    ? sp.getMaSP().toLowerCase() : "";

            if (!maSP.contains(maSPCanTim)) {
                continue;
            }

            ArrayList<ChiTietSanPham> dsCT
                    = ctsp_dao.getChiTietSanPhamTheoMaSP(sp.getMaSP());

            boolean coChiTietPhuHop = false;

            for (ChiTietSanPham ct : dsCT) {

                String maCTSP = ct.getMaCTSP() != null
                        ? ct.getMaCTSP().toLowerCase() : "";

                String mauSac = ct.getMauSac() != null
                        ? ct.getMauSac().toLowerCase() : "";

                String kichCo = String.valueOf(ct.getKichCo()).toLowerCase();

                boolean dungMaCTSP = maCTSP.contains(maCTSPCanTim);
                boolean dungMauSac = mauSac.contains(mauSacCanTim);
                boolean dungKichCo = kichCo.contains(kichCoCanTim);

                if (dungMaCTSP && dungMauSac && dungKichCo) {
                    coChiTietPhuHop = true;
                    break;
                }
            }

            if (coChiTietPhuHop) {
                dsSanPhamTimKiem.add(sp);
                cboSanPham.addItem(sp.getMaSP() + " - " + sp.getTenSP());
            }
        }

        if (cboSanPham.getItemCount() > 0) {
            cboSanPham.setSelectedIndex(0);
            onSanPhamChanged();
        } else {
            dsCTSPTheoSP.clear();
            cboChiTietSanPham.removeAllItems();
            txtDonGiaNhap.setText("0");
            txtThanhTien.setText("0");
        }
    }

    private void onSanPhamChanged() {

        int idx = cboSanPham.getSelectedIndex();

        if (idx < 0 || idx >= dsSanPhamTimKiem.size()) {
            return;
        }

        SanPham sp = dsSanPhamTimKiem.get(idx);

        ArrayList<ChiTietSanPham> dsCTSP
                = ctsp_dao.getChiTietSanPhamTheoMaSP(sp.getMaSP());

        dsCTSPTheoSP.clear();
        cboChiTietSanPham.removeAllItems();

        String maCTSPCanTim = txtTimMaCTSP.getText().trim().toLowerCase();
        String mauSacCanTim = txtTimMauSac.getText().trim().toLowerCase();
        String kichCoCanTim = txtTimKichCo.getText().trim().toLowerCase();

        for (ChiTietSanPham ct : dsCTSP) {

            String maCTSP = ct.getMaCTSP() != null
                    ? ct.getMaCTSP().toLowerCase() : "";

            String mauSac = ct.getMauSac() != null
                    ? ct.getMauSac().toLowerCase() : "";

            String kichCo = String.valueOf(ct.getKichCo()).toLowerCase();

            boolean dungMaCTSP = maCTSP.contains(maCTSPCanTim);
            boolean dungMauSac = mauSac.contains(mauSacCanTim);
            boolean dungKichCo = kichCo.contains(kichCoCanTim);

            if (dungMaCTSP && dungMauSac && dungKichCo) {

                dsCTSPTheoSP.add(ct);

                cboChiTietSanPham.addItem(
                        ct.getMaCTSP()
                        + " (Size: " + ct.getKichCo()
                        + ", Màu: " + ct.getMauSac()
                        + ", Tồn: " + ct.getSoLuongTon() + ")"
                );
            }
        }

        if (sp.getGiaBan() != null) {

            BigDecimal donGiaGoiY = sp.getGiaBan()
                    .multiply(new BigDecimal("0.70"))
                    .setScale(0, RoundingMode.HALF_UP);

            txtDonGiaNhap.setText(donGiaGoiY.toPlainString());

        } else {
            txtDonGiaNhap.setText("0");
        }

        tinhThanhTien();
    }

    private void tinhThanhTien() {

        try {
            String slStr = txtSoLuong.getText().trim();
            String dgStr = txtDonGiaNhap.getText().trim();

            if (!slStr.isEmpty() && !dgStr.isEmpty()) {

                int sl = Integer.parseInt(slStr);
                BigDecimal dg = new BigDecimal(dgStr);

                if (sl >= 0 && dg.compareTo(BigDecimal.ZERO) >= 0) {
                    BigDecimal tt = dg.multiply(BigDecimal.valueOf(sl));
                    txtThanhTien.setText(tt.toPlainString());
                    return;
                }
            }

        } catch (Exception ignored) {
        }

        txtThanhTien.setText("");
    }

    public void docDuLieuVaoBang(String maPN) {

        tableModel.setRowCount(0);
        dsChiTiet = ctpn_dao.layChiTietTheoMaPN(maPN);

        // Lấy mã và tên nhà cung cấp từ DAO
        if (!dsChiTiet.isEmpty()) {

            PhieuNhap pn = dsChiTiet.get(0).getPhieuNhap();

            if (pn != null && pn.getNhaCungCap() != null) {

                txtNhaCungCap.setText(
                        pn.getNhaCungCap().getMaNCC()
                );

                txtTenNhaCungCap.setText(
                        pn.getNhaCungCap().getTenNCC()
                );
            }
        }

        BigDecimal tong = BigDecimal.ZERO;

        for (ChiTietPhieuNhap ct : dsChiTiet) {

            ChiTietSanPham ctsp = ct.getChiTietSanPham();

            String tenSP = (ctsp != null && ctsp.getSanPham() != null)
                    ? ctsp.getSanPham().getTenSP() : "";

            String kichCo = ctsp != null
                    ? String.valueOf(ctsp.getKichCo()) : "";

            String mauSac = ctsp != null ? ctsp.getMauSac() : "";

            BigDecimal thanhTien = ct.tinhThanhTien();
            tong = tong.add(thanhTien);

            tableModel.addRow(new Object[]{
                ctsp != null ? ctsp.getMaCTSP() : "",
                tenSP,
                kichCo,
                mauSac,
                ct.getSoLuong(),
                dinhDangTien(ct.getDonGiaNhap()),
                dinhDangTien(thanhTien)
            });
        }

        txtTongTien.setText(dinhDangTien(tong));
    }

    private String dinhDangTien(BigDecimal tien) {

        if (tien == null) {
            tien = BigDecimal.ZERO;
        }

        DecimalFormat df = new DecimalFormat("#,##0");
        return df.format(tien) + " VNĐ";
    }

    private void themChiTiet() {

        String maPN = txtMaPN.getText().trim();

        if (maPN.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có thông tin mã phiếu nhập!");
            return;
        }

        int ctspIdx = cboChiTietSanPham.getSelectedIndex();

        if (ctspIdx < 0 || ctspIdx >= dsCTSPTheoSP.size()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chi tiết sản phẩm cần nhập!");
            return;
        }

        ChiTietSanPham ctsp = dsCTSPTheoSP.get(ctspIdx);

        int soLuong;

        try {
            soLuong = Integer.parseInt(txtSoLuong.getText().trim());

            if (soLuong <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng nhập phải lớn hơn 0!");
                return;
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên dương hợp lệ!");
            return;
        }

        BigDecimal donGiaNhap;

        try {
            donGiaNhap = new BigDecimal(txtDonGiaNhap.getText().trim());

            if (donGiaNhap.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(this, "Đơn giá nhập không hợp lệ!");
                return;
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Đơn giá nhập không hợp lệ!");
            return;
        }

        if (phieuNhapHienTai == null) {
            phieuNhapHienTai = new PhieuNhap(maPN);
        }

        if (ctpn_dao.kiemTraTonTai(maPN, ctsp.getMaCTSP())) {

            int soLuongCu = 0;

            for (ChiTietPhieuNhap item : dsChiTiet) {
                if (item.getChiTietSanPham().getMaCTSP().equals(ctsp.getMaCTSP())) {
                    soLuongCu = item.getSoLuong();
                    break;
                }
            }

            int soLuongMoi = soLuongCu + soLuong;

            ChiTietPhieuNhap ct = new ChiTietPhieuNhap(
                    phieuNhapHienTai,
                    ctsp,
                    soLuongMoi,
                    donGiaNhap
            );

            if (ctpn_dao.capNhatChiTietPhieuNhap(ct, soLuongCu)) {
                docDuLieuVaoBang(maPN);

                if (parentGUI != null) {
                    parentGUI.Docdldatatable();
                }

            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }

            return;
        }

        ChiTietPhieuNhap ctMoi = new ChiTietPhieuNhap(
                phieuNhapHienTai,
                ctsp,
                soLuong,
                donGiaNhap
        );

        if (ctpn_dao.themChiTietPhieuNhap(ctMoi)) {
            docDuLieuVaoBang(maPN);

            if (parentGUI != null) {
                parentGUI.Docdldatatable();
            }

        } else {
            JOptionPane.showMessageDialog(this, "Thêm chi tiết phiếu nhập thất bại!");
        }
    }

    private void suaChiTiet() {

        int r = table.getSelectedRow();

        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng chi tiết cần sửa!");
            return;
        }

        String maPN = txtMaPN.getText().trim();
        String maCTSP = tableModel.getValueAt(r, 0).toString();

        int soLuong;

        try {
            soLuong = Integer.parseInt(txtSoLuong.getText().trim());

            if (soLuong <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!");
                return;
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ!");
            return;
        }

        BigDecimal donGiaNhap;

        try {
            donGiaNhap = new BigDecimal(txtDonGiaNhap.getText().trim());

            if (donGiaNhap.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(this, "Đơn giá nhập không hợp lệ!");
                return;
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Đơn giá nhập không hợp lệ!");
            return;
        }

        int soLuongCu = Integer.parseInt(tableModel.getValueAt(r, 4).toString());

        ChiTietPhieuNhap ct = new ChiTietPhieuNhap(
                new PhieuNhap(maPN),
                new ChiTietSanPham(maCTSP),
                soLuong,
                donGiaNhap
        );

        if (ctpn_dao.capNhatChiTietPhieuNhap(ct, soLuongCu)) {

            JOptionPane.showMessageDialog(this, "Cập nhật chi tiết phiếu nhập thành công!");
            docDuLieuVaoBang(maPN);

            if (parentGUI != null) {
                parentGUI.Docdldatatable();
            }

        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
        }
    }

    private void xoaChiTiet() {

        int r = table.getSelectedRow();

        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng chi tiết cần xóa!");
            return;
        }

        String maPN = txtMaPN.getText().trim();
        String maCTSP = tableModel.getValueAt(r, 0).toString();

        int hoi = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn xóa chi tiết sản phẩm "
                + maCTSP + " khỏi phiếu nhập?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION
        );

        if (hoi == JOptionPane.YES_OPTION) {

            if (ctpn_dao.xoaChiTietPhieuNhap(maPN, maCTSP)) {

                JOptionPane.showMessageDialog(this, "Xóa chi tiết phiếu nhập thành công!");
                docDuLieuVaoBang(maPN);
                lamRong();

                if (parentGUI != null) {
                    parentGUI.Docdldatatable();
                }

            } else {
                JOptionPane.showMessageDialog(this, "Xóa chi tiết thất bại!");
            }
        }
    }

    private void lamRong() {

        txtSoLuong.setText("1");

        if (cboSanPham.getItemCount() > 0) {
            cboSanPham.setSelectedIndex(0);
        }

        table.clearSelection();
        tinhThanhTien();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        int r = table.getSelectedRow();

        if (r == -1) {
            return;
        }

        String maCTSP = tableModel.getValueAt(r, 0).toString();

        ChiTietPhieuNhap ct = null;

        for (ChiTietPhieuNhap item : dsChiTiet) {
            if (item.getChiTietSanPham() != null
                    && item.getChiTietSanPham().getMaCTSP().equals(maCTSP)) {
                ct = item;
                break;
            }
        }

        if (ct == null) {
            return;
        }

        ChiTietSanPham ctsp = ct.getChiTietSanPham();

        if (ctsp != null && ctsp.getSanPham() != null) {

            txtTimMaSP.setText("");
            txtTimMaCTSP.setText("");
            txtTimMauSac.setText("");
            txtTimKichCo.setText("");

            String maSP = ctsp.getSanPham().getMaSP();

            for (int i = 0; i < dsSanPhamTimKiem.size(); i++) {
                if (dsSanPhamTimKiem.get(i).getMaSP().equals(maSP)) {
                    cboSanPham.setSelectedIndex(i);
                    break;
                }
            }

            for (int j = 0; j < dsCTSPTheoSP.size(); j++) {
                if (dsCTSPTheoSP.get(j).getMaCTSP().equals(ctsp.getMaCTSP())) {
                    cboChiTietSanPham.setSelectedIndex(j);
                    break;
                }
            }
        }

        txtSoLuong.setText(String.valueOf(ct.getSoLuong()));

        txtDonGiaNhap.setText(
                ct.getDonGiaNhap() != null
                ? ct.getDonGiaNhap().toPlainString() : ""
        );

        txtThanhTien.setText(ct.tinhThanhTien().toPlainString());
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

    public void xuatrafileXML() {

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Xuất danh sách chi tiết phiếu nhập");

        chooser.setSelectedFile(new File("DanhSachChiTietPhieuNhap.xml"));

        int chon = chooser.showSaveDialog(this);

        if (chon != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();

        if (!file.getName().toLowerCase().endsWith(".xml")) {
            file = new File(file.getAbsolutePath() + ".xml");
        }

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.newDocument();

            Element root = doc.createElement("DanhSachChiTietPhieuNhap");
            doc.appendChild(root);

            for (int i = 0; i < table.getRowCount(); i++) {

                Element ct = doc.createElement("ChiTietPhieuNhap");
                root.appendChild(ct);

                Element maCTSP = doc.createElement("MaCTSP");
                maCTSP.setTextContent(table.getValueAt(i, 0).toString());
                ct.appendChild(maCTSP);

                Element tenSP = doc.createElement("TenSanPham");
                tenSP.setTextContent(table.getValueAt(i, 1).toString());
                ct.appendChild(tenSP);

                Element kichCo = doc.createElement("KichCo");
                kichCo.setTextContent(table.getValueAt(i, 2).toString());
                ct.appendChild(kichCo);

                Element mauSac = doc.createElement("MauSac");
                mauSac.setTextContent(table.getValueAt(i, 3).toString());
                ct.appendChild(mauSac);

                Element soLuong = doc.createElement("SoLuong");
                soLuong.setTextContent(table.getValueAt(i, 4).toString());
                ct.appendChild(soLuong);

                Element donGia = doc.createElement("DonGiaNhap");
                donGia.setTextContent(table.getValueAt(i, 5).toString());
                ct.appendChild(donGia);

                Element thanhTien = doc.createElement("ThanhTien");
                thanhTien.setTextContent(table.getValueAt(i, 6).toString());
                ct.appendChild(thanhTien);
            }

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);

            transformer.transform(source, result);

            JOptionPane.showMessageDialog(this, "Xuất file XML thành công!");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Xuất file XML thất bại: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new ChiTietPhieuNhap_GUI().setVisible(true);
    }
}
