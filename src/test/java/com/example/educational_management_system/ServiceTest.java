package com.example.educational_management_system;

import com.example.educational_management_system.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceTest {
    @Autowired
    private AdminService adminService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private SelectCourseService selectCourseService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private TermScheduleService termScheduleService;



    @Test
    void serviceTest() {
//        adminService.list();
//        courseService.list();
//        deptService.list();
//        selectCourseService.list();
        studentService.list();
        teacherService.list();
        termScheduleService.list();
    }
}
