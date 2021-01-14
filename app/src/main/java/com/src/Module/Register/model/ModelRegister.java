package com.src.Module.Register.model;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.src.Model.BaseResponse;
import com.src.Model.ErrorResponse;
import com.src.Model.User;
import com.src.Module.Register.presenter.PresenterRegister;
import com.src.Network.APIFsBs;
import com.src.Network.IApiFsBs;
import com.src.Utils.ErrorUtils;

public class ModelRegister {
    public void register(User user, final PresenterRegister presenterRegister) {
        IApiFsBs apiService = APIFsBs.getAPIProduct().create(IApiFsBs.class);
        Call<BaseResponse<User>> call = apiService.handlerRegister(user.getEmail(), user.getPassword(),
                user.getPhone(), user.getAddress(), user.getUsername());
        call.enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(Call<BaseResponse<User>> call, Response<BaseResponse<User>> response) {
                if (response.isSuccessful()) {
                    presenterRegister.resultRegister(true, "Đăng ký thành công");
                } else {
                    ErrorResponse err = ErrorUtils.parseError(response);
                    presenterRegister.resultRegister(false, err.getStatusCode() + " - " + err.getErr());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<User>> call, Throwable t) {
                presenterRegister.resultRegister(false, t.getMessage());

            }
        });
    }
}

