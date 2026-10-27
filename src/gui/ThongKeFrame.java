package gui;

import bus.ThongKeBUS;
import dao.ThongKeDAO;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ThongKeFrame extends JDialog {
    private final ThongKeBUS bus = new ThongKeBUS();
    private final JTextField from = new JTextField(LocalDate.now().withDayOfMonth(1).toString(), 10);
    private final JTextField to = new JTextField(LocalDate.now().toString(), 10);
    private final JTextArea output = new JTextArea();

    public ThongKeFrame(Frame owner) {
        super(owner, "Thống kê", false);
        setSize(820, 520);
        setLocationRelativeTo(owner);
        output.setEditable(false);
        output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JPanel filter = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filter.add(new JLabel("Từ ngày (yyyy-MM-dd):")); filter.add(from);
        filter.add(new JLabel("Đến ngày:")); filter.add(to);
        JButton load = new JButton("Lọc");
        load.addActionListener(e -> load());
        filter.add(load);
        JButton reset = new JButton("Làm mới");
        reset.addActionListener(e -> { from.setText(LocalDate.now().withDayOfMonth(1).toString()); to.setText(LocalDate.now().toString()); load(); });
        filter.add(reset);
        add(filter, BorderLayout.NORTH);
        add(new JScrollPane(output), BorderLayout.CENTER);
        load();
    }

    private void load() {
        try {
            LocalDate start = LocalDate.parse(from.getText().trim());
            LocalDate end = LocalDate.parse(to.getText().trim());
            StringBuilder text = new StringBuilder();
            BigDecimal revenue = bus.netRevenue(start, end);
            text.append("DOANH THU THUẦN: ").append(revenue).append("\n\n");
            text.append("DOANH THU THEO PHƯƠNG THỨC\n");
            for (ThongKeDAO.RevenueRow row : bus.revenueByPayment(start, end)) {
                text.append(String.format("%-18s %12s (%d hóa đơn)%n", row.paymentMethod(), row.revenue(), row.invoiceCount()));
            }
            text.append("\nHÀNG BÁN CHẠY\n");
            for (ThongKeDAO.ProductRow row : bus.bestSelling(start, end, 10)) {
                text.append(String.format("%-16s %-25s SL bán: %4d  Doanh thu: %s%n",
                        row.sku(), row.productName(), row.soldQuantity(), row.revenue()));
            }
            text.append("\nTỒN THẤP\n");
            for (ThongKeDAO.ProductRow row : bus.lowStock(10)) {
                text.append(String.format("%-16s %-25s Tồn: %d%n", row.sku(), row.productName(), row.stock()));
            }
            text.append("\nKHÁCH MUA NHIỀU\n");
            for (ThongKeDAO.CustomerRow row : bus.topCustomers(start, end, 10)) {
                text.append(String.format("%-12s %-25s HĐ: %d  Chi tiêu: %s%n",
                        row.customerId(), row.customerName(), row.invoiceCount(), row.spending()));
            }
            output.setText(text.toString());
        } catch (Exception ex) {
            output.setText("");
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Không thể tải thống kê", JOptionPane.ERROR_MESSAGE);
        }
    }
}
