package com.bwei.shopcar.ShopCar.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bwei.shopcar.R;
import com.bwei.shopcar.adapter.CarAdapter;
import com.bwei.shopcar.adapter.ShangpinAdapter;
import com.bwei.shopcar.bean.CarBean;

import java.util.ArrayList;
import java.util.List;

public class CustomAddView extends RelativeLayout implements View.OnClickListener {
    private EditText mEditCar;
    private TextView jia_car;
    private TextView jian_car;
    Context context;
    private int num;
    private ShangpinAdapter shangpinAdapter;

    private List<CarBean.DataBean.ListBean> mlist=new ArrayList<>();
    private int position;

    public CustomAddView(Context context) {
        super(context);
    }

    public CustomAddView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        View view = View.inflate(context, R.layout.shop_car_price_layout, null);
        jian_car = view.findViewById(R.id.jian_car);
        jia_car = view.findViewById(R.id.jia_car);
        mEditCar = view.findViewById(R.id.edit_shop_car);
        jian_car.setOnClickListener(this);
        jia_car.setOnClickListener(this);
        addView(view);

        mEditCar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                num=Integer.parseInt(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public CustomAddView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomAddView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.jia_car:
                //改变数量，设置数量，改变对象内容，回调，局部刷新
                num++;
                mEditCar.setText(num+"");
                mlist.get(position).setNum(num+"");
                callBackListener.callBack();
                break;
            case R.id.jian_car:
                if (num>1){
                    num--;
                }else {
                    Toast.makeText(context,"商品数量不能小于1",Toast.LENGTH_SHORT).show();
                }
                mEditCar.setText(num+"");
                mlist.get(position).setNum(num+"");
                callBackListener.callBack();
                break;
            default:
                break;
        }
    }

    public void setData(ShangpinAdapter shangpinAdapter, List<CarBean.DataBean.ListBean> list, int i){
        this.mlist=list;
        this.shangpinAdapter=shangpinAdapter;
        position=i;
        num = Integer.parseInt(list.get(i).getNum());
        mEditCar.setText(num+"");
    }

    private CallBackListener callBackListener;
    public void setOnCallBack(CallBackListener listener){
        this.callBackListener=listener;
    }

    public interface CallBackListener{
        void callBack();
    }
}
