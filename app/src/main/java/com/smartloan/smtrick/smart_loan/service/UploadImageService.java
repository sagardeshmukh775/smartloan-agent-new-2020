package com.smartloan.smtrick.smart_loan.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.IBinder;
import android.os.SystemClock;

import com.smartloan.smtrick.smart_loan.callback.CallBack;
import com.smartloan.smtrick.smart_loan.constants.Constant;
import com.smartloan.smtrick.smart_loan.firebasestorage.StorageService;
import com.smartloan.smtrick.smart_loan.models.ImagesModel;
import com.smartloan.smtrick.smart_loan.repository.LeedRepository;
import com.smartloan.smtrick.smart_loan.repository.impl.LeedRepositoryImpl;
import com.smartloan.smtrick.smart_loan.service.impl.ImageCompressionServiceImp;
import com.smartloan.smtrick.smart_loan.utilities.FileUtils;
import com.smartloan.smtrick.smart_loan.utilities.Utility;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UploadImageService extends Service {

    public UploadImageService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        try {
            if (intent != null) {
                ArrayList<Uri> imagesUriList = (ArrayList<Uri>) intent.getSerializableExtra(Constant.IMAGE_URI_LIST);
                String leedId = intent.getStringExtra(Constant.LEED_ID);
                if (imagesUriList != null && !imagesUriList.isEmpty()) {
                    for (Uri uri : imagesUriList) {
                   /* Intent intentToUpload = new Intent(this, ImageUploadIntentService.class);
                    intentToUpload.putExtra(Constant.BITMAP_IMG, uri);
                    intentToUpload.putExtra(Constant.STORAGE_PATH, Constant.DOCUMENTS_PATH);
                    intentToUpload.putExtra(Constant.LEED_ID, leedId);
                    this.startService(intentToUpload);*/

                        if (uri != null) {
                            String path = FileUtils.getPath(this, uri);
                            compressBitmap(leedId, path, Constant.DOCUMENTS_PATH);
                        }
                    }
                } else
                    stopSelf();
            }
        } catch (Exception e) {
            e.printStackTrace();
            stopSelf();
        }
        return START_STICKY_COMPATIBILITY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void compressBitmap(final String leedId, String bitmapPath, final String storagePath) {
        ImageCompressionService imageCompressionService = new ImageCompressionServiceImp();
        imageCompressionService.compressImage(bitmapPath, new CallBack() {
            @Override
            public void onSuccess(Object object) {
                if (object != null) {
                    Bitmap bitmap = (Bitmap) object;
                    uploadImage(leedId, bitmap, storagePath);
                }
            }

            @Override
            public void onError(Object object) {
            }
        });
    }

    void uploadImage(final String leedId, Bitmap bitmap, String storagePath) {
        try {
            InputStream imageInputStream = Utility.returnInputStreamFromBitmap(bitmap);
            StorageService.uploadImageStreamToFirebaseStorage(imageInputStream, storagePath, new CallBack() {
                public void onSuccess(Object object) {
                    //  final Uri downloadUrlLarge= (Uri) object;
                    String downloadUrlLarge = (String) object;
                    updateLeed(leedId, downloadUrlLarge);
                }

                public void onError(Object object) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateLeed(String leedId, String imagePath) {
        LeedRepository leedRepository = new LeedRepositoryImpl();
        ImagesModel imagesModel = new ImagesModel();
        imagesModel.setLargImage(imagePath);
        String key = Constant.LEEDS_TABLE_REF.push().getKey();
        Map<String, ImagesModel> map = new HashMap<>();
        map.put(key, imagesModel);
        leedRepository.updateLeedDocuments(leedId, map, new CallBack() {
            @Override
            public void onSuccess(Object object) {
            }

            @Override
            public void onError(Object object) {
            }
        });
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }
}
