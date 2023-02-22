package com.example.educational_management_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.educational_management_system.dto.CourseDTO;
import com.example.educational_management_system.entity.Course;
import com.example.educational_management_system.entity.Dept;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {
    @Results(id = "withDept", value = {
            @Result(
                    column = "dept_id", property = "dept", javaType = Dept.class,
                    one = @One(select = "com.example.educational_management_system.mapper.DeptMapper.selectById")
            )
    })
    @Select("""
            select course.id, course_name, dept_id, hour, credit, is_deleted
            from course
            join dept on course.dept_id = dept.id
            """)
    IPage<CourseDTO> selectPageDTO(IPage<CourseDTO> iPage);

    @ResultMap(value = "withDept")
    @Select("""
            select course.id, course_name, dept_id, hour, credit, is_deleted
            from course
            join dept on course.dept_id = dept.id
            where course.id = #{id}
            """)
    CourseDTO selectByIdDTO(@Param("id") int id);
}
