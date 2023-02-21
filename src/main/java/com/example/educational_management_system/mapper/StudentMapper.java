package com.example.educational_management_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.educational_management_system.dto.StudentDTO;
import com.example.educational_management_system.entity.Dept;
import com.example.educational_management_system.entity.Student;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.RequestParam;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {

    @Results(id = "withDept", value = {
        @Result(
                column = "dept_id", property = "dept", javaType = Dept.class,
                one = @One(select = "com.example.educational_management_system.mapper.DeptMapper.selectById")
        )
    })
    @Select("""
            select student.id, username, real_name, dept_id,
            if(status=1, '正常', '禁止') status, if(gender=1, '男', '女') gender, birthday
            from student
            join dept on student.dept_id = dept.id
            """)
    IPage<StudentDTO> selectPageDTO(IPage<StudentDTO> iPage);

    @ResultMap(value = "withDept")
    @Select("""
            select student.id, username, real_name, dept_id,
            if(status=1, '正常', '禁止') status, if(gender=1, '男', '女') gender, birthday
            from student
            join dept on student.dept_id = dept.id
            where student.id = #{id}
            """)
    StudentDTO selectByIdDTO(@RequestParam("id") int id);
}
