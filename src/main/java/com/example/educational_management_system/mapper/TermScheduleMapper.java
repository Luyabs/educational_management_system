package com.example.educational_management_system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.example.educational_management_system.dto.CourseDTO;
import com.example.educational_management_system.dto.TermScheduleDTO;
import com.example.educational_management_system.entity.TermSchedule;
import org.apache.ibatis.annotations.*;

@Mapper
public interface TermScheduleMapper extends BaseMapper<TermSchedule> {
    @Results(id = "withCourseAndTeacher", value = {
            @Result(
                    column = "course_id", property = "course", javaType = CourseDTO.class,
                    one = @One(select = "com.example.educational_management_system.mapper.CourseMapper.selectByIdDTO")
            ),
            @Result(
                    column = "teacher_id", property = "teacher", javaType = CourseDTO.class,
                    one = @One(select = "com.example.educational_management_system.mapper.TeacherMapper.selectByIdDTO")
            )
    })
    @Select("""
            select term_schedule.id, term, course_id, teacher_id, time
            from term_schedule
            join teacher on teacher_id = teacher.id
            join course on course_id = course.id
            ${ew.customSqlSegment}
            """)
    IPage<TermScheduleDTO> selectPageDTO(IPage<TermScheduleDTO> iPage, @Param(Constants.WRAPPER) LambdaQueryWrapper<TermSchedule> wrapper);


    @ResultMap(value = "withCourseAndTeacher")
    @Select("""
            select term_schedule.id, term, course_id, teacher_id, time
            from term_schedule
            join teacher on teacher_id = teacher.id
            join course on course_id = course.id
            where term_schedule.id = #{id}
            """)
    TermScheduleDTO selectByIdDTO(@Param("id") int id);
}
