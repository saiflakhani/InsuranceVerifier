package com.quicsolv.insurance;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.quicsolv.insurance.pojo.Verifier;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class OTPLogin extends AppCompatActivity {
    Button btnGenerateOTP, btnSignIn;
    EditText etPhoneNumber, etOTP;
    String phoneNumber, otp;
    String vendorID ="";
    FirebaseAuth auth;
    ProgressDialog dialog;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    public String verificationCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_otplogin);
        findViews();
        StartFirebaseLogin();
        btnGenerateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = etPhoneNumber.getText().toString();
                if (checkPhoneNumber(phoneNumber)) {
                    phoneNumber = "+91" + phoneNumber;
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,                     // Phone number to verify
                            20,                           // Timeout duration
                            TimeUnit.SECONDS,                // Unit of timeout
                            OTPLogin.this,        // Activity (for callback binding)
                            mCallback);                      // OnVerificationStateChangedCallbacks
                } else {
                    Toast.makeText(OTPLogin.this, "Invalid Phone number. Please contact Exide Life for assistance.", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        btnSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                otp=etOTP.getText().toString();
//                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);
//                SigninWithPhone(credential);
//            }
//        });
    }

    private boolean checkPhoneNumber(String phoneNumber)
    {
        Pattern p = Pattern.compile("^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$");//. represents single character
        Matcher m = p.matcher(phoneNumber);
        boolean authorized = false;
        if(m.matches()){
            //ProgressDialog dialog = ProgressDialog.show(this,"Please wait, authenticating",)
            Log.d("VERIFIER LIST",SplashScreen.verifierList.toString());
            if(SplashScreen.verifierList!=null && SplashScreen.verifierList.size()>0) {
                for (Verifier v : SplashScreen.verifierList) {
                    if (phoneNumber.contains(v.getContact())) {
                        vendorID = v.getVendorAffiliation();

                        authorized = true;
                        break;
                    }
                }
            }
        }
        return authorized;
    }


    private void SigninWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(OTPLogin.this,MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(OTPLogin.this,"Incorrect OTP",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void findViews() {
        btnGenerateOTP=findViewById(R.id.btnProceedOtp);
        //btnSignIn=findViewById(R.id.btnProceedOtp);
        etPhoneNumber=findViewById(R.id.eTphNo);
        //etOTP=findViewById(R.id.eTphNo);
    }

    private void StartFirebaseLogin() {
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Waiting for code, please wait. (20 seconds max)");
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(OTPLogin.this,"Verification Completed",Toast.LENGTH_SHORT).show();
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                SharedPreferences pref = OTPLogin.this.getSharedPreferences("MyPref", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("verifierPhoneNumber",phoneNumber);
                editor.putString("vendorID",vendorID);
                editor.apply();
                Intent i = new Intent(OTPLogin.this, MainActivity.class);
                startActivity(i);
                finish();
            }
            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(OTPLogin.this,"Verification Failed",Toast.LENGTH_SHORT).show();
                Log.e("TAG",e.getMessage());
                btnGenerateOTP.setEnabled(true);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

            }
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(OTPLogin.this,"Code Sent", Toast.LENGTH_SHORT).show();
                btnGenerateOTP.setEnabled(false);
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String var1)
            {
                Toast.makeText(OTPLogin.this,"Verification Failed",Toast.LENGTH_SHORT).show();
                btnGenerateOTP.setEnabled(true);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        };
    }
}