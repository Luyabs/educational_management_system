package com.example.educational_management_system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.educational_management_system.common.Result;
import com.example.educational_management_system.dto.CourseDTO;
import com.example.educational_management_system.entity.Course;
import com.example.educational_management_system.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService courseService;

    /**
     * 分页获取课程
     * @return page
     */
    @GetMapping("/page")
    public Result getPage(int currentPage, int pageSize) {
        IPage<CourseDTO> page = courseService.getPage(currentPage, pageSize);
        return Result.success().data("page", page);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable int id) {
        CourseDTO course = courseService.getByIdDTO(id);
        return Result.success().data("course", course);
    }

    /**
     * 添加课程
     * @param course
     */
    @PostMapping
    public Result add(@RequestBody Course course) {
        boolean flag = courseService.save(course);
        return flag ? Result.success().message("增加成功") : Result.error().message("增加失败");
    }

    /**
     * 修改课程 [不修改 isDeleted]
     * @param course 需要传入id
     */
    @PutMapping
    public Result edit(@RequestBody Course course) {
        boolean flag = courseService.update(course);
        return flag ? Result.success().message("修改成功") : Result.error().message("修改失败");
    }

    /**
     * 删除课程
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable int id) {
        boolean flag = courseService.delete(id);
        return flag ? Result.success().message("课程删除成功") : Result.error().message("课程删除失败");
    }
}
