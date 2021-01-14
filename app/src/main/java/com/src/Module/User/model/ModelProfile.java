package com.src.Module.User.model;

import android.util.Log;

import com.src.Model.BaseResponse;
import com.src.Model.ErrorResponse;
import com.src.Model.User;
import com.src.Module.User.presenter.PresenterProfile;
import com.src.Network.APIFsBs;
import com.src.Network.IApiFsBs;
import com.src.Utils.ErrorUtils;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModelProfile {
    private IApiFsBs apiService = APIFsBs.getAPIProduct().create(IApiFsBs.class);

    public void profile(PresenterProfile presenterProfile) {
        Call<BaseResponse<User>> callProfile = apiService.getProfile();
        callProfile.enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(@NotNull Call<BaseResponse<User>> call, @NotNull Response<BaseResponse<User>> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    presenterProfile.resultGetProfile(true, response.body().getData(), null);
                } else {
                    ErrorResponse err = ErrorUtils.parseError(response);
                    presenterProfile.resultGetProfile(false, null, err.getErr());
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseResponse<User>> call, @NotNull Throwable t) {
                presenterProfile.resultGetProfile(false, null, t.getMessage());

            }
        });
    }


    public void changePassword(String odlPassword, String newPassword, PresenterProfile presenterProfile) {
        Call<BaseResponse<String>> callChangePass = apiService.handlerChangePassword(odlPassword, newPassword);
        callChangePass.enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                Log.d("FsBs", "onResponse: r" +response.code());
                if (response.isSuccessful()) {
                    presenterProfile.resultChangePassword(true, response.body().getData());
                } else {
                    ErrorResponse err = ErrorUtils.parseError(response);
                    presenterProfile.resultChangePassword(false, err.getErr());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                presenterProfile.resultChangePassword(false, t.getMessage());

            }
        });
    }
}
