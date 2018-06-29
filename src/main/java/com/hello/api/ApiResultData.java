package com.hello.api;

import java.util.Arrays;
import java.util.List;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;


@Data
@JacksonXmlRootElement(namespace="com.hello" , localName="response")
public class ApiResultData {
    
    @JsonIgnore
    private HttpStatus status;
    
    @JacksonXmlProperty
    private String message;
    
    @JacksonXmlProperty
    private int code;
    
    @JacksonXmlElementWrapper(localName = "errors")
    @JacksonXmlCData
    @JacksonXmlProperty(localName="error" )
    private List<String> errors;
    
    
    @JacksonXmlCData
    @JacksonXmlProperty(localName="datas" )
    private List<?> datas;
    
    public ApiResultData(HttpStatus status) {
        this(status, status.getReasonPhrase(), status.value(), "");
    }
    
    public ApiResultData(HttpStatus status, String error) {
        this(status, status.getReasonPhrase(), status.value(), error);
    }
    
    public ApiResultData(HttpStatus status, List<String> errors) {
        this(status, status.getReasonPhrase(), status.value(), errors);
    }

    public ApiResultData(HttpStatus status, String message, int code, List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.code = code;
        this.errors = errors;
    }

    public ApiResultData(HttpStatus status, String message, int code, String error) {
        super();
        this.status = status;
        this.message = message;
        this.code = code;
        errors = Arrays.asList(error);
    }
}
