package com.quicsolv.insurance.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.quicsolv.insurance.MainActivity;
import com.quicsolv.insurance.R;
import com.quicsolv.insurance.pojo.PhotoList;
import com.quicsolv.insurance.pojo.PhotoVO;

import java.util.ArrayList;

public class PhotosAdapter extends ArrayAdapter<PhotoList> {

    private ArrayList<PhotoList> dataSet;
    private Context mContext;

    // View lookup cache
    private static class ViewHolder {
        ImageView iVPhoto;
        TextView tVPhotoDesc;
        Button btnUpload;
    }

    public PhotosAdapter(ArrayList<PhotoList> data, Context context) {
        super(context, R.layout.question_list_item, data);
        this.dataSet = data;
        this.mContext=context;

    }



    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        PhotoList s1 = dataSet.get(position);
        //Log.d("Question", s1.getQuestionText());
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.photo_list_item, parent, false);
            viewHolder.tVPhotoDesc = (TextView) convertView.findViewById(R.id.tVphotoDesc);
            //viewHolder.eTAnswerText = (EditText) convertView.findViewById(R.id.eTAnswer);
            viewHolder.iVPhoto = (ImageView) convertView.findViewById(R.id.iVPhoto);

            //viewHolder.iVattempted.setVisibility(View.GONE);
            //viewHolder.txtVersion = (TextView) convertView.findViewById(R.id.version_number);
            //viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        //Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        //result.startAnimation(animation);
        lastPosition = position;
        //viewHolder.iVattempted.setVisibility(View.INVISIBLE);

        try {
//            viewHolder.tVPhotoDesc.setText(s1.getDescription());
//            viewHolder.iVPhoto.setImageBitmap(StringToBitMap(s1.getPhoto()));
            Glide.with(mContext).load(MainActivity.BASE_URL + s1.getPath()).apply(new RequestOptions().centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)).thumbnail(0.5f).into(viewHolder.iVPhoto);
            viewHolder.tVPhotoDesc.setText(s1.getDescription());
        }catch (NullPointerException e){
            //Toast.makeText(getContext(),"An error occurred",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        // Return the completed view to render on screen
        return result;
    }


//    public Bitmap StringToBitMap(String encodedString){
//        try {
//            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
//            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//            return bitmap;
//        } catch(Exception e) {
//            e.getMessage();
//            return null;
//        }
//    }
}
