package com.quicsolv.insurance.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.quicsolv.insurance.CheckListActivity;
import com.quicsolv.insurance.R;
import com.quicsolv.insurance.adapters.TaskAdapter;
import com.quicsolv.insurance.pojo.ApplicantDataVO;

import java.util.ArrayList;

import static com.quicsolv.insurance.MainActivity.getJSONData;

public class PendingFragment extends Fragment
    {
        public PendingFragment(){}
        static ArrayList<ApplicantDataVO> curList = new ArrayList<>();
        public static TaskAdapter adapter;
        public static ListView lVTasks;
        public static TextView textView;
        static int curPosition = 0;
        public SwipeRefreshLayout pullToRefresh;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            CheckListActivity.whichList = 0;
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            textView = (TextView) rootView.findViewById(R.id.section_label);
            pullToRefresh = (SwipeRefreshLayout)rootView.findViewById(R.id.pullToRefresh);
            pullToRefresh.setOnRefreshListener(pullToRefreshListener);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            int position = getArguments().getInt("CurPos",0);
            adapter = new TaskAdapter(curList,getActivity(), getActivity());
            //Log.v("LIST -->", "" + curList.size());
            lVTasks = rootView.findViewById(R.id.lVTasks);
            lVTasks.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            return rootView;
        }

        public static PendingFragment newInstance(ArrayList<ApplicantDataVO> listofTasks, int position)
        {
            PendingFragment fragment = new PendingFragment();
            curList = listofTasks;
            curPosition = position;
            Bundle args = new Bundle();
            args.putInt("CurPos",position);
            fragment.setArguments(args);
            return fragment;
        }
        public SwipeRefreshLayout.OnRefreshListener pullToRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getJSONData();
                pullToRefresh.setRefreshing(false);
            }
        };
    }

