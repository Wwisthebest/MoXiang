package com.zpark.book.Handler;

import com.zpark.book.Exception.ParamException;
import com.zpark.book.enums.ErrorMsg;
import com.zpark.book.vo.ResultVo;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    /**
     * controller的方法参数错误
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVo methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        Map<String, String> collect = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return ResultVo.fail(ErrorMsg.PARAM_ERROR, collect);
    }

    /**
     * 缺少request body错误
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVo httpMessageNotReadableExceptionHandler() {
        return ResultVo.fail(ErrorMsg.MISSING_PARAMETER, "requestBody错误!");
    }

    /**
     * url中缺少Query Params
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVo missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        return ResultVo.fail(ErrorMsg.MISSING_PARAMETER, "缺少参数" + e.getParameterName());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVo constraintViolationExceptionHandler(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> set = e.getConstraintViolations();
        Map<String, String> map = new HashMap<>();
        if (set.size() > 0) {
            for (ConstraintViolation<?> cv : set) {
                String[] param = cv.getPropertyPath().toString().split("\\.");
                String message = cv.getMessage();
                map.put(param[param.length - 1], message);
            }
        }
        return ResultVo.fail(ErrorMsg.PARAM_ERROR, map);
    }

    @ExceptionHandler(ParamException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVo paramExceptionHandler(ParamException e) {
        return ResultVo.fail(ErrorMsg.PARAM_ERROR, e.getMap());
    }

    /**
     * 拦截cookie缺失异常
     */
    @ExceptionHandler(MissingRequestCookieException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVo missingRequestCookieExceptionHandler(MissingRequestCookieException e) {
        return ResultVo.fail(ErrorMsg.COOKIE_ERROR, "缺少Cookie: " + e.getCookieName());
    }

    /**
     * 处理未捕获的其他异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultVo commonExceptionHandler(Exception e) {
        // 记录详细日志
        e.printStackTrace();
        return ResultVo.fail(ErrorMsg.SERVER_ERROR, "服务器内部错误");
    }
}
