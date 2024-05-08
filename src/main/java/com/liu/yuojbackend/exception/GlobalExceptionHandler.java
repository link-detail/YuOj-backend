package com.liu.yuojbackend.exception;



import com.liu.yuojbackend.common.BaseResponse;
import com.liu.yuojbackend.common.ErrorCode;
import com.liu.yuojbackend.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice
@Slf4j
/**
 * @Author 刘渠好
 * @Date 2024-05-08 20:48
 * 全局异常处理器
 */
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    /**
     * 此处的BusinessException e就是 抛出的throw new BusinessException(...)
     */
    public BaseResponse<?> businessExceptionHandler(BusinessException e){
        log.error ("BusinessException--->",e);
        return ResultUtils.error (e.getCode (),e.getMessage ());

    }

    @ExceptionHandler(RuntimeException.class)
    /**
     * BaseResponse<?> 说明该对象的类型未知，可能会有很多类型，无关紧要
     */
    public BaseResponse<?> runtimeExceptionHandle(RuntimeException e){
        log.error ("RuntimeException--->:",e);
        return ResultUtils.error (ErrorCode.SYSTEM_ERROR);
    }
}
