package com.hello.api;


import org.springframework.lang.Nullable;

public enum ApiCode {

    SUSSCESS(200, "success")
    ,WRONG_API_KEY(400, "wrong api key");
    
    private final int value;

    private final String reasonPhrase;

    ApiCode(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }


    /**
     * Return the integer value of this status code.
     */
    public int value() {
        return this.value;
    }

    /**
     * Return the reason phrase of this status code.
     */
    public String getReasonPhrase() {
        return this.reasonPhrase;
    }
    
    public static ApiCode valueOf(int statusCode) {
        ApiCode status = resolve(statusCode);
        if (status == null) {
            throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
        }
        return status;
    }
    
    @Nullable
    public static ApiCode resolve(int statusCode) {
        for (ApiCode status : values()) {
            if (status.value == statusCode) {
                return status;
            }
        }
        return null;
    }
}
