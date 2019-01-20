package com.bwei.shopcar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bwei.shopcar.ShopCar.presenter.Presenter;
import com.bwei.shopcar.adapter.CarAdapter;
import com.bwei.shopcar.bean.CarBean;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Presenter presenter;
    private RecyclerView recycle;
    private CarBean carBean;
    private CarAdapter carAdapter;
    private CheckBox mlvCirle;
    private TextView mAllPriceTxt;
    private TextView nSumPrice;
    private List<CarBean.DataBean> mList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        getData();
    }

    private void init() {
        recycle = findViewById(R.id.recycle);

        mlvCirle = findViewById(R.id.iv_cricle);
        mAllPriceTxt = findViewById(R.id.all_price);
        nSumPrice = findViewById(R.id.sum_price_txt);
        mlvCirle.setOnClickListener(this);
    }

    public void getViewData(Object obj){
        if (obj instanceof CarBean){
            carBean= (CarBean) obj;
            mList=carBean.getData();
            if (mList!=null){
                mList.remove(0);
                carAdapter.setList(mList);
            }
        }
    }

    private void getData() {
        presenter = new Presenter(this);
        presenter.getPresentData();
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        recycle.setLayoutManager(linearLayoutManager);

        carAdapter = new CarAdapter(MainActivity.this,carBean);
        recycle.setAdapter(carAdapter);
        carAdapter.setListener(new CarAdapter.CarCallBackListener() {
            @Override
            public void callBack(List<CarBean.DataBean> list) {
                //在这里重新遍历已经改状态后的数据
                //这里不能break跳出，因为还需要计算后面点击商品的价格和数目，所以必须跑完整个循环
                double totalPrice=0;

                //勾选商品的数量，不是该商品购买的数量
                int num=0;
                //所有商品总数，和上面的数量做比对，如果两者相等，则说明全选
                int totalNum=0;
                for (int a=0;a<list.size();a++){
                    //获取商家里的商品
                    List<CarBean.DataBean.ListBean> listAll =list.get(a).getList();
                    for (int i=0;i<listAll.size();i++){
                        totalNum=totalNum+Integer.parseInt(listAll.get(i).getNum());
                        double aaa = Double.valueOf(listAll.get(i).getPrice());
                        int bbb = Integer.parseInt(listAll.get(i).getNum());
                        //获取选中的状态
                        if (listAll.get(i).isCheck()){
                            totalPrice=totalPrice+(aaa*bbb);
                            num=num+Integer.parseInt(listAll.get(i).getNum());
                        }
                    }
                }
                if (num<totalNum){
                    //不是全部选中
                    mlvCirle.setChecked(false);
                }else {
                    //是全部选中
                    mlvCirle.setChecked(true);
                }
                mAllPriceTxt.setText("合计:"+totalPrice);
                nSumPrice.setText("去结算("+num+")");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter!=null){
            presenter.onDestory();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_cricle:
                checkSeller(mlvCirle.isChecked());
                carAdapter.notifyDataSetChanged();
                break;
            default:
        }
    }
    //修改选中状态，获取价格和数量
    private void checkSeller(boolean bool){
        double totalPrice=0;
        int num=0;
        for (int a=0;a<mList.size();a++){
            //遍历商家，改变状态
            CarBean.DataBean dataBean = mList.get(a);
            dataBean.setCheck(bool);
            List<CarBean.DataBean.ListBean> listAll = mList.get(a).getList();
            for (int i = 0; i < listAll.size(); i++) {
                //遍历商品，改变状态
                listAll.get(i).setCheck(bool);
                double aaa = Double.valueOf(listAll.get(i).getPrice());
                int bbb = Integer.parseInt(listAll.get(i).getNum());
                totalPrice = totalPrice + (aaa* bbb);
                num = num +Integer.parseInt( listAll.get(i).getNum());
            }
        }
        if (bool) {
            mAllPriceTxt.setText("合计：" + totalPrice);
            nSumPrice.setText("去结算(" + num + ")");
        } else {
            mAllPriceTxt.setText("合计：0.00");
            nSumPrice.setText("去结算(0)");
        }
    }
}
