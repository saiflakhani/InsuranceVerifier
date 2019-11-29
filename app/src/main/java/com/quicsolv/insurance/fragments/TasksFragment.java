package com.quicsolv.insurance.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.quicsolv.insurance.CheckListActivity;
import com.quicsolv.insurance.R;
import com.quicsolv.insurance.adapters.TaskAdapter;
import com.quicsolv.insurance.apiCalls.ApiService;
import com.quicsolv.insurance.apiCalls.RetrofitClient;
import com.quicsolv.insurance.pojo.ApplicantDataVO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.quicsolv.insurance.MainActivity.getJSONData;

public class TasksFragment extends Fragment {
    public TasksFragment() {
    }

    static ArrayList<ApplicantDataVO> curList = new ArrayList<>();
    public static TaskAdapter adapter;
    public SwipeRefreshLayout pullToRefresh;
    public static boolean refreshDone = false;
    static int curPosition = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        CheckListActivity.whichList = 2;
        pullToRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(pullToRefreshListener);
        //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
        int position = getArguments().getInt("CurPos", 0);
        adapter = new TaskAdapter(curList, getActivity(), getActivity());
        //Log.v("LIST -->", "" + curList.size());
        ListView lVTasks = rootView.findViewById(R.id.lVTasks);
        lVTasks.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return rootView;
    }

    public static TasksFragment newInstance(ArrayList<ApplicantDataVO> listofTasks, int position) {
        TasksFragment fragment = new TasksFragment();
        curList = listofTasks;
        curPosition = position;
        Bundle args = new Bundle();
        args.putInt("CurPos", position);
        fragment.setArguments(args);
        return fragment;
    }

    public SwipeRefreshLayout.OnRefreshListener pullToRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            refreshDone = false;
            getJSONData();
            refreshDone = true;
            pullToRefresh.setRefreshing(false);
        }
    };

}

