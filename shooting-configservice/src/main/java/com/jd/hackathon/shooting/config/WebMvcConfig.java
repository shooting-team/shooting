package com.jd.hackathon.shooting.config;

import com.alibaba.fastjson.JSON;
import com.jd.hackathon.shooting.util.DescribeException;
import com.jd.hackathon.shooting.util.ExceptionEnum;
import com.jd.hackathon.shooting.util.Result;
import com.jd.hackathon.shooting.util.ResultGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author dubiaopei
 * @Description: MVC配置
 * @date 2018/5/9 14:38
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    private final Logger logger = LoggerFactory.getLogger(WebMvcConfig.class);


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(tokenVerifyInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

    /**
     * 统一异常处理
     * @param exceptionResolvers
     */
    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new HandlerExceptionResolver() {
            public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
                if (e instanceof DescribeException) {
                    /** 业务失败的异常，如“账号或密码错误”*/
                    DescribeException pingjiaException = new DescribeException(ExceptionEnum.UNAUTHORIZED);
                    responseResult(response, ResultGenerator.genGenResult(pingjiaException.getCode(),"未认证（签名错误）"));
                    logger.info(e.getMessage());
                } else if (e instanceof NoHandlerFoundException) {
                    DescribeException pingjiaException = new DescribeException(ExceptionEnum.NOT_FOUND);
                    responseResult(response,ResultGenerator.genGenResult(pingjiaException.getCode(),"接口 [" + request
                            .getRequestURI() +
                            "] 不存在"));
                } else if (e instanceof ServletException) {
                    responseResult(response, ResultGenerator.genFailResult(e.getMessage()));
                } else {
                    DescribeException pingjiaException = new DescribeException(ExceptionEnum.INTERNAL_SERVER_ERROR);
                    responseResult(response, ResultGenerator.genGenResult(pingjiaException.getCode(),"接口 [" +
                            request.getRequestURI() + "] 内部错误，请联系管理员"));
                    String message;
                    if (handler instanceof HandlerMethod) {
                        HandlerMethod handlerMethod = (HandlerMethod) handler;
                        message = String.format("接口 [%s] 出现异常，方法：%s.%s，异常摘要：%s",
                                request.getRequestURI(),
                                handlerMethod.getBean().getClass().getName(),
                                handlerMethod.getMethod().getName(),
                                e.getMessage());
                    } else {
                        message = e.getMessage();
                    }
                    logger.error(message, e);
                }
                return new ModelAndView();
            }

        });
    }

    private void responseResult(HttpServletResponse response, Result result) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        response.setStatus(200);
        try {
            response.flushBuffer();
            response.getOutputStream();
//            response.getWriter().write(JSON.toJSONString(result));
            logger.debug(JSON.toJSONString(result));
        } catch (IOException ex) {
            logger.error(ex.getMessage());
        }
    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {

        configurer.setDefaultTimeout(1000000);
    }
}
