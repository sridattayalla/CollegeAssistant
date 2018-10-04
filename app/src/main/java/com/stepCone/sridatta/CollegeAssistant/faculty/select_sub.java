package com.stepCone.sridatta.CollegeAssistant.faculty;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.stepCone.sridatta.CollegeAssistant.R;

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

public class select_sub extends AppCompatActivity {
    EditText section,subject;
    String subString,OrignalSubString,sectionString;
    String perId;
    int subcount;
    List HandlingSectionsList=new ArrayList();
    Boolean isThere;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sub);
        section=findViewById(R.id.add_sub_sec);
        subject=findViewById(R.id.add_sub_sub);
        add=findViewById(R.id.save_sub);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new getSub().execute();
            }
        });


    }

    class getSub extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {

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
                sectionString=s+","+section.getText().toString();

                //while (sub!=null){HandlingSectionsList.add(s); sub=bufferedReader.readLine();}
            }
            catch (Exception e){HandlingSectionsList.add("catch");}

            //getting sections
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
                //perId
                SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(getApplication());
                perId=sf.getString("id",null);
                br.write(URLEncoder.encode("perId","UTF-8")+"="+URLEncoder.encode(perId,"UTF-8"));
                br.flush();
                br.close();
                os.close();
                //getting sections list
                InputStream is=conn.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
                subString=bufferedReader.readLine();
                String mydelim = ",";
                StringTokenizer stringTokenizer =
                        new StringTokenizer(subString, mydelim);
                subcount = stringTokenizer.countTokens();
                for (int i = 0; i <subcount; i++){
                    HandlingSectionsList.add(stringTokenizer.nextToken());
                }
                subString=subString+",sub"+Integer.toString(subcount+1);

                }
                catch (Exception e){}
                return null;

                //while (sub!=null){HandlingSectionsList.add(s); sub=bufferedReader.readLine();}
            }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new addsub().execute();
        }
    }

    class postsub extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            //adding subjects
            try{URL url=new URL("http://fullstackdevelopment.thecompletewebhosting.com/college_assistant/add_subjects.php");
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(10000);
                conn.connect();
                OutputStream os=conn.getOutputStream();
                BufferedWriter br=new BufferedWriter(new OutputStreamWriter(os));
                //perId
                SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(getApplication());
                perId=sf.getString("id",null);
                br.write(URLEncoder.encode("roll_no","UTF-8")+"="+URLEncoder.encode(perId,"UTF-8")+"&"+URLEncoder.encode("original_sub","UTF-8")+"="+URLEncoder.encode(OrignalSubString,"UTF-8")+"&"+URLEncoder.encode("subjects","UTF-8")+"="+URLEncoder.encode(subString,"UTF-8")+"&"+URLEncoder.encode("section_list","UTF-8")+"="+URLEncoder.encode(sectionString,"UTF-8"));
                br.flush();
                br.close();
                os.close();
                //getting sections list
                InputStream is=conn.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
                subString=bufferedReader.readLine();
                return subString;
            }
            catch (Exception e){}
            return "0";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TastyToast.makeText(getApplicationContext(),"Added", Toast.LENGTH_SHORT,TastyToast.SUCCESS).show();
        }
    }

    class addsub extends AsyncTask<String, String, List> {
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
                OrignalSubString=s+","+subject.getText().toString();

                return null;
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(List list) {
            super.onPostExecute(list);
            new postsub().execute();
        }
    }
}
