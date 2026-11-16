package gui;

import connectDB.ConnectDB;
import dao.ChiTietPhieuNhap_DAO;
import dao.NhaCungCap_DAO;
import dao.PhieuNhap_DAO;
import entity.NhaCungCap;
import entity.NhanVien;
import entity.PhieuNhap;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
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
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PhieuNhap_GUI extends JFrame implements MouseListener, ActionListener {

    private static final long serialVersionUID = 1L;

    private JComboBox<String> cboNhaCungCap;

    private JTextField txtNhanVien;
    private JTextField txtTenNhanVien;
    private JTextField txtTenNCC;
    private JTextField txtNgayNhap;
    private JTextField txtMaPN;
    private JTextField txtTimMaNCC;

    // Hai JTextField mới
    private JTextField txtTimNCC;
    private JTextField txtTenNCC;

    private JButton btnThem;
    private JButton btnXoaRong;
    private JButton btnXoa;
    private JButton btnLuu;
    private JButton btnXemChiTiet;
    private JButton btnTimKiem;

    private JComboBox<String> cboTimKiem;
    private JTextField txtTimKiem;

    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    private PhieuNhap_DAO pn_dao;
    private NhaCungCap_DAO ncc_dao;
    private ChiTietPhieuNhap_DAO ctpn_dao;

    private ChiTietPhieuNhap_GUI chiTietGUI;

<<<<<<< HEAD
    private ArrayList<NhaCungCap> dsNCC = new ArrayList<>();
=======
    // Lưu toàn bộ danh sách nhà cung cấp
    private ArrayList<NhaCungCap> dsNCC;
>>>>>>> main

    public PhieuNhap_GUI() {

        setTitle("Quản lý phiếu nhập");
<<<<<<< HEAD
        setSize(1100, 700);
=======
        setSize(1000, 700);
>>>>>>> main
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        buildUI();
    }

    public PhieuNhap_GUI(String maNCC) {
        this();
        setNhaCungCap(maNCC);
    }

    public void setNhaCungCap(String maNCC) {

        if (maNCC == null) {
            return;
        }

        String trimmed = maNCC.trim();

<<<<<<< HEAD
=======
        txtTimNCC.setText("");

>>>>>>> main
        for (int i = 0; i < cboNhaCungCap.getItemCount(); i++) {

            if (cboNhaCungCap.getItemAt(i).equalsIgnoreCase(trimmed)) {
                cboNhaCungCap.setSelectedIndex(i);
                return;
            }
        }

        cboNhaCungCap.addItem(trimmed);
        cboNhaCungCap.setSelectedItem(trimmed);
    }

    private void buildUI() {

        pn_dao = new PhieuNhap_DAO();
        ncc_dao = new NhaCungCap_DAO();
        ctpn_dao = new ChiTietPhieuNhap_DAO();

        try {

            ConnectDB.getInstance().connect();
            System.out.println("Thành công");

        } catch (Exception e) {

            System.out.println("Thất bại");
            e.printStackTrace();
        }

<<<<<<< HEAD
        JPanel pnlNorth = new JPanel();

        add(pnlNorth, BorderLayout.NORTH);

        pnlNorth.setPreferredSize(new Dimension(0, 205));
=======
        // Lấy danh sách nhà cung cấp
        dsNCC = ncc_dao.getalltbNhacc();

        JPanel pnlNorth;
        add(pnlNorth = new JPanel(), BorderLayout.NORTH);

        pnlNorth.setPreferredSize(new Dimension(0, 195));
>>>>>>> main
        pnlNorth.setBorder(
                BorderFactory.createTitledBorder("Thông tin phiếu nhập")
        );
        pnlNorth.setLayout(null);

<<<<<<< HEAD
        JLabel lblNhaCungCap = new JLabel("Mã NCC:");
        JLabel lblTimMaNCC = new JLabel("Tìm mã NCC:");
        JLabel lblTenNCC = new JLabel("Tên nhà cung cấp:");
=======
        JLabel lblNhaCungCap, lblNhanVien, lblNgayNhap, lblMaPN;
        JLabel lblTimNCC, lblTenNCC;
>>>>>>> main

        JLabel lblNhanVien = new JLabel("Mã nhân viên:");
        JLabel lblTenNhanVien = new JLabel("Tên nhân viên:");

<<<<<<< HEAD
        JLabel lblNgayNhap = new JLabel("Ngày nhập:");
        JLabel lblMaPN = new JLabel("Mã phiếu nhập:");

        pnlNorth.add(lblNhaCungCap);
        pnlNorth.add(lblTimMaNCC);
        pnlNorth.add(lblTenNCC);
        pnlNorth.add(lblNhanVien);
        pnlNorth.add(lblTenNhanVien);
        pnlNorth.add(lblNgayNhap);
        pnlNorth.add(lblMaPN);

        cboNhaCungCap = new JComboBox<>();

        dsNCC = ncc_dao.getalltbNhacc();

        if (dsNCC != null) {
            for (NhaCungCap ncc : dsNCC) {
                cboNhaCungCap.addItem(ncc.getMaNCC());
            }
=======
        pnlNorth.add(lblTimNCC = new JLabel("Tìm nhà cung cấp:"));
        pnlNorth.add(lblTenNCC = new JLabel("Tên nhà cung cấp:"));

        pnlNorth.add(cboNhaCungCap = new JComboBox<String>());

        // Đổ dữ liệu ComboBox
        for (NhaCungCap ncc : dsNCC) {
            cboNhaCungCap.addItem(ncc.getMaNCC());
>>>>>>> main
        }

        pnlNorth.add(cboNhaCungCap);

<<<<<<< HEAD
        txtTimMaNCC = new JTextField();
        pnlNorth.add(txtTimMaNCC);

        txtTenNCC = new JTextField();
        txtTenNCC.setEditable(false);
        pnlNorth.add(txtTenNCC);

        txtNhanVien = new JTextField("NV0001");
        txtNhanVien.setEditable(false);
        pnlNorth.add(txtNhanVien);

        txtTenNhanVien = new JTextField();
        txtTenNhanVien.setEditable(false);

        try {

            txtTenNhanVien.setText(
                    pn_dao.layTenNhanVien("NV0001")
            );

        } catch (Exception e) {
            e.printStackTrace();
        }

        pnlNorth.add(txtTenNhanVien);

        txtNgayNhap = new JTextField();

        txtNgayNhap.setText(
                LocalDate.now().format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                )
        );

        pnlNorth.add(txtNgayNhap);

        txtMaPN = new JTextField();
        txtMaPN.setText(taoMaPN());
        txtMaPN.setEditable(false);

        pnlNorth.add(txtMaPN);
=======
        // Hai TextField mới
        pnlNorth.add(txtTimNCC = new JTextField());
        pnlNorth.add(txtTenNCC = new JTextField());

        txtNhanVien.setText("NV0001");
        txtNhanVien.setEditable(false);

        txtNgayNhap.setText(
                LocalDate.now().format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        txtMaPN.setText(taoMaPN());
        txtMaPN.setEditable(false);

        // Không cho chỉnh sửa tên NCC
        txtTenNCC.setEditable(false);
>>>>>>> main

        int labelW = 110;
        int fieldW = 250;
        int h = 28;

        int x1 = 30;
        int x2 = 150;
        int x3 = 520;
        int x4 = 640;

<<<<<<< HEAD
        int y1 = 15;
        int y2 = 55;
        int y3 = 95;
        int y4 = 135;
=======
        int y1 = 25;
        int y2 = 75;
        int y3 = 125;
>>>>>>> main

        lblNhaCungCap.setBounds(x1, y1, labelW, h);
        cboNhaCungCap.setBounds(x2, y1, fieldW, h);

        lblTimMaNCC.setBounds(x3, y1, labelW, h);
        txtTimMaNCC.setBounds(x4, y1, fieldW, h);

        lblTenNCC.setBounds(x1, y2, labelW, h);
        txtTenNCC.setBounds(x2, y2, fieldW, h);

        lblNhanVien.setBounds(x3, y2, labelW, h);
        txtNhanVien.setBounds(x4, y2, fieldW, h);

<<<<<<< HEAD
        lblTenNhanVien.setBounds(x1, y3, labelW, h);
        txtTenNhanVien.setBounds(x2, y3, fieldW, h);

        lblNgayNhap.setBounds(x3, y3, labelW, h);
        txtNgayNhap.setBounds(x4, y3, fieldW, h);

        lblMaPN.setBounds(x1, y4, labelW, h);
        txtMaPN.setBounds(x2, y4, fieldW, h);

        JPanel pnlCenter = new JPanel(new BorderLayout());
        add(pnlCenter, BorderLayout.CENTER);
=======
        // Vị trí hai TextField mới
        lblTimNCC.setBounds(x1, y3, labelW, h);
        txtTimNCC.setBounds(x2, y3, fieldW, h);

        lblTenNCC.setBounds(x3, y3, labelW, h);
        txtTenNCC.setBounds(x4, y3, fieldW, h);

        // Hiển thị tên NCC ban đầu
        hienThiTenNCC();

        JPanel pnlCenter;
        add(pnlCenter = new JPanel(new BorderLayout()), BorderLayout.CENTER);
>>>>>>> main

        JPanel pnlButton = new JPanel();

        btnThem = new JButton("Thêm");
        btnXoaRong = new JButton("Làm rỗng");
        btnXoa = new JButton("Xóa");
        btnLuu = new JButton("Xuất file danh sách phiếu nhập");
        btnXemChiTiet = new JButton("Xem chi tiết");

<<<<<<< HEAD
        pnlButton.add(btnThem);
        pnlButton.add(btnXoaRong);
        pnlButton.add(btnXoa);
        pnlButton.add(btnLuu);
        pnlButton.add(btnXemChiTiet);
=======
        pnlButton.add(btnLuu = new JButton("Xuất file danh sách phiếu nhập"));
        pnlButton.add(btnXemChiTiet = new JButton("Xem chi tiết"));
>>>>>>> main

        JPanel pnlTimKiem = new JPanel();

        pnlTimKiem.add(new JLabel("Tìm theo:"));

        cboTimKiem = new JComboBox<>(new String[]{
            "Mã phiếu nhập",
            "Mã nhà cung cấp",
            "Tên nhà cung cấp",
            "Mã nhân viên",
            "Tên nhân viên"
        });

        cboTimKiem.setPreferredSize(new Dimension(160, 25));

        txtTimKiem = new JTextField(15);
        btnTimKiem = new JButton("Tìm kiếm");

        pnlTimKiem.add(cboTimKiem);
        pnlTimKiem.add(txtTimKiem);
        pnlTimKiem.add(btnTimKiem);

        pnlCenter.add(pnlButton, BorderLayout.NORTH);
        pnlCenter.add(pnlTimKiem, BorderLayout.CENTER);

        String[] headers = {
            "Mã PN",
            "Mã NCC",
            "Tên nhà cung cấp",
            "Mã nhân viên",
            "Tên nhân viên",
            "Ngày nhập",
            "Tổng tiền"
        };

        tableModel = new DefaultTableModel(headers, 0) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        JScrollPane scroll = new JScrollPane(
                table,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        scroll.setBorder(
                BorderFactory.createTitledBorder("Danh sách phiếu nhập")
        );

        table.setRowHeight(25);

        add(scroll, BorderLayout.SOUTH);
        scroll.setPreferredSize(new Dimension(0, 350));

        Docdldatatable();

        table.addMouseListener(this);

        btnTimKiem.addActionListener(this);
        txtTimKiem.addActionListener(e -> {
            timKiem();
        });
        btnLuu.addActionListener(this);
        btnThem.addActionListener(this);
        btnXoaRong.addActionListener(this);
        btnXoa.addActionListener(this);
        btnXemChiTiet.addActionListener(this);

<<<<<<< HEAD
        txtTimKiem.addActionListener(e -> timKiem());

        ganSuKienTimKiem(txtTimMaNCC);

        cboNhaCungCap.addActionListener(e -> capNhatTenNCC());

        capNhatTenNCC();
    }

    private void ganSuKienTimKiem(JTextField txt) {

        txt.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                timKiemNCC();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                timKiemNCC();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                timKiemNCC();
            }
        });
    }

    private void timKiemNCC() {

        String maNCCCanTim = txtTimMaNCC.getText()
                .trim()
                .toLowerCase();
=======
        // Tìm NCC khi nhập nội dung
        txtTimNCC.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                timNhaCungCap();
            }
        });

        // Khi chọn mã NCC thì hiển thị tên
        cboNhaCungCap.addActionListener(e -> {
            hienThiTenNCC();
        });
    }

    // Tìm kiếm nhà cung cấp theo mã hoặc tên
    public void timNhaCungCap() {

        String tuKhoa = txtTimNCC.getText().trim().toLowerCase();

        String maDangChon = (String) cboNhaCungCap.getSelectedItem();
>>>>>>> main

        cboNhaCungCap.removeAllItems();

        for (NhaCungCap ncc : dsNCC) {

<<<<<<< HEAD
            if (ncc.getMaNCC() == null) {
                continue;
            }

            String maNCC = ncc.getMaNCC().toLowerCase();

            if (maNCC.contains(maNCCCanTim)) {
=======
            String ma = ncc.getMaNCC() == null
                    ? "" : ncc.getMaNCC().toLowerCase();

            String ten = ncc.getTenNCC() == null
                    ? "" : ncc.getTenNCC().toLowerCase();

            if (ma.contains(tuKhoa) || ten.contains(tuKhoa)) {
>>>>>>> main
                cboNhaCungCap.addItem(ncc.getMaNCC());
            }
        }

<<<<<<< HEAD
        if (cboNhaCungCap.getItemCount() > 0) {
            cboNhaCungCap.setSelectedIndex(0);
        } else {
            txtTenNCC.setText("");
        }
    }

    private void capNhatTenNCC() {

        String maNCC = (String) cboNhaCungCap.getSelectedItem();

        txtTenNCC.setText("");

        if (maNCC == null) {
=======
        // Giữ lại mã đang chọn nếu vẫn còn trong kết quả
        if (maDangChon != null) {
            cboNhaCungCap.setSelectedItem(maDangChon);
        }

        if (cboNhaCungCap.getItemCount() == 0) {
            txtTenNCC.setText("");
        } else {
            hienThiTenNCC();
        }
    }

    // Hiển thị tên nhà cung cấp theo mã được chọn
    public void hienThiTenNCC() {

        String maNCC = (String) cboNhaCungCap.getSelectedItem();

        if (maNCC == null) {
            txtTenNCC.setText("");
>>>>>>> main
            return;
        }

        for (NhaCungCap ncc : dsNCC) {

<<<<<<< HEAD
            if (ncc.getMaNCC() != null
                    && ncc.getMaNCC().equalsIgnoreCase(maNCC)) {

=======
            if (ncc.getMaNCC().equalsIgnoreCase(maNCC)) {
>>>>>>> main
                txtTenNCC.setText(ncc.getTenNCC());
                return;
            }
        }
<<<<<<< HEAD
    }

    public void Docdldatatable() {

        tableModel.setRowCount(0);

        List<PhieuNhap> list = pn_dao.layphieunhap();

        for (PhieuNhap pn : list) {

            BigDecimal tong
                    = ctpn_dao.layTongTienTheoMaPN(pn.getMaPN());

            String ngayStr = pn.getNgayNhap() != null
                    ? pn.getNgayNhap().format(
                            DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    )
                    : "";

            tableModel.addRow(new Object[]{
                pn.getMaPN(),
                pn.getNhaCungCap() != null
                ? pn.getNhaCungCap().getMaNCC() : "",
                pn.getNhaCungCap() != null
                ? pn.getNhaCungCap().getTenNCC() : "",
                pn.getNhanVien() != null
                ? pn.getNhanVien().getMaNhanVien() : "",
                pn.getNhanVien() != null
                ? pn.getNhanVien().getTenNhanVien() : "",
                ngayStr,
                DinhDangTien.hienThi(tong)
            });
        }
=======

        txtTenNCC.setText("");
>>>>>>> main
    }

    public void timKiem() {

        String tuKhoa = txtTimKiem.getText().trim();

        if (tuKhoa.isEmpty()) {
            sorter.setRowFilter(null);
            return;
        }

<<<<<<< HEAD
        int cotTimKiem;

        switch (cboTimKiem.getSelectedIndex()) {

=======
        int cot = cboTimKiem.getSelectedIndex();
        int cotTimKiem;

        switch (cot) {
>>>>>>> main
            case 0:
                cotTimKiem = 0;
                break;

            case 1:
                cotTimKiem = 1;
                break;

            case 2:
                cotTimKiem = 2;
                break;

            case 3:
                cotTimKiem = 3;
                break;

            case 4:
                cotTimKiem = 4;
                break;

            default:
                cotTimKiem = 0;
        }

        sorter.setRowFilter(
                RowFilter.regexFilter(
                        "(?i)" + Pattern.quote(tuKhoa),
                        cotTimKiem
                )
        );

        if (table.getRowCount() == 0) {

            JOptionPane.showMessageDialog(
                    this,
                    "Không tìm thấy phiếu nhập phù hợp!"
            );
        }
    }

<<<<<<< HEAD
=======
    public void Docdldatatable() {

        tableModel.setRowCount(0);

        List<PhieuNhap> list = pn_dao.layphieunhap();

        for (PhieuNhap pn : list) {

            BigDecimal tong
                    = ctpn_dao.layTongTienTheoMaPN(pn.getMaPN());

            String ngayStr = pn.getNgayNhap() != null
                    ? pn.getNgayNhap().format(
                            DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    : "";

            tableModel.addRow(new Object[]{
                pn.getMaPN(),
                pn.getNhaCungCap() != null
                ? pn.getNhaCungCap().getMaNCC() : "",
                pn.getNhanVien() != null
                ? pn.getNhanVien().getMaNhanVien() : "",
                ngayStr,
                new java.text.DecimalFormat("#,##0.##", java.text.DecimalFormatSymbols.getInstance(java.util.Locale.forLanguageTag("vi-VN"))).format(tong)
            });
        }
    }

>>>>>>> main
    @Override
    public void actionPerformed(ActionEvent e) {

        Object o = e.getSource();

        if (o.equals(btnTimKiem)) {
            timKiem();
        }

        if (o.equals(btnThem)) {

            if (kiemTraNgayNhap()) {

                String ma = txtMaPN.getText().trim();

                if (ma.isEmpty()) {
                    ma = taoMaPN();
                }

                String mncc = (String) cboNhaCungCap.getSelectedItem();

                if (mncc == null) {
                    JOptionPane.showMessageDialog(
                            this, "Vui lòng chọn nhà cung cấp!");
                    return;
                }

                String mv = txtNhanVien.getText().trim();

<<<<<<< HEAD
                if (mncc == null || mncc.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Vui lòng chọn nhà cung cấp!"
                    );

                    return;
                }

=======
>>>>>>> main
                if (mv.isEmpty()) {
                    mv = "NV0001";
                }

                NhaCungCap ncc = new NhaCungCap(mncc);
                NhanVien nv = new NhanVien(mv);

                String ngaynhap = txtNgayNhap.getText().trim();

                DateTimeFormatter formatter = DateTimeFormatter
                        .ofPattern("dd/MM/uuuu")
                        .withResolverStyle(ResolverStyle.STRICT);

                LocalDateTime nn = LocalDate.parse(
<<<<<<< HEAD
                        ngaynhap,
                        formatter
                ).atStartOfDay();

                PhieuNhap pn = new PhieuNhap(
                        ma,
                        ncc,
                        nv,
                        nn
                );
=======
                        ngaynhap, formatter).atStartOfDay();

                PhieuNhap pn = new PhieuNhap(ma, ncc, nv, nn);
>>>>>>> main

                try {

                    if (pn_dao.taopn(pn)) {

                        JOptionPane.showMessageDialog(
                                this,
                                "Tạo phiếu nhập " + ma
<<<<<<< HEAD
                                + " thành công! Chuyển sang chi tiết phiếu để thêm đơn hàng."
                        );
=======
                                + " thành công! Chuyển sang chi tiết phiếu để thêm đơn hàng.");
>>>>>>> main

                        Docdldatatable();

                        moChiTietPhieu(pn);

                        txtMaPN.setText(taoMaPN());

                    } else {
<<<<<<< HEAD

                        JOptionPane.showMessageDialog(
                                this,
                                "Thêm phiếu nhập thất bại!"
                        );
=======
                        JOptionPane.showMessageDialog(
                                this, "Thêm phiếu nhập thất bại!");
>>>>>>> main
                    }

                } catch (Exception s) {

                    JOptionPane.showMessageDialog(
<<<<<<< HEAD
                            this,
                            "Thêm thất bại: " + s.getMessage()
                    );
=======
                            this, "Thêm thất bại: " + s.getMessage());
>>>>>>> main

                    s.printStackTrace();
                }
            }
        }

        if (o.equals(btnXemChiTiet)) {

            int r = table.getSelectedRow();

            if (r == -1) {
<<<<<<< HEAD

                JOptionPane.showMessageDialog(
                        this,
                        "Vui lòng chọn phiếu nhập cần xem chi tiết!"
                );

                return;
            }

            moChiTietPhieuTheoDong(r);
=======
                JOptionPane.showMessageDialog(
                        this,
                        "Vui lòng chọn phiếu nhập cần xem chi tiết!");
                return;
            }

            // Chuyển từ chỉ số dòng đã sắp xếp sang dòng model
            int modelRow = table.convertRowIndexToModel(r);
            moChiTietPhieuTheoDong(modelRow);
>>>>>>> main
        }

        if (o.equals(btnXoaRong)) {

            txtNgayNhap.setText(
                    LocalDate.now().format(
<<<<<<< HEAD
                            DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    )
            );

            txtMaPN.setText(taoMaPN());

            txtTimMaNCC.setText("");
=======
                            DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            txtMaPN.setText(taoMaPN());

            txtTimNCC.setText("");

            // Hiện lại toàn bộ NCC
            cboNhaCungCap.removeAllItems();

            for (NhaCungCap ncc : dsNCC) {
                cboNhaCungCap.addItem(ncc.getMaNCC());
            }
>>>>>>> main

            if (cboNhaCungCap.getItemCount() > 0) {
                cboNhaCungCap.setSelectedIndex(0);
            }

<<<<<<< HEAD
            txtTimKiem.setText("");
            sorter.setRowFilter(null);
=======
            hienThiTenNCC();

>>>>>>> main
            table.clearSelection();
        }

        if (o.equals(btnLuu)) {
            xuatrafileXML();
        }

        if (o.equals(btnXoa)) {

            try {

                int r = table.getSelectedRow();

                if (r != -1) {

<<<<<<< HEAD
                    int row = table.convertRowIndexToModel(r);

                    String maPN
                            = tableModel.getValueAt(row, 0).toString();
=======
                    int modelRow = table.convertRowIndexToModel(r);

                    String maPN = tableModel.getValueAt(
                            modelRow, 0).toString();
>>>>>>> main

                    int hoi = JOptionPane.showConfirmDialog(
                            this,
                            "Bạn có chắc chắn muốn xóa phiếu nhập này cùng tất cả chi tiết?",
                            "Xác nhận xóa",
                            JOptionPane.YES_NO_OPTION
                    );

                    if (hoi == JOptionPane.YES_OPTION) {

                        ctpn_dao.xoaHetChiTietTheoMaPN(maPN);

                        if (pn_dao.xoapn(maPN)) {

                            JOptionPane.showMessageDialog(
<<<<<<< HEAD
                                    this,
                                    "Xóa thành công!"
                            );
=======
                                    this, "Xóa thành công!");
>>>>>>> main

                            Docdldatatable();

                        } else {

                            JOptionPane.showMessageDialog(
<<<<<<< HEAD
                                    this,
                                    "Xóa thất bại!"
                            );
=======
                                    this, "Xóa thất bại!");
>>>>>>> main
                        }
                    }

                } else {

                    JOptionPane.showMessageDialog(
<<<<<<< HEAD
                            this,
                            "Vui lòng chọn dòng cần xóa!"
                    );
=======
                            this, "Vui lòng chọn dòng cần xóa!");
>>>>>>> main
                }

            } catch (Exception s) {

                JOptionPane.showMessageDialog(
<<<<<<< HEAD
                        this,
                        "Xóa thất bại: " + s.getMessage()
                );
=======
                        this, "Xóa thất bại: " + s.getMessage());
