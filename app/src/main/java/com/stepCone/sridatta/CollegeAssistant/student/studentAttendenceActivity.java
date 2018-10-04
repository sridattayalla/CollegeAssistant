package com.stepCone.sridatta.CollegeAssistant.student;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.stepCone.sridatta.CollegeAssistant.R;
import com.stepCone.sridatta.CollegeAssistant.faculty.AttendenceRegister;
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

/**
 * Created by HP-PC on 10-01-2018.
 */

public class studentAttendenceActivity extends Fragment {
    TextView subject,present,absent,percentagePresent;
    student_attedenceInterface bh;
    ImageView no_internet_image;
    Button retry;
    AVLoadingIndicatorView loadingIndicator;
    PieChart pc;
    Activity context;
    List<TextView> subjects;
    List<TextView> presentList;
    List<TextView> AbsentList;
    List<TextView> PercentageList;
    List<String> pList;
    List<String> aList;
    String jntuno;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //activity
        context=getActivity();
        //changing toolbar name
        Toolbar toolbar = (Toolbar) context.findViewById(R.id.student_home_tool_bar);
        toolbar.setTitle("Attendence");
        View v=inflater.inflate(R.layout.student_attendence_layout,container,false);
        //loading
        loadingIndicator=v.findViewById(R.id.student_attendence_loader);
        loadingIndicator.show();
        //internet connection
        no_internet_image=v.findViewById(R.id.student_attendence_no_internet_image);
        retry=v.findViewById(R.id.student_attendence_retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bh.attendencerefresh("attendence");
            }
        });
        //getting jntuno
        SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(getActivity());
        jntuno=sf.getString("id",null);
        //pi chart
        pc=v.findViewById(R.id.attendence_pi_chart);
        pc.setRotationEnabled(true);
        pc.setHoleRadius(60f);
        pc.setTransparentCircleAlpha(20);
        pc.setCenterTextColor(getResources().getColor(R.color.colorPrimary));
        pc.setTransparentCircleColor(getResources().getColor(R.color.cardview_shadow_start_color));
        pc.setDrawEntryLabels(false);
        pc.setContentDescription("Attendence piChart");
        //getting textview instances
        subject=v.findViewById(R.id.subject);
        subjects=new ArrayList<>();
        subjects.add((TextView)v.findViewById(R.id.subject_one));
        subjects.add((TextView)v.findViewById(R.id.subject_two));
        subjects.add((TextView)v.findViewById(R.id.subject_three));
        subjects.add((TextView)v.findViewById(R.id.subject_four));
        subjects.add((TextView)v.findViewById(R.id.subject_five));
        subjects.add((TextView)v.findViewById(R.id.subject_six));
        present=v.findViewById(R.id.present);
        presentList=new ArrayList<TextView>();
        presentList.add((TextView)v.findViewById(R.id.present_one));
        presentList.add((TextView)v.findViewById(R.id.present_two));
        presentList.add((TextView)v.findViewById(R.id.present_three));
        presentList.add((TextView)v.findViewById(R.id.present_four));
        presentList.add((TextView)v.findViewById(R.id.present_five));
        presentList.add((TextView)v.findViewById(R.id.present_six));
        absent=v.findViewById(R.id.absent);
        AbsentList=new ArrayList<TextView>();
        AbsentList.add((TextView)v.findViewById(R.id.absent_one));
        AbsentList.add((TextView)v.findViewById(R.id.absent_two));
        AbsentList.add((TextView)v.findViewById(R.id.absent_three));
        AbsentList.add((TextView)v.findViewById(R.id.absent_four));
        AbsentList.add((TextView)v.findViewById(R.id.absent_five));
        AbsentList.add((TextView)v.findViewById(R.id.absent_six));
        percentagePresent=v.findViewById(R.id.attendence_percentage);
        PercentageList=new ArrayList<TextView>();
        PercentageList.add((TextView)v.findViewById(R.id.attendence_percentage_one));
        PercentageList.add((TextView)v.findViewById(R.id.attendence_percentage_two));
        PercentageList.add((TextView)v.findViewById(R.id.attendence_percentage_three));
        PercentageList.add((TextView)v.findViewById(R.id.attendence_percentage_four));
        PercentageList.add((TextView)v.findViewById(R.id.attendence_percentage_five));
        PercentageList.add((TextView)v.findViewById(R.id.attendence_percentage_six));

        pList=new ArrayList<>();
        aList=new ArrayList<>();
        if(!checkInternetConnection()){
            no_internet_image.setVisibility(View.VISIBLE);
            retry.setVisibility(View.VISIBLE);loadingIndicator.hide();
            pc.setVisibility(View.GONE);
            subject.setVisibility(View.GONE);
            present.setVisibility(View.GONE);
            absent.setVisibility(View.GONE);
            percentagePresent.setVisibility(View.GONE);
            for (int i=0;i<6;i++){
                subjects.get(i).setVisibility(View.GONE);
                AbsentList.get(i).setVisibility(View.GONE);
                PercentageList.get(i).setVisibility(View.GONE);
            }
        }
        else{new getAttendence().execute();}
        return v;
    }

    class getAttendence extends AsyncTask<String,String,String>{
        float attended,absented;
        URL url;
        HttpURLConnection conn;
        int READ_TIMEOUT=10000;
        int READ_CONNECTION=10000;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try{url=new URL("http://fullstackdevelopment.thecompletewebhosting.com/college_assistant/retrieve_attendance.php");
                conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(READ_CONNECTION);
                conn.connect();
                OutputStream os=conn.getOutputStream();
                BufferedWriter br=new BufferedWriter(new OutputStreamWriter(os));
                br.write(URLEncoder.encode("roll_no","UTF-8")+"="+URLEncoder.encode(jntuno,"UTF-8"));
                br.flush();
                br.close();
                os.close();
                //take
                InputStream is=conn.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
                String s=bufferedReader.readLine();
                String mydelim = ",";
                StringTokenizer stringTokenizer =
                        new StringTokenizer(s, mydelim);
                int count = stringTokenizer.countTokens();

                for (int i = 0; i <count; i++){
                    if(i%2==0){aList.add(stringTokenizer.nextToken());}
                    else{pList.add(stringTokenizer.nextToken());}
                }
                for(int i=0;i<count/2;i++){
                    attended=attended+Integer.parseInt(aList.get(i).toString());
                    absented=absented+Integer.parseInt(pList.get(i).toString());
                }
            }
            catch (Exception e){}
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Toast.makeText(context,Integer.toString((int)attended),Toast.LENGTH_LONG).show();
            //entering data
            ArrayList<PieEntry> entries=new ArrayList<>();
            entries.add(new PieEntry(attended,1));
            entries.add(new PieEntry(absented,2));

            PieDataSet dataset=new PieDataSet(entries,"present");
            dataset.setSliceSpace(5);
            dataset.setValueTextSize(20);

            //adding colors
            ArrayList<Integer> colors=new ArrayList<>();
            colors.add(Color.parseColor("#008080"));
            colors.add(Color.parseColor("#FF7373"));
            dataset.setColors(colors);

            //getting legend
            Legend legend=pc.getLegend();
            legend.setForm(Legend.LegendForm.CIRCLE);
            legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

            //creating pi data
            PieData pieData=new PieData(dataset);
            pc.setData(pieData);
            pc.invalidate();

            //setting center text
            pc.setCenterTextColor(Color.BLUE);
            pc.setCenterTextSize(50);
            float AttendencePercentage=attended/(attended+absented)*100;
            String percentage=Integer.toString((int)AttendencePercentage)+"%";
            pc.setCenterText(percentage);

            //setting text values
            for(int i=0;i<aList.size();i++){
                presentList.get(i).setText(aList.get(i).toString());
                AbsentList.get(i).setText(pList.get(i).toString());
                int percent;
                try {
                    percent=(Integer.parseInt(aList.get(i).toString()))*100/((Integer.parseInt(aList.get(i).toString()))+(Integer.parseInt(pList.get(i).toString())));
                }
                catch (ArithmeticException e){percent=0;}
                PercentageList.get(i).setText(Integer.toString(percent));
            }

            //hiding loading
            loadingIndicator.hide();
            pc.setVisibility(View.VISIBLE);
            subject.setVisibility(View.VISIBLE);
            present.setVisibility(View.VISIBLE);
            absent.setVisibility(View.VISIBLE);
            percentagePresent.setVisibility(View.VISIBLE);
            for(int i=0;i<6;i++){
                AbsentList.get(i).setVisibility(View.VISIBLE);
                presentList.get(i).setVisibility(View.VISIBLE);
                subjects.get(i).setVisibility(View.VISIBLE);
                PercentageList.get(i).setVisibility(View.VISIBLE);
            }
        }
    }

    // checking internet connection

    public  boolean checkInternetConnection(){
        ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=cm.getActiveNetworkInfo();
        return info!=null && info.isConnectedOrConnecting();
    }

    public interface student_attedenceInterface{
        void attendencerefresh(String s);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        bh=(student_attedenceInterface) activity;
    }
}
