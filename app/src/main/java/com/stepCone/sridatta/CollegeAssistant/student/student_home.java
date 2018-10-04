package com.stepCone.sridatta.CollegeAssistant.student;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.utils.L;
import com.stepCone.sridatta.CollegeAssistant.R;
import com.stepCone.sridatta.CollegeAssistant.imageContainer;
import com.stepCone.sridatta.CollegeAssistant.linkContainer;
import com.stepCone.sridatta.CollegeAssistant.student.student_home_elements.studentRecycleViewAdapter;
import com.wang.avi.AVLoadingIndicatorView;

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
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

/**
 * Created by HP-PC on 09-01-2018.
 */

public class student_home extends Fragment {
    student_homeInterface bh;
    ImageView no_internet_image;
    Button retry;
    AVLoadingIndicatorView loadingIndicator;
    RecyclerView recyclerView;
    Activity context;
    View v;
    List<Object> catalogue=new ArrayList<>();
    String count;
    studentRecycleViewAdapter adapter;
    Bitmap facultyImage;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        context=getActivity();
        //changing toolbar name
        Toolbar toolbar = (Toolbar) context.findViewById(R.id.student_home_tool_bar);
        toolbar.setTitle("College Assistant");

        v=inflater.inflate(R.layout.student_home,container,false);
        //loading
        loadingIndicator=v.findViewById(R.id.student_home_loader);
        loadingIndicator.show();
        //internet connection
        no_internet_image=v.findViewById(R.id.student_home_no_internet_image);
        retry=v.findViewById(R.id.student_home_retry);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               bh.refresh();
            }
        });
        recyclerView=(RecyclerView)v.findViewById(R.id.feed_recycle_view);
        catalogue.add(new imageContainer(getResources().getDrawable(R.drawable.rccar),"Register Now!","Get Ready For The Race"));
        adapter=new studentRecycleViewAdapter(getActivity(),catalogue);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //checking connection
        if(!checkInternetConnection()){no_internet_image.setVisibility(View.VISIBLE); retry.setVisibility(View.VISIBLE);loadingIndicator.hide();}
        else{new getCount().execute();}
        return v;
    }

    class getCount extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {
            try{URL url=new URL("http://fullstackdevelopment.thecompletewebhosting.com/college_assistant/get_posts.php");
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(10000);
                conn.connect();
                //getting image
                InputStream is=conn.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
                count=bufferedReader.readLine();
            return count;}
            catch (Exception e){}
            return "0";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
            for(int i=Integer.parseInt(s);i>0;i--){new getRecycleView().execute(Integer.toString(i));}}
            catch (Exception e){
                no_internet_image.setVisibility(View.VISIBLE); retry.setVisibility(View.VISIBLE);loadingIndicator.hide();
            }
        }
    }

    class getRecycleView extends AsyncTask<String,String,String>{
        Bitmap bitmap;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
           String num=strings[0];
            try{URL url=new URL("http://fullstackdevelopment.thecompletewebhosting.com/college_assistant/posts.php");
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(10000);
                conn.connect();
                OutputStream os=conn.getOutputStream();
                BufferedWriter br=new BufferedWriter(new OutputStreamWriter(os));
                br.write(URLEncoder.encode("roll_no","UTF-8")+"="+URLEncoder.encode(num,"UTF-8"));
                br.flush();
                br.close();
                os.close();
                //getting image
                InputStream is=conn.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
                String receivedString=bufferedReader.readLine();
                new getFacultyImage().execute(receivedString);

              return receivedString;
            }
            catch (Exception e){}
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

    class getFacultyImage extends AsyncTask<String,String,Bitmap>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            String receivedString=strings[0];
            String mydelim = "~";
            StringTokenizer stringTokenizer =
                    new StringTokenizer(receivedString, mydelim);
            String facultyId=stringTokenizer.nextToken();
            String facultyName=stringTokenizer.nextToken();
            String link=stringTokenizer.nextToken();
            String description=stringTokenizer.nextToken();
            String date=stringTokenizer.nextToken();
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
                br.write(URLEncoder.encode("roll_no","UTF-8")+"="+URLEncoder.encode(facultyId,"UTF-8"));
                br.flush();
                br.close();
                os.close();
                //getting image
                InputStream is=conn.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
                Bitmap bitmap= BitmapFactory.decodeStream(is);

                //getting bitmap

                catalogue.add(catalogue.size(),new linkContainer(bitmap,link,description,facultyName,date));
                return bitmap;
                //while (sub!=null){HandlingSectionsList.add(s); sub=bufferedReader.readLine();}
            }
            catch (Exception e){Toast.makeText(getActivity(),"error",Toast.LENGTH_LONG).show();}
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            adapter.notifyItemInserted(catalogue.size()-1);
            loadingIndicator.hide();
        }
    }

    public  boolean checkInternetConnection(){
        ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=cm.getActiveNetworkInfo();
        return info!=null && info.isConnectedOrConnecting();
    }

    public interface student_homeInterface{
        void refresh();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        bh=(student_homeInterface) activity;
    }
}
