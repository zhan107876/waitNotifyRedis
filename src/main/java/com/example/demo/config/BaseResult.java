package com.example.demo.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

@Data
@JsonIgnoreProperties({"success"})
public class BaseResult {
    public final static String SUCCESS = "SUCCESS";
    public final static String FAILED = "FAILED";

    /**
     * 错误代码.
     */
    private String errorCode;

    /**
     * 消息描述.
     */
    @JsonInclude(Include.NON_DEFAULT)
    private String message;

    public BaseResult() {
    }

    public BaseResult(String errorCode) {
        this.errorCode = errorCode;
    }

    public BaseResult(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

}
