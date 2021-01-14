package com.src.Module.User.presenter;

import com.src.Model.User;
import com.src.Module.User.model.ModelProfile;
import com.src.Module.User.view.IViewProfile;

public class PresenterProfile implements IPresenterProfile{
    private IViewProfile iViewProfile;
    private ModelProfile modelProfile;

    public PresenterProfile(IViewProfile iViewProfile) {
        this.iViewProfile = iViewProfile;
        modelProfile = new ModelProfile();
    }

    @Override
    public void getProfile() {
        modelProfile.profile(this);
    }

    @Override
    public void resultGetProfile(boolean success, User user, String msg) {
        if(success){
            iViewProfile.onSuccess(user);
        }else{
            iViewProfile.onFailed(msg);
        }
    }



    @Override
    public void changePassword(String oldPassword, String newPassword) {
        modelProfile.changePassword(oldPassword,newPassword,this);
    }

    @Override
    public void resultChangePassword(boolean success, String msg) {
        if(success){
            iViewProfile.onChangePasswordSuccess(msg);
        }else {
            iViewProfile.onChangePasswordFail(msg);
        }
    }



}
