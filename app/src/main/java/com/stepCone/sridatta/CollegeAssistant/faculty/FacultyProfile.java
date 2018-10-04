package com.stepCone.sridatta.CollegeAssistant.faculty;

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

import com.sdsmdg.tastytoast.TastyToast;
import com.stepCone.sridatta.CollegeAssistant.R;
import com.stepCone.sridatta.CollegeAssistant.Validation;
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

import butterknife.BindAnim;
import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class FacultyProfile extends AppCompatActivity {
    @BindView(R.id.profile_faculty_jntuno) TextView jntuno;
    TextView name;
    Bitmap bitmap;
    CircleImageView studentImage;
    Button save;
    String password;
    TextView designation;
    EditText oldPassword;
    EditText newPassword;
    EditText confirmPassword;
    TextView changePassword;
    Button cancel;
    AVLoadingIndicatorView loader;
    TextView loading;
    CardView card;
    TextView addSubjects,fetchStudentData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_profile);
        //add sub
        addSubjects=findViewById(R.id.add_subject);
        addSubjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FacultyProfile.this,select_sub.class);
                startActivity(intent);
            }
        });
        //toolbar
        android.support.v7.widget.Toolbar tb=findViewById(R.id.faculty_profile_toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //internet exception
        loader=findViewById(R.id.faculty_profile_submission_loader);
        loader.hide();
        loading=findViewById(R.id.faculty_profile_loading_text);
        card=findViewById(R.id.faculty_profile_card);

        //getting intent
        Bundle extras=getIntent().getExtras();

        cancel=findViewById(R.id.faculty_profile_cancel);
        save=findViewById(R.id.save_faculty_details);
        oldPassword=findViewById(R.id.add_sub_sec);
        newPassword=findViewById(R.id.add_sub_sub);
        confirmPassword=findViewById(R.id.profile_faculty_confirm_password);

        //shared preference
        SharedPreferences imagePreference= PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String imageString=imagePreference.getString("facultyImage",null);
        //setting profile
        designation=findViewById(R.id.faculty_designation);
        designation.setText(extras.getString("designation"));
        jntuno=findViewById(R.id.profile_faculty_jntuno);
        jntuno.setText(imagePreference.getString("id",null));
        name=findViewById(R.id.profile_faculty_name);
        name.setText(extras.getString("name"));
        //name.setText(imagePreference.getString("facultyName",null));
        //setting image
        studentImage=findViewById(R.id.profile_faculty_image);
        byte [] encodeByte= Base64.decode(imageString,Base64.DEFAULT);
        bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        studentImage.setImageBitmap(bitmap);
        //cancel
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldPassword.setText("");
                newPassword.setText("");
                confirmPassword.setText("");
                cancel.setVisibility(View.GONE);
                changePassword.setVisibility(View.VISIBLE);
                oldPassword.setVisibility(View.GONE);
                newPassword.setVisibility(View.GONE);
                confirmPassword.setVisibility(View.GONE);
                save.setVisibility(View.GONE);
                cancel.setVisibility(View.GONE);
            }
        });
        //change password
        changePassword=findViewById(R.id.faculty_change_password);
        changePassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                changePassword.setVisibility(View.GONE);
                oldPassword.setVisibility(View.VISIBLE);
                newPassword.setVisibility(View.VISIBLE);
                confirmPassword.setVisibility(View.VISIBLE);
                save.setVisibility(View.VISIBLE);
                cancel.setVisibility(View.VISIBLE);
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
                            new getPassword().execute();
                            card.setVisibility(View.GONE);
                            loader.show();
                            loading.setVisibility(View.VISIBLE);
                        }
                        else{ TastyToast.makeText(getApplicationContext(),"Password Not Matched",Toast.LENGTH_SHORT,TastyToast.WARNING).show();}}

                }

            }
        });
    }


    class getPassword extends AsyncTask<String,String,String> {
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
                br.write(URLEncoder.encode("faculty_id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8"));
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
            if(s.equals(oldPassword.getText().toString())){new FacultyProfile.setPassword().execute();}
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
                br.write(URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(newPassword.getText().toString(),"UTF-8")+ "&" + URLEncoder.encode("faculty_id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8"));
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
            cancel.setVisibility(View.GONE);
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
//        EditText emp_id,emp_password;
//        emp_password = (EditText)findViewById(R.id.add_sub_sub);

        String password = newPassword.getText().toString();
        Validation validate=new Validation();

        if(!(error=validate.passwordValidation(password)).equals("true")){
            newPassword.setError(error);
            return false;
        }
        return true;
    }
}
