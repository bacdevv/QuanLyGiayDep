package gui;
import  connectDB.ConnectDB;
import dao.NhaCungCap_DAO;
import entity.NhaCungCap;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
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

public class NhaCungCap_GUI extends JFrame implements ActionListener, MouseListener {

    private static final long serialVersionUID = 1L;

    private JTextField txtTenNCC;
    private JTextField txtSoDienThoai;
    private JTextField txtTimKiem;

    private JCheckBox chkHoatDong;

    private JButton btnThem;
    private JButton btnXoaRong;
    private JButton btnTimKiem;
    private JButton btnSua;
    private JButton btnLuu;
    private JButton btnLoc;
    private JButton btnTaoPhieu;

    private JComboBox<String> cboTimKiem;
    private JComboBox<String> cboTrangThai;

    private JTable table;
    private DefaultTableModel tableModel;

    private NhaCungCap_DAO ncc_dao;
    private PhieuNhap_GUI phieuNhapGUI;

    public NhaCungCap_GUI() {

        ncc_dao = new NhaCungCap_DAO();

        setTitle("Quản lý nhà cung cấp");
        setSize(900, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        try {
            ConnectDB.getInstance().connect();
            System.out.println("Thành công");
        } catch (Exception e) {
            System.out.println("Lỗi kết nối");
            e.printStackTrace();
        }

        buildUI();
    }

    private void buildUI() {

        setLayout(new BorderLayout(5, 5));

        // ================= PHẦN NORTH =================
        JPanel pnlNorth = new JPanel();
        pnlNorth.setPreferredSize(new Dimension(0, 140));
        pnlNorth.setBorder(BorderFactory.createTitledBorder(
                "Thông tin nhà cung cấp"));
        pnlNorth.setLayout(null);

        JLabel lblTenNCC = new JLabel("Tên nhà cung cấp:");
        JLabel lblSoDienThoai = new JLabel("Số điện thoại:");

        txtTenNCC = new JTextField();
        txtSoDienThoai = new JTextField();
        chkHoatDong = new JCheckBox("Đang hoạt động");

        pnlNorth.add(lblTenNCC);
        pnlNorth.add(lblSoDienThoai);
        pnlNorth.add(txtTenNCC);
        pnlNorth.add(txtSoDienThoai);
        pnlNorth.add(chkHoatDong);

        int w1 = 120, w2 = 300, h = 20;

        lblTenNCC.setBounds(20, 55, w1, h);
        txtTenNCC.setBounds(150, 55, w2, h);

        lblSoDienThoai.setBounds(20, 85, w1, h);
        txtSoDienThoai.setBounds(150, 85, 200, h);

        chkHoatDong.setBounds(450, 85, 150, h);

        add(pnlNorth, BorderLayout.NORTH);

        // ================= PHẦN CENTER: 3 HÀNG CHỨC NĂNG =================
        JPanel pnlCenter = new JPanel();
        pnlCenter.setLayout(new BoxLayout(pnlCenter, BoxLayout.Y_AXIS));
        pnlCenter.setPreferredSize(new Dimension(0, 135));

        // HÀNG 1: CÁC NÚT CHỨC NĂNG
        JPanel pnlButton = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 15, 5));

        btnThem = new JButton("Thêm");
        btnXoaRong = new JButton("Làm rỗng");
        btnSua = new JButton("Sửa");
        btnLuu = new JButton("In danh sách nhà cung cấp");
        btnTaoPhieu = new JButton("Tạo phiếu nhập");

        pnlButton.add(btnThem);
        pnlButton.add(btnXoaRong);
        pnlButton.add(btnSua);
        pnlButton.add(btnLuu);
        pnlButton.add(btnTaoPhieu);

