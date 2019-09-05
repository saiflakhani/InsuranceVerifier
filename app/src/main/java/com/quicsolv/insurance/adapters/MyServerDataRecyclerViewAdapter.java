package com.quicsolv.insurance.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quicsolv.insurance.CheckListActivity;
import com.quicsolv.insurance.R;
import com.quicsolv.insurance.fragments.ServerDataFragment.OnListFragmentInteractionListener;

import com.quicsolv.insurance.pojo.ApplicantDataVO;

import java.util.List;



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
                s2 = CheckListActivity.applicantDataVO.getVendorID();
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
                s2 = CheckListActivity.applicantDataVO.getReceiptDate();
                break;
            case 7: s1="Document Type";
                s2 = CheckListActivity.applicantDataVO.getDocumentType();
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
        }

        holder.mIdView.setText(s1);
        holder.mContentView.setText(s2);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
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

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
