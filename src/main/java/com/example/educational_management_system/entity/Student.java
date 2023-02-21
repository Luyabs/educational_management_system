package com.example.educational_management_system.entity;

import lombok.Data;

@Data
public class Student {
    // 学号
    private Integer id;

    // 用户名
    private String username;

    // 密码
    private String password;

    // 真实姓名
    private String realName;

    // 院系号 [逻辑外键]
    private Integer deptId;

    // 状态 1: 正常   0: 已离校
    private Integer status;

    // 性别 1: 男性   0: 女性
    private Integer gender;

    // 出生日期
    private String birthday;
}
