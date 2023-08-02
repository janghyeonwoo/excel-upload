package com.example.excel.dto;

import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
public class UploadlDto {

    //    Long idx;
    @NotNull(message = "이름")
    String name;
    @NotNull(message = "나이")
    String age;

}
