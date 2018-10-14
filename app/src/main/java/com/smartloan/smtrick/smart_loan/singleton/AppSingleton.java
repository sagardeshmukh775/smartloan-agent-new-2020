package com.smartloan.smtrick.smart_loan.singleton;

import android.content.Context;
import android.os.AsyncTask;

import com.google.firebase.database.FirebaseDatabase;

public class AppSingleton {
    private static AppSingleton appSingleton;

    //private constructor.
    private AppSingleton(Context context) {
        initFirebasePersistence();
    }

    private void initFirebasePersistence() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                    FirebaseDatabase.getInstance().goOnline();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static AppSingleton getInstance(Context context) {
        //if there is no instance available... create new one
        if (appSingleton == null) {
            appSingleton = new AppSingleton(context);
        }
        return appSingleton;
    }

    public String[] getLoanType() {
        return new String[]{"Select Loan Type", "HL", "LAP"};
    }
    public String[] getEmployeeType() {
        return new String[]{"Select Occupation", "Salaried", "Businessman"};
    }
}