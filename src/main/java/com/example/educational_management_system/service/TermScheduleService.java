package com.example.educational_management_system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.educational_management_system.dto.TermScheduleDTO;
import com.example.educational_management_system.entity.TermSchedule;

public interface TermScheduleService extends IService<TermSchedule> {
    IPage<TermScheduleDTO> getPage(int currentPage, int pageSize);

    TermScheduleDTO getByIdDTO(int id);

    boolean saveDTO(TermScheduleDTO termScheduleDTO);

    boolean updateDTO(TermScheduleDTO termScheduleDTO);

    boolean delete(int id);
}
