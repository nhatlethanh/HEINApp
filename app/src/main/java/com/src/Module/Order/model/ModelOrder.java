package com.src.Module.Order.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.src.Model.BaseResponse;
import com.src.Model.ErrorResponse;
import com.src.Model.Order;
import com.src.Model.OrderDetails;
import com.src.Module.Order.presenter.PresenterOrder;
import com.src.Network.APIFsBs;
import com.src.Network.IApiFsBs;
import com.src.Utils.ErrorUtils;

public class ModelOrder {
    IApiFsBs apiService = APIFsBs.getAPIProduct().create(IApiFsBs.class);
        /// object sang json và ngược lại
    // chuyển json thành object java
    public void addToCart(Order order, List<OrderDetails> orderDetails, PresenterOrder presenterOrder) {
        Gson gson = new GsonBuilder().create(); //khởi tạo Gson

        Call<BaseResponse<String>> callOrder = apiService.addOrder(gson.toJson(orderDetails), gson.toJson(order));

        callOrder.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if (response.isSuccessful()) { //nếu phản hồi thành công thì get data thành công
                    presenterOrder.resultAddCart(true, response.body().getData());
                } else {
                    ErrorResponse err = ErrorUtils.parseError(response);
                    presenterOrder.resultAddCart(false, err.getErr());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                presenterOrder.resultAddCart(false, t.getMessage());
            }
        });
    }
}
