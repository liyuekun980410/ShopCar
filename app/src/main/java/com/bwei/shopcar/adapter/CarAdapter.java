package com.bwei.shopcar.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bwei.shopcar.R;
import com.bwei.shopcar.ShopCar.view.CustomAddView;
import com.bwei.shopcar.bean.CarBean;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.ViewHolder> {
    Context context;
    CarBean carBean;
    List<CarBean.DataBean.ListBean> list=new ArrayList<>();
    List<CarBean.DataBean> mlist=new ArrayList<>();
    private LayoutInflater inflater;

    public CarAdapter(Context context, CarBean carBean) {
        this.context = context;
        this.carBean = carBean;
        this.inflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View inflate = inflater.inflate(R.layout.layout_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(inflate);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CarAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.text1.setText(mlist.get(i).getSellerName());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        viewHolder.shangpin.setLayoutManager(linearLayoutManager);
        //list = carBean.getData().get(i).getList();
        final ShangpinAdapter shangpinAdapter = new ShangpinAdapter(context, mlist.get(i).getList());
        viewHolder.shangpin.setAdapter(shangpinAdapter);

        viewHolder.check.setChecked(mlist.get(i).isCheck());
        shangpinAdapter.setListener(new ShangpinAdapter.ShopCallBackListener() {
            @Override
            public void callBack() {
                //从商品适配里回调回来，回给activity，activity计算价格和数量
                if (carCallBackListener!=null){
                    carCallBackListener.callBack(mlist);
                }

                List<CarBean.DataBean.ListBean> listBeans =mlist.get(i).getList();
                //创建一个临时的标志位，用来记录现在点击的状态
                boolean isAllChecked=true;
                for (CarBean.DataBean.ListBean bean:listBeans){
                    if (!bean.isCheck()){
                        //只要有一个商品未选中，标志位设置成false，并且跳出循环
                        isAllChecked=false;
                        break;
                    }
                }

                //刷新商家的状态
                viewHolder.check.setChecked(isAllChecked);
                mlist.get(i).setCheck(isAllChecked);
            }
        });

        //监听checkBox的点击事件
        //目的是改变旗下所有商品的选中状态
        viewHolder.check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //首先改变自己的标志位
                mlist.get(i).setCheck(viewHolder.check.isChecked());
                //调用产品adapter的方法，用来全选和反选
                shangpinAdapter.selectOrRemoveAll(viewHolder.check.isChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public void setList(List<CarBean.DataBean> list){
        this.mlist=list;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private final CheckBox check;
        private final TextView text1;
        private final RecyclerView shangpin;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            check = itemView.findViewById(R.id.check);
            text1 = itemView.findViewById(R.id.text1);
            shangpin = itemView.findViewById(R.id.shangpin);
        }
    }

    private CarCallBackListener carCallBackListener;

    public void setListener(CarCallBackListener listener){
        this.carCallBackListener=listener;
    }
    public interface CarCallBackListener{
        void callBack(List<CarBean.DataBean> list);
    }
}
