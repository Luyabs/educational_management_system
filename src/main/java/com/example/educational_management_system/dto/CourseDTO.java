package com.example.educational_management_system.dto;

import com.example.educational_management_system.entity.Course;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class CourseDTO extends Course {
    // 院系名 [多表]
    private String deptName;

    public CourseDTO() {
    }

    public CourseDTO(Course course) {
        BeanUtils.copyProperties(course, this);
    }
}
