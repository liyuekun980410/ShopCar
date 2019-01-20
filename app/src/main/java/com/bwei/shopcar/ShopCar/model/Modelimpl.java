package com.bwei.shopcar.ShopCar.model;

import android.util.Log;

import com.bwei.shopcar.bean.CarBean;
import com.bwei.shopcar.network.HttpUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Modelimpl implements IModel{

    @Override
    public void getModelData(String url, final IModelinter iModelinter) {
        HttpUtils.getInstance().doget(url, CarBean.class, new HttpUtils.NetCallBack() {
            @Override
            public void onSuccess(Object o) {
                Log.i("aaa", "onSuccess: "+o);
                iModelinter.getSuccess(o);
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }
}
