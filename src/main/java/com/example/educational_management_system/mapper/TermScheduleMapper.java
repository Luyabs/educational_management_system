package com.example.educational_management_system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.educational_management_system.dto.TermScheduleDTO;
import com.example.educational_management_system.entity.TermSchedule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TermScheduleMapper extends BaseMapper<TermSchedule> {
    @Select("select term_schedule.id, term, course_name, real_name teacher_name, time\n" +
            "from term_schedule\n" +
            "join teacher on teacher_id = teacher.id\n" +
            "join course on course_id = course.id")
    IPage<TermScheduleDTO> selectPageDTO(IPage<TermScheduleDTO> iPage);
}
