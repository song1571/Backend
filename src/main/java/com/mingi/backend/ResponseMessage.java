package com.mingi.backend;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseMessage {

    private String message;
    private String accessToken;

    public ResponseMessage(String message) {
        this.message = message;
    }

    public ResponseMessage(String message, String accessToken) {
        this.message = message;
        this.accessToken = accessToken;
    }
}
