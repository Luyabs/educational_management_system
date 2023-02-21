package com.example.educational_management_system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.educational_management_system.dto.StudentDTO;
import com.example.educational_management_system.entity.Student;

import java.util.Map;

public interface StudentService extends IService<Student> {
    String login(Student student);

    Map<String, Object> decodeToken(String token);

    IPage<StudentDTO> getPage(int currentPage, int pageSize);

    StudentDTO getByIdDTO(int id);

    boolean saveDTO(StudentDTO studentDTO);

    boolean updateDTO(StudentDTO studentDTO);

    boolean updateStatus(StudentDTO studentDTO);
}
