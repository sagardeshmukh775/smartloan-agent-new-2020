package com.smartloan.smtrick.smart_loan.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.smartloan.smtrick.smart_loan.models.User;

import static com.smartloan.smtrick.smart_loan.constants.Constant.MALE;


public class AppSharedPreference {
    private SharedPreferences sharedPref;
    private Context context;
    private SharedPreferences.Editor editor;
    private String USERNAME = "USERNAME";
    private String PASSWORD = "PASSWORD";
    private String EMAIL_ID = "EMAIL_ID";
    private String MOBILE_NO = "MOBILE_NO";
    private String WHATSAPP_NO = "WHATSAPP_NO";
    private String AADHAAR_NO = "AADHAAR_NO";
    private String PROFILE_SMALL_IMAGE = "PROFILE_SMALL_IMAGE";
    private String PROFILE_LARGE_IMAGE = "PROFILE_LARGE_IMAGE";
    private String COVER_LARGE_IMAGE = "COVER_LARGE_IMAGE";
    private String REG_ID = "REG_ID";
    private String IS_USER_LOGGED_IN = "IS_USER_LOGGED_IN";
    private String USER_ID = "USER_ID";
    private String CREATED_DATE_TIME = "CREATED_DATE_TIME";
    private String UPDATED_DATE_TIME = "UPDATED_DATE_TIME";
    private String REGISTRATION_TOKEN = "REGISTRATION_TOKEN";
    private String ADDRESS = "ADDRESS";
    private String ROLE = "ROLE";
    private String GENDER = "GENDER";
    private String AGENT_ID = "AGENT_ID";
    private String ACCOUNT_HOLDER_NAME = "ACCOUNT_HOLDER_NAME";
    private String ACCOUNT_NUMBER = "ACCOUNT_NUMBER";
    private String BRANCH_NAME = "BRANCH_NAME";
    private String IFSC = "IFSC";
    private String BANK_NAME = "BANK_NAME";
    private String STATE = "STATE";
    private String CITY = "CITY";


