package com.src.Module.Login.presenter;


public interface IPresenterLogin {
    void handlerLogin(String email,String password);
    void resultLogin(boolean success, String msg);
}
