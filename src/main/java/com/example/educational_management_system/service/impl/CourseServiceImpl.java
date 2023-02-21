package com.example.educational_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.educational_management_system.common.ServiceException;
import com.example.educational_management_system.dto.CourseDTO;
import com.example.educational_management_system.entity.Course;
import com.example.educational_management_system.entity.Dept;
import com.example.educational_management_system.mapper.CourseMapper;
import com.example.educational_management_system.mapper.DeptMapper;
import com.example.educational_management_system.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {
    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private DeptMapper deptMapper;

    /**
     * 分页查询
     */
    public IPage<CourseDTO> getPage(int currentPage, int pageSize) {
        // course - dept
        List<CourseDTO> courseDTOS = new ArrayList<>();
        // 找未被删除的课程 course.id_deleted = 0
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Course::getIsDeleted, 0);

        IPage<Course> page = courseMapper.selectPage(new Page<>(currentPage, pageSize), wrapper);
        List<Course> courses = page.getRecords();
        // 对应组装 List<DTO>
        for (Course course : courses) {
            Dept dept = deptMapper.selectById(course.getDeptId());
            CourseDTO courseDTO = new CourseDTO(course);
            courseDTO.setDeptName(dept.getDeptName());
            courseDTOS.add(courseDTO);
        }
        // 组装成 Page<DTO>
        IPage<CourseDTO> pageDTO = new Page<>(currentPage, pageSize);
        pageDTO.setRecords(courseDTOS);
        pageDTO.setTotal(page.getTotal());
        pageDTO.setPages(page.getPages());
        return pageDTO;
    }

    /**
     * 按id查询
     */
    @Override
    public CourseDTO getByIdDTO(int id) {
        Course course = courseMapper.selectById(id);
        if (course == null)
            throw new ServiceException("id=" + id + "的课程不存在");
        Dept dept = deptMapper.selectById(course.getDeptId());
        CourseDTO courseDTO = new CourseDTO(course);
        courseDTO.setDeptName(dept.getDeptName());
        return courseDTO;
    }

    /**
     * 新增教师
     */
    @Override
    public boolean saveDTO(CourseDTO courseDTO) {
        // 通过dept.deptName 替换dept_id
        setDeptName(courseDTO);

        return courseMapper.insert(courseDTO) > 0;
    }

    /**
     * 修改教师
     */
    @Override
    public boolean updateDTO(CourseDTO courseDTO) {
        // 去除掉本不该出现的dept_id 与 is_deleted
        courseDTO.setDeptId(null);
        courseDTO.setIsDeleted(null);

        // 如果有修改dept.deptName 则通过dept.deptName 替换dept_id
        if (courseDTO.getDeptName() != null) {
            setDeptName(courseDTO);
        }
        return courseMapper.updateById(courseDTO) > 0;
    }

    /**
     * 删除课程
     */
    @Override
    public boolean delete(int id) {
        Course course = courseMapper.selectById(id);
        if (course == null) // 课程不存在
            throw new ServiceException("id=" + id + "的课程不存在");
        course.setIsDeleted(1);
        return courseMapper.updateById(course) > 0;
    }

    /**
     * 检查是否真实存在部门 并赋值部门id
     */
    private void setDeptName(CourseDTO courseDTO) {
        LambdaQueryWrapper<Dept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dept::getDeptName, courseDTO.getDeptName());
        Dept dept = deptMapper.selectOne(wrapper);
        if (dept == null)   // 院系不存在
            throw new ServiceException("院系不存在");
        courseDTO.setDeptId(dept.getId());
    }
}
