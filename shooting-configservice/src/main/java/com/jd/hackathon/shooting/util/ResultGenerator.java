package com.jd.hackathon.shooting.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * 响应结果生成工具
 */
public class ResultGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";

    public static Result genSuccessResult() {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE);
    }

    public static Result genSuccessResult(String data) {
        if(JsonUtils.isJSONValid(data)){
            return new Result()
                    .setCode(ResultCode.SUCCESS)
                    .setMessage(DEFAULT_SUCCESS_MESSAGE)
                    .setData(JSONObject.parseObject(data));
        }
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);
    }

    public static Result genSuccessResult(Object data) {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE)
                .setData(data);
    }

    public static Result genSuccessArr(String data) {
        return new Result()
                .setCode(ResultCode.SUCCESS)
                .setMessage(DEFAULT_SUCCESS_MESSAGE)
                .setData(JSONArray.parseArray(data));
    }

    public static Result genFailResult(String message) {
        return new Result()
                .setCode(ResultCode.FAIL)
                .setMessage(message);
    }

    public static Result genFailResult(List<String> messages) {
        return new Result(ResultCode.FAIL.code,messages);
    }

    public static Result genRedirctResult(Object data) {
        return new Result()
                .setCode(ResultCode.REDIRECT)
                .setData(data);
    }

    public static Result genResult(ResultCode code, String message) {
        return new Result().setCode(code).setMessage(message);
    }

    public static Result genGenResult(int code, String message) {
        return new Result().setCode(code).setMessage(message);
    }

}
