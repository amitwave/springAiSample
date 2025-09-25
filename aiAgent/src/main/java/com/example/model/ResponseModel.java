package com.example.model;

import lombok.Data;

import java.util.List;

@Data
public class ResponseModel {
    private String key;
    private List<String> values;
}
