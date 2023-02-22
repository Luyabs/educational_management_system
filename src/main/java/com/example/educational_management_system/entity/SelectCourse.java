package com.example.educational_management_system.entity;

import lombok.Data;

@Data
public class SelectCourse {
    // 选课表序号
    private Integer id;

    // 学号 [逻辑外键]
    private Integer studentId;

    // 开课表序号 [逻辑外键]
    private Integer termScheduleId;

    // 平时成绩
    private Double scoreUsual;

    // 考试成绩
    private Double scoreExam;

    // 总成绩
    private Double scoreTotal;

    public SelectCourse() {
    }

    public SelectCourse(int studentId, int termScheduleId) {
        this.studentId = studentId;
        this.termScheduleId = termScheduleId;
    }
}
