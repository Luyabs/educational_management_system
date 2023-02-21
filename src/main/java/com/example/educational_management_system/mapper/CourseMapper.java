package com.example.educational_management_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.educational_management_system.dto.CourseDTO;
import com.example.educational_management_system.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {
    @Select("select course.id, course_name, dept_name, hour, credit\n" +
            "from course join dept on course.dept_id = dept.id\n" +
            "where is_deleted = 0")
    IPage<CourseDTO> selectPageDTO(IPage<CourseDTO> iPage);
}
