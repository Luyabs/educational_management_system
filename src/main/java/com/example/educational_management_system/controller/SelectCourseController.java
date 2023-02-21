package com.example.educational_management_system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.educational_management_system.common.Result;
import com.example.educational_management_system.dto.SelectCourseDTO;
import com.example.educational_management_system.entity.SelectCourse;
import com.example.educational_management_system.service.SelectCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/select_course")
public class SelectCourseController {
    @Autowired
    private SelectCourseService selectCourseService;

    /**
     * 分页获取总选课-成绩记录
     * @return page
     */
    @GetMapping("/page")
    public Result getPage(int currentPage, int pageSize) {
        IPage<SelectCourseDTO> page = selectCourseService.getPage(currentPage, pageSize);
        return Result.success().data("page", page);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable int id) {
        SelectCourseDTO selectCourse = selectCourseService.getByIdDTO(id);
        return Result.success().data("selectCourse", selectCourse);
    }

    /**
     * 获取学号为student_id的学生的所有选课信息与成绩
     */
    @GetMapping("/course/{student_id}")
    public Result getOneStudentCourse(@PathVariable(name = "student_id") int studentId) {
        List<SelectCourseDTO> courses = selectCourseService.getOnesCoursesDTO(studentId);
        return Result.success().data("courses", courses);
    }

    /**
     * 选课
     * @param studentId 学生id
     * @param termScheduleId 开课表id
     */
    @PostMapping("/{student_id}/{term_schedule_id}")
    public Result selectCourse(@PathVariable(name = "student_id") int studentId, @PathVariable(name = "term_schedule_id") int termScheduleId) {
        boolean flag = selectCourseService.selectCourse(studentId, termScheduleId);
        return flag ? Result.success().message("选课成功") : Result.error().message("选课失败");
    }

    /**
     * 登分
     * @param selectCourse 需传入主键 id 指定对象
     */
    @PutMapping("/score")
    public Result updateScore(@RequestBody SelectCourse selectCourse) {
        boolean flag = selectCourseService.updateScore(selectCourse);
        return flag ? Result.success().message("登分成功") : Result.error().message("登分失败");
    }

    /**
     * 退课
     * @param studentId 学生id
     * @param termScheduleId 开课表id
     * @return
     */
    @DeleteMapping("/{student_id}/{term_schedule_id}")
    public Result giveUpSelectCourse(@PathVariable(name = "student_id") int studentId, @PathVariable(name = "term_schedule_id") int termScheduleId) {
        boolean flag = selectCourseService.giveUpCourse(studentId, termScheduleId);
        return flag ? Result.success().message("退课成功") : Result.error().message("退课失败");
    }

    /**
     * 获取班上所有学生的选课-成绩
     */
    @GetMapping("/class/{term_schedule_id}")
    public Result getOneClassCourse(@PathVariable(name = "term_schedule_id") int termScheduleId) {
        List<SelectCourseDTO> courses = selectCourseService.getClassCourses(termScheduleId);
        return Result.success().data("courses", courses);
    }
}
