package com.liaoyun56.template.network;

import com.google.gson.Gson;
import com.google.gson.Gson;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 返回 单个 对象   LiaoYun 2017-8-22
 * @param <T>
 */
public class ResponseVO <T> implements Serializable{
    private Integer state;                                                          //0--失败， 1--成功
    private T data;
    private String message;

    public ResponseVO() {
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResponseVO{" +
                "state=" + state +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
//==========================json字符串转 对象========================================================
    public static ResponseVO fromJson(String json, Class clazz) {
        Gson gson = new Gson();
        Type objectType = type(ResponseVO.class, clazz);
        return gson.fromJson(json, objectType);
    }

    public String toJson(Class<T> clazz) {
        Gson gson = new Gson();
        Type objectType = type(ResponseVO.class, clazz);
        return gson.toJson(this, objectType);
    }

    static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }
            public Type[] getActualTypeArguments() {
                return args;
            }
            public Type getOwnerType() {
                return null;
            }
        };
    }
}