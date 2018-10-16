package com.smartloan.smtrick.smart_loan.repository.impl;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smartloan.smtrick.smart_loan.callback.CallBack;
import com.smartloan.smtrick.smart_loan.constants.Constant;
import com.smartloan.smtrick.smart_loan.exception.ExceptionUtil;
import com.smartloan.smtrick.smart_loan.models.User;
import com.smartloan.smtrick.smart_loan.repository.FirebaseTemplateRepository;
import com.smartloan.smtrick.smart_loan.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

import static com.smartloan.smtrick.smart_loan.constants.Constant.EMAIL_POSTFIX;


public class UserRepositoryImpl extends FirebaseTemplateRepository implements UserRepository {
    private Activity _activity;

    public UserRepositoryImpl(final Activity activity) {
        _activity = activity;
    }
    /********************************************** Firebase Call ***************************************************/
    /**
     * @param mobileNumber
     * @param callBack
     */
    @Override
    public void readUserByMobileNumber(final String mobileNumber, final CallBack callBack) {
        final Query query = Constant.USER_TABLE_REF.orderByChild("mobileNumber").equalTo(mobileNumber);
        fireBaseReadData(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                        DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
                        User user = firstChild.getValue(User.class);
                        callBack.onSuccess(user);
                    } else {
                        callBack.onError(null);
                    }
                }
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }

    /**
     * @param mobileNumber
     * @param callBack
     */
    @Override
    public void notifyReadUserByMobileNumber(final String mobileNumber, final CallBack callBack) {
        final Query query = Constant.USER_TABLE_REF.orderByChild("mobileNumber").equalTo(mobileNumber);
        fireBaseNotifyChange(query, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    DataSnapshot dataSnapshot = (DataSnapshot) object;
                    if (dataSnapshot.getValue() != null & dataSnapshot.hasChildren()) {
                        DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
                        User user = firstChild.getValue(User.class);
                        callBack.onSuccess(user);
                    } else {
                        callBack.onError(null);
                    }
                } else callBack.onError(null);
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }

    /**
     * @param userId
     * @param callback
     */
    @Override
    public void readUser(final String userId, final CallBack callback) {
        final Query query = Constant.USER_TABLE_REF.orderByChild("userId").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null && dataSnapshot.hasChildren()) {
                    try {
                        final DataSnapshot firstChild = dataSnapshot.getChildren().iterator().next();
                        callback.onSuccess(firstChild.getValue(User.class));
                    } catch (Exception e) {
                        callback.onError(e);
                        ExceptionUtil.logException(e);
                    }
                } else
                    callback.onError(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }

    /**
     * @param callback
     */
    @Override
    public void readLoggedInUser(final CallBack callback) {
        final FirebaseUser firebaseUser = Constant.AUTH.getCurrentUser();
        if (firebaseUser == null) {
            return;
        }
        final String userId = firebaseUser.getUid();
        readUser(userId, callback);
    }

    /**
     * @param callback
     */
    @Override
    public void createUser(final User userModel, final CallBack callback) {
        Constant.AUTH.createUserWithEmailAndPassword(userModel.getMobileNumber() + EMAIL_POSTFIX, userModel.getPassword())
                .addOnCompleteListener(_activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final FirebaseUser firebaseUser = Constant.AUTH.getCurrentUser();
                            if (firebaseUser == null) {
                                callback.onError(task);
                                return;
                            }
                            userModel.setUserId(firebaseUser.getUid());
                            //userModel.setPassword(null);
                            // FireBase Create User
                            Map userMap = userModel.toMap();
                            // userMap.put("imeiNumber", REGISTRATION_CONSTANT);
                            createUserData(userModel, callback);
                        } else {
                            callback.onError(task);
                        }
                    }
                });
    }

    @Override
    public void createUserData(User user, final CallBack callBack) {
        DatabaseReference databaseReference = Constant.USER_TABLE_REF.child(user.getUserId());
        fireBaseCreate(databaseReference, user, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                callBack.onSuccess(object);
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }

    @Override
    public void signIn(final String mobileNumber, final String password, final CallBack callback) {
        Constant.AUTH.signInWithEmailAndPassword(mobileNumber + EMAIL_POSTFIX, password)
                .addOnCompleteListener(_activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess(task);
                        } else {
                            callback.onError(task);
                        }
                    }
                });
    }

    /**
     * @param newPassword
     * @param callback
     */
    @Override
    public void changePassword(String newPassword, final CallBack callback) {
        final FirebaseUser firebaseUser = Constant.AUTH.getCurrentUser();
        if (firebaseUser == null) {
            callback.onError(null);
            return;
        }
        firebaseUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onSuccess(task);
                } else {
                    callback.onError(task);
                }
            }
        });
    }

    /**
     * @param callback
     */
    @Override
    public void updateUser(final User userModel, final CallBack callback) {
        final Map<String, Object> userProfileMap = new HashMap<>();
        userProfileMap.put(userModel.getRegId(), userModel);
        final DatabaseReference databaseReference = Constant.USER_TABLE_REF;
        fireBaseUpdateChildren(databaseReference, userProfileMap, callback);
    }

    /**
     * @param userId
     * @param callback
     * @return
     */
    @Override
    public void deleteUser(final String userId, final CallBack callback) {
        DatabaseReference databaseReference = Constant.USER_TABLE_REF.child(userId);
        fireBaseDelete(databaseReference, callback);
    }

    @Override
    public void updateUser(String userId, Map userMap, final CallBack callBack) {
        final DatabaseReference databaseReference = Constant.USER_TABLE_REF.child(userId);
        fireBaseUpdateChildren(databaseReference, userMap, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                callBack.onSuccess(object);
            }

            @Override
            public void onError(Object object) {
                callBack.onError(object);
            }
        });
    }


    @Override
    public void readUserByUserId(String userId, final CallBack callBack) {
        final Query query = Constant.USER_TABLE_REF.child(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    if (dataSnapshot.getValue() != null) {
                        try {
                            if (dataSnapshot.hasChildren()) {
                                callBack.onSuccess(dataSnapshot.getValue(User.class));
                            } else {
                                callBack.onError(null);
                            }
                        } catch (Exception e) {
                            ExceptionUtil.logException(e);
                        }
                    } else
                        callBack.onError(null);
                } else
                    callBack.onError(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callBack.onError(databaseError);
            }
        });
    }

    @Override
    public void setDataChangeListnerOnUserLogin(String regId, final CallBack callback) {
        Query query = Constant.USER_TABLE_REF.child(regId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    if (dataSnapshot.getValue() != null) {
                        User user = dataSnapshot.getValue(User.class);
                        callback.onSuccess(user);
                    } else {
                        callback.onSuccess(null);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError(databaseError);
            }
        });
    }
}