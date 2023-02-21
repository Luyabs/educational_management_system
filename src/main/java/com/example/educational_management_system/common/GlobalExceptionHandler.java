package com.example.educational_management_system.common;

import com.fasterxml.jackson.core.JsonParseException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 自定义异常类
    @ExceptionHandler(ServiceException.class)
    private Result customException(ServiceException ex) {
        log.info(ex.getMessage());
        return Result.error().message(ex.getMessage());
    }

    @ExceptionHandler({SignatureException.class})
    private Result wrongJWT() {
        log.info("token错误");
        return Result.error().message("Wrong token!");
    }

    @ExceptionHandler(JsonParseException.class)
    private Result wrongJSON() {
        log.info("JSON格式错误");
        return Result.error().message("JSON格式错误!");
    }

    @ExceptionHandler(NullPointerException.class)
    private Result nullPointer() {
        log.info("NullPointerException");
        return Result.error().message("NullPointerException");
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    private Result primaryKeyRepeat() {
        log.info("属性重复");
        return Result.error().message("属性重复！");
    }

    @ExceptionHandler(SQLException.class)
    private Result notNullConstraint() {
        log.info("SQLException, 可能只传入了一个id");
        return Result.error().message("SQLException 可能有些字段不能为空 也可能日期有问题");
    }

    @ExceptionHandler(SQLSyntaxErrorException.class)
    private Result onlyId() {
        log.info("SQLSyntaxErrorException, 可能只传入了一个id");
        return Result.error().message("SQLSyntaxErrorException, 可能只传入了一个id");
    }
}
