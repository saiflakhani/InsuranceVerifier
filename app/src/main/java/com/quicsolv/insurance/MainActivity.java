package com.quicsolv.insurance;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.quicsolv.insurance.apiCalls.ApiService;
import com.quicsolv.insurance.apiCalls.RetrofitClient;
import com.quicsolv.insurance.fragments.PendingFragment;
import com.quicsolv.insurance.fragments.TasksFragment;
import com.quicsolv.insurance.pojo.ApplicantDataVO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static SectionsPagerAdapter mSectionsPagerAdapter;
    public static int currentViewPagerPosition;
    private Context mContext;
    public static String BASE_URL = "http://digital.quicsolv.com:3000/";

    public static List<ApplicantDataVO> applicantDataList = new ArrayList<>();
    public static ArrayList<ApplicantDataVO> pendingTasksList = new ArrayList<>();
    public static ArrayList<ApplicantDataVO> completedTasksList = new ArrayList<>();
//    public static ArrayList<ApplicantDataVO> issuesTaskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        mContext = getApplicationContext();
        tabLayout.setVisibility(View.GONE);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.

        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(pageListener);

        tabLayout.setupWithViewPager(mViewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.hide();
//        fab.setOnClickListener(view -> showAlertDialog());

    }

    @Override
    protected void onResume() {
        super.onResume();
        getJSONData();
    }

    public static void getJSONData()
    {
        ApiService retroFit = RetrofitClient.getClient(BASE_URL).create(ApiService.class);
        retroFit.getVendorClientList(SplashScreen.phoneNumber).enqueue(new Callback<List<ApplicantDataVO>>() {
            @Override
            public void onResponse(Call<List<ApplicantDataVO>> call, Response<List<ApplicantDataVO>> response) {
                applicantDataList = response.body();
                if(applicantDataList != null) {
                    Log.e("APPLICANT DATA LIST", "" + applicantDataList.size());
                    //Toast.makeText(getApplicationContext(),applicantDataList.toString(),Toast.LENGTH_LONG).show();
                    performSeparation();
                }
            }

            @Override
            public void onFailure(Call<List<ApplicantDataVO>> call, Throwable t) {
                Log.e("LIST LOAD", t.getMessage());
            }


        });
    }

    private static void performSeparation() {
        if (applicantDataList.size() > 0) {
//            issuesTaskList.clear();
            pendingTasksList.clear();
            completedTasksList.clear();
            for (int i = 0; i < applicantDataList.size(); i++) {
                ApplicantDataVO curObj = applicantDataList.get(i);
                String status = curObj.getStatus();
                if (status.startsWith("Assigned to verifier") || status.startsWith("Reassign by verifier") || status.startsWith("Reassigned by vendor")  || status.startsWith("Assign to verifier") || status.startsWith("Pending / Created")) {
                    pendingTasksList.add(curObj);
                } else if (status.startsWith("On hold") || status.startsWith("Issues")) {
//                    issuesTaskList.add(curObj);
                } else {
                    completedTasksList.add(curObj);
                }
            }

            //syncDataFromSharedPrefs();


            //TODO Refresh ALL fragments and lists
            //getSupportFragmentManager().
            try {
                PendingFragment.adapter.notifyDataSetChanged();
//                IssuesFragment.adapter.notifyDataSetChanged();
                if (pendingTasksList.size() > 0) {
                    PendingFragment.textView.setVisibility(View.GONE);
                } else PendingFragment.textView.setVisibility(View.VISIBLE);
//                if (issuesTaskList.size() > 0) {
//                    IssuesFragment.textView.setVisibility(View.GONE);
//                } else IssuesFragment.textView.setVisibility(View.VISIBLE);
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

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

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
            TextView textView = rootView.findViewById(R.id.section_label);
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

        SectionsPagerAdapter(FragmentManager fm) {
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
//                case 1:
//                    //CheckListActivity.whichList = 1;
//                    return IssuesFragment.newInstance(issuesTaskList,1);
                default: return TasksFragment.newInstance(pendingTasksList,0);
            }
            //return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            switch (position) {
                case 0:
                    return "Pending Tasks";
//                case 1:
//                    return "Issues";
                default:
                    return null;
            }
        }
    }
}