>>>>>>> main

                s.printStackTrace();
            }
        }
    }

    private String taoMaPN() {

        List<PhieuNhap> lis = pn_dao.layphieunhap();

        int max = 0;

        for (PhieuNhap pn : lis) {

            String ma = pn.getMaPN();

            if (ma != null && ma.startsWith("PN")) {

                try {

                    int so = Integer.parseInt(ma.substring(2));

                    if (so > max) {
                        max = so;
                    }

                } catch (NumberFormatException e) {
                }
            }
        }

        return String.format("PN%08d", max + 1);
<<<<<<< HEAD
    }

    public void moChiTietPhieu(PhieuNhap pn) {

        if (chiTietGUI == null || !chiTietGUI.isDisplayable()) {

            chiTietGUI = new ChiTietPhieuNhap_GUI(pn, this);
            chiTietGUI.setVisible(true);

        } else {

            chiTietGUI.setPhieuNhap(pn);
            chiTietGUI.setParentGUI(this);
            chiTietGUI.setVisible(true);
            chiTietGUI.toFront();
            chiTietGUI.requestFocus();
        }
    }

    public void moChiTietPhieuTheoDong(int r) {

        if (r < 0 || r >= table.getRowCount()) {
            return;
        }

        int row = table.convertRowIndexToModel(r);

        String maPN
                = tableModel.getValueAt(row, 0).toString();

        String maNCC
                = tableModel.getValueAt(row, 1).toString();

        String maNV
                = tableModel.getValueAt(row, 3).toString();

        String ngayStr
                = tableModel.getValueAt(row, 5).toString();

        LocalDateTime ngayNhap;

        try {

            ngayNhap = LocalDate.parse(
                    ngayStr,
                    DateTimeFormatter.ofPattern("dd/MM/yyyy")
            ).atStartOfDay();

        } catch (Exception ex) {

            ngayNhap = LocalDateTime.now();
        }

        PhieuNhap pn = new PhieuNhap(
                maPN,
                new NhaCungCap(maNCC),
                new NhanVien(maNV),
                ngayNhap
        );

        moChiTietPhieu(pn);
=======
>>>>>>> main
    }

    public void xuatrafileXML() {

        JFileChooser chooser = new JFileChooser();

        chooser.setDialogTitle("Xuất danh sách phiếu nhập");

        chooser.setSelectedFile(
<<<<<<< HEAD
                new File("DanhSachPhieuNhap.xml")
        );
=======
                new File("DanhSachPhieuNhap.xml"));
