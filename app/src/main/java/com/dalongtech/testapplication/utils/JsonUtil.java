package com.dalongtech.testapplication.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;


public class JsonUtil {
    private static Gson gson;
    private static JsonParser jsonParser = new JsonParser();

    static {
        gson = new GsonBuilder()
                .create();
    }

    public static String toJson(Object src) {
        return gson.toJson(src);
    }

    public static String toJson(Object src, Type typeOfSrc) {
        return gson.toJson(src, typeOfSrc);
    }


    public static <T> T fromJson(String json, Class<T> classOfT) {
        Object result = null;

        try {
            result = gson.fromJson(json, classOfT);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return (T) result;
    }

    public static <T> T fromJson(JsonObject json, Class<T> classOfT) {
        Object result = null;

        try {
            result = gson.fromJson(json, classOfT);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return (T) result;
    }

    public static <T> T fromJson(String json, Type type) {
        Object result = null;

        try {
            result = gson.fromJson(json, type);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return (T) result;
    }

    public static JsonElement parseToJsonObject(String jsonObj) {
        return jsonParser.parse(jsonObj);
    }

    public static Gson getGson() {
        return gson;
    }
}
