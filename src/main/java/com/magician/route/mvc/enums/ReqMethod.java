package com.magician.route.mvc.enums;

public enum ReqMethod {

    POST("POST"), GET("GET"), PUT("PUT"), HEAD("HEAD")
    , PATCH("PATCH"),  DELETE("DELETE")
    ,TRACE("TRACE"), CONNECT("CONNECT");

    private String code;

    ReqMethod(String code){
        this.code = code;
    }

    public String getCode(){
        return this.code;
    }
}