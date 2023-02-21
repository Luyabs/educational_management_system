package com.example.educational_management_system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.educational_management_system.dto.CourseDTO;
import com.example.educational_management_system.entity.Course;

public interface CourseService extends IService<Course> {
    IPage<CourseDTO> getPage(int currentPage, int pageSize);

    CourseDTO getByIdDTO(int id);

    boolean save(Course course);

    boolean update(Course course);

    boolean delete(int id);
}
