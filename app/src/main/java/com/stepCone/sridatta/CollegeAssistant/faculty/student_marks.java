package com.stepCone.sridatta.CollegeAssistant.faculty;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.stepCone.sridatta.CollegeAssistant.R;
import com.stepCone.sridatta.CollegeAssistant.student.student_credits;
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

public class student_marks extends AppCompatActivity {
    student_credits.student_marksInterface bh;
    ImageView no_internet_image;
    Button retry;
    AVLoadingIndicatorView loadingIndicator;
    View v;
    List<TextView> m1TextView=new ArrayList<>();
    List<TextView> m2TextView=new ArrayList<>();
    List<TextView> m3TextView=new ArrayList<>();
    List<String> s1markList=new ArrayList<>();
    List<String> s2markList=new ArrayList<>();
    List<String> s3markList=new ArrayList<>();
    CardView card1,card2,card3;
    String jntuno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_marks);
        loadingIndicator=v.findViewById(R.id.student_marks_loader);
        loadingIndicator.show();
        //internet connection
        no_internet_image=v.findViewById(R.id.student_marks_no_internet_image);
        retry=v.findViewById(R.id.student_marks_retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bh.marksrefresh();
            }
        });
        //initiating text views
        m1TextView.add((TextView) v.findViewById(R.id.m1_s1));
        m1TextView.add((TextView) v.findViewById(R.id.m1_s2));
        m1TextView.add((TextView) v.findViewById(R.id.m1_s3));
        m1TextView.add((TextView) v.findViewById(R.id.m1_s4));
        m1TextView.add((TextView) v.findViewById(R.id.m1_s5));
        m1TextView.add((TextView) v.findViewById(R.id.m1_s6));

        m2TextView.add((TextView) v.findViewById(R.id.m2_s6));
        m2TextView.add((TextView) v.findViewById(R.id.m2_s5));
        m2TextView.add((TextView) v.findViewById(R.id.m2_s4));
        m2TextView.add((TextView) v.findViewById(R.id.m2_s3));
        m2TextView.add((TextView) v.findViewById(R.id.m2_s2));
        m2TextView.add((TextView) v.findViewById(R.id.m2_s1));

        m3TextView.add((TextView) v.findViewById(R.id.m3_s6));
        m3TextView.add((TextView) v.findViewById(R.id.m3_s5));
        m3TextView.add((TextView) v.findViewById(R.id.m3_s4));
        m3TextView.add((TextView) v.findViewById(R.id.m3_s3));
        m3TextView.add((TextView) v.findViewById(R.id.m3_s2));
        m3TextView.add((TextView) v.findViewById(R.id.m3_s1));

        //card view
        card1=v.findViewById(R.id.ark_card_one);
        card2=v.findViewById(R.id.ark_card_two);
        card3=v.findViewById(R.id.ark_card_three);


        if(!checkInternetConnection()){no_internet_image.setVisibility(View.VISIBLE); retry.setVisibility(View.VISIBLE);loadingIndicator.hide();}
        else{new getsem1Marks().execute();}

    }

    class getsem1Marks extends AsyncTask<String,String,List> {
        URL url;
        HttpURLConnection conn;
        int READ_TIMEOUT=10000;
        int READ_CONNECTION=10000;
        @Override
        protected List doInBackground(String... strings) {
            try {
                url = new URL("http://fullstackdevelopment.thecompletewebhosting.com/college_assistant/retrieve_marks.php");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(READ_CONNECTION);
                conn.connect();
                OutputStream os = conn.getOutputStream();
                BufferedWriter br = new BufferedWriter(new OutputStreamWriter(os));

                br.write(URLEncoder.encode("roll_no", "UTF-8") + "=" + URLEncoder.encode(jntuno, "UTF-8"));
                br.flush();
                br.close();
                os.close();
                //take
                InputStream is = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                String s = bufferedReader.readLine();
                try{ String mydelim = ",";
                    StringTokenizer stringTokenizer =
                            new StringTokenizer(s, mydelim);
                    int count = stringTokenizer.countTokens();

                    for (int i = 0; i < 6; i++) {
                        s1markList.add(stringTokenizer.nextToken());
                    }
                    for (int i = 0; i < 6; i++) {
                        s2markList.add(stringTokenizer.nextToken());
                    }
                    for (int i = 0; i < 6; i++) {
                        s3markList.add(stringTokenizer.nextToken());
                    }
                }
                catch (Exception e){
                    for (int i = 0; i < 6; i++) {
                        s1markList.add("0");
                    }
                    for (int i = 0; i < 6; i++) {
                        s2markList.add("0");
                    }
                    for (int i = 0; i < 6; i++) {
                        s3markList.add("0 ");
                    }
                }
            }
            catch (Exception e){

            }
            return s1markList;
        }

        @Override
        protected void onPostExecute(List list) {
            super.onPostExecute(list);
            for(int i=0;i<s1markList.size();i++){
                m1TextView.get(i).setText(s1markList.get(i));
            }
            for(int i=0;i<s2markList.size();i++){
                m2TextView.get(i).setText(s2markList.get(i));
            }
            for(int i=0;i<s1markList.size();i++){
                m3TextView.get(i).setText(s3markList.get(i));
            }

            //setting card visibility
            card1.setVisibility(View.VISIBLE);
            card2.setVisibility(View.VISIBLE);
            card3.setVisibility(View.VISIBLE);
            loadingIndicator.hide();
            //Toast.makeText(getActivity(),"complete",Toast.LENGTH_SHORT).show();
        }
    }

    // checking internet connection

    public  boolean checkInternetConnection(){
        ConnectivityManager cm =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=cm.getActiveNetworkInfo();
        return info!=null && info.isConnectedOrConnecting();
    }

}
