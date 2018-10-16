package com.smartloan.smtrick.smart_loan.constants;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Constant {
    /************************************** Firebase Storage reference constants ***************************************************************************/
    private static final FirebaseDatabase DATABASE = FirebaseDatabase.getInstance();
    public static final DatabaseReference USER_TABLE_REF = DATABASE.getReference("user");
    public static final DatabaseReference LEEDS_TABLE_REF = DATABASE.getReference("leeds");
    /************************************** Firebase Authentication reference constants ***************************************************************************/
    public static final FirebaseAuth AUTH = FirebaseAuth.getInstance();
    /************************************** Calender Constatns ***************************************************************************/
    public static final Calendar cal = Calendar.getInstance();
    public static final int DAY = cal.get(Calendar.DAY_OF_MONTH);
    public static final int MONTH = cal.get(Calendar.MONTH);
    public static final int YEAR = cal.get(Calendar.YEAR);
    public static String TWO_DIGIT_LIMIT = "%02d";
    public static String FOUR_DIGIT_LIMIT = "%04d";
    public static final String SUCCESS = "Success";
    public static final String MALE = "Male";
    public static final String FEMALE = "Female";
    public static final String AGENT = "AGENT";
    public static final String AGENT_PREFIX = "AG-";
    public static final String LEED_PREFIX = "L-";
    public static final String EMAIL_POSTFIX = "@smartloan.com";
    //********************************************STATUS FLEADS*****************************
    public static final String STATUS_GENERATED = "GENERATED";
    public static final String STATUS_APPROVED = "APPROVED";
    public static final String STATUS_REJECTED = "REJECTED";
    public static final String STATUS_IN_PROGRESS = "IN-PROGRESS";
    public static final String GLOBAL_DATE_FORMATE = "dd MMM yyyy hh:mm a";
    public static final String CALANDER_DATE_FORMATE = "dd/MM/yy";
    public static final String LEED_DATE_FORMATE = "dd MMM, yyyy";
    public static final String DAY_DATE_FORMATE = "EEEE";
    public static final String TIME_DATE_FORMATE = "hh:mm a";
    //****************************************************************
}
