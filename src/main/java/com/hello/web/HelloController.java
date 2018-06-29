package com.hello.web;

import org.springframework.web.bind.annotation.RestController;

import com.hello.api.ApiResultData;
import com.hello.exception.ApiKeyWrongException;
import com.hello.exception.BaseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {
    
    @RequestMapping("/")
    public String index() {
        return "Hello Spring Boot!";
    }
    @RequestMapping("/data")
    public ApiResultData data() {
        ApiResultData apiResultData = new ApiResultData(HttpStatus.OK);
        
        List<Map<String, String>> dataList =  new ArrayList<>();
        Map<String, String> data1 = new HashMap<>();
        data1.put("_id", "A1");
        dataList.add(data1);
        apiResultData.setDatas(dataList);
        return apiResultData;
    }
    @RequestMapping("/test1")
    public String test1(){
     // will be catched by global exception handler method handleBaseException
        throw new BaseException("Base Exception");
    }

    @RequestMapping("/test2")
    public String test2(){
        // will be catched by global exception handler method handleBaseException
        throw new ApiKeyWrongException();
    }

    @RequestMapping("/test3")
    public String test3(){
        // will be catched by global exception handler method handleException
        throw new NullPointerException("null pointer exception");
    }
}
