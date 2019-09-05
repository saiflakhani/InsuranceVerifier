package com.quicsolv.insurance.adapters;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.quicsolv.insurance.R;
import com.quicsolv.insurance.pojo.QuestionDataVO;

import java.util.ArrayList;

public class QuestionsAdapter extends ArrayAdapter<QuestionDataVO> {

    private ArrayList<QuestionDataVO> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView tVquestionText;
        EditText eTAnswerText;
        ImageView iVattempted;
    }

    public QuestionsAdapter(ArrayList<QuestionDataVO> data, Context context) {
        super(context, R.layout.question_list_item, data);
        this.dataSet = data;
        this.mContext=context;

    }



    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        QuestionDataVO s1 = dataSet.get(position);
        //Log.d("Question", s1.getQuestionText());
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.question_list_item, parent, false);
            viewHolder.tVquestionText = (TextView) convertView.findViewById(R.id.tVQuestionNumber);
            viewHolder.eTAnswerText = (EditText) convertView.findViewById(R.id.eTAnswer);
            viewHolder.iVattempted = (ImageView)convertView.findViewById(R.id.iVAttempted);
            viewHolder.iVattempted.setVisibility(View.GONE);
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
        viewHolder.iVattempted.setVisibility(View.INVISIBLE);

        try {
            viewHolder.tVquestionText.setText(s1.getQuestionText());
            viewHolder.eTAnswerText.setText(s1.getAnswer());
            if(!s1.getAnswer().equals(""))
            {
                viewHolder.iVattempted.setVisibility(View.VISIBLE);
            }
            //viewHolder.txtType.setText(QuestionDataVO.getType());
            //viewHolder.txtVersion.setText(QuestionDataVO.getVersion_number());
            //viewHolder.tVquestionText.setOnClickListener(this);
            //viewHolder.tVquestionText.setTag(position);
        }catch (NullPointerException e){
            //Toast.makeText(getContext(),"An error occurred",Toast.LENGTH_SHORT).show();
            //e.printStackTrace();
        }
        // Return the completed view to render on screen
        return result;
    }
}
