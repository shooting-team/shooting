package com.jd.hackathon.shooting.util;

/**
 * @Description: 错误枚举类
 * @author dubiaopei
 * @date 2018/5/9 11:34
*/
public enum ExceptionEnum {
    UNKNOW_ERROR(-1,"未知错误"),
    USER_NOT_FIND(-101,"用户不存在"),
    FAIL(400,"数据校验错误"),
    REDIRECT(304,"跳转URL错误"),
    UNAUTHORIZED(401,"未认证（签名错误）"),
    NOT_FOUND(404,"接口不存在"),
    INTERNAL_SERVER_ERROR(500,"服务器内部错误");

    private Integer code;

    private String msg;

    ExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
