package com.volkangurbuz.lightme;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class LightActivity extends AppCompatActivity {

    private static final int CAMERA_REQ = 2;
    private boolean flashPermission = false;
    private Switch aSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);
        aSwitch = findViewById(R.id.runSwitch);


        final boolean hasCameraFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        // boolean isEnabled = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;

        ActivityCompat.requestPermissions(LightActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQ);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (hasCameraFlash && flashPermission) {

                    if (isChecked) {

                        flashLightOn();
                    } else
                        flashLightOff();
                } else
                    Toast.makeText(LightActivity.this, "no flash avaliable on your device or chech the permission.", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void flashLightOff() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);


        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }


    private void flashLightOn() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);


        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQ:
                if ((grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    flashPermission = true;

                } else {
                    flashPermission = false;
                    Toast.makeText(this, "permission denied for the camera", Toast.LENGTH_SHORT).show();
                }

                break;
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        flashLightOff();
    }
}
