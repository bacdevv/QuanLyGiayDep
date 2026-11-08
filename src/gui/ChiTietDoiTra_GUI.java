package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

public class ChiTietDoiTra_GUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField txtMaPDT;
    private JComboBox<String> cboChiTietSanPham;
    private JTextField txtSoLuong, txtSoLuongNhapLai;
    private JButton btnThem, btnXoaRong, btnXoa, btnSua, btnLuu;
    private JTable table;
    private DefaultTableModel tableModel;

    public ChiTietDoiTra_GUI() {
        setTitle("Chi tiết đổi trả");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        buildUI();
    }

    private void buildUI() {
        // Phần North
        JPanel pnlNorth = new JPanel();
        add(pnlNorth, BorderLayout.NORTH);
        pnlNorth.setPreferredSize(new Dimension(0, 140));
        pnlNorth.setBorder(BorderFactory.createTitledBorder("Thông tin chi tiết đổi trả"));
        pnlNorth.setLayout(null);

        JLabel lblMaPDT = new JLabel("Mã phiếu đổi trả:");
        JLabel lblSanPham = new JLabel("Sản phẩm:");
        JLabel lblSoLuong = new JLabel("Số lượng trả:");
        JLabel lblSoLuongNhapLai = new JLabel("Số nhập lại kho:");
        pnlNorth.add(lblMaPDT);
        pnlNorth.add(lblSanPham);
        pnlNorth.add(lblSoLuong);
        pnlNorth.add(lblSoLuongNhapLai);
        pnlNorth.add(txtMaPDT = new JTextField());
        pnlNorth.add(cboChiTietSanPham = new JComboBox<String>());
        pnlNorth.add(txtSoLuong = new JTextField());
        pnlNorth.add(txtSoLuongNhapLai = new JTextField());
        txtMaPDT.setEditable(false);

        int w1 = 120, w2 = 250, h = 20;
        lblMaPDT.setBounds(20, 25, w1, h);
        txtMaPDT.setBounds(150, 25, 200, h);
        lblSanPham.setBounds(450, 25, w1, h);
        cboChiTietSanPham.setBounds(570, 25, w2, h);
        lblSoLuong.setBounds(20, 55, w1, h);
        txtSoLuong.setBounds(150, 55, 200, h);
        lblSoLuongNhapLai.setBounds(450, 55, w1, h);
        txtSoLuongNhapLai.setBounds(570, 55, 200, h);

        // Phần Center
        JPanel pnlCenter = new JPanel();
        add(pnlCenter, BorderLayout.CENTER);
        pnlCenter.add(btnThem = new JButton("Thêm"));
        pnlCenter.add(btnXoaRong = new JButton("Làm rỗng"));
        pnlCenter.add(btnXoa = new JButton("Xóa"));
        pnlCenter.add(btnSua = new JButton("Sửa"));
        pnlCenter.add(btnLuu = new JButton("Lưu"));

        // Phần South
        String[] headers = "Mã CTSP;Tên sản phẩm;Kích cỡ;Màu sắc;Số lượng trả;Số nhập lại kho".split(";");
        tableModel = new DefaultTableModel(headers, 0);
        table = new JTable(tableModel);
        table.setDefaultEditor(Object.class, null);
        table.setRowHeight(20);
        JScrollPane scroll = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setBorder(BorderFactory.createTitledBorder("Danh sách chi tiết đổi trả"));
        scroll.setPreferredSize(new Dimension(0, 350));
        add(scroll, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        new ChiTietDoiTra_GUI().setVisible(true);
    }
}
