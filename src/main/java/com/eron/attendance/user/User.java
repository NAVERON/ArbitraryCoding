package com.eron.attendance.user;

/**
 * 用户的结构
 *
 * @param name
 * @param password
 *
 * @author ERON
 */
public class User {

    private String name, password;

    public User() {
        super();
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
