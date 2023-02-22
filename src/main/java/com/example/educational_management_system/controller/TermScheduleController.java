package com.example.educational_management_system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.educational_management_system.common.Result;
import com.example.educational_management_system.dto.TermScheduleDTO;
import com.example.educational_management_system.entity.TermSchedule;
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
     */
    @GetMapping("/page")
    public Result getPage(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "10") int pageSize, TermSchedule termSchedule) {
        IPage<TermScheduleDTO> page = termScheduleService.getPage(currentPage, pageSize, termSchedule);
        return Result.success().data("page", page);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable int id) {
        TermScheduleDTO termSchedule = termScheduleService.getByIdDTO(id);
        return Result.success().data("termSchedule", termSchedule);
    }

    /**
     * 添加开设的课程
     */
    @PostMapping
    public Result add(@RequestBody TermSchedule termSchedule) {
        boolean flag = termScheduleService.save(termSchedule);
        return flag ? Result.success().message("增加成功") : Result.error().message("增加失败");
    }

    /**
     * 修改开设的课程
     * @param termSchedule 需要传入id
     */
    @PutMapping
    public Result edit(@RequestBody TermSchedule termSchedule) {
        boolean flag = termScheduleService.update(termSchedule);
        return flag ? Result.success().message("修改成功") : Result.error().message("修改失败");
    }

    /**
     * 删除开设的课 [要删除的课不能出现在选课表中]
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable int id) {
        boolean flag = termScheduleService.delete(id);
        return flag ? Result.success().message("课程删除成功") : Result.error().message("课程删除失败");
    }
}
