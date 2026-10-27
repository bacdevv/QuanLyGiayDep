package bus;

import dao.ThongKeDAO;
import util.SessionContext;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ThongKeBUS {
    private final ThongKeDAO dao;

    public ThongKeBUS() {
        this(new ThongKeDAO());
    }

    public ThongKeBUS(ThongKeDAO dao) {
        this.dao = dao;
    }

    public BigDecimal netRevenue(LocalDate from, LocalDate to) throws SQLException {
        validateRange(from, to);
        requireManager();
        return dao.netRevenue(from, to);
    }

    public List<ThongKeDAO.RevenueRow> revenueByPayment(LocalDate from, LocalDate to) throws SQLException {
        validateRange(from, to);
        requireManager();
        return dao.revenueByPayment(from, to);
    }

    public List<ThongKeDAO.ProductRow> bestSelling(LocalDate from, LocalDate to, int limit) throws SQLException {
        validateRange(from, to);
        requireManager();
        if (limit < 1 || limit > 100)
            throw new IllegalArgumentException("Số dòng phải từ 1 đến 100");
        return dao.bestSelling(from, to, limit);
    }

    public List<ThongKeDAO.ProductRow> lowStock(int limit) throws SQLException {
        requireManager();
        if (limit < 1 || limit > 100)
            throw new IllegalArgumentException("Số dòng phải từ 1 đến 100");
        return dao.lowStock(limit);
    }

    public List<ThongKeDAO.CustomerRow> topCustomers(LocalDate from, LocalDate to, int limit) throws SQLException {
        validateRange(from, to);
        requireManager();
        if (limit < 1 || limit > 100)
            throw new IllegalArgumentException("Số dòng phải từ 1 đến 100");
        return dao.topCustomers(from, to, limit);
    }

    private static void validateRange(LocalDate from, LocalDate to) {
        if (from == null || to == null || from.isAfter(to)) {
            throw new IllegalArgumentException("Khoảng ngày không hợp lệ");
        }
    }

    private static void requireManager() {
        SessionContext.requireManager();
    }
}
