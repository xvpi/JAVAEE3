package com.xvpi.cas.dao;
import com.xvpi.cas.domain.LoginHistory;
import com.xvpi.cas.util.JdbcHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoginHistoryDao {
    private static final String GET_TOTAL_RECORD_COUNT = "SELECT COUNT(*) FROM login_history";
    // 添加登录记录
    public void addLoginRecord(int userId, String username, String ipAddress, String deviceInfo) throws SQLException {
        String sql = "INSERT INTO login_history (user_id, username, ip_address, device_info) VALUES (?, ?, ?, ?)";
        try (Connection conn = JdbcHelper.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, username);
            ps.setString(3, ipAddress);
            ps.setString(4, deviceInfo);
            ps.executeUpdate();
        }
    }

    // 更新登出时间
    public void updateLogoutTime(int userId) throws SQLException {
        String sql = "UPDATE login_history AS lh " +
                "JOIN (SELECT MAX(id) AS max_id FROM login_history WHERE user_id = ?) AS sub " +
                "ON lh.id = sub.max_id " +
                "SET lh.logout_time = CURRENT_TIMESTAMP";
        try (Connection conn = JdbcHelper.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    // 分页查询所有用户的登录记录
    public List<LoginHistory> getLoginHistory(int page, int pageSize) throws SQLException {
        String sql = "SELECT * FROM login_history ORDER BY login_time DESC LIMIT ?, ?";
        List<LoginHistory> historyList = new ArrayList<>();
        try (Connection conn = JdbcHelper.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, (page - 1) * pageSize);  // 计算偏移量
            ps.setInt(2, pageSize);  // 设置每页记录数
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LoginHistory history = new LoginHistory(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getTimestamp("login_time"),
                            rs.getTimestamp("logout_time"),
                            rs.getString("ip_address"),
                            rs.getString("device_info")
                    );
                    historyList.add(history);
                }
            }
        }
        return historyList;
    }

    // 获取总记录数
    public static int getTotalRecordCount() throws SQLException {
        int totalRecords = 0;
        try (Connection connection = JdbcHelper.getConn();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(GET_TOTAL_RECORD_COUNT)) {

            if (resultSet.next()) {
                totalRecords = resultSet.getInt(1); // 获取总记录数
            }
        }
        return totalRecords;
    }
}
