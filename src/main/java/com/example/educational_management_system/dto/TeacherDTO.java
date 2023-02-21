package com.example.educational_management_system.dto;

import com.example.educational_management_system.entity.Teacher;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class TeacherDTO extends Teacher {
    // 院系名 [多表]
    private String deptName;

    public TeacherDTO() {
    }

    public TeacherDTO(Teacher teacher) {
        BeanUtils.copyProperties(teacher, this);
    }
}
