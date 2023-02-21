package com.example.educational_management_system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.educational_management_system.dto.SelectCourseDTO;
import com.example.educational_management_system.entity.SelectCourse;

import java.util.List;

public interface SelectCourseService extends IService<SelectCourse> {

    IPage<SelectCourseDTO> getPage(int currentPage, int pageSize);

    SelectCourseDTO getByIdDTO(int id);

    boolean selectCourse(int studentId, int termScheduleId);

    List<SelectCourseDTO> getOnesCoursesDTO(int studentId);

    boolean updateScore(SelectCourse selectCourse);

    boolean giveUpCourse(int studentId, int termScheduleId);

    List<SelectCourseDTO> getClassCourses(int termScheduleId);
}
