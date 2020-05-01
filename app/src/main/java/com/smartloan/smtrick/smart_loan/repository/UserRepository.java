package com.smartloan.smtrick.smart_loan.repository;

import com.smartloan.smtrick.smart_loan.callback.CallBack;
import com.smartloan.smtrick.smart_loan.models.User;

import java.util.Map;

public interface UserRepository {

    void signIn(final String emailId, final String password, final CallBack callback);

    void readLoggedInUser(final CallBack callback);

    void readUser(final String userId, final CallBack callback);

    void readUserByMobileNumber(final String mobileNumber, final CallBack callBack);

    void notifyReadUserByMobileNumber(final String mobileNumber, final CallBack callBack);

    void changePassword(final String newPassword, final CallBack callback);

    void createUser(final User userModel, final CallBack callback);

    void createUserData(final User userModel, final CallBack callback);

    void updateUser(final User userModel, final CallBack callback);

    void deleteUser(final String userId, final CallBack callback);

    void updateUser(final String regId, final Map userMap, final CallBack callback);

    void readUserByUserId(final String regId, final CallBack callBack);

    void setDataChangeListnerOnUserLogin(final String regId, final CallBack callback);

    void resetPasswordWithEmailId(final String emailId, final CallBack callBack);
}