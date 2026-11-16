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
    private JTextField txtNgayNhap;
    private JTextField txtMaPN;

    // Ô tìm kiếm mã nhà cung cấp
    private JTextField txtTimMaNCC;

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

    // Danh sách nhà cung cấp ban đầu
    private ArrayList<NhaCungCap> dsNCC = new ArrayList<>();

    // ================= CONSTRUCTOR =================
    public PhieuNhap_GUI() {

        setTitle("Quản lý phiếu nhập");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        buildUI();
    }

    public PhieuNhap_GUI(String maNCC) {
        this();
        setNhaCungCap(maNCC);
    }

    // ================= CHỌN NHÀ CUNG CẤP =================
    public void setNhaCungCap(String maNCC) {

        if (maNCC == null) {
            return;
        }

        String trimmed = maNCC.trim();

        // Nếu mã NCC chưa có trong ComboBox thì thêm vào
        for (int i = 0; i < cboNhaCungCap.getItemCount(); i++) {

            if (cboNhaCungCap.getItemAt(i).equalsIgnoreCase(trimmed)) {
                cboNhaCungCap.setSelectedIndex(i);
                return;
            }
        }

        cboNhaCungCap.addItem(trimmed);
        cboNhaCungCap.setSelectedItem(trimmed);
    }

    // ================= GIAO DIỆN =================
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

        // ================= NORTH =================
        JPanel pnlNorth = new JPanel();

        add(pnlNorth, BorderLayout.NORTH);

        pnlNorth.setPreferredSize(new Dimension(0, 165));

        pnlNorth.setBorder(
                BorderFactory.createTitledBorder("Thông tin phiếu nhập")
        );

        pnlNorth.setLayout(null);

        JLabel lblNhaCungCap = new JLabel("Nhà cung cấp:");
        JLabel lblTimMaNCC = new JLabel("Tìm mã NCC:");

        JLabel lblNhanVien = new JLabel("Mã nhân viên:");
        JLabel lblTenNhanVien = new JLabel("Tên nhân viên:");

        JLabel lblNgayNhap = new JLabel("Ngày nhập:");
        JLabel lblMaPN = new JLabel("Mã phiếu nhập:");

        pnlNorth.add(lblNhaCungCap);
        pnlNorth.add(lblTimMaNCC);
        pnlNorth.add(lblNhanVien);
        pnlNorth.add(lblTenNhanVien);
        pnlNorth.add(lblNgayNhap);
        pnlNorth.add(lblMaPN);

        // ================= COMBOBOX NHÀ CUNG CẤP =================
        cboNhaCungCap = new JComboBox<>();

        // Lấy toàn bộ nhà cung cấp từ database
        dsNCC = ncc_dao.getalltbNhacc();

        for (NhaCungCap ncc : dsNCC) {

            cboNhaCungCap.addItem(ncc.getMaNCC());
        }

        pnlNorth.add(cboNhaCungCap);

        // ================= TEXTFIELD TÌM MÃ NCC =================
        txtTimMaNCC = new JTextField();

        pnlNorth.add(txtTimMaNCC);

        // ================= MÃ NHÂN VIÊN =================
        txtNhanVien = new JTextField("NV0001");

        txtNhanVien.setEditable(false);

        pnlNorth.add(txtNhanVien);

        // ================= TÊN NHÂN VIÊN =================
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

        // ================= NGÀY NHẬP =================
        txtNgayNhap = new JTextField();

        txtNgayNhap.setText(
                LocalDate.now().format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy")
                )
        );

        pnlNorth.add(txtNgayNhap);

        // ================= MÃ PHIẾU NHẬP =================
        txtMaPN = new JTextField();

        txtMaPN.setText(taoMaPN());

        txtMaPN.setEditable(false);

        pnlNorth.add(txtMaPN);

        // ================= SET BOUNDS =================
        int labelW = 110;
        int fieldW = 250;
        int h = 28;

        int x1 = 30;
        int x2 = 150;
        int x3 = 520;
        int x4 = 640;

        int y1 = 15;
        int y2 = 55;
        int y3 = 95;

        lblNhaCungCap.setBounds(x1, y1, labelW, h);
        cboNhaCungCap.setBounds(x2, y1, fieldW, h);

        lblTimMaNCC.setBounds(x3, y1, labelW, h);
        txtTimMaNCC.setBounds(x4, y1, fieldW, h);

        lblNhanVien.setBounds(x1, y2, labelW, h);
        txtNhanVien.setBounds(x2, y2, fieldW, h);

        lblTenNhanVien.setBounds(x3, y2, labelW, h);
        txtTenNhanVien.setBounds(x4, y2, fieldW, h);

        lblNgayNhap.setBounds(x1, y3, labelW, h);
        txtNgayNhap.setBounds(x2, y3, fieldW, h);

        lblMaPN.setBounds(x3, y3, labelW, h);
        txtMaPN.setBounds(x4, y3, fieldW, h);

        // ================= CENTER =================
        JPanel pnlCenter = new JPanel(new BorderLayout());

        add(pnlCenter, BorderLayout.CENTER);

        // ================= CÁC NÚT =================
        JPanel pnlButton = new JPanel();

        btnThem = new JButton("Thêm");
        btnXoaRong = new JButton("Làm rỗng");
        btnXoa = new JButton("Xóa");
        btnLuu = new JButton("Xuất file danh sách phiếu nhập");
        btnXemChiTiet = new JButton("Xem chi tiết");

        pnlButton.add(btnThem);
        pnlButton.add(btnXoaRong);
        pnlButton.add(btnXoa);
        pnlButton.add(btnLuu);
        pnlButton.add(btnXemChiTiet);

        // ================= TÌM KIẾM BẢNG =================
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

        // ================= TABLE =================
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

        // Đọc dữ liệu vào bảng
        Docdldatatable();

        table.addMouseListener(this);

        // ================= EVENTS =================
        btnTimKiem.addActionListener(this);
        btnLuu.addActionListener(this);
        btnThem.addActionListener(this);
        btnXoaRong.addActionListener(this);
        btnXoa.addActionListener(this);
        btnXemChiTiet.addActionListener(this);

        txtTimKiem.addActionListener(e -> timKiem());

        // Tìm kiếm NCC realtime giống ChiTietPhieuNhap_GUI
        ganSuKienTimKiem(txtTimMaNCC);

    }

    // ================= GẮN SỰ KIỆN TÌM KIẾM =================
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

    // ================= TÌM KIẾM NCC =================
    private void timKiemNCC() {

        String maNCCCanTim = txtTimMaNCC.getText()
                .trim()
                .toLowerCase();

        // Xóa danh sách cũ trong ComboBox
        cboNhaCungCap.removeAllItems();

        // Duyệt danh sách nhà cung cấp ban đầu
        for (NhaCungCap ncc : dsNCC) {

            String maNCC = ncc.getMaNCC() != null
                    ? ncc.getMaNCC().toLowerCase()
                    : "";

            // Kiểm tra mã NCC có chứa từ khóa hay không
            if (maNCC.contains(maNCCCanTim)) {

                cboNhaCungCap.addItem(ncc.getMaNCC());
            }
        }

        // Nếu có kết quả thì chọn phần tử đầu tiên
        if (cboNhaCungCap.getItemCount() > 0) {

            cboNhaCungCap.setSelectedIndex(0);
        }
    }

    // ================= ĐỌC DỮ LIỆU =================
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
    }

    // ================= TÌM KIẾM BẢNG =================
    public void timKiem() {

        String tuKhoa = txtTimKiem.getText().trim();

        if (tuKhoa.isEmpty()) {

            sorter.setRowFilter(null);
            return;
        }

        int cotTimKiem;

        switch (cboTimKiem.getSelectedIndex()) {

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

    // ================= ACTION =================
    @Override
    public void actionPerformed(ActionEvent e) {

        Object o = e.getSource();

        if (o.equals(btnTimKiem)) {

            timKiem();
        }

        // ================= THÊM PHIẾU NHẬP =================
        if (o.equals(btnThem)) {

            if (kiemTraNgayNhap()) {

                String ma = txtMaPN.getText().trim();

                if (ma.isEmpty()) {
                    ma = taoMaPN();
                }

                String mncc = (String) cboNhaCungCap.getSelectedItem();

                String mv = txtNhanVien.getText().trim();

                if (mncc == null || mncc.isEmpty()) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Vui lòng chọn nhà cung cấp!"
                    );

                    return;
                }

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
                        ngaynhap,
                        formatter
                ).atStartOfDay();

                PhieuNhap pn = new PhieuNhap(
                        ma,
                        ncc,
                        nv,
                        nn
                );

                try {

                    if (pn_dao.taopn(pn)) {

                        JOptionPane.showMessageDialog(
                                this,
                                "Tạo phiếu nhập " + ma
                                + " thành công! Chuyển sang chi tiết phiếu để thêm đơn hàng."
                        );

                        Docdldatatable();

                        moChiTietPhieu(pn);

                        txtMaPN.setText(taoMaPN());

                    } else {

                        JOptionPane.showMessageDialog(
                                this,
                                "Thêm phiếu nhập thất bại!"
                        );
                    }

                } catch (Exception s) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Thêm thất bại: " + s.getMessage()
                    );

                    s.printStackTrace();
                }
            }
        }

        // ================= XEM CHI TIẾT =================
        if (o.equals(btnXemChiTiet)) {

            int r = table.getSelectedRow();

            if (r == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Vui lòng chọn phiếu nhập cần xem chi tiết!"
                );

                return;
            }

            moChiTietPhieuTheoDong(r);
        }

        // ================= LÀM RỖNG =================
        if (o.equals(btnXoaRong)) {

            txtNgayNhap.setText(
                    LocalDate.now().format(
                            DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    )
            );

            txtMaPN.setText(taoMaPN());

            // Xóa nội dung tìm kiếm NCC
            // DocumentListener sẽ tự động hiển thị lại toàn bộ NCC
            txtTimMaNCC.setText("");

            if (cboNhaCungCap.getItemCount() > 0) {
                cboNhaCungCap.setSelectedIndex(0);
            }

            txtTimKiem.setText("");

            sorter.setRowFilter(null);

            table.clearSelection();
        }

        // ================= XUẤT XML =================
        if (o.equals(btnLuu)) {

            xuatrafileXML();
        }

        // ================= XÓA PHIẾU NHẬP =================
        if (o.equals(btnXoa)) {

            try {

                int r = table.getSelectedRow();

                if (r != -1) {

                    int row = table.convertRowIndexToModel(r);

                    String maPN
                            = tableModel.getValueAt(row, 0).toString();

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
                                    this,
                                    "Xóa thành công!"
                            );

                            Docdldatatable();

                        } else {

                            JOptionPane.showMessageDialog(
                                    this,
                                    "Xóa thất bại!"
                            );
                        }
                    }

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Vui lòng chọn dòng cần xóa!"
                    );
                }

            } catch (Exception s) {

                JOptionPane.showMessageDialog(
                        this,
                        "Xóa thất bại: " + s.getMessage()
                );

                s.printStackTrace();
            }
        }
    }

    // ================= TẠO MÃ PHIẾU NHẬP =================
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
                    // Bỏ qua mã không đúng định dạng
                }
            }
        }

        return String.format("PN%08d", max + 1);
    }

    // ================= MỞ CHI TIẾT =================
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

    // ================= MỞ CHI TIẾT THEO DÒNG =================
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
    }

    // ================= XUẤT XML =================
    public void xuatrafileXML() {

        JFileChooser chooser = new JFileChooser();

        chooser.setDialogTitle("Xuất danh sách phiếu nhập");

        chooser.setSelectedFile(
                new File("DanhSachPhieuNhap.xml")
        );

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
            }

            TransformerFactory tf
                    = TransformerFactory.newInstance();

            Transformer transformer = tf.newTransformer();

            transformer.setOutputProperty(
                    OutputKeys.INDENT,
                    "yes"
            );

            transformer.setOutputProperty(
                    OutputKeys.ENCODING,
                    "UTF-8"
            );

            DOMSource source = new DOMSource(doc);

            StreamResult result = new StreamResult(file);

            transformer.transform(source, result);

            JOptionPane.showMessageDialog(
                    this,
                    "Xuất file XML thành công!"
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Xuất file XML thất bại: " + e.getMessage()
            );

            e.printStackTrace();
        }
    }

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

    // ================= KIỂM TRA NGÀY NHẬP =================
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

    // ================= MOUSE LISTENER =================
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

    // ================= MAIN =================
    public static void main(String[] args) {

        new PhieuNhap_GUI().setVisible(true);
    }
}
