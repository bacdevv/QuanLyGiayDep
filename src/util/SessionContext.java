package util;

import entity.TaiKhoan;

public final class SessionContext {
    private static TaiKhoan currentUser;

    private SessionContext() {
    }

    public static void login(TaiKhoan account) {
        currentUser = account;
    }

    public static TaiKhoan getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static boolean isManager() {
        return currentUser != null && "QUAN_LY".equals(currentUser.getVaiTro());
    }

    public static void requireManager() {
        if (!isManager()) {
            throw new SecurityException("Chỉ quản lý mới được thực hiện thao tác này");
        }
    }

    public static void logout() {
        currentUser = null;
    }
}
