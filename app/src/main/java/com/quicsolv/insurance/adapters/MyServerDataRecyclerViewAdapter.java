package com.quicsolv.insurance.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quicsolv.insurance.CheckListActivity;
import com.quicsolv.insurance.R;
import com.quicsolv.insurance.SplashScreen;
import com.quicsolv.insurance.fragments.ServerDataFragment.OnListFragmentInteractionListener;

import com.quicsolv.insurance.pojo.ApplicantDataVO;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MyServerDataRecyclerViewAdapter extends RecyclerView.Adapter<MyServerDataRecyclerViewAdapter.ViewHolder> {

    private final List<ApplicantDataVO> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyServerDataRecyclerViewAdapter(List<ApplicantDataVO> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_serverdata, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        String s1="",s2 ="";
        switch(position)
        {
            case 0: s1="Applicant Name";
                    s2 = CheckListActivity.applicantDataVO.getApplicantName();
                    break;
            case 1: s1="Vendor ID";
//                s2 = CheckListActivity.applicantDataVO.getVendorID();
                s2 = SplashScreen.vendorID;
                break;
            case 2: s1="Region";
                s2 = CheckListActivity.applicantDataVO.getRegion();
                break;
            case 3: s1="Pin Code";
                s2 = CheckListActivity.applicantDataVO.getPinCode();
                break;
            case 4: s1="Watch Category";
                s2 = CheckListActivity.applicantDataVO.getWatchCategory();
                break;
            case 5: s1="Referenced By";
                s2 = CheckListActivity.applicantDataVO.getReferencedBy();
                break;
            case 6: s1="Receipt Date";
                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                DateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm aa", Locale.getDefault());
                String outputDateStr;
                try {
                    Date date = inputFormat.parse(CheckListActivity.applicantDataVO.getReceiptDate());
                    outputDateStr = outputFormat.format(date);
//                    outputDateStr = "on " + outputDateStr;
                } catch (ParseException e) {
                    e.printStackTrace();
                    outputDateStr = "";
                }
                s2 = outputDateStr;
                break;
            case 7: s1="Contact";
                s2 = CheckListActivity.applicantDataVO.getContact();
                break;
            case 8: s1="Investigation Category";
                s2 = CheckListActivity.applicantDataVO.getInvCategory();
                break;
            case 9: s1="Investigation Type";
                s2 = CheckListActivity.applicantDataVO.getInvType();
                break;
            case 10: s1="Investigation Results";
                s2 = CheckListActivity.applicantDataVO.getInvResults();
                break;
            case 11: s1="Status";
                s2 = CheckListActivity.applicantDataVO.getStatus();
                break;
            default: holder.rLContent.setVisibility(View.GONE);
                break;
        }

        holder.mIdView.setText(s1);
        holder.mContentView.setText(s2);

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public ApplicantDataVO mItem;
        public RelativeLayout rLContent;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
            rLContent = view.findViewById(R.id.rLContent);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
