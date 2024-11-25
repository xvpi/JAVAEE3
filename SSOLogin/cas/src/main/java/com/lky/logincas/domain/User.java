package com.lky.logincas.domain;
import java.io.Serializable;
import java.util.Date;

public class User {
    private Integer id;
    private String username;
    private String password;
    private String loginTime;
    private String token;

    public User() {
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User(Integer id, String username, String password, String loginTime, String token
                ) {
        super();
        this.id = id;
        this.username = username;
        this.password = password;
        this.loginTime = loginTime;
        this.token = token;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public int getId(){
        return this.id;
    }
    public String getUsername(){
        return this.username;
    }
    public String getPassword(){
        return this.password;
    }
    public String getToken(){
        return this.token;
    }
    public String getLoginTime(){
        return this.loginTime.toString();
    }
    public String toString()
    {
        final String TAB = "    ";

        String retValue = "";

        retValue = "Login ( "
                + super.toString() + TAB
                + "id = " + this.id + TAB
                + "username = " + this.username + TAB
                + "password = " + this.password + TAB
                + "loginTime = " + this.loginTime + TAB
                + " )";

        return retValue;
    }

    public void setId(int id) {
        this.id = id;
    }
}