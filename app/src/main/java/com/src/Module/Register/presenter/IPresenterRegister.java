package com.src.Module.Register.presenter;


import com.src.Model.User;

public interface IPresenterRegister {
    void handlerRegister(User user);
    void resultRegister(boolean success, String msg);
}
