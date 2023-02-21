package com.example.educational_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.educational_management_system.common.JwtUtils;
import com.example.educational_management_system.common.ServiceException;
import com.example.educational_management_system.dto.StudentDTO;
import com.example.educational_management_system.entity.Dept;
import com.example.educational_management_system.entity.Student;
import com.example.educational_management_system.mapper.DeptMapper;
import com.example.educational_management_system.mapper.StudentMapper;
import com.example.educational_management_system.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private DeptMapper deptMapper;

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
        map.put("name", student.getUsername());
        map.put("avatar", "https://www.w3.org/comm/assets/icons/megaphone.png");
        return map;
    }

    /**
     * 分页查询
     */
    public IPage<StudentDTO> getPage(int currentPage, int pageSize) {
        // student - dept
        List<StudentDTO> studentDTOS = new ArrayList<>();

        IPage<Student> page = studentMapper.selectPage(new Page<>(currentPage, pageSize), null);
        List<Student> students = page.getRecords();
        // 对应组装 List<DTO>
        for (Student student : students) {
            Dept dept = deptMapper.selectById(student.getDeptId());
            StudentDTO studentDTO = new StudentDTO(student);
            studentDTO.setDeptName(dept.getDeptName());
            studentDTOS.add(studentDTO);
        }
        // 组装成 Page<DTO>
        IPage<StudentDTO> pageDTO = new Page<>(currentPage, pageSize);
        pageDTO.setRecords(studentDTOS);
        pageDTO.setTotal(page.getTotal());
        pageDTO.setPages(page.getPages());
        return pageDTO;
    }

    /**
     * 按id查询
     */
    @Override
    public StudentDTO getByIdDTO(int id) {
        Student student = studentMapper.selectById(id);
        if (student == null)
            throw new ServiceException("id=" + id + "的学生不存在");
        Dept dept = deptMapper.selectById(student.getDeptId());

        StudentDTO studentDTO = new StudentDTO(student);
        studentDTO.setDeptName(dept.getDeptName());
        return studentDTO;
    }

    /**
     * 新增学生
     */
    @Override
    public boolean saveDTO(StudentDTO studentDTO) {
        // 通过dept.deptName 替换dept_id
        setDeptName(studentDTO);
        return studentMapper.insert(studentDTO) > 0;
    }

    /**
     * 修改学生
     */
    @Override
    public boolean updateDTO(StudentDTO studentDTO) {
        // 去除掉本不该出现的dept_id 与 status
        studentDTO.setDeptId(null);
        studentDTO.setStatus(null);

        // 如果有修改dept.deptName 则通过dept.deptName 替换dept_id
        if (studentDTO.getDeptName() != null) {
            setDeptName(studentDTO);
        }
        return studentMapper.updateById(studentDTO) > 0;
    }

    /**
     * 修改状态
     */
    @Override
    public boolean updateStatus(StudentDTO studentDTO) {
        // 拷贝成student对象防止改变其他属性
        Student student = new Student();
        student.setId(studentDTO.getId());
        student.setStatus(studentDTO.getStatus());
        // 做修改
        return studentMapper.updateById(student) > 0;
    }

    /**
     * 检查是否真实存在部门 并赋值部门id
     */
    private void setDeptName(StudentDTO studentDTO) {
        LambdaQueryWrapper<Dept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dept::getDeptName, studentDTO.getDeptName());
        Dept dept = deptMapper.selectOne(wrapper);
        if (dept == null)   // 院系不存在
            throw new ServiceException("院系不存在");
        studentDTO.setDeptId(dept.getId());
    }
}
