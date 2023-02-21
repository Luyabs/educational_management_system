package com.example.educational_management_system.dto;

import com.example.educational_management_system.entity.SelectCourse;
import com.example.educational_management_system.entity.TermSchedule;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class SelectCourseDTO extends SelectCourse {
    // 学生名 [多表]
    private String studentName;

    // 开课项 [多表]
    private TermSchedule termSchedule;

    public SelectCourseDTO() {
    }

    public SelectCourseDTO(SelectCourse selectCourse) {
        BeanUtils.copyProperties(selectCourse, this);
    }
}
