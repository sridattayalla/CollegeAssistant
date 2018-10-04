package com.stepCone.sridatta.CollegeAssistant.faculty;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.StringTokenizer;

public class postMarks extends AppCompatActivity {

    ArrayList<String> HandlingSectionsList;
    ArrayList<String> HandlingSubjectsList;
    Button chooseSection,retry;
    String perId;
    Context context;
    String section_name;
    String subject;
    Spinner postMarksSpinner;
    CardView cardView;
    TextView textView;
    AVLoadingIndicatorView loader;
    ImageView networkImage;
    Toolbar tb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_marks);

        //toolbar
        tb=findViewById(R.id.toolbar3);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //card
        cardView=findViewById(R.id.post_marks_card);

        //network
        networkImage=findViewById(R.id.postmarks_no_internet_image);
        retry=findViewById(R.id.postmarks_retry);
        loader=findViewById(R.id.postmarks_loader);
        loader.show();
        textView=findViewById(R.id.textView24);

        //perId
        SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(this);
        perId=sf.getString("id",null);

        //spinner
        postMarksSpinner=findViewById(R.id.post_marks_spinner);
        if(!checkInternetConnection()){
            networkImage.setVisibility(View.VISIBLE);
            retry.setVisibility(View.VISIBLE);
            postMarksSpinner.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            loader.hide();
        }
        else{
        new getHandlingSectionsList().execute();}

        //retry
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loader.show();
                finish();
                startActivity(getIntent());
            }
        });

        //image buttons
        ImageButton mid1=findViewById(R.id.mid1);
        mid1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(postMarks.this,postMArksToSection.class);
                intent.putExtra("subject",subject);
                intent.putExtra("exam","mid1");
                intent.putExtra("section",section_name);
                startActivity(intent);
            }
        });
        ImageButton mid2=findViewById(R.id.mid2);
        mid2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(postMarks.this,postMArksToSection.class);
                intent.putExtra("subject",subject);
                intent.putExtra("exam","mid2");
                intent.putExtra("section",section_name);
                startActivity(intent);
            }
        });
        ImageButton mid3=findViewById(R.id.mid3);
        mid3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(postMarks.this,postMArksToSection.class);
                intent.putExtra("subject",subject);
                intent.putExtra("exam","sem");
                intent.putExtra("section",section_name);
                startActivity(intent);
            }
        });
        //on item slected
        postMarksSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subject=HandlingSubjectsList.get(i);
                section_name=postMarksSpinner.getSelectedItem().toString();
                loader.hide();
                cardView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        HandlingSectionsList=new ArrayList<String>();
        HandlingSubjectsList=new ArrayList<String>();




        //new getHandlingSectionsList().execute();
        try {
            if(!checkInternetConnection()){

                chooseSection.setVisibility(View.INVISIBLE);
            }
            else{}
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //method to check internet connection
    public  boolean checkInternetConnection(){
        ConnectivityManager cm =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=cm.getActiveNetworkInfo();
        return info!=null && info.isConnectedOrConnecting();
    }


    class getHandlingSectionsList extends AsyncTask<Void,Void,ArrayList<String> > {
        ProgressDialog pd=new ProgressDialog(getApplicationContext());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            postMarksSpinner.setVisibility(View.INVISIBLE);
        }

        @Override
        protected ArrayList doInBackground(Void... voids) {

            //getting sections
            try{URL url=new URL("http://fullstackdevelopment.thecompletewebhosting.com/college_assistant/check.php");
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(10000);
                conn.connect();
                OutputStream os=conn.getOutputStream();
                BufferedWriter br=new BufferedWriter(new OutputStreamWriter(os));
                br.write(URLEncoder.encode("perId","UTF-8")+"="+URLEncoder.encode(perId,"UTF-8"));
                br.flush();
                br.close();
                os.close();
                //getting sections list
                InputStream is=conn.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
                String s=bufferedReader.readLine();
                String mydelim = ",";
                StringTokenizer stringTokenizer =
                        new StringTokenizer(s, mydelim);
                int count = stringTokenizer.countTokens();
                System.out.println("Number of tokens : " + count + "\n");
                for (int i = 0; i <count; i++){
                    HandlingSectionsList.add(stringTokenizer.nextToken());
                }

                //while (sub!=null){HandlingSectionsList.add(s); sub=bufferedReader.readLine();}
            }
            catch (Exception e){HandlingSectionsList.add("catch");}

            //getting subjects

            try{URL url=new URL("http://fullstackdevelopment.thecompletewebhosting.com/college_assistant/sub_check.php");
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(10000);
                conn.connect();
                OutputStream os=conn.getOutputStream();
                BufferedWriter br=new BufferedWriter(new OutputStreamWriter(os));
                br.write(URLEncoder.encode("perId","UTF-8")+"="+URLEncoder.encode(perId,"UTF-8"));
                br.flush();
                br.close();
                os.close();
                //getting sections list
                InputStream is=conn.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
                String s=bufferedReader.readLine();
                String mydelim = ",";
                StringTokenizer stringTokenizer =
                        new StringTokenizer(s, mydelim);
                int count = stringTokenizer.countTokens();
                for (int i = 0; i <count; i++){
                    HandlingSubjectsList.add(stringTokenizer.nextToken());
                }

                //while (sub!=null){HandlingSectionsList.add(s); sub=bufferedReader.readLine();}
            }
            catch (Exception e){HandlingSubjectsList.add("catch");}

            return HandlingSubjectsList;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
            // convert to simple array
            //chooseSection.setVisibility(View.VISIBLE);
            postMarksSpinner.setVisibility(View.VISIBLE);

            //
            //setting spinner adapter
            ArrayAdapter<String> items = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, HandlingSectionsList);
            items.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //setting adapter
            postMarksSpinner.setAdapter(items);
        }
    }

}

