package com.smartloan.smtrick.smart_loan.view.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class Registeractivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener, View.OnTouchListener, View.OnClickListener {
    TextView txtlogin, txttc;
    Button btlogin, btnotp;
    EditText etname, etaddress, etmobile, editTextPassword, etusername, etpassword, emailid, etreenterpassword, etotp;
    Spinner spin;
    RadioButton Rdmale, RdFemale;
    private UserRepository userRepository;
    private ProgressDialogClass progressDialogClass;
    private AppSharedPreference appSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.registeractivity);
        userRepository = new UserRepositoryImpl(this);
        progressDialogClass = new ProgressDialogClass(this);
        appSharedPreference = new AppSharedPreference(this);
        String[] Userstype = new String[]{"Agent"};
        //txtlogin = (TextView) findViewById(R.id.txtRegister);
        btlogin = (Button) findViewById(R.id.buttonsubmit);
        btnotp = (Button) findViewById(R.id.buttongenerateotp);
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
        // spin = (Spinner) findViewById(R.id.spinnerselectusertype);
        // spin.setOnItemSelectedListener(this);
        txttc = (TextView) findViewById(R.id.txttermsandconditions);
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
                Intent intent = new Intent(Registeractivity.this, TermsCondition_Activity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.backslide_in, R.anim.backslide_out);
                break;
        }
    }

    private void setTouchListner() {
        Rdmale.setOnTouchListener(this);
        RdFemale.setOnTouchListener(this);
        etname.setOnTouchListener(this);
        etaddress.setOnTouchListener(this);
        etmobile.setOnTouchListener(this);
        etusername.setOnTouchListener(this);
        etpassword.setOnTouchListener(this);
        emailid.setOnTouchListener(this);
        etreenterpassword.setOnTouchListener(this);
        etotp.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Animation zoomOutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoomin);
        view.startAnimation(zoomOutAnimation);
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
            }else if (!Utility.isValidMobileNumber(user.getMobileNumber())) {
                validationMessage = getMessage(R.string.INVALID_MOBILE_NUMBER);
                etmobile.setError(validationMessage);
                isValid = false;
            }
            if (Utility.isEmptyOrNull(user.getPassword())) {
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
}//end of activity
