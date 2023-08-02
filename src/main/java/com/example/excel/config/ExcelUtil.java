package com.example.excel.config;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.SheetCollate;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.apache.poi.ss.usermodel.CellType.STRING;

@Component
public class ExcelUtil {
    public static final List<String> excelExtensions = List.of("xlsx", "xls");

    //ExcelData -> DtoList로 변환
    public <T> List<T> getExcelDtoList(final MultipartFile file, final Class<T> clazz) {
        //확장자 검사
        checkExcelExtension(file);
        //엑셀 파일을 Workbook에담기
        Workbook workbook = getWorkBook(file);
        //0번째 시트
        Sheet sheet = workbook.getSheetAt(0);
        final int lastRowNum = sheet.getPhysicalNumberOfRows();
        return IntStream.range(1, lastRowNum)
                .mapToObj(rowIdx -> rowToExcelDto(sheet.getRow(rowIdx),clazz)).collect(Collectors.toList());
    }

    private String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case FORMULA:
                return String.valueOf(cell.getCellFormula());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case ERROR:
                return ErrorEval.getText(cell.getErrorCellValue());
            case BLANK:
            case _NONE:
                return null;
            default:
                throw new IllegalArgumentException("엑셀 형변환 실패!");
        }
    }


    private void checkExcelExtension(final MultipartFile file) {
        if (!ExcelUtil.excelExtensions.contains(FilenameUtils.getExtension(file.getOriginalFilename()))) {
            throw new RuntimeException("확장자를 확인해주세요");
        }
    }

    private <T> void setField(T ob, Row row, Cell cell, Field field, String value){
        field.setAccessible(true);
        try {
            field.set(ob, value);
            checkValidation(ob,row.getRowNum(),cell.getColumnIndex(),field);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(getExcelErrMsg(row.getRowNum(),cell.getColumnIndex(),"변환 에러 발생"), e);
        }
    }

    private Workbook getWorkBook(final MultipartFile file) {
        try {
            return WorkbookFactory.create(file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("엑셀 파일을 읽는 도중 에러가 발생했습니다.");
        }
    }

    /**
     * Class to Object 변환
     *
     * @param clazz
     * @param <T>
     * @return
     */
    private <T> T getNewInstance(Class<T> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 객체 Valid를 체크해서 에러시 ExcelException 발생
     *
     * @param object
     * @param rowIndex
     * @param cellIndex
     * @param <T>
     */
    private <T> void checkValidation(T object, int rowIndex , int cellIndex, Field field) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> constraintValidations = validator.validate(object);
        ConstraintViolation<T> validData = constraintValidations.stream()
                .filter(data -> data.getPropertyPath().toString().equals(field.getName()))
                .findFirst().orElse(null);
        if (Objects.nonNull(validData)) {
            throw new RuntimeException(getExcelErrMsg(rowIndex,cellIndex,validData.getMessage()));
        }
    }

    private String getExcelErrMsg(int rowNum, int cellIndex, String msg){
        return String.format("ROW : %d , CELL : %d, MSG : %s", rowNum, cellIndex, msg);
    }

    /**
     * Row를  ExcelDto로 변환
     * @param row
     * @param clazz
     * @param <T>
     * @return
     */
    private <T> T rowToExcelDto(Row row, Class<T> clazz) {
        T ob = getNewInstance(clazz);
        AtomicInteger i = new AtomicInteger();
        for (Field field : clazz.getDeclaredFields()) {
            Cell cell = row.getCell(i.get());
            setField(ob, row, cell, field, getCellValue(cell));
            i.getAndIncrement();
        }
        return ob;
    }

}
