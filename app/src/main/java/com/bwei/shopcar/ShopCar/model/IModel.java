package com.bwei.shopcar.ShopCar.model;

public interface IModel {
    public void getModelData(String url,IModelinter iModelinter);
    interface IModelinter{
        public void getSuccess(Object obj);

        public void getFail();
    }
}
