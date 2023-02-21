package com.example.educational_management_system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.educational_management_system.common.KeyCheck;
import com.example.educational_management_system.common.exception.ServiceException;
import com.example.educational_management_system.dto.CourseDTO;
import com.example.educational_management_system.entity.Course;
import com.example.educational_management_system.mapper.CourseMapper;
import com.example.educational_management_system.mapper.DeptMapper;
import com.example.educational_management_system.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.educational_management_system.common.KeyCheck.checkCourse;

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
        return courseMapper.selectPageDTO(new Page<>(currentPage, pageSize));
    }

    /**
     * 按id查询
     */
    @Override
    public CourseDTO getByIdDTO(int id) {
        CourseDTO courseDTO = courseMapper.selectByIdDTO(id);
        if (courseDTO == null)
            throw new ServiceException("没有id=" + id + "的课程");
        return courseDTO;
    }

    /**
     * 新增教师
     */
    @Override
    public boolean save(Course course) {
        KeyCheck.checkDept(course.getDeptId());
        return courseMapper.insert(course) > 0;
    }

    /**
     * 修改课程
     */
    @Override
    public boolean update(Course course) {
        Course temp = checkCourse(course.getId());
        if (course.getDeptId() != null)
            KeyCheck.checkDept(temp.getDeptId());
        if (temp.getIsDeleted() == 1)
            throw new ServiceException("该课程已处于被删状态");
        course.setIsDeleted(null);  //禁止修改删除位
        return courseMapper.updateById(course) > 0;
    }

    /**
     * 删除课程
     */
    @Override
    public boolean delete(int id) {
        Course course = KeyCheck.checkCourse(id);
        if (course.getIsDeleted() == 1)
            throw new ServiceException("该课程已处于被删状态");
        course.setIsDeleted(1);
        return courseMapper.updateById(course) > 0;
    }
}
