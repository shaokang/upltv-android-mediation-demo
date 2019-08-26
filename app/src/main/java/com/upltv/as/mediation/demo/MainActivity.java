package com.upltv.as.mediation.demo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.up.ads.UPAdsSdk;

public class MainActivity extends Activity {
    Button btnVideo;
    Button btnBanner;
    Button btnInterstitial;
    Button btnIsLogOpened;
    CheckBox isChild;
    TextView logStatus;
    boolean hasSetChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //GDPR根据游戏需求确认是否调用。
//        UPAdsSdk.isEuropeanUnionUser(this, new UPAdsSdk.UPEuropeanUnionUserCheckCallBack() {
//            @Override
//            public void isEuropeanUnionUser(boolean isEuropeanUnionUser) {
//                if (isEuropeanUnionUser) {
//                    //这是GDPR第一种授权方式
//                    UPAdsSdk.updateAccessPrivacyInfoStatus(MainActivity.this, AccessPrivacyInfoManager.UPAccessPrivacyInfoStatusEnum.UPAccessPrivacyInfoStatusAccepted);
//                    initSDK();
//                } else {
//                    initSDK();
//                }
//            }
//        });
        isChild = findViewById(R.id.isChild);
        isChild.setVisibility(View.VISIBLE);

        initSDK();

        isChild.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !hasSetChild) {
                    UPAdsSdk.setIsChild(true);
                    hasSetChild = true;
                } else if (!isChecked && !hasSetChild) {
                    UPAdsSdk.setIsChild(false);
                    hasSetChild = true;
                } else {
                    Toast.makeText(MainActivity.this, "已设置，请勿重复设置！", Toast.LENGTH_SHORT).show();
                    isChild.setOnCheckedChangeListener(null);
                    isChild.setChecked(!isChecked);
                }
            }
        });

        btnBanner = findViewById(R.id.btnBanner);
        btnBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BannerActivity.class);
                startActivity(intent);
            }
        });

        btnInterstitial = findViewById(R.id.btnInterstitial);
        btnInterstitial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InterstitialActivity.class);
                startActivity(intent);
            }
        });

        btnVideo = findViewById(R.id.btnVideo);
        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                startActivity(intent);
            }
        });

        logStatus = findViewById(R.id.logStatus);

        btnIsLogOpened = findViewById(R.id.btnIsLogOpen);
        btnIsLogOpened.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = "isLogOpened:" + UPAdsSdk.isLogOpened();
                logStatus.setText(text);
            }
        });

    }

    public String getAndroid(Context context) {
        final String androidId;
        androidId = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        return androidId;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initSDK() {
        UPAdsSdk.init(MainActivity.this, UPAdsSdk.UPAdsGlobalZone.UPAdsGlobalZoneForeign);
    }
}
