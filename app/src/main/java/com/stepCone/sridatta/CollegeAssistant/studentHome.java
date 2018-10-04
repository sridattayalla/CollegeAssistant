package com.stepCone.sridatta.CollegeAssistant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.stepCone.sridatta.CollegeAssistant.faculty.AttendenceRegister;
import com.stepCone.sridatta.CollegeAssistant.student.StudentProfile;
import com.stepCone.sridatta.CollegeAssistant.student.studentAttendenceActivity;
import com.stepCone.sridatta.CollegeAssistant.student.student_credits;
import com.stepCone.sridatta.CollegeAssistant.student.student_home;
import com.stepCone.sridatta.CollegeAssistant.student.student_practice_quiz;
import com.stepCone.sridatta.CollegeAssistant.student.student_take_quiz;

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
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class studentHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,student_home.student_homeInterface,student_credits.student_marksInterface
,studentAttendenceActivity.student_attedenceInterface{

    String jntuno;
    boolean doubleBackToExitPressedOnce = false;
    CircleImageView studentImage;
    TextView studentName,studentJntuno;
    Spinner quizSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.student_home_tool_bar);
        setSupportActionBar(toolbar);


        //navigation drawer things
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header=navigationView.getHeaderView(0);
        //setting quiz spinner
      /*  quizSpinner=(Spinner) navigationView.getMenu().findItem(R.id.item_quiz_spinner).getActionView();
        List<String> list=new ArrayList<>();
        list.add("Take Quiz");list.add("Practice Quiz");
        ArrayAdapter<String> items=new ArrayAdapter<String>(getBaseContext(),android.R.layout.simple_list_item_1,list);
        items.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quizSpinner.setAdapter(items);

        //setting on click
        quizSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){student_take_quiz studentTakeQuiz=new student_take_quiz();
                    FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.student_container,studentTakeQuiz);
                    ft.commit();}
                if(i==1){
                    student_practice_quiz studentPracticeQuiz=new student_practice_quiz();
                    FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.student_container,studentPracticeQuiz);
                    ft.commit();
                }
            }
        });
*/
        studentImage=(CircleImageView)header.findViewById(R.id.studentImag);
        studentName=header.findViewById(R.id.student_name);
        studentJntuno=header.findViewById(R.id.jntu_no);
        studentJntuno.setText(jntuno);
        navigationView.setNavigationItemSelectedListener(this);

        //getting jntuno from intent
        jntuno=getIntent().getStringExtra("jntuno");

        //open profile page
        try {
            studentImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(studentHome.this, StudentProfile.class);
                    startActivity(intent);
                }
            });
        }
        catch (Exception e){}


        //using sharedPreference to directly open main page
        if(jntuno!=null) {
            SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sf.edit();
            editor.putString("id", jntuno);
            editor.commit();
            //
            SharedPreferences choose = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor edit = sf.edit();
            edit.putInt("choose",3);
            edit.commit();
        }

        SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(this);
        jntuno=sf.getString("id","opps");



        //fragment setting
        student_home studentHome=new student_home();
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.student_container,studentHome);
        ft.commit();

        //setting image
        SharedPreferences imagePreference=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String imageString=imagePreference.getString("studentImage",null);
        if(imageString==null){
        try {
            new getStudentImage().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }}
        else{
            byte [] encodeByte=Base64.decode(imageString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            studentImage.setImageBitmap(bitmap);
        }
        //setting name
        SharedPreferences namePreference=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String nameString=namePreference.getString("studentName",null);
        if(nameString==null){
        try {
            new getStudentName().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }}
        else {
            studentName.setText(nameString);
            studentJntuno.setText(jntuno);
        }


//            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//            fab.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                }
//            });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            Exit();

        } else {
            //super.onBackPressed();
            Exit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.student_home) {
            student_home studentHome=new student_home();
           FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.student_container,studentHome,"student_home_fragment");
            ft.addToBackStack("student_home_fragment").commit();

        }

        else if (id == R.id.attendence) {
            // Handle the attendence action
            studentAttendenceActivity sac=new studentAttendenceActivity();
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.student_container,sac);
            ft.commit();
        }

        else if (id == R.id.menu_take_quiz) {
            // Handle the attendence action
            student_take_quiz stq=new student_take_quiz();
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.student_container,stq);
            ft.commit();
        }

        else if (id == R.id.menu_practice_quiz) {
            // Handle the attendence action
            student_practice_quiz stq=new student_practice_quiz();
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.student_container,stq);
            ft.commit();
        }


        else if (id == R.id.credits) {
            student_credits stq=new student_credits();
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.student_container,stq);
            ft.commit();
        }

        else if (id == R.id.about) {

        }

        else if (id == R.id.log_out) {

            new AlertDialog.Builder(this)
                    .setTitle("")
                    .setMessage("Sure you want to log out?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Log out", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            logout();
                        }})
                    .setNegativeButton("cancel", null).show();



        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void Exit(){
        if (doubleBackToExitPressedOnce) {
            finish();
            System.exit(0);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        student_home studentHome=new student_home();
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.student_container,studentHome,"student_home_fragment");
        ft.addToBackStack("student_home_fragment").commit();
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }



    class getStudentImage extends AsyncTask<String,String,Bitmap>{
        @Override
        protected Bitmap doInBackground(String... strings) {
            try{URL url=new URL("http://fullstackdevelopment.thecompletewebhosting.com/college_assistant/get_image.php");
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(10000);
                conn.connect();
                OutputStream os=conn.getOutputStream();
                BufferedWriter br=new BufferedWriter(new OutputStreamWriter(os));
                br.write(URLEncoder.encode("roll_no","UTF-8")+"="+URLEncoder.encode(jntuno,"UTF-8"));
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
                String temp= Base64.encodeToString(b, Base64.DEFAULT);
                //saving imageString
                SharedPreferences sf=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor=sf.edit();
                editor.putString("studentImage",temp);
                editor.commit();
                //converting string to image
                byte [] encodeByte=Base64.decode(temp,Base64.DEFAULT);
                bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                return bitmap;
                //while (sub!=null){HandlingSectionsList.add(s); sub=bufferedReader.readLine();}
            }
            catch (Exception e){
                logout();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            studentImage.setImageBitmap(bitmap);
        }
    }

    class getStudentName extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try{URL url=new URL("http://fullstackdevelopment.thecompletewebhosting.com/college_assistant/get_student_profile.php");
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(10000);
                conn.connect();
                OutputStream os=conn.getOutputStream();
                BufferedWriter br=new BufferedWriter(new OutputStreamWriter(os));
                br.write(URLEncoder.encode("roll_no","UTF-8")+"="+URLEncoder.encode(jntuno,"UTF-8"));
                br.flush();
                br.close();
                os.close();
                //getting sections list
                InputStream is=conn.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
                String nameString=bufferedReader.readLine();
                //saving String name
                SharedPreferences sf=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor=sf.edit();
                editor.putString("studentName",nameString);
                editor.commit();
                return nameString;
                //while (sub!=null){HandlingSectionsList.add(s); sub=bufferedReader.readLine();}
            }
            catch (Exception e){
                logout();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            studentName.setText(s);
            studentJntuno.setText(jntuno);
        }
    }


    //interfaces
    @Override
    public void refresh() {
        student_home studentHome=new student_home();
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.student_container,studentHome,"student_home_fragment");
        ft.addToBackStack("student_home_fragment").commit();
    }

    @Override
    public void marksrefresh() {
        student_credits studentHome=new student_credits();
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.student_container,studentHome,"student_home_fragment");
        ft.addToBackStack("student_home_fragment").commit();
    }

    @Override
    public void attendencerefresh(String s) {
        switch (s){
            case "attendence": studentAttendenceActivity studentHome=new studentAttendenceActivity();
                FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.student_container,studentHome,"student_home_fragment");
                ft.addToBackStack("student_home_fragment").commit();
                break;
            case "take_quiz":student_take_quiz stq=new student_take_quiz();
            FragmentTransaction ft1=getSupportFragmentManager().beginTransaction();
                ft1.replace(R.id.student_container,stq,"student_take_quiz_fragment");
                ft1.addToBackStack("student_home_fragment").commit();
                break;

            case "practice_quiz":student_practice_quiz spq=new student_practice_quiz();
                FragmentTransaction ft2=getSupportFragmentManager().beginTransaction();
                ft2.replace(R.id.student_container,spq,"student_take_quiz_fragment");
                ft2.addToBackStack("student_home_fragment").commit();
                break;
        }

    }

    //log out method
    void logout(){
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
        imageeditor.putString("studentImage", null);
        imageeditor.commit();
        //
        SharedPreferences nameSf = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor nameedit = nameSf.edit();
        nameedit.putString("studentName",null);
        nameedit.commit();

        FirebaseMessaging.getInstance().unsubscribeFromTopic("DBMS");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("JAVA");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("PYTHON");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("GEOLOGY");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("APTITUDE");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("OS");

        Intent intent=new Intent(getApplicationContext(),ChooserPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}
