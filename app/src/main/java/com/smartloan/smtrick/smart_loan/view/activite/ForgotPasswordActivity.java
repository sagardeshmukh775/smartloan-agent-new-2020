package com.smartloan.smtrick.smart_loan.view.activite;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.callback.CallBack;
import com.smartloan.smtrick.smart_loan.constants.Constant;
import com.smartloan.smtrick.smart_loan.exception.ExceptionUtil;
import com.smartloan.smtrick.smart_loan.models.User;
import com.smartloan.smtrick.smart_loan.preferences.AppSharedPreference;
import com.smartloan.smtrick.smart_loan.repository.UserRepository;
import com.smartloan.smtrick.smart_loan.repository.impl.UserRepositoryImpl;
import com.smartloan.smtrick.smart_loan.utilities.Utility;
import com.smartloan.smtrick.smart_loan.view.dialog.ProgressDialogClass;

public class ForgotPasswordActivity extends AppCompatActivity {
    Button login;
    EditText etEmailId;
    private UserRepository userRepository;
    private ProgressDialogClass progressDialog;
    private LinearLayout ll_mobile_number;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forgot_password);
        userRepository = new UserRepositoryImpl(this);
        login = findViewById(R.id.buttonlogin);
        etEmailId = findViewById(R.id.edittext_email);
        ll_mobile_number = findViewById(R.id.ll_mobile_number);
        login.setOnClickListener((View v) -> {
            sendResetPasswordLink();
        });
        etEmailId.setOnTouchListener((View arg0, MotionEvent arg1) -> {
            Animation zoomOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
            ll_mobile_number.startAnimation(zoomOutAnimation);
            return false;
        });
    }


    public void sendResetPasswordLink() {
        if (!Utility.isNetworkConnected(this)) {
            Utility.showMessage(this, this.getString(R.string.network_not_available));
            return;
        }
        final String emailId = etEmailId.getText().toString();
        if (validate(emailId)) {
            if (!Utility.isNetworkConnected(this)) {
                Utility.showMessage(this, this.getString(R.string.network_not_available));
                return;
            }
            progressDialog = new ProgressDialogClass(this);
            progressDialog.showDialog(this.getString(R.string.SIGNING_IN), this.getString(R.string.PLEASE_WAIT));
            userRepository.resetPasswordWithEmailId(emailId, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    Utility.showLongMessage(ForgotPasswordActivity.this, getMessage(R.string.reset_password_success));
                    finish();
                }

                @Override
                public void onError(Object object) {
                    if (progressDialog != null)
                        progressDialog.dismissDialog();
                    Utility.showSnackBar(activity, etEmailId, getMessage(R.string.reset_password_error));
                }
            });
        }
    }

    private String getMessage(int id) {
        return getString(id);
    }

    private boolean validate(String emailId) {
        String validationMessage;
        boolean isValid = true;
        try {
            if (emailId == null || !Utility.isValidEmail(emailId)) {
                validationMessage = getString(R.string.invalid_email);
                etEmailId.setError(validationMessage);
                isValid = false;
            }
        } catch (Exception e) {
            isValid = false;
            ExceptionUtil.logException(e);
        }
        return isValid;
    }
}