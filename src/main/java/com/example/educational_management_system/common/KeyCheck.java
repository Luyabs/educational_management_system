package com.example.educational_management_system.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.educational_management_system.common.exception.ForeignKeyException;
import com.example.educational_management_system.common.exception.ServiceException;
import com.example.educational_management_system.entity.*;
import com.example.educational_management_system.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KeyCheck {

    private static DeptMapper deptMapper;
    private static StudentMapper studentMapper;
    private static TeacherMapper teacherMapper;
    private static CourseMapper courseMapper;
    private static TermScheduleMapper termScheduleMapper;
    private static SelectCourseMapper selectCourseMapper;

    @Autowired
    public KeyCheck(DeptMapper deptMapper, StudentMapper studentMapper, TeacherMapper teacherMapper, CourseMapper courseMapper, TermScheduleMapper termScheduleMapper, SelectCourseMapper selectCourseMapper) {
        KeyCheck.deptMapper = deptMapper;
        KeyCheck.studentMapper = studentMapper;
        KeyCheck.teacherMapper = teacherMapper;
        KeyCheck.courseMapper = courseMapper;
        KeyCheck.termScheduleMapper = termScheduleMapper;
        KeyCheck.selectCourseMapper = selectCourseMapper;
    }

    public static Dept checkDept(int id) {
        Dept dept = deptMapper.selectById(id);
        if (dept == null)
            throw new ForeignKeyException("不存在id=" + id + "的学院");
        return dept;
    }

    public static Student checkStudent(int id) {
        Student student = studentMapper.selectById(id);
        if (student == null)
            throw new ForeignKeyException("不存在id=" + id + "的学生");
        return student;
    }

    public static Teacher checkTeacher(int id) {
        Teacher teacher = teacherMapper.selectById(id);
        if (teacher == null)
            throw new ForeignKeyException("不存在id=" + id + "的教师");
        return teacher;
    }

    public static Course checkCourse(int id) {
        Course course = courseMapper.selectById(id);
        if (course == null)
            throw new ForeignKeyException("不存在id=" + id + "的课程");
        return course;
    }

    public static TermSchedule checkTermSchedule(int id) {
        TermSchedule termSchedule = termScheduleMapper.selectById(id);
        if (termSchedule == null)
            throw new ForeignKeyException("不存在或未开设id=" + id + "的课程");
        return termSchedule;
    }

    public static SelectCourse checkSelectCourse(int id) {
        SelectCourse selectCourse = selectCourseMapper.selectById(id);
        if (selectCourse == null)
            throw new ForeignKeyException("不存在id=" + id + "的选课记录");
        return selectCourse;
    }

    /**
     * 检查TermSchedule表逻辑主键  term + courseId + teacherId
     */
    public static void primaryCheckTermSchedule(TermSchedule termSchedule) {
        LambdaQueryWrapper<TermSchedule> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TermSchedule::getTerm, termSchedule.getTerm());
        wrapper.eq(TermSchedule::getCourseId, termSchedule.getCourseId());
        wrapper.eq(TermSchedule::getTeacherId, termSchedule.getTeacherId());
        if (termScheduleMapper.exists(wrapper))  //主键重复
            throw new ServiceException("该term, course_id, teacher_id组合在表中已存在" );
    }

    /**
     * 检查TermSchedule表逻辑主键  studentId + termScheduleId  [实际上由isTimeConflict已确保termScheduleId不会重复]
     */
    public static void primaryCheckSelectCourse(SelectCourse selectCourse) {
        LambdaQueryWrapper<SelectCourse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SelectCourse::getStudentId, selectCourse.getStudentId());
        wrapper.eq(SelectCourse::getTermScheduleId, selectCourse.getTermScheduleId());
        if (selectCourseMapper.exists(wrapper))  //主键重复
            throw new ServiceException("该student_id, term_schedule_id组合在表中已存在" );
    }

}
