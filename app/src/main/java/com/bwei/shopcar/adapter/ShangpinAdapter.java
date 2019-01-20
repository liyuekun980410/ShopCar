package com.bwei.shopcar.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bwei.shopcar.R;
import com.bwei.shopcar.ShopCar.view.CustomAddView;
import com.bwei.shopcar.bean.CarBean;

import java.util.ArrayList;
import java.util.List;

public class ShangpinAdapter extends RecyclerView.Adapter<ShangpinAdapter.ViewHolder> {
    Context context;
    List<CarBean.DataBean.ListBean> list;
    LayoutInflater inflater;

    public ShangpinAdapter(Context context, List<CarBean.DataBean.ListBean> list) {
        this.context = context;
        this.list = list;
        this.inflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ShangpinAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = inflater.inflate(R.layout.car_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShangpinAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.priceTv.setText("￥:" + list.get(i).getPrice());
        viewHolder.titleTv.setText(list.get(i).getTitle());
        Glide.with(context).load(list.get(i).getImages().split("\\|")[0].replace("htttps","http")).into(viewHolder.iv);

        //根据我记录的状态，改变勾选
        viewHolder.checkbox.setChecked(list.get(i).isCheck());

        //商家的跟商家的有所不同，商品添加了选中改变的监听
        viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //优先改变自己的状态
                list.get(i).setCheck(isChecked);
                //回调，目的是告诉activity，有人选中状态被改变
                if (shopCallBackListener!=null){
                    shopCallBackListener.callBack();
                }
            }
        });

        //设置自定义View里的Edit
        viewHolder.mCustomShopCarPrice.setData(this,list,i);
        viewHolder.mCustomShopCarPrice.setOnCallBack(new CustomAddView.CallBackListener() {
            @Override
            public void callBack() {
                if (shopCallBackListener!=null){
                    shopCallBackListener.callBack();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView iv;
        private CheckBox checkbox;
        private TextView titleTv;
        private TextView priceTv;
        private final CustomAddView mCustomShopCarPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv_product);
            checkbox = itemView.findViewById(R.id.checkbox);
            titleTv = itemView.findViewById(R.id.title);
            priceTv = itemView.findViewById(R.id.price);
            mCustomShopCarPrice = itemView.findViewById(R.id.custom_product_counter);
        }
    }

    //在我们子商品的adapter中，修改子商品的全选和反选
    public void selectOrRemoveAll(boolean isSelectAll){
        for (CarBean.DataBean.ListBean listBean:list){
            listBean.setCheck(isSelectAll);
        }
        notifyDataSetChanged();
    }

    private ShopCallBackListener shopCallBackListener;

    public void setListener(ShopCallBackListener listener){
        this.shopCallBackListener=listener;
    }
    public interface ShopCallBackListener{
        void callBack();
    }
}
