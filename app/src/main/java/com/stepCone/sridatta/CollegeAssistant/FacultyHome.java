package com.stepCone.sridatta.CollegeAssistant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
import com.stepCone.sridatta.CollegeAssistant.faculty.AttendenceRegister;
import com.stepCone.sridatta.CollegeAssistant.faculty.FacultyProfile;
import com.stepCone.sridatta.CollegeAssistant.faculty.PostLinkActivity;
import com.stepCone.sridatta.CollegeAssistant.faculty.getStudentProgress;
import com.stepCone.sridatta.CollegeAssistant.faculty.postMarks;
import com.stepCone.sridatta.CollegeAssistant.faculty.questionActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by HP-PC on 13-01-2018.
 */

public class FacultyHome extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    String perId;
    CircleImageView facultyImage;
    String facultyName;
    String designation;
    TextView faculty_name;
    TextView getFaculty_designation;
    TextView faculty_id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faculty_home);
        //getting perId
        perId=getIntent().getStringExtra("perId");
       // Toast.makeText(this,perId,Toast.LENGTH_SHORT).show();

        //using sharedPreference to directly open main page
        if(perId!=null) {
            SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sf.edit();
            editor.putString("id", perId);
            editor.commit();
            //
            SharedPreferences choose = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor edit = sf.edit();
            edit.putInt("choose",2);
            edit.commit();
        }

        //facutly image onclick
        facultyImage=findViewById(R.id.faculty_image);
        facultyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(FacultyHome.this, FacultyProfile.class);
                intent.putExtra("name",facultyName);
                intent.putExtra("designation",designation);
                startActivity(intent);
            }
        });

        //testing
        SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(this);
        perId=sf.getString("id","opps");
       // Toast.makeText(this,perId,Toast.LENGTH_SHORT).show();

        //image retriving
        SharedPreferences imageSf=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String imageString=imageSf.getString("facultyImage",null);
        if(imageString==null){
        try {
            new getFacultyImage().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }}
        else{
            byte [] encodeByte=Base64.decode(imageString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            facultyImage=findViewById(R.id.faculty_image);
            facultyImage.setImageBitmap(bitmap);
        }

        //setting facuty details
        faculty_name=findViewById(R.id.faculty_name);
        getFaculty_designation=findViewById(R.id.faculty_designation);
        faculty_id=findViewById(R.id.perId);
        faculty_id.setText(perId);

        SharedPreferences namesf=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String nameString=namesf.getString("facultyName",null);
        if(nameString==null){
        try {
            new getFacultyName().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }}
        else{
            String mydelim = ",";
            StringTokenizer stringTokenizer =
                    new StringTokenizer(nameString, mydelim);
            int count = stringTokenizer.countTokens();
            System.out.println("Number of tokens : " + count + "\n");
            facultyName=stringTokenizer.nextToken();
            designation=stringTokenizer.nextToken();
            faculty_name.setText(facultyName);
            getFaculty_designation.setText(designation);
        }

        //CollapsingToolbarLayout collapsingToolbarLayout=findViewById(R.id.faculty_collapsing_toolbar);
    }

    void Exit(){
        if (doubleBackToExitPressedOnce) {
            finish();
            System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    public void onBackPressed() {
        Exit();
    }


    //card view onClick events



    public void openChooseClassActivity(View v){
        startActivity(new Intent(this, AttendenceRegister.class));}

    public void facultyLogOut(View v){


        new AlertDialog.Builder(this)
                .setTitle("LogOut")
                .setMessage("Sure you want to logout?")
                .setPositiveButton("LogOut", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = sf.edit();
                        editor.putString("id", null);
                        editor.commit();
                        //
                        SharedPreferences choose = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor edit = choose.edit();
                        edit.putInt("choose",0);
                        edit.commit();
                        //
                        SharedPreferences imagesf = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor imageeditor = imagesf.edit();
                        imageeditor.putString("facultyImage", null);
                        imageeditor.commit();
                        //
                        SharedPreferences namesf = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor nameedit = namesf.edit();
                        nameedit.putString("facultyName",null);
                        nameedit.commit();
                        //
                        Intent intent=new Intent(getApplicationContext(),ChooserPage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                    }})
                .setNegativeButton("Cancel", null).show();


    }

    public void MakeAnExam(View v){startActivity(new Intent(FacultyHome.this,questionActivity.class));}

    public void GetStudentProgress(View v){startActivity(new Intent(FacultyHome.this,postMarks.class)); }

    public void PostALink(View v){startActivity(new Intent(FacultyHome.this, PostLinkActivity.class));}

    public void ShowFacultyProfile(View v){}

    class getFacultyImage extends AsyncTask<String,String,Bitmap>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            try{URL url=new URL("http://fullstackdevelopment.thecompletewebhosting.com/college_assistant/get_faculty_image.php");
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(10000);
                conn.connect();
                OutputStream os=conn.getOutputStream();
                BufferedWriter br=new BufferedWriter(new OutputStreamWriter(os));
                br.write(URLEncoder.encode("roll_no","UTF-8")+"="+URLEncoder.encode(perId,"UTF-8"));
                br.flush();
                br.close();
                os.close();
                //getting image
                InputStream is=conn.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
                Bitmap bitmap= BitmapFactory.decodeStream(is);
                //converting bitmap to string
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
                byte [] b=baos.toByteArray();
                String temp=Base64.encodeToString(b, Base64.DEFAULT);
                //saving imageString
                SharedPreferences sf=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor=sf.edit();
                editor.putString("facultyImage",temp);
                editor.commit();
                //converting string to image
                byte [] encodeByte=Base64.decode(temp,Base64.DEFAULT);
                bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                return bitmap;
                //while (sub!=null){HandlingSectionsList.add(s); sub=bufferedReader.readLine();}
            }
            catch (Exception e){}
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            facultyImage.setImageBitmap(bitmap);
        }
    }

    class getFacultyName extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {
            try{URL url=new URL("http://fullstackdevelopment.thecompletewebhosting.com/college_assistant/get_faculty_profile.php");
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(10000);
                conn.connect();
                OutputStream os=conn.getOutputStream();
                BufferedWriter br=new BufferedWriter(new OutputStreamWriter(os));
                br.write(URLEncoder.encode("roll_no","UTF-8")+"="+URLEncoder.encode(perId,"UTF-8"));
                br.flush();
                br.close();
                os.close();
                //getting name
                InputStream is=conn.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
                String nameString=bufferedReader.readLine();
                //saving String name
                SharedPreferences sf=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor=sf.edit();
                editor.putString("facultyName",nameString);
                editor.commit();
                return nameString;
                //while (sub!=null){HandlingSectionsList.add(s); sub=bufferedReader.readLine();}
            }
            catch (Exception e){}

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
            String mydelim = ",";
            StringTokenizer stringTokenizer =
                    new StringTokenizer(s, mydelim);
            int count = stringTokenizer.countTokens();
            System.out.println("Number of tokens : " + count + "\n");
            facultyName=stringTokenizer.nextToken();
            designation=stringTokenizer.nextToken();
            faculty_name.setText(facultyName);
            getFaculty_designation.setText(designation);}
            catch (Exception e){
                //logout
                SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sf.edit();
                editor.putString("id", null);
                editor.commit();
                //
                SharedPreferences choose = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor edit = choose.edit();
                edit.putInt("choose",0);
                edit.commit();
                //
                SharedPreferences imagesf = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor imageeditor = imagesf.edit();
                imageeditor.putString("facultyImage", null);
                imageeditor.commit();
                //
                SharedPreferences namesf = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor nameedit = namesf.edit();
                nameedit.putString("facultyName",null);
                nameedit.commit();
                //
                Intent intent=new Intent(getApplicationContext(),ChooserPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                TastyToast.makeText(getApplicationContext(),"Check Internet Connection",TastyToast.LENGTH_SHORT,TastyToast.CONFUSING).show();
            }
        }
    }
}
