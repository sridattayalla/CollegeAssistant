package com.stepCone.sridatta.CollegeAssistant.faculty;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;
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
import java.util.List;
import java.util.StringTokenizer;

public class postMArksToSection extends AppCompatActivity {
    String subject;
    String exam;
    List<TextView> rollno=new ArrayList<>();
    List<EditText> marks=new ArrayList<>();
    List<String> receivedNumbers=new ArrayList<>();
    String section_name;
    int studentCount;
    String postedMarks;
    String jntuno;
    TextView section,textViewExam;
    ImageView internetErrorImage;
    Button retry;
    CardView card;
    AVLoadingIndicatorView loader,submitting;
    TextView loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_marks_to_section);
        Bundle extras=getIntent().getExtras();
        subject=extras.getString("subject");
        exam=extras.getString("exam");
        section_name=extras.getString("section");
        //Toast.makeText(getApplicationContext(),subject+exam,Toast.LENGTH_LONG).show();

        //toolbar
        Toolbar toolbar=findViewById(R.id.post_marks_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Click send to post Marks");

        textViewExam=findViewById(R.id.postmark_section_exam);
        section=findViewById(R.id.postmark_section_sec);
        textViewExam.setText(exam);
        section.setText(section_name);

        //internet error
        retry=findViewById(R.id.post_to_sec_retry);
        internetErrorImage=findViewById(R.id.post_to_sec_no_internet_image);
        loader=findViewById(R.id.post_to_sec_loader);
        loader.show();
        loading=findViewById(R.id.postmarks_loading_text);
        submitting=findViewById(R.id.postmarks_to_sec_loader);
        submitting.hide();


        //card
        card=findViewById(R.id.postmark_to_section_card);

        //getting textview instances
        rollno.add((TextView) findViewById(R.id.marks_student_id1));
        rollno.add((TextView) findViewById(R.id.marks_student_id2));
        rollno.add((TextView) findViewById(R.id.marks_student_id3));
        rollno.add((TextView) findViewById(R.id.marks_student_id4));
        rollno.add((TextView) findViewById(R.id.marks_student_id5));
        rollno.add((TextView) findViewById(R.id.marks_student_id6));
        rollno.add((TextView) findViewById(R.id.marks_student_id7));
        rollno.add((TextView) findViewById(R.id.marks_student_id8));
        rollno.add((TextView) findViewById(R.id.marks_student_id9));
        rollno.add((TextView) findViewById(R.id.marks_student_id10));
        rollno.add((TextView) findViewById(R.id.marks_student_id11));
        rollno.add((TextView) findViewById(R.id.marks_student_id12));
        rollno.add((TextView) findViewById(R.id.marks_student_id13));
        rollno.add((TextView) findViewById(R.id.marks_student_id14));
        rollno.add((TextView) findViewById(R.id.marks_student_id15));
        rollno.add((TextView) findViewById(R.id.marks_student_id16));
        rollno.add((TextView) findViewById(R.id.marks_student_id17));
        rollno.add((TextView) findViewById(R.id.marks_student_id18));
        rollno.add((TextView) findViewById(R.id.marks_student_id19));
        rollno.add((TextView) findViewById(R.id.marks_student_id20));
        rollno.add((TextView) findViewById(R.id.marks_student_id21));
        rollno.add((TextView) findViewById(R.id.marks_student_id22));
        rollno.add((TextView) findViewById(R.id.marks_student_id23));
        rollno.add((TextView) findViewById(R.id.marks_student_id24));
        rollno.add((TextView) findViewById(R.id.marks_student_id25));
        rollno.add((TextView) findViewById(R.id.marks_student_id26));
        rollno.add((TextView) findViewById(R.id.marks_student_id27));
        rollno.add((TextView) findViewById(R.id.marks_student_id28));
        rollno.add((TextView) findViewById(R.id.marks_student_id29));
        rollno.add((TextView) findViewById(R.id.marks_student_id30));
        //getting edit texts
        marks.add((EditText)findViewById(R.id.marks_student_marks1));
        marks.add((EditText)findViewById(R.id.marks_student_marks2));
        marks.add((EditText)findViewById(R.id.marks_student_marks3));
        marks.add((EditText)findViewById(R.id.marks_student_marks4));
        marks.add((EditText)findViewById(R.id.marks_student_marks5));
        marks.add((EditText)findViewById(R.id.marks_student_marks6));
        marks.add((EditText)findViewById(R.id.marks_student_marks7));
        marks.add((EditText)findViewById(R.id.marks_student_marks8));
        marks.add((EditText)findViewById(R.id.marks_student_marks9));
        marks.add((EditText)findViewById(R.id.marks_student_marks10));
        marks.add((EditText)findViewById(R.id.marks_student_marks11));
        marks.add((EditText)findViewById(R.id.marks_student_marks12));
        marks.add((EditText)findViewById(R.id.marks_student_marks13));
        marks.add((EditText)findViewById(R.id.marks_student_marks14));
        marks.add((EditText)findViewById(R.id.marks_student_marks15));
        marks.add((EditText)findViewById(R.id.marks_student_marks16));
        marks.add((EditText)findViewById(R.id.marks_student_marks17));
        marks.add((EditText)findViewById(R.id.marks_student_marks18));
        marks.add((EditText)findViewById(R.id.marks_student_marks19));
        marks.add((EditText)findViewById(R.id.marks_student_marks20));
        marks.add((EditText)findViewById(R.id.marks_student_marks21));
        marks.add((EditText)findViewById(R.id.marks_student_marks22));
        marks.add((EditText)findViewById(R.id.marks_student_marks23));
        marks.add((EditText)findViewById(R.id.marks_student_marks24));
        marks.add((EditText)findViewById(R.id.marks_student_marks25));
        marks.add((EditText)findViewById(R.id.marks_student_marks26));
        marks.add((EditText)findViewById(R.id.marks_student_marks27));
        marks.add((EditText)findViewById(R.id.marks_student_marks28));
        marks.add((EditText)findViewById(R.id.marks_student_marks29));
        marks.add((EditText)findViewById(R.id.marks_student_marks30));

        if(!checkInternetConnection()){
            retry.setVisibility(View.VISIBLE);
            internetErrorImage.setVisibility(View.VISIBLE);
            loader.hide();
        }
        else {
            new getStudentRollno().execute();
        }


        //retry
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loader.show();
                finish();
                startActivity(getIntent());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.attendence_register_menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        //call lock

        //call send
        if(item.getItemId()==R.id.submit){
            new AlertDialog.Builder(this)
                    .setTitle("Submit Marks")
                    .setMessage("Sure you want to submit?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            if(!checkInternetConnection()){
                                TastyToast.makeText(getApplicationContext(),"Check Internet Connection",Toast.LENGTH_SHORT,TastyToast.CONFUSING).show();}
                            else{
                                card.setVisibility(View.GONE);
                                loading.setVisibility(View.VISIBLE);
                                submitting.show();
                                new postMarks().execute();}

                        }})
                    .setNegativeButton("cancel", null).show();

        }
        return true;
    }

    class getStudentRollno extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{URL url=new URL("http://fullstackdevelopment.thecompletewebhosting.com/college_assistant/student_list.php");
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(10000);
                conn.connect();
                OutputStream os=conn.getOutputStream();
                BufferedWriter br=new BufferedWriter(new OutputStreamWriter(os));
                br.write(URLEncoder.encode("section_name","UTF-8")+"="+URLEncoder.encode(section_name,"UTF-8"));
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
                    receivedNumbers.add(stringTokenizer.nextToken());
                }

            }
            catch (Exception e){receivedNumbers.add("catch");}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            studentCount=receivedNumbers.size();
            for(int i=0;i<receivedNumbers.size();i++){
                rollno.get(i).setText(receivedNumbers.get(i));
            }
            for (int i=0;i<receivedNumbers.size();i++){
                rollno.get(i).setVisibility(View.VISIBLE);
            }
            for(int i=29;i>(receivedNumbers.size()-1);i--){
                marks.get(i).setVisibility(View.GONE);
                rollno.get(i).setVisibility(View.GONE);
            }
            loader.hide();
            card.setVisibility(View.VISIBLE);

