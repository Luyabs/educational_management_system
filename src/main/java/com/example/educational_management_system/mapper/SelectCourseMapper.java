package com.example.educational_management_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.educational_management_system.dto.SelectCourseDTO;
import com.example.educational_management_system.dto.StudentDTO;
import com.example.educational_management_system.dto.TermScheduleDTO;
import com.example.educational_management_system.entity.SelectCourse;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Mapper
public interface SelectCourseMapper extends BaseMapper<SelectCourse> {
    @Results(id = "withStudentAndTermSchedule", value = {
            @Result(
                    column = "student_id", property = "student", javaType = StudentDTO.class,
                    one = @One(select = "com.example.educational_management_system.mapper.StudentMapper.selectByIdDTO")
            ),
            @Result(
                    column = "term_schedule_id", property = "termSchedule", javaType = TermScheduleDTO.class,
                    one = @One(select = "com.example.educational_management_system.mapper.TermScheduleMapper.selectByIdDTO")
            )
    })
    @Select("""
            select select_course.id, student_id, term_schedule_id, score_usual, score_exam, score_total
            from select_course
            join student on student_id = student.id
            join term_schedule on term_schedule_id = term_schedule.id
            """)
    IPage<SelectCourseDTO> selectPageDTO(Page<Object> objectPage);


    @ResultMap(value = "withStudentAndTermSchedule")
    @Select("""
            select select_course.id, student_id, term_schedule_id, score_usual, score_exam, score_total
            from select_course
            join student on student_id = student.id
            join term_schedule on term_schedule_id = term_schedule.id
            where select_course.id = #{id}
            """)
    SelectCourseDTO selectByIdDTO(@RequestParam("id") int id);

    @Results(id = "withTermSchedule", value = {
            @Result(
                    column = "term_schedule_id", property = "termSchedule", javaType = TermScheduleDTO.class,
                    one = @One(select = "com.example.educational_management_system.mapper.TermScheduleMapper.selectByIdDTO")
            )
    })
    @Select("""
            select select_course.id, student_id, term_schedule_id, score_usual, score_exam, score_total
            from select_course
            join term_schedule on term_schedule_id = term_schedule.id
            where student_id = #{student_id}
            """)
    List<SelectCourseDTO> selectListByStudentIdDTO(@RequestParam("student_id") int studentId);    // 获取某人所选课程的合集

    @Results(id = "withStudent", value = {
            @Result(
                    column = "student_id", property = "student", javaType = StudentDTO.class,
                    one = @One(select = "com.example.educational_management_system.mapper.StudentMapper.selectByIdDTO")
            )
    })
    @Select("""
            select select_course.id, student_id, term_schedule_id, score_usual, score_exam, score_total
            from select_course
            join student on student_id = student.id
            where term_schedule_id = #{term_schedule_id}
            """)
    List<SelectCourseDTO> selectListByTermScheduleIdDTO(@RequestParam("term_schedule_id") int termScheduleId);  // 获取某门课下所有人信息合集

    @Select("""
            select term_schedule.time
            from select_course
            join term_schedule on term_schedule_id = term_schedule.id
            where student_id = #{student_id}
            """)
    List<String> getOnesAllCoursesTimeList(@RequestParam("student_id")int studentId);    //获取某人选课时间的时间表
}
