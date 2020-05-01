package com.smartloan.smtrick.smart_loan.view.activite;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.smartloan.smtrick.smart_loan.R;
import com.smartloan.smtrick.smart_loan.callback.CallBack;
import com.smartloan.smtrick.smart_loan.exception.ExceptionUtil;
import com.smartloan.smtrick.smart_loan.models.User;
import com.smartloan.smtrick.smart_loan.preferences.AppSharedPreference;
import com.smartloan.smtrick.smart_loan.repository.UserRepository;
import com.smartloan.smtrick.smart_loan.repository.impl.UserRepositoryImpl;
import com.smartloan.smtrick.smart_loan.utilities.Utility;
import com.smartloan.smtrick.smart_loan.view.dialog.ProgressDialogClass;

import static com.smartloan.smtrick.smart_loan.constants.Constant.AGENT;
import static com.smartloan.smtrick.smart_loan.constants.Constant.AGENT_PREFIX;
import static com.smartloan.smtrick.smart_loan.constants.Constant.FEMALE;
import static com.smartloan.smtrick.smart_loan.constants.Constant.MALE;

import com.smartloan.smtrick.smart_loan.databinding.RegisteractivityBinding;

import java.util.concurrent.TimeUnit;

public class Registeractivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener, View.OnTouchListener, View.OnClickListener {
    TextView txtlogin, txttc,tvResendOtp;
    Button btlogin, btnotp, buttonVerifyOtp;
    EditText etname, etaddress, etmobile, editTextPassword, etusername, etpassword, emailid, etreenterpassword, etotp;
    Spinner spin;
    RadioButton Rdmale, RdFemale;
    private CheckBox chk_terms_condition;
    private UserRepository userRepository;
    private ProgressDialogClass progressDialogClass;
    private AppSharedPreference appSharedPreference;
    private RegisteractivityBinding registeractivityBinding;
    private String firebaseVerificationId;
    private PhoneAuthProvider.ForceResendingToken firebaseForceResendingToken;
    private LinearLayout ll_otp_verified_layout,ll_otp_verification_layout,ll_otp_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.registeractivity);
        registeractivityBinding = DataBindingUtil.setContentView(this, R.layout.registeractivity);
        userRepository = new UserRepositoryImpl(this);
        progressDialogClass = new ProgressDialogClass(this);
        appSharedPreference = new AppSharedPreference(this);
        String[] Userstype = new String[]{"Agent"};
        //txtlogin = (TextView) findViewById(R.id.txtRegister);
        btlogin = (Button) findViewById(R.id.buttonsubmit);
        btnotp = (Button) findViewById(R.id.buttongenerateotp);
        buttonVerifyOtp = (Button) findViewById(R.id.buttonVerifyOtp);
        etname = (EditText) findViewById(R.id.edittextname);
        etaddress = (EditText) findViewById(R.id.edittextaddress);
        etmobile = (EditText) findViewById(R.id.edittextmobile);
        etusername = (EditText) findViewById(R.id.edittextusername);
        etpassword = (EditText) findViewById(R.id.edittextpassword);
        emailid = (EditText) findViewById(R.id.edittextemailid);
        etreenterpassword = (EditText) findViewById(R.id.edittextreenterpassword);
        editTextPassword = (EditText) findViewById(R.id.edittextreenterpassword);
        etotp = (EditText) findViewById(R.id.edittextenterotp);
        Rdmale = (RadioButton) findViewById(R.id.radiomale);
        RdFemale = (RadioButton) findViewById(R.id.radiofemale);
        chk_terms_condition = findViewById(R.id.chk_terms_condition);
        // spin = (Spinner) findViewById(R.id.spinnerselectusertype);
        // spin.setOnItemSelectedListener(this);
        txttc = (TextView) findViewById(R.id.txttermsandconditions);
        tvResendOtp = (TextView) findViewById(R.id.tvResendOtp);
        ll_otp_verified_layout =  findViewById(R.id.ll_otp_verified_layout);
        ll_otp_verification_layout =  findViewById(R.id.ll_otp_verification_layout);
        ll_otp_layout =  findViewById(R.id.ll_otp_layout);
        setClickListners();
        setTouchListner();
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private void setClickListners() {
        btlogin.setOnClickListener(this);
        txttc.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonsubmit:
                validateAndCreateUser();
                break;
            case R.id.txttermsandconditions:
                Intent intent = new Intent(Registeractivity.this, TermsConditionActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.backslide_in, R.anim.backslide_out);
                break;
        }
    }

    private void setTouchListner() {
        registeractivityBinding.edittextname.setOnTouchListener(this);
        registeractivityBinding.edittextmobile.setOnTouchListener(this);
        registeractivityBinding.edittextaddress.setOnTouchListener(this);
        registeractivityBinding.edittextemailid.setOnTouchListener(this);
        registeractivityBinding.edittextusername.setOnTouchListener(this);
        registeractivityBinding.edittextpassword.setOnTouchListener(this);
        registeractivityBinding.edittextenterotp.setOnTouchListener(this);
        registeractivityBinding.radiomale.setOnTouchListener(this);
        registeractivityBinding.radiofemale.setOnTouchListener(this);
        //registeractivityBinding.llMobileNumber.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Animation zoomOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
        View animationView = registeractivityBinding.llNameLayout;
        if (view.getId() == registeractivityBinding.edittextname.getId())
            animationView = registeractivityBinding.llNameLayout;
        else if (view.getId() == registeractivityBinding.edittextmobile.getId())
            animationView = registeractivityBinding.llMobileNumber;
        else if (view.getId() == registeractivityBinding.edittextaddress.getId())
            animationView = registeractivityBinding.llAddressLayout;
        else if (view.getId() == registeractivityBinding.edittextemailid.getId())
            animationView = registeractivityBinding.llEmailLayout;
        else if (view.getId() == registeractivityBinding.radiomale.getId())
            animationView = registeractivityBinding.radiomale;

        else if (view.getId() == registeractivityBinding.radiofemale.getId())
            animationView = registeractivityBinding.radiofemale;

        else if (view.getId() == registeractivityBinding.edittextusername.getId())
            animationView = registeractivityBinding.llUserNameLayout;

        else if (view.getId() == registeractivityBinding.edittextpassword.getId())
            animationView = registeractivityBinding.llPasswordLayout;

        else if (view.getId() == registeractivityBinding.edittextenterotp.getId())
            animationView = registeractivityBinding.llOtpLayout;
        animationView.startAnimation(zoomOutAnimation);
        return false;
    }

    private void validateAndCreateUser() {
        User user = fillUserModel();
        if (validate(user))
            createUser(user);
    }

    private User fillUserModel() {
        User user = new User();
        user.setUserName(etname.getText().toString());
        user.setMobileNumber(etmobile.getText().toString());
        user.setAddress(etaddress.getText().toString());
        user.setEmail(emailid.getText().toString());
        user.setPassword(etpassword.getText().toString());
        user.setAgentId(Utility.generateAgentId(AGENT_PREFIX));
        user.setRole(AGENT);
        if (Rdmale.isChecked())
            user.setGender(MALE);
        else
            user.setGender(FEMALE);
        return user;
    }

    private boolean validate(User user) {
        String validationMessage;
        boolean isValid = true;
        try {
            if (Utility.isEmptyOrNull(user.getUserName())) {
                validationMessage = getString(R.string.PLEASE_ENTER_NAME);
                etname.setError(validationMessage);
                isValid = false;
            }
            if (Utility.isEmptyOrNull(user.getMobileNumber())) {
                validationMessage = getString(R.string.MOBILE_NUMBER_SHOULD_NOT_BE_EMPTY);
                etmobile.setError(validationMessage);
                isValid = false;
            } else if (!Utility.isValidMobileNumber(user.getMobileNumber())) {
                validationMessage = getMessage(R.string.INVALID_MOBILE_NUMBER);
                etmobile.setError(validationMessage);
                isValid = false;
            }
            if (Utility.isEmptyOrNull(user.getPassword())) {
                validationMessage = getString(R.string.PASSWORD_SHOULD_NOT_BE_EMPTY);
                etpassword.setError(validationMessage);
                isValid = false;
            }
            if (user.getEmail() == null || !Utility.isValidEmail(user.getEmail())) {
                validationMessage = getString(R.string.invalid_email);
                emailid.setError(validationMessage);
                isValid = false;
            }
            if (!chk_terms_condition.isChecked()) {
                Utility.showLongMessage(this, getMessage(R.string.terms_condition_text));
                isValid = false;
            }
        } catch (Exception e) {
            isValid = false;
            ExceptionUtil.logException(e);
        }
        return isValid;
    }

    private String getMessage(int id) {
        return getString(id);
    }

    private void createUser(final User user) {
        progressDialogClass.showDialog(getMessage(R.string.loading), getMessage(R.string.please_wait));
        userRepository.createUser(user, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                addUserDataToPreferences(user);
                loginToApp();
            }

            @Override
            public void onError(Object object) {
                progressDialogClass.dismissDialog();
                Utility.showMessage(Registeractivity.this, getMessage(R.string.registration_fail));
            }
        });
    }

    private void addUserDataToPreferences(User user) {
        appSharedPreference.addUserDetails(user);
        appSharedPreference.createUserLoginSession();
    }

    private void loginToApp() {
        Toast.makeText(Registeractivity.this, "User Register Successfully", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(Registeractivity.this, MainActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void sendOtp(View view) {
        if (Utility.isEmptyOrNull(etmobile.getText().toString())) {
            etmobile.setError(getString(R.string.MOBILE_NUMBER_SHOULD_NOT_BE_EMPTY));
        } else if (!Utility.isValidMobileNumber(etmobile.getText().toString())) {
            etmobile.setError(getMessage(R.string.INVALID_MOBILE_NUMBER));
        }else{
            sendOTP(etmobile.getText().toString());
        }
    }
    public void resendOtp(View view) {
        if (Utility.isEmptyOrNull(etmobile.getText().toString())) {
            etmobile.setError(getString(R.string.MOBILE_NUMBER_SHOULD_NOT_BE_EMPTY));
        } else if (!Utility.isValidMobileNumber(etmobile.getText().toString())) {
            etmobile.setError(getMessage(R.string.INVALID_MOBILE_NUMBER));
        }else{
            resendOTP(etmobile.getText().toString());
        }
    }

    public void verifyOtp(View view) {
        if (Utility.isEmptyOrNull(etotp.getText().toString())||etotp.getText().toString().length()<6) {
            etotp.setError(getString(R.string.invalid_otp));
        } else{
            verifyMobileNumber(etotp.getText().toString());
        }
    }

    private void sendOTP(String mobileNumber) {
        progressDialogClass.showDialog(getMessage(R.string.loading), getMessage(R.string.please_wait));
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobileNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        progressDialogClass.dismissDialog();
                        tvResendOtp.setVisibility(View.GONE);
                        ll_otp_layout.setVisibility(View.GONE);
                        buttonVerifyOtp.setVisibility(View.GONE);
                        Utility.showLongMessage(Registeractivity.this, "Failed to send OTP.");
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        progressDialogClass.dismissDialog();
                        Utility.showLongMessage(Registeractivity.this, "Failed to send OTP.");
                        tvResendOtp.setVisibility(View.GONE);
                        ll_otp_layout.setVisibility(View.GONE);
                        buttonVerifyOtp.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verificationId, forceResendingToken);
                        firebaseVerificationId = verificationId;
                        tvResendOtp.setVisibility(View.VISIBLE);
                        ll_otp_layout.setVisibility(View.VISIBLE);
                        buttonVerifyOtp.setVisibility(View.VISIBLE);
                        firebaseForceResendingToken = forceResendingToken;
                        progressDialogClass.dismissDialog();
                        Utility.showLongMessage(Registeractivity.this, "OTP Sent successfully.");
                    }
                });
    }

    private void resendOTP(String mobileNumber) {
        progressDialogClass.showDialog(getMessage(R.string.loading), getMessage(R.string.please_wait));
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobileNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        progressDialogClass.dismissDialog();
                        Utility.showLongMessage(Registeractivity.this, "Failed to send OTP.");
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        progressDialogClass.dismissDialog();
                        Utility.showLongMessage(Registeractivity.this, "Failed to send OTP.");
                    }

                    @Override
                    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verificationId, forceResendingToken);
                        firebaseVerificationId = verificationId;
                        firebaseForceResendingToken = forceResendingToken;
                        progressDialogClass.dismissDialog();
                        Utility.showLongMessage(Registeractivity.this, "OTP Sent successfully.");
                    }
                },firebaseForceResendingToken);
    }

    private void verifyMobileNumber(String code) {
        progressDialogClass.showDialog(getMessage(R.string.loading), getMessage(R.string.please_wait));
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(firebaseVerificationId, code);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            deleteAccount();
                        } else {
                            progressDialogClass.dismissDialog();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Utility.showLongMessage(Registeractivity.this, "OTP verification Fail");
                            }
                        }
                    }
                });
    }

    private void deleteAccount() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    ll_otp_verification_layout.setVisibility(View.GONE);
                    ll_otp_verified_layout.setVisibility(View.VISIBLE);
                    btlogin.setVisibility(View.VISIBLE);
                    etmobile.setEnabled(false);
                    etmobile.setFocusable(false);
                    etmobile.setClickable(false);
                    Utility.showLongMessage(Registeractivity.this, "OTP verification Done");
                } else {
                    Utility.showLongMessage(Registeractivity.this, "OTP verification Fail");
                }
                progressDialogClass.dismissDialog();
            }
        });
    }
}//end of activity
