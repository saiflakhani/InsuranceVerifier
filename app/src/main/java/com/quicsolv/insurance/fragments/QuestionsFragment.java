package com.quicsolv.insurance.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quicsolv.insurance.CheckListActivity;
import com.quicsolv.insurance.MainActivity;
import com.quicsolv.insurance.R;
import com.quicsolv.insurance.adapters.QuestionsAdapter;
import com.quicsolv.insurance.apiCalls.ApiService;
import com.quicsolv.insurance.apiCalls.RetrofitClient;
import com.quicsolv.insurance.pojo.QuestionDataVO;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

public class QuestionsFragment extends Fragment {
    public static final String Question = "QUESTION";
    public static View previousView = null;
    public static boolean isOpen = false;
    public static List<QuestionDataVO> questionsList = new ArrayList<>();
    static List<String> questionsListdummy = new ArrayList<>();
    static String currentAnswer;
    public SwipeRefreshLayout pullToRefresh;
    QuestionsAdapter adapter;
    int prevPosition = -1;
    public View.OnClickListener closeClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            previousView.findViewById(R.id.rLAnswer).setVisibility(View.GONE);
            EditText eTAnswer = previousView.findViewById(R.id.eTAnswer);
            String answer = eTAnswer.getText().toString();
            closeKeyboard();
            if (!answer.equals("")) {
                boolean wasSaveSuccessful = writeToDatabase(answer);
//                if (wasSaveSuccessful) {
//                    Button btnSave = previousView.findViewById(R.id.btnSave);
//                    btnSave.setVisibility(View.VISIBLE);
//                } else {
//                    Button btnSave = previousView.findViewById(R.id.btnSave);
//                    btnSave.setVisibility(View.INVISIBLE);
//                }
                adapter.notifyDataSetChanged();
            }
            isOpen = false;
        }
    };

    public void closeKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public AdapterView.OnItemClickListener expandableItemClickListener = (parent, view, position, id) -> {
        RelativeLayout expandable = view.findViewById(R.id.rLAnswer);
        EditText eTAnswer = view.findViewById(R.id.eTAnswer);
        eTAnswer.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(eTAnswer, InputMethodManager.SHOW_IMPLICIT);
        ImageView iVClose = view.findViewById(R.id.iVClose);
        iVClose.setOnClickListener(closeClick);
        Button btnSave = view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(closeClick);
        if (isFirstTime()) {
            new MaterialShowcaseView.Builder(getActivity())
                    .setTarget(btnSave)
                    .setDismissText("GOT IT")
                    .setContentText("This button saves your answer before you close it.")
                    .singleUse("ranBefore") // provide a unique ID used to ensure it is only shown once
                    .show();
        }
        if (prevPosition == position && isOpen) {
            expandable.setVisibility(View.GONE);
            isOpen = false;
        } else {
            expandable.setVisibility(View.VISIBLE);
            isOpen = true;
        }
        if (previousView != null && position != prevPosition) {
            previousView.findViewById(R.id.rLAnswer).setVisibility(View.GONE);
        }
        previousView = view;
        prevPosition = position;

        //if text is changed, store in a question object, add to applicantdataVO, and send to server
    };
    TextView tVQuestions;
    public SwipeRefreshLayout.OnRefreshListener pullToRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            getQuestionsFromServer();
            pullToRefresh.setRefreshing(false);
        }
    };

    public static final QuestionsFragment newInstance(String message) {
        QuestionsFragment f = new QuestionsFragment();
        Bundle bdl = new Bundle(1);
        bdl.putString(Question, message);
        for (int i = 0; i < 4; i++) {
            questionsListdummy.add("Question " + i);
        }
        f.setArguments(bdl);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String message = getArguments().getString(Question);
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        tVQuestions = (TextView) v.findViewById(R.id.section_label);
        tVQuestions.setText("There are no questions at the moment");
        pullToRefresh = (SwipeRefreshLayout) v.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(pullToRefreshListener);

        getQuestionsFromServer();


        if (!questionsList.isEmpty()) tVQuestions.setVisibility(View.INVISIBLE);

        ListView lVQuestions = (ListView) v.findViewById(R.id.lVTasks);
        adapter = new QuestionsAdapter((ArrayList<QuestionDataVO>) questionsList, getActivity());
        lVQuestions.setAdapter(adapter);
        lVQuestions.setOnItemClickListener(expandableItemClickListener);
        return v;
    }

    private void getQuestionsFromServer() {
        ApiService retroFit = RetrofitClient.getClient(MainActivity.BASE_URL).create(ApiService.class);
        retroFit.getQuestionsList().enqueue(new Callback<List<QuestionDataVO>>() {
            @Override
            public void onResponse(@NonNull Call<List<QuestionDataVO>> call, @NonNull Response<List<QuestionDataVO>> response) {
                if (response.body() != null) {
                    questionsList.clear();
                    questionsList.addAll(response.body());
                    if (!questionsList.isEmpty()) tVQuestions.setVisibility(View.INVISIBLE);
                    adapter.notifyDataSetChanged();
                    syncLocalAnswers();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<QuestionDataVO>> call, @NonNull Throwable t) {
                Log.e("Questions load", t.getMessage());
            }
        });
    }

    private void syncLocalAnswers() {
        for (int i = 0; i < questionsList.size(); i++) {
            List<QuestionDataVO> localList = CheckListActivity.applicantDataVO.getQuestionsAsked();
            for (int j = 0; j < localList.size(); j++) {
                if (localList.get(j).getQuestionID().equals(questionsList.get(i).getQuestionID())) {
                    questionsList.set(i, localList.get(j));
                }
            }
        }
    }

    private boolean isFirstTime() {
        SharedPreferences preferences = getActivity().getSharedPreferences("MyPref", 0);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.apply();
        }
        return !ranBefore;
    }

    private boolean writeToDatabase(String answer) {
        boolean status = false;
        QuestionDataVO currentQuestion = questionsList.get(prevPosition);
        currentQuestion.setAnswer(answer);
        List<QuestionDataVO> answeredQuestions = CheckListActivity.applicantDataVO.getQuestionsAsked();
        boolean questionExists = false;
        for (int i = 0; i < answeredQuestions.size(); i++) {
            if (currentQuestion.getQuestionID().equals(answeredQuestions.get(i).getQuestionID())) {
                questionExists = true;
                answeredQuestions.get(i).setAnswer(answer);
            }
        }
        if (!questionExists) {
            answeredQuestions.add(currentQuestion);
        }
        CheckListActivity.applicantDataVO.setQuestionsAsked(answeredQuestions);
        if (getActivity() != null) {
            CheckListActivity activity = (CheckListActivity) getActivity();
            status = activity.saveStuffToServer(true, false);
        }
//         SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
//         SharedPreferences.Editor editor = pref.edit();
//         Gson gson = new Gson();
//
//
//         try {
//             switch (MainActivity.currentViewPagerPosition) {
//                 case 0:
//                     pendingTasksList.set(CheckListActivity.currentPosition, CheckListActivity.applicantDataVO);
//                     // convert your list to json
//                     String jsonPendingList = gson.toJson(pendingTasksList);
//                     editor.putString("pendingList",jsonPendingList);
//                     editor.apply();
//                     break;
//                 case 1:
//                     issuesTaskList.set(CheckListActivity.currentPosition, CheckListActivity.applicantDataVO);
//                     String jsonIssuesList = gson.toJson(pendingTasksList);
//                     editor.putString("issuesList",jsonIssuesList);
//                     editor.apply();
//                     break;
//                 case 2:
//                     completedTasksList.set(CheckListActivity.currentPosition, CheckListActivity.applicantDataVO);
//                     String jsonCompletedList = gson.toJson(pendingTasksList);
//                     editor.putString("completedList",jsonCompletedList);
//                     editor.apply();
//                     break;
//             }
//         }catch (IndexOutOfBoundsException e)
//         {
//             e.printStackTrace();
//         }
        return status;
    }


}