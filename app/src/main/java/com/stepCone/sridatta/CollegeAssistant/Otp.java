package com.stepCone.sridatta.CollegeAssistant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sdsmdg.tastytoast.TastyToast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by HP-PC on 31-12-2017.
 */

public class Otp extends Fragment {
    CountDownTimer ct;
    TextView time;
    Button b;
    final long startTime=120000;
    long leftTime=startTime;
    Activity context;
    String mobileNum;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context=getActivity();
        final View v=inflater.inflate(R.layout.otp_layout,container,false);

//        //setting visibilities
//        TextView resendOtp=v.findViewById(R.id.resend_otp);
//        resendOtp.setVisibility(View.GONE);
//        time=v.findViewById(R.id.time);
//        time.setVisibility(View.VISIBLE);
//        b=v.findViewById(R.id.submit);
//        b.setVisibility(View.VISIBLE);


       /* ct=new CountDownTimer(leftTime,1000) {
            @Override
            public void onTick(long l) {
                leftTime=l;
                int minutes=(int)(leftTime/1000/60);
                int seconds=(int)(leftTime/1000)%60;
                String timeFormatted=String.format("%02d:%02d",minutes,seconds);
                time=v.findViewById(R.id.time);
                time.setText(timeFormatted);

            }

            @Override
            public void onFinish() {
//                //setting visibilities
//                TextView ResendOtp=v.findViewById(R.id.resend_otp);
//                ResendOtp.setVisibility(View.VISIBLE);
//                time=v.findViewById(R.id.time);
//                time.setText("00:00");
//                b=v.findViewById(R.id.submit);
//                b.setVisibility(View.GONE);
            }
        }.start();*/

        //on resend click

        //return
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        final EditText mobile=context.findViewById(R.id.editText3);
        Button submit=context.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobileNum=mobile.getText().toString();
                if(!checkInternetConnection()){
                    TastyToast.makeText(getActivity(),"Check Internet Connection",Toast.LENGTH_SHORT,TastyToast.CONFUSING).show();}
                else{new sendPassword().execute();}
            }
        });
    }

    class sendPassword extends AsyncTask<String,String,String> {
        ProgressDialog pd=new ProgressDialog(context);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setCancelable(false);
            pd.setMessage("loading");
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try{URL url=new URL("http://fullstackdevelopment.thecompletewebhosting.com/college_assistant/forgot_password.php");
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(10000);
                conn.connect();
                OutputStream os=conn.getOutputStream();
                BufferedWriter br=new BufferedWriter(new OutputStreamWriter(os));
                br.write(URLEncoder.encode("roll_no","UTF-8")+"="+URLEncoder.encode(mobileNum,"UTF-8"));
                br.flush();
                br.close();
                os.close();
                //getting image
                InputStream is=conn.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
                String result=bufferedReader.readLine();
                if(result.equalsIgnoreCase("found")){return "1";}
                else {return "0";}
            }
            catch (Exception e){return "0";}

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s.equalsIgnoreCase("1")){
                pd.dismiss();
                TastyToast.makeText(context,"Check your mobile for Otp",Toast.LENGTH_SHORT,TastyToast.SUCCESS).show();
                Intent intent=new Intent(context,ChooserPage.class);
                startActivity(intent);}
            else {
                pd.dismiss();
                TastyToast.makeText(context,"Incorrect Mobile Number",Toast.LENGTH_SHORT,TastyToast.WARNING).show();}
        }
    }

    public  boolean checkInternetConnection(){
        ConnectivityManager cm =(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=cm.getActiveNetworkInfo();
        return info!=null && info.isConnectedOrConnecting();
    }
}
