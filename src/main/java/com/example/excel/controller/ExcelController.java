package com.example.excel.controller;

import com.example.excel.service.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("excel")
@RequiredArgsConstructor
@RestController
public class ExcelController {
    private final ExcelService excelService;
    @PostMapping
    public String getExcel(MultipartFile file){
        excelService.uploadExcel(file);
        return "OK";
    }
}
