package com.sunli.sunli0112;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.gavin.com.library.BaseDecoration;

/**
 * @Author sunli
 * @Data 2019/1/13
 */

public class CustomRecyclerView extends RecyclerView {
    private BaseDecoration baseDecoration;

    public CustomRecyclerView(@NonNull Context context) {
        super(context);
    }

    public CustomRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void addItemDecoration(@NonNull ItemDecoration decor) {
        if(decor!=null&&decor instanceof BaseDecoration){
            baseDecoration = (BaseDecoration) decor;
        }
        super.addItemDecoration(decor);
    }

    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        if(baseDecoration !=null){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    baseDecoration.onEventDown(event);
                    break;
                case MotionEvent.ACTION_UP:
                    if(baseDecoration.onEventUp(event)){
                        return true;
                    }
                    break;
                default:
            }
        }
        return super.onInterceptHoverEvent(event);
    }
}