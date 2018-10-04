package com.stepCone.sridatta.CollegeAssistant.faculty;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.stepCone.sridatta.CollegeAssistant.FacultyHome;
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
import java.util.StringTokenizer;

/**
 * Created by HP-PC on 13-01-2018.
 */

public class questionActivity extends AppCompatActivity {
    List<questionFormat> quizList = new ArrayList<>();
    List<String> list = new ArrayList<>();
    int i = 0;
    int count = 0;
    int questionSize = 0;
    Spinner subjectSpinner;
    String selectedSubject;
    AVLoadingIndicatorView loader,populateLoader;
    TextView loading;
    ImageView no_internet_image;
    Button retry;
    TextView textView11,TextView12;
    CardView cardView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_maker);
        textView11=findViewById(R.id.textView11);
        TextView12=findViewById(R.id.textView12);
        //card View
        cardView=findViewById(R.id.post_question_card);
        cardView.setVisibility(View.GONE);
        //internet error
        no_internet_image=findViewById(R.id.question_post_no_internet_image);
        loading=findViewById(R.id.submitting);
        loader=findViewById(R.id.question_post_loader);
        populateLoader=findViewById(R.id.question_post_loader2);
        populateLoader.show();
        retry=findViewById(R.id.question_post_retry);
        //toolbar
        Toolbar tb = findViewById(R.id.question_toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setTitle("Question Maker");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getting ui elements
        final CheckBox checkBox1 = findViewById(R.id.faculty_checkbox1);
        final CheckBox checkBox2 = findViewById(R.id.faculty_checkbox2);
        final CheckBox checkBox3 = findViewById(R.id.faculty_checkbox3);
        final CheckBox checkBox4 = findViewById(R.id.faculty_checkbox4);
        final EditText faculty_question = findViewById(R.id.faculty_question);
        final EditText faculty_option1 = findViewById(R.id.faculty_option_one);
        final EditText faculty_option2 = findViewById(R.id.faculty_option_two);
        final EditText faculty_option3 = findViewById(R.id.faculty_option_three);
        final EditText faculty_option4 = findViewById(R.id.faculty_option_four);
        subjectSpinner = findViewById(R.id.question_spinner);

        //populate spinner
        if(!checkInternetConnection()){
            populateLoader.hide();
            cardView.setVisibility(View.GONE);
            subjectSpinner.setVisibility(View.GONE);
            retry.setVisibility(View.VISIBLE);
            no_internet_image.setVisibility(View.VISIBLE);
        }
        else{new populateSpinner().execute();}

        //retry
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent());
            }
        });


        //subject spinner on click
        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSubject = subjectSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //submit button
        final Button submit = findViewById(R.id.quiz_submit);
        //next button
        final Button b = findViewById(R.id.next);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 0;
                if (checkBox1.isChecked() == true) {
                    i = i * 10 + 1;
                }
                if (checkBox2.isChecked() == true) {
                    i = i * 10 + 2;
                }
                if (checkBox3.isChecked() == true) {
                    i = i * 10 + 3;
                }
                if (checkBox4.isChecked() == true) {
                    i = i * 10 + 4;
                }

                if(faculty_question.getText().toString()==null||faculty_question.getText().toString().equals("")){
                    TastyToast.makeText(getApplicationContext(),"Enter Question",Toast.LENGTH_SHORT,TastyToast.WARNING).show();
                }
               else if(faculty_option1.getText().toString()==null||faculty_option1.getText().toString().equals("")){
                    TastyToast.makeText(getApplicationContext(),"Enter Option1",Toast.LENGTH_SHORT,TastyToast.WARNING).show();
                }

                else if(faculty_option2.getText().toString()==null||faculty_option2.getText().toString().equals("")){
                    TastyToast.makeText(getApplicationContext(),"Enter Option2",Toast.LENGTH_SHORT,TastyToast.WARNING).show();
                }

                else if(faculty_option3.getText().toString()==null||faculty_option3.getText().toString().equals("")){
                    TastyToast.makeText(getApplicationContext(),"Enter Option3",Toast.LENGTH_SHORT,TastyToast.WARNING).show();
                }
                else if(faculty_option4.getText().toString()==null||faculty_option4.getText().toString().equals("")){
                    TastyToast.makeText(getApplicationContext(),"Enter Option4",Toast.LENGTH_SHORT,TastyToast.WARNING).show();
                }

               else if(i==0){
                    TastyToast.makeText(getApplicationContext(),"Choose An Option",Toast.LENGTH_SHORT,TastyToast.INFO).show();}
                else if(i/10!=0){TastyToast.makeText(getApplicationContext(),"Choose Only One Option",Toast.LENGTH_SHORT,TastyToast.INFO).show();}
              else {
                    questionFormat qf = new questionFormat(faculty_question.getText().toString(), faculty_option1.getText().toString(), faculty_option2.getText().toString(), faculty_option3.getText().toString(), faculty_option4.getText().toString(), i);
                    //Toast.makeText(getApplicationContext(), Integer.toString(i), Toast.LENGTH_SHORT).show();
                    quizList.add(qf);
                    count++;
                    //setting visibility
                    if (count == 5) {
                        submit.setVisibility(View.VISIBLE);
                        cardView.setVisibility(View.GONE);
                    }
                    //changing ui state
                    if(checkBox1.isChecked()){checkBox1.toggle();}
                    if(checkBox2.isChecked()){checkBox2.toggle();}
                    if(checkBox3.isChecked()){checkBox3.toggle();}
                    if(checkBox4.isChecked()){checkBox4.toggle();}
                    faculty_question.setText(null);
                    faculty_option1.setText(null);
                    faculty_option2.setText(null);
                    faculty_option3.setText(null);
                    faculty_option4.setText(null);
                }

            }
        });

        //submit button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedSubject == null) {
                    TastyToast.makeText(getApplicationContext(), "Select a Subject", Toast.LENGTH_LONG,TastyToast.INFO).show();
                } else {
                    if(!checkInternetConnection()){TastyToast.makeText(getApplicationContext(), "Check Internet connection", Toast.LENGTH_LONG,TastyToast.CONFUSING).show();}
                   else{
                        submit.setVisibility(View.GONE);
                        loader.show();
                        loading.setVisibility(View.VISIBLE);
                        new getPracticeQuestionSize().execute();}
                }
            }
        });
    }

    /*class dropTable extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            subjectSpinner.setVisibility(View.INVISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL("http://fullstackdevelopment.thecompletewebhosting.com/college_assistant/delete_que.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(10000);
                conn.connect();
                OutputStream os = conn.getOutputStream();
                BufferedWriter br = new BufferedWriter(new OutputStreamWriter(os));
                br.write(URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(selectedSubject, "UTF-8"));
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
            new updateQuiz().execute();
        }
    }*/


   /* class updateQuiz extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            for (int i = 0; i < quizList.size(); i++) {
                String question = quizList.get(i).getQuestion();
                String choice1 = quizList.get(i).getOption1();
                String choice2 = quizList.get(i).getOption2();
                String choice3 = quizList.get(i).getOption3();
                String choice4 = quizList.get(i).getOption4();
                String answer = Integer.toString(quizList.get(i).getAnswer());
                try {
                    URL url = new URL("http://fullstackdevelopment.thecompletewebhosting.com/college_assistant/take_quiz.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(10000);
                    conn.connect();
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter br = new BufferedWriter(new OutputStreamWriter(os));
                    br.write(URLEncoder.encode("sno", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(i), "UTF-8") + "&" + URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(selectedSubject, "UTF-8") + "&" + URLEncoder.encode("question", "UTF-8") + "=" + URLEncoder.encode(question, "UTF-8") + "&" + URLEncoder.encode("choice1", "UTF-8") + "=" + URLEncoder.encode(choice1, "UTF-8") + "&" + URLEncoder.encode("choice2", "UTF-8") + "=" + URLEncoder.encode(choice2, "UTF-8") + "&" + URLEncoder.encode("choice3", "UTF-8") + "=" + URLEncoder.encode(choice3, "UTF-8") + "&" + URLEncoder.encode("choice4", "UTF-8") + "=" + URLEncoder.encode(choice4, "UTF-8") + "&" + URLEncoder.encode("answer", "UTF-8") + "=" + URLEncoder.encode(answer, "UTF-8"));
                    br.flush();
                    br.close();
                    os.close();
                    InputStream is = conn.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                    String line = bufferedReader.readLine();
                } catch (Exception e) {
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new getPracticeQuestionSize().execute();
        }
    }*/


    class getPracticeQuestionSize extends AsyncTask<String, String, String> {
        public getPracticeQuestionSize() {
            super();
            subjectSpinner.setVisibility(View.INVISIBLE);
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
                br.write(URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(selectedSubject, "UTF-8"));
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
            //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            questionSize = Integer.parseInt(s);
            new updatePracticeQuestons().execute();
        }
    }

    class updatePracticeQuestons extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            for (int i =1; i < quizList.size()+1; i++) {
                int currentnum=i-1;
                String question = quizList.get(currentnum).getQuestion();
                String choice1 = quizList.get(currentnum).getOption1();
                String choice2 = quizList.get(currentnum).getOption2();
                String choice3 = quizList.get(currentnum).getOption3();
                String choice4 = quizList.get(currentnum).getOption4();
                String answer = Integer.toString(quizList.get(currentnum).getAnswer());

                    try {
                        URL url = new URL("http://fullstackdevelopment.thecompletewebhosting.com/college_assistant/multicast.php");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("POST");
                        conn.setDoOutput(true);
                        conn.setDoInput(true);
                        conn.setReadTimeout(10000);
                        conn.setConnectTimeout(10000);
                        conn.connect();
                        OutputStream os = conn.getOutputStream();
                        BufferedWriter br = new BufferedWriter(new OutputStreamWriter(os));
                        br.write(URLEncoder.encode("sno", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(i + questionSize), "UTF-8") + "&" + URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(selectedSubject, "UTF-8") + "&" + URLEncoder.encode("question", "UTF-8") + "=" + URLEncoder.encode(question, "UTF-8") + "&" + URLEncoder.encode("choice1", "UTF-8") + "=" + URLEncoder.encode(choice1, "UTF-8") + "&" + URLEncoder.encode("choice2", "UTF-8") + "=" + URLEncoder.encode(choice2, "UTF-8") + "&" + URLEncoder.encode("choice3", "UTF-8") + "=" + URLEncoder.encode(choice3, "UTF-8") + "&" + URLEncoder.encode("choice4", "UTF-8") + "=" + URLEncoder.encode(choice4, "UTF-8") + "&" + URLEncoder.encode("answer", "UTF-8") + "=" + URLEncoder.encode(answer, "UTF-8"));
                        br.flush();
                        br.close();
                        os.close();
                        InputStream is = conn.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                        String line = bufferedReader.readLine();
                    } catch (Exception e) {
                    }
                }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            subjectSpinner.setVisibility(View.VISIBLE);
            loader.hide();
            loading.setVisibility(View.GONE);
            TastyToast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT,TastyToast.SUCCESS).show();
            Intent intent=new Intent(questionActivity.this, FacultyHome.class);
            startActivity(intent);
                   // if(s.equalsIgnoreCase("No Field sould be Empty")){Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();}
        }
    }

    class populateSpinner extends AsyncTask<String, String, List> {
        @Override
        protected List doInBackground(String... strings) {
            try {
                URL url = new URL("http://fullstackdevelopment.thecompletewebhosting.com/college_assistant/get_subjects.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(10000);
                conn.connect();
                OutputStream os = conn.getOutputStream();
                BufferedWriter br = new BufferedWriter(new OutputStreamWriter(os));
                SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                String jntuno = sf.getString("id", "opps");
                br.write(URLEncoder.encode("roll_no", "UTF-8") + "=" + URLEncoder.encode(jntuno, "UTF-8"));
                br.flush();
                br.close();
                os.close();
                InputStream is = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                String s = bufferedReader.readLine();
                String mydelim = ",";
                StringTokenizer stringTokenizer =
                        new StringTokenizer(s, mydelim);
                int count = stringTokenizer.countTokens();
                System.out.println("Number of tokens : " + count + "\n");
                for (int i = 0; i < count; i++) {
                    list.add(stringTokenizer.nextToken());
                }

                return null;
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(List s) {
            super.onPostExecute(s);
            //setting spinner adapter
            ArrayAdapter<String> items = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, list);
            items.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //setting adapter
            subjectSpinner.setAdapter(items);
            populateLoader.hide();
            cardView.setVisibility(View.VISIBLE);
        /* ArrayAdapter<String> items=new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_list_item_1,s);
         items.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         //setting adapter
         //setting adapter
         subjectSpinner.setAdapter(items);*/

        }
    }

    public  boolean checkInternetConnection(){
        ConnectivityManager cm =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=cm.getActiveNetworkInfo();
        return info!=null && info.isConnectedOrConnecting();
    }
}
