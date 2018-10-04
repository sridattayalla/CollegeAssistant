package com.stepCone.sridatta.CollegeAssistant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

public class FacultySignIn extends Fragment {
    Activity context;
    TextView userid;
    TextView userpassword;
    TextInputLayout passwordLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.faculty_sign_in,container,false);
        context=getActivity();
        return v;
    }

    //open otp activity
    void OpenOtpActivity(){
        Intent intent=new Intent(context,OtpActivity.class);
        startActivity(intent);
    }


    //overriding on onstart method


    @Override
    public void onStart() {
        super.onStart();
        Button b=context.findViewById(R.id.faculty_login);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!checkInternetConnection())
                {
                    TastyToast.makeText(context,"check internet connection",Toast.LENGTH_LONG, TastyToast.WARNING).show();
                }
                else if(validated()){

                    TextView userid=context.findViewById(R.id.faculty_id);
                    String id=userid.getText().toString();
                    TextView userpassword=context.findViewById(R.id.etfaculty_password);
                    String password=userpassword.getText().toString();
                    new FacultySignIn.CheckFacultyDetails().execute(id,password);
                }


            }
        });

        //forgot password
        TextView forgotPassword=context.findViewById(R.id.faculty_forget_password);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenOtpActivity();
            }
        });
    }

    //method to open faculty home ativity
    void openFacultyActivity(){
        Intent intent=new Intent(context,FacultyHome.class);
        TextView userid=context.findViewById(R.id.faculty_id);
        intent.putExtra("perId",userid.getText().toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    //method to show details wrong
    void showError(){
        EditText password=context.findViewById(R.id.faculty_id);
        password.setError("Incorrect Id or Password",context.getResources().getDrawable(R.drawable.ic_error));
    }

    static FacultySignIn FacultySignIn(){
        return new FacultySignIn();
    }


    //async task for login and otp activity

    class CheckFacultyDetails extends AsyncTask<String,String,String> {
        URL url;
        HttpURLConnection conn;
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            pd=new ProgressDialog(getContext());
            pd.show();
            pd.setCancelable(false);
            pd.setMessage("loading");
        }

        @Override
        protected String doInBackground(String... strings) {
            int READ_TIMEOUT=10000;
            int READ_CONNECTION=10000;
            String Id=strings[0];
            String pass=strings[1];
            //writing url
            try{url=new URL("http://fullstackdevelopment.thecompletewebhosting.com/college_assistant/faculty_login.php");}
            catch (Exception e){}
            //setting connection
            try{
                conn=(HttpURLConnection)url.openConnection();
            }
            catch (Exception e){}

            try{conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(READ_CONNECTION);
                conn.connect();
                OutputStream os=conn.getOutputStream();
                BufferedWriter br=new BufferedWriter(new OutputStreamWriter(os));
                br.write(URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(Id,"UTF-8")+"&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(pass,"UTF-8"));
                br.flush();
                br.close();
                os.close();
                //input stream
                InputStream is=conn.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
                if(bufferedReader.readLine().equalsIgnoreCase("Successful")){
                    return "succefully login";
                }
                else{return "Unsuccesfull";}
            }
            catch (Exception e){
                e.printStackTrace();
                return "connection problem";
            }

            finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if(s.equalsIgnoreCase("succefully login")){
               // TastyToast.makeText(getContext(),s,Toast.LENGTH_LONG,TastyToast.SUCCESS).show();
                openFacultyActivity();}
            else if(s.equalsIgnoreCase("Unsuccesfull")) {
                TastyToast.makeText(getContext(),s,Toast.LENGTH_SHORT,TastyToast.WARNING).show();
                showError();
            }
            pd.dismiss();
        }
    }

    //validate
    public boolean validated() {
        String error;
        EditText emp_id,emp_password;
        emp_id = (EditText)context.findViewById(R.id.faculty_id);
        emp_password = (EditText)context.findViewById(R.id.etfaculty_password);
        String id = emp_id.getText().toString();
        String password = emp_password.getText().toString();
        Validation validate=new Validation();
        if(!(error=validate.idValidation(id)).equals("true")){
            emp_id.setError(error);
            return false;
        }
       /* if(!(error=validate.passwordValidation(password)).equals("true")){
            emp_id.setError(error);
            return false;
        }*/
        return true;
    }

    // checking internet connection

    public  boolean checkInternetConnection(){
        ConnectivityManager cm =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=cm.getActiveNetworkInfo();
        return info!=null && info.isConnectedOrConnecting();
    }
}

