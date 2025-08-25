package com.bitbyashish.featureflag.dto;

import lombok.Data;

@Data
public class FlagRuleResponse {
    private Long id;
    private String userGroup;
    private String condition;
}
