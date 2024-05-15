package com.aparzero.videomaker.domain;

import lombok.Data;

@Data
public class Response {
    private int status;
    private String message;
    private Boolean success;
    private Object data;


    public  void success(final String message,final Object data ){
        this.status = 200;
        this.message = message;
        this.data = data;
        this.success = true;
    }

    public  void fail(final String message,final int status ){
        this.status = status;
        this.message = message;
        this.data = null;
        this.success = false;
    }
}
