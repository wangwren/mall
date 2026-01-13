/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有,侵权必究!
 */

package com.core.mall.common.exception;

import com.core.mall.common.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.exceptions.PersistenceException;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 *
 * 用于统一处理各类异常，确保返回标准的 JSON 响应格式
 *
 * @author Claude Code
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(RenException.class)
    public Result handleRenException(RenException ex) {
        log.error("业务异常: code={}, msg={}", ex.getCode(), ex.getMsg(), ex);

        Result result = new Result();
        result.error(ex.getCode(), ex.getMsg());
        return result;
    }

    /**
     * 处理数据库唯一键冲突异常
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleDuplicateKeyException(DuplicateKeyException ex) {
        log.error("数据库唯一键冲突", ex);

        Result result = new Result();
        result.error(ErrorCode.DB_RECORD_EXISTS);
        return result;
    }

    /**
     * 处理 MyBatis 系统异常
     * 例如：Mapped Statements collection does not contain value
     */
    @ExceptionHandler(MyBatisSystemException.class)
    public Result handleMyBatisSystemException(MyBatisSystemException ex) {
        log.error("MyBatis系统异常", ex);

        String errorMsg = "数据库操作失败";

        // 提取具体的错误信息
        Throwable cause = ex.getCause();
        if (cause != null) {
            String causeMsg = cause.getMessage();
            if (causeMsg != null) {
                if (causeMsg.contains("does not contain value")) {
                    errorMsg = "数据库映射配置错误，请联系管理员";
                } else if (causeMsg.contains("parameter")) {
                    errorMsg = "数据库操作参数错误";
                }
            }
        }

        return new Result().error(ErrorCode.INTERNAL_SERVER_ERROR, errorMsg);
    }

    /**
     * 处理 MyBatis 持久化异常
     */
    @ExceptionHandler(PersistenceException.class)
    public Result handlePersistenceException(PersistenceException ex) {
        log.error("MyBatis持久化异常", ex);
        return new Result().error(ErrorCode.INTERNAL_SERVER_ERROR, "数据持久化失败");
    }

    /**
     * 处理数据访问异常
     */
    @ExceptionHandler(DataAccessException.class)
    public Result handleDataAccessException(DataAccessException ex) {
        log.error("数据访问异常", ex);
        return new Result().error(ErrorCode.INTERNAL_SERVER_ERROR, "数据访问失败");
    }

    /**
     * 处理参数校验异常 (@Valid)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        log.error("参数校验失败: {}", errorMsg);
        return new Result().error(ErrorCode.PARAMS_GET_ERROR, errorMsg);
    }

    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    public Result handleBindException(BindException ex) {
        String errorMsg = ex.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        log.error("参数绑定失败: {}", errorMsg);
        return new Result().error(ErrorCode.PARAMS_GET_ERROR, errorMsg);
    }

    /**
     * 处理参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.error("参数类型不匹配: 参数名={}, 期望类型={}, 实际值={}",
                ex.getName(), ex.getRequiredType(), ex.getValue());

        String errorMsg = String.format("参数 '%s' 类型错误", ex.getName());
        return new Result().error(ErrorCode.PARAMS_GET_ERROR, errorMsg);
    }

    /**
     * 处理非法参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("非法参数异常", ex);

        String errorMsg = ex.getMessage() != null ? ex.getMessage() : "参数错误";
        return new Result().error(ErrorCode.PARAMS_GET_ERROR, errorMsg);
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public Result handleNullPointerException(NullPointerException ex) {
        log.error("空指针异常", ex);
        return new Result().error(ErrorCode.INTERNAL_SERVER_ERROR, "系统内部错误");
    }

    /**
     * 处理其他未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception ex) {
        log.error("未捕获的异常", ex);
        return new Result().error(ErrorCode.INTERNAL_SERVER_ERROR, "系统繁忙，请稍后重试");
    }
}