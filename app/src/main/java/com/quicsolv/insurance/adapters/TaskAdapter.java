package com.quicsolv.insurance.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.quicsolv.insurance.CheckListActivity;
import com.quicsolv.insurance.R;
import com.quicsolv.insurance.SplashScreen;
import com.quicsolv.insurance.pojo.ApplicantDataVO;
import com.quicsolv.insurance.pojo.Vendor;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends ArrayAdapter<ApplicantDataVO> implements View.OnClickListener{

    private ArrayList<ApplicantDataVO> dataSet;
    private Context mContext;
    private Activity activity;

    // View lookup cache
    private static class ViewHolder {
        TextView applicantName;
        TextView docType, docPriority;
        TextView region;
        TextView pin, tVPolicyNo;
//        ImageView background;
        RelativeLayout listItem;
    }

    public TaskAdapter(ArrayList<ApplicantDataVO> data, Context context, Activity activity) {
        super(context, R.layout.list_item, data);
        this.dataSet = data;
        this.mContext=context;
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();

//        Object object = getItem(position);
//        ApplicantDataVO applicantDataVO = (ApplicantDataVO)object;
        ApplicantDataVO applicantDataVO = dataSet.get(position);
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
            viewHolder.docPriority = (TextView) convertView.findViewById(R.id.docPriority);
            viewHolder.pin = (TextView) convertView.findViewById(R.id.tVpinCode);
            viewHolder.tVPolicyNo = convertView.findViewById(R.id.tVPolicyNo);
//            viewHolder.background = (ImageView)convertView.findViewById(R.id.iVBack);

            result = convertView;

        //Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        //result.startAnimation(animation);
        lastPosition = position;

        viewHolder.applicantName.setText(applicantDataVO.getApplicantName());
        if (applicantDataVO.getPriority().equals("L")) {
            viewHolder.docPriority.setText("Priority: Low");
        } else if (applicantDataVO.getPriority().equals("M")) {
            viewHolder.docPriority.setText("Priority: Medium");
        } else if (applicantDataVO.getPriority().equals("H")) {
            viewHolder.docPriority.setText("Priority: High");
        } else {
            viewHolder.docPriority.setVisibility(View.GONE);
        }
        viewHolder.region.setText(applicantDataVO.getRegion());
        for (Vendor vendor: applicantDataVO.getVendors()) {
            if (vendor.getVendorID().equals(SplashScreen.vendorID)) {
                if(applicantDataVO.getVendors().get(applicantDataVO.getVendors().indexOf(vendor)).getVendorWork()!= null) {
                    viewHolder.docType.setText(applicantDataVO.getVendors().get(applicantDataVO.getVendors().indexOf(vendor)).getVendorWork() + " / "+applicantDataVO.getInvCategory()+" / "+applicantDataVO.getInvType());
                }
            }
        }

        viewHolder.tVPolicyNo.setText("Policy No. ".concat(applicantDataVO.getPolicyNo()));

        viewHolder.listItem.setOnClickListener(this);
        viewHolder.listItem.setTag(position);
        //viewHolder.listItem.setTag(9,position);
        // Return the completed view to render on screen
        return convertView;
    }
}
