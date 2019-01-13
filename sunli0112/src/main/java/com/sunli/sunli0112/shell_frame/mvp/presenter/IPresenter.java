package com.sunli.sunli0112.shell_frame.mvp.presenter;

import java.util.Map;

/**
 * @Author sunli
 * @Data 2019/1/12
 */
public interface IPresenter {
    void startRequestGet(String urlStr, String params, Class clazz);
    void startRequestPost(String urlStr, Map<String,String> params, Class clazz);
}
