package com.bwei.shopcar.ShopCar.presenter;

import android.util.Log;

import com.bwei.shopcar.MainActivity;
import com.bwei.shopcar.ShopCar.model.IModel;
import com.bwei.shopcar.ShopCar.model.Modelimpl;

public class Presenter implements IPresenter{
    private final Modelimpl modelimpl;
    MainActivity mainActivity;

    public Presenter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        modelimpl = new Modelimpl();
    }

    @Override
    public void getPresentData() {
        modelimpl.getModelData("http://www.zhaoapi.cn/product/getCarts?uid=71", new IModel.IModelinter() {
            @Override
            public void getSuccess(Object obj) {
                mainActivity.getViewData(obj);
            }

            @Override
            public void getFail() {

            }
        });
    }

    public void onDestory(){
        if (mainActivity!=null){
            mainActivity=null;
        }
    }
}
