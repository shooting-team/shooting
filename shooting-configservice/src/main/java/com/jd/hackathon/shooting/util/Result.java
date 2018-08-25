package com.jd.hackathon.shooting.util;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;

import java.util.List;

/**
 * 统一API响应结果封装
 */
public class Result {
    private int code;
    private String message;
    private Object data;

    Result() {
    }

    Result(int code, String message) {
        this.code = code;
        this.message = message;
    }

    Result(int code, List<String> messages) {
        this.code = code;
        this.message = Joiner.on(",").join(messages).toString();
    }

    public Result setCode(ResultCode resultCode) {
        this.code = resultCode.code;
        return this;
    }

    public int getCode() {
        return code;
    }

    public Result setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Result setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public Result setData(Object data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
