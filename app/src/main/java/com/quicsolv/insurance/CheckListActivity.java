package com.quicsolv.insurance;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.quicsolv.insurance.apiCalls.ApiService;
import com.quicsolv.insurance.apiCalls.RetrofitClient;
import com.quicsolv.insurance.fragments.IssuesFragment;
import com.quicsolv.insurance.fragments.PendingFragment;
import com.quicsolv.insurance.fragments.PhotosFragment;
import com.quicsolv.insurance.fragments.QuestionsFragment;
import com.quicsolv.insurance.fragments.ServerDataFragment;
import com.quicsolv.insurance.fragments.TasksFragment;
import com.quicsolv.insurance.pojo.ApplicantDataVO;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckListActivity extends AppCompatActivity {
    MyPageAdapter pageAdapter;
    public static ApplicantDataVO applicantDataVO;
    public static int currentPosition = -1;
    public static int whichList = -1;
    ProgressDialog dialog;
    boolean showOnce = false;
    public static boolean photoTaken = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);
        //List<Fragment> fragments = getFragments();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarlist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        pageAdapter = new MyPageAdapter(getSupportFragmentManager());
        TabLayout tb = findViewById(R.id.tabLayoutCheckList);

        ViewPager pager =
                (ViewPager)findViewById(R.id.chkListVp);
        pager.setAdapter(pageAdapter);
        tb.setupWithViewPager(pager);
        photoTaken = false;
        Bundle bundle = getIntent().getExtras();
        try {
            applicantDataVO = (ApplicantDataVO) bundle.getSerializable("data");
            currentPosition = bundle.getInt("position");
        }catch (NullPointerException e){
            Log.e("NO OBJ","NONE");
            e.printStackTrace();
        }
        Button saveOnline = findViewById(R.id.btnsaveOnline);
        saveOnline.setOnClickListener(saveOnlineClick);

//        Button submitTask = findViewById(R.id.btnSubmitTask);
//        submitTask.setOnClickListener(submitClick);
        //pager.setAdapter(pageAdapter);
    }

    private View.OnClickListener submitClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(checkIfEverythingFilledUp())
            {
                applicantDataVO.setStatus("Completed");
                saveStuffToServer(true,true);
            }
        }
    };

    private boolean checkIfEverythingFilledUp()
    {
        boolean flag = false;
        try{
            if((applicantDataVO.getQuestionsAsked().size() == QuestionsFragment.questionsList.size()) && QuestionsFragment.questionsList.size()!=0){
                if(applicantDataVO.getPhotoList().size()>=1){
                    flag=true;
                }
            }
        }catch (NullPointerException e)
        {
            flag = false;
        }

        return flag;
    }

    class MyPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;
        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch(position)
            {
                case 0: return ServerDataFragment.newInstance(applicantDataVO);
                case 1: return QuestionsFragment.newInstance("Hello");
                case 2: return PhotosFragment.newInstance("1","2");
                default: return MainActivity.PlaceholderFragment.newInstance(0);
            }
            //return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            switch (position) {
                case 0:
                    return "Status";
                case 1:
                    return "Questions";
                case 2:
                    return "Photos";
                default:
                    return null;
            }
        }
    }

    private void showSaveDialog(){
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Save Data");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Would you like to save the photos online before quitting?\nPhotos will be lost if cancelled.");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showOnce = true;
                saveStuffToServer(true,true);
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showOnce = true;
                //CheckListActivity.super.onBackPressed();
                finish();
            }
        });

        alertDialog.show();
    }

    private void showSubmitDialog(){
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Save Data");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Would you like to submit this case?\nYou will not be able to change it again.");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                applicantDataVO.setStatus("Verifier Submitted");
                showOnce = true;
                saveStuffToServer(true,true);
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //showOnce = true;
                //CheckListActivity.super.onBackPressed();
                //finish();
            }
        });

        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        String s = "hello";
        if(applicantDataVO.getPhotoList()!=null && applicantDataVO.getPhotoList().size()>0 && !showOnce && photoTaken)
            showSaveDialog();

        else {

            if (QuestionsFragment.isOpen && QuestionsFragment.previousView != null) {
                QuestionsFragment.previousView.findViewById(R.id.rLAnswer).setVisibility(View.GONE);
                QuestionsFragment.isOpen = false;

            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private View.OnClickListener saveOnlineClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showSubmitDialog();
        }
    };

    public void saveStuffToServer(boolean saveData, boolean savePhotos)
    {
        dialog = new ProgressDialog(CheckListActivity.this);
        dialog.setCancelable(true);
        dialog.setMessage("Uploading, please wait");
        dialog.setTitle("Saving to server");
        Gson gson = new Gson();
        String jsonString = gson.toJson(applicantDataVO);
        if(saveData) {
            dialog.show();
            ApiService retroFit = RetrofitClient.getClient(MainActivity.BASE_URL).create(ApiService.class);
            retroFit.addAnswer(applicantDataVO).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d("OK SUCCESS", "" + response.body());
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("FAILuRE", "" + t.getMessage());
                    dialog.dismiss();
                }
            });
        }
        if(savePhotos) {
            dialog.show();
            showOnce = true;
            if(applicantDataVO.getPhotoList() != null) {
                for (int i = 0; i < applicantDataVO.getPhotoList().size(); i++) {
                    uploadPhotoToServer(applicantDataVO.getPhotoList().get(i).getLocalPath(), i);
                }
            }
        }
    }



    private void uploadPhotoToServer(String picturePath, final int whichPhoto) {
        //We worshipped you, your red right hand, won't we see you once again

        ApiService retroFit = RetrofitClient.getClient(MainActivity.BASE_URL).create(ApiService.class);

        File file = new File(picturePath);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("photo",file.getName(),requestFile);

        Call<ResponseBody> responseBodyCall = retroFit.uploadPhoto(multipartBody);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("Success", "success "+response.code());
                Log.d("Success", "success "+response.message());
                if(whichPhoto == applicantDataVO.getPhotoList().size()-1){
                    Toast.makeText(CheckListActivity.this,"Success!",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                showOnce = true;
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("failure", "message = " + t.getMessage());
                Log.d("failure", "cause = " + t.getCause());
                showOnce = false;
                Toast.makeText(CheckListActivity.this,"Failed",Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });



    }

}
