package com.example.educational_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.educational_management_system.common.JwtUtils;
import com.example.educational_management_system.common.ServiceException;
import com.example.educational_management_system.dto.TeacherDTO;
import com.example.educational_management_system.entity.Dept;
import com.example.educational_management_system.entity.Teacher;
import com.example.educational_management_system.mapper.DeptMapper;
import com.example.educational_management_system.mapper.TeacherMapper;
import com.example.educational_management_system.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {
    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private DeptMapper deptMapper;

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
        map.put("name", teacher.getUsername());
        map.put("avatar", "https://www.w3.org/comm/assets/icons/megaphone.png");
        return map;
    }

    /**
     * 分页查询
     */
    public IPage<TeacherDTO> getPage(int currentPage, int pageSize) {
        // 查teacher - dept
        List<TeacherDTO> teacherDTOS = new ArrayList<>();
        IPage<Teacher> page = teacherMapper.selectPage(new Page<>(currentPage, pageSize), null);
        List<Teacher> teachers = page.getRecords();
        // 对应组装 List<DTO>
        for (Teacher teacher : teachers) {
            Dept dept = deptMapper.selectById(teacher.getDeptId());
            TeacherDTO teacherDTO = new TeacherDTO(teacher);
            teacherDTO.setDeptName(dept.getDeptName());
            teacherDTOS.add(teacherDTO);
        }
        // 组装成 Page<DTO>
        IPage<TeacherDTO> pageDTO = new Page<>(currentPage, pageSize);
        pageDTO.setRecords(teacherDTOS);
        pageDTO.setTotal(page.getTotal());
        pageDTO.setPages(page.getPages());
        return pageDTO;
    }

    /**
     * 按id查询
     */
    @Override
    public TeacherDTO getByIdDTO(int id) {
        Teacher teacher = teacherMapper.selectById(id);
        if (teacher == null)
            throw new ServiceException("id=" + id + "的老师不存在");
        Dept dept = deptMapper.selectById(teacher.getDeptId());

        TeacherDTO teacherDTO = new TeacherDTO(teacher);
        teacherDTO.setDeptName(dept.getDeptName());
        return teacherDTO;
    }

    /**
     * 新增教师
     */
    @Override
    public boolean saveDTO(TeacherDTO teacherDTO) {
        // 通过dept.deptName 替换dept_id
        setDeptName(teacherDTO);
        return teacherMapper.insert(teacherDTO) > 0;
    }

    /**
     * 修改教师
     */
    @Override
    public boolean updateDTO(TeacherDTO teacherDTO) {
        // 去除掉本不该出现的dept_id 与 status
        teacherDTO.setDeptId(null);
        teacherDTO.setStatus(null);

        // 如果有修改dept.deptName 则通过dept.deptName 替换dept_id
        if (teacherDTO.getDeptName() != null) {
            setDeptName(teacherDTO);
        }
        return teacherMapper.updateById(teacherDTO) > 0;
    }

    /**
     * 修改状态
     */
    @Override
    public boolean updateStatus(TeacherDTO teacherDTO) {
        // 拷贝成teacher对象防止改变其他属性
        Teacher teacher = new Teacher();
        teacher.setId(teacherDTO.getId());
        teacher.setStatus(teacherDTO.getStatus());
        // 做修改
        return teacherMapper.updateById(teacher) > 0;
    }

    /**
     * 检查是否真实存在部门 并赋值部门id
     */
    private void setDeptName(TeacherDTO teacherDTO) {
        LambdaQueryWrapper<Dept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dept::getDeptName, teacherDTO.getDeptName());
        Dept dept = deptMapper.selectOne(wrapper);
        if (dept == null)   //院系不存在
            throw new ServiceException("院系不存在");
        teacherDTO.setDeptId(dept.getId());
    }
}
