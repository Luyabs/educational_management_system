package com.example.educational_management_system.dto;

import com.example.educational_management_system.entity.Student;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class StudentDTO extends Student {

    // 学院名 [间接 多表]
    private String deptName;

    public StudentDTO() {
    }

    public StudentDTO(Student student) {
        BeanUtils.copyProperties(student, this);
    }
}
