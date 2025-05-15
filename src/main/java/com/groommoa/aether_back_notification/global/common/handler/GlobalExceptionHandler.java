package com.groommoa.aether_back_notification.global.common.handler;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.groommoa.aether_back_notification.global.common.constants.HttpStatus;
import com.groommoa.aether_back_notification.global.common.response.ErrorResponse;
import com.mongodb.MongoException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.bson.types.ObjectId;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, Object> details = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        ConstraintViolation::getMessage,
                        (existing, replacement) -> existing
                ));
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST, "잘못된 요청 파라미터입니다.", details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public ResponseEntity<Void> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex) {
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleIOException(IOException ex) {
        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, "알림 전송 중 네트워크 오류가 발생했습니다", null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex) {
        if (ex.getMessage() != null && ex.getMessage().contains("already committed")){
            ErrorResponse response = new ErrorResponse(
                    HttpStatus.CONFLICT, "이미 종료된 SSE 연결에 접근했습니다.", null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        // 다음 핸들러로 넘기기
        throw ex;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Throwable cause = ExceptionUtils.getRootCause(ex);
        if (cause == null) cause = ex.getCause();

        String message = "유효하지 않은 입력값입니다.";
        List<Map<String, String>> errors = new ArrayList<>();

        switch (cause) {
            case InvalidFormatException ife -> {
                Class<?> targetType = ife.getTargetType();
                Object invalidValue = ife.getValue();
                String field = extractFieldFromPath(ife.getPathReference());

                if (targetType.isEnum()) {
                    message = String.format("'%s'은(는) 지원하지 않는 값입니다.", invalidValue);
                    errors.add(Map.of(
                            "field", field,
                            "reason", String.format("가능한 값: %s", Arrays.toString(targetType.getEnumConstants()))
                    ));
                } else if (targetType == ObjectId.class) {
                    message = "잘못된 ObjectId 형식입니다.";
                    errors.add(Map.of(
                            "field", field,
                            "reason", String.format("'%s'은(는) 올바르지 않은 ObjectId입니다.", invalidValue)
                    ));
                } else {
                    message = "입력값 형식 오류";
                    errors.add(Map.of(
                            "field", field,
                            "reason", String.format("'%s'은(는) %s 타입으로 변환할 수 없습니다.",
                                    invalidValue, targetType.getSimpleName())
                    ));
                }
            }
            case IllegalArgumentException iae when iae.getMessage() != null -> {
                String msg = iae.getMessage();
                if (msg.contains("No enum constant")) {
                    try {
                        String[] parts = msg.split("No enum constant ")[1].split("\\.");
                        String className = String.join(".", Arrays.copyOf(parts, parts.length - 1));
                        String invalidValue = parts[parts.length - 1];
                        String simpleClassName = className.substring(className.lastIndexOf(".") + 1);

                        message = "Enum 값이 올바르지 않습니다.";
                        errors.add(Map.of(
                                "field", simpleClassName,
                                "reason", String.format("'%s'은(는) %s에서 지원하지 않는 Enum 값입니다. 대소문자나 오타를 확인해주세요.",
                                        invalidValue, simpleClassName)
                        ));
                    } catch (Exception e) {
                        message = "Enum 값이 올바르지 않습니다.";
                    }
                } else if (msg.contains("invalid hexadecimal representation")) {
                    message = "잘못된 ObjectId 형식입니다.";
                } else {
                    message = msg;
                }
            }
            case MismatchedInputException mismatchedInputException -> message = "입력값의 형식이 맞지 않습니다.";
            case JsonParseException jsonParseException -> message = "잘못된 JSON 형식입니다.";
            case null, default -> message = Objects.requireNonNullElse(cause, ex).getMessage();
        }

        Map<String, Object> details = errors.isEmpty() ? null : Map.of("errors", errors);
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST, message, details);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> Map.of(
                        "field", error.getField(),
                        "reason", error.getDefaultMessage()
                ))
                .toList();

        Map<String, Object> details = Map.of("errors", errors);

        ErrorResponse response = new ErrorResponse(
                HttpStatus.BAD_REQUEST, "유효하지 않은 입력값입니다.", details
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String field = ex.getName();
        String value = String.valueOf(ex.getValue());
        Class<?> expectedType = ex.getRequiredType();

        String expected = "유효한 값";
        if (expectedType == Integer.class || expectedType == Long.class) {
            expected = "정수 값";
        } else if (expectedType == Boolean.class) {
            expected = "true 또는 false 값";
        } else if (expectedType != null && expectedType.isEnum()) {
            expected = "다음 중 하나의 값: " + Arrays.toString(expectedType.getEnumConstants());
        }

        Map<String, Object> details = Map.of(
                "field", field,
                "reason", String.format("'%s'은(는) %s 이어야 합니다.", value, expected)
        );

        ErrorResponse response = new ErrorResponse(
                400,
                "요청 파라미터 형식이 올바르지 않습니다.",
                details
        );

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleHandlerMethodValidation(HandlerMethodValidationException ex) {
        Map<String, Object> details = new LinkedHashMap<>();

        ex.getParameterValidationResults().forEach(result -> {
            result.getResolvableErrors().forEach(error -> {
                String field = extractParamName(error);
                String reason = generateCustomMessage(error);
                details.put(field, reason);
            });
        });

        ErrorResponse response = new ErrorResponse(
                400,
                "요청 파라미터 검증에 실패했습니다.",
                details
        );
        return ResponseEntity.badRequest().body(response);
    }


    @ExceptionHandler(MongoException.class)
    public ResponseEntity<ErrorResponse> handleMongoException(MongoException ex) {
        ex.printStackTrace();
        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 오류가 발생했습니다.", null);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ex.printStackTrace();
        ErrorResponse response = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.", null);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    private String extractFieldFromPath(String pathReference) {
        if (pathReference == null) return "unknown";
        int start = pathReference.lastIndexOf("[\"") + 2;
        int end = pathReference.lastIndexOf("\"]");
        if (start > 1 && end > start) {
            return pathReference.substring(start, end);
        }
        return "unknown";
    }

    private String extractParamName(MessageSourceResolvable error) {
        String[] codes = error.getCodes();
        if (codes != null && codes.length > 0) {
            String[] parts = codes[0].split("\\.");
            return parts[parts.length - 1];
        }
        return "unknown";
    }

    private String generateCustomMessage(MessageSourceResolvable error) {
        String code = error.getCodes() != null ? error.getCodes()[0] : "";
        if (code.contains("Min")) return "0 이상의 정수여야 합니다.";
        if (code.contains("Max")) return "지정된 최대값을 초과할 수 없습니다.";
        return error.getDefaultMessage();
    }
}
