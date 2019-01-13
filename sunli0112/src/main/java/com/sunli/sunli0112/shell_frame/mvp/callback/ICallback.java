package com.sunli.sunli0112.shell_frame.mvp.callback;

/**
 * @Author sunli
 * @Data 2019/1/12
 */
public interface ICallback<T> {
    void setData(T data);
    void setDataFail(String e);
}
