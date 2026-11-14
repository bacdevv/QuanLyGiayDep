package gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import dao.ChiTietDoiTra_DAO;
import entity.ChiTietDoiTra;

public class ChiTietDoiTra_GUI extends JFrame {
    private JTextField txtMaPDT;
    private DefaultTableModel model;
    private JTable table;
    private ChiTietDoiTra_DAO ct_dao = new ChiTietDoiTra_DAO();

    public ChiTietDoiTra_GUI(String maPDT) {
        setTitle("Chi tiết đổi/trả");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel lbl = new JLabel("CHI TIẾT PHIẾU ĐỔI/TRẢ", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 24));
        add(lbl, BorderLayout.NORTH);

        JPanel pnl = new JPanel(new BorderLayout(5, 5));
        pnl.setBorder(BorderFactory.createTitledBorder("Thông tin phiếu"));
        JPanel pnlTim = new JPanel();
        pnlTim.add(new JLabel("Mã phiếu đổi/trả:"));
        txtMaPDT = new JTextField(maPDT, 18);
        txtMaPDT.setEditable(false);
        pnlTim.add(txtMaPDT);
        pnl.add(pnlTim, BorderLayout.NORTH);

        model = new DefaultTableModel(new String[] {
                "Mã CTSP", "Tên sản phẩm", "Size", "Màu", "Số lượng trả", "Số nhập lại"
        }, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(28);
        pnl.add(new JScrollPane(table), BorderLayout.CENTER);
        add(pnl, BorderLayout.CENTER);

        JButton btnIn = new JButton("In phiếu");
        btnIn.addActionListener(e -> {
            try {
                table.print(JTable.PrintMode.FIT_WIDTH,
                        new MessageFormat("Phiếu đổi/trả " + maPDT), null);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });
        JPanel duoi = new JPanel();
        duoi.add(btnIn);
        add(duoi, BorderLayout.SOUTH);

        try {
            docDuLieuDatabaseVaoTable(maPDT);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    public void docDuLieuDatabaseVaoTable(String maPDT) throws SQLException {
        model.setRowCount(0);
        ArrayList<ChiTietDoiTra> ds = ct_dao.getChiTietTheoMaPDT(maPDT);
        for (ChiTietDoiTra ct : ds) {
            model.addRow(new Object[] {
                    ct.getChiTietSanPham().getMaCTSP(),
                    ct.getChiTietSanPham().getSanPham().getTenSP(),
                    ct.getChiTietSanPham().getKichCo(),
                    ct.getChiTietSanPham().getMauSac(),
                    ct.getSoLuong(), ct.getSoLuongNhapLai()
            });
        }
        if (ds.isEmpty()) throw new SQLException("Không tìm thấy chi tiết phiếu " + maPDT);
    }
}
