package com.java.mod.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理类，用于统一处理控制器中抛出的异常
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理 MethodArgumentNotValidException 异常
     * 当控制器中的参数绑定和验证失败时触发此异常
     *
     * @param ex 验证异常
     * @return 包含字段名称及其对应错误消息的HTTP响应
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    /**
     * 处理 ResourceNotFoundException 异常
     * 当资源未找到时触发此异常
     *
     * @param ex 资源未找到异常
     * @return 包含错误消息的HTTP响应，状态码为 404 Not Found
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
