package com.quicsolv.insurance.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quicsolv.insurance.CheckListActivity;
import com.quicsolv.insurance.R;
import com.quicsolv.insurance.pojo.ApplicantDataVO;

import java.util.ArrayList;

public class TaskAdapter extends ArrayAdapter<ApplicantDataVO> implements View.OnClickListener{

    private ArrayList<ApplicantDataVO> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView applicantName;
        TextView docType;
        TextView region;
        TextView pin;
        ImageView background;
        RelativeLayout listItem;
    }

    public TaskAdapter(ArrayList<ApplicantDataVO> data, Context context) {
        super(context, R.layout.list_item, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();

        Object object= getItem(position);
        ApplicantDataVO applicantDataVO = (ApplicantDataVO)object;
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", applicantDataVO);
        bundle.putInt("position", position);
        //TODO DIFFERENTIATE BETWEEN FRAGMENTS HERE

        Intent i = new Intent(getContext(), CheckListActivity.class);
        i.putExtras(bundle);
        getContext().startActivity(i);
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ApplicantDataVO applicantDataVO = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;


        viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder.listItem = (RelativeLayout)convertView.findViewById(R.id.rLListItem);
            viewHolder.applicantName = (TextView) convertView.findViewById(R.id.tVapplicantName);
            viewHolder.docType = (TextView) convertView.findViewById(R.id.docType);
            viewHolder.region = (TextView) convertView.findViewById(R.id.tVRegion);
            viewHolder.pin = (TextView) convertView.findViewById(R.id.tVpinCode);
            viewHolder.background = (ImageView)convertView.findViewById(R.id.iVBack);

            result=convertView;

        //Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        //result.startAnimation(animation);
        lastPosition = position;

        viewHolder.applicantName.setText(applicantDataVO.getApplicantName());
        viewHolder.region.setText(applicantDataVO.getRegion());
        viewHolder.docType.setText(applicantDataVO.getDocumentType() + " / "+applicantDataVO.getInvCategory()+" / "+applicantDataVO.getInvType());
        viewHolder.listItem.setOnClickListener(this);
        viewHolder.listItem.setTag(position);
        //viewHolder.listItem.setTag(9,position);
        // Return the completed view to render on screen
        return convertView;
    }
}
