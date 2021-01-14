package com.src.Module.User.presenter;

import okhttp3.MultipartBody;
import com.src.Model.User;

public interface IPresenterProfile {
    void getProfile();
    void resultGetProfile(boolean success, User user, String msg);
    void changePassword(String oldPassword, String newPassword);
    void resultChangePassword(boolean success,String msg);
}
