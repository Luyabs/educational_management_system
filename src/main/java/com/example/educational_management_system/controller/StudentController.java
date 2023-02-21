package com.example.educational_management_system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.educational_management_system.common.Result;
import com.example.educational_management_system.dto.StudentDTO;
import com.example.educational_management_system.entity.Student;
import com.example.educational_management_system.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    /**
     * 登录
     * @param student [username, password]
     */
    @PostMapping("/login")
    public Result login(@RequestBody Student student) {
        // 找到目标
        String token = studentService.login(student);
        return token != null ? Result.success().data("token", token) : Result.error().message("用户名或密码错误");
    }

    /**
     * 登出
     */
    @PostMapping("/logout")
    public Result logout() {
        return Result.success();
    }

    /**
     * 解析 token => id
     * @param token
     * @return username + 头像
     */
    @GetMapping("/info")
    public Result info(String token){
        Map<String, Object> map = studentService.decodeToken(token);
        return Result.success().data(map);
    }

    /**
     * 分页获取学生
     * @return page
     */
    @GetMapping("/page")
    public Result getPage(int currentPage, int pageSize) {
        IPage<StudentDTO> page = studentService.getPage(currentPage, pageSize);
        return Result.success().data("page", page);
    }

    /**
     * 获取id={id}学生
     * @param id
     * @return student
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable int id) {
        StudentDTO student = studentService.getByIdDTO(id);
        return Result.success().data("student", student);
    }

    /**
     * 添加学生
     * @param studentDTO all inform (dept_name instead of dept_id)
     */
    @PostMapping
    public Result add(@RequestBody StudentDTO studentDTO) {
        boolean flag = studentService.saveDTO(studentDTO);
        return flag ? Result.success().message("增加成功") : Result.error().message("增加失败");
    }

    /**
     * 修改学生
     * @param studentDTO all inform (dept_name instead of dept_id)
     */
    @PutMapping
    public Result edit(@RequestBody StudentDTO studentDTO) {
        boolean flag = studentService.updateDTO(studentDTO);
        return flag ? Result.success().message("修改成功") : Result.error().message("修改失败: 可能id不存在");
    }

    /**
     * 修改状态
     * @param studentDTO id status
     */
    @PutMapping("/status")
    public Result changeStatus(@RequestBody StudentDTO studentDTO) {
        boolean flag = studentService.updateStatus(studentDTO);
        return flag ? Result.success().message("状态修改成功") : Result.error().message("状态修改失败: 可能id不存在");
    }
}
