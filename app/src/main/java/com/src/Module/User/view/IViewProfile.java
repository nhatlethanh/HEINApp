package com.src.Module.User.view;

import com.src.Model.User;

public interface IViewProfile {
    void onSuccess(User user);
    void onFailed(String msg);

    void onChangePasswordSuccess(String msg);
    void onChangePasswordFail(String msg);
}
