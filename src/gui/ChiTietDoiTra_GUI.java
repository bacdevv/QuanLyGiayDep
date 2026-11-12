package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import dao.ChiTietDoiTra_DAO;
import entity.ChiTietDoiTra;

// Chi xem cac san pham da doi/tra cua mot phieu, khong thao tac CRUD.
public class ChiTietDoiTra_GUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private final ChiTietDoiTra_DAO ct_dao = new ChiTietDoiTra_DAO();
    private DefaultTableModel model;
    private JTable table;

    public ChiTietDoiTra_GUI(String maPDT) {
        setTitle("Chi tiết đổi trả " + maPDT);
        setSize(1200, 700);
        setMinimumSize(new java.awt.Dimension(900, 500));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel tieuDe = new JLabel("CHI TIẾT ĐỔI TRẢ - " + maPDT,
                SwingConstants.CENTER);
        tieuDe.setForeground(Color.BLUE);
        tieuDe.setFont(new Font("Arial", Font.BOLD, 26));
        add(tieuDe, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[] {
                "Mã CTSP", "Tên sản phẩm", "Size", "Màu",
                "Số lượng trả", "Nhập lại kho" }, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 15));
        table.setRowHeight(32);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        add(new JScrollPane(table), BorderLayout.CENTER);

        try {
            docDuLieuDatabaseVaoTable(maPDT);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    public void docDuLieuDatabaseVaoTable(String maPDT) throws SQLException {
        ArrayList<ChiTietDoiTra> ds = ct_dao.getChiTietTheoMaPDT(maPDT);
        model.setRowCount(0);
        for (ChiTietDoiTra ct : ds) {
            model.addRow(new Object[] {
                    ct.getChiTietSanPham().getMaCTSP(),
                    ct.getChiTietSanPham().getSanPham().getTenSP(),
                    ct.getChiTietSanPham().getKichCo(),
                    ct.getChiTietSanPham().getMauSac(),
                    ct.getSoLuong(), ct.getSoLuongNhapLai()
            });
        }
        if (ds.isEmpty()) throw new SQLException("Không có chi tiết phiếu: " + maPDT);
    }
}
