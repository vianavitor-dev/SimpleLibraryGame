package com.vianavitor.simplelibrarygame.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private String path;

    public ApiResponse(boolean success, String message, T data, String path) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
        this.path = path;
    }

    public static <T> ApiResponse<T> success(T data, String message, String path) {
        return new ApiResponse<>(true, message, data, path);
    }

    public static <T> ApiResponse<T> success(T data, String path) {
        return new ApiResponse<>(true, "Operation successful", data, path);
    }

    public static <T> ApiResponse<T> error(String message, String path) {
        return new ApiResponse<>(false, message, null, path);
    }

    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getPath() { return path; }
}