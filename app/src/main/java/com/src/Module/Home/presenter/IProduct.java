package com.src.Module.Home.presenter;

import java.util.List;

import com.src.Model.Product;

public interface IProduct {
    interface IPresenterProduct{
        void getListProduct();
        void getNewListProduct();
        void getListProductMore(int page);
        void onResult(boolean success, List<Product> productList, String msg);
        void onResultNewProduct(boolean success, List<Product> productList,String msg);

        void handlerSearch(String query);
        void resultSearch(boolean success, List<Product> productList);

    }
    interface IViewProduct{
        void onGetListProductSuccess(List<Product> productList);
        void onGetListProductFailed(String msg);

        void onGetListNewProductSuccess(List<Product> productList);
        void onGetListNewProductFailed(String msg);

        void onSearchProductSuccess(List<Product> productList);
        void onSearchProductFailed(String msg);
    }
}