>>>>>>> main

        int chon = chooser.showSaveDialog(this);

        if (chon != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();

        if (!file.getName().toLowerCase().endsWith(".xml")) {

            file = new File(
                    file.getAbsolutePath() + ".xml"
            );
        }

        try {

            DocumentBuilderFactory factory
                    = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder
                    = factory.newDocumentBuilder();

            Document doc = builder.newDocument();

            Element root
                    = doc.createElement("DanhSachPhieuNhap");

            doc.appendChild(root);

            for (int i = 0; i < table.getRowCount(); i++) {

                Element pn = doc.createElement("PhieuNhap");

                root.appendChild(pn);

<<<<<<< HEAD
                themElement(doc, pn, "MaPN",
                        table.getValueAt(i, 0));

                themElement(doc, pn, "MaNCC",
                        table.getValueAt(i, 1));

                themElement(doc, pn, "TenNCC",
                        table.getValueAt(i, 2));

                themElement(doc, pn, "MaNhanVien",
                        table.getValueAt(i, 3));

                themElement(doc, pn, "TenNhanVien",
                        table.getValueAt(i, 4));

                themElement(doc, pn, "NgayNhap",
                        table.getValueAt(i, 5));

                themElement(doc, pn, "TongTien",
                        table.getValueAt(i, 6));
=======
                Element ma = doc.createElement("MaPN");
                ma.setTextContent(
                        table.getValueAt(i, 0).toString());
                pn.appendChild(ma);

                Element maNCC = doc.createElement("MaNCC");
                maNCC.setTextContent(
                        table.getValueAt(i, 1).toString());
                pn.appendChild(maNCC);

                Element maNV = doc.createElement("MaNhanVien");
                maNV.setTextContent(
                        table.getValueAt(i, 2).toString());
                pn.appendChild(maNV);

                Element ngayNhap = doc.createElement("NgayNhap");
                ngayNhap.setTextContent(
                        table.getValueAt(i, 3).toString());
                pn.appendChild(ngayNhap);
>>>>>>> main
            }

            TransformerFactory tf
                    = TransformerFactory.newInstance();

            Transformer transformer = tf.newTransformer();

            transformer.setOutputProperty(
<<<<<<< HEAD
                    OutputKeys.INDENT,
                    "yes"
            );

            transformer.setOutputProperty(
                    OutputKeys.ENCODING,
                    "UTF-8"
            );
=======
                    OutputKeys.INDENT, "yes");

            transformer.setOutputProperty(
                    OutputKeys.ENCODING, "UTF-8");
>>>>>>> main

            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(file);

            transformer.transform(source, result);

            JOptionPane.showMessageDialog(
<<<<<<< HEAD
                    this,
                    "Xuất file XML thành công!"
            );
=======
                    this, "Xuất file XML thành công!");
