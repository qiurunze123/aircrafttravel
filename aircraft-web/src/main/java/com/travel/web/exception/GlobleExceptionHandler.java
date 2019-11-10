package com.travel.web.exception;

import com.travel.commons.enums.ResultStatus;
import com.travel.commons.resultbean.AbstractResult;
import com.travel.commons.resultbean.ResultGeekQ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @auther 邱润泽 bullock
 * @date 2019/11/9
 */
@ControllerAdvice
@ResponseBody
public class GlobleExceptionHandler {

    public static Logger logger = LoggerFactory.getLogger(GlobleExceptionHandler.class);
    @ExceptionHandler(value = Exception.class)
    public ResultGeekQ<String> exceptionHandler(HttpServletRequest request,Exception e){
        logger.error("======== 拦截到异常请注意！==========");
        ResultGeekQ resultGeekQ = ResultGeekQ.build();
        if(e instanceof BindException){
            BindException ex = (BindException)e;
            List<ObjectError> errorList = ex.getAllErrors();
            //找到第一个错误
            ObjectError error = errorList.stream().findFirst().get();
            String msg = error.getDefaultMessage();
            logger.error("======异常信息为==== error:{}",msg);
            resultGeekQ.withErrorArgs(ResultStatus.BIND_ERROR.getCode(), msg, error);
        }else{
            resultGeekQ.withErrorCodeAndMessage(ResultStatus.SYSTEM_ERROR);
        }
        return resultGeekQ;
    }
}
