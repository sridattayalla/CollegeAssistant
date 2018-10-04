package com.stepCone.sridatta.CollegeAssistant;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by HP-PC on 03-01-2018.
 */

public class ChooserPage extends AppCompatActivity {
    Intent i;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chooser_page);
        //getting tool bar
//        Toolbar tb=findViewById(R.id.welcome_toolbar);
//        ActionBar ab=getSupportActionBar();
//        tb.setTitle("Welcome");
//        tb.setTitleTextColor(getResources().getColor(R.color.orange));
    }

    //image buttons methods
    public void administratorOptionClicked(View v){

    }

    public void facultyOptionClicked(View v){
        i=new Intent(ChooserPage.this,MainActivity.class);
        i.putExtra("chooseFragment",1);
        startActivity(i);
    }

    public void studentOptionClicked(View v){
            i=new Intent(ChooserPage.this,MainActivity.class);
            i.putExtra("chooseFragment",2);
            startActivity(i);

    }

    // checking internet connection

    public  boolean checkInternetConnection(){
        ConnectivityManager cm =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=cm.getActiveNetworkInfo();
        return info!=null && info.isConnectedOrConnecting();
    }

    //handling hardware back button

    @Override
    public void onBackPressed() {
//        finish();
//        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }
}
