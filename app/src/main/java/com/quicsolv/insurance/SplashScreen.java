package com.quicsolv.insurance;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.quicsolv.insurance.apiCalls.ApiService;
import com.quicsolv.insurance.apiCalls.RetrofitClient;
import com.quicsolv.insurance.pojo.Verifier;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends Activity {

    private final int SPLASH_DISPLAY_LENGTH = 500;
    public static List<Verifier> verifierList;
    public static String phoneNumber = null;
    public static String vendorID = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        phoneNumber = pref.getString("verifierPhoneNumber",null);
        vendorID = pref.getString("vendorID",null);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                ApiService retroFit = RetrofitClient.getClient(MainActivity.BASE_URL).create(ApiService.class);
                retroFit.getVerifierList().enqueue(new Callback<List<Verifier>>() {
                    @Override
                    public void onResponse(Call<List<Verifier>> call, Response<List<Verifier>> response) {
                        verifierList = response.body();
                        if(phoneNumber==null) {
                            Intent mainIntent = new Intent(SplashScreen.this, OTPLogin.class);
                            SplashScreen.this.startActivity(mainIntent);
                            SplashScreen.this.finish();
                        }else {
                            Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                            SplashScreen.this.startActivity(mainIntent);
                            SplashScreen.this.finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Verifier>> call, Throwable t) {
                        Toast.makeText(SplashScreen.this,"No internet connection. Please try again.",Toast.LENGTH_SHORT).show();
                        ProgressBar bar = findViewById(R.id.progressBar);
                        bar.setVisibility(View.INVISIBLE);
                        Button btnRetry = findViewById(R.id.btnRetry);
                        btnRetry.setVisibility(View.VISIBLE);
                        btnRetry.setOnClickListener(retryClick);
                    }
                });
                /* Create an Intent that will start the Menu-Activity. */

            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    private View.OnClickListener retryClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ApiService retroFit = RetrofitClient.getClient(MainActivity.BASE_URL).create(ApiService.class);
            final ProgressDialog dialog = new ProgressDialog(SplashScreen.this);
            dialog.setMessage("Please wait..");
            dialog.setCancelable(false);
            retroFit.getVerifierList().enqueue(new Callback<List<Verifier>>() {
                @Override
                public void onResponse(Call<List<Verifier>> call, Response<List<Verifier>> response) {
                    verifierList = response.body();
                    Log.d("Response",response.body().toString());
                    if(dialog.isShowing()){
                        dialog.dismiss();
                    }
                    if(phoneNumber==null) {
                        Intent mainIntent = new Intent(SplashScreen.this, OTPLogin.class);
                        SplashScreen.this.startActivity(mainIntent);
                        SplashScreen.this.finish();
                    }else {
                        Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                        SplashScreen.this.startActivity(mainIntent);
                        SplashScreen.this.finish();
                    }

                }

                @Override
                public void onFailure(Call<List<Verifier>> call, Throwable t) {
                    if(dialog.isShowing()){
                        dialog.dismiss();
                    }
                    Toast.makeText(SplashScreen.this,"No internet connection. Please try again.",Toast.LENGTH_SHORT).show();
                }
            });
        }
    };
}
