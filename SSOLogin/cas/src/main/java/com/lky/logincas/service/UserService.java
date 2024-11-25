package com.lky.logincas.service;



import com.lky.logincas.dao.UserDao;
import com.lky.logincas.domain.User;
import com.lky.logincas.util.JdbcHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public final class UserService {
    private UserDao userDao = UserDao.getInstance();
    private static UserService userService = new UserService();

    public UserService() {
    }

    public static UserService getInstance(){
        return UserService.userService;
    }


    public User valToken(String token) throws SQLException {
        return UserDao.getInstance().valToken(token);
    }

    public User login(String username, String password) throws SQLException{
        return UserDao.getInstance().login(username, password);
    }
    public void logout(String token) throws SQLException{
        UserDao.getInstance().logout(token);
    }
    public ArrayList<User> showAllUser() throws SQLException {
        return UserDao.getInstance().showAllUser();
    }
    // 根据用户名查找用户
    public User findUserByUsername(String username) throws SQLException {
        return UserDao.getInstance().findUserByUsername(username);
    }
    // 根据token获取用户信息
    public User getUserByToken(String token) throws SQLException {
        String sql = "SELECT * FROM user WHERE token = ?";
        try (Connection conn = JdbcHelper.getConn();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // 从结果集中获取用户信息，并创建 User 对象
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setToken(rs.getString("token"));
                // 根据实际情况设置其他字段
                return user;
            }
        }
        return null;  // 如果没有找到用户，返回null
    }


    // 用户注册
    public User register(String username, String password) throws SQLException {
        User user = new User(username, password);
        return UserDao.getInstance().insertUser(user);
    }
//    public boolean changePassword(User user) throws SQLException{
//        return UserDao.getInstance().changePassword(user);
//    }
}