package com.quicsolv.insurance;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class VerifyOTP extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_verify_otp);
    }
}
