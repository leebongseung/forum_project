package com.example.forum_project.config;

import io.swagger.v3.oas.models.examples.Example;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PROTECTED)
public class ExampleHolder {
    private Example holder;
    private String name;
    private int code;
}