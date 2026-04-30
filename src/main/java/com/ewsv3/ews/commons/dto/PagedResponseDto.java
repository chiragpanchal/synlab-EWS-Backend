package com.ewsv3.ews.commons.dto;

import org.springframework.http.HttpStatus;

public class PagedResponseDto<T> {

    private PagedDataDto<T> data;
    private String httpStatus;
    private MessageDto message;

    public PagedResponseDto() {}

    public PagedResponseDto(PagedDataDto<T> data, HttpStatus status, MessageDto message) {
        this.data = data;
        this.httpStatus = status.toString();
        this.message = message;
    }

    public PagedDataDto<T> getData() {
        return data;
    }

    public void setData(PagedDataDto<T> data) {
        this.data = data;
    }

    public String getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }

    public MessageDto getMessage() {
        return message;
    }

    public void setMessage(MessageDto message) {
        this.message = message;
    }
}
