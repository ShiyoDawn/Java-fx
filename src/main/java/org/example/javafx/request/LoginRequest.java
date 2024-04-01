package org.example.javafx.request;
/**
 * LoginRequest 登录请求数据类
 * String username 用户名
 * String password 密码
 */

public class LoginRequest {
    public String userName;

    public String password;

    public LoginRequest(String username, String password) {
        this.userName = username;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}