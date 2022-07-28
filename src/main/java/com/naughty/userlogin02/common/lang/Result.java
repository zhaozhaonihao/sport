package com.naughty.userlogin02.common.lang;

import lombok.Data;

import java.io.Serializable;
@Data
public class Result implements Serializable {
    private int code;
    private String msg;
    private Object data;

    public static Result succ(Object data){
        return succ(200,"操作成功",data);
    }

    public static Result succ(int code,String msg,Object data){
        Result re=new Result();
        re.setCode(code);
        re.setData(data);
        re.setMsg(msg);
        return re;
    }

    public static Result faile(int code,String msg,Object data){
        Result re=new Result();
        re.setCode(code);
        re.setData(data);
        re.setMsg(msg);
        return re;
    }
    public static Result faile(String msg){
        return faile(400,msg,null);
    }
    public static Result faile(String msg,Object data){
        return faile(400,msg,data);
    }
}
