package com.example.educational_management_system.entity;

import lombok.Data;

@Data
public class Course {
    // 课程号
    private Integer id;

    // 课程名
    private String courseName;

    // 院系号 [逻辑外键]
    private Integer deptId;

    // 学时
    private Double hour;

    // 学分
    private Double credit;

    // 逻辑删除 1: 已删除   0: 正常
    private Integer isDeleted;
}
