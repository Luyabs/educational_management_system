package com.example.educational_management_system.dto;

import lombok.Data;

@Data
public class SelectCourseDTO {
    // 选课表序号
    private Integer id;

    // 学生
    private StudentDTO student;

    // 开设的课
    private TermScheduleDTO termSchedule;

    // 平时成绩
    private Double scoreUsual;

    // 考试成绩
    private Double scoreExam;

    // 总成绩
    private Double scoreTotal;

}
