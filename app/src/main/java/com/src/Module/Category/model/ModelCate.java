package com.src.Module.Category.model;

import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.src.Model.BaseResponse;
import com.src.Model.Cate;
import com.src.Model.ErrorResponse;
import com.src.Model.Product;
import com.src.Module.Category.presenter.PresenterCate;
import com.src.Network.APIFsBs;
import com.src.Network.IApiFsBs;
import com.src.Utils.ErrorUtils;

public class ModelCate {
    IApiFsBs apiService = APIFsBs.getAPIProduct().create(IApiFsBs.class);

    public void getListCate(PresenterCate presenterCate){

        Log.d("FsBs", "getListCate: ");
        Call<BaseResponse<List<Cate>>> callProduct = apiService.getListCate();
        callProduct.enqueue(new Callback<BaseResponse<List<Cate>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Cate>>> call, Response<BaseResponse<List<Cate>>> response) {
                if(response.isSuccessful()){
                    presenterCate.resultGetListCate(true,response.body().getData(),"");
                }else {
                    ErrorResponse err = ErrorUtils.parseError(response);
                    presenterCate.resultGetListCate(false, null,err.getErr());
                }

            }

            @Override
            public void onFailure(Call<BaseResponse<List<Cate>>> call, Throwable t) {
                presenterCate.resultGetListCate(false, null,t.getMessage());

            }
        });

    }
    public void listProductForCate(String cateId, PresenterCate presenterCate) {
        Log.d("FsBs", "listProductForCate: " + cateId);
        Call<BaseResponse<List<Product>>> callProduct = apiService.getListProductForCate(cateId);
        callProduct.enqueue(new Callback<BaseResponse<List<Product>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Product>>> call, Response<BaseResponse<List<Product>>> response) {
                if (response.isSuccessful()) {
                    Log.d("FsBs", "onResponse: " + response.body().getData().size());
                    presenterCate.resultGetProductForCate(true, response.body().getData(), "");
                } else {
                    ErrorResponse err = ErrorUtils.parseError(response);
                    presenterCate.resultGetProductForCate(false, null, err.getErr());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Product>>> call, Throwable t) {
                presenterCate.resultGetProductForCate(false, null, t.getMessage());
            }
        });

    }

}
