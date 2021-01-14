package com.src.Module.Home.model;

import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.src.Model.BaseResponse;
import com.src.Model.Images;
import com.src.Module.Home.presenter.PresenterBanner;
import com.src.Network.APIFsBs;
import com.src.Network.EndPoint;
import com.src.Network.IApiFsBs;

public class ModelBanner {
    public void getBanner(PresenterBanner presenterBanner){
        IApiFsBs apiService = APIFsBs.getAPIProduct().create(IApiFsBs.class);
        apiService.getBanner().enqueue(new Callback<BaseResponse<List<Images>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Images>>> call, Response<BaseResponse<List<Images>>> response) {

                Log.d("FsBs", "onResponse: c" + response.code() );
                if(response.isSuccessful()){
                    String[] list = new String[response.body().getData().size()];
                    for(int i = 0; i < response.body().getData().size(); i++){
                        list[i] = EndPoint.BASE_URL_PUBLIC + response.body().getData().get(i).getUrl();
                    }
                    presenterBanner.resultGetBanner(true, list,"");
                }else {
                    presenterBanner.resultGetBanner(false, null,"" + response.code());

                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Images>>> call, Throwable t) {
                presenterBanner.resultGetBanner(false, null,t.getMessage());

            }
        });
    }
}

