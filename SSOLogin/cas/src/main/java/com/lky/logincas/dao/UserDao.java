package com.lky.logincas.dao;

import com.lky.logincas.domain.User;
import com.lky.logincas.util.GenerateToken;
import com.lky.logincas.util.JdbcHelper;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.TreeSet;


public final class UserDao {
    private static UserDao userDao=new UserDao();
    private UserDao(){}
    public static UserDao getInstance(){
        return userDao;
    }

    public User valToken(String token){
        User user = null;
        try{
            //获得数据库连接对象
            Connection connection = JdbcHelper.getConn();
            //在该连接上创建语句盒子对象
            Statement stmt = connection.createStatement();
            //执行SQL查询语句并获得结果集对象
            String findByUsername_sql = "SELECT * FROM USER WHERE token=?";
            //在该连接上创建预编译语句对象
            PreparedStatement preparedStatement = connection.prepareStatement(findByUsername_sql);
            //为预编译参数赋值
            preparedStatement.setString(1,token);
            ResultSet resultSet = preparedStatement.executeQuery();
            //若结果存在下一条，执行循环体
            while (resultSet.next()) {
                user = new User(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("loginTime"), resultSet.getString("token"));

            }
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return user;
    }
    public ArrayList<User> showAllUser() throws SQLException {
        ArrayList<User> userArrayList = new ArrayList<>() ;
        //获得数据库连接对象
        Connection connection = JdbcHelper.getConn();

        String findByUsername_sql = "SELECT * FROM USER WHERE token!=''";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(findByUsername_sql);
        //执行预编译语句
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            userArrayList.add(new User(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("loginTime"),"token"));
        }
        System.out.println(userArrayList);
        //关闭资源
        JdbcHelper.close(resultSet,preparedStatement,connection);

        return userArrayList;
    }

    public User login(String username,String password) throws SQLException{
        //声明一个User类型的变量
        User user = null;
        String token =null;
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        //获得数据库连接对象
        Connection connection = JdbcHelper.getConn();

        String findByUsername_sql = "SELECT * FROM USER WHERE username=? AND password=?";
        PreparedStatement preparedStatement = connection.prepareStatement(findByUsername_sql);
        preparedStatement.setString(1,username);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()){
            token = GenerateToken.generateToken(new String [] {username,password});
            changeToken(resultSet.getInt("id"),token);
            changeLoginTime(resultSet.getInt("id"),formattedDateTime);
            user = new User(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("password"), formattedDateTime, token);
        }
        System.out.println(user);

        JdbcHelper.close(resultSet,preparedStatement,connection);
        return user;
    }
    public void logout(String token) throws SQLException{
        //声明一个User类型的变量
        User user = null;
        //获得数据库连接对象
        Connection connection = JdbcHelper.getConn();

        String findByUsername_sql = "SELECT * FROM USER WHERE token=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(findByUsername_sql);
        preparedStatement.setString(1, token);
        //执行预编译语句
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()){
            token = null;
            changeToken(resultSet.getInt("id"),token);
            user = new User(resultSet.getInt("id"), resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("loginTime"), resultSet.getString("token"));
        }
        System.out.println(user);
        //关闭资源
        JdbcHelper.close(resultSet,preparedStatement,connection);
        return;
    }
    public boolean changeToken(int id,String token) throws SQLException{

        Connection connection = JdbcHelper.getConn();
        String updatePassword_sql = " update user set token=? where id=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(updatePassword_sql);

        preparedStatement.setString(1,token);
        preparedStatement.setInt(2,id);
        int affectedRows = preparedStatement.executeUpdate();

        JdbcHelper.close(preparedStatement,connection);
        return affectedRows>0;
    }
    public boolean changeLoginTime(int id,String loginTime) throws SQLException{
        //获得数据库连接对象
        Connection connection = JdbcHelper.getConn();
        String updatePassword_sql = " update user set loginTime=? where id=?";
        //在该连接上创建预编译语句对象
        PreparedStatement preparedStatement = connection.prepareStatement(updatePassword_sql);
        //为预编译参数赋值
        preparedStatement.setString(1,loginTime);
        preparedStatement.setInt(2,id);
        //执行预编译语句，获取改变记录行数并赋值给affectedRowNum
        int affectedRows = preparedStatement.executeUpdate();
        //关闭资源
        JdbcHelper.close(preparedStatement,connection);
        return affectedRows>0;
    }
    // 查找用户名是否已存在
    public User findUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM user WHERE username = ?";
        try (Connection conn = JdbcHelper.getConn();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getString("username"), rs.getString("password"));
            }
            return null;
        }
    }


    // 插入新用户
    public User insertUser(User user) throws SQLException {
        String sql = "INSERT INTO user (username, password) VALUES (?, ?)";
        try (Connection conn = JdbcHelper.getConn();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        user.setId(rs.getInt(1));
                    }
                }
                return user;
            }
            return null;
        }
    }
}