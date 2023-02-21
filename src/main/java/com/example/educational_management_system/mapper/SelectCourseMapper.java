package com.example.educational_management_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.educational_management_system.entity.Dept;
import com.example.educational_management_system.entity.SelectCourse;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SelectCourseMapper extends BaseMapper<SelectCourse> {
}
