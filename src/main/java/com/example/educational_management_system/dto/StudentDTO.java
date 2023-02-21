package com.example.educational_management_system.dto;

import com.example.educational_management_system.entity.Dept;
import lombok.Data;

@Data
public class StudentDTO {
    // 学号
    private Integer id;

    // 用户名
    private String username;

    // 密码 [隐藏]

    // 真实姓名
    private String realName;

    // 院系 [逻辑外键] [1:1]
    private Dept dept;

    // 状态 1: 正常   0: 已离校
    private String status;

    // 性别 1: 男性   0: 女性
    private String gender;

    // 出生日期
    private String birthday;
}