>>>>>>> main

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
<<<<<<< HEAD
                    "Xuất file XML thất bại: " + e.getMessage()
            );
=======
                    "Xuất file XML thất bại: " + e.getMessage());
>>>>>>> main

            e.printStackTrace();
        }
    }

<<<<<<< HEAD
    private void themElement(
            Document doc,
            Element parent,
            String ten,
            Object giaTri
    ) {

        Element element = doc.createElement(ten);

        element.setTextContent(
                giaTri == null ? "" : giaTri.toString()
        );

        parent.appendChild(element);
    }

    public boolean kiemTraNgayNhap() {

        String ngay = txtNgayNhap.getText().trim();

        String regex
                = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/[0-9]{4}$";

        if (!ngay.matches(regex)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ngày nhập phải có định dạng dd/MM/yyyy!"
            );

            txtNgayNhap.requestFocus();

            return false;
        }

        try {

            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

            LocalDate.parse(ngay, formatter);

            return true;

        } catch (DateTimeParseException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ngày nhập không tồn tại!"
            );

            txtNgayNhap.requestFocus();

            return false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
=======
    public void moChiTietPhieu(PhieuNhap pn) {

        if (chiTietGUI == null || !chiTietGUI.isDisplayable()) {

            chiTietGUI = new ChiTietPhieuNhap_GUI(pn, this);
            chiTietGUI.setVisible(true);

        } else {

            chiTietGUI.setPhieuNhap(pn);
            chiTietGUI.setParentGUI(this);
            chiTietGUI.setVisible(true);
            chiTietGUI.toFront();
            chiTietGUI.requestFocus();
        }
    }

    public void moChiTietPhieuTheoDong(int r) {

        if (r < 0 || r >= tableModel.getRowCount()) {
            return;
        }

        String maPN = tableModel.getValueAt(r, 0).toString();
        String maNCC = tableModel.getValueAt(r, 1).toString();
        String maNV = tableModel.getValueAt(r, 2).toString();
        String ngayStr = tableModel.getValueAt(r, 3).toString();

        LocalDateTime ngayNhap = LocalDateTime.now();

        try {

            if (ngayStr.length() == 10 && ngayStr.contains("/")) {

                ngayNhap = LocalDate.parse(
                        ngayStr,
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                ).atStartOfDay();

            } else {

                ngayNhap = LocalDateTime.parse(ngayStr);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        PhieuNhap pn = new PhieuNhap(
                maPN,
                new NhaCungCap(maNCC),
                new NhanVien(maNV),
                ngayNhap);

        moChiTietPhieu(pn);
>>>>>>> main
    }

    public boolean kiemTraNgayNhap() {

        String ngay = txtNgayNhap.getText().trim();

        String regex
                = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/[0-9]{4}$";

        if (!ngay.matches(regex)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Ngày nhập phải có định dạng dd/MM/yyyy!");

            txtNgayNhap.requestFocus();

            return false;
        }

        try {

            DateTimeFormatter formatter = DateTimeFormatter
                    .ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

            LocalDate.parse(ngay, formatter);

            return true;

        } catch (DateTimeParseException e) {

            JOptionPane.showMessageDialog(
                    this, "Ngày nhập không tồn tại!");

            txtNgayNhap.requestFocus();

            return false;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
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

        new PhieuNhap_GUI().setVisible(true);
    }
}
