package com.example.educational_management_system.common.exception;

import com.example.educational_management_system.common.Result;
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
    @ExceptionHandler({ServiceException.class, ForeignKeyException.class})
    private Result customException(RuntimeException ex) {
        log.info(ex.getMessage());
        return Result.error().message(ex.getMessage());
    }

    // UNIQUE约束 异常
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    private Result uniqueKeyRepeat(Exception ex) {
        String errorProperty = ex.getMessage().split(" ")[12].split("\'")[1];
        errorProperty = errorProperty.replaceFirst("PRIMARY", "id");   //额外判断主键

        log.info(errorProperty + "在表中已存在, 请更换" + errorProperty);
        return Result.error().message(errorProperty + "在表中已存在, 请更换" + errorProperty);
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
    private Result nullPointer(Exception ex) {
        log.info("NullPointerException");
        ex.printStackTrace();
        return Result.error().message("NullPointerException");
    }


    @ExceptionHandler(SQLException.class)
    private Result notNullConstraint() {
        log.info("SQLException");
        return Result.error().message("SQLException 字段不能为空/日期有问题");
    }

    @ExceptionHandler(SQLSyntaxErrorException.class)
    private Result onlyId() {
        log.info("SQLSyntaxErrorException");
        return Result.error().message("SQLSyntaxErrorException, 可能只传入了一个id/字段名不存在-可能拼写错误");
    }
}
