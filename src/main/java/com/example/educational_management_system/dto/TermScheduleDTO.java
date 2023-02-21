package com.example.educational_management_system.dto;

import com.example.educational_management_system.entity.TermSchedule;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class TermScheduleDTO extends TermSchedule {
    // 课名 [多表]
    private String courseName;

    // 学院名 [间接 多表]
    private String deptName;

    // 教师名 [多表]
    private String teacherName;

    public TermScheduleDTO() {
    }

    public TermScheduleDTO(TermSchedule termSchedule) {
        BeanUtils.copyProperties(termSchedule, this);
    }
}
