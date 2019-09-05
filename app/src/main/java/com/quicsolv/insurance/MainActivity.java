package com.quicsolv.insurance;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.quicsolv.insurance.adapters.TaskAdapter;
import com.quicsolv.insurance.apiCalls.ApiService;
import com.quicsolv.insurance.apiCalls.RetrofitClient;
import com.quicsolv.insurance.fragments.IssuesFragment;
import com.quicsolv.insurance.fragments.PendingFragment;
import com.quicsolv.insurance.fragments.TasksFragment;
import com.quicsolv.insurance.pojo.ApplicantDataVO;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public static SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public static int currentViewPagerPosition;
    private static Context mContext;
    public static String BASE_URL = "http://digital.quicsolv.com:3000";

    public static List<ApplicantDataVO> applicantDataList = new ArrayList<>();
    public static ArrayList<ApplicantDataVO> pendingTasksList = new ArrayList<>();
    public static ArrayList<ApplicantDataVO> completedTasksList = new ArrayList<>();
    public static ArrayList<ApplicantDataVO> issuesTaskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        mContext = getApplicationContext();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(pageListener);

        tabLayout.setupWithViewPager(mViewPager);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        getJSONData();
    }

    public static void getJSONData()
    {
        ApiService retroFit = RetrofitClient.getClient(BASE_URL).create(ApiService.class);
        retroFit.getVendorClientList(SplashScreen.vendorID).enqueue(new Callback<List<ApplicantDataVO>>() {
            @Override
            public void onResponse(Call<List<ApplicantDataVO>> call, Response<List<ApplicantDataVO>> response) {
                applicantDataList = response.body();
                Log.e("APPLICANT DATA LIST",""+applicantDataList.size());
                //Toast.makeText(getApplicationContext(),applicantDataList.toString(),Toast.LENGTH_LONG).show();
                performSeparation();
            }

            @Override
            public void onFailure(Call<List<ApplicantDataVO>> call, Throwable t) {
                Log.e("LIST LOAD", t.getMessage());
            }


        });
    }

    private static void performSeparation() {
        if (applicantDataList.size() > 0) {
            issuesTaskList.clear();
            pendingTasksList.clear();
            completedTasksList.clear();
            for (int i = 0; i < applicantDataList.size(); i++) {
                ApplicantDataVO curObj = applicantDataList.get(i);
                //Log.d("Status",curObj.getStatus());
                String status = curObj.getStatus();
                if (status.startsWith("Pending") || status.startsWith("Created")) {
                    pendingTasksList.add(curObj);
                } else if (status.startsWith("On") || status.startsWith("Issues")) {
                    //Log.d("Created task","Found");
                    issuesTaskList.add(curObj);
                } else {
                    completedTasksList.add(curObj);
                }
            }

            //syncDataFromSharedPrefs();


            //TODO Refresh ALL fragments and lists
            //getSupportFragmentManager().
            try {

                PendingFragment.adapter.notifyDataSetChanged();
                IssuesFragment.adapter.notifyDataSetChanged();

            }catch (NullPointerException e)
            {
                Log.v("Exception", "Adapter not loaded");
            }


        }
    }

    private void showAlertDialog()
    {
        //String returnString = "";
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.alert_add_description, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Enter an IP Address");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("IP to connect to");


        final EditText etComments = (EditText) view.findViewById(R.id.etComments);
        etComments.setText(BASE_URL);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    BASE_URL = etComments.getText().toString();
                }catch (IndexOutOfBoundsException e)
                {
                    //THIS HAPPENS WHEN PHOTO IS REJECTED
                    Log.d("URL","Rejected");
                }
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel Image", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        alertDialog.setView(view);
        alertDialog.show();
    }




//    private static void syncDataFromSharedPrefs()
//    {
//        SharedPreferences pref = mContext.getSharedPreferences("MyPref", 0); // 0 - for private mode
//        SharedPreferences.Editor editor = pref.edit();
//
//        String jsonOfPendingList = pref.getString("pendingList", null); // getting String
//        String jsonOfIssuesList = pref.getString("issuesList", null); // getting String
//        String jsonOfCompletedList = pref.getString("completedList", null); // getting String
//
//        try{
//            //ObjectMapper mapper = new ObjectMapper();
//            Gson gson = new Gson();
//            Type type = new TypeToken<ArrayList<ApplicantDataVO>>(){}.getType();
//            ArrayList<ApplicantDataVO> pendingList = gson.fromJson(jsonOfPendingList,type);
//            ArrayList<ApplicantDataVO> issuesList = gson.fromJson(jsonOfIssuesList,type);
//            ArrayList<ApplicantDataVO> completedList = gson.fromJson(jsonOfCompletedList,type);
//            switch(currentViewPagerPosition)
//            {
//                case 0: compareAndSyncStoredData(pendingList,0);
//                break;
//                case 1: compareAndSyncStoredData(issuesList,1);
//                break;
//                case 2: compareAndSyncStoredData(completedList,2);
//                break;
//            }
//        }catch(NullPointerException e)
//        {
//            Log.d("SharedPrefs","Preference does not exist, doing nothing.");
//        }
//    }

//    private static void compareAndSyncStoredData(ArrayList<ApplicantDataVO> listToCompare, int globalList){
//        switch(globalList)
//        {
//            case 0: for(int i=0;i<pendingTasksList.size();i++){
//                ApplicantDataVO currObj1 = pendingTasksList.get(i);
//                for(int j=0;j<listToCompare.size();j++)
//                {
//                    ApplicantDataVO currObj2 = listToCompare.get(i);
//                    if(currObj1.getId().equals(currObj2.getId())){
//                        pendingTasksList.set(i,currObj2);
//                    }
//                }
//            }
//            break;
//            case 1: for(int i=0;i<issuesTaskList.size();i++){
//                ApplicantDataVO currObj1 = issuesTaskList.get(i);
//                for(int j=0;j<listToCompare.size();j++)
//                {
//                    ApplicantDataVO currObj2 = listToCompare.get(i);
//                    if(currObj1.getId().equals(currObj2.getId())){
//                        issuesTaskList.set(i,currObj2);
//                    }
//                }
//            }
//            break;
//            case 2: for(int i=0;i<completedTasksList.size();i++){
//                ApplicantDataVO currObj1 = completedTasksList.get(i);
//                for(int j=0;j<listToCompare.size();j++)
//                {
//                    ApplicantDataVO currObj2 = listToCompare.get(i);
//                    if(currObj1.getId().equals(currObj2.getId())){
//                        completedTasksList.set(i,currObj2);
//                    }
//                }
//            }
//            break;
//        }
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    private ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            currentViewPagerPosition = i;
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };




    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch(position)
            {
                case 0:
                    //CheckListActivity.whichList = 0;
                    return PendingFragment.newInstance(pendingTasksList,0);
                case 1:
                    //CheckListActivity.whichList = 1;
                    return IssuesFragment.newInstance(issuesTaskList,1);
                default: return TasksFragment.newInstance(pendingTasksList,0);
            }
            //return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            switch (position) {
                case 0:
                    return "Pending Tasks";
                case 1:
                    return "Issues";
                default:
                    return null;
            }
        }
    }
}
