package com.example.educational_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.educational_management_system.common.KeyCheck;
import com.example.educational_management_system.common.JwtUtils;
import com.example.educational_management_system.common.exception.ServiceException;
import com.example.educational_management_system.dto.TeacherDTO;
import com.example.educational_management_system.entity.Teacher;
import com.example.educational_management_system.mapper.TeacherMapper;
import com.example.educational_management_system.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {
    @Autowired
    private TeacherMapper teacherMapper;

    /**
     * 教师登录
     */
    public String login(Teacher teacher) {
        // 找到目标
        LambdaQueryWrapper<Teacher> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Teacher::getUsername, teacher.getUsername());
        Teacher target = teacherMapper.selectOne(wrapper);

        // 数据异常
        if (target == null)
            throw new ServiceException("账号不存在");
        if (!teacher.getPassword().equals(target.getPassword()))
            throw new ServiceException("密码错误");

        // 生成token
        return JwtUtils.generateToken(target.getId().toString());
    }

    /**
     * 解析token
     */
    public Map<String, Object> decodeToken(String token) {
        int id = Integer.parseInt(JwtUtils.decodeByToken(token));
        Teacher teacher = teacherMapper.selectById(id);

        Map<String, Object> map = new HashMap<>();
        map.put("id", teacher.getId());
        map.put("name", teacher.getUsername());
        map.put("avatar", "https://www.w3.org/comm/assets/icons/megaphone.png");
        return map;
    }

    /**
     * 分页查询
     */
    public IPage<TeacherDTO> getPage(int currentPage, int pageSize) {
        return teacherMapper.selectPageDTO(new Page<>(currentPage, pageSize));
    }

    /**
     * 按id查询
     */
    @Override
    public TeacherDTO getByIdDTO(int id) {
        TeacherDTO teacherDTO = teacherMapper.selectByIdDTO(id);
        if (teacherDTO == null)
            throw new ServiceException("没有id=" + id + "的老师");
        return teacherDTO;
    }

    /**
     * 新增教师
     */
    @Override
    public boolean save(Teacher teacher) {
        KeyCheck.checkDept(teacher.getDeptId());
        return teacherMapper.insert(teacher) > 0;
    }

    /**
     * 修改教师
     */
    @Override
    public boolean update(Teacher teacher) {
        KeyCheck.checkTeacher(teacher.getId());
        if (teacher.getDeptId() != null)
            KeyCheck.checkDept(teacher.getDeptId());
        teacher.setStatus(null);    //禁止修改状态位
        return teacherMapper.updateById(teacher) > 0;
    }

    /**
     * 修改状态
     */
    @Override
    public boolean updateStatus(Teacher teacher) {
        KeyCheck.checkTeacher(teacher.getId());

        // 拷贝成teacher对象防止改变其他属性
        Teacher temp = new Teacher();
        temp.setId(teacher.getId());
        temp.setStatus(teacher.getStatus());

        return teacherMapper.updateById(temp) > 0;
    }
}
