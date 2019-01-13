package com.sunli.sunli0112.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sunli.sunli0112.R;
import com.sunli.sunli0112.bean.ShopCarBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SellerThingsAdapter extends RecyclerView.Adapter<SellerThingsAdapter.SellerTingsViewHolder> {

    private List<ShopCarBean.DataBean.ListBean> listBeanList;
    private Context listBeanContext;

    public SellerThingsAdapter(List<ShopCarBean.DataBean.ListBean> listBeanList, Context listBeanContext) {
        this.listBeanList = listBeanList;
        this.listBeanContext = listBeanContext;
    }

    @NonNull
    @Override
    public SellerTingsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = View.inflate(listBeanContext, R.layout.adapter_item_seller_things, null);
        SellerTingsViewHolder sellerTingsViewHolder = new SellerTingsViewHolder(view);
        return sellerTingsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SellerTingsViewHolder sellerTingsViewHolder, final int i) {
        String url = listBeanList.get(i).getImages().split("\\|")[0].replace("https", "http");
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(url)
                .build();
        sellerTingsViewHolder.sellerthings_icon.setController(controller);
        sellerTingsViewHolder.sellerthings_icon.setScaleType(ImageView.ScaleType.FIT_XY);
        sellerTingsViewHolder.sellerthings_title.setText(listBeanList.get(i).getTitle());
        sellerTingsViewHolder.sellerthings_price.setText(listBeanList.get(i).getPrice() + "");

        sellerTingsViewHolder.sellerthings_ckAll.setChecked(listBeanList.get(i).isCheck());

        sellerTingsViewHolder.sellerthings_ckAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listBeanList.get(i).setCheck(isChecked);
                if (carCallBackListener != null) {
                    carCallBackListener.callBack();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBeanList.size();
    }

    public class SellerTingsViewHolder extends RecyclerView.ViewHolder {
        CheckBox sellerthings_ckAll;
        SimpleDraweeView sellerthings_icon;
        TextView sellerthings_title;
        TextView sellerthings_price;

        public SellerTingsViewHolder(@NonNull View itemView) {
            super(itemView);
            sellerthings_ckAll = itemView.findViewById(R.id.item_things_check_product);
            sellerthings_icon = itemView.findViewById(R.id.item_things_icon_product);
            sellerthings_title = itemView.findViewById(R.id.item_things_text_product_title);
            sellerthings_price = itemView.findViewById(R.id.item_things_text_product_price);
        }
    }

    public void selectOrRemoveAll(boolean isSelectedAll) {
        for (ShopCarBean.DataBean.ListBean listBean : listBeanList) {
            listBean.setCheck(isSelectedAll);
        }
        notifyDataSetChanged();
    }

    private CarCallBackListener carCallBackListener;

    public void setListener(CarCallBackListener listener) {
        this.carCallBackListener = listener;
    }

    public interface CarCallBackListener {
        void callBack();
    }
}
