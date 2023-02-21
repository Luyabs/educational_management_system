package com.example.educational_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.educational_management_system.common.KeyCheck;
import com.example.educational_management_system.common.JwtUtils;
import com.example.educational_management_system.common.exception.ServiceException;
import com.example.educational_management_system.dto.StudentDTO;
import com.example.educational_management_system.entity.Student;
import com.example.educational_management_system.mapper.StudentMapper;
import com.example.educational_management_system.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Autowired
    private StudentMapper studentMapper;

    /**
     * 学生登录
     */
    public String login(Student student) {
        // 找到目标
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Student::getUsername, student.getUsername());
        Student target = studentMapper.selectOne(wrapper);

        // 数据异常
        if (target == null)
            throw new ServiceException("账号不存在");
        if (!student.getPassword().equals(target.getPassword()))
            throw new ServiceException("密码错误");
        // 生成token
        return JwtUtils.generateToken(target.getId().toString());
    }

    /**
     * 解析token
     */
    public Map<String, Object> decodeToken(String token) {
        int id = Integer.parseInt(JwtUtils.decodeByToken(token));
        Student student = studentMapper.selectById(id);

        Map<String, Object> map = new HashMap<>();
        map.put("id", student.getId());
        map.put("name", student.getUsername());
        map.put("avatar", "https://www.w3.org/comm/assets/icons/megaphone.png");
        return map;
    }

    /**
     * 分页查询
     */
    public IPage<StudentDTO> getPage(int currentPage, int pageSize) {
        return studentMapper.selectPageDTO(new Page<>(currentPage, pageSize));
    }

    /**
     * 按id查询
     */
    @Override
    public StudentDTO getByIdDTO(int id) {
        StudentDTO studentDTO = studentMapper.selectByIdDTO(id);
        if (studentDTO == null)
            throw new ServiceException("没有id=" + id + "的学生");
        return studentDTO;
    }

    /**
     * 新增学生
     */
    @Override
    public boolean save(Student student) {
        KeyCheck.checkDept(student.getDeptId());
        return studentMapper.insert(student) > 0;
    }

    /**
     * 修改学生
     */
    @Override
    public boolean update(Student student) {
        KeyCheck.checkStudent(student.getId());
        if (student.getDeptId() != null)
            KeyCheck.checkDept(student.getDeptId());
        student.setStatus(null);    //禁止修改状态位
        return studentMapper.updateById(student) > 0;
    }

    /**
     * 修改状态
     */
    @Override
    public boolean updateStatus(Student student) {
        KeyCheck.checkStudent(student.getId());

        // 拷贝成student对象防止改变其他属性
        Student temp = new Student();
        temp.setId(student.getId());
        temp.setStatus(student.getStatus());

        return studentMapper.updateById(temp) > 0;
    }
}
