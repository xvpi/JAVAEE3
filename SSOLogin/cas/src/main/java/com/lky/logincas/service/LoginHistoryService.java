package com.lky.logincas.service;

import com.lky.logincas.dao.LoginHistoryDao;
import com.lky.logincas.domain.LoginHistory;

import java.sql.SQLException;
import java.util.List;

public class LoginHistoryService {
    private final LoginHistoryDao loginHistoryDao = new LoginHistoryDao();

    // 添加登录记录
    public void recordLogin(int userId, String username, String ipAddress, String deviceInfo) throws SQLException {
        loginHistoryDao.addLoginRecord(userId, username, ipAddress, deviceInfo);
    }

    // 更新登出时间
    public void recordLogout(int userId) throws SQLException {
        loginHistoryDao.updateLogoutTime(userId);
    }

    // 分页获取登录记录
    public List<LoginHistory> getLoginHistory(int page, int pageSize) throws SQLException {
        return loginHistoryDao.getLoginHistory(page, pageSize);
    }

    // 获取历史记录的总数
    public static int getTotalRecordCount() throws SQLException {
        return LoginHistoryDao.getTotalRecordCount();
    }
}
