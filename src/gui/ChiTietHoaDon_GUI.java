package gui;

import dao.ChiTietHoaDon_DAO;
import dao.HoaDon_DAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

// Chi xem va in cac dong cua mot hoa don da luu, khong them/sua/xoa tai day.
public class ChiTietHoaDon_GUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private final HoaDon_DAO hd_dao = new HoaDon_DAO();
    private final ChiTietHoaDon_DAO ct_dao = new ChiTietHoaDon_DAO();
    private DefaultTableModel model;
    private JTable table;
    private JLabel lblThongTin;

    public ChiTietHoaDon_GUI(String maHD) {
        setTitle("Chi tiết hóa đơn " + maHD);
        setSize(1200, 700);
        setMinimumSize(new java.awt.Dimension(900, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel tieuDe = new JLabel("CHI TIẾT HÓA ĐƠN", SwingConstants.CENTER);
        tieuDe.setForeground(Color.BLUE);
        tieuDe.setFont(new Font("Arial", Font.BOLD, 26));
        add(tieuDe, BorderLayout.NORTH);

        JPanel noiDung = new JPanel(new BorderLayout(8, 8));
        lblThongTin = new JLabel("", SwingConstants.CENTER);
        lblThongTin.setFont(new Font("Arial", Font.PLAIN, 16));
        noiDung.add(lblThongTin, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[] {
                "Mã CTSP", "Tên sản phẩm", "Size", "Màu", "Số lượng",
                "Đơn giá", "Thành tiền" }, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 15));
        table.setRowHeight(32);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        DinhDangTien.dinhDangCotTien(table, 5, 6);
        noiDung.add(new JScrollPane(table), BorderLayout.CENTER);
        add(noiDung, BorderLayout.CENTER);

        JPanel duoi = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnIn = new JButton("In hóa đơn");
        btnIn.setFont(new Font("Arial", Font.PLAIN, 16));
        btnIn.addActionListener(e -> {
            try {
                table.print(JTable.PrintMode.FIT_WIDTH,
                        new MessageFormat("Hóa đơn " + maHD),
                        new MessageFormat(lblThongTin.getText()));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });
        duoi.add(btnIn);
        add(duoi, BorderLayout.SOUTH);

        try {
            docDuLieuDatabaseVaoTable(maHD);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    public void docDuLieuDatabaseVaoTable(String maHD) throws SQLException {
        HoaDon hd = hd_dao.getHoaDonTheoMaHD(maHD);
        if (hd == null) throw new SQLException("Không tìm thấy hóa đơn: " + maHD);
        ArrayList<ChiTietHoaDon> ds = ct_dao.getChiTietTheoMaHD(maHD);
        model.setRowCount(0);
        for (ChiTietHoaDon ct : ds) {
            model.addRow(new Object[] {
                    ct.getChiTietSanPham().getMaCTSP(),
                    ct.getChiTietSanPham().getSanPham().getTenSP(),
                    ct.getChiTietSanPham().getKichCo(),
                    ct.getChiTietSanPham().getMauSac(),
                    ct.getSoLuong(), ct.getDonGia(), ct.tinhThanhTien()
            });
        }
        BigDecimal tong = hd.getTongTien();
        lblThongTin.setText("Mã HD: " + maHD + "     |     Tổng thanh toán: "
                + DinhDangTien.hienThi(tong));
    }
 
}
