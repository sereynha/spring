package com.school.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDto {
    String statusCode;
    String title;
    String detail;
    List<String> fieldErrors;
    public ErrorDto(String statusCode, String title, String detail){
        this(statusCode, title, detail, new ArrayList<String>());
    }
}
