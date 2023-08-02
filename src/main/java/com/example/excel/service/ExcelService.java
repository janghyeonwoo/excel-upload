package com.example.excel.service;

import com.example.excel.config.ExcelUtil;
import com.example.excel.dto.KcpSalesDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExcelService {
    private final ExcelUtil excelUtil;


    public void uploadKcpSales(MultipartFile file) {

        List<KcpSalesDto> kcpSalesDtos = excelUtil.getExcelDtoList(file, KcpSalesDto.class);
        log.info("kcpSales : {}", kcpSalesDtos);



    }

}
