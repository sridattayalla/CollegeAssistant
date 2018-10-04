package com.stepCone.sridatta.CollegeAssistant.student;

import android.app.ProgressDialog;
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
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.messaging.FirebaseMessaging;
import com.sdsmdg.tastytoast.TastyToast;
import com.stepCone.sridatta.CollegeAssistant.R;
import com.stepCone.sridatta.CollegeAssistant.studentHome;

import net.igenius.customcheckbox.CustomCheckBox;

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
import java.util.concurrent.ExecutionException;

/**
 * Created by HP-PC on 26-01-2018.
 */

public class studentSubjectList extends AppCompatActivity {
    List<String> subjects;
    String subjectString;
    CustomCheckBox cb1,cb2,cb3,cb4,cb5,cb6;
    TextView tv1,tv2,tv3,tv4,tv5,tv6;
    android.support.v7.widget.Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_subjects);
        //tool bar
        toolbar=findViewById(R.id.subjects_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //check boxes
        cb1=findViewById(R.id.sub1_checkbox);
        cb2=findViewById(R.id.sub2_checkbox);
        cb3=findViewById(R.id.sub3_checkbox);
        cb4=findViewById(R.id.sub4_checkbox);
        cb5=findViewById(R.id.sub5_checkbox);
        cb6=findViewById(R.id.sub6_checkbox);
        //text views
        tv1=findViewById(R.id.sub1_text);
        tv2=findViewById(R.id.sub2_text);
        tv3=findViewById(R.id.sub3_text);
        tv4=findViewById(R.id.sub4_text);
        tv5=findViewById(R.id.sub5_text);
        tv6=findViewById(R.id.sub6_text);

        //save button
        Button saveButton=findViewById(R.id.save_subjects);
        saveButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                if(!checkInternetConnection()){
                    TastyToast.makeText(getApplicationContext(),"Check Internet Connection",Toast.LENGTH_SHORT,TastyToast.CONFUSING).show();}

               else {
                    subjects = new ArrayList<>();
                    subjects.clear();
                    if (cb1.isChecked()) {
                        subjects.add(tv1.getText().toString());
                        FirebaseMessaging.getInstance().subscribeToTopic(tv1.getText().toString());
                    } else {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(tv1.getText().toString());
                    }

                    if (cb2.isChecked()) {
                        subjects.add(tv2.getText().toString());
                        FirebaseMessaging.getInstance().subscribeToTopic(tv2.getText().toString());
                    } else {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(tv2.getText().toString());
                    }

                    if (cb3.isChecked()) {
                        subjects.add(tv3.getText().toString());
                        FirebaseMessaging.getInstance().subscribeToTopic(tv3.getText().toString());
                    } else {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(tv3.getText().toString());
                    }

                    if (cb4.isChecked()) {
                        subjects.add(tv4.getText().toString());
                        FirebaseMessaging.getInstance().subscribeToTopic(tv4.getText().toString());
                    } else {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(tv4.getText().toString());
                    }

                    if (cb5.isChecked()) {
                        subjects.add(tv5.getText().toString());
                        FirebaseMessaging.getInstance().subscribeToTopic(tv5.getText().toString());
                    } else {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(tv5.getText().toString());
                    }

                    if (cb6.isChecked()) {
                        subjects.add(tv6.getText().toString());
                        FirebaseMessaging.getInstance().subscribeToTopic(tv6.getText().toString());
                    } else {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(tv6.getText().toString());
                    }

                    //subject string
                    subjectString = null;
                    int num = subjects.size();
                    try {
                        subjectString = subjects.get(0).toString();
                        for (int i = 0; i < num; i++) {
                            subjectString = subjectString + "," + subjects.get(i);
                            new sendSubjects().execute();
                            onBackPressed();
                        }
                    }
                    catch (IndexOutOfBoundsException e){
                        TastyToast.makeText(getApplicationContext(),"Select Atleast One Subject",Toast.LENGTH_SHORT,TastyToast.DEFAULT).show();
                    }




                }
            }
        });
    }



    class sendSubjects extends AsyncTask<String,String,String>{
        ProgressDialog pd=new ProgressDialog(getApplicationContext());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pd.setMessage("Loading");
//            pd.setCancelable(false);
//            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url=new URL("http://fullstackdevelopment.thecompletewebhosting.com/college_assistant/update_subjects.php");
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(10000);
                conn.connect();
                OutputStream os=conn.getOutputStream();
                BufferedWriter br=new BufferedWriter(new OutputStreamWriter(os));
                SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                String jntuno=sf.getString("id","opps");
                br.write(URLEncoder.encode("roll_no","UTF-8")+"="+URLEncoder.encode(jntuno,"UTF-8")+"&"+URLEncoder.encode("subjects","UTF-8")+"="+URLEncoder.encode(subjectString,"UTF-8"));
                br.flush();
                br.close();
                os.close();
                InputStream is=conn.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
                String line=bufferedReader.readLine();
                return line;
            }
            catch (Exception e){}
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            pd.dismiss();
            TastyToast.makeText(getBaseContext(),"Successful",Toast.LENGTH_SHORT,TastyToast.SUCCESS).show();
        }
    }

    // checking internet connection

    public  boolean checkInternetConnection(){
        ConnectivityManager cm =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=cm.getActiveNetworkInfo();
        return info!=null && info.isConnectedOrConnecting();
    }
}
