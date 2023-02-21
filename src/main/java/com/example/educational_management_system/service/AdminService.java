package com.example.educational_management_system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.educational_management_system.entity.Admin;

import java.util.Map;

public interface AdminService extends IService<Admin> {
    String login(Admin admin);

    Map<String, Object> decodeToken(String token);

    IPage<Admin> page(int currentPage, int pageSize);
}
