package com.example.educational_management_system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.educational_management_system.common.Result;
import com.example.educational_management_system.dto.TeacherDTO;
import com.example.educational_management_system.entity.Teacher;
import com.example.educational_management_system.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/teacher")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    /**
     * 登录
     * @param teacher [username, password]
     */
    @PostMapping("/login")
    public Result login(@RequestBody Teacher teacher) {
        // 找到目标
        String token = teacherService.login(teacher);
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
        Map<String, Object> map = teacherService.decodeToken(token);
        return Result.success().data(map);
    }

    /**
     * 分页获取教师
     */
    @GetMapping("/page")
    public Result getPage(int currentPage, int pageSize) {
        IPage<TeacherDTO> page = teacherService.getPage(currentPage, pageSize);
        return Result.success().data("page", page);
    }

    /**
     * 获取id={id}教师
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable int id) {
        TeacherDTO teacher = teacherService.getByIdDTO(id);
        return Result.success().data("teacher", teacher);
    }

    /**
     * 添加教师
     * @param teacher
     */
    @PostMapping
    public Result add(@RequestBody Teacher teacher) {
        boolean flag = teacherService.save(teacher);
        return flag ? Result.success().message("增加成功") : Result.error().message("增加失败");
    }

    /**
     * 修改教师 [不修改 status]
     * @param teacher 需要传入id
     */
    @PutMapping
    public Result edit(@RequestBody Teacher teacher) {
        boolean flag = teacherService.update(teacher);
        return flag ? Result.success().message("修改成功") : Result.error().message("修改失败");
    }

    /**
     * 修改状态
     * @param teacher 需要传入id 只影响status
     */
    @PutMapping("/status")
    public Result changeStatus(@RequestBody Teacher teacher) {
        boolean flag = teacherService.updateStatus(teacher);
        return flag ? Result.success().message("状态修改成功") : Result.error().message("状态修改失败");
    }
}
