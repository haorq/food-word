package com.meiyuan.catering.core.config;

import com.meiyuan.catering.core.exception.*;
import com.meiyuan.catering.core.util.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.List;

/**
 * @author admin
 */
@ControllerAdvice
@Order(value = Ordered.LOWEST_PRECEDENCE)
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object seriousHandler(Exception e) {
        log.error("Exception:", e);
        return Result.systemError();
    }

    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public Object customException(CustomException e) {
        log.error("CustomException:{}:{}", e.getMsg(), e);
        Result<Object> fail = Result.fail(e.getCode(), e.getMsg());
        fail.setData(e.getData());
        return fail;
    }

    @ExceptionHandler(AllinpayException.class)
    @ResponseBody
    public Object allinpayException(AllinpayException e) {
        log.error("AllinpayException:{}:{}", e.getMessage(), e);
        return Result.fail(e.getMessage());
    }

    /**
     * 统一处理 BindException 错误
     *
     * @param ex 参数验证失败错误
     * @return 参数验证失败响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Object methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("methodArgumentNotValidException:{}", ex);
        // 获取错误信息
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        // 目前消息只返回一条错误信息，所以只需要取第一条错误信息即可
        for (FieldError fieldError : fieldErrors) {
            return Result.fail(402, fieldError.getDefaultMessage());
        }
        return Result.badArgument();
    }


    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Object unauthorizedHandler(UnauthorizedException e) {
        log.error("UnauthorizedException:{}", e.getmessage());
        return Result.fail(e.getCode(), e.getmessage());
    }

    @ExceptionHandler(AdminUnauthorizedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Object unauthorizedHandler(AdminUnauthorizedException e) {
        log.error("AdminUnauthorizedException:{}", e.getmessage());
        return Result.fail(401, e.getmessage());
    }

    @ExceptionHandler(AppUnauthorizedException.class)
    @ResponseBody
    public Object appUnauthorizedHandler(AppUnauthorizedException e) {
        log.error("AppUnauthorizedException:{}", e.getmessage());
        return Result.fail(e.getCode(), e.getmessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public Object maxUploadSizeHandler(MaxUploadSizeExceededException e) {
        return Result.maxUploadSize();
    }

//
//    @ExceptionHandler(IllegalArgumentException.class)
//    @ResponseBody
//    public Object badArgumentHandler(IllegalArgumentException e) {
//        log.error("IllegalArgumentException:{}", e.getMessage());
//        return Result.badArgumentValue();
//    }
//
//    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
//    @ResponseBody
//    public Object badArgumentHandler(MethodArgumentTypeMismatchException e) {
//        log.error("MethodArgumentTypeMismatchException:{}", e.getMessage());
//        return Result.badArgumentValue();
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseBody
//    public Object badArgumentHandler(MethodArgumentNotValidException e) {
//        log.error("MethodArgumentNotValidException:{}", e.getMessage());
//        BindingResult result = e.getBindingResult();
//        List<ObjectError> allErrors = result.getAllErrors();
//        for (ObjectError error : allErrors) {
//            return Result.fail(402, error.getDefaultMessage());
//        }
//        return Result.badArgumentValue();
//    }
//
//    @ExceptionHandler(MissingServletRequestParameterException.class)
//    @ResponseBody
//    public Object badArgumentHandler(MissingServletRequestParameterException e) {
//        log.error("MissingServletRequestParameterException:{}", e.getMessage());
//        return Result.badArgumentValue();
//    }
//
//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    @ResponseBody
//    public Object badArgumentHandler(HttpMessageNotReadableException e) {
//        log.error("HttpMessageNotReadableException:{}", e.getMessage());
//        return Result.badArgumentValue();
//    }
//
//    @ExceptionHandler(ValidationException.class)
//    @ResponseBody
//    public Object badArgumentHandler(ValidationException e) {
//        log.error("ValidationException:{}", e.getMessage());
//        if (e instanceof ConstraintViolationException) {
//            ConstraintViolationException exs = (ConstraintViolationException) e;
//            Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
//            for (ConstraintViolation<?> item : violations) {
//                String message = ((PathImpl) item.getPropertyPath()).getLeafNode().getName() + item.getMessage();
//                return Result.fail(402, message);
//            }
//        }
//        return Result.badArgumentValue();
//    }


//    @ExceptionHandler(MyBatisSystemException.class)
//    @ResponseBody
//    public Object myBatisSystemException(MyBatisSystemException e) {
//        log.error("myBatisSystemException:", e);
//        return Result.fail("不支持特殊字符");
//    }

//    @ExceptionHandler(NullPointerException.class)
//    @ResponseBody
//    public Object seriousHandler(NullPointerException e) {
//        log.error(e.getMessage(), e);
//        return Result.serious();
//    }


}
