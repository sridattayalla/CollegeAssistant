package com.stepCone.sridatta.CollegeAssistant;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{
    android.support.v7.widget.Toolbar tb;
    ActionBar ab;
    ImageView identifyImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_main);
        identifyImage=findViewById(R.id.identifing_image);
//        //toolbar
//        tb=(Toolbar)findViewById(R.id.main_activity_toolbar);
//        setSupportActionBar(tb);
//       ab=getSupportActionBar();
//       ab.setDisplayHomeAsUpEnabled(true);

        //getting intent
        Intent i=getIntent();
        int check=i.getIntExtra("chooseFragment",1);
        if(check==1){
            faculty();
        }
        if(check==2){
            student();
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case android.R.id.home:
//                onBackPressed();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


    //Fragment insert methods
    public void student(){
       // ab.setTitle("Student");
        identifyImage.setImageDrawable(getResources().getDrawable(R.drawable.faculty_login_image));
        StudentSignInSignUp studentFragment=new StudentSignInSignUp();
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container,studentFragment);
        ft.commit();
    }

    public void faculty(){
       // ab.setTitle("Faculty");
        identifyImage.setImageDrawable(getResources().getDrawable(R.drawable.faculty_login_icon_orig));
        FacultySignInSignUp facultyFragment=new FacultySignInSignUp();
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container,facultyFragment);
        ft.commit();
    }




}
