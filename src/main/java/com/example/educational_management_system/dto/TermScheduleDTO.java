package com.example.educational_management_system.dto;

import lombok.Data;

@Data
public class TermScheduleDTO {
    // 选课表序号
    private Integer id;

    // 学期
    private String term;

    // 课程
    private CourseDTO course;

    // 教师
    private TeacherDTO teacher;

    // 上课时间
    private String time;
}
