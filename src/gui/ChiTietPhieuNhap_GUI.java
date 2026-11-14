package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
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

import connectDB.ConnectDB;
import dao.ChiTietPhieuNhap_DAO;
import dao.ChiTietSanPham_DAO;
import dao.PhieuNhap_DAO;
import dao.SanPham_DAO;
import entity.ChiTietPhieuNhap;
import entity.ChiTietSanPham;
import entity.PhieuNhap;
import entity.SanPham;

public class ChiTietPhieuNhap_GUI extends JFrame implements ActionListener, MouseListener {

    private static final long serialVersionUID = 1L;

    // Thông tin phiếu nhập
    private JTextField txtMaPN;
    private JTextField txtNhaCungCap;
    private JTextField txtNhanVien;
    private JTextField txtNgayNhap;

    // Thông tin sản phẩm
    private JComboBox<String> cboSanPham;
    private JComboBox<String> cboChiTietSanPham;
    private JTextField txtSoLuong;
    private JTextField txtDonGiaNhap;
    private JTextField txtThanhTien;

    // Nút chức năng
    private JButton btnThem;
    private JButton btnXoaRong;
    private JButton btnXoa;
    private JButton btnSua;

    // Bảng và tổng tiền
    private JTextField txtTongTien;
    private JTable table;
    private DefaultTableModel tableModel;

    // DAO và dữ liệu
    private ChiTietPhieuNhap_DAO ctpn_dao;
    private SanPham_DAO sp_dao;
    private ChiTietSanPham_DAO ctsp_dao;
    private PhieuNhap_DAO pn_dao;

    private PhieuNhap phieuNhapHienTai;
    private PhieuNhap_GUI parentGUI;
    private ArrayList<SanPham> dsSanPham = new ArrayList<>();
    private ArrayList<ChiTietSanPham> dsCTSPTheoSP = new ArrayList<>();
    private ArrayList<ChiTietPhieuNhap> dsChiTiet = new ArrayList<>();

