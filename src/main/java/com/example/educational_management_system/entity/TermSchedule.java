package com.example.educational_management_system.entity;

import lombok.Data;

@Data
public class TermSchedule {
    // 选课表序号
    private Integer id;

    // 学期
    private String term;

    // 课号 [逻辑外键]
    private Integer courseId;

    // 教师工号 [逻辑外键]
    private Integer teacherId;

    // 上课时间
    private String time;
}
