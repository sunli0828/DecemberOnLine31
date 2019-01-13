package com.sunli.sunli0112.shell_frame.mvp.model;

import com.sunli.sunli0112.shell_frame.mvp.callback.ICallback;

import java.util.Map;

/**
 * @Author sunli
 * @Data 2019/1/12
 */
public interface IModel {
    void getRsponseDataGet(String urlStr, String params, Class clazz, ICallback iCallback);
    void getRsponseDataPost(String urlStr, Map<String,String> params, Class clazz, ICallback iCallback);
}
