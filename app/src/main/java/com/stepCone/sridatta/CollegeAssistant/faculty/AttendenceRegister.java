package com.stepCone.sridatta.CollegeAssistant.faculty;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.sdsmdg.tastytoast.TastyToast;
import com.stepCone.sridatta.CollegeAssistant.MainActivity;
import com.stepCone.sridatta.CollegeAssistant.R;
import com.wang.avi.AVLoadingIndicatorView;

import net.igenius.customcheckbox.CustomCheckBox;

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

/**
 * Created by HP-PC on 15-01-2018.
 */

public class AttendenceRegister extends AppCompatActivity implements AttendenceDialog.attendenceDialogHandle{
    String perId;
    ArrayList<String> HandlingSectionsList;
    ArrayList<String> HandlingSubjectsList;
    Context context;
    String section_name;
    String subject;
    List<TextView> rollnoText=new ArrayList<>();
    List<CustomCheckBox> customCheckBoxes=new ArrayList<>();
    int section_no;
    String TAG="AttendenceRegister.java";
    String popUpContents[];
    PopupWindow popupWindowSections;
    Button buttonShowDropDown;
    int studentCount=0;
    Button retry;
    Button chooseSection;
    AVLoadingIndicatorView loadingIndicatorView,submissionLoader,subjectsLoader;
    AttendenceDialog cf=new AttendenceDialog();
    TextView loading;
    ImageView no_internet_image;
    Button clear,mark;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendence_register);
        //clear
        clear=findViewById(R.id.clear_all);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<31;i++) {
                   // customCheckBoxes.get(i).setChecked(true);
                    if (customCheckBoxes.get(i).isChecked()) {
                        customCheckBoxes.get(i).toggle();
                    }
                }
            }
        });

        mark=findViewById(R.id.mark_all);
        mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<31;i++) {
                    //customCheckBoxes.get(i).setChecked(false);
                    if (!customCheckBoxes.get(i).isChecked()) {
                        customCheckBoxes.get(i).toggle();
                    }
                }
            }
        });
        //instantiating checkboxes
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox1));
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox2));
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox3));
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox4));
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox5));
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox6));
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox7));
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox8));
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox9));
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox10));
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox11));
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox12));
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox13));
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox14));
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox15));
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox16));
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox17));
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox18));
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox19));
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox20));
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox21));
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox22));
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox23));
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox24));
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox25));
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox26));
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox27));
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox28));
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox29));
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox30));
        customCheckBoxes.add((CustomCheckBox) findViewById(R.id.a_checkbox31));
        //instantiating textviews
        rollnoText.add((TextView)findViewById(R.id.a_textview1));
        rollnoText.add((TextView)findViewById(R.id.a_textview2));
        rollnoText.add((TextView)findViewById(R.id.a_textview3));
        rollnoText.add((TextView)findViewById(R.id.a_textview4));
        rollnoText.add((TextView)findViewById(R.id.a_textview5));
        rollnoText.add((TextView)findViewById(R.id.a_textview6));
        rollnoText.add((TextView)findViewById(R.id.a_textview7));
        rollnoText.add((TextView)findViewById(R.id.a_textview8));
        rollnoText.add((TextView)findViewById(R.id.a_textview9));
        rollnoText.add((TextView)findViewById(R.id.a_textview10));
        rollnoText.add((TextView)findViewById(R.id.a_textview11));
        rollnoText.add((TextView)findViewById(R.id.a_textview12));
        rollnoText.add((TextView)findViewById(R.id.a_textview13));
        rollnoText.add((TextView)findViewById(R.id.a_textview14));
        rollnoText.add((TextView)findViewById(R.id.a_textview15));
        rollnoText.add((TextView)findViewById(R.id.a_textview16));
        rollnoText.add((TextView)findViewById(R.id.a_textview17));
        rollnoText.add((TextView)findViewById(R.id.a_textview18));
        rollnoText.add((TextView)findViewById(R.id.a_textview19));
        rollnoText.add((TextView)findViewById(R.id.a_textview20));
        rollnoText.add((TextView)findViewById(R.id.a_textview21));
        rollnoText.add((TextView)findViewById(R.id.a_textview22));
        rollnoText.add((TextView)findViewById(R.id.a_textview23));
        rollnoText.add((TextView)findViewById(R.id.a_textview24));
        rollnoText.add((TextView)findViewById(R.id.a_textview25));
        rollnoText.add((TextView)findViewById(R.id.a_textview26));
        rollnoText.add((TextView)findViewById(R.id.a_textview27));
        rollnoText.add((TextView)findViewById(R.id.a_textview28));
        rollnoText.add((TextView)findViewById(R.id.a_textview29));
        rollnoText.add((TextView)findViewById(R.id.a_textview30));
        rollnoText.add((TextView)findViewById(R.id.a_textview31));

        //making invisible
        for(int i=0;i<customCheckBoxes.size();i++){
            customCheckBoxes.get(i).setVisibility(View.INVISIBLE);
            rollnoText.get(i).setVisibility(View.INVISIBLE);
        }

        //perId
        SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(this);
        perId=sf.getString("id",null);

        //context
        context=getApplicationContext();


        //tool bar
        Toolbar toolbar=findViewById(R.id.attendence_register_toolbar);
        setSupportActionBar(toolbar);



        //internet error
        no_internet_image=findViewById(R.id.attendence_register_no_internet_image);
        retry=findViewById(R.id.attendence_register_retry);
        loading=findViewById(R.id.attendence_loading_text);
        subjectsLoader=findViewById(R.id.faculty_attendence_subjects_loader);
        subjectsLoader.show();
        submissionLoader=findViewById(R.id.faculty_attendence_submission_loader);
        submissionLoader.hide();
        loadingIndicatorView=findViewById(R.id.faculty_attendence_loader);
        loadingIndicatorView.hide();


        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingIndicatorView.show();
                finish();
                startActivity(getIntent());
            }
        });

        HandlingSectionsList=new ArrayList<String>();
        HandlingSubjectsList=new ArrayList<String>();
        chooseSection=findViewById(R.id.post_marks_choose_section);

        //new getHandlingSectionsList().execute();
        try {
            if(!checkInternetConnection()){
                for(int i=0;i<customCheckBoxes.size();i++){customCheckBoxes.get(i).setVisibility(View.GONE);
                    rollnoText.get(i).setVisibility(View.GONE);
                    no_internet_image.setVisibility(View.VISIBLE);
                    retry.setVisibility(View.VISIBLE);
                    chooseSection.setVisibility(View.INVISIBLE);
                    subjectsLoader.hide();
                }
            }
           else{new getHandlingSectionsList().execute();}
        } catch (Exception e) {
            e.printStackTrace();
        }/* catch (ExecutionException e) {
            e.printStackTrace();
        }*/
        // HandlingSectionsList=new getHandlingSectionsList().execute()get();


      /*  // convert to simple array
        popUpContents = new String[HandlingSectionsList.size()];
        HandlingSectionsList.toArray(popUpContents);*/


        //button handling
        buttonShowDropDown=findViewById(R.id.post_marks_choose_section);
        buttonShowDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindowSections.showAsDropDown(view, -5, 0);
            }
        });


    }



    public void setAndgetRollno(){
        new AttendenceRegisterViewer().execute();
    }

   /* public void makeThemVisible(){
        for(int i=0;i<studentCount;i++){
            customCheckBoxes.get(i).setVisibility(View.VISIBLE);
            rollnoText.get(i).setVisibility(View.VISIBLE);
        }
    }*/

    public  boolean checkInternetConnection(){
        ConnectivityManager cm =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=cm.getActiveNetworkInfo();
        return info!=null && info.isConnectedOrConnecting();
    }



    public PopupWindow popupWindowSections(){
        // initialize a pop up window type
        PopupWindow popupWindow = new PopupWindow(this);

        // the drop down list is a list view
        ListView listViewDogs = new ListView(this);

        // set our adapter and pass our pop up window contents
        listViewDogs.setAdapter(sectionAdapter(popUpContents));

        // set the item click listener
        listViewDogs.setOnItemClickListener(new sectionDropDown());

        // some other visual settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth(250);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // set the list view as pop up window content
        popupWindow.setContentView(listViewDogs);

        return popupWindow;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.attendence_register_menu,menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item){
        //call lock

        //call send
        if(item.getItemId()==R.id.submit) {
            /*
            FragmentManager fm=getSupportFragmentManager();
            cf.show(fm,"AttendenceDialog");*/
            if (section_name == null || section_name.equals("")) {
                TastyToast.makeText(getBaseContext(),"Choose a Section",Toast.LENGTH_SHORT,TastyToast.WARNING).show();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Submit Attendence")
                        .setMessage("Once updated cant be changed directly, confirm post")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("post", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                for (int i = 0; i < studentCount; i++) {
                                    customCheckBoxes.get(i).setVisibility(View.GONE);
                                    rollnoText.get(i).setVisibility(View.GONE);
                                }
                                new SumbmitAttendence().execute();
                            }
                        })
                        .setNegativeButton("cancel", null).show();
            }
        }
        return true;
    }


    //handling attendence dialog
    @Override
    public void clicked(String s) {
        if(s.equals("confirm")){new SumbmitAttendence().execute();}
        cf.dismiss();
    }


    class getHandlingSectionsList extends AsyncTask<Void,Void,ArrayList<String> >{
        ProgressDialog pd=new ProgressDialog(context);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            chooseSection.setVisibility(View.INVISIBLE);

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
            if(HandlingSectionsList.get(0).equals("catch")){

            }
            // convert to simple array
           else{ popUpContents = new String[HandlingSectionsList.size()];
            HandlingSectionsList.toArray(popUpContents);
            popupWindowSections=popupWindowSections();
            chooseSection.setVisibility(View.VISIBLE);}
            subjectsLoader.hide();
        }
    }

    class AttendenceRegisterViewer extends AsyncTask<Void,Void,Void>{
        List<String> rollno=new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingIndicatorView.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{URL url=new URL("http://fullstackdevelopment.thecompletewebhosting.com/college_assistant/get_student_list.php");
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
                    rollno.add(stringTokenizer.nextToken());
                }

            }
            catch (Exception e){rollno.add("catch");}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            studentCount=rollno.size();
            for(int i=0;i<rollno.size();i++){
                rollnoText.get(i).setText(rollno.get(i));
                customCheckBoxes.get(i).setTag(rollno.get(i));
            }
            for (int i=0;i<rollno.size();i++){
                customCheckBoxes.get(i).setVisibility(View.VISIBLE);
                rollnoText.get(i).setVisibility(View.VISIBLE);
            }
            for(int i=30;i>(rollno.size()-1);i--){
                customCheckBoxes.get(i).setVisibility(View.GONE);
                rollnoText.get(i).setVisibility(View.GONE);
            }

            loadingIndicatorView.hide();
        }
    }

    class SumbmitAttendence extends AsyncTask<Void,Void,String>{
        ProgressDialog pd=new ProgressDialog(context);
        URL url;
        HttpURLConnection conn;
        String error;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            submissionLoader.show();
            loading.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {
            for(int i=0;i<studentCount;i++){
                String num=customCheckBoxes.get(i).getTag().toString();
                String isPresent;
                if(customCheckBoxes.get(i).isChecked()){isPresent=subject+"_absent";}
                else{isPresent=subject+"_present";}

                int READ_TIMEOUT=10000;
            int READ_CONNECTION=10000;
            try{
                url=new URL("http://fullstackdevelopment.thecompletewebhosting.com/college_assistant/mark_attendance.php");
                conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(READ_CONNECTION);
                conn.connect();
                OutputStream os=conn.getOutputStream();
                BufferedWriter br=new BufferedWriter(new OutputStreamWriter(os));
                br.write(URLEncoder.encode("roll_no","UTF-8")+"="+URLEncoder.encode(num,"UTF-8")+"&"+URLEncoder.encode("ispresent","UTF-8")+"="+URLEncoder.encode(isPresent,"UTF-8"));
                br.flush();
                br.close();
                os.close();
                //take
                InputStream is=conn.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
                int toIncrement;
                toIncrement=Integer.parseInt(bufferedReader.readLine());
                toIncrement++;
                error=Integer.toString(toIncrement);
                new updateEachStudentAttendence().execute(num,isPresent,Integer.toString(toIncrement),Integer.toString(i+1));
               /* //give back
                url=new URL("http://unrestrainable-fore.000webhostapp.com/college_assistant/inc_attendance.php");
                conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(READ_CONNECTION);
                conn.connect();
                br.write(URLEncoder.encode("roll_no","UTF-8")+"="+URLEncoder.encode(num,"UTF-8")+"&"+URLEncoder.encode("is_present","UTF-8")+"="+URLEncoder.encode(isPresent,"UTF-8")+"&"+URLEncoder.encode("increment","UTF-8")+"="+URLEncoder.encode(Integer.toString(toIncrement),"UTF-8"));*/


            } catch (Exception e) {
                e.printStackTrace();
            }


            }
            return "succesfull";
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);


                TastyToast.makeText(getBaseContext(),"Succesful",Toast.LENGTH_SHORT,TastyToast.SUCCESS).show();
                section_name=null;
                submissionLoader.hide();
                loading.setVisibility(View.GONE);
                //unchecking boxes
                for(int i=0;i<31;i++){
                    if(customCheckBoxes.get(i).isChecked()){
                        customCheckBoxes.get(i).toggle();
                    }
                }

        }
    }

    class updateEachStudentAttendence extends AsyncTask<String,String,String>{

        URL url;
        HttpURLConnection conn;
        ProgressDialog pd;
        String isPresent;
        String rollno;
        int nowSubmitted;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... strings) {
           int READ_TIMEOUT=10000;
            int READ_CONNECTION=10000;
            isPresent=strings[1];
            rollno=strings[0];
            String toIncrement=strings[2];
            nowSubmitted=Integer.parseInt(strings[3]);

            String num;
            //assigning url
            try{url=new URL("http://fullstackdevelopment.thecompletewebhosting.com/college_assistant/inc_attendance.php");}
            catch (Exception e){}
            //setting connection
            try{
                conn=(HttpURLConnection)url.openConnection();
            }
            catch (Exception e){}

            try{
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(READ_CONNECTION);
                conn.connect();
                OutputStream os=conn.getOutputStream();
                BufferedWriter br=new BufferedWriter(new OutputStreamWriter(os));
                br.write(URLEncoder.encode("roll_no","UTF-8")+"="+URLEncoder.encode(rollno,"UTF-8")+"&"+URLEncoder.encode("is_present","UTF-8")+"="+URLEncoder.encode(isPresent,"UTF-8")+"&"+URLEncoder.encode("increment","UTF-8")+"="+URLEncoder.encode(toIncrement,"UTF-8"));
                br.flush();
                br.close();
                os.close();
                InputStream is=conn.getInputStream();
                //BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
                //toIncrement=bufferedReader.readLine();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return toIncrement;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }


    private ArrayAdapter<String> sectionAdapter(final String sectionArray[]){
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, sectionArray){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                TextView listItem = new TextView(AttendenceRegister.this);

                listItem.setText(sectionArray[position]);
                listItem.setTag(sectionArray[position]);
                listItem.setTextSize(22);
                listItem.setPadding(10, 10, 10, 10);
                listItem.setTextColor(Color.WHITE);

                return listItem;
            }
        };
        return adapter;
    }


}
