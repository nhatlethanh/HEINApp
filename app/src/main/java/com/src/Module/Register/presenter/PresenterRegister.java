package com.src.Module.Register.presenter;

import com.src.Model.User;
import com.src.Module.Register.model.ModelRegister;
import com.src.Module.Register.view.IViewRegister;

public class PresenterRegister implements IPresenterRegister {
    IViewRegister iViewRegister;
    ModelRegister modelRegister;

    public PresenterRegister(IViewRegister iViewRegister) {
        this.iViewRegister = iViewRegister;
        modelRegister = new ModelRegister();
    }

    @Override
    public void handlerRegister(User user) {
        modelRegister.register(user,this);
    }

    @Override
    public void resultRegister(boolean success, String msg) {
        if(success){
            iViewRegister.onSuccess();
        }else {
            iViewRegister.onFailed(msg);

        }
    }
}
