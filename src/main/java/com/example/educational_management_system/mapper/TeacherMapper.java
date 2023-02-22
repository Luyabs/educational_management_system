package com.example.educational_management_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.educational_management_system.dto.TeacherDTO;
import com.example.educational_management_system.entity.Dept;
import com.example.educational_management_system.entity.Teacher;
import org.apache.ibatis.annotations.*;

@Mapper
public interface TeacherMapper extends BaseMapper<Teacher> {
    @Results(id = "withDept", value = {
            @Result(
                    column = "dept_id", property = "dept", javaType = Dept.class,
                    one = @One(select = "com.example.educational_management_system.mapper.DeptMapper.selectById")
            )
    })
    @Select("""
            select teacher.id, username, real_name, dept_id,
            if(status=1, '正常', '禁止') status, title
            from teacher
            join dept on teacher.dept_id = dept.id
            """)
    IPage<TeacherDTO> selectPageDTO(IPage<TeacherDTO> iPage);

    @ResultMap(value = "withDept")
    @Select("""
            select teacher.id, username, real_name, dept_id,
            if(status=1, '正常', '禁止') status, title
            from teacher
            join dept on teacher.dept_id = dept.id
            where teacher.id = #{id}
            """)
    TeacherDTO selectByIdDTO(@Param("id") int id);
}
