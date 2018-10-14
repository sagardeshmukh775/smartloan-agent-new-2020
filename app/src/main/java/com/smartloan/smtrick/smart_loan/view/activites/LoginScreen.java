package com.smartloan.smtrick.smart_loan.view.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginScreen extends AppCompatActivity {
    TextView txtregister;
    Button login, Register;
    EditText etMobileNumber, etpassword;
    private AppSharedPreference appSharedPreference;
    private UserRepository userRepository;
    private ProgressDialogClass progressDialog;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loginscreen);
        userRepository = new UserRepositoryImpl(this);
        appSharedPreference = new AppSharedPreference(this);
        checkLoginState();
        Register = (Button) findViewById(R.id.buttonRegister);
        login = (Button) findViewById(R.id.buttonlogin);
        etMobileNumber = (EditText) findViewById(R.id.edittext_mobile_number);
        etpassword = (EditText) findViewById(R.id.edittextpassword);
        Register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(LoginScreen.this, Registeractivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                //overridePendingTransition(R.anim.backslide_in, R.anim.backslide_out);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login();
            }
        });
        etMobileNumber.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                Animation zoomOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
                etMobileNumber.startAnimation(zoomOutAnimation);
                return false;
            }
        });
        etpassword.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                Animation zoomOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
                etpassword.startAnimation(zoomOutAnimation);
                return false;
            }
        });
    }

    public void checkLoginState() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (appSharedPreference != null && appSharedPreference.getUserLoginStatus()) {
                        if (appSharedPreference.getRegId() != null && appSharedPreference.getUserId() != null) {
                            loginToApp();
                        }
                    }
                } catch (Exception e) {
                    ExceptionUtil.logException("Method: checkLoginState", "Class: LoginScreen", e);
                }
            }
        });
    }

    private void loginToApp() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void login() {
        if (!Utility.isNetworkConnected(this)) {
            Utility.showMessage(this, this.getString(R.string.network_not_available));
            return;
        }
        final String mobileNumber = etMobileNumber.getText().toString();
        final String password = etpassword.getText().toString();
        if (validate(mobileNumber, password)) {
            if (!Utility.isNetworkConnected(this)) {
                Utility.showMessage(this, this.getString(R.string.network_not_available));
                return;
            }
            progressDialog = new ProgressDialogClass(this);
            progressDialog.showDialog(this.getString(R.string.SIGNING_IN), this.getString(R.string.PLEASE_WAIT));
            userRepository.signIn(mobileNumber, password, new CallBack() {
                @Override
                public void onSuccess(Object object) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser userFirebase = Constant.AUTH.getCurrentUser();
                    if (userFirebase != null) {
                        signInUserData(userFirebase.getUid());
                    }
                }

                @Override
                public void onError(Object object) {
                    if (progressDialog != null)
                        progressDialog.dismissDialog();
                    Utility.showSnackBar(activity,etpassword, getMessage(R.string.login_fail_try_again));
                }
            });
        }
    }

    private void signInUserData(final String userId) {
        userRepository.readUserByUserId(userId, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    User user = (User) object;
                    appSharedPreference.createUserLoginSession();
                    appSharedPreference.addUserDetails(user);
                    loginToApp();
                } else {
                    Utility.showTimedSnackBar(activity,etpassword, getMessage(R.string.login_fail_try_again));
                }
                if (progressDialog != null)
                    progressDialog.dismissDialog();
            }

            @Override
            public void onError(Object object) {
                if (progressDialog != null)
                    progressDialog.dismissDialog();
                Utility.showTimedSnackBar(activity,etpassword, getMessage(R.string.login_fail_try_again));
            }
        });
    }

    private String getMessage(int id) {
        return getString(id);
    }

    private boolean validate(String mobileNumber, String password) {
        String validationMessage;
        boolean isValid = true;
        try {
            if (Utility.isEmptyOrNull(mobileNumber)) {
                validationMessage = getString(R.string.MOBILE_NUMBER_SHOULD_NOT_BE_EMPTY);
                etMobileNumber.setError(validationMessage);
                isValid = false;
            } else if (!Utility.isValidMobileNumber(mobileNumber)) {
                validationMessage = getMessage(R.string.INVALID_MOBILE_NUMBER);
                etMobileNumber.setError(validationMessage);
                isValid = false;
            }
            if (Utility.isEmptyOrNull(password)) {
                validationMessage = getString(R.string.PASSWORD_SHOULD_NOT_BE_EMPTY);
                etpassword.setError(validationMessage);
                isValid = false;
            }
        } catch (Exception e) {
            isValid = false;
            ExceptionUtil.logException("Method: validate", "Class: LoginScreen", e);
        }
        return isValid;
    }
}