//            progressBar.setVisibility(View.GONE);
//            updateProgressbar.setMax(studentCount);
        }
    }

    class postMarks extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {
            for(int i=0;i<receivedNumbers.size();i++) {
                postedMarks=marks.get(i).getText().toString();
                jntuno=rollno.get(i).getText().toString();
                try {
                    URL url = new URL("http://fullstackdevelopment.thecompletewebhosting.com/college_assistant/marks_updation.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(10000);
                    conn.connect();
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter br = new BufferedWriter(new OutputStreamWriter(os));
                    br.write(URLEncoder.encode("exam", "UTF-8") + "=" + URLEncoder.encode(exam, "UTF-8") + "&" + URLEncoder.encode("section_name", "UTF-8") + "=" + URLEncoder.encode(section_name, "UTF-8") + "&" + URLEncoder.encode("subject", "UTF-8") + "=" + URLEncoder.encode(subject, "UTF-8") + "&" + URLEncoder.encode("roll_no", "UTF-8") + "=" + URLEncoder.encode(jntuno, "UTF-8") + "&" + URLEncoder.encode("marks", "UTF-8") + "=" + URLEncoder.encode(postedMarks, "UTF-8"));
                    br.flush();
                    br.close();
                    os.close();
                    //getting sections list
                    InputStream is = conn.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                    String s = bufferedReader.readLine();
                } catch (Exception e) {
                    receivedNumbers.add("catch");
                }
            }
            return "finish";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            card.setVisibility(View.VISIBLE);
            submitting.hide();
            loading.setVisibility(View.GONE);
            TastyToast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT,TastyToast.SUCCESS).show();
        }
    }

    //method to check internet connection
    public  boolean checkInternetConnection(){
        ConnectivityManager cm =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=cm.getActiveNetworkInfo();
        return info!=null && info.isConnectedOrConnecting();
    }

}
