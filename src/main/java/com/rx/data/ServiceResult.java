package com.rx.data;

import org.springframework.http.HttpStatus;

/**
 * Created by multi-view on 2/11/17.
 */
public class ServiceResult<T> {

    private T value;

    private HttpStatus status;


    public ServiceResult(T value, HttpStatus status) {
        this.value = value;
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public T getValue() {
        return value;
    }

}
