package com.sunli.sunli0112.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sunli.sunli0112.R;
import com.sunli.sunli0112.bean.ShopCarBean;

import java.util.ArrayList;
import java.util.List;

public class SellerAdapter extends RecyclerView.Adapter<SellerAdapter.SellerViewHolder> {

    private List<ShopCarBean.DataBean> dataBeanList = new ArrayList<>();
    private Context dataContext;

    public SellerAdapter(Context dataContext) {
        this.dataContext = dataContext;
    }

    @NonNull
    @Override
    public SellerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(dataContext, R.layout.adapter_item_seller, null);
        SellerViewHolder sellerViewHolder = new SellerViewHolder(view);
        return sellerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SellerViewHolder sellViewHolder, final int i) {
        sellViewHolder.seller_name.setText(dataBeanList.get(i).getSellerName());

        final SellerThingsAdapter sellerThingsAdapter = new SellerThingsAdapter(dataBeanList.get(i).getList(), dataContext);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(dataContext);
        sellViewHolder.seller_recycler.setLayoutManager(linearLayoutManager);
        sellViewHolder.seller_recycler.setAdapter(sellerThingsAdapter);

        sellViewHolder.seller_check.setChecked(dataBeanList.get(i).isCheck());

        sellerThingsAdapter.setListener(new SellerThingsAdapter.CarCallBackListener() {
            @Override
            public void callBack() {
                if (carCallBackListener != null) {
                    carCallBackListener.callBack(dataBeanList);
                }

                List<ShopCarBean.DataBean.ListBean> listBeans = dataBeanList.get(i).getList();
                boolean isAllChecked = true;
                for (ShopCarBean.DataBean.ListBean bean : listBeans) {
                    if (!bean.isCheck()) {
                        isAllChecked = false;
                        break;
                    }
                }

                sellViewHolder.seller_check.setChecked(isAllChecked);
                dataBeanList.get(i).setCheck(isAllChecked);
            }
        });

        sellViewHolder.seller_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBeanList.get(i).setCheck(sellViewHolder.seller_check.isChecked());
                sellerThingsAdapter.selectOrRemoveAll(sellViewHolder.seller_check.isChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataBeanList.size();
    }

    public class SellerViewHolder extends RecyclerView.ViewHolder {
        RecyclerView seller_recycler;
        TextView seller_name;
        CheckBox seller_check;
        public SellerViewHolder(@NonNull View itemView) {
            super(itemView);
            seller_recycler = itemView.findViewById(R.id.seller_recycler);
            seller_name = itemView.findViewById(R.id.item_text_sellerName);
            seller_check = itemView.findViewById(R.id.checked_seller);
        }
    }

    public void setList(List<ShopCarBean.DataBean> list) {
        this.dataBeanList = list;
        notifyDataSetChanged();
    }

    private CarCallBackListener carCallBackListener;

    public void setListener(CarCallBackListener listener) {
        this.carCallBackListener = listener;
    }

    public interface CarCallBackListener {
        void callBack(List<ShopCarBean.DataBean> list);
    }
}
