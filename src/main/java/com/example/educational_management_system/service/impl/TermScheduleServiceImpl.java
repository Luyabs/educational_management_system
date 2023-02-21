package com.example.educational_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.educational_management_system.common.ServiceException;
import com.example.educational_management_system.dto.TermScheduleDTO;
import com.example.educational_management_system.entity.*;
import com.example.educational_management_system.mapper.*;
import com.example.educational_management_system.service.TermScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TermScheduleServiceImpl extends ServiceImpl<TermScheduleMapper, TermSchedule> implements TermScheduleService {
    @Autowired
    private TermScheduleMapper termScheduleMapper;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private SelectCourseMapper selectCourseMapper;

    /**
     * 分页查询
     */
    public IPage<TermScheduleDTO> getPage(int currentPage, int pageSize) {
        // 查termSchedule - dept - teacher
        List<TermScheduleDTO> termScheduleDTOS = new ArrayList<>();
        IPage<TermSchedule> page = termScheduleMapper.selectPage(new Page<>(currentPage, pageSize), null);
        List<TermSchedule> termSchedules = page.getRecords();
        // 对应组装 List<DTO>
        for (TermSchedule termSchedule : termSchedules) {
            Course course = courseMapper.selectById(termSchedule.getCourseId());
            Dept dept = deptMapper.selectById(course.getDeptId());
            Teacher teacher = teacherMapper.selectById(termSchedule.getTeacherId());

            TermScheduleDTO termScheduleDTO = new TermScheduleDTO(termSchedule);
            termScheduleDTO.setCourseName(course.getCourseName());
            termScheduleDTO.setDeptName(dept.getDeptName());
            termScheduleDTO.setTeacherName(teacher.getRealName());
            termScheduleDTOS.add(termScheduleDTO);
        }
        // 组装成 Page<DTO>
        IPage<TermScheduleDTO> pageDTO = new Page<>(currentPage, pageSize);
        pageDTO.setRecords(termScheduleDTOS);
        pageDTO.setTotal(page.getTotal());
        pageDTO.setPages(page.getPages());
        return pageDTO;
    }

    /**
     * 按id查课
     */
    @Override
    public TermScheduleDTO getByIdDTO(int id) {
        TermSchedule termSchedule = termScheduleMapper.selectById(id);
        if (termSchedule == null)
            throw new ServiceException("id=" + id + "的课程未开设");

        Course course = courseMapper.selectById(termSchedule.getCourseId());
        Dept dept = deptMapper.selectById(course.getDeptId());
        Teacher teacher = teacherMapper.selectById(termSchedule.getTeacherId());

        TermScheduleDTO termScheduleDTO = new TermScheduleDTO(termSchedule);
        termScheduleDTO.setCourseName(course.getCourseName());
        termScheduleDTO.setDeptName(dept.getDeptName());
        termScheduleDTO.setTeacherName(teacher.getRealName());
        return termScheduleDTO;
    }

    /**
     * 新开课
     */
    @Override
    public boolean saveDTO(TermScheduleDTO termScheduleDTO) {
        // 通过name 替换课程与教师id
        setCourseName(termScheduleDTO);
        setTeacherName(termScheduleDTO);
        return termScheduleMapper.insert(termScheduleDTO) > 0;
    }


    /**
     * 修改开课
     */
    @Override
    public boolean updateDTO(TermScheduleDTO termScheduleDTO) {
        termScheduleDTO.setCourseId(null);
        termScheduleDTO.setTeacherId(null);

        // 如果有修改课程名 则通过course.courseName 替换course_id
        if (termScheduleDTO.getCourseName() != null) {
            setCourseName(termScheduleDTO);
        }
        if (termScheduleDTO.getTeacherName() != null) {
            setTeacherName(termScheduleDTO);
        }
        return termScheduleMapper.updateById(termScheduleDTO) > 0;
    }


    /**
     * 删除开课
     * @param id
     */
    @Override
    public boolean delete(int id) {
        LambdaQueryWrapper<SelectCourse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SelectCourse::getTermScheduleId, id);
        if (selectCourseMapper.exists(wrapper)) // SelectCourse的外键约束
            throw new ServiceException("课程已有学生选/过去曾开设过 不可删除");
        return termScheduleMapper.deleteById(id) > 0;
    }

    /**
     * 检查是否真实存在课程名 并赋值课程id
     */
    private void setCourseName(TermScheduleDTO termScheduleDTO) {
        // check course + set courseId
        LambdaQueryWrapper<Course> courseWrapper = new LambdaQueryWrapper<>();
        courseWrapper.eq(Course::getCourseName, termScheduleDTO.getCourseName());
        Course course = courseMapper.selectOne(courseWrapper);
        if (course == null)   // 课程不存在或被删除
            throw new ServiceException("课程不存在");
        if (course.getIsDeleted() == 1)
            throw new ServiceException("课程已被删除");
        termScheduleDTO.setCourseId(course.getId());

    }

    /**
     * 检查是否真实存在教师 并赋值教师id
     */
    private void setTeacherName(TermScheduleDTO termScheduleDTO) {
        // check teacher + set teacherId
        LambdaQueryWrapper<Teacher> teacherWrapper = new LambdaQueryWrapper<>();
        teacherWrapper.eq(Teacher::getRealName, termScheduleDTO.getTeacherName());
        Teacher teacher = teacherMapper.selectOne(teacherWrapper);
        if (teacher == null)   // 教师状态非正常
            throw new ServiceException("教师不存在");
        if (teacher.getStatus() != 1)
            throw new ServiceException("教师处于非正常状态");
        termScheduleDTO.setTeacherId(teacher.getId());
    }
}
