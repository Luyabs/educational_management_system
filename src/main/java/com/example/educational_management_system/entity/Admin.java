package com.example.educational_management_system.entity;

import lombok.Data;

@Data
public class Admin {
    // 管理员id
    private Integer id;

    // 用户名
    private String username;

    // 密码
    private String password;
}
