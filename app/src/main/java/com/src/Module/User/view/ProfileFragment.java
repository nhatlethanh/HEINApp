package com.src.Module.User.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.R;
import com.google.android.material.textfield.TextInputEditText;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;
import com.src.Model.User;
import com.src.Module.Login.view.LoginActivity;
import com.src.Module.User.presenter.PresenterProfile;
import com.src.Utils.DialogLoading;
import com.src.Utils.SplashScreenActivity;

import java.util.Objects;

import es.dmoral.toasty.Toasty;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment implements IViewProfile, View.OnClickListener {

    private PresenterProfile presenterProfile;
    private TextView txtNameProfile,txtPhoneProfile,txtAddress,txtLogout,txtEmailProfile;
    private GoogleProgressBar progressBarProfile;
    private ImageView imgProfile;
    private LinearLayout layoutChangePassword,layoutLogout;
    private String realPathImages;
    private Dialog dialogChanePass,dialogLogout;
    private  Button btnLogout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user, container, false);
        initView(v);
        DialogLoading.LoadingGoogle(true, progressBarProfile);
        presenterProfile.getProfile();

        return v;
    }

    private void initView(View v) {
        presenterProfile = new PresenterProfile(this);
        txtNameProfile = v.findViewById(R.id.txtNameProfile);
        txtPhoneProfile = v.findViewById(R.id.txtPhoneProfile);
        txtAddress = v.findViewById(R.id.txtAddress);
        txtEmailProfile = v.findViewById(R.id.txtEmailProfile);
        progressBarProfile = v.findViewById(R.id.progressBarProfile);
        imgProfile = v.findViewById(R.id.imageProfile);
        layoutLogout = v.findViewById(R.id.layoutLogout);
        layoutChangePassword = v.findViewById(R.id.layoutChangePassword);
        layoutChangePassword.setOnClickListener(this);
        btnLogout = v.findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                },500);
            }
        });


    }


    @Override
    public void onSuccess(User user) {
        if (user != null) {
            DialogLoading.LoadingGoogle(false, progressBarProfile);
            txtNameProfile.setText(user.getUsername());
            txtPhoneProfile.setText(user.getPhone());
            txtAddress.setText(user.getAddress());
            txtEmailProfile.setText(user.getEmail());
        }
    }

    @Override
    public void onFailed(String msg) {
        DialogLoading.LoadingGoogle(false, progressBarProfile);
        Toasty.error(Objects.requireNonNull(getActivity()), msg, Toast.LENGTH_SHORT, true).show();
    }

    @Override
    public void onChangePasswordSuccess(String msg) {
        DialogLoading.LoadingGoogle(false, progressBarProfile);
        dialogChanePass.dismiss();
        Toasty.success(Objects.requireNonNull(getActivity()), "Đổi mật khẩu thành công", Toasty.LENGTH_LONG).show();
        Toasty.warning(Objects.requireNonNull(getActivity()), "Vui lòng đăng nhập lại!", Toast.LENGTH_SHORT, true).show();
        SharedPreferences pref = getActivity().getSharedPreferences("User", MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = pref.edit();
        editor.putString("token", null);
        editor.apply();
        new Handler().postDelayed(() -> {
            startActivity(new Intent(getActivity(), SplashScreenActivity.class));
            getActivity().finish();
        }, 3000);
    }

    @Override
    public void onChangePasswordFail(String msg) {
        DialogLoading.LoadingGoogle(false, progressBarProfile);
        dialogChanePass.dismiss();
        Toasty.error(Objects.requireNonNull(getActivity()), msg, Toasty.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layoutChangePassword:
                handlerChangePassword();
                break;

        }

    }



    private void handlerChangePassword() {
        dialogChanePass = new Dialog(Objects.requireNonNull(getActivity()));
        dialogChanePass.setContentView(R.layout.custom_dialog_change_password);
        TextInputEditText edtOldPassword, edtNewPassword, edtNewPasswordAgain;
        TextView txtCloseDialogChangePassword;
        Button btnChangePassword = dialogChanePass.findViewById(R.id.btnChangePassword);
        edtOldPassword = dialogChanePass.findViewById(R.id.edtOldPassword);
        edtNewPassword = dialogChanePass.findViewById(R.id.edtNewPassword);
        edtNewPasswordAgain = dialogChanePass.findViewById(R.id.edtNewPasswordAgain);
        txtCloseDialogChangePassword = dialogChanePass.findViewById(R.id.txtCloseDialogChangePassword);
        btnChangePassword.setOnClickListener(v -> {
            String oldPassword = edtOldPassword.getText().toString().trim();
            String newPassword = edtNewPassword.getText().toString().trim();
            String newPasswordAgain = edtNewPasswordAgain.getText().toString().trim();
            if (edtOldPassword.getText().toString().isEmpty()) {
                edtOldPassword.setError("Empty");
            } else if (newPassword.isEmpty()) {
                edtNewPassword.setError("Empty");
            } else if (newPasswordAgain.isEmpty()) {
                edtNewPasswordAgain.setError("Empty");
            } else if (!newPasswordAgain.equals(newPassword)) {
                edtNewPasswordAgain.setError("New Password Different");
            } else {
                DialogLoading.LoadingGoogle(true, progressBarProfile);
                presenterProfile.changePassword(oldPassword, newPassword);
            }
        });
        txtCloseDialogChangePassword.setOnClickListener(v -> dialogChanePass.dismiss());

        dialogChanePass.show();

    }




}
