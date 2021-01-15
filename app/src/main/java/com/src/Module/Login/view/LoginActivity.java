package com.src.Module.Login.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.R;
import com.google.android.material.textfield.TextInputEditText;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;

import es.dmoral.toasty.Toasty;

import com.src.Navigation2Activity;
import com.src.NavigationActivity;
import com.src.Module.Login.presenter.PresenterLogin;
import com.src.Module.Register.view.RegisterActivity;
import com.src.Utils.DialogLoading;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener,IViewLogin {
    private LinearLayout bodyLogin,signUpNew;
    private Button btnLogin,btnBack;
    private PresenterLogin presenterLogin;
    private TextInputEditText edtEmailLogin,edtPasswordLogin;
    private GoogleProgressBar googleProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        init();

    }



    @SuppressLint("CutPasteId")
    private void init() {
        bodyLogin = findViewById(R.id.bodyLogin);
        signUpNew = findViewById(R.id.signUpNew);
        btnLogin = findViewById(R.id.btnLogin);
        btnBack = findViewById(R.id.btnBack);
        edtEmailLogin = findViewById(R.id.edtEmailLogin);
        edtPasswordLogin = findViewById(R.id.edtPasswordLogin);
        googleProgressBar = findViewById(R.id.google_progress);
        presenterLogin = new PresenterLogin(this,getApplicationContext());
//
        edtEmailLogin.setText("@gmail.com");
//        edtPasswordLogin.setText("123456");
        btnLogin.setOnClickListener(this);
        signUpNew.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpNew:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.btnLogin:
                if(checkValid()){
                    DialogLoading.LoadingGoogle(true,googleProgressBar);
                    presenterLogin.handlerLogin(edtEmailLogin.getText().toString().trim(),edtPasswordLogin.getText().toString().trim());
                }
                break;
            case  R.id.btnBack:
                startActivity(new Intent(LoginActivity.this, Navigation2Activity.class));
                break;
        }
    }

    @Override
    public void onSuccess() {
        DialogLoading.LoadingGoogle(false,googleProgressBar);
        Toasty.success(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT, true).show();
        startActivity(new Intent(LoginActivity.this, NavigationActivity.class));
        finish();
    }

    @Override
    public void onFailed(String msg) {
        DialogLoading.LoadingGoogle(false,googleProgressBar);
        Toasty.error(LoginActivity.this, "Lỗi đăng nhập", Toast.LENGTH_SHORT, true).show();

    }

    private boolean checkValid(){
        String email = edtEmailLogin.getText().toString().trim();
        String password = edtPasswordLogin.getText().toString().trim();
        if(email.length() == 0){
            edtEmailLogin.setError("Email Invalid");
            edtEmailLogin.requestFocus();
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmailLogin.setError("Email Invalid");
            edtEmailLogin.requestFocus();
            return false;

        }else if (password.length() == 0) {
            edtPasswordLogin.setError("Password Invalid");
            edtPasswordLogin.requestFocus();
            return false;
        }
        return  true;
    }

}