    public ChiTietPhieuNhap_GUI() {
        ctpn_dao = new ChiTietPhieuNhap_DAO();
        sp_dao = new SanPham_DAO();
        ctsp_dao = new ChiTietSanPham_DAO();
        pn_dao = new PhieuNhap_DAO();

        setTitle("Quản lý chi tiết phiếu nhập");
        setSize(1000, 650);
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
        if (phieuNhap != null) {
            txtMaPN.setText(phieuNhap.getMaPN());
            txtNhaCungCap.setText(phieuNhap.getNhaCungCap() != null ? phieuNhap.getNhaCungCap().getMaNCC() : "");
            txtNhanVien.setText(phieuNhap.getNhanVien() != null ? phieuNhap.getNhanVien().getMaNhanVien() : "");
            if (phieuNhap.getNgayNhap() != null) {
                txtNgayNhap.setText(phieuNhap.getNgayNhap().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
            setTitle("Quản lý chi tiết phiếu nhập - " + phieuNhap.getMaPN());
            docDuLieuVaoBang(phieuNhap.getMaPN());
            // Làm mới phần nhập sản phẩm để sẵn sàng thêm
            lamRong();
        }
    }

    private void buildUI() {

        // ================= PHẦN NORTH =================
        JPanel pnlNorth;
        add(pnlNorth = new JPanel(), BorderLayout.NORTH);

        pnlNorth.setPreferredSize(new Dimension(0, 125));
        pnlNorth.setBorder(
                BorderFactory.createTitledBorder("Thông tin phiếu nhập"));
        pnlNorth.setLayout(null);

        JLabel lblMaPN, lblNhaCungCap, lblNhanVien, lblNgayNhap;

        pnlNorth.add(lblMaPN = new JLabel("Mã phiếu nhập:"));
        pnlNorth.add(lblNhaCungCap = new JLabel("Nhà cung cấp:"));
        pnlNorth.add(lblNhanVien = new JLabel("Nhân viên lập:"));
        pnlNorth.add(lblNgayNhap = new JLabel("Ngày nhập:"));

        pnlNorth.add(txtMaPN = new JTextField());
        pnlNorth.add(txtNhaCungCap = new JTextField());
        pnlNorth.add(txtNhanVien = new JTextField());
        pnlNorth.add(txtNgayNhap = new JTextField());

        txtMaPN.setEditable(false);
        txtNhaCungCap.setEditable(false);
        txtNhanVien.setEditable(false);
        txtNgayNhap.setEditable(false);

        int w1 = 120, h = 25;

        lblMaPN.setBounds(20, 25, w1, h);
        txtMaPN.setBounds(145, 25, 230, h);

        lblNhaCungCap.setBounds(480, 25, w1, h);
        txtNhaCungCap.setBounds(600, 25, 280, h);

        lblNhanVien.setBounds(20, 65, w1, h);
        txtNhanVien.setBounds(145, 65, 230, h);

        lblNgayNhap.setBounds(480, 65, w1, h);
        txtNgayNhap.setBounds(600, 65, 200, h);

        // ================= PHẦN CENTER =================
        JPanel pnlCenter;
        add(pnlCenter = new JPanel(new BorderLayout()), BorderLayout.CENTER);

        // Panel thông tin sản phẩm
        JPanel pnlThongTin;
        pnlCenter.add(pnlThongTin = new JPanel(new GridLayout(3, 4, 10, 12)),
                BorderLayout.CENTER);

        pnlThongTin.setBorder(
                BorderFactory.createTitledBorder("Thông tin sản phẩm nhập"));

        JLabel lblSanPham, lblChiTietSP;
        JLabel lblSoLuong, lblDonGiaNhap;
        JLabel lblThanhTien;

        // Hàng 1
        pnlThongTin.add(lblSanPham = new JLabel("Sản phẩm:"));
        pnlThongTin.add(cboSanPham = new JComboBox<String>());
        pnlThongTin.add(lblChiTietSP = new JLabel("Chi tiết sản phẩm:"));
        pnlThongTin.add(cboChiTietSanPham = new JComboBox<String>());

        // Hàng 2
        pnlThongTin.add(lblSoLuong = new JLabel("Số lượng:"));
        pnlThongTin.add(txtSoLuong = new JTextField("1"));
        pnlThongTin.add(lblDonGiaNhap = new JLabel("Đơn giá nhập:"));
        pnlThongTin.add(txtDonGiaNhap = new JTextField());
        txtDonGiaNhap.setEditable(true);

        // Hàng 3
        pnlThongTin.add(lblThanhTien = new JLabel("Thành tiền:"));
        pnlThongTin.add(txtThanhTien = new JTextField());
        txtThanhTien.setEditable(false);

        pnlThongTin.add(new JLabel(""));
        pnlThongTin.add(new JLabel(""));

        cboSanPham.setPreferredSize(new Dimension(200, 25));
        cboChiTietSanPham.setPreferredSize(new Dimension(200, 25));

        // Panel nút chức năng
        JPanel pnlButton = new JPanel();

        pnlButton.add(btnThem = new JButton("Thêm"));
        pnlButton.add(btnXoaRong = new JButton("Làm rỗng"));
        pnlButton.add(btnSua = new JButton("Sửa"));
        pnlButton.add(btnXoa = new JButton("Xóa"));

        pnlCenter.add(pnlButton, BorderLayout.SOUTH);

        // ================= PHẦN SOUTH =================
        JPanel pnlSouth;
        add(pnlSouth = new JPanel(new BorderLayout()), BorderLayout.SOUTH);

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

        JScrollPane scroll;
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pnlSouth.add(scroll = new JScrollPane(
                table,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED),
                BorderLayout.CENTER);

        scroll.setBorder(
                BorderFactory.createTitledBorder("Danh sách chi tiết phiếu nhập"));

        table.setRowHeight(22);

        // Tổng tiền
        JPanel pnlTongTien = new JPanel();

        pnlTongTien.add(new JLabel("Tổng tiền phiếu nhập:"));
        pnlTongTien.add(txtTongTien = new JTextField(15));
        txtTongTien.setEditable(false);

        pnlSouth.add(pnlTongTien, BorderLayout.SOUTH);
        pnlSouth.setPreferredSize(new Dimension(0, 300));

        // Đăng ký sự kiện
        btnThem.addActionListener(this);
        btnXoaRong.addActionListener(this);
        btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        table.addMouseListener(this);

        // Tự động tính thành tiền khi thay đổi số lượng hoặc đơn giá
        DocumentListener docListener = new DocumentListener() {
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

        txtSoLuong.getDocument().addDocumentListener(docListener);
        txtDonGiaNhap.getDocument().addDocumentListener(docListener);
    }

    private void khoiTaoDuLieu() {
        dsSanPham = sp_dao.getallSanPham();
        cboSanPham.removeAllItems();
        for (SanPham sp : dsSanPham) {
            cboSanPham.addItem(sp.getMaSP() + " - " + sp.getTenSP());
        }

        cboSanPham.addActionListener(e -> onSanPhamChanged());
        cboChiTietSanPham.addActionListener(e -> onChiTietSanPhamChanged());

        if (cboSanPham.getItemCount() > 0) {
            cboSanPham.setSelectedIndex(0);
        }
    }

    private void onSanPhamChanged() {
        int idx = cboSanPham.getSelectedIndex();
        if (idx < 0 || idx >= dsSanPham.size()) {
            return;
        }

        SanPham sp = dsSanPham.get(idx);
        dsCTSPTheoSP = ctsp_dao.getChiTietSanPhamTheoMaSP(sp.getMaSP());
        cboChiTietSanPham.removeAllItems();

        for (ChiTietSanPham ct : dsCTSPTheoSP) {
            cboChiTietSanPham.addItem(ct.getMaCTSP() + " (Size: " + ct.getKichCo()
                    + ", Màu: " + ct.getMauSac() + ", Tồn: " + ct.getSoLuongTon() + ")");
        }

        // Gợi ý đơn giá nhập mặc định (70% giá bán nếu có)
        if (sp.getGiaBan() != null) {
            BigDecimal donGiaGoiY = sp.getGiaBan().multiply(new BigDecimal("0.70")).setScale(0, RoundingMode.HALF_UP);
            txtDonGiaNhap.setText(donGiaGoiY.toPlainString());
        } else {
            txtDonGiaNhap.setText("0");
        }

        tinhThanhTien();
    }

    private void onChiTietSanPhamChanged() {
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
                    txtThanhTien.setText(DinhDangTien.hienThi(tt));
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
        BigDecimal tong = BigDecimal.ZERO;

        for (ChiTietPhieuNhap ct : dsChiTiet) {
            ChiTietSanPham ctsp = ct.getChiTietSanPham();
            String tenSP = (ctsp != null && ctsp.getSanPham() != null) ? ctsp.getSanPham().getTenSP() : "";
            String kichCo = ctsp != null ? ctsp.getKichCo() : "";
            String mauSac = ctsp != null ? ctsp.getMauSac() : "";
            BigDecimal thanhTien = ct.tinhThanhTien();
            tong = tong.add(thanhTien);

            tableModel.addRow(new Object[]{
                ctsp != null ? ctsp.getMaCTSP() : "",
                tenSP,
                kichCo,
                mauSac,
                ct.getSoLuong(),
                DinhDangTien.hienThi(ct.getDonGiaNhap()),
                DinhDangTien.hienThi(thanhTien)
            });
        }

        txtTongTien.setText(DinhDangTien.hienThi(tong));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o == btnThem) {
            themChiTiet();
        } else if (o == btnSua) {
            suaChiTiet();
        } else if (o == btnXoa) {
            xoaChiTiet();
        } else if (o == btnXoaRong) {
            lamRong();
        }
    }

    private void themChiTiet() {
        String maPN = txtMaPN.getText().trim();
        if (maPN.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Chưa có thông tin mã phiếu nhập!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int ctspIdx = cboChiTietSanPham.getSelectedIndex();
        if (ctspIdx < 0 || ctspIdx >= dsCTSPTheoSP.size()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chi tiết sản phẩm cần nhập!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ChiTietSanPham ctsp = dsCTSPTheoSP.get(ctspIdx);

        int soLuong;
        try {
            soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            if (soLuong <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng nhập phải lớn hơn 0!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                txtSoLuong.requestFocus();
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên dương hợp lệ!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            txtSoLuong.requestFocus();
            return;
        }

        BigDecimal donGiaNhap;
        try {
            donGiaNhap = new BigDecimal(txtDonGiaNhap.getText().trim());
            if (donGiaNhap.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(this, "Đơn giá nhập phải lớn hơn hoặc bằng 0!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                txtDonGiaNhap.requestFocus();
                return;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Đơn giá nhập không hợp lệ!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            txtDonGiaNhap.requestFocus();
            return;
        }

        if (phieuNhapHienTai == null) {
            phieuNhapHienTai = new PhieuNhap(maPN);
        }

        // Kiểm tra xem sản phẩm này đã có trong phiếu nhập chưa
        if (ctpn_dao.kiemTraTonTai(maPN, ctsp.getMaCTSP())) {
            int hoi = JOptionPane.showConfirmDialog(this,
                    "Chi tiết sản phẩm " + ctsp.getMaCTSP() + " đã có trong phiếu nhập.\nBạn có muốn cộng dồn thêm số lượng (" + soLuong + ") không?",
                    "Sản phẩm đã tồn tại",
                    JOptionPane.YES_NO_OPTION);

            if (hoi == JOptionPane.YES_OPTION) {
                // Tìm số lượng cũ
                int soLuongCu = 0;
                for (ChiTietPhieuNhap item : dsChiTiet) {
                    if (item.getChiTietSanPham().getMaCTSP().equals(ctsp.getMaCTSP())) {
                        soLuongCu = item.getSoLuong();
                        break;
                    }
                }
                int soLuongMoi = soLuongCu + soLuong;
                ChiTietPhieuNhap ct = new ChiTietPhieuNhap(phieuNhapHienTai, ctsp, soLuongMoi, donGiaNhap);
                if (ctpn_dao.capNhatChiTietPhieuNhap(ct, soLuongCu)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật cộng dồn số lượng thành công!");
                    docDuLieuVaoBang(maPN);
                    onSanPhamChanged(); // làm mới tồn kho trong combobox
                    if (parentGUI != null) parentGUI.Docdldatatable();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
            return;
        }

        ChiTietPhieuNhap ctMoi = new ChiTietPhieuNhap(phieuNhapHienTai, ctsp, soLuong, donGiaNhap);
        if (ctpn_dao.themChiTietPhieuNhap(ctMoi)) {
            JOptionPane.showMessageDialog(this, "Thêm sản phẩm vào phiếu nhập thành công!");
            docDuLieuVaoBang(maPN);
            onSanPhamChanged(); // làm mới tồn kho trong combobox
            if (parentGUI != null) parentGUI.Docdldatatable();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm chi tiết phiếu nhập thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void suaChiTiet() {
        int r = table.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng chi tiết phiếu nhập trong bảng cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maPN = txtMaPN.getText().trim();
        String maCTSP = tableModel.getValueAt(r, 0).toString();

        int soLuong;
        try {
            soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            if (soLuong <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng nhập phải lớn hơn 0!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                txtSoLuong.requestFocus();
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên dương hợp lệ!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            txtSoLuong.requestFocus();
            return;
        }

        BigDecimal donGiaNhap;
        try {
            donGiaNhap = new BigDecimal(txtDonGiaNhap.getText().trim());
            if (donGiaNhap.compareTo(BigDecimal.ZERO) < 0) {
                JOptionPane.showMessageDialog(this, "Đơn giá nhập phải lớn hơn hoặc bằng 0!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
                txtDonGiaNhap.requestFocus();
                return;
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Đơn giá nhập không hợp lệ!", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            txtDonGiaNhap.requestFocus();
            return;
        }

        int soLuongCu = Integer.parseInt(tableModel.getValueAt(r, 4).toString());
        ChiTietPhieuNhap ct = new ChiTietPhieuNhap(new PhieuNhap(maPN), new ChiTietSanPham(maCTSP), soLuong, donGiaNhap);

        if (ctpn_dao.capNhatChiTietPhieuNhap(ct, soLuongCu)) {
            JOptionPane.showMessageDialog(this, "Cập nhật chi tiết phiếu nhập thành công!");
            docDuLieuVaoBang(maPN);
            onSanPhamChanged();
            if (parentGUI != null) parentGUI.Docdldatatable();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaChiTiet() {
        int r = table.getSelectedRow();
        if (r == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng chi tiết cần xóa trong bảng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maPN = txtMaPN.getText().trim();
        String maCTSP = tableModel.getValueAt(r, 0).toString();

        int hoi = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn xóa chi tiết sản phẩm " + maCTSP + " khỏi phiếu nhập?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION);

        if (hoi == JOptionPane.YES_OPTION) {
            if (ctpn_dao.xoaChiTietPhieuNhap(maPN, maCTSP)) {
                JOptionPane.showMessageDialog(this, "Xóa chi tiết phiếu nhập thành công!");
                docDuLieuVaoBang(maPN);
                onSanPhamChanged();
                lamRong();
                if (parentGUI != null) parentGUI.Docdldatatable();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa chi tiết thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
        if (r != -1 && r < dsChiTiet.size()) {
            ChiTietPhieuNhap ct = dsChiTiet.get(r);
            ChiTietSanPham ctsp = ct.getChiTietSanPham();
            if (ctsp != null) {
                String maSP = ctsp.getSanPham() != null ? ctsp.getSanPham().getMaSP() : "";
                for (int i = 0; i < dsSanPham.size(); i++) {
                    if (dsSanPham.get(i).getMaSP().equals(maSP)) {
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
            txtDonGiaNhap.setText(ct.getDonGiaNhap() != null ? ct.getDonGiaNhap().toPlainString() : "");
            txtThanhTien.setText(DinhDangTien.hienThi(ct.tinhThanhTien()));
        }
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

    public static void main(String[] args) {
        new ChiTietPhieuNhap_GUI().setVisible(true);
    }
}
