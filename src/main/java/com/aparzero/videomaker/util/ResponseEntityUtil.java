package com.aparzero.videomaker.util;

import com.aparzero.videomaker.domain.Response;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityUtil {

    public static ResponseEntity<Response> successResponse(final String message,final Object data ){
        final Response response = new Response();
        response.success(message,data);
        return ResponseEntity.ok().body(response);
    }
    public static ResponseEntity<Response> fail(final String message,final int status ){
        final Response response = new Response();
        response.fail(message,status);
        final HttpStatus httpStatus = 500 >= status?  HttpStatus.BAD_REQUEST : HttpStatus.GATEWAY_TIMEOUT;
        return new ResponseEntity<>(response, httpStatus);
    }
}
