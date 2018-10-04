package com.stepCone.sridatta.CollegeAssistant.student;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.stepCone.sridatta.CollegeAssistant.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * Created by HP-PC on 27-01-2018.
 */

public class student_practice_quiz extends Fragment {
    studentAttendenceActivity.student_attedenceInterface bh;
    CardView quizcard;
    View v;
    int i;
    int questionCount=0;
    int score;
    Activity context;
    Button next;
    TextView question;
    CheckBox choice1;
    CheckBox choice2;
    CheckBox choice3;
    CheckBox choice4;
    String currentAnswer;
    String subject;
    Spinner subjectSpinner;
    int questionSize;
    int marks=0;
    TextView correctAnswers;
    TextView currentQuestionNum;
    int Max,min=2;
    Random randomnum=new Random();
    MediaPlayer wrongMedia,correctMedia;
    ImageView no_internet_image;
    Button retry;
    AVLoadingIndicatorView loadingIndicator;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context=getActivity();
        //changing toolbar name
        Toolbar toolbar = (Toolbar) context.findViewById(R.id.student_home_tool_bar);
        toolbar.setTitle("Practice Quiz");
        v=inflater.inflate(R.layout.practice_quiz,container,false);
        //card
        quizcard=v.findViewById(R.id.practice_quiz_card);
        //sounds
        correctMedia= MediaPlayer.create(getActivity(),R.raw.correct);
        wrongMedia=MediaPlayer.create(getActivity(),R.raw.wrong);
        //loading
        loadingIndicator=v.findViewById(R.id.student_practicequiz_loader);
        loadingIndicator.show();
        //internet connection
        no_internet_image=v.findViewById(R.id.student_practicequiz_no_internet_image);
        retry=v.findViewById(R.id.student_practicequiz_retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bh.attendencerefresh("practice_quiz");
            }
        });
        question=v.findViewById(R.id.practice_quiz_question);
        choice1=v.findViewById(R.id.practice_quiz_choice1);
        choice2=v.findViewById(R.id.practice_quiz_choice2);
        choice3=v.findViewById(R.id.practice_quiz_choice3);
        choice4=v.findViewById(R.id.practice_quiz_choice4);
        correctAnswers=v.findViewById(R.id.practice_correct_answers);
        currentQuestionNum=v.findViewById(R.id.practice_attempted_questions_int);
        currentQuestionNum.setText("0");
        subjectSpinner=v.findViewById(R.id.practice_quiz_apinner);
        List<String> subjectList=new ArrayList<>();
        subjectList.add("JAVA");subjectList.add("DBMS");subjectList.add("PYTHON");subjectList.add("OS");subjectList.add("GEOLOGY");subjectList.add("APTITUDE");
        //setting spinner adapter
        ArrayAdapter<String> items = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, subjectList);
        items.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //setting adapter
        subjectSpinner.setAdapter(items);
        //setting visibility
        question.setVisibility(View.INVISIBLE);
        choice1.setVisibility(View.INVISIBLE);
        choice2.setVisibility(View.INVISIBLE);
        choice3.setVisibility(View.INVISIBLE);
        choice4.setVisibility(View.INVISIBLE);
        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subject=subjectSpinner.getSelectedItem().toString();
                if(!checkInternetConnection()){
                    no_internet_image.setVisibility(View.VISIBLE);
                    retry.setVisibility(View.VISIBLE);
                    loadingIndicator.hide();
                    quizcard.setVisibility(View.GONE);
                }
                else{
                    quizcard.setVisibility(View.GONE);
                    correctAnswers.setText("0");
                    score=0;
                    currentQuestionNum.setText("0");
                    questionCount=0;
                    new getPracticeQuestionSize().execute();}
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
        next=context.findViewById(R.id.practice_quiz_next);
        next.setVisibility(View.VISIBLE);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                next.setClickable(false);

                i = 0;
                if (choice1.isChecked() == true) {
                    i = i * 10 + 1;
                }
                if (choice2.isChecked() == true) {
                    i = i * 10 + 2;
                }
                if (choice3.isChecked() == true) {
                    i = i * 10 + 3;
                }
                if (choice4.isChecked() == true) {
                    i = i * 10 + 4;
                }
                String answer=Integer.toString(i);
                if(i==0){TastyToast.makeText(getActivity(),"Select an Option",Toast.LENGTH_SHORT,TastyToast.WARNING).show();}
                else {
                    if (answer.equalsIgnoreCase(currentAnswer)) {
                        correctMedia.start();
                        TastyToast.makeText(context, "Correct", Toast.LENGTH_SHORT, TastyToast.SUCCESS).show();
                        score++;
                        String currentScore = Integer.toString(score);
                        correctAnswers.setText(currentScore);
                        currentQuestionNum.setText(Integer.toString(questionCount));
                        if (!checkInternetConnection()) {
                            no_internet_image.setVisibility(View.VISIBLE);
                            retry.setVisibility(View.VISIBLE);
                            loadingIndicator.hide();
                            quizcard.setVisibility(View.GONE);
                            quizcard.setVisibility(View.GONE);
                            loadingIndicator.show();
                        } else {
                            new student_practice_quiz.getQuestion().execute();
                        }
                    } else {
                        wrongMedia.start();
                        TastyToast.makeText(context, "Wrong", Toast.LENGTH_SHORT, TastyToast.INFO).show();
                        currentQuestionNum.setText(Integer.toString(questionCount));
                        if (!checkInternetConnection()) {
                            no_internet_image.setVisibility(View.VISIBLE);
                            retry.setVisibility(View.VISIBLE);
                            loadingIndicator.hide();
                            quizcard.setVisibility(View.GONE);
                            quizcard.setVisibility(View.GONE);
                            loadingIndicator.show();
                        } else {
                            new student_practice_quiz.getQuestion().execute();
                        }
                    }
                    if (choice1.isChecked()) {
                        choice1.toggle();
                    }
                    if (choice2.isChecked()) {
                        choice2.toggle();
                    }
                    if (choice3.isChecked()) {
                        choice3.toggle();
                    }
                    if (choice4.isChecked()) {
                        choice4.toggle();
                    }
                }
            }
        });
    }

    class getPracticeQuestionSize extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://fullstackdevelopment.thecompletewebhosting.com/college_assistant/practice_que.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(10000);
                conn.connect();
                OutputStream os = conn.getOutputStream();
                BufferedWriter br = new BufferedWriter(new OutputStreamWriter(os));
                br.write(URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(subject, "UTF-8"));
                br.flush();
                br.close();
                os.close();
                InputStream is = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                String line = bufferedReader.readLine();
                return line;
            } catch (Exception e) {
            }
            return "0";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            questionSize = Integer.parseInt(s);
            if(!checkInternetConnection()){
                no_internet_image.setVisibility(View.VISIBLE);
                retry.setVisibility(View.VISIBLE);
                loadingIndicator.hide();
                quizcard.setVisibility(View.GONE);
            }
            else{new getQuestion().execute();}
        }
    }

    class getQuestion extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {
           int questionNum=min+randomnum.nextInt(questionSize);
            try {
                URL url = new URL("http://fullstackdevelopment.thecompletewebhosting.com/college_assistant/retrieve_quiz.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(10000);
                conn.connect();
                OutputStream os = conn.getOutputStream();
                BufferedWriter br = new BufferedWriter(new OutputStreamWriter(os));
                br.write(URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(subject, "UTF-8") + "&" +URLEncoder.encode("sno", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(questionNum), "UTF-8"));
                questionCount++;
                br.flush();
                br.close();
                os.close();
                InputStream is = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                String line = bufferedReader.readLine();
                return line;
            } catch (Exception e) {
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(context,s,Toast.LENGTH_LONG).show();

            //setting visibility
            question.setVisibility(View.VISIBLE);
            choice1.setVisibility(View.VISIBLE);
            choice2.setVisibility(View.VISIBLE);
            choice3.setVisibility(View.VISIBLE);
            choice4.setVisibility(View.VISIBLE);
            //setting question and answers
           try{//seperating question and answers
               String mydelim = "~";
               StringTokenizer stringTokenizer =
                       new StringTokenizer(s, mydelim);
               int count = stringTokenizer.countTokens();
               question.setText(stringTokenizer.nextToken());
            choice1.setText(stringTokenizer.nextToken());
            choice2.setText(stringTokenizer.nextToken());
            choice3.setText(stringTokenizer.nextToken());
            choice4.setText(stringTokenizer.nextToken());
            currentAnswer=stringTokenizer.nextToken();
           quizcard.setVisibility(View.VISIBLE);}
            catch (NoSuchElementException e){//Toast.makeText(context,"Try Again",Toast.LENGTH_SHORT).show();
           }
            catch (NullPointerException e){
                no_internet_image.setVisibility(View.VISIBLE);
                retry.setVisibility(View.VISIBLE);
                loadingIndicator.hide();
                quizcard.setVisibility(View.GONE);
                loadingIndicator.show();}
            next.setClickable(true);

        }

    }

    public  boolean checkInternetConnection(){
        ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=cm.getActiveNetworkInfo();
        return info!=null && info.isConnectedOrConnecting();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        bh=(studentAttendenceActivity.student_attedenceInterface) activity;
    }
}