        // HÀNG 2: TÌM KIẾM
        JPanel pnlTimKiem = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 5));

        JLabel lblTimKiem = new JLabel("Tìm kiếm:");

        cboTimKiem = new JComboBox<>(
                new String[]{"Mã NCC", "Tên NCC"});

        txtTimKiem = new JTextField(20);
        btnTimKiem = new JButton("Tìm kiếm");

        pnlTimKiem.add(lblTimKiem);
        pnlTimKiem.add(cboTimKiem);
        pnlTimKiem.add(txtTimKiem);
        pnlTimKiem.add(btnTimKiem);

        // HÀNG 3: LỌC TRẠNG THÁI
        JPanel pnlLoc = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 5));

        JLabel lblLoc = new JLabel("Lọc trạng thái:");

        cboTrangThai = new JComboBox<>(
                new String[]{"Tất cả", "Hoạt động", "Ngừng hoạt động"});

        cboTrangThai.setPreferredSize(new Dimension(160, 25));

        btnLoc = new JButton("Lọc");

        pnlLoc.add(lblLoc);
        pnlLoc.add(cboTrangThai);
        pnlLoc.add(btnLoc);

        // Thêm 3 hàng vào CENTER
        pnlCenter.add(pnlButton);
        pnlCenter.add(pnlTimKiem);
        pnlCenter.add(pnlLoc);

        add(pnlCenter, BorderLayout.CENTER);

        // ================= PHẦN TABLE =================
        String[] headers = {
            "Mã NCC",
            "Tên nhà cung cấp",
            "Số điện thoại",
            "Hoạt động"
        };

        tableModel = new DefaultTableModel(headers, 0);

        table = new JTable(tableModel);
        table.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(
                table,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        scroll.setBorder(BorderFactory.createTitledBorder(
                "Danh sách nhà cung cấp"));

        scroll.setPreferredSize(new Dimension(0, 280));

        add(scroll, BorderLayout.SOUTH);

        // Đọc dữ liệu
        docDuLieuDdsvaobang();

        // ================= ĐĂNG KÝ SỰ KIỆN =================
        table.addMouseListener(this);

        btnThem.addActionListener(this);
        btnXoaRong.addActionListener(this);
        btnTimKiem.addActionListener(this);
        btnSua.addActionListener(this);
        btnLuu.addActionListener(this);
        btnLoc.addActionListener(this);
        btnTaoPhieu.addActionListener(this);
    }

    // ================= SỰ KIỆN CLICK BẢNG =================
    @Override
    public void mouseClicked(MouseEvent e) {
        int r = table.getSelectedRow();
        if (r != -1) {
            txtTenNCC.setText(tableModel.getValueAt(r, 1).toString());
            txtSoDienThoai.setText(tableModel.getValueAt(r, 2).toString());
            chkHoatDong.setSelected(tableModel.getValueAt(r, 3).toString().equals("Hoạt động"));
            if (e.getClickCount() == 2) {
                String maNCC = tableModel.getValueAt(r, 0).toString();
                moPhieuNhap(maNCC);
            }
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

    @Override
    public void actionPerformed(ActionEvent e) {

        Object o = e.getSource();

        if (o == btnThem) {

            String ten = txtTenNCC.getText().trim();
            String sdt = txtSoDienThoai.getText().trim();

            if (ten.isEmpty() || sdt.isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Vui lòng nhập đầy đủ thông tin");
                return;
            }

            String ma = taoMaNCC();

            NhaCungCap a = new NhaCungCap(
                    ma,
                    ten,
                    sdt,
                    chkHoatDong.isSelected()
            );

            try {

                ncc_dao.createNCC(a);

                JOptionPane.showMessageDialog(this,
                        "Thêm nhà cung cấp thành công");

                docDuLieuDdsvaobang();

            } catch (Exception ex) {

                JOptionPane.showMessageDialog(this,
                        "Thêm thất bại");

                ex.printStackTrace();
            }
        } else if (o == btnXoaRong) {
            txtSoDienThoai.setText("");
            txtTimKiem.setText("");

            chkHoatDong.setSelected(false);

        } else if (o == btnTimKiem) {

            String tuKhoa = txtTimKiem.getText().trim();

            if (tuKhoa.isEmpty()) {

                docDuLieuDdsvaobang();
                return;
            }

            int cot = cboTimKiem.getSelectedIndex();

            tableModel.setRowCount(0);

            List<NhaCungCap> lis = ncc_dao.getalltbNhacc();

            for (NhaCungCap ncc : lis) {

                boolean ketQua = false;

                if (cot == 0) {

                    ketQua = ncc.getMaNCC()
                            .toLowerCase()
                            .contains(tuKhoa.toLowerCase());

                } else if (cot == 1) {

                    ketQua = ncc.getTenNCC()
                            .toLowerCase()
                            .contains(tuKhoa.toLowerCase());
                }

                if (ketQua) {

                    Object[] row = {
                        ncc.getMaNCC(),
                        ncc.getTenNCC(),
                        ncc.getSoDienThoai(),
                        ncc.isHoatDong()
                        ? "Hoạt động"
                        : "Ngừng hoạt động"
                    };

                    tableModel.addRow(row);
                }
            }

            if (tableModel.getRowCount() == 0) {

                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy nhà cung cấp");
            }
        } else if (o == btnSua) {

            int r = table.getSelectedRow();

            if (r == -1) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn nhà cung cấp cần sửa!");
                return;
            }

            String maNCC = tableModel.getValueAt(r, 0).toString();

            String tenNCC = txtTenNCC.getText().trim();
            String sdt = txtSoDienThoai.getText().trim();
            boolean hoatDong = chkHoatDong.isSelected();

            if (tenNCC.isEmpty() || sdt.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            NhaCungCap a = new NhaCungCap(
                    maNCC,
                    tenNCC,
                    sdt,
                    hoatDong
            );

            if (ncc_dao.updateNCC(a)) {

                JOptionPane.showMessageDialog(this,
                        "Cập nhật nhà cung cấp thành công!");

                docDuLieuDdsvaobang();

                txtTenNCC.setText("");
                txtSoDienThoai.setText("");
                chkHoatDong.setSelected(false);

            } else {

                JOptionPane.showMessageDialog(this,
                        "Cập nhật nhà cung cấp thất bại!");
            }
        } else if (o == btnLuu) {
            xuatrafileXML();
        } else if (o == btnLoc) {

            String trangThai = cboTrangThai.getSelectedItem().toString();

            tableModel.setRowCount(0);

            List<NhaCungCap> lis = ncc_dao.getalltbNhacc();

            for (NhaCungCap ncc : lis) {

                String trangThaiNCC = ncc.isHoatDong()
                        ? "Hoạt động"
                        : "Ngừng hoạt động";

                if (trangThai.equalsIgnoreCase("Tất cả")
                        || trangThaiNCC.equals(trangThai)) {

                    Object[] row = {
                        ncc.getMaNCC(),
                        ncc.getTenNCC(),
                        ncc.getSoDienThoai(),
                        trangThaiNCC
                    };

                    tableModel.addRow(row);
                }
            }

            if (tableModel.getRowCount() == 0) {

                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy nhà cung cấp có trạng thái này");
            }
        } else if (o == btnTaoPhieu) {
            int r = table.getSelectedRow();
            if (r == -1) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn một nhà cung cấp trong bảng để tạo phiếu nhập!");
                return;
            }
            String maNCC = tableModel.getValueAt(r, 0).toString();
            moPhieuNhap(maNCC);
        }
    }

    public void moPhieuNhap(String maNCC) {
        if (phieuNhapGUI == null || !phieuNhapGUI.isDisplayable()) {
            phieuNhapGUI = new PhieuNhap_GUI(maNCC);
            phieuNhapGUI.setVisible(true);
        } else {
            phieuNhapGUI.setNhaCungCap(maNCC);
            phieuNhapGUI.setVisible(true);
            phieuNhapGUI.toFront();
            phieuNhapGUI.requestFocus();
        }
    }

    private String taoMaNCC() {

        List<NhaCungCap> lis = ncc_dao.getalltbNhacc();

        int max = 0;

        for (NhaCungCap ncc : lis) {

            String ma = ncc.getMaNCC();

            int so = Integer.parseInt(ma.substring(3));

            if (so > max) {
                max = so;
            }
        }

        return String.format("NCC%04d", max + 1);
    }

    public void docDuLieuDdsvaobang() {

        tableModel.setRowCount(0);

        List<NhaCungCap> lis = ncc_dao.getalltbNhacc();

        for (NhaCungCap ncc : lis) {

            Object[] row = {
                ncc.getMaNCC(),
                ncc.getTenNCC(),
                ncc.getSoDienThoai(),
                ncc.isHoatDong()
                ? "Hoạt động"
                : "Ngừng hoạt động"
            };

            tableModel.addRow(row);
        }
    }

    public void xuatrafileXML() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Lưu danh sách nhà cung cấp");
        chooser.setSelectedFile(new File("DanhSachNhaCungCap.xml"));

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

            Element root = doc.createElement("DanhSachNhaCungCap");
            doc.appendChild(root);

            for (int i = 0; i < table.getRowCount(); i++) {
                Element ncc = doc.createElement("NhaCungCap");
                root.appendChild(ncc);

                Element ma = doc.createElement("MaNCC");
                ma.setTextContent(table.getValueAt(i, 0).toString());
                ncc.appendChild(ma);

                Element ten = doc.createElement("TenNCC");
                ten.setTextContent(table.getValueAt(i, 1).toString());
                ncc.appendChild(ten);

                Element sdt = doc.createElement("SoDienThoai");
                sdt.setTextContent(table.getValueAt(i, 2).toString());
                ncc.appendChild(sdt);

                Element trangThai = doc.createElement("HoatDong");
                trangThai.setTextContent(table.getValueAt(i, 3).toString());
                ncc.appendChild(trangThai);
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
            JOptionPane.showMessageDialog(this, "Xuất file XML thất bại: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        new NhaCungCap_GUI().setVisible(true);
    }
}
