package com.example.gymsaas.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiRes<T> {

    private boolean success;
    private String message;
    private T data;

    public ApiRes() {
    }

    public ApiRes(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public static <T> ApiRes<T> success(String message, T data) {
        return new ApiRes<>(true, message, data);
    }

    public static <T> ApiRes<T> error(String message) {
        return new ApiRes<>(false, message, null);
    }

    /** Used for validation errors, where data holds the bad fields. */
    public static <T> ApiRes<T> error(String message, T data) {
        return new ApiRes<>(false, message, data);
    }
}