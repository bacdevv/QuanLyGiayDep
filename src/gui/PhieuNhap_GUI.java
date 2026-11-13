package gui;

import connectDB.ConnectDB;
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
    private JTextField txtNgayNhap;

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

    public PhieuNhap_GUI() {
        setTitle("Quản lý phiếu nhập");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        buildUI();
    }

    private void buildUI() {

        pn_dao = new PhieuNhap_DAO();
        ncc_dao = new NhaCungCap_DAO();

        try {
            ConnectDB.getInstance().connect();
            System.out.println("thành công");
        } catch (Exception e) {
            System.out.println("thất bại");
            e.printStackTrace();
        }

        JPanel pnlNorth;
        add(pnlNorth = new JPanel(), BorderLayout.NORTH);

        pnlNorth.setPreferredSize(new Dimension(0, 150));
        pnlNorth.setBorder(
                BorderFactory.createTitledBorder("Thông tin phiếu nhập"));
        pnlNorth.setLayout(null);

        JLabel lblNhaCungCap, lblNhanVien, lblNgayNhap;

        pnlNorth.add(lblNhaCungCap = new JLabel("Nhà cung cấp:"));
        pnlNorth.add(lblNhanVien = new JLabel("Nhân viên lập:"));
        pnlNorth.add(lblNgayNhap = new JLabel("Ngày nhập:"));

        pnlNorth.add(cboNhaCungCap = new JComboBox<String>());

        ArrayList<NhaCungCap> list = ncc_dao.getalltbNhacc();

        for (NhaCungCap p : list) {
            cboNhaCungCap.addItem(p.getMaNCC());
        }

        pnlNorth.add(txtNhanVien = new JTextField());
        pnlNorth.add(txtNgayNhap = new JTextField());

        txtNhanVien.setEditable(false);

        int labelW = 110;
        int fieldW = 250;
        int h = 28;

        int x1 = 30;
        int x2 = 150;
        int x3 = 520;
        int x4 = 640;

        int y1 = 25;
        int y2 = 75;

        lblNhaCungCap.setBounds(x1, y1, labelW, h);
        cboNhaCungCap.setBounds(x2, y1, fieldW, h);

        lblNhanVien.setBounds(x3, y1, labelW, h);
        txtNhanVien.setBounds(x4, y1, fieldW, h);

        lblNgayNhap.setBounds(x1, y2, labelW, h);
        txtNgayNhap.setBounds(x2, y2, fieldW, h);

        JPanel pnlCenter;
        add(pnlCenter = new JPanel(new BorderLayout()), BorderLayout.CENTER);

        JPanel pnlButton = new JPanel();

        pnlButton.add(btnThem = new JButton("Thêm"));
        pnlButton.add(btnXoaRong = new JButton("Làm rỗng"));
        pnlButton.add(btnXoa = new JButton("Xóa"));

        pnlButton.add(btnLuu = new JButton("xuất file danh sách phiếu nhập"));
        pnlButton.add(btnXemChiTiet = new JButton("Xem chi tiết"));

        JPanel pnlTimKiem = new JPanel();

        pnlTimKiem.add(new JLabel("Tìm theo:"));

        pnlTimKiem.add(cboTimKiem = new JComboBox<String>(
                new String[]{
                    "Mã phiếu nhập",
                    "Mã nhân viên",
                    "Mã nhà cung cấp"
                }));

        cboTimKiem.setPreferredSize(new Dimension(150, 25));

        pnlTimKiem.add(txtTimKiem = new JTextField(15));

        pnlTimKiem.add(btnTimKiem = new JButton("Tìm kiếm"));

        pnlCenter.add(pnlButton, BorderLayout.NORTH);
        pnlCenter.add(pnlTimKiem, BorderLayout.CENTER);

        JScrollPane scroll;

        String[] headers
                = "Mã PN;Nhà cung cấp;Nhân viên;Ngày nhập;Tổng tiền".split(";");

        tableModel = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        add(scroll = new JScrollPane(
                table,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        ), BorderLayout.SOUTH);

        scroll.setBorder(
                BorderFactory.createTitledBorder("Danh sách phiếu nhập"));

        table.setRowHeight(25);

        scroll.setPreferredSize(new Dimension(0, 350));

        Docdldatatable();
        btnTimKiem.addActionListener(this);
        btnLuu.addActionListener(this);
        btnThem.addActionListener(this);
        btnXoaRong.addActionListener(this);
        btnXoa.addActionListener(this);
    }

    public void timKiem() {

        String tuKhoa = txtTimKiem.getText().trim();
        if (tuKhoa.isEmpty()) {
            sorter.setRowFilter(null);
            return;
        }
        int cot = cboTimKiem.getSelectedIndex();
        int cotTimKiem;
        switch (cot) {
            case 0:
                cotTimKiem = 0;
                break;

            case 1:
                cotTimKiem = 2;
                break;

            case 2:
                cotTimKiem = 1;
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

    public void Docdldatatable() {

        tableModel.setRowCount(0);

        List<PhieuNhap> list = pn_dao.layphieunhap();

        for (PhieuNhap pn : list) {

            tableModel.addRow(new Object[]{
                pn.getMaPN(),
                pn.getNhaCungCap().getMaNCC(),
                pn.getNhanVien().getMaNhanVien(),
                pn.getNgayNhap(),
                ""
            });
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o.equals(btnTimKiem)) {
            timKiem();
        }
        if (o.equals(btnThem)) {
            if (kiemTraNgayNhap()) {
                String ma = taoMaPN();

                String mncc = "NCC0001";
                String mv = "NV0002";

                NhaCungCap ncc = new NhaCungCap(mncc);
                NhanVien nv = new NhanVien(mv);

                String ngaynhap = txtNgayNhap.getText().trim();

                DateTimeFormatter formatter = DateTimeFormatter
                        .ofPattern("dd/MM/uuuu")
                        .withResolverStyle(ResolverStyle.STRICT);

                LocalDateTime nn = LocalDate.parse(ngaynhap, formatter)
                        .atStartOfDay();
                PhieuNhap pn = new PhieuNhap(ma, ncc, nv, nn);
                try {
                    pn_dao.taopn(pn);
                    tableModel.addRow(new Object[]{
                        ma,
                        mncc,
                        mv,
                        nn.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    });
                } catch (Exception s) {
                    JOptionPane.showMessageDialog(this, "thêm tất bại");
                }
            }
        }
        if (o.equals(btnXoaRong)) {
            txtNgayNhap.setText("");
        }
        if (o.equals(btnLuu)) {
            xuatrafileXML();
        }
        if (o.equals(btnXoa)) {
            try {
                int r = table.getSelectedRow();

                if (r != -1) {
                    
		String maPN = table.getModel().getValueAt(r, 0).toString();

                    int hoi = JOptionPane.showConfirmDialog(this,
                            "Bạn có chắc chắn muốn xóa phiếu nhập này?",
                            "Xác nhận xóa",
                            JOptionPane.YES_NO_OPTION);

                    if (hoi == JOptionPane.YES_OPTION) {
                        if (pn_dao.xoapn(maPN)) {
                            JOptionPane.showMessageDialog(this, "Xóa thành công!");
                            Docdldatatable();
                        } else {
                            JOptionPane.showMessageDialog(this, "Xóa thất bại!");
                        }
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần xóa!");
                }

            } catch (Exception s) {
                JOptionPane.showMessageDialog(this, "Xóa thất bại: " + s.getMessage());
                s.printStackTrace();
            }
        }
    }

    private String taoMaPN() {

        List<PhieuNhap> lis = pn_dao.layphieunhap();

        int max = 0;

        for (PhieuNhap pn : lis) {

            String ma = pn.getMaPN();

            int so = Integer.parseInt(ma.substring(2));

            if (so > max) {
                max = so;
            }
        }

        return String.format("PN%04d", max + 1);
    }

    public void xuatrafileXML() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("xuất danh sách phiếu nhập");
        chooser.setSelectedFile(new File("DanhSachPhieuNhap.xml"));

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

            Element root = doc.createElement("DanhSachPhieuNhap");
            doc.appendChild(root);

            for (int i = 0; i < table.getRowCount(); i++) {
                Element pn = doc.createElement("PhieuNhap");
                root.appendChild(pn);

                Element ma = doc.createElement("MaPN");
                ma.setTextContent(table.getValueAt(i, 0).toString());
                pn.appendChild(ma);

                Element maNCC = doc.createElement("MaNCC");
                maNCC.setTextContent(table.getValueAt(i, 1).toString());
                pn.appendChild(maNCC);

                Element maNV = doc.createElement("MaNhanVien");
                maNV.setTextContent(table.getValueAt(i, 2).toString());
                pn.appendChild(maNV);

                Element ngayNhap = doc.createElement("NgayNhap");
                ngayNhap.setTextContent(table.getValueAt(i, 3).toString());
                pn.appendChild(ngayNhap);
            }

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);

            transformer.transform(source, result);

            JOptionPane.showMessageDialog(this,
                    "Xuất file XML thành công!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Xuất file XML thất bại: " + e.getMessage());
            e.printStackTrace();
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

    public boolean kiemTraNgayNhap() {
        String ngay = txtNgayNhap.getText().trim();

        String regex = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/[0-9]{4}$";

        if (!ngay.matches(regex)) {
            JOptionPane.showMessageDialog(this,
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
            JOptionPane.showMessageDialog(this,
                    "Ngày nhập không tồn tại!");
            txtNgayNhap.requestFocus();
            return false;
        }
    }

    public static void main(String[] args) {
        new PhieuNhap_GUI().setVisible(true);
    }

}
