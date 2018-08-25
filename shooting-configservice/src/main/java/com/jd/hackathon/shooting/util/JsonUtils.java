package com.jd.hackathon.shooting.util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * @author dubiaopei
 * @Description: json格式判断
 * @date 2018/6/2 18:10
 */
public class JsonUtils {

    /**
     * 暴力解析:Alibaba fastjson
     * @param test
     * @return
     */
    public final static boolean isJSONValid(String test) {
        try {
            JSONObject.parseObject(test);
        } catch (JSONException ex) {
            try {
                JSONObject.parseArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

//    private static boolean isGoodGson(String targetStr,Class clazz) {
//        if(StringUtils.isBlank(targetStr)){
//            return false;
//        }
//        try {
//            new Gson().fromJson(targetStr,clazz);
//            return true;
//        } catch(JsonSyntaxException ex) {
//            LOG.error("targetStr={} is not a valid {}",targetStr,clazz.getName(),ex);
//            return false;
//        }
//    }

    /**
     * 是否是合法的JsonArray (alibaba 认为前1种不是JSON串)
     * 例如：[{a:b}]  [{'a':'b'}]  [{"a":"b"}]
     * @param targetStr
     * @return
     */
    public static boolean isJsonArray(String targetStr){
//        return isGoodGson(targetStr,JsonArray.class);
        return isJSONValid(targetStr);
    }

    /**
     * 是否是合法的JsonObject(alibaba 认为前1种不是JSON串)
     * 例如：{a:b} {'a':'b'} {"a":"b"}
     * @param targetStr
     * @return
     */
    public static boolean isJsonObject(String targetStr){
//        return isGoodGson(targetStr,JsonObject.class);
        return isJSONValid(targetStr);
    }
}
