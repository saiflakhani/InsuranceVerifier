package com.quicsolv.insurance.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.quicsolv.insurance.CheckListActivity;
import com.quicsolv.insurance.R;
import com.quicsolv.insurance.adapters.MyServerDataRecyclerViewAdapter;
import com.quicsolv.insurance.pojo.ApplicantDataVO;
import com.quicsolv.insurance.pojo.QuestionDataVO;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ServerDataFragment extends Fragment {
    List<String> statuses;
    Spinner spinner;
    static boolean savedOnce;

    private static final String ARG_COLUMN_COUNT = "column-count";

    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    List<ApplicantDataVO> myList = new ArrayList<>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ServerDataFragment() {
        myList.clear();
        for(int i=0;i<12;i++)
        {
            ApplicantDataVO obj = new ApplicantDataVO();
            myList.add(obj);
        }
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ServerDataFragment newInstance(ApplicantDataVO applicantDataVO) {
        ServerDataFragment fragment = new ServerDataFragment();
//        Bundle args = new Bundle();
//        try {
//            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//            String json = ow.writeValueAsString(applicantDataVO);
//            //Log.d("JSON -->",json);
//        }catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        args.putSerializable("DATA", applicantDataVO);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_serverdata_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.list);
        Context context = view.getContext();
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.setAdapter(new MyServerDataRecyclerViewAdapter(myList, mListener));
        spinner = view.findViewById(R.id.spinnerStatus);

        statuses = new ArrayList<String>();

        statuses.add("Pending / Created");
        statuses.add("Issues / On hold");
        //statuses.add("Completed");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, statuses);
        spinner.setAdapter(dataAdapter);


        spinner.setOnItemSelectedListener(spinnerListener);
        setSpinnerSelection();
        return view;
    }


    private void setSpinnerSelection()
    {
        String status = CheckListActivity.applicantDataVO.getStatus();
        if (status.startsWith("Pending") || status.startsWith("Created")) {
            spinner.setSelection(0);
        } else if (status.startsWith("On") || status.startsWith("Issues")) {
            //Log.d("Created task","Found");
            spinner.setSelection(1);
        } else {
            spinner.setSelection(0);
        }
    }
    private AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String currentStatus = statuses.get(position);
            CheckListActivity.applicantDataVO.setStatus(currentStatus);
            CheckListActivity activity = (CheckListActivity)getActivity();
            //activity.saveStuffToServer(true,false);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(ApplicantDataVO item);
    }
}
