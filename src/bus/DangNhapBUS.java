package bus;

import dao.TaiKhoanDAO;
import entity.TaiKhoan;
import util.PasswordUtil;
import util.SessionContext;

import java.sql.SQLException;

public class DangNhapBUS {
    private final TaiKhoanDAO dao;

    public DangNhapBUS() {
        this(new TaiKhoanDAO());
    }

    public DangNhapBUS(TaiKhoanDAO dao) {
        this.dao = dao;
    }

    public TaiKhoan login(String username, String password) throws SQLException {
        String name = username == null ? "" : username.trim();
        if (name.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập tên đăng nhập và mật khẩu");
        }
        TaiKhoan account = dao.findForLogin(name);
        if (account == null || !account.isHoatDong()
                || !"DANG_LAM".equals(account.getTrangThaiNhanVien())
                || !PasswordUtil.matches(password, account.getMatKhau())) {
            throw new IllegalArgumentException("Tên đăng nhập hoặc mật khẩu không đúng, hoặc tài khoản không hoạt động");
        }
        if (!"QUAN_LY".equals(account.getVaiTro()) && !"BAN_HANG".equals(account.getVaiTro())) {
            throw new IllegalArgumentException("Vai trò tài khoản không hợp lệ");
        }
        SessionContext.login(account);
        return account;
    }

    public void logout() {
        SessionContext.logout();
    }
}
