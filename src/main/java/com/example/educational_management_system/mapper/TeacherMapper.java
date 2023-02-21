package com.example.educational_management_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.educational_management_system.dto.TeacherDTO;
import com.example.educational_management_system.entity.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TeacherMapper extends BaseMapper<Teacher> {
    // 还是在service层组装好
    @Select("select teacher.id, username, password, real_name, dept_name," +
            " if(status=1, '正常', '禁止') status, title\n" +
            "from teacher join dept on teacher.dept_id = dept.id")
    IPage<TeacherDTO> selectPageDTO(IPage<TeacherDTO> iPage);
}
