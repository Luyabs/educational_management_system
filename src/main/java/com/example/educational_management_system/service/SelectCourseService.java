package com.example.educational_management_system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.educational_management_system.dto.SelectCourseDTO;
import com.example.educational_management_system.entity.SelectCourse;

import java.util.List;

public interface SelectCourseService extends IService<SelectCourse> {

    IPage<SelectCourseDTO> getPage(int currentPage, int pageSize);

    SelectCourseDTO getByIdDTO(int id);

    boolean chooseCourse(int studentId, int termScheduleId);

    boolean giveUpCourse(int id);

    List<SelectCourseDTO> getOnesAllCoursesDTO(int studentId);

    List<SelectCourseDTO> getClassAllCoursesDTO(int termScheduleId);

    boolean updateScore(SelectCourse selectCourse);




}
