package gui;

import bus.ThongKeBUS;
import dao.ThongKeDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ThongKeFrame extends JPanel {
    private final ThongKeBUS bus = new ThongKeBUS();
    private final JTextField from = new JTextField(LocalDate.now().withDayOfMonth(1).toString(), 10);
    private final JTextField to = new JTextField(LocalDate.now().toString(), 10);
    private final JComboBox<String> payment = new JComboBox<>(
            new String[]{"Tất cả", "TIEN_MAT", "CHUYEN_KHOAN", "QUET_THE"});
    private final JLabel revenueValue = new JLabel("0");
    private final DefaultTableModel revenueModel = new DefaultTableModel(
            new Object[]{"Phương thức thanh toán", "Số hóa đơn", "Doanh thu"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final DefaultTableModel productModel = new DefaultTableModel(
            new Object[]{"SKU", "Tên sản phẩm", "Tồn kho", "Đã bán", "Doanh thu"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final DefaultTableModel customerModel = new DefaultTableModel(
            new Object[]{"Mã khách hàng", "Tên khách hàng", "Số hóa đơn", "Chi tiêu"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };

    public ThongKeFrame() {
        super(new BorderLayout());
        setBackground(new Color(245, 247, 250));
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setOpaque(false);
        root.setBorder(BorderFactory.createEmptyBorder(8, 14, 14, 14));
        JLabel title = new JLabel("THỐNG KÊ DOANH THU", SwingConstants.CENTER);
        title.setForeground(new Color(70, 127, 177));
        title.setFont(new Font("Segoe UI", Font.PLAIN, 30));
        JPanel filter = new JPanel(new GridBagLayout());
        filter.setBackground(Color.WHITE);
        filter.setBorder(BorderFactory.createTitledBorder("Thông tin thống kê"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 8, 6, 8);
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0; c.gridy = 0; filter.add(new JLabel("Từ ngày:"), c);
        c.gridx = 1; c.weightx = 0.35; filter.add(from, c);
        c.gridx = 2; c.weightx = 0; filter.add(new JLabel("Đến ngày:"), c);
        c.gridx = 3; c.weightx = 0.35; filter.add(to, c);
        c.gridx = 4; c.weightx = 0; filter.add(new JLabel("Phương thức:"), c);
        c.gridx = 5; c.weightx = 0.3; filter.add(payment, c);
        JButton load = new JButton("Lọc");
        load.addActionListener(e -> load());
        c.gridx = 6; c.weightx = 0; filter.add(load, c);
        JButton reset = new JButton("Làm mới");
        reset.addActionListener(e -> { from.setText(LocalDate.now().withDayOfMonth(1).toString()); to.setText(LocalDate.now().toString()); load(); });
        c.gridx = 7; filter.add(reset, c);
        JPanel heading = new JPanel(new BorderLayout(0, 8));
        heading.setOpaque(false);
        heading.add(title, BorderLayout.NORTH);
        heading.add(filter, BorderLayout.CENTER);
        root.add(heading, BorderLayout.NORTH);

        revenueValue.setFont(new Font("Segoe UI", Font.BOLD, 18));
        revenueValue.setForeground(new Color(42, 105, 156));
        JPanel total = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 7));
        total.setBackground(Color.WHITE);
        total.setBorder(BorderFactory.createLineBorder(new Color(210, 220, 230)));
        total.add(new JLabel("Tổng doanh thu thuần:"));
        total.add(revenueValue);

        JTable revenueTable = createTable(revenueModel);
        JPanel revenue = new JPanel(new BorderLayout(0, 7));
        revenue.setOpaque(false);
        revenue.add(total, BorderLayout.NORTH);
        revenue.add(new JScrollPane(revenueTable), BorderLayout.CENTER);

        JTable products = createTable(productModel);
        JTable customers = createTable(customerModel);
        JTabbedPane details = new JTabbedPane();
        details.addTab("Hàng bán chạy", new JScrollPane(products));
        details.addTab("Khách mua nhiều", new JScrollPane(customers));

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, revenue, details);
        split.setResizeWeight(0.52);
        split.setBorder(null);
        root.add(split, BorderLayout.CENTER);
        add(root);
        load();
    }

    private static JTable createTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(27);
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setBackground(new Color(211, 226, 239));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        return table;
    }

    private void load() {
        try {
            LocalDate start = LocalDate.parse(from.getText().trim());
            LocalDate end = LocalDate.parse(to.getText().trim());
            BigDecimal revenue = bus.netRevenue(start, end);
            revenueValue.setText(String.format("%,d", revenue.longValue()) + " VNĐ");
            revenueModel.setRowCount(0);
            String selectedPayment = (String) payment.getSelectedItem();
            for (ThongKeDAO.RevenueRow row : bus.revenueByPayment(start, end)) {
                if (!"Tất cả".equals(selectedPayment) && !selectedPayment.equals(row.paymentMethod())) continue;
                revenueModel.addRow(new Object[]{paymentLabel(row.paymentMethod()), row.invoiceCount(),
                        String.format("%,d VNĐ", row.revenue().longValue())});
            }

            productModel.setRowCount(0);
            for (ThongKeDAO.ProductRow row : bus.bestSelling(start, end, 10)) {
                productModel.addRow(new Object[]{row.sku(), row.productName(), row.stock(),
                        row.soldQuantity(), String.format("%,d VNĐ", row.revenue().longValue())});
            }

            customerModel.setRowCount(0);
            for (ThongKeDAO.CustomerRow row : bus.topCustomers(start, end, 10)) {
                customerModel.addRow(new Object[]{row.customerId(), row.customerName(),
                        row.invoiceCount(), String.format("%,d VNĐ", row.spending().longValue())});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Không thể tải thống kê", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String paymentLabel(String value) {
        return switch (value) {
            case "TIEN_MAT" -> "Tiền mặt";
            case "CHUYEN_KHOAN" -> "Chuyển khoản";
            case "QUET_THE" -> "Quẹt thẻ";
            default -> value;
        };
    }
}
