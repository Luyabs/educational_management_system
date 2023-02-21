package com.example.educational_management_system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.educational_management_system.common.Result;
import com.example.educational_management_system.entity.Admin;
import com.example.educational_management_system.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    /**
     * 登录
     * @param admin [username, password]
     */
    @PostMapping("/login")
    public Result login(@RequestBody Admin admin) {
        // 找到目标
        String token = adminService.login(admin);
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
        Map<String, Object> map = adminService.decodeToken(token);
        return Result.success().data(map);
    }

    /**
     * 分页获取管理员
     * @return page
     */
    @GetMapping("/page")
    public Result getPage(int currentPage, int pageSize) {
        IPage<Admin> page = adminService.page(currentPage, pageSize);
        return Result.success().data("page", page);
    }

    /**
     * 获取id={id}管理员
     * @param id
     * @return admin
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable int id) {
        Admin admin = adminService.getById(id);
        return Result.success().data("admin", admin);
    }
}
