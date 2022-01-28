package com.example.integrlinebyline.dto;

import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class ExampleBody {
    public String id;
    public String docNumber;
    public String fileDest;
}
