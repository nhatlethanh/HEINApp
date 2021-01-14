package com.src.Module.Login.model;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.src.Model.BaseResponse;
import com.src.Model.ErrorResponse;
import com.src.Model.User;
import com.src.Module.Login.presenter.PresenterLogin;
import com.src.Network.APIFsBs;
import com.src.Network.IApiFsBs;
import com.src.Utils.ErrorUtils;

public class ModelLogin {
    public void loginUser(String email, String password, final PresenterLogin presenterLogin) {
        Log.d("FsBs", "Tài khoản đăng nhập: " + email);
        IApiFsBs apiService = APIFsBs.getAPIProduct().create(IApiFsBs.class);
        Call<BaseResponse<User>> call = apiService.handlerLogin(email, password);
        call.enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(@NotNull Call<BaseResponse<User>> call, @NotNull Response<BaseResponse<User>> response) {
                //success code >= 200 <= 300
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    presenterLogin.resultLogin(true, response.body().getData().getToken());
                    Log.d("FsBs", "onResponse: " + response.body().getData().getToken());
                } else {
                    ErrorResponse err = ErrorUtils.parseError(response);
                    presenterLogin.resultLogin(false, err.getStatusCode() + " - " + err.getErr());
                }
            }
            @Override
            public void onFailure(@NotNull Call<BaseResponse<User>> call, @NotNull Throwable t) {
                presenterLogin.resultLogin(false,t.getMessage());
            }
        });
    }
}
