package com.sunli.sunli0112;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.gavin.com.library.StickyDecoration;
import com.gavin.com.library.listener.GroupListener;
import com.sunli.sunli0112.adapter.SellerAdapter;
import com.sunli.sunli0112.bean.ShopCarBean;
import com.sunli.sunli0112.shell_frame.mvp.presenter.IPresenterImpl;
import com.sunli.sunli0112.shell_frame.mvp.view.IView;
import com.sunli.sunli0112.shell_frame.network.ApiUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity implements IView {

    private IPresenterImpl iPresenter;
    private Unbinder bind;
    @BindView(R.id.activity_main_recyclerview_car)
    CustomRecyclerView recyclerView_car;
    @BindView(R.id.activity_main_ck_all)
    CheckBox ck_all;
    @BindView(R.id.activity_main_text_totalPrice)
    TextView text_totalPrice;
    @BindView(R.id.activity_main_btn_totalNum)
    Button btn_totalNum;
    @BindView(R.id.activity_main_text_editor)
    TextView text_editor;

    private SellerAdapter sellerAdapter;
    private List<ShopCarBean.DataBean> carBeanData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iPresenter = new IPresenterImpl(this);
        bind = ButterKnife.bind(this);

        iPresenter.startRequestGet(ApiUtils.get_Url_Shop_Car, null, ShopCarBean.class);
        sellerAdapter = new SellerAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView_car.setLayoutManager(linearLayoutManager);
        recyclerView_car.setAdapter(sellerAdapter);

        GroupListener groupListener = new GroupListener() {
            @Override
            public String getGroupName(int position) {
                if(position<carBeanData.size()&&position>-1){
                    return carBeanData.get(position).getSellerName();
                }
                return null;
            }
        };
        //创建StickyDecoration，实现悬浮栏
        StickyDecoration decoration = StickyDecoration.Builder
                //初始化 加载省名
                .init(groupListener)
                //设置背景颜色
                .setGroupBackground(Color.parseColor("#48BDFF"))
                //设置悬浮的高度
                .setGroupHeight(50)
                //其条目监听
                /*.setOnClickListener(new OnGroupClickListener() {
                    @Override
                    public void onClick(int position, int id) {
                        Toast.makeText(MainActivity.this,dataList.get(position).getProvince(),Toast.LENGTH_SHORT).show();
                    }
                })*/
                .build();
        recyclerView_car.addItemDecoration(decoration);

        sellerAdapter.setListener(new SellerAdapter.CarCallBackListener() {
            @Override
            public void callBack(List<ShopCarBean.DataBean> list) {
                double totalPrice = 0;
                int num = 0, totalNum = 0;
                for (int a = 0; a < list.size(); a++) {
                    List<ShopCarBean.DataBean.ListBean> listSellerThings = list.get(a).getList();
                    for (int i = 0; i < listSellerThings.size(); i++) {
                        totalNum += listSellerThings.get(i).getNum();
                        if (listSellerThings.get(i).isCheck()) {
                            totalPrice += listSellerThings.get(i).getPrice() * listSellerThings.get(i).getNum();
                            num += listSellerThings.get(i).getNum();
                        }
                    }
                }

                if (num < totalNum) {
                    ck_all.setChecked(false);
                } else {
                    ck_all.setChecked(true);
                }

                text_totalPrice.setText("合计: ¥" + totalPrice);
                 btn_totalNum.setText("付款(" + num + ")");
            }
        });
    }

    @OnCheckedChanged(R.id.activity_main_ck_all)
    public void isCheckAll() {
        boolean checked = ck_all.isChecked();
        double totalPrice = 0;
        int num = 0;

        for (int a = 0; a < carBeanData.size(); a++) {
            ShopCarBean.DataBean dataBean = carBeanData.get(a);
            dataBean.setCheck(checked);

            List<ShopCarBean.DataBean.ListBean> listSellerThings = carBeanData.get(a).getList();

            for (int i = 0; i < listSellerThings.size(); i++) {
                listSellerThings.get(i).setCheck(checked);
                totalPrice = totalPrice + (listSellerThings.get(i).getPrice() * listSellerThings.get(i).getNum());
                num = num + listSellerThings.get(i).getNum();
            }
        }

        if (checked) {
            text_totalPrice.setText("合计: " + totalPrice);
            btn_totalNum.setText("付款(" + num + ")");
        } else {
            text_totalPrice.setText("合计: 0.00");
            btn_totalNum.setText("付款(0)");
        }
        sellerAdapter.notifyDataSetChanged();
    }
    @OnClick(R.id.activity_main_text_editor)
    public void isClickTextEditor() {
        startActivity(new Intent(MainActivity.this, SearchActivity.class));
    }

    @Override
    public void showResponseData(Object data) {
        if (data instanceof ShopCarBean) {
            ShopCarBean carBean = (ShopCarBean) data;
            carBeanData = carBean.getData();
            if (carBeanData != null) {
                carBeanData.remove(0);
                sellerAdapter.setList(carBeanData);
            }
        }
    }

    @Override
    public void showResponseFail(String e) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iPresenter.onDetach();
        bind.unbind();
    }
}
