package com.example.educational_management_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.educational_management_system.entity.Dept;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeptMapper extends BaseMapper<Dept> {

//    @Select("select dept_name from dept where id = #{id}")
//    String selectNameById(int id);

}
