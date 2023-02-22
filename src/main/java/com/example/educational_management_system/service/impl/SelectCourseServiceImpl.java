package com.example.educational_management_system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.educational_management_system.common.KeyCheck;
import com.example.educational_management_system.common.exception.ServiceException;
import com.example.educational_management_system.dto.SelectCourseDTO;
import com.example.educational_management_system.entity.SelectCourse;
import com.example.educational_management_system.entity.Student;
import com.example.educational_management_system.entity.TermSchedule;
import com.example.educational_management_system.mapper.SelectCourseMapper;
import com.example.educational_management_system.service.SelectCourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SelectCourseServiceImpl extends ServiceImpl<SelectCourseMapper, SelectCourse> implements SelectCourseService {
    @Autowired
    private SelectCourseMapper selectCourseMapper;

    /**
     * 分页查询
     */
    @Override
    public IPage<SelectCourseDTO> getPage(int currentPage, int pageSize) {
        return selectCourseMapper.selectPageDTO(new Page<>(currentPage, pageSize));
    }

    /**
     * 按id查选课-成绩
     */
    @Override
    public SelectCourseDTO getByIdDTO(int id) {
        SelectCourseDTO selectCourseDTO = selectCourseMapper.selectByIdDTO(id);
        if (selectCourseDTO == null)
            throw new ServiceException("不存在id=" + id + "的选课/成绩记录");
        return selectCourseDTO;
    }

    /**
     * 选课
     */
    @Override
    public boolean chooseCourse(int studentId, int termScheduleId) {
        Student student = KeyCheck.checkStudent(studentId);
        TermSchedule termSchedule = KeyCheck.checkTermSchedule(termScheduleId);

        // 选课时间冲突
        if (isTimeConflict(studentId, termSchedule))
            throw new ServiceException("选课时间冲突");
        // 学生状态非正常
        if (student.getStatus() != 1)
            throw new ServiceException("学生不处于正常状态");
        // 可选
        SelectCourse selectCourse = new SelectCourse(studentId, termScheduleId);

        return selectCourseMapper.insert(selectCourse) > 0;
    }


    /**
     * 退课
     */
    @Override
    public boolean giveUpCourse(int id) {
        SelectCourse courseRecord = KeyCheck.checkSelectCourse(id);

        // 查询这门课是否有成绩
        if (courseRecord.getScoreUsual() != null || courseRecord.getScoreExam() != null || courseRecord.getScoreTotal() != null)
            throw new ServiceException("此课已登分, 不可退课");
        return selectCourseMapper.deleteById(id) > 0;
    }

    /**
     * 获取id=studentId学生所选的所有课程DTO
     */
    @Override
    public List<SelectCourseDTO> getOnesAllCoursesDTO(int studentId) {
        KeyCheck.checkStudent(studentId);
        return selectCourseMapper.selectListByStudentIdDTO(studentId);
    }


    /**
     * 获取班上所有学生的选课-成绩信息
     */
    @Override
    public List<SelectCourseDTO> getClassAllCoursesDTO(int termScheduleId) {
        KeyCheck.checkTermSchedule(termScheduleId);
        return selectCourseMapper.selectListByTermScheduleIdDTO(termScheduleId);
    }

    /**
     * 登分
     */
    @Override
    public boolean updateScore(SelectCourse selectCourse) {
        SelectCourse temp = KeyCheck.checkSelectCourse(selectCourse.getId());
        temp.setScoreUsual(selectCourse.getScoreUsual());
        temp.setScoreExam(selectCourse.getScoreExam());
        temp.setScoreTotal(selectCourse.getScoreTotal());
        return selectCourseMapper.updateById(temp) > 0;
    }



    /**
     * 检测是否学生选课时间冲突
     */
    private boolean isTimeConflict(int studentId, TermSchedule termSchedule) {
        KeyCheck.primaryCheckSelectCourse(new SelectCourse(studentId, termSchedule.getId()));
        List<String> timeList = selectCourseMapper.getOnesAllCoursesTimeList(studentId, termSchedule.getTerm());
        log.info(timeList.toString());

        for (String time : timeList) {   //遍历这名学生对应学期选的所有课 如果选课时间冲突则返回true

            String[] timeSegment = time.split(" ");
            String[] termScheduleSegment = termSchedule.getTime().split(" ");
            for (String seg1 : timeSegment) {
                for (String seg2 : termScheduleSegment) {
                    if (seg1.charAt(2) == seg2.charAt(2)) {    //如果星期相同 继续判断
                        if (!(seg1.charAt(5) < seg2.charAt(3) || seg1.charAt(3) > seg2.charAt(5)))    // 时间没有重合部分
                            return true;
                    }
                }
            }
        }
        return false;
    }

}
