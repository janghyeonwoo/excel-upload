package com.example.excel.service;

import com.example.excel.config.ExcelUtil;
import com.example.excel.dto.UploadlDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExcelService {
    private final ExcelUtil excelUtil;

    public void uploadExcel(MultipartFile file) {
        List<UploadlDto> uploadDtos = excelUtil.getExcelDtoList(file, UploadlDto.class);
        log.info("uploadDtos : {}", uploadDtos);
    }

}
