package com.example.educational_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.educational_management_system.common.ServiceException;
import com.example.educational_management_system.dto.SelectCourseDTO;
import com.example.educational_management_system.dto.TermScheduleDTO;
import com.example.educational_management_system.entity.*;
import com.example.educational_management_system.mapper.*;
import com.example.educational_management_system.service.SelectCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SelectCourseServiceImpl extends ServiceImpl<SelectCourseMapper, SelectCourse> implements SelectCourseService {
    @Autowired
    private SelectCourseMapper selectCourseMapper;

    @Autowired
    private TermScheduleMapper termScheduleMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private DeptMapper deptMapper;

    /**
     * 分页查询
     */
    @Override
    public IPage<SelectCourseDTO> getPage(int currentPage, int pageSize) {
        // 查 selectCourses
        List<SelectCourseDTO> selectCourseDTOS = new ArrayList<>();
        IPage<SelectCourse> page = selectCourseMapper.selectPage(new Page<>(currentPage, pageSize), null);
        List<SelectCourse> selectCourses = page.getRecords();

        // 对应组装 List<DTO>
        for (SelectCourse selectCourse : selectCourses) {
            // 下面封装selectCourseDTO对象
            Student student = studentMapper.selectById(selectCourse.getStudentId());    // 直接外键

            // 下面封装TermScheduleDTO对象
            TermSchedule termSchedule = termScheduleMapper.selectById(selectCourse.getTermScheduleId());    // 直接外键

            Course course = courseMapper.selectById(termSchedule.getCourseId());    // 间接外键
            Dept dept = deptMapper.selectById(course.getDeptId());  // 间接2次外键
            Teacher teacher = teacherMapper.selectById(termSchedule.getTeacherId());    // 间接外键

            TermScheduleDTO termScheduleDTO = new TermScheduleDTO(termSchedule);
            termScheduleDTO.setCourseName(course.getCourseName());
            termScheduleDTO.setTeacherName(teacher.getRealName());
            termScheduleDTO.setDeptName(dept.getDeptName());
            // 封装完成

            SelectCourseDTO selectCourseDTO = new SelectCourseDTO(selectCourse);
            selectCourseDTO.setTermSchedule(termScheduleDTO);
            selectCourseDTO.setStudentName(student.getRealName());
            // 封装完成
            selectCourseDTOS.add(selectCourseDTO);
        }
        // 组装成Ipage
        IPage<SelectCourseDTO> pageDTO = new Page<>(currentPage, pageSize);
        pageDTO.setRecords(selectCourseDTOS);
        pageDTO.setTotal(page.getTotal());
        pageDTO.setPages(page.getPages());
        return pageDTO;
    }

    /**
     * 按id查选课-成绩
     */
    @Override
    public SelectCourseDTO getByIdDTO(int id) {
        SelectCourse selectCourse = selectCourseMapper.selectById(id);
        if (selectCourse == null)
            throw new ServiceException("id为" + id + "的选课记录不存在");

        Student student = studentMapper.selectById(selectCourse.getStudentId());    // 直接外键

        // 依次展开外键
        TermSchedule termSchedule = termScheduleMapper.selectById(selectCourse.getTermScheduleId());    // 直接外键
        TermScheduleDTO termScheduleDTO = new TermScheduleDTO(termSchedule);

        Course course = courseMapper.selectById(termSchedule.getCourseId());    // 间接外键
        Dept dept = deptMapper.selectById(course.getDeptId());  // 间接2次外键
        Teacher teacher = teacherMapper.selectById(termSchedule.getTeacherId());    // 间接外键

        termScheduleDTO.setCourseName(course.getCourseName());
        termScheduleDTO.setDeptName(dept.getDeptName());
        termScheduleDTO.setTeacherName(teacher.getRealName());

        // 封装数据
        SelectCourseDTO selectCourseDTO = new SelectCourseDTO(selectCourse);
        selectCourseDTO.setStudentName(student.getRealName());
        selectCourseDTO.setTermSchedule(termScheduleDTO);
        return selectCourseDTO;
    }

    /**
     * 选课
     */
    @Override
    public boolean selectCourse(int studentId, int termScheduleId) {
        Student student = studentMapper.selectById(studentId);
        TermSchedule termSchedule = termScheduleMapper.selectById(termScheduleId);
        // 学号或开课号不存在
        if (student == null)
            throw new ServiceException("学生不存在");
        if (termSchedule == null)
            throw new ServiceException("课程不存在/未开设");
        // 选课时间冲突
        if (isTimeConflict(studentId, termSchedule))
            throw new ServiceException("选课时间冲突");
        // 学生状态非正常
        if (student.getStatus() != 1)
            throw new ServiceException("学生不处于正常状态");
        // 可选
        SelectCourse selectCourse = new SelectCourse();
        selectCourse.setStudentId(studentId);
        selectCourse.setTermScheduleId(termScheduleId);
        return selectCourseMapper.insert(selectCourse) > 0;
    }

    /**
     * 获取id=studentId学生所选的所有课程DTO
     */
    @Override
    public List<SelectCourseDTO> getOnesCoursesDTO(int studentId) {
        List<SelectCourse> selectCourses = getOnesCourses(studentId);
        List<SelectCourseDTO> selectCourseDTOS = new ArrayList<>();
        for (SelectCourse selectCourse : selectCourses) {       // 依次加载成DTO
            SelectCourseDTO selectCourseDTO = getByIdDTO(selectCourse.getId());
            selectCourseDTOS.add(selectCourseDTO);
        }
        return selectCourseDTOS;
    }


    /**
     * 登分
     */
    @Override
    public boolean updateScore(SelectCourse selectCourse) {
        SelectCourse temp = getById(selectCourse.getId());
        temp.setScoreUsual(selectCourse.getScoreUsual());
        temp.setScoreExam(selectCourse.getScoreExam());
        temp.setScoreTotal(selectCourse.getScoreTotal());
        return selectCourseMapper.updateById(temp) > 0;
    }

    /**
     * 退课
     */
    @Override
    public boolean giveUpCourse(int studentId, int termScheduleId) {
        LambdaQueryWrapper<SelectCourse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SelectCourse::getStudentId, studentId);
        wrapper.eq(SelectCourse::getTermScheduleId, termScheduleId);
        SelectCourse temp = getOne(wrapper);
        if (temp == null)
            throw new ServiceException("学生没有选此课");
        // 没有登成绩则可退课
        if (temp.getScoreUsual() != null || temp.getScoreExam() != null || temp.getScoreTotal() != null)
            throw new ServiceException("此课已登分, 不可退课");
        return selectCourseMapper.deleteById(temp.getId()) > 0;
    }


    /**
     * 获取班上所有学生的选课-成绩信息
     */
    @Override
    public List<SelectCourseDTO> getClassCourses(int termScheduleId) {
        if (selectCourseMapper.selectById(termScheduleId) == null)
            throw new ServiceException("课程不存在/未开设");
        LambdaQueryWrapper<SelectCourse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SelectCourse::getTermScheduleId, termScheduleId);
        List<SelectCourse> selectCourses = selectCourseMapper.selectList(wrapper);  // 先查出Entity

        List<SelectCourseDTO> resultList = new ArrayList<>();
        for (SelectCourse selectCourse : selectCourses) {
            resultList.add(getByIdDTO(selectCourse.getId()));
        }
        return resultList;
    }

    /**
     * 获取id=studentId学生所选的所有课程
     */
    private List<SelectCourse> getOnesCourses(int studentId) {
        if (studentMapper.selectById(studentId) == null)
            throw new ServiceException("id为" + studentId + "的学生不存在");
        LambdaQueryWrapper<SelectCourse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SelectCourse::getStudentId, studentId);
        List<SelectCourse> selectCourses = selectCourseMapper.selectList(wrapper);  //找到该学生选的所有课
        return selectCourses;
    }


    /**
     * 检测是否学生选课时间冲突
     */
    private boolean isTimeConflict(int studentId, TermSchedule termSchedule) {
        List<SelectCourse> selectCourses = getOnesCourses(studentId);

        for (SelectCourse selectCourse : selectCourses) {   //遍历这名学生选的所有课 如果选课时间则返回true
            TermSchedule temp = termScheduleMapper.selectById(selectCourse.getTermScheduleId());
            String[] tempTime = temp.getTime().split(" ");
            String[] tarTime = termSchedule.getTime().split(" ");
            for (int i = 0; i < tempTime.length; i++) {
                for (int j = 0; j < tarTime.length; j++) {
                    if (tempTime[i].charAt(2) == tarTime[j].charAt(2)) {    //如果星期相同 继续判断
                        if (tempTime[i].charAt(5) > tarTime[j].charAt(3) && tempTime[i].charAt(3) < tarTime[j].charAt(5))
                            return true;
                    }
                }
            }
        }
        return false;
    }

}
