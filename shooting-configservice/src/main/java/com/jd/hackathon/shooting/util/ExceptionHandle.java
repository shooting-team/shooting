package com.jd.hackathon.shooting.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description: 错误定义
 * @author dubiaopei
 * @date 2018/5/9 11:35
*/
@ControllerAdvice
public class ExceptionHandle {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExceptionHandle.class);

    /**
     * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result exceptionGet(Exception e){
        if(e instanceof DescribeException){
            DescribeException pingjiaException = (DescribeException) e;
            return ResultGenerator.genResult(ResultCode.FAIL,pingjiaException.getMessage());
        }

        LOGGER.error("【系统异常】{}",e);
        return ResultGenerator.genFailResult(ExceptionEnum.UNKNOW_ERROR.getMsg());
    }
}
