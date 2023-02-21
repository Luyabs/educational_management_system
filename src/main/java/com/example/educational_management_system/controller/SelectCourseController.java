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
     */
    @GetMapping("/page")
    public Result getPage(int currentPage, int pageSize) {
        IPage<SelectCourseDTO> page = selectCourseService.getPage(currentPage, pageSize);
        return Result.success().data("page", page);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable int id) {
        SelectCourseDTO selectCourse = selectCourseService.getByIdDTO(id);
        return Result.success().data("selectedCourse", selectCourse);
    }

    /**
     * 选课
     * @param studentId 学号
     * @param termScheduleId 开课号
     */
    @PostMapping("/{student_id}/{term_schedule_id}")
    public Result chooseCourse(@PathVariable(name = "student_id") int studentId, @PathVariable(name = "term_schedule_id") int termScheduleId) {
        boolean flag = selectCourseService.chooseCourse(studentId, termScheduleId);
        return flag ? Result.success().message("选课成功") : Result.error().message("选课失败");
    }

    /**
     * 退课
     * @param id 选课记录id 即SelectCourse的主键
     */
    @DeleteMapping("/{select_course_id}")
    public Result giveUpSelectCourse(@PathVariable(name = "select_course_id") int id) {
        boolean flag = selectCourseService.giveUpCourse(id);
        return flag ? Result.success().message("退课成功") : Result.error().message("退课失败");
    }

    /**
     * 获取学号为student_id的学生的所有选课信息与成绩
     * @Param studentId 学号
     */
    @GetMapping("/course/{student_id}")
    public Result getOneStudentCourses(@PathVariable(name = "student_id") int studentId) {
        List<SelectCourseDTO> courses = selectCourseService.getOnesAllCoursesDTO(studentId);
        return Result.success().data("selectedCourses", courses);
    }

    /**
     * 获取班上所有学生的选课-成绩
     * @Param termScheduleId 开课号
     */
    @GetMapping("/class/{term_schedule_id}")
    public Result getOneClassCourses(@PathVariable(name = "term_schedule_id") int termScheduleId) {
        List<SelectCourseDTO> courses = selectCourseService.getClassAllCoursesDTO(termScheduleId);
        return Result.success().data("selectedCourses", courses);
    }

    /**
     * 登分
     * @param selectCourse 需传入主键 id
     */
    @PutMapping("/score")
    public Result updateScore(@RequestBody SelectCourse selectCourse) {
        boolean flag = selectCourseService.updateScore(selectCourse);
        return flag ? Result.success().message("登分成功") : Result.error().message("登分失败");
    }

    // TODO: 成绩统计

}