    public AppSharedPreference(Context context) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
    }

    public void addUserDetails(User user) {
        editor = sharedPref.edit();
        if (user != null) {
            if (user.getUserName() != null)
                editor.putString(USERNAME, (user.getUserName()));
            if (user.getEmail() != null)
                editor.putString(EMAIL_ID, (user.getEmail()));
            if (user.getRegId() != null)
                editor.putString(REG_ID, (user.getRegId()));
            if (user.getMobileNumber() != null)
                editor.putString(MOBILE_NO, (user.getMobileNumber()));
            if (user.getWhatsappNumber() != null)
                editor.putString(WHATSAPP_NO, (user.getWhatsappNumber()));
            if (user.getAadhaarNumber() != null)
                editor.putString(AADHAAR_NO, (user.getAadhaarNumber()));
            if (user.getUserProfileImageSmall() != null)
                editor.putString(PROFILE_SMALL_IMAGE, (user.getUserProfileImageSmall()));
            if (user.getUserProfileImageLarge() != null)
                editor.putString(PROFILE_LARGE_IMAGE, (user.getUserProfileImageLarge()));
            if (user.getUserCoverImage() != null)
                editor.putString(COVER_LARGE_IMAGE, (user.getUserCoverImage()));
            if (user.getUserId() != null)
                editor.putString(USER_ID, (user.getUserId()));
            if (user.getRegistrationToken() != null)
                editor.putString(REGISTRATION_TOKEN, (user.getRegistrationToken()));
            if (user.getAddress() != null)
                editor.putString(ADDRESS, (user.getAddress()));
            if (user.getRole() != null)
                editor.putString(ROLE, (user.getRole()));
            if (user.getGender() != null)
                editor.putString(GENDER, (user.getGender()));
            if (user.getAgentId() != null)
                editor.putString(AGENT_ID, (user.getAgentId()));
            if (user.getState() != null)
                editor.putString(STATE, (user.getState()));
            if (user.getCity() != null)
                editor.putString(CITY, (user.getCity()));
            if (user.getKycDetails() != null) {
                if (user.getKycDetails().getAccountHolderName() != null)
                    editor.putString(ACCOUNT_HOLDER_NAME, user.getKycDetails().getAccountHolderName());
                if (user.getKycDetails().getAccountNumber() != null)
                    editor.putString(ACCOUNT_NUMBER, user.getKycDetails().getAccountNumber());
                if (user.getKycDetails().getBankName() != null)
                    editor.putString(BANK_NAME, user.getKycDetails().getBankName());
                if (user.getKycDetails().getBranchName() != null)
                    editor.putString(BRANCH_NAME, user.getKycDetails().getBranchName());
                if (user.getKycDetails().getIfsc() != null)
                    editor.putString(IFSC, user.getKycDetails().getIfsc());
            }
        }
        editor.apply();
    }

    public void createUserLoginSession() {
        editor = sharedPref.edit();
        editor.putBoolean(IS_USER_LOGGED_IN, true);
        editor.apply();
    }

    //User
    public String getUserName() {
        return (sharedPref.getString(USERNAME, ""));
    }

    public String getPassword() {
        return (sharedPref.getString(PASSWORD, ""));
    }

    public boolean getUserLoginStatus() {
        return sharedPref.getBoolean(IS_USER_LOGGED_IN, (false));
    }

    public String getRole() {
        return (sharedPref.getString(ROLE, ""));
    }

    public String getGender() {
        return (sharedPref.getString(GENDER, MALE));
    }

    public String getRegId() {
        return (sharedPref.getString(REG_ID, ""));
    }

    public String getEmaiId() {
        return (sharedPref.getString(EMAIL_ID, ""));
    }

    public String getAgeniId() {
        return (sharedPref.getString(AGENT_ID, ""));
    }

    public String getMobileNo() {
        return (sharedPref.getString(MOBILE_NO, ""));
    }

    public String getWhatsappNo() {
        return (sharedPref.getString(WHATSAPP_NO, ""));
    }

    public String getAadhaarNo() {
        return (sharedPref.getString(AADHAAR_NO, ""));
    }

    public String getProfileSmallImage() {
        return (sharedPref.getString(PROFILE_SMALL_IMAGE, ""));
    }

    public String getProfileLargeImage() {
        return (sharedPref.getString(PROFILE_LARGE_IMAGE, ""));
    }

    public String getCoverLargeImage() {
        return (sharedPref.getString(COVER_LARGE_IMAGE, ""));
    }


    public String getAccountHolderName() {
        return (sharedPref.getString(ACCOUNT_HOLDER_NAME, ""));
    }

    public String getAccountNumber() {
        return (sharedPref.getString(ACCOUNT_NUMBER, ""));
    }

    public String getBranchName() {
        return (sharedPref.getString(BRANCH_NAME, ""));
    }

    public String getIfsc() {
        return (sharedPref.getString(IFSC, ""));
    }

    public String getBankName() {
        return (sharedPref.getString(BANK_NAME, ""));
    }

    public String getCity() {
        return (sharedPref.getString(CITY, ""));
    }

    public String getState() {
        return (sharedPref.getString(STATE, ""));
    }


    public void setUserProfileImages(String imagePath) {
        editor = sharedPref.edit();
        if (imagePath != null) {
            editor.putString(PROFILE_SMALL_IMAGE, imagePath);
            editor.putString(PROFILE_LARGE_IMAGE, imagePath);
        }
        editor.apply();
    }

    public String getUserId() {
        return (sharedPref.getString(USER_ID, ""));
    }

    public String getCreatedDateTime() {
        return (sharedPref.getString(CREATED_DATE_TIME, ""));
    }

    public String getUpdatedDateTime() {
        return (sharedPref.getString(UPDATED_DATE_TIME, ""));
    }

    public String getRegistrationToken() {
        return (sharedPref.getString(REGISTRATION_TOKEN, ""));
    }

    public String getAddress() {
        return (sharedPref.getString(ADDRESS, ""));
    }

    public void clear() {
        editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }
}
