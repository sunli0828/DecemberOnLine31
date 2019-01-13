package com.sunli.sunli0112.shell_frame.mvp.presenter;

import com.sunli.sunli0112.shell_frame.mvp.callback.ICallback;
import com.sunli.sunli0112.shell_frame.mvp.model.IModelImpl;
import com.sunli.sunli0112.shell_frame.mvp.view.IView;

import java.util.Map;

/**
 * @Author sunli
 * @Data 2019/1/12
 */
public class IPresenterImpl implements IPresenter {
    private IView iView;
    private IModelImpl iModel;

    public IPresenterImpl(IView iView) {
        this.iView = iView;
        iModel = new IModelImpl();
    }

    @Override
    public void startRequestGet(String urlStr, String params, Class clazz) {
        iModel.getRsponseDataGet(urlStr, params, clazz, new ICallback() {
            @Override
            public void setData(Object data) {
                iView.showResponseData(data);
            }

            @Override
            public void setDataFail(String e) {
                iView.showResponseFail(e);
            }
        });
    }

    @Override
    public void startRequestPost(String urlStr, Map<String, String> params, Class clazz) {
        iModel.getRsponseDataPost(urlStr, params, clazz, new ICallback() {
            @Override
            public void setData(Object data) {
                iView.showResponseData(data);
            }

            @Override
            public void setDataFail(String e) {
                iView.showResponseFail(e);
            }
        });
    }

    public void onDetach() {
        if (iModel != null) {
            iModel = null;
        }
        if (iView != null) {
            iView = null;
        }
    }
}
