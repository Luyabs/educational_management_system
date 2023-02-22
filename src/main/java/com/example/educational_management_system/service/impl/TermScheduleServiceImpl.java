package com.example.educational_management_system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.educational_management_system.common.KeyCheck;
import com.example.educational_management_system.common.exception.ServiceException;
import com.example.educational_management_system.dto.TermScheduleDTO;
import com.example.educational_management_system.entity.SelectCourse;
import com.example.educational_management_system.entity.TermSchedule;
import com.example.educational_management_system.mapper.SelectCourseMapper;
import com.example.educational_management_system.mapper.TermScheduleMapper;
import com.example.educational_management_system.service.TermScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TermScheduleServiceImpl extends ServiceImpl<TermScheduleMapper, TermSchedule> implements TermScheduleService {
    @Autowired
    private TermScheduleMapper termScheduleMapper;

    @Autowired
    private SelectCourseMapper selectCourseMapper;

    /**
     * 分页查询
     */
    public IPage<TermScheduleDTO> getPage(int currentPage, int pageSize) {
        return termScheduleMapper.selectPageDTO(new Page<>(currentPage, pageSize));
    }

    /**
     * 按id查课
     */
    @Override
    public TermScheduleDTO getByIdDTO(int id) {
        TermScheduleDTO termScheduleDTO = termScheduleMapper.selectByIdDTO(id);
        if (termScheduleDTO == null)
            throw new ServiceException("不存在或未开设id=" + id + "的课程");
        return termScheduleDTO;
    }

    /**
     * 新开课
     */
    @Override
    public boolean save(TermSchedule termSchedule) {
        KeyCheck.primaryCheckTermSchedule(termSchedule);
        System.out.println("123123123231123123123123");
        KeyCheck.checkTeacher(termSchedule.getTeacherId());
        KeyCheck.checkCourse(termSchedule.getCourseId());
        return termScheduleMapper.insert(termSchedule) > 0;
    }


    /**
     * 修改开课
     */
    @Override
    public boolean update(TermSchedule termSchedule) {
        TermSchedule temp = KeyCheck.checkTermSchedule(termSchedule.getId());
        if (termSchedule.getTeacherId() != null) {
            KeyCheck.checkTeacher(termSchedule.getTeacherId());
            temp.setTeacherId(termSchedule.getTeacherId());
        }
        if (termSchedule.getCourseId() != null) {
            KeyCheck.checkCourse(termSchedule.getCourseId());
            temp.setCourseId(termSchedule.getCourseId());
        }
        // 确保主键不重复
        KeyCheck.primaryCheckTermSchedule(temp);
        return termScheduleMapper.updateById(termSchedule) > 0;
    }


    /**
     * 删除开课
     */
    @Override
    public boolean delete(int id) {
        KeyCheck.checkTermSchedule(id);
        // 已被选过的课不允许被删除
        LambdaQueryWrapper<SelectCourse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SelectCourse::getTermScheduleId, id);
        if (selectCourseMapper.exists(wrapper)) // SelectCourse的约束
            throw new ServiceException("课程已有学生选/过去曾开设过 不可删除");
        return termScheduleMapper.deleteById(id) > 0;
    }

}
