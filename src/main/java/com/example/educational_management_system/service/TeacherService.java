package com.example.educational_management_system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.educational_management_system.dto.TeacherDTO;
import com.example.educational_management_system.entity.Teacher;

import java.util.Map;

public interface TeacherService extends IService<Teacher> {
    String login(Teacher teacher);

    Map<String, Object> decodeToken(String token);

    IPage<TeacherDTO> getPage(int currentPage, int pageSize);

    TeacherDTO getByIdDTO(int id);

    boolean saveDTO(TeacherDTO teacherDTO);

    boolean updateDTO(TeacherDTO teacherDTO);

    boolean updateStatus(TeacherDTO teacherDTO);
}
