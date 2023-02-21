package com.example.educational_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.educational_management_system.common.JwtUtils;
import com.example.educational_management_system.common.ServiceException;
import com.example.educational_management_system.entity.Admin;
import com.example.educational_management_system.mapper.AdminMapper;
import com.example.educational_management_system.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    /**
     * 学生登录
     */
    public String login(Admin admin) {
        // 找到目标
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getUsername, admin.getUsername());
        Admin target = adminMapper.selectOne(wrapper);

        // 数据异常
        if (target == null)
            throw new ServiceException("账号不存在");
        if (!admin.getPassword().equals(target.getPassword()))
            throw new ServiceException("密码错误");
        // 生成token
        return JwtUtils.generateToken(target.getId().toString());
    }

    /**
     * 解析token
     */
    public Map<String, Object> decodeToken(String token) {
        int id = Integer.parseInt(JwtUtils.decodeByToken(token));
        Admin admin = adminMapper.selectById(id);

        Map<String, Object> map = new HashMap<>();
        map.put("name", admin.getUsername());
        map.put("avatar", "https://www.w3.org/comm/assets/icons/megaphone.png");
        return map;
    }

    /**
     * 分页查询
     */
    public IPage<Admin> page(int currentPage, int pageSize) {
        IPage<Admin> iPage = new Page<>(currentPage, pageSize);
        IPage<Admin> page = adminMapper.selectPage(iPage, null);
        return page;
    }
}
