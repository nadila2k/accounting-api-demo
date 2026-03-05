package com.nadila.accounting_app_demo.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)  // Hides null fields from response
public class ApiResponse<T> {

    private final int status;
    private final boolean success;
    private final String message;
    private final T data;
    private final List<String> errors;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;

    // ✅ Success with data
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message("Operation successful")
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // ✅ Success with custom message + data
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // ✅ Success with custom status + message + data (e.g. 201 Created)
    public static <T> ApiResponse<T> success(HttpStatus httpStatus, String message, T data) {
        return ApiResponse.<T>builder()
                .status(httpStatus.value())
                .success(true)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // ✅ Error with single message
    public static <T> ApiResponse<T> error(HttpStatus httpStatus, String message) {
        return ApiResponse.<T>builder()
                .status(httpStatus.value())
                .success(false)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    // ✅ Error with multiple validation errors
    public static <T> ApiResponse<T> error(HttpStatus httpStatus, String message, List<String> errors) {
        return ApiResponse.<T>builder()
                .status(httpStatus.value())
                .success(false)
                .message(message)
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
