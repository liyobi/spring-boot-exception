package com.hello.exception.handler;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.hello.api.ApiCode;
import com.hello.api.ApiResultData;
import com.hello.exception.ApiKeyWrongException;
import com.hello.exception.BaseException;
import com.hello.util.ClientUtils;

import lombok.extern.slf4j.Slf4j;


@RestControllerAdvice()
@Slf4j
public class GlobalExceptionHandler  {

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<?> throwble(HttpServletRequest request, HttpServletResponse response, Throwable ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiResultData apiResponse = new ApiResultData(status);
        return responseAndLog(ex, request, apiResponse);
    };
    
    
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<?> baseException(HttpServletRequest request, HttpServletResponse response, BaseException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiResultData apiResponse = new ApiResultData(status);
        return responseAndLog(ex, request, apiResponse);
    };
    
    @ExceptionHandler(ApiKeyWrongException.class)
    public ResponseEntity<?> apiKeyWrongException(HttpServletRequest request, HttpServletResponse response, ApiKeyWrongException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiResultData apiResponse = new ApiResultData(status , ApiCode.WRONG_API_KEY.getReasonPhrase() , ApiCode.WRONG_API_KEY.value() , ex.getMessage() );
        return responseAndLog(ex, request, apiResponse);
    };
   
    
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handleNoHandlerFoundException(HttpServletRequest request, HttpServletResponse response, NoHandlerFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        String error = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
        //List<String> error = new ArrayList<String>();
        //error.add("No handler found for1 " + ex.getHttpMethod() + " " + ex.getRequestURL());
        //error.add("No handler found for2 " + ex.getHttpMethod() + " " + ex.getRequestURL());
        ApiResultData apiResponse = new ApiResultData(status , error);
        return responseAndLog(ex, request, apiResponse);
    }
    
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(HttpServletRequest request, HttpServletResponse response, MethodArgumentNotValidException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        List<String> errors = new ArrayList<String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        
        ApiResultData apiResponse = new ApiResultData(status , errors);
        return responseAndLog(ex, request, apiResponse);
    }


    public ResponseEntity<?> responseEntity(HttpServletRequest request , ApiResultData data ){
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<?> responseEntity = null;
        //xml
        if(MediaType.APPLICATION_XML_VALUE.equalsIgnoreCase(request.getHeader("accept"))
        || MediaType.APPLICATION_ATOM_XML_VALUE.equalsIgnoreCase(request.getHeader("accept"))
        ) {
            headers.setContentType(MediaType.APPLICATION_ATOM_XML);
            responseEntity = new ResponseEntity<ApiResultData>(data, headers, data.getStatus());
        }
        //json
        else if(MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(request.getHeader("accept"))
              || MediaType.APPLICATION_JSON_UTF8_VALUE.equalsIgnoreCase(request.getHeader("accept"))
        ){
            headers.setContentType(MediaType.APPLICATION_JSON);
            responseEntity = new ResponseEntity<ApiResultData>(data, headers, data.getStatus());
        }
        //html
        else {
            headers.setContentType(MediaType.TEXT_HTML);
             /** error page template */
               String content = 
               "<header>"
             + "<h1><span></span>" + data.getMessage()  + "</h1>"
             + "</header>";
             responseEntity = new ResponseEntity<String>(content, headers, data.getStatus()); 
        }
        
        return responseEntity;
    }

    public void log(HttpServletRequest request, Throwable ex){
        String trace = null;
        if(null != ex) {
            Throwable root = ExceptionUtils.getRootCause(ex);
            root = root == null ? ex : root;
            StackTraceElement[] stacks = root.getStackTrace();
            trace = StringUtils.join(stacks," < ");    
        }
        String ip = ClientUtils.getRemoteAddr(request);

        StringBuffer logInfo = new StringBuffer();
        logInfo.append(ex.getClass().getName());
        logInfo.append("|").append(request.getRequestURI());//URL
        logInfo.append("|").append(ip); // 접속IP
        logInfo.append("|").append(null==ex.getMessage()?"":ex.getMessage().replaceAll("\n","").replaceAll("\r", ""));    //Exception Message
        logInfo.append("|").append(trace);
        log.error(logInfo.toString());
    }
    
    public ResponseEntity<?> responseAndLog(Throwable ex, HttpServletRequest request,ApiResultData apiResponse) {
        log(request, ex);
        return responseEntity(request, apiResponse);
    }
}