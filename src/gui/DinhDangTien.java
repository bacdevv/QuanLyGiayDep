package gui;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

// Chi dinh dang hien thi tien. Du lieu / tinh toan van la BigDecimal.
public final class DinhDangTien {
    // DECIMAL(18,2): 18 chu so tong cong, trong do 2 chu so thap phan.
    public static final BigDecimal GIOI_HAN_SQL = new BigDecimal("9999999999999999.99");

    private DinhDangTien() {
    }

    public static String hienThi(BigDecimal tien) {
        if (tien == null)
            return "";
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        DecimalFormat format = new DecimalFormat("#,##0.##", symbols);
        format.setRoundingMode(RoundingMode.HALF_UP);
        return format.format(tien);
    }

    public static void kiemTraGioiHan(BigDecimal tien, String ten)
            throws IllegalArgumentException {
        if (tien == null || tien.signum() < 0
                || tien.compareTo(GIOI_HAN_SQL) > 0
                || tien.stripTrailingZeros().scale() > 2) {
            throw new IllegalArgumentException(ten
                    + " phải từ 0 đến " + hienThi(GIOI_HAN_SQL)
                    + " và có tối đa 2 chữ số thập phân (SQL DECIMAL(18,2)).");
        }
    }

    public static void dinhDangCotTien(JTable table, int... cot) {
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void setValue(Object value) {
                if (value instanceof BigDecimal)
                    setText(hienThi((BigDecimal) value));
                else
                    super.setValue(value);
            }
        };
        renderer.setHorizontalAlignment(SwingConstants.RIGHT);
        for (int i : cot)
            table.getColumnModel().getColumn(i)
                    .setCellRenderer(renderer);
    }
}
