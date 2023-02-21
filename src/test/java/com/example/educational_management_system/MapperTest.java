package com.example.educational_management_system;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.educational_management_system.dto.StudentDTO;
import com.example.educational_management_system.mapper.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MapperTest {
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private CourseMapper courseMapper;
    @Autowired
    private DeptMapper deptMapper;
    @Autowired
    private SelectCourseMapper selectCourseMapper;
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private TermScheduleMapper termScheduleMapper;

    @Test
    void mapperTest() {
        System.out.println(adminMapper.selectList(null));
        System.out.println(courseMapper.selectList(null));
        System.out.println(deptMapper.selectList(null));
        System.out.println(selectCourseMapper.selectList(null));
        System.out.println(studentMapper.selectList(null));
        System.out.println(teacherMapper.selectList(null));
        System.out.println(termScheduleMapper.selectList(null));
    }

    @Test
    void mapperTest2() {
        IPage<StudentDTO> page = studentMapper.selectPageDTO(new Page<>(1, 1));
        System.out.println(page.getRecords());

        StudentDTO studentDTO = studentMapper.selectByIdDTO(12347890);
        System.out.println(studentDTO);
    }
}
