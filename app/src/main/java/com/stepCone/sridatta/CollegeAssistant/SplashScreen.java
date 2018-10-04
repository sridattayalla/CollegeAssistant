package com.stepCone.sridatta.CollegeAssistant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

/**
 * Created by HP-PC on 09-01-2018.
 */

public class SplashScreen extends AppCompatActivity {
    Intent intent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        final Context context=getApplicationContext();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //splash
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    SharedPreferences sf= PreferenceManager.getDefaultSharedPreferences(context);
                    //SharedPreferences choose = PreferenceManager.getDefaultSharedPreferences(context);

                     /*if(sf.getString("check",null)==null){
                        Intent i=new Intent(SplashScreen.this,ChooserPage.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    }
                    else{
                        Intent i=new Intent(SplashScreen.this,studentHome.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    }*/

                    int i=sf.getInt("choose",0);
                    String id=sf.getString("id",null);

                    switch (i){

                        case 0:intent=new Intent(SplashScreen.this,ChooserPage.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                                break;
                        case 1:break;
                        case 2:if(id==null){
                                intent=new Intent(SplashScreen.this,ChooserPage.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                                break;
                                }
                                else{
                                    Intent intent=new Intent(SplashScreen.this,FacultyHome.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                    break;
                                }
                        case 3:if(id==null){
                                    intent=new Intent(SplashScreen.this,ChooserPage.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                    break;
                                }
                                else{
                                    Intent intent=new Intent(SplashScreen.this,studentHome.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                    break;
                                }
                        default:intent=new Intent(SplashScreen.this,ChooserPage.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                                break;

                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread=new Thread(runnable);
        thread.start();
    }
}
