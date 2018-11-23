package com.smartloan.smtrick.smart_loan.service;


import com.smartloan.smtrick.smart_loan.callback.CallBack;

public interface ImageCompressionService {
    void compressImage(String ImagePath, CallBack callBack);
}
