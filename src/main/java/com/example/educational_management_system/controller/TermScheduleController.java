package com.example.educational_management_system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.educational_management_system.common.Result;
import com.example.educational_management_system.dto.TermScheduleDTO;
import com.example.educational_management_system.service.TermScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/term_schedule")
public class TermScheduleController {
    @Autowired
    private TermScheduleService termScheduleService;

    /**
     * 分页获取总开课表
     * @return page
     */
    @GetMapping("/page")
    public Result getPage(int currentPage, int pageSize) {
        IPage<TermScheduleDTO> page = termScheduleService.getPage(currentPage, pageSize);
        return Result.success().data("page", page);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable int id) {
        TermScheduleDTO termSchedule = termScheduleService.getByIdDTO(id);
        return Result.success().data("termSchedule", termSchedule);
    }

    /**
     * 添加开设的课程
     * @param termScheduleDTO all inform (XX_name instead of XX_id)
     */
    @PostMapping
    public Result add(@RequestBody TermScheduleDTO termScheduleDTO) {
        boolean flag = termScheduleService.saveDTO(termScheduleDTO);
        return flag ? Result.success().message("增加成功") : Result.error().message("增加失败");
    }

    /**
     * 修改开设的课程
     * @param termScheduleDTO all inform (XX_name instead of XX_id)
     */
    @PutMapping
    public Result edit(@RequestBody TermScheduleDTO termScheduleDTO) {
        boolean flag = termScheduleService.updateDTO(termScheduleDTO);
        return flag ? Result.success().message("修改成功") : Result.error().message("修改失败: 可能id不存在");
    }

    /**
     * 删除开设的课
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable int id) {
        boolean flag = termScheduleService.delete(id);
        return flag ? Result.success().message("课程删除成功") : Result.error().message("课程删除失败: 可能id不存在");
    }
}
