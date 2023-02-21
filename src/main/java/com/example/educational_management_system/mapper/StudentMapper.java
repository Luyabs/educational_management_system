package com.example.educational_management_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.educational_management_system.dto.StudentDTO;
import com.example.educational_management_system.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface StudentMapper extends BaseMapper<Student> {

    @Select("select student.id, username, password, real_name, dept_name," +
            " if(status=1, '正常', '禁止') status, if(gender=1, '男', '女') gender, birthday\n" +
            "from student join dept on student.dept_id = dept.id")
    IPage<StudentDTO> selectPageDTO(IPage<StudentDTO> iPage);
}
