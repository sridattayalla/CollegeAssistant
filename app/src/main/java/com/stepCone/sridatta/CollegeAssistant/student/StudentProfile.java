package com.stepCone.sridatta.CollegeAssistant.student;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.sdsmdg.tastytoast.TastyToast;
import com.stepCone.sridatta.CollegeAssistant.R;
import com.stepCone.sridatta.CollegeAssistant.Validation;
import com.stepCone.sridatta.CollegeAssistant.faculty.FacultyProfile;
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
import java.util.StringTokenizer;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentProfile extends AppCompatActivity {
    TextView jntuno, name, changePassword, loading;
    Bitmap bitmap;
    CircleImageView studentImage;
    Button save, cancel;
    String password;
    EditText oldPassword, newPassword, confirmPassword;
    AVLoadingIndicatorView loader;
    CardView card;
    android.support.v7.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        //internet exception
        loader=findViewById(R.id.student_profile_submission_loader);
        loader.hide();
        loading=findViewById(R.id.student_profile_loading_text);
        card=findViewById(R.id.student_profile_card);
        //tool bar
        toolbar=findViewById(R.id.student_profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        save=findViewById(R.id.save_student_details);
        cancel=findViewById(R.id.student_profile_cancel);
        oldPassword=findViewById(R.id.profile_student_old_password);
        newPassword=findViewById(R.id.profile_student_password);
        confirmPassword=findViewById(R.id.profile_student_confirm_password);
        //choose subjects
        TextView chooseSubjects=findViewById(R.id.my_subjects);
        chooseSubjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StudentProfile.this,studentSubjectList.class);
                startActivity(intent);
            }
        });
        //shared preference
        SharedPreferences imagePreference= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String imageString=imagePreference.getString("studentImage",null);
        //setting profile
        jntuno=findViewById(R.id.profile_student_jntuno);
        jntuno.setText(imagePreference.getString("id",null));
        name=findViewById(R.id.profile_student_name);
        name.setText(imagePreference.getString("studentName",null));
        //setting image
        studentImage=findViewById(R.id.profile_student_image);
        byte [] encodeByte= Base64.decode(imageString,Base64.DEFAULT);
        bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        studentImage.setImageBitmap(bitmap);
        //handling image on click
        //change password
        changePassword=findViewById(R.id.student_change_password);
        changePassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                cancel.setVisibility(View.VISIBLE);
                changePassword.setVisibility(View.GONE);
                oldPassword.setVisibility(View.VISIBLE);
                newPassword.setVisibility(View.VISIBLE);
                confirmPassword.setVisibility(View.VISIBLE);
                save.setVisibility(View.VISIBLE);
            }
        });

        //cancel
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel.setVisibility(View.GONE);
                oldPassword.setText("");
                newPassword.setText("");
                confirmPassword.setText("");
                changePassword.setVisibility(View.VISIBLE);
                oldPassword.setVisibility(View.GONE);
                newPassword.setVisibility(View.GONE);
                confirmPassword.setVisibility(View.GONE);
                save.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
            }
        });

        //save
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkInternetConnection()){
                    TastyToast.makeText(getApplicationContext(),"Check Internet Connection",Toast.LENGTH_SHORT,TastyToast.CONFUSING).show();
                }
                else{
                    if(validated()){
                        if(newPassword.getText().toString().equals(confirmPassword.getText().toString())){
                            new StudentProfile.getPassword().execute();
                            card.setVisibility(View.GONE);
                            loader.show();
                            loading.setVisibility(View.VISIBLE);
                        }
                        else{ TastyToast.makeText(getApplicationContext(),"Password Not Matched",Toast.LENGTH_SHORT,TastyToast.WARNING).show();}}

                }

            }
        });
    }

    class getPassword extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {
            //getting sections
            try{URL url=new URL("http://fullstackdevelopment.thecompletewebhosting.com/college_assistant/get_old_password.php");
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(10000);
                conn.connect();
                OutputStream os=conn.getOutputStream();
                BufferedWriter br=new BufferedWriter(new OutputStreamWriter(os));
                SharedPreferences imagePreference= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String id=imagePreference.getString("id",null);
                br.write(URLEncoder.encode("roll_no","UTF-8")+"="+URLEncoder.encode(id,"UTF-8"));
                br.flush();
                br.close();
                os.close();
                //getting sections list
                InputStream is=conn.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
                String password=bufferedReader.readLine();
                return password;
            }
            catch (Exception e){}
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
           if(s.equals(oldPassword.getText().toString())){new setPassword().execute();}
            else{
               TastyToast.makeText(getApplicationContext(),"Incorrect Password",Toast.LENGTH_SHORT,TastyToast.WARNING).show();}
        }
    }

    class setPassword extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            try{URL url=new URL("http://fullstackdevelopment.thecompletewebhosting.com/college_assistant/change_password.php");
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(10000);
                conn.connect();
                OutputStream os=conn.getOutputStream();
                BufferedWriter br=new BufferedWriter(new OutputStreamWriter(os));
                SharedPreferences imagePreference= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String id=imagePreference.getString("id",null);
                br.write(URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(newPassword.getText().toString(),"UTF-8")+ "&" + URLEncoder.encode("roll_no", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8"));
                br.flush();
                br.close();
                os.close();
                //getting sections list
                InputStream is=conn.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
                String password=bufferedReader.readLine();
                return password;
            }
            catch (Exception e){}
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            oldPassword.setText("");
            newPassword.setText("");
            confirmPassword.setText("");
            changePassword.setVisibility(View.VISIBLE);
            oldPassword.setVisibility(View.GONE);
            newPassword.setVisibility(View.GONE);
            confirmPassword.setVisibility(View.GONE);
            save.setVisibility(View.GONE);
            cancel.setVisibility(View.INVISIBLE);
            card.setVisibility(View.VISIBLE);
            loader.hide();
            loading.setVisibility(View.GONE);
            TastyToast.makeText(getApplicationContext(),"Password Changed",Toast.LENGTH_SHORT,TastyToast.SUCCESS).show();
        }
    }

    public  boolean checkInternetConnection(){
        ConnectivityManager cm =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=cm.getActiveNetworkInfo();
        return info!=null && info.isConnectedOrConnecting();
    }

    //validate
    public boolean validated() {
        String error;
        EditText emp_id,emp_password;
        emp_password = (EditText)findViewById(R.id.profile_student_password);

        String password = emp_password.getText().toString();
        Validation validate=new Validation();

        if(!(error=validate.passwordValidation(password)).equals("true")){
            emp_password.setError(error);
            return false;
        }
        return true;
    }
}
